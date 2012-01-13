//This class might have to be split in two, to deal with the conversation history and to deal with 
package diet.server;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import bsh.Interpreter;
import bsh.util.JConsole;
import client.MazeGameController2WAY;
import client.MazeGameController3WAY;
import diet.client.CBYCDocumentWithEnforcedTurntaking;
import diet.message.Message;
import diet.message.MessageCBYCChangeTurntakingStatusConfirm;
import diet.message.MessageKeypressed;
import diet.parameters.StringParameterFixed;
import diet.parameters.ui.JExperimentParameterTable;
import diet.parameters.ui.JExperimentParametersFrame;
import diet.server.conversationhistory.Conversant;
import diet.server.conversationhistory.ConversationHistory;
import diet.server.conversationhistory.turn.ContiguousTurn;
import diet.server.conversationhistory.turn.Turn;
import diet.server.conversationhistory.ui.JContiguousTurnsListTable;
import diet.server.conversationhistory.ui.JConversationFrame;
import diet.server.conversationhistory.ui.JConversationParseTreesFrame;
import diet.server.conversationhistory.ui.JLexiconTable;
import diet.server.conversationhistory.ui.JPermissionsTable;
import diet.server.conversationhistory.ui.JTurnsHorizontalTable;
import diet.server.conversationhistory.ui.JTurnsListTable;
import diet.server.ui.JClientEnterFieldsFrame;
import diet.task.TaskController;
import edu.stanford.nlp.trees.Tree;

/**
 * Contains references to all the user interface objects associated with the Conversation and associated methods for updating the 
 * components.
 * @author user
 */
public class ConversationUIManager {
    
    boolean isUsingUI = false;
    
    private ConversationHistory cH;
    private Conversation c;
    private JConversationFrame jcf;
    private JConversationParseTreesFrame jcps;
    private ContiguousTurn ctPrev=null; //To check if contiguous turn has already been displayed
    private JExperimentParametersFrame jepf;
   // private JControlPanel jcp;
    
    private JClientEnterFieldsFrame jceff; 
    private StringParameterFixed displayParseTrees;
    
    
    private JLexiconTable jlt;
    private JTurnsListTable jtlt;
    private JContiguousTurnsListTable jctlt;
    private JTurnsHorizontalTable jtht;
    private JPermissionsTable jperm;
    private JExperimentParameterTable jept;
        
    JCheckBox jCheckBox1 = new JCheckBox("Scroll");
    JCheckBox jCheckBox2 = new JCheckBox("Scroll");
    JCheckBox jCheckBox3 = new JCheckBox("Scroll");
    JCheckBox jCheckBox4 = new JCheckBox("Scroll");
    JCheckBox jCheckBox5 = new JCheckBox("Scroll");
    JCheckBox jCheckBox6 = new JCheckBox("Scroll");   
    
    JTextField jEnterTextField1 = new JTextField();
    JTextField jEnterTextField2 = new JTextField();
    JTextField jEnterTextField3 = new JTextField();
    JTextField jEnterTextField4 = new JTextField();
    JTextField jEnterTextField5 = new JTextField();
    JTextField jEnterTextField6 = new JTextField();
    
    JScrollPane js1 = new JScrollPane();
    JScrollPane js2 = new JScrollPane();
    JScrollPane js3 = new JScrollPane();
    JScrollPane js4 = new JScrollPane();
    JScrollPane js5 = new JScrollPane();
    JScrollPane js6 = new JScrollPane();
    
    boolean scrollLexicon=false;
    boolean scrollTurnsListTable =false;
    boolean scrollContiguousTurnsListTable = false;
    boolean scrollTurnsHorizontalTable = false;
    boolean scrollPermissionsTable = false;
    
    
    private Interpreter interpreter;
    
    
    
    
    public ConversationUIManager(ConversationHistory cH,Conversation c) {
         this.cH = cH;
         this.c=c;
         //jcf = new JConversationFrame(cH,c);
        
         //jcf.getContentPane().add(jcps.horizontalPanel,BorderLayout.SOUTH);
         //jcf.pack();
         //jcf.validate();
         //jcf.setVisible(true);
         
         //jepf = new JExperimentParametersFrame(c.getController().getExpSettings());
         
         displayParseTrees = (StringParameterFixed)c.getExperimentSettings().getParameter("Display parse tree");
         
        // JTabbedPane jt = createConversationTabbedPane();
        // JFrame jf = new JFrame();
        // jf.getContentPane().add(jt);
        // jf.pack();
        // jf.setVisible(true);
         
    }
    
    
    /**
     * 
     * @return a tabbed pane containing all the UI components.
     */
    public JTabbedPane createConversationTabbedPane(){
        if(this.c.getExpManager().emui==null)return null;
        isUsingUI=true;
        
        js1 = new JScrollPane();
        js1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        jlt = new JLexiconTable(cH);
        js1.getViewport().add(jlt);

        
        js2 = new JScrollPane();
        js2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        jtlt = new JTurnsListTable(cH);
        js2.getViewport().add(jtlt);

        
        js3 = new JScrollPane();
        js3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        jctlt = new JContiguousTurnsListTable(cH);
        js3.getViewport().add(jctlt);

        
        js4 = new JScrollPane();
        js4.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        jtht = new JTurnsHorizontalTable(cH);
        js4.getViewport().add(jtht);

        js5 = new JScrollPane();
        js5.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        jperm = new JPermissionsTable(c);
        js5.getViewport().add(jperm);
        
        js6 = new JScrollPane();
         jept = new JExperimentParameterTable(c.getExperimentSettings());
        js6.getViewport().add(jept);
        
        
        
        
        
        JPanel jPanel1 = new JPanel();
        jPanel1.setLayout(new BorderLayout());        
        JPanel jPanel1SouthPanel = new JPanel();
        jPanel1SouthPanel.setLayout(new BorderLayout());
        //jPanel1SouthPanel.add(jCheckBox1,BorderLayout.EAST);
        //jPanel1SouthPanel.add(jEnterTextField1,BorderLayout.CENTER);
        //jPanel1SouthPanel.add(new JLabel("Query: "),BorderLayout.WEST);
        //jPanel1.add(jPanel1SouthPanel,BorderLayout.SOUTH);
        jPanel1.add(js1,BorderLayout.CENTER);
        
        
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout());
        JPanel jPanel2SouthPanel = new JPanel();
        jPanel2SouthPanel.setLayout(new BorderLayout());
        //jPanel2SouthPanel.add(jCheckBox2,BorderLayout.EAST);
        //jPanel2SouthPanel.add(jEnterTextField2,BorderLayout.CENTER);
        //jPanel2SouthPanel.add(new JLabel("Query: "),BorderLayout.WEST);
        //jPanel2.add(jPanel2SouthPanel,BorderLayout.SOUTH);
        jPanel2.add(js2,BorderLayout.CENTER);     
        
        
        
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new BorderLayout());
        JPanel jPanel3SouthPanel = new JPanel();
        //jPanel3SouthPanel.setLayout(new BorderLayout());
        //jPanel3SouthPanel.add(jCheckBox3,BorderLayout.EAST);
        //jPanel3SouthPanel.add(jEnterTextField3,BorderLayout.CENTER);
        //jPanel3SouthPanel.add(new JLabel("Query: "),BorderLayout.WEST);
        //jPanel3.add(jPanel3SouthPanel,BorderLayout.SOUTH);
        jPanel3.add(js3,BorderLayout.CENTER);     
        
        
        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new BorderLayout());
        JPanel jPanel4SouthPanel = new JPanel();
        jPanel4SouthPanel.setLayout(new BorderLayout());
        //jPanel4SouthPanel.add(jCheckBox4,BorderLayout.EAST);
        //jPanel4SouthPanel.add(jEnterTextField4,BorderLayout.CENTER);
        //jPanel4SouthPanel.add(new JLabel("Query: "),BorderLayout.WEST);
        //jPanel4.add(jPanel4SouthPanel,BorderLayout.SOUTH);
        jPanel4.add(js4,BorderLayout.CENTER);     
        
        JPanel jPanel5 = new JPanel();
        jPanel5.setLayout(new BorderLayout());
        JPanel jPanel5SouthPanel = new JPanel();
        jPanel5SouthPanel.setLayout(new BorderLayout());
        //jPanel5SouthPanel.add(jCheckBox5,BorderLayout.EAST);
        //jPanel5SouthPanel.add(jEnterTextField5,BorderLayout.CENTER);
        //jPanel5SouthPanel.add(new JLabel("Query: "),BorderLayout.WEST);
        //jPanel5.add(jPanel5SouthPanel,BorderLayout.SOUTH);
        jPanel5.add(js5,BorderLayout.CENTER);     
        
        JPanel jPanel6 = new JPanel();
        jPanel6.setLayout(new BorderLayout());
        JPanel jPanel6SouthPanel = new JPanel();
        jPanel6SouthPanel.setLayout(new BorderLayout());
        //jPanel6SouthPanel.add(jCheckBox6,BorderLayout.EAST);
        //jPanel6SouthPanel.add(jEnterTextField6,BorderLayout.CENTER);
        //jPanel6SouthPanel.add(new JLabel("Query: "),BorderLayout.WEST);
        //jPanel6.add(jPanel6SouthPanel,BorderLayout.SOUTH);
        jPanel6.add(js6,BorderLayout.CENTER);     
        
        JPanel jPanel7 = new JPanel();
        
        
        
        bsh.util.JConsole console = new JConsole();
        console.setVisible(true);
        interpreter = new Interpreter( console );
        try{
          //interpreter.eval("import java.*;");
          //interpreter.eval("diet.parameters.*");
          //interpreter.eval("import diet.message.*");
          //interpreter.eval("import diet.parameters.*");
          //interpreter.eval("import diet.parameters.ui.*;");
          //interpreter.eval("import diet.beanshell.*");
          //interpreter.eval("import diet.client.*");
          //interpreter.eval("import diet.server.*");
          //interpreter.eval("import diet.*;");
          //interpreter.eval("import diet.server.conversationhistory.ui;");
          interpreter.set("c", c);
          interpreter.set("ch", this.cH);
          interpreter.set("cc",this.c.getController());
          interpreter.set("cC",this.c.getController());
          interpreter.set("expSett",c.getExperimentSettings());
          if(c.getTaskController()!=null){
              TaskController tc = c.getTaskController();
              if(tc instanceof MazeGameController3WAY ){
                  MazeGameController3WAY mgc = (MazeGameController3WAY)c.getTaskController();
                  interpreter.set("mgc", mgc);
              }
              if(tc instanceof MazeGameController2WAY ){
                  MazeGameController2WAY mgc = (MazeGameController2WAY)c.getTaskController();
                  interpreter.set("mgc", mgc);
              }
          }
          
          new Thread( interpreter ).start(); // start a thread to call the run() me
        }catch(Exception e){
            System.out.println("COULD NOT SET UP CONSOLE");
        }  
     // interpreter.set
      
        
        
        
        JTabbedPane jtp = new JTabbedPane();
       
        jtp.addTab("Turns",jPanel4);
        jtp.addTab("Turns detailed",jPanel2);
        jtp.addTab("Lexicon",jPanel1);      
        jtp.addTab("Contiguous Turns",jPanel3);
        jtp.addTab("Window policy",jPanel5);
        jtp.addTab("Parameters",jPanel6);
        jtp.addTab("Script console", console);
        
        
        return jtp;
       
    }
    
    /**
     * Returns the component displaying the dynamically generated parse trees for each participant's turn
     * @return Component displaying parse trees
     */
    public JConversationParseTreesFrame createConversationParseTrees(){
         System.out.println("HERE01");
         jcps = new JConversationParseTreesFrame(cH);
          System.out.println("HERE02");
         jcps.setVisible(true);
          System.out.println("HERE03");
         return jcps;
         
    }
    /**
    * Returns the collection of components displaying the text as it is typed in the chat window.
    * @return Component displaying text typed as it is typed
    */
    public JClientEnterFieldsFrame createClientTextEntryFields(){
        //jcp = new JControlPanel();
         jceff = new JClientEnterFieldsFrame(this);
         jceff.setVisible(true);        
         return jceff;
    }
     
    
    /**
     * Updates the user interface with the information contained in each message. Not currently implemented
     * in order to preserve clarity in the user interface.
     * @param m
     */
    public void updateControlPanel(Message m){
        //jcp.addMessage(m);
    }
    
    
    
    /**
     * Updates all the tables with the information contained in a turn.
     * @param t turn to be added
     */
    public void updateHistoryUI(Turn t){
        if(this.c.getExpManager().emui==null)return;
        try{
            
            ContiguousTurn ct = t.getContiguousTurn();
            //System.out.println(t.getC)
            if(ct==null)System.err.println("ERROR IN CONVERSATIONUIMANAGER");
            //Check if the contiguous turn has already been displayed
            if(ct!=ctPrev){
               // jcf.updateBothContiguousTurnsAndSingleTurns(t,ct);
                jtht.refresh();
                jctlt.addRow(ct);
                jtlt.addRow(t);
                jlt.refresh(); 
                jperm.refresh();
                
                
                ctPrev = ct;
            }
            else{
                
              //jcf.updateOnlySingleTurns(t);
              jtht.refresh();
              //Excluding contiguous turns
              jtlt.addRow(t);
              jlt.refresh();  
              jperm.refresh();
            }
            jctlt.refresh();
            
            
            //System.out.println("SENDING1");
            Vector conversants = cH.getConversants();
            Vector vParseTrees = new Vector();
            Vector vLabels = new Vector();
            //System.out.println("SENDING3");
            for(int i =0;i<conversants.size();i++){
                Conversant c2 = (Conversant)conversants.elementAt(i);
                //System.out.println("SENDINGFORLOOP "+i);
                Vector turnsProduced = c2.getTurnsProduced();
                if(turnsProduced.size()==0){
                    vParseTrees.addElement(null);
                }
                else{
                   vParseTrees.addElement(((Turn)turnsProduced.lastElement()).getParseTree());
                }   
                vLabels.addElement(c2.getUsername());
            }
            //System.out.println("SENDING111");
            //jcps.displayTrees(vParseTrees,vLabels);
            //System.out.println("SENDING2");
            this.doScrolling();
        }catch(Exception e){
            System.err.println("ERROR UPDATING UI IN CONVERSATIONMANAGER"+e.getMessage());
        }
    }
    
  
    
    /**
     * Toggles whether a window scrolls down when new rows are added.
     * @param s
     */
    public void toggleScrolling(String s){
        if(s.equalsIgnoreCase("Contiguous Turns")){
            this.scrollContiguousTurnsListTable=!this.scrollContiguousTurnsListTable;
        }
        else if(s.equalsIgnoreCase("Lexicon")){
            this.scrollLexicon=!this.scrollLexicon;
        }
        else if(s.equalsIgnoreCase("Window policy")){
            this.scrollPermissionsTable=!this.scrollPermissionsTable;
        }
        else if(s.equalsIgnoreCase("Turns")){
             this.scrollTurnsHorizontalTable=!this.scrollTurnsHorizontalTable;
        }
        else if(s.equalsIgnoreCase("Turns detailed")){
            this.scrollTurnsListTable=!this.scrollTurnsListTable;
        }
    }
    
   
    
   
    
    
    /**
     * Scrolls the window on receiving a new turn
     */
    private void doScrolling(){
      
      try{      
       SwingUtilities.invokeLater(new Runnable(){   
           public void run(){
              if(scrollContiguousTurnsListTable){
                  js3.validate();
                  js3.repaint();
                  js3.getVerticalScrollBar().setValue(js3.getVerticalScrollBar().getMaximum());
              }
              if(scrollLexicon){
                 js1.validate();
                 js1.repaint();
                 js1.getVerticalScrollBar().setValue(js1.getVerticalScrollBar().getMaximum());
              }
              if(scrollPermissionsTable){
                 js5.validate();
                 js5.repaint();
                 js5.getVerticalScrollBar().setValue(js5.getVerticalScrollBar().getMaximum());
              }
              if(scrollTurnsHorizontalTable){
                 js4.validate();
                 js4.repaint();
                 js4.getVerticalScrollBar().setValue(js4.getVerticalScrollBar().getMaximum());
              }
              if(scrollTurnsListTable){
                 js2.validate();
                 js2.repaint();
                 js2.getVerticalScrollBar().setValue(js2.getVerticalScrollBar().getMaximum());
              }
           }
       });   
      }catch (Exception e){
          System.err.println("Problem with autoscrolling the tables: " +e.getMessage().toString());
      }
    }
    
    
    
    /**
     * Deprecated
     * @param t
     */
    public void updateHistoryUIDeprecated(Turn t){
        try{
            
            ContiguousTurn ct = t.getContiguousTurn();
            //System.out.println(t.getC)
            if(ct==null)System.err.println("ERROR IN CONVERSATIONUIMANAGER");
            //Check if the contiguous turn has already been displayed
            if(ct!=ctPrev){
                jcf.updateBothContiguousTurnsAndSingleTurns(t,ct);
                ctPrev = ct;
            }
            else{
                
                jcf.updateOnlySingleTurns(t);
            }
            
            
            //System.out.println("SENDING1");
            Vector conversants = cH.getConversants();
            Vector vParseTrees = new Vector();
            Vector vLabels = new Vector();
            //System.out.println("SENDING3");
            for(int i =0;i<conversants.size();i++){
                Conversant c2 = (Conversant)conversants.elementAt(i);
                //System.out.println("SENDINGFORLOOP "+i);
                Vector turnsProduced = c2.getTurnsProduced();
                if(turnsProduced.size()==0){
                    vParseTrees.addElement(null);
                }
                else{
                   vParseTrees.addElement(((Turn)turnsProduced.lastElement()).getParseTree());
                }   
                vLabels.addElement(c2.getUsername());
            }
            //System.out.println("SENDING111");
            //jcps.displayTrees(vParseTrees,vLabels);
            //System.out.println("SENDING2");
        }catch(Exception e){
            System.err.println("ERROR UPDATING UI IN CONVERSATIONMANAGER"+e.getMessage());
        }
    }
   
    /**
     * Parses and displays parse tree of new turn typed by a participant.
     * @param p
     * @param txt
     */
    public void parseTreeAndDisplayOfParticipant(Participant p,String txt){
       
        if(!(displayParseTrees==null)){
           String yesOrNo = displayParseTrees.getValue();
           if(yesOrNo==null){
               
           }
           else if(yesOrNo.equalsIgnoreCase("No")){
               return;
           }
       }
       
       final String text = txt;
       final Participant particip = p;
       Thread rr = new Thread(){
         public void run(){
              //Check for null
              Vector v = cH.getParserWrapper().parseText(text);
              Tree t = (Tree)v.elementAt(1);
              if(t!=null) displayParseTreeOfParticipant(particip,t);
         }  
       };
       rr.start();
   }
   
    /**
     * Displays parse tree of text typed by a participant.
     * @param p
     * @param t
     */
    private void displayParseTreeOfParticipant(Participant p, Tree t){
      
       
       try{
       final int indexOfParticipant = c.getParticipants().getAllParticipants().indexOf(p);
       this.jcps.displayTree(indexOfParticipant,t,p.getUsername());
       System.err.println("TREE IS "+t.nodeString()+" ");
       System.err.println("TREE IS "+t.children()+" ");
       System.err.println("----------------------------");
       }catch(Exception e){System.err.println("ERROR IN DISPLAYTREE "+e.getCause().toString());}
       
       //Vector v = cH.getParserWrapper().splitIntoSBAR(t);
       //if(v!=null){
          // String s = (String)v.elementAt(0);
          // String s2 = (String)v.elementAt(1);
          // System.err.println(s+"--------------"+s2);
       //}
   }
   
   
   
   
   
    /**
     * Displays the text being typed by the participant
     * @param mkp
     */
    public void updateChatToolTextEntryFieldsUI(MessageKeypressed mkp){
       if(jceff!=null)this.jceff.displayText(mkp.getEmail()+"-"+mkp.getUsername(),mkp.getContentsOfTextEntryWindow());
       
   }
    
   /**
     * Displays the text being typed by the participant
     * @param p
     */ 
   public void updateChatToolTextEntryFieldsUI(Participant p){
       if(jceff!=null){
           this.jceff.displayText(p.getParticipantID()+"--"+p.getUsername(),c.getTurnBeingConstructed(p).getParsedText());
          
       }
   }
   
   public void updateChatToolTextEntryFieldsUI(Participant p, diet.message.MessageCBYCTypingUnhinderedRequest mCBYCTUR){
       if(jceff!=null)this.jceff.displayText(p.getUsername(),mCBYCTUR.getText());    
   }
   
   public void updateChatToolTextEntryFieldsUI(Participant p, diet.message.MessageCBYCChangeTurntakingStatus mCBYCTURNCHANGE){
       //if(jceff!=null)this.jceff.displayText(p.getUsername(),mCBYCTURNCHANGE.);
       if(mCBYCTURNCHANGE.getNewStatus()==CBYCDocumentWithEnforcedTurntaking.typingunhindered){
           this.jceff.displayBackground(p.getUsername(), Color.GREEN);
       }
       else if(mCBYCTURNCHANGE.getNewStatus()==CBYCDocumentWithEnforcedTurntaking.othertyping){
           this.jceff.displayBackground(p.getUsername(), Color.RED);
       }
       else if(mCBYCTURNCHANGE.getNewStatus()==CBYCDocumentWithEnforcedTurntaking.nooneelsetyping){
           this.jceff.displayBackground(p.getUsername(), Color.yellow);
       }
       
   }
   public void updateChatToolTextEntryFieldsUI(Participant p,MessageCBYCChangeTurntakingStatusConfirm m){
       
   }

   public void updateChatToolTextEntryFieldsUI(String name, String text){
       if(jceff!=null)this.jceff.displayText(name,text);
   }

   public void updateChatToolTextEntryFieldsUI(Participant p, diet.message.MessageCBYCDocChangeFromClient mCBYCDOCCHANGE){
       if(jceff!=null)this.jceff.displayText(p.getUsername(),mCBYCDOCCHANGE.getDocChange().elementString);    
   }
   
   
   /**
    * 
    * @return the idetifier of the conversation
    */
   public String getConvID(){
       return c.getConversationIdentifier();
   }
   
   /**
    * 
    * @return the BeanShell interpreter
    */
   public Interpreter getInterpreter(){
       return interpreter;
   }
   
   /**
    * Closes down the resources associated with the Conversation
    * @throws java.lang.Exception
    */
   public void closedown() throws Exception{
       //This needs to be done methodically. References are not destroyed properly.
       SwingUtilities.invokeLater(new Runnable(){
          public void run(){
            if(jcf!=null){
                jcf.setVisible(false);
                jcf.dispose();
            }
            jcps.setVisible(false);
            jcps.dispose();       
          } 
       });
       
   }
    
}
