
package diet.server;

import java.io.Serializable;
import java.util.Date;
import java.util.Random;
import java.util.Vector;

import diet.message.Message;
import diet.message.MessageWYSIWYGDocumentSyncFromClientInsert;
import diet.message.MessageWYSIWYGDocumentSyncFromClientRemove;
import diet.server.CbyC.DocChange;
import diet.server.CbyC.DocInsert;
import diet.server.CbyC.DocRemove;

/**
 * This class is still under construction. It was originally developed for a demo, and needs
 * radical overhaul. If attempting to write a WYSIWYG intervention, it is advisable to look at
 * ConversationControllerINTERCEPTUHH and copy the relevant fragments. This class is only used by WYSIWYG
 * implementations.
 * 
 * @author user
 */
public class DocChangesOutgoingSequenceFIFO implements Serializable {
    

    private Participant p;
    private StringBuilder sequence = new StringBuilder();
    //private StringOfDocChangeInserts sodci = new StringOfDocChangeInserts();
    private Vector allInsertsAndRemoves = new Vector();
    private long startTime;
    
    //private int charsToDelayBy = 5;
    private int pointerToNextDocumentChange =0;
    private long maximumDelay = 3000;
    private long timeOfLastSending;
    
    private boolean relayEnabled=true;
    private long timeOfParsingAttempt=0;
    private int indexOfLastChange =0;
    
    private boolean hasPerformedArtificialSubstitution = false;
    
    private int numberOfSubstitutionsPerformed =0;
    private long trickleNumberOfGenuineUserChanges =0;
    private long trickleNumberOfAdditionalInsertedChanges = 0;
    private long trickleTimeOfLastGenuineUserChange;
    
    private int trickleMaxBufferSize =10;
    
    private Random r = new Random();
   
    public DocChangesOutgoingSequenceFIFO(Participant p,int maxTrickleSize,long startTime) {
       this.p=p;
       this.startTime = startTime;
       this.trickleMaxBufferSize=maxTrickleSize;
    }
    
    
    
    
     public synchronized void addMessage(Participant p, Message m){
       this.trickleTimeOfLastGenuineUserChange=new Date().getTime();
       trickleNumberOfGenuineUserChanges++;  
       System.out.print("SERVER FIFO QUEUE IS RECEIVING ");  
       if (m instanceof MessageWYSIWYGDocumentSyncFromClientInsert){
           MessageWYSIWYGDocumentSyncFromClientInsert mWYSIWYG = (MessageWYSIWYGDocumentSyncFromClientInsert)m;
           insert(mWYSIWYG.getTextToAppendToWindow(),mWYSIWYG.getOffset(),mWYSIWYG.getTimeStamp().getTime());  
           System.out.println("SERVER: INSERTING MESSAGE FROM "+p.getUsername()+": "+mWYSIWYG.getTextToAppendToWindow());
       } 
       else if(m instanceof MessageWYSIWYGDocumentSyncFromClientRemove){
           MessageWYSIWYGDocumentSyncFromClientRemove mWYSIWYG = (MessageWYSIWYGDocumentSyncFromClientRemove)m;
           remove(mWYSIWYG.getOffset(),mWYSIWYG.getLength(),mWYSIWYG.getTimeStamp().getTime());  
       }
       //if(!queues.containsKey(p))queues.put(p,)    
    }
    
    
    
    
    private void insert(String s,int offsetFrmRight,long timestamp){
        try{
          int offsetFrmLeft = sequence.length()-offsetFrmRight;
          if(offsetFrmLeft<0)offsetFrmLeft=0;
          if(offsetFrmLeft>sequence.length())offsetFrmLeft=sequence.length();       
          //System.err.println("SERVER: DocChagesOutgoing--internal state is: "+this.getParsedText()+"---------INSERTING "+s+" with length "+s.length());
          sequence.insert(offsetFrmLeft,s);
          //System.err.println("-----------SHOULD BE SAME LENGTH AS "+sequence.length());
        }catch(Exception e){
            System.err.println("Error in DocumentChangeSequenceOffsetFromRight Inserting "+s+" offsetFrmRight: "+offsetFrmRight);
        }  
          DocInsert insert = new DocInsert(offsetFrmRight,s,null);
          insert.setTimestamp(timestamp);
          
          allInsertsAndRemoves.addElement(insert);
          //sodci.insert(insert);
    }
   
    private void remove(int offsetFrmRight,int length, long timestamp){
        DocRemove remove = new DocRemove(offsetFrmRight,length);
        remove.setTimestamp(timestamp);
        
        try{
          int offsetFrmLeft = sequence.length()-remove.getOffs();
          if(offsetFrmLeft<0)offsetFrmLeft=0;
          if(offsetFrmLeft>sequence.length())offsetFrmLeft=sequence.length();
          int len = remove.getLen();
          if(offsetFrmLeft+len>sequence.length())len = sequence.length()-offsetFrmLeft;  
          //sequence.replace(sequence.length()-offsetFrmRight,sequence.length()-offsetFrmRight+length,"");
          sequence.replace(offsetFrmLeft,offsetFrmLeft+remove.getLen(),"");
          allInsertsAndRemoves.addElement(remove);
          //sodci.remove(remove);
        }catch (Exception e){
            System.err.println("Error in DocumentChangeSequenceOffsetFromRight Removing offsetFrmRight2: "+remove.getOffs()+" "+remove.getLen());
        }   
           
    }
    public Participant getParticipant(){
        return p;
    }    
    public boolean hasSentAllMessages(){
        return (pointerToNextDocumentChange>=allInsertsAndRemoves.size());
    }
    public long getStartTime(){
        return this.startTime;
    }
    public long getFinishTime(){
        DocChange dc = (DocChange)allInsertsAndRemoves.elementAt(allInsertsAndRemoves.size()-1);
        return dc.getTimestamp();
    }
    public Vector getAllInsertsAndRemoves(){
        return allInsertsAndRemoves;
    }
    public boolean hasBeenChangedArtificially(){
        return this.hasPerformedArtificialSubstitution;
    }
    
    public synchronized StringOfDocChangeInserts getStringOfDocChangeInserts(){
        
        this.timeOfParsingAttempt = new Date().getTime();
        try{
           StringOfDocChangeInserts sodci = new StringOfDocChangeInserts(allInsertsAndRemoves);
           return sodci;        
        }catch(Exception e){
            System.err.println("Cannot get buffered text");
            return null;
        }
    }
    public synchronized int getFirstIndexForChanges(){
         return Math.max(pointerToNextDocumentChange,indexOfLastChange);
    }
    
    

    
    
    public boolean checkSequenceIsContinuousAndNotAlreadySentOrAlreadyChanged(Vector v){
        String s = new StringOfDocChangeInserts(v).getString();
        
        try{
        if(v==null)return false;
        System.out.print("CHECKING1: "+s+" ");
        if(v.size()==0)return false;
        System.out.print("CHECKING2: "+s+" ");
        if(!allInsertsAndRemoves.containsAll(v))return false;
        System.out.print("CHECKING3: "+s+" ");
        int idx = allInsertsAndRemoves.indexOf(v.elementAt(0));
        System.out.print("CHECKING4: "+s+" ");
        if(idx<Math.max(this.indexOfLastChange,this.pointerToNextDocumentChange))return false;
        System.out.print("CHECKING5: "+s+" ");
        for(int i=0;i<v.size();i++){
            System.out.print("CHECKING6: "+s+" ");
            if(!v.elementAt(i).equals(allInsertsAndRemoves.elementAt(i+idx)))return false;
        }
        System.out.println("CHECKING7 VALID TO BE CHANGED: : "+s+" ");
        return true;
        }catch (Exception e){
            
            return false;
        }
        
    }
    
    public synchronized boolean getLastCharacterIsSpaceOrNewLine(){
       
        boolean endOfStringsequenceIsWhitespace=false;
        boolean lastInsertTextIsWhitespace=false;
        try{ 
        String s = getStringOfDocChangeInserts().getString();   
        endOfStringsequenceIsWhitespace = Character.isWhitespace(s.charAt(s.length()-1));
        DocChange dc = (DocChange)allInsertsAndRemoves.elementAt(allInsertsAndRemoves.size()-1);
        if(dc instanceof DocRemove)return false;
        if(dc instanceof DocInsert){
            DocInsert di = (DocInsert)dc;
            lastInsertTextIsWhitespace = Character.isWhitespace(di.getStr().charAt(di.getStr().length()-1));     
       }
       }catch (Exception e){
           return false;
       } 
       return endOfStringsequenceIsWhitespace && lastInsertTextIsWhitespace;      
        
    }
    
    public synchronized void replaceSequenceWithSequenceChangingTimestampOfEnsuingSequenceUsingOldTurnAsBasisFortypingTime(Vector vToBeRemoved,Vector vToBeAdded){
        //Assumes that the vector to be removed is a continuous sequence of inserts
        
        //Give vToBeAdded the proper timestamps
        //Remove vToBeRemoved
        //Update the remainder Timestamps     
        if(vToBeRemoved.size()==0||vToBeAdded.size()==0)return;
        
        hasPerformedArtificialSubstitution =true;
        DocChange oldSequenceFromPositionOfChangeFirstElement = (DocChange) vToBeRemoved.elementAt(0);  
        int indexOfChange = allInsertsAndRemoves.indexOf(oldSequenceFromPositionOfChangeFirstElement);  
        Vector oldSequenceFromPositionOfChange = subVector(allInsertsAndRemoves,indexOfChange);
        
        
        
        
        allInsertsAndRemoves.removeAll(vToBeRemoved);   
        
        
        for(int i=0;i<vToBeAdded.size();i++){
            DocChange dc = (DocChange)vToBeAdded.elementAt(i);
            allInsertsAndRemoves.insertElementAt(dc,i+indexOfChange);
        }
        
        long previousTime = oldSequenceFromPositionOfChangeFirstElement.getTimestamp();
        for(int i=indexOfChange;i<allInsertsAndRemoves.size();i++){
             DocChange dc = (DocChange)allInsertsAndRemoves.elementAt(i); 
             if(i<oldSequenceFromPositionOfChange.size()){
                 DocChange dcOLD = (DocChange)oldSequenceFromPositionOfChange.elementAt(i);
                 dc.setTimestamp(dcOLD.getTimestamp());
                 previousTime = dcOLD.getTimestamp();
             }
             else{              
                 long newTime = previousTime+50+ ((long) r.nextGaussian()^2*500);
                 dc.setTimestamp(previousTime+500);
                 previousTime = newTime;                 
             }
        }        
        
        indexOfLastChange = indexOfChange+vToBeAdded.size();
        trickleNumberOfAdditionalInsertedChanges = trickleNumberOfAdditionalInsertedChanges + vToBeAdded.size()-vToBeRemoved.size();
        numberOfSubstitutionsPerformed ++;
    }    
       
    public synchronized void addInsertsAndDeletes(Vector v){
        try{
          for(int i=0;i<v.size();i++){
             this.allInsertsAndRemoves.addElement(v.elementAt(i));
          }
        }  
        catch (Exception e){
            
        }  
        numberOfSubstitutionsPerformed++;
    }
    
     public Vector  subVector(Vector vSource,int indexOfChange){
         Vector v = new Vector();
         for(int i=indexOfChange;i<vSource.size();i++){
             v.addElement(vSource.elementAt(i));
         }
         return v;
     }  
    
       public String displayInfo(){
           String info = "Full Queue: "+new StringOfDocChangeInserts(allInsertsAndRemoves).getString() + "\n" +
           "pointerToNextDocumentChange: "+this.pointerToNextDocumentChange +" \n";
            for(int i=0;i<allInsertsAndRemoves.size();i++){
               DocChange dc = (DocChange)allInsertsAndRemoves.elementAt(i);
               System.out.print(dc.getTimestamp());
               if(dc instanceof DocInsert){
                   System.out.println(((DocInsert)dc).getStr());
               }
               else{
                   System.out.println("");
               }
            }       
              
           
                   
           return info;
       }
     
       public void setRelayEnabled(boolean b){
           relayEnabled = b;
       }  
     
       public int numberOfSubstitutionsPerformed(){
           return numberOfSubstitutionsPerformed;
       }
       
       public void setMaxTrickleSize(int maxTrickleSize){
           trickleMaxBufferSize = maxTrickleSize;
       }
       public int getMaxTrickleSize(){
           return trickleMaxBufferSize;
       }
       
       private boolean letTrickle(){
           
          try{
           if(pointerToNextDocumentChange>=allInsertsAndRemoves.size()) return false;        
           int numberOfChangesLeftToDispatch = allInsertsAndRemoves.size()-pointerToNextDocumentChange;
             
           
           if(this.trickleTimeOfLastGenuineUserChange+1500 + r.nextInt(1500)< new Date().getTime()){
             Thread.sleep(10+r.nextInt(40)+r.nextInt(200));
               return true;
          }
           
           if(numberOfChangesLeftToDispatch>10){
               Thread.sleep(50+r.nextInt(280));
               return true;
           }  
           
           int bufferedcharacters = Math.min( (int)(trickleNumberOfGenuineUserChanges/2),trickleMaxBufferSize);
          
              
           if(pointerToNextDocumentChange+bufferedcharacters>=allInsertsAndRemoves.size()-this.trickleNumberOfAdditionalInsertedChanges)return false;
           return true;
           
           
           
           
          
          }catch(Exception e){
              return true;
          }
           
           //return false
       }
       
       
       
       public synchronized DocChange getNextDocumentChange(){
        
        if(!relayEnabled)return null;
        if(!letTrickle())return null;
        DocChange dc = (DocChange)allInsertsAndRemoves.elementAt(pointerToNextDocumentChange);
        //if(new Date().getTime()-dc.getTimestamp()>getMaximumDelay()){
        pointerToNextDocumentChange++;
        this.timeOfLastSending=new Date().getTime();
        //System.out.println("Flushing keypress----------------------------------------------------------------------------------------------------"+dc.getClass().toString());
        return dc;         
            
       
    }
     
}