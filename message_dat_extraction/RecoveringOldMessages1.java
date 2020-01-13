/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package diet.message;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.Vector;

/**
 *
 * @author LX1C
 */
public class RecoveringOldMessages1 {
     
    public static void main (String[] s){
           RecoveringOldMessages1 rom = new RecoveringOldMessages1();
           
           
           rom.processDirectory();
     }
    
    public void processDirectory(){
         
       String userdir =  System.getProperty("user.dir");
        
        
       File f = new File(userdir);
       File[] files = f.listFiles();
       for (File fil:files){
           if(!fil.isDirectory()){
               if(fil.getName().endsWith("messages.dat")){
                   processVector(fil);
               }
           }
       }
       
       
       String path = f.getAbsolutePath();
       
     
         
         
         
    }
    
    
    
     public void processVector(File filename){
         BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
         String allmessages = "" ;
         Vector vOBj = new Vector();
         try{
              ObjectInputStream obj = new ObjectInputStream(new FileInputStream(filename));
              boolean loadobj=true;
               while(loadobj){
                  Object o = obj.readObject();
                  //System.err.println(o);
                  String msgasstring = this.getMessageAsString((Message)o);
                  allmessages = allmessages + msgasstring + "\n";
               }
             // obj.close();
          
        
         }catch(Exception e){
            // e.printStackTrace();
             
         }     
         
         try{ 
           File outputFile = new File(filename.getAbsolutePath()+"out");
           BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
           writer.write(allmessages);
           writer.close();
         }catch(Exception e){
             e.printStackTrace();
         }
     } 

     
     
     
     
     
     
     public String getMessageAsString(Message m){
         
         String type = m.getClass().getName();
         String email = m.getEmail();
         String username= m.getUsername();
         String dateofreceipt = ""+ m.getTimeStamp().getTime();
         
         String additionaltext = "";
         
         
         
         
         if(m instanceof MessageWYSIWYGDocumentSyncFromClientInsert){
            MessageWYSIWYGDocumentSyncFromClientInsert wdsfc = (MessageWYSIWYGDocumentSyncFromClientInsert)m;
            
            String alltextinwindow = wdsfc.getAllTextInWindow().replaceAll("\n", "((NEWLINE))");
            String texttyped =   wdsfc.getTextTyped().replaceAll("\n", "((NEWLINE))");
            int offset = wdsfc.getOffset();
            
             additionaltext = "textinwindow: "+alltextinwindow+",  texttyped:"+texttyped+ ", offset:"+offset;
            
         }
         else if (m instanceof MessageWYSIWYGDocumentSyncFromClientRemove){
             MessageWYSIWYGDocumentSyncFromClientRemove wdsfc = (MessageWYSIWYGDocumentSyncFromClientRemove)m;
            
            int offset = wdsfc.getOffset();
            int length = wdsfc.getLength();
            
            additionaltext = "offset: "+offset + ", length: "+length;
         
         }
         else if (m instanceof MessageKeypressed){
              MessageKeypressed mk = ( MessageKeypressed)m;
              String alltextinwindow = mk.getContentsOfTextEntryWindow();
              int keycode =  mk.getKeypressed().getKeycode();
              additionaltext = "textinwindow: "+alltextinwindow+",  keycode:"+keycode;
         }
         
         else if (m instanceof MessageChatTextFromClient){
              MessageChatTextFromClient mct = (MessageChatTextFromClient)m;
              String text = mct.getText();
              long typingonset = mct.getTypingOnset();
              Vector keypresses = mct.getKeyPresses();
              
              String keyps = "";
              for(int i=0;i<keypresses.size();i++){
                  Keypress kp = (Keypress)keypresses.elementAt(i);
                  keyps = keyps+ " "+ kp.getKeycode();
              }
              
              additionaltext = "text: "+text+",  keypresses:"+keyps;
    
         }
         
        //I think this was the method in the previous versions
        
        String output =  type + "|" + email + "|" +  username + "|" + dateofreceipt + "|" + additionaltext;
        System.err.println(output);
                
        return output;

     } 
         
        
     
 }
