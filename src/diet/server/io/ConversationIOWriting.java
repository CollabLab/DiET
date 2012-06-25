/*
 * ConversationIOWriting.java
 *
 * Created on 17 November 2007, 17:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.server.io;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Vector;

import diet.message.Message;
import diet.message.MessageKeypressed;
import diet.message.MessageChatTextFromClient;
import diet.server.Conversation;
import diet.server.Participant;
import diet.server.CbyC.DocChange;
import diet.server.conversationhistory.Conversant;
import diet.server.conversationhistory.LexiconEntry;
import diet.server.conversationhistory.turn.DataToBeSaved;
import diet.server.conversationhistory.turn.Turn;
import diet.utils.FilenameToolkit;
/**
 *
 * @author user
 */
public class ConversationIOWriting {
    
    File conversationDirectory;
    File msgsSerialized;
    File msgsAsText;
    File turnsSerialized;
    File turnsAsText;        
    File docEditsSerialized;
    File docEditsAsText;   
    HashMap<String, PrintWriter> windowLogFiles;
    
    ObjectOutputStream msgsSerializedOut;
    FileWriter msgsAsTextOut;
    ObjectOutputStream turnsSerializedOut;
    FileWriter turnsAsTextOut;
    //ObjectOutputStream docEditsSerializedOut;
    //FileWriter docEditsAsTextOut;
    
    int turnsWritingResetThreshold =30;
    int turnsWritesSinceLastReset =0;
    int msgsWritingResetThreshold =30;
    int msgsWritesSinceLastReset =0; 
    
    //int docEditsWritingResetThreshold =30;
    //int docEditsWritesSinceLastReset =0;  
    
    FileOutputStream fos ;
    PrintWriter pwerrorfos;
    Conversation c;
    
    public ConversationIOWriting(Conversation c,String parentDir,String suffix,Turn t) {
        this.c=c;
        this.windowLogFiles=new HashMap<String, PrintWriter>();
        System.err.println("PARENTDIR "+parentDir+" : SUFFIX : "+suffix);
        File pDirF = new File(parentDir);
        String[] dirlist = pDirF.list();
        String prefixNumber=FilenameToolkit.getNextPrefixInteger(dirlist);            
        conversationDirectory = new File(parentDir+File.separatorChar+prefixNumber+suffix);
        try{
          conversationDirectory.mkdirs();
          
          msgsSerialized= new File(conversationDirectory.getAbsolutePath()+File.separatorChar+"messages.dat");
          msgsSerialized.createNewFile();          
          FileOutputStream msgsOutput = new FileOutputStream(msgsSerialized);
          msgsSerializedOut =  new ObjectOutputStream(msgsOutput);
          
     
          msgsAsText = new File(conversationDirectory.getAbsolutePath()+File.separatorChar+"messages.txt");
          msgsAsTextOut = new FileWriter(msgsAsText);
          
          
          this.turnsSerialized = new File(conversationDirectory.getAbsolutePath()+File.separatorChar+"turns.dat");
          turnsSerialized.createNewFile();          
          FileOutputStream turnsfout = new FileOutputStream(turnsSerialized);
          turnsSerializedOut =  new ObjectOutputStream(turnsfout);
         
          
          turnsAsText = new File(conversationDirectory.getAbsolutePath()+File.separatorChar+"turns.txt");
          turnsAsTextOut = new FileWriter(turnsAsText);
          
          
          
          String turnsHeader = "ParticipantID" + "|" + "Sender" + "|" + "Type" +"|" + "Onset" + "|" + "Enter" + "|" +  "Typingti" + "|" +
                               "Speed" + "|" +  "AppOrig." + "|" + "Text" + "|" +  "Recipients" + "|" +
                               "Blocked" + "|" + "KDels" + "|" +  "DDels" + "|" + "DIns" + "|" +
                               "DDels*N" + "|" + "DIns*N" + "|" + "TaggedText";
            
         turnsHeader = turnsHeader+t.getIOAdditionalHeaders();
               
          
          turnsAsTextOut.write(turnsHeader+"\n");
          turnsAsTextOut.flush();
          
          //Not implemented yet
          //this.docEditsSerialized = new File(conversationDirectory.getAbsolutePath()+File.separatorChar+"docedits.dat");
          //docEditsSerialized.createNewFile();          
          //FileOutputStream docEditsfout = new FileOutputStream(docEditsSerialized);
          //docEditsSerializedOut =  new ObjectOutputStream(docEditsfout);
          
          //docEditsAsText = new File(conversationDirectory.getAbsolutePath()+File.separatorChar+"docedits.txt");
          //docEditsAsTextOut = new FileWriter(turnsAsText);
          
          fos = new FileOutputStream(new File(conversationDirectory,"errorlog.txt"),true);
          pwerrorfos = new PrintWriter(fos);
          
        }
        catch (Exception e){
            System.out.println("Error creating file "+e.toString());
            System.exit(-1);        
        }
    }
  
   
    public File getFileNameContainingConversationData(){
        return conversationDirectory;
    }
    
    
    public void saveMessage(Message m){        
        try{
            msgsSerializedOut.writeObject(m);
            msgsSerializedOut.flush();
           
            msgsWritesSinceLastReset++;
            
            String line = ""
;           if(m.getEmail()!=null){
                line = line + m.getEmail() + "|"; 
            }
             else{
                line = line + "| |";
            }
            if(m.getUsername()!=null){
                line = line + m.getUsername() + "|";
            }
             else{
                line = line + "| |";
            }
            if(m.getMessageClass()!=null){
                line = line + m.getMessageClass() +"|";
            }
             else{
                line = line + "| |";
            }
            if(m.getTimeStamp()!=null){
                line = line + m.getTimeStamp().getTime()+"|";
            }
             else{
                line = line + "| |";
            }
            if(m instanceof MessageChatTextFromClient){
                MessageChatTextFromClient m2 = (MessageChatTextFromClient)m;
                line = line + m2.getTypingOnset() + "|"+ m2.getEndOfTyping()+ "|";
                line = line+ m2.getText()+"|";
            }
            else if(m instanceof MessageKeypressed){
                MessageKeypressed m2 = (MessageKeypressed)m;
                line = line + m2.getKeypressed().getKeycode() + "|";
            }
            else{
                line = line + "| |";
            }
            this.msgsAsTextOut.write(line+"\n");
            this.msgsAsTextOut.flush();
            msgsWritesSinceLastReset++;
            if(msgsWritesSinceLastReset>this.msgsWritingResetThreshold){  
                 this.msgsSerializedOut.flush();
                 this.msgsSerializedOut.reset();
                 msgsWritesSinceLastReset =0;
            }         
            
        }catch (Exception e){
            try{
               System.err.println("ERROR SAVING MESSAGE: "+m.getTimeStamp()+m.getUsername()+m.getMessageClass()+"  ");
            }catch (Exception e2){
                System.err.println("ERROR SAVING MESSAGE");
            }
        }
    }
    
    public void saveDocEdit(DocChange dc){
        
    }
    public void saveWindowTextToLog(String windowName, String text)
    {
    	try{
    		
    	
    		if (this.windowLogFiles.containsKey(windowName))
    		{
    			PrintWriter writer=this.windowLogFiles.get(windowName);
    			writer.println(text);
    			
    		}
    		else    			
    		{
    			PrintWriter writer=new PrintWriter(new FileOutputStream(new File(this.conversationDirectory,windowName+".log")));
    			this.windowLogFiles.put(windowName, writer);
    			writer.println(text);
    		}
    	}
    	catch(Exception e)
    	{
    		System.out.println("Error saving window log text");
    		e.printStackTrace();
    	}
    }
    
    public void saveTurn(Turn t){

      String sendersUsername = "Uninitialized";
      String participantID = "Unititialized";
      String apparentSenderUsername = "Uninitialized";


     if(t instanceof DataToBeSaved){
         DataToBeSaved dtbs = (DataToBeSaved)t;
         sendersUsername = dtbs.getSenderName();
         apparentSenderUsername = dtbs.getApparentSenderName();
         participantID = dtbs.getSenderID();
     }
     else{
        sendersUsername = t.getSender().getUsername();
        Participant sender = c.getParticipants().findParticipantWithUsername(sendersUsername);
        participantID = "Could not find "+sendersUsername;
        apparentSenderUsername = t.getApparentSender().getUsername();
        if(sendersUsername.equalsIgnoreCase("server")){
            participantID="server";
        }
        if(sender!=null){
            participantID=sender.getParticipantID();
        }
     }
    
        String line = "";
        line =  participantID +
               "|" + sendersUsername +
                "|" + t.getType()+
               "|" + t.getTypingOnsetNormalized()+
               "|" + t.getTypingReturnPressedNormalized()+
               "|" + (t.getTypingReturnPressedNormalized()-t.getTypingOnsetNormalized());       

        long typingtime = t.getTypingReturnPressedNormalized()-t.getTypingOnsetNormalized();
        if(typingtime<=0){
            line = line + "|"+0;
        }
        else{
            line = line+ "|"+ (((long)t.getTextString().length())*1000/typingtime);
        }    
        line = line+"|" +apparentSenderUsername +
               "|"+ t.getTextString();
    
    
        Vector v = t.getRecipients();
        String names ="";
        for(int i=0;i<v.size();i++){
            Conversant c = (Conversant)v.elementAt(i);
            names = names+", "+c.getUsername();
        }
        
        line = line+ "|"+names;
        
    
    if (t.getTypingWasBlockedDuringTyping()){
        line = line + "|" + "BLOCKED";
    }    
    else{
        line = line + "|"+"OK";
     }
    
    line = line + "|" + t.getKeypressDeletes() +
           "|" + t.getDocDeletes() +
           "|" + t.getDocInsertsBeforeTerminal() +
           "|" + t.getDocDelsScore()+
           "|" + t.getDocInsScore();
    String returnText="";
    Vector v2 = t.getWordsAsLexicalEntries();
    for(int i=0;i<v2.size();i++){
            LexiconEntry lxe= (LexiconEntry)v2.elementAt(i);
            returnText = returnText+lxe.getWord()+" ("+lxe.getPartOfSpeech()+") ";
    }
    line = line +"|"+returnText;
   
    line = line+t.getIOAdditionalValues();
   

    try{
      this.turnsAsTextOut.write(line+"\n");
      this.turnsAsTextOut.flush();
      this.turnsSerializedOut.writeObject(t);
      this.turnsSerializedOut.flush();
      
     turnsWritesSinceLastReset++;
      if(turnsWritesSinceLastReset>this.turnsWritingResetThreshold){  
         this.turnsSerializedOut.reset();
         turnsWritesSinceLastReset =0;
      }
      
    }catch(Exception e){
        System.err.println("Error saving turn" +e.getMessage());
    }
    
    }
    
    
    public void closeAllFiles(){
        try{
          msgsSerializedOut.flush();
        }catch (Exception e){}
        try{
          msgsAsTextOut.flush();
        }  catch (Exception e){}
        try{
          turnsSerializedOut.flush();
        }catch (Exception e){}
        try{
          turnsAsTextOut.flush();
        }catch (Exception e){}
        try{ 
          msgsSerializedOut.close();
        }  catch (Exception e){}
        try{
         msgsAsTextOut.close();
        } catch (Exception e){}
        try{ 
         turnsSerializedOut.close();
        } catch (Exception e){} 
        try{ 
         turnsAsTextOut.close();
        } catch (Exception e){}
        try{
            pwerrorfos.close();
        } catch (Exception e){}
    }
        
    public void finalize(){
         closeAllFiles();
    }    
    
    public void saveErrorLog(Throwable t){
        try{
           t.printStackTrace(pwerrorfos);
           pwerrorfos.flush();
           
        }catch (Exception e){
            
        }  
        //file f = new File()
    }
    
     public void saveErrorLog(String s){
        try{
           
           pwerrorfos.print(s);
           pwerrorfos.flush();
           
        }catch (Exception e){
            
        }  
        //file f = new File()
    }



     public void saveData(Turn t){

        String sendersUsername = t.getSender().getUsername();

        Participant sender = c.getParticipants().findParticipantWithUsername(sendersUsername);
        String participantID = "Could not find "+sendersUsername;
        if(sendersUsername.equalsIgnoreCase("server")){
            participantID="server";
        }
        if(sender!=null){
            participantID=sender.getParticipantID();
        }


        String line = "";
        line =  participantID +
               "|" + t.getSender().getUsername() +
                "|" + t.getType()+
               "|" + t.getTypingOnsetNormalized()+
               "|" + t.getTypingReturnPressedNormalized()+
               "|" + (t.getTypingReturnPressedNormalized()-t.getTypingOnsetNormalized());

        long typingtime = t.getTypingReturnPressedNormalized()-t.getTypingOnsetNormalized();
        if(typingtime<=0){
            line = line + "|"+0;
        }
        else{
            line = line+ "|"+ (((long)t.getTextString().length())*1000/typingtime);
        }
        line = line+"|" + t.getApparentSender().getUsername() +
               "|"+ t.getTextString();


        Vector v = t.getRecipients();
        String names ="";
        for(int i=0;i<v.size();i++){
            Conversant c = (Conversant)v.elementAt(i);
            names = names+", "+c.getUsername();
        }

        line = line+ "|"+names;


    if (t.getTypingWasBlockedDuringTyping()){
        line = line + "|" + "BLOCKED";
    }
    else{
        line = line + "|"+"OK";
     }

    line = line + "|" + t.getKeypressDeletes() +
           "|" + t.getDocDeletes() +
           "|" + t.getDocInsertsBeforeTerminal() +
           "|" + t.getDocDelsScore()+
           "|" + t.getDocInsScore();
    String returnText="";
    Vector v2 = t.getWordsAsLexicalEntries();
    for(int i=0;i<v2.size();i++){
            LexiconEntry lxe= (LexiconEntry)v2.elementAt(i);
            returnText = returnText+lxe.getWord()+" ("+lxe.getPartOfSpeech()+") ";
    }
    line = line +"|"+returnText;
    line = line+t.getIOAdditionalValues();

    try{
      this.turnsAsTextOut.write(line+"\n");
      this.turnsAsTextOut.flush();
      this.turnsSerializedOut.writeObject(t);
      this.turnsSerializedOut.flush();

     turnsWritesSinceLastReset++;
      if(turnsWritesSinceLastReset>this.turnsWritingResetThreshold){
         this.turnsSerializedOut.reset();
         turnsWritesSinceLastReset =0;
      }

    }catch(Exception e){
        System.err.println("Error saving turn" +e.getMessage());
    }

    }




}
