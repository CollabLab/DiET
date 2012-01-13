/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.experimentmanager;

import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import bsh.Interpreter;
import diet.client.ConnectionToServer;
import diet.parameters.ExperimentSettings;
import diet.parameters.IntParameter;
import diet.parameters.ui.SavedExperimentsAndSettingsFile;
import diet.server.Conversation;
import diet.server.ConversationUIManager;
import diet.server.ExperimentManager;

/**
 *
 * @author user
 */
public class EMUI {

   
    //int portNo = 4444;
    JExperimentManagerMainFrame jmf;

   
    ExperimentManager expmanager;
    Hashtable eachConversationsParseTrees = new Hashtable();
    Hashtable eachConversationsDynamicTextDisplay = new Hashtable();
    Hashtable eachTabbedPanesConversation = new Hashtable();
    static JExperimentManagerMainFrame jmfStatic;
    IntParameter iPortNumber;
    
    
    
    /**
     * Main class
     */
    public EMUI(){
        final EMUI thisEMUI = this;
        File generalSettingsFile = new File(System.getProperty("user.dir")+File.separator+"data"+File.separator+"General settings.xml");
        Vector v =SavedExperimentsAndSettingsFile.readParameterObjects(generalSettingsFile); 
        ExperimentSettings generalSett = new ExperimentSettings(v);
        iPortNumber=(IntParameter)generalSett.getParameter("Port Number");
        
        expmanager = new ExperimentManager(this,generalSett);
        try{
          SwingUtilities.invokeLater(new Runnable(){
              public void run(){
                  jmf = new JExperimentManagerMainFrame(thisEMUI,System.getProperty("user.dir")+File.separator+"data");
                  
              }
          });  
          
        }catch (Exception e){
            System.out.println("ERROR SETTING UP JEXPERIMENTMANAGERMAINFRAME");
        }
        jmfStatic = jmf;
              //ExperimentSettings expSett = getExperimentSettings();
              //expmanager.createAndActivateNewExperiment(expSett);
              //int n = (Integer)expSett.getV("Number of participants per conversation");
              //createClientsLocally(n,4444);
              //EMUI.println("NEW WINDOW", "HELLO");
        
    }    
    
    
    
    
    
    public Vector getCurrentlyRunningExperiments(){
        return new Vector();
    }
    
    
        
    public void runExperiment(final ExperimentSettings expSett){ 
        Thread r = new Thread(){
           public void run(){           
              ////expSett.changeParameterValue("Experiment Data Folder",System.getProperty("user.dir")+"\\data\\Saved experimental data");
              ////expSett.changeParameterValue("Experiment Data Folder",System.getProperty("user.dir")+File.separator+"data"+File.separator+"Saved experimental data");
              expmanager.createAndActivateNewExperiment(expSett);
              int n = (Integer)expSett.getV("Number of participants per conversation");
           }
        };  
         r.start();
    }   
    
    public void runExperimentLocally(final ExperimentSettings expSett){
         Thread r = new Thread(){
           public void run(){           
              /////expSett.changeParameterValue("Experiment Data Folder",System.getProperty("user.dir")+"\\data\\Saved experimental data");
              expSett.changeParameterValue("Experiment Data Folder",System.getProperty("user.dir")+File.separator+"data"+File.separator+"Saved experimental data");
              expmanager.createAndActivateNewExperiment(expSett);
              int n = (Integer)expSett.getV("Number of participants per conversation");
              createClientsLocally(n,iPortNumber.getValue());
           }
        };  
         r.start();   
    }
    
    public void runExperiment(final FileTreeNode ftn){
        //System.exit(-2);
        if(ftn==null){
            System.exit(-1232131);
        }
        Thread r = new Thread(){
        
           public void run(){ 
             
              File f = (File) ftn.getUserObject();
              if(ftn.get_DIR_XML_BSH().equalsIgnoreCase("XML")){
                  Vector v =SavedExperimentsAndSettingsFile.readParameterObjects(f); 
                  ExperimentSettings expSett = new ExperimentSettings(v);
                  //expSett.changeParameterValue("Experiment Data Folder",System.getProperty("user.dir")+"\\data\\Saved experimental data");
                  expSett.changeParameterValue("Experiment Data Folder",System.getProperty("user.dir")+File.separator+"data"+File.separator+"Saved experimental data");
                  expmanager.createAndActivateNewExperiment(expSett);
                  int n = (Integer)expSett.getV("Number of participants per conversation");
                  
              }
              else if(ftn.get_DIR_XML_BSH().equalsIgnoreCase("BSH")){
                  String s = SavedExperimentsAndSettingsFile.readBSHFromFile(f);
                  Interpreter i = new Interpreter();
                  try{
                     i.eval(s);
                     System.err.println("HERE0");
                     ExperimentSettings expSettings = (ExperimentSettings)i.eval("getExperimentParameters()");
                     expSettings.changeParameterValue("Experiment Data Folder",System.getProperty("user.dir")+File.separator+"data"+File.separator+"Saved experimental data");
                     System.err.println("HERE1");
                     expmanager.createAndActivateNewExperiment(i);
                     System.err.println("HERE2");
                     int n = (Integer)expSettings.getV("Number of participants per conversation");
                     System.err.println("HERE3");
                     
                  }catch (Exception e){
                      System.err.println("ERROR CREATING INTERPRETER "+e.getMessage().toString());
                  }   
              }
           }     
        
        
        };
        r.start();
    }   
    
    
    
    public void runExperimentLocally(final FileTreeNode ftn){
        //System.exit(-2);
        if(ftn==null){
            System.exit(-1232131);
        }
        Thread r = new Thread(){
        
           public void run(){ 
             
              File f = (File) ftn.getUserObject();
              if(ftn.get_DIR_XML_BSH().equalsIgnoreCase("XML")){
                  Vector v =SavedExperimentsAndSettingsFile.readParameterObjects(f); 
                  ExperimentSettings expSett = new ExperimentSettings(v);
                  //expSett.changeParameterValue("Experiment Data Folder",System.getProperty("user.dir")+"\\data\\Saved experimental data");
                  expSett.changeParameterValue("Experiment Data Folder",System.getProperty("user.dir")+File.separator+"data"+File.separator+"Saved experimental data");
                  expmanager.createAndActivateNewExperiment(expSett);
                  int n = (Integer)expSett.getV("Number of participants per conversation");
                  createClientsLocally(n,iPortNumber.getValue());
              }
              else if(ftn.get_DIR_XML_BSH().equalsIgnoreCase("BSH")){
                  String s = SavedExperimentsAndSettingsFile.readBSHFromFile(f);
                  Interpreter i = new Interpreter();
                  try{
                     i.eval(s);
                     System.err.println("HERE0");
                     ExperimentSettings expSettings = (ExperimentSettings)i.eval("getExperimentParameters()");
                     System.err.println("HERE1");
                     expSettings.changeParameterValue("Experiment Data Folder",System.getProperty("user.dir")+File.separator+"data"+File.separator+"Saved experimental data");
                     expmanager.createAndActivateNewExperiment(i);
                     System.err.println("HERE2");
                     int n = (Integer)expSettings.getV("Number of participants per conversation");
                     System.err.println("HERE3");
                     createClientsLocally(n,iPortNumber.getValue());
                  }catch (Exception e){
                      System.err.println("ERROR CREATING INTERPRETER "+e.getMessage().toString());
                  }   
              }
           }     
        
        
        };
        r.start();
    }   
    
    
    
    public void displayConversationUI(Conversation c, ConversationUIManager convUI){
        
        JTabbedPane jtp = (JTabbedPane)convUI.createConversationTabbedPane();
        JFrame jc =convUI.createConversationParseTrees();
        this.eachConversationsParseTrees.put(c, jc);
        jc = convUI.createClientTextEntryFields();
        this.eachConversationsDynamicTextDisplay.put(c,jc);
        this.jmf.addConvIOtoLeftTabbedPane(c.getConversationIdentifier(),jtp);
        if(c==null)System.exit(-12341234);
        if(jtp==null)System.exit(-12341235);
        this.eachTabbedPanesConversation.put(jtp,c);
    }
    
      public void stopAllExperimentThreads(JComponent jc,boolean closeWindowOnServer,boolean closeClientsDown){
        System.err.println("STOP THE EXPERIMENT: " +jc.getClass().toString());
        Conversation c = (Conversation)this.eachTabbedPanesConversation.get(jc);
        if(c==null){
            System.exit(-123456);
        }
        this.expmanager.allConversations.remove(c);
        c.closeDown(closeClientsDown);
        if(closeWindowOnServer){
            this.eachTabbedPanesConversation.remove(jc);
            this.jmf.removeConvIOFromLeftTabbedPane(jc);
            
            //ADD IN CODE REMOVING THE TABLES FROM THE WINDOW
        }
        
    }
    
    public void toggleScrolling(JTabbedPane tabbedPane){
        Conversation c = (Conversation)this.eachTabbedPanesConversation.get(tabbedPane);
        int idx = tabbedPane.getSelectedIndex();
        String s = tabbedPane.getTitleAt(idx);
        c.getCHistoryUIM().toggleScrolling(s);
    }
    
    public void displayONOFFParseTreesOfConversationCorrespondingToTabbedPane(JComponent tabbedPane){
         
         System.out.println("HEREA");
         Conversation c = (Conversation)this.eachTabbedPanesConversation.get(tabbedPane);
         System.out.println(eachTabbedPanesConversation.size());
         System.out.println("CLASS IS: "+tabbedPane.getClass().toString());
         //System.exit(-3214);
         System.out.println("HEREB");
         Object o = this.eachConversationsParseTrees.get(c);
         System.out.println(o.getClass().toString());
         //System.exit(-3214);
         JFrame jf = (JFrame)this.eachConversationsParseTrees.get(c);
         System.out.println("HEREC");
        jf.setVisible(!jf.isVisible());
    }
    
    public void displayONOFFDynamicTextDisplayOfConversationCorrespondingToTabbedPane(JComponent tabbedPane){
        Conversation c = (Conversation)this.eachTabbedPanesConversation.get(tabbedPane);
        JFrame jf = (JFrame)this.eachConversationsDynamicTextDisplay.get(c);
        jf.setVisible(!jf.isVisible());
       
    }
    
    
  
    
    
    
    
    
    
    
   
    
    
    
    
    public void createClientsLocally(int n, int portNumber){
        for(int i=0;i<n;i++){
            ConnectionToServer cts = new ConnectionToServer("localhost",portNumber);
            cts.start();
        }
    }
    
    public  void println(String windowName,String s){
        if(jmf!=null){
            jmf.displayTextOutputInBottomTextarea(windowName, s+"\n");
        } 
         else{
            System.out.println("Client "+windowName+": "+s);
        }
    }
    
    public  void print(String windowName,String s){
        if(jmf!=null){
            jmf.displayTextOutputInBottomTextarea(windowName, s);
        }
        else{
            System.out.print("Client "+windowName+": "+s);
        }
    }
     public JExperimentManagerMainFrame getJMF() {
        return jmf;
    }
    
}
