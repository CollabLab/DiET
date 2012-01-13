/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.client;

import diet.message.Keypress;
import java.util.Date;
import java.util.Random;

import javax.swing.SwingUtilities;

/**
 *
 * @author Greg
 */
public class DebugThread extends Thread{

    
    ClientEventHandler clevh;
    Random r = new Random(); 
            
    public DebugThread(diet.client.ClientEventHandler clevh){
        this.clevh=clevh;
        this.start();
        
    }


    public void generateCBYC(){
        int i=0;
        String chrn = "undefined";
        if(clevh.getCts().getUsername().equalsIgnoreCase("Participant1"))chrn="a";
        if(clevh.getCts().getUsername().equalsIgnoreCase("Participant2"))chrn="b";
        if(clevh.getCts().getUsername().equalsIgnoreCase("Participant3"))chrn="c";
        if(clevh.getCts().getUsername().equalsIgnoreCase("Participant4"))chrn="d";

        if(chrn.equalsIgnoreCase("undefined"))return;

        JChatFrame jf = clevh.getChatFrame();
        if(jf instanceof JChatFrameSingleWindowCBYCWithEnforcedTurntaking)
        {
            final JChatFrameSingleWindowCBYCWithEnforcedTurntaking jfcbyc = (JChatFrameSingleWindowCBYCWithEnforcedTurntaking)jf;
            final CBYCDocumentWithEnforcedTurntaking jfcbycdoc = (CBYCDocumentWithEnforcedTurntaking)jfcbyc.jTextPane.getStyledDocument();
            while(2<5){
                 final String chr=chrn;
                 try{Thread.sleep(r.nextInt(450)); }catch(Exception e){}
                 final int i2 =i;
                 SwingUtilities.invokeLater(new Runnable(){
                    public void run(){
                    jfcbycdoc.remove((r.nextInt(jfcbycdoc.getLength()+1)), r.nextInt(jfcbycdoc.getLength()+1));
                    jfcbycdoc.replace(jfcbycdoc.getLength()+1+r.nextInt(5), 0,chr, null);
                    int repeat =r.nextInt(10);
                    String chrnrep = "";
                    for(int i=0;i<repeat;i++){
                        jfcbycdoc.replace(r.nextInt(jfcbycdoc.getLength()+10)-r.nextInt(10), r.nextInt(10), chrnrep, null);
                        chrnrep = chr + chrnrep;
                    }
                    jfcbycdoc.replace(r.nextInt(jfcbycdoc.getLength()+10)-r.nextInt(10), r.nextInt(10), chr, null);

                    jfcbycdoc.replace(jfcbycdoc.getLength()-5+r.nextInt(5), r.nextInt(8),chr, null);

                    int i2 = r.nextInt(10);
                    if(i2>7){
                        jfcbycdoc.replace(jfcbycdoc.getLength(), 0,"M", null);
                        try{Thread.sleep(1000); }catch(Exception e){}
                        jfcbycdoc.replace(jfcbycdoc.getLength(), -1,"DELETES", null);
                        //try{Thread.sleep(5000); }catch(Exception e){}
                        jfcbycdoc.replace(jfcbycdoc.getLength(), 0,"IT SHOULDOVERWRITE THE LAST E", null);
                        //try{Thread.sleep(5000); }catch(Exception e){}

                        jfcbycdoc.replace(jfcbycdoc.getLength(), 0,"beginspace ", null);
                        jfcbycdoc.replace(jfcbycdoc.getLength(), 0,"endspace", null);
                    }
                    else if(i2 >4){
                        jfcbycdoc.replace(jfcbycdoc.getLength(), 0,"", null);
                        jfcbycdoc.replace(jfcbycdoc.getLength()-1, 1,"", null);
                        jfcbycdoc.replace(jfcbycdoc.getLength()-2, 1,"", null);
                    }
                    else if(i2>3){
                        jfcbycdoc.insertString(jfcbycdoc.getLength(), "S", null);
                    }
                 }
             });
             int max = r.nextInt(10);
             for(int j=0;j<max;j++){
                    try{Thread.sleep(r.nextInt(10000)); }catch(Exception e){}
                    SwingUtilities.invokeLater(new Runnable(){
                       public void run(){
                         jfcbycdoc.replace(jfcbycdoc.getLength(), 0,chr , null);
                         jfcbycdoc.replace(jfcbycdoc.getLength(), 0,chr , null);
                       }
                    });
             }
             i=i+1;
             if(i==20){
                 i=0;
                 try{
                     Thread.sleep(r.nextInt(6000)+1000);
                 }catch(Exception e){};
             }

            }
        }
    }

    public void generateISTYPINGANDTURNS(){
        JChatFrame jf = clevh.getChatFrame();
        if(jf instanceof JChatFrameMultipleWindowsWithSendButton){
           //return;
        }
        try{
        while(2<5){
                //if(r.nextInt(100)==0)System.out.println("DBGTHREAD");
                try{Thread.sleep(50+r.nextInt(500));} catch (Exception e){e.printStackTrace();}
                clevh.getCts().sendChatText(clevh.getCts().getUsername(), new Date().getTime(), true, null);
                //if(r.nextInt(5)==0)clevh.getCts().sendChatText("/nbv", new Date().getTime(), true, null);
                int kyp = r.nextInt(20);
                for(int i=0;i<kyp;i++){
                    //try{Thread.sleep(r.nextInt(10));} catch (Exception e){}
                    //clevh.getCts().sendClientIsTyping(new Keypress(45,new Date().getTime()), "KEYPRESS"+i);
                }

            }
        }catch (Exception e2){
            System.err.println(e2.toString());
            e2.printStackTrace();
        }
    }


    public void run(){
        if(diet.debug.Debug.generateFakeCBYCTurns){
            generateCBYC();
        }
        else if (diet.debug.Debug.generateFAKEIsTypingAndTurns){
            this.generateISTYPINGANDTURNS();
        }


       
        


        
        
       
           
        
    }
    
}
