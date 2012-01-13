/*
 * TypoGenerator.java
 *
 * Created on 24 October 2007, 00:27
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.textmanipulationmodules.TypoGenerator;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;

import diet.server.Conversation;
import diet.server.CbyC.DocInsert;
import diet.server.CbyC.DocRemove;
import diet.server.CbyC.Sequence.MazeGameBufferedTypoReplacement;
import diet.server.CbyC.Sequence.FlowControl.FlowControlFakeEditDelay;

 

/**
 * This is used to add noise to Strings and to sequences of DocChanges. It also includes a model of a qwerty
 * keyboard (which keys are close to each other) to generate more authentic typos.
 * @author user
 */
public class TypoGenerator {
    
    
    Hashtable typos = new Hashtable();
    Random r = new Random();
    
    /** Creates a new instance of TypoGenerator */
    public TypoGenerator() {
        typos.put("1","q2");
        typos.put("2","1qw3");
        typos.put("3","2we4");
        typos.put("4","3er5");
        typos.put("5","4rt6");
        typos.put("6","5ty7");
        typos.put("7","6yu8");
        typos.put("8","7ui9");
        typos.put("9","8io0");
        typos.put("q","12wa");
        typos.put("w","q23esa");
        typos.put("e","w34rds");
        typos.put("r","edft54");
        typos.put("t","rfgy65");
        typos.put("y","tghu76");
        typos.put("u","yhji87");
        typos.put("i","ujko98");
        typos.put("o","iklp09");
        typos.put("p","ol0");
        typos.put("a","zswq");
        typos.put("s","azxdew");
        typos.put("d","sxcfre");
        typos.put("f","dcvgtr");
        typos.put("g","fvbhyt");
        typos.put("h","gbnjuy");
        typos.put("j","hnmkiu");
        typos.put("k","jmloi");
        typos.put("l","kop");
        typos.put("z","asx");
        typos.put("x","zsdc");
        typos.put("c","xdfv");
        typos.put("v","cfgb");
        typos.put("b","vghn");
        typos.put("n","bhjm");
        typos.put("m","njk");
             
    }
    
    
    
     public Vector createTypoForFakeEdit(MazeGameBufferedTypoReplacement seq,String s,String apparentSender,String recipient,Object a,int delaybaseline,int delayvariance){
         int numberOfTypos =1;
         if(s.length()>2){
            numberOfTypos = r.nextInt(s.length()/2);
         }
         if(s.length()==1)numberOfTypos = 1;
         if(s.length()==2)numberOfTypos = 1;
         if(s.length()==3)numberOfTypos = 2+r.nextInt(1);
         if(s.length()==4)numberOfTypos = 2+r.nextInt(2);
         if(s.length()==5)numberOfTypos = 2+r.nextInt(2);
         if(s.length()==6)numberOfTypos = 2+r.nextInt(2);
         if(s.length()==7)numberOfTypos = 2+r.nextInt(3);
         if(s.length()==8)numberOfTypos = 3+r.nextInt(2);
         if(s.length()==9)numberOfTypos = 3+r.nextInt(2);
         if(s.length()==10)numberOfTypos = 3+r.nextInt(3);
         if(s.length()==11)numberOfTypos = 3+r.nextInt(3);
         if(s.length()>11)numberOfTypos = 3+r.nextInt(4);
         
         
         Vector indicesOfTypos = new Vector();
         while(indicesOfTypos.size()<numberOfTypos){
             int potentialTypo = new Integer(r.nextInt(s.length()));
             boolean indexExists = false;
             for(int i=0;i<indicesOfTypos.size();i++){
                 Integer indexlookedup = (Integer)indicesOfTypos.elementAt(i);
                 if(indexlookedup==potentialTypo) {
                     indexExists = true;
                     break;
                 }
             }
             if(!indexExists){
                 indicesOfTypos.addElement(potentialTypo);
                 Conversation.printWSln("Main", "Adding typo index "+potentialTypo);
             }
         }
         
         Vector docInserts = new Vector();
         
         for(int i=0;i<s.length();i++){
             boolean indexExists = false;
             for(int j=0;j<indicesOfTypos.size();j++){
                 Integer indexlookedup = (Integer)indicesOfTypos.elementAt(j);
                 if(indexlookedup==i) {
                     indexExists = true;
                     break;
                 }
             }
             if(indexExists){
                 int randm = r.nextInt(3);
                 if(randm==0){
                    DocInsert di = new DocInsert("server", apparentSender,recipient,0, ""+s.charAt(i),  a);
                    docInserts.add(di);
                    DocInsert di2 = new DocInsert("server", apparentSender,recipient,0, ""+getTypo(""+s.charAt(i)),  a);
                    docInserts.add(di2);
                    FlowControlFakeEditDelay fcd = new FlowControlFakeEditDelay(seq,delaybaseline+ r.nextInt(delayvariance));
                    docInserts.add(fcd);
                 }
                 else if(randm==1){
                    DocInsert di2 = new DocInsert("server", apparentSender,recipient,0, ""+getTypo(""+s.charAt(i)),  a);
                    docInserts.add(di2);
                    DocInsert di = new DocInsert("server", apparentSender,recipient,0, ""+s.charAt(i),  a);
                    docInserts.add(di);
                    FlowControlFakeEditDelay fcd = new FlowControlFakeEditDelay(seq,delaybaseline+ r.nextInt(delayvariance));
                    docInserts.add(fcd); 
                 }
                 else {
                    DocInsert di2 = new DocInsert("server", apparentSender,recipient,0, ""+getTypo(""+s.charAt(i)),  a);
                    docInserts.add(di2);
                    FlowControlFakeEditDelay fcd = new FlowControlFakeEditDelay(seq,delaybaseline+ r.nextInt(delayvariance));
                    docInserts.add(fcd); 
                 }
             }
             else{
                DocInsert di = new DocInsert("server", apparentSender,recipient,0, ""+s.charAt(i),  a);
                docInserts.add(di);
                FlowControlFakeEditDelay fcd = new FlowControlFakeEditDelay(seq,delaybaseline+ r.nextInt(delayvariance));
                docInserts.add(fcd);
             }
             
         }
         return docInserts;
         
     }
    
    static public String getString(Vector docInserts){
        String s = "";
        for(int i=0;i<docInserts.size();i++){
            Object o = (Object)docInserts.elementAt(i);
            if(o instanceof DocInsert){
                DocInsert di = (DocInsert)o;
                s = s+di.str;
            }
        }
        return s;
    }
    
    
    private String getTypo(String s){
        if(s.length()<1){
            Conversation.printWSln("TG", "String is zero ");
            return s;
        } 
        String s2 = (String)typos.get(s);
        if(s2==null){
            Conversation.printWSln("TG", "Can't find typo for: "+s);
            return s;
        }
        char s3 = s2.charAt(r.nextInt(s.length()));
        if( Character.isUpperCase((s.charAt(0)))){
            Conversation.printWSln("TG", "Found typo for: "+s+"...transformed to:"+s3);
            return ""+Character.toUpperCase(s3);
        }
        Conversation.printWSln("TG", "Found typo for: "+s+"...transformed to:"+s3);
        return ""+s3;
    }
    
    
    
    
    
    
    /**
     * This needs to be reimplemented as a collection of methods that only perform a single kind of manipulation.
     * @param s
     * @param numberOfBadReplacements
     * @param numberOfUncorrectedTypos
     * @param changesToLowerCase
     * @return
     */
    public String createTypoOfStringDeprecated(String s, int numberOfBadReplacements, int numberOfUncorrectedTypos, int changesToLowerCase){
        try{
        Vector vCandidates = new Vector();
        StringBuilder sResult = new StringBuilder();
        StringBuilder s2Result = new StringBuilder();
        //Build Vector of possible candidates
         for(int i=0;i<s.length();i++){
            String s2 = ""+s.charAt(i);
            s2Result.append(s2);
            boolean isUpperCase = isUpperCase(s2);
            if(typos.containsKey(s2.toUpperCase())||typos.containsKey(s2.toLowerCase())){
                if(changesToLowerCase>0&&isUpperCase&&r.nextInt(changesToLowerCase)==0){
                    sResult.append(s2.toLowerCase());
                }
                else{
                    String possCandidate = (String)typos.get(s2.toLowerCase());
                    if(numberOfUncorrectedTypos>0&&r.nextInt(numberOfUncorrectedTypos)==0){
                        String s3 = ""+possCandidate.charAt(r.nextInt(possCandidate.length()));
                        System.out.println("s31:"+s3);
                        sResult.append(s3);
                    }
                   else if(numberOfUncorrectedTypos>0&&r.nextInt(numberOfUncorrectedTypos)==0){
                         String s3= ""+possCandidate.charAt(r.nextInt(possCandidate.length()));
                         System.out.println("s32:"+s3+":s2:"+s2);
                         if(r.nextInt(2)==0){
                            sResult.append(s3+s2);
                         }
                         else{
                            sResult.append(s2+s3);
                         }
                   }  
                   else{sResult.append(s2); 
                        
                }                
                }
            } else{
                sResult.append(s2);
            
            }           
         }
            
        return sResult.toString();
        }catch(Exception e){
            System.out.println("ERROR");
            return s;
        } 
    }

    public boolean isUpperCase(String s){
        if(s.toUpperCase().equals(s))return true;
        return false;
    }
  
    /**
     * Creates a sequence of DocChanges which describes a String being constructed and then subsequently
     * deleted (see ConversationControllerINTERCEPTUHHREPLACE) for example of this.
     * @param textSequence
     * @param avgSpeed
     * @return
     */
    public Vector createInsertRemoveSequence(Object[] textSequence,long avgSpeed){
        Vector insertsAndRemoves = new Vector();
        long timeIndex = 0;
        for(int i=0;i<textSequence.length;i++){
            Object o = textSequence[i];
            
            if (o instanceof String){
                String s = (String)o;
                for(int j=0;j<s.length(); j++){
                    timeIndex= timeIndex+r.nextInt((int)avgSpeed*2)*1000;
                    DocInsert dII = new DocInsert(0,""+s.charAt(j),null);
                    dII.setTimestamp(timeIndex);
                    insertsAndRemoves.addElement(dII);
                }
            }
            else if(o instanceof Integer){
                    int offsetFrmRight = (Integer)o;
                    for(int j=0;j<offsetFrmRight;j++){
                    timeIndex= timeIndex+r.nextInt((int)avgSpeed*2)*1000;
                    DocRemove dRR = new DocRemove(1,1);
                    dRR.setTimestamp(timeIndex);
                    insertsAndRemoves.addElement(dRR);
                  }
            }
            else if (o instanceof Integer[]){
                   Integer[] offsetAndLength= (Integer[])o;
                   timeIndex= timeIndex+r.nextInt((int)avgSpeed*2)*1000;
                   DocRemove dRR = new DocRemove(offsetAndLength[0],offsetAndLength[1]);
                   dRR.setTimestamp(timeIndex);
                   insertsAndRemoves.addElement(dRR);
                   
            }
        }
        return insertsAndRemoves;
    }
    
    
}
