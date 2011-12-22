/*
 * EMTester.java
 *
 * Created on 05 January 2008, 19:12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.server.experimentmanager;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Vector;


import javax.swing.UIManager;


import diet.client.ConnectionToServer;
import diet.debug.Debug;
import diet.parameters.ExperimentSettings;
import diet.parameters.IntParameter;
import diet.parameters.LongParameter;
import diet.parameters.Parameter;
import diet.parameters.StringListParameter;
import diet.parameters.StringParameter;
import diet.parameters.StringParameterFixed;
import diet.parameters.ui.JListEditorWithSingleSelectionOption;
import diet.parameters.ui.SavedExperimentsAndSettingsFile;
import diet.server.ConversationController.CCSEQUENCE;
import diet.server.ExperimentManager;
import diet.server.ServerStartup;
import java.io.File;

/**
 *
 * @author user
 */
public class EMStarter {
    
    /**
     * Creates a new instance of EMTester
     */
    public EMStarter() {
       
        
    }
    
    
 /*    static public void beanshellTester(){
     try{  
       Interpreter i = new Interpreter();  // Construct an interpreter
       i.set("foo", 5);                    // Set variables
       i.set("date", new Date() ); 

        Date date = (Date)i.get("date");    // retrieve a variable

       // Eval a statement and get the result
       i.eval("bar = foo*10");      
       i.eval("classBrowser()");
       System.out.println( i.get("bar") );
       ClassBrowser cb= new ClassBrowser(i.getClassManager());
       JConsole jc = new JConsole();
       JFrame jf = new JFrame();
       jf.getContentPane().add(cb);
       jf.getContentPane().add(jc);
       jf.pack();
       jf.setVisible(true);
       
      }catch(Exception e){
          System.out.println(e.getMessage().toString());
          //System.exit(-2);
      }
      
      
      
  
      
        
    } */
    
    static public void GUITester(){
        
        StringListParameter slp = new StringListParameter("NAME","FIRSTVALUE");
        slp.addNewString("TESTINGVALUE1", false);
        slp.addNewString("TESTINGVALUE2", false);
        
        //JListEditor jles = new  JListEditor(slp);
        JListEditorWithSingleSelectionOption jlesd= new JListEditorWithSingleSelectionOption(slp);
      
        
    }
    
    

    public static void startNOGUI(String f){

    }


    
    public static void main (String [] args){
    
    //System.out.print("\007");
    //System.out.flush();
    
    
    //This is a local setting
        if (args.length==0) {
           args = new String[3];
           args[0]="client";
           args[1]= ""+CCSEQUENCE.ipADDRESS;
           args[2]= "20000";
        }
    if(args.length>1&&(args[0].equalsIgnoreCase("nogui")||args[0].equalsIgnoreCase("nogui_autologin")) ){
        //Format = "NOGUI" NAME PORTNUMBER NUMBEROFCLIENTS
        //EMStarter.startNOGUI(args[1]);
        //int portNumber = Integer.parseInt(args[1]);
        //    int numberOfClients = Integer.parseInt(args[2]);
            if(args[0].equalsIgnoreCase("nogui_autologin")){
                Debug.doAUTOLOGIN = true;
            }
            String s = args[1];
            for(int i=2;i<args.length;i++){
                 s=s+" ";
                 s = s+args[i];
           }        
           File generalSettingsFile = new File(System.getProperty("user.dir")+File.separator+"data"+File.separator+"General settings.xml");
           Vector v =SavedExperimentsAndSettingsFile.readParameterObjects(generalSettingsFile);
           ExperimentSettings generalSett = new ExperimentSettings(v);

           String separatorChar = File.separator;

           String realPathname = System.getProperty("user.dir") + separatorChar + "data" + separatorChar + "Interventions" + separatorChar 				+ "Templates" + separatorChar + s;
		System.err.println(":" + realPathname);
		Vector vExp = SavedExperimentsAndSettingsFile.readParameterObjects(new File(realPathname));
		final ExperimentSettings expSett = new ExperimentSettings(vExp);
		expSett.changeParameterValue("Experiment Data Folder", System
				.getProperty("user.dir")
				+ File.separator
				+ "data"
				+ File.separator
				+ "Saved experimental data");



           ExperimentManager em = new ExperimentManager(null,generalSett);
           em.createAndActivateNewExperiment(expSett);

           IntParameter portNo = (IntParameter) generalSett.getParameter("Port Number");
           int numberOfClients = (Integer)expSett.getV("Number of participants per conversation");

           for (int i = 0; i < numberOfClients; i++) {
			ConnectionToServer cts = new ConnectionToServer("localhost",portNo.getValue());
			cts.start();
		}

        
    }


    if(args.length>=2&&args[0].equalsIgnoreCase("client")){
           try{
               //UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");;
               Integer i = Integer.parseInt(args[2]);
               ConnectionToServer cts = new ConnectionToServer(args[1],i);
               cts.start();
           }
           catch(Exception e){
               System.err.println("Could not start client");
               e.printStackTrace();
           }       
           return;
    }
    
    ServerStartup.initializeDirectories();
    
    if(args.length>2){

            
    }
    else{
        try {
            
             //System.setProperty("apple.laf.useScreenMenuBar", "true");
             //System.setProperty("com.apple.mrj.application.apple.menu.about.name", "DiET Chattool");
             UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
       
            
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "DiET Chattool");
             UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            //  UIManager.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());

             try {
                 UIManager.setLookAndFeel(
                 UIManager.getCrossPlatformLookAndFeelClassName());
             } catch (Exception e) { }


         } catch (Exception e) { 
             System.exit(-1);
         } 
       EMUI emn= new EMUI();
    }          
 }
    
    
    public static void write(){
        try{    
        XMLEncoder e = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("output.xml")));
        System.out.println("HEREA");
        StringParameter p = new StringParameter("IDNAMESTRING","DEFAULT","VALUE");
        IntParameter p2 = new IntParameter("IDNAMEINT",5,6);
        LongParameter p3 = new LongParameter("ID2NAMEINT",55,70);
        //StringParameterFixed p4 = new StringParameterFixed("this","that","then");
        Vector v2 = new Vector();
        v2.addElement("List item1");
        v2.addElement("List item2");
        v2.addElement("List item3");
        StringParameterFixed p5 = new StringParameterFixed("thisalso",1,v2,2);
        System.out.println("HEREB");
        e.writeObject(p);
        e.writeObject(p2);
        e.writeObject(p3);
        //e.writeObject(p4);
        e.writeObject(p5);
        System.out.println("HEREC");
        e.flush();
        e.close();
        read();
       e.close();
      }catch (Exception e){
          System.out.println("ERROR");
      }
    }     
      public static void read(){    
      try{
       System.out.println("HERE1");   
       XMLDecoder d = new XMLDecoder(new BufferedInputStream(new FileInputStream("Testbutton.xml")));
       System.out.println("HERE2");
       Object result = d.readObject();
       System.out.println("HERE3");
        Parameter p = (StringParameter)result;
       System.out.println("PARAMETERIS "+p.getValue());
       //Object result2 = d.readObject();
       while(2<5){
           System.out.println("OBJECT "+result.getClass().toString());
           
           result = d.readObject();
          
               
       }
      
      }  catch(Exception e){
          System.out.println("ERROR "+e.toString());
          
      }
        
        
    }
    
    
}
