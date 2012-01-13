package diet.client;

import diet.message.MessageChangeClientInterfaceProperties;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;

/**
 * Chat window interface with separate text entry area. Text is only sent when participant presses
 * "ENTER"/"RETURN"/"SEND". Can be configured with single window (similar to existing commercial chat
 * tools) or with multiple windows.
 * @author user
 */
public class JChatFrameMultipleWindowsWithSendButtonWidthByHeight extends JChatFrame {

    JCollectionOfChatWindows jccw;
    BorderLayout borderLayout1 = new BorderLayout();
    BorderLayout borderLayout2 = new BorderLayout();
    BorderLayout borderLayout3 = new BorderLayout();
    JPanel jPanel1 = new JPanel();
    JButton jSENDButton = new JButton();
    JPanel jPanel2 = new JPanel();
    JScrollPane jTextEntryScrollPane = new JScrollPane();
    JTextPane jTextEntryPane = new JTextPane();
    //JTextArea jEnterTextArea = new JTextArea();
    // public JTextPaneCBYC jEnterTextArea;

    JLabel jLabeldisplay = new JLabel("Normal operation");
    int participantsOwnWindowForTextEntry;
    InputDocumentListener jtpDocumentListener;

    public int maxcharlength = 90;
    JChatFrameMultipleWindowsWithSendButtonWidthByHeightDocument jcfd= new JChatFrameMultipleWindowsWithSendButtonWidthByHeightDocument(this);
     StyledDocumentStyleSettings wstyles;


    public JChatFrameMultipleWindowsWithSendButtonWidthByHeight(ClientEventHandler clevh, int numberOfWindows, int mainWindowWidth, int mainWindowHeight,boolean isVertical,boolean hasStatusWindow,int windowOfOwnText, int textEntryWidth, int textEntryHeight, int maxCharlength, StyledDocumentStyleSettings styles) {
        super (clevh);
        try {
        this.getContentPane().setLayout(borderLayout1);
        jccw = new JCollectionOfChatWindows(numberOfWindows,mainWindowWidth,mainWindowHeight,isVertical,hasStatusWindow,windowOfOwnText, styles);
        this.getContentPane().add(jccw,BorderLayout.NORTH);
        jSENDButton.setHorizontalTextPosition(SwingConstants.CENTER);
        this.wstyles = styles;
        //jPanel1.setPreferredSize(new Dimension(50, 200));
        jPanel1.setLayout(borderLayout2);

        this.getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);
        jPanel1.add(jPanel2,BorderLayout.EAST);

        
        this.maxcharlength=maxCharlength;
        this.participantsOwnWindowForTextEntry = windowOfOwnText;

        jPanel2.add(jSENDButton, java.awt.BorderLayout.EAST);
        jSENDButton.setPreferredSize(new Dimension(73,64));
        jSENDButton.setText("SEND");
        jSENDButton.addActionListener(new JChatFrameSENDButtonActionListener());
        jSENDButton.setMargin(new Insets(0,0,0,0));

        this.setResizable(false);


         jTextEntryPane.setFocusable(true);
         jTextEntryPane.setEditable(false);
         jTextEntryPane.setFocusable(false);
         jTextEntryPane.setBackground(styles.getBackgroundColor());
         jTextEntryPane.getCaret().setVisible(true);
         jTextEntryPane.setCaretColor(styles.getCaretColor());
         //jTextEntryPane.

         jTextEntryScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
         jTextEntryScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
         jTextEntryScrollPane.getViewport().add(jTextEntryPane);
         jTextEntryScrollPane.getViewport().setPreferredSize(new Dimension(textEntryWidth,textEntryHeight));
         jTextEntryScrollPane.setPreferredSize(new Dimension(textEntryWidth,textEntryHeight));

        jTextEntryPane.setDocument(jcfd);
        styles.setStyles(jcfd);
        jTextEntryPane.setBackground(styles.getBackgroundColor());
        jTextEntryPane.setForeground(styles.getCaretColor());

        jtpDocumentListener = new InputDocumentListener(jTextEntryPane);
        jTextEntryPane.getDocument().addDocumentListener(jtpDocumentListener);

        jTextEntryScrollPane.getViewport().add(jTextEntryPane);
        jTextEntryPane.addKeyListener(new JChatFrameKeyEventListener());

        jPanel1.add(jTextEntryScrollPane, BorderLayout.CENTER);
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setVisible(true);
        this.pack();
        this.validate();
        }catch (Exception ex){
            System.err.println("COULD NOT SET UP CHAT INTERFACE");
            ex.printStackTrace();
        }

    }


    
 public void changeInterfaceProperties(final int newInterfaceproperties){
       //System.err.println("ERROR "+newInterfaceproperties +".."+ MessageChangeClientInterfaceProperties.disableRETURNANDSEND_enableTEXTENTRY);
       
       
       SwingUtilities.invokeLater( new Runnable(){ public void run(){ 
                if(newInterfaceproperties==diet.message.MessageChangeClientInterfaceProperties.disableRETURNANDSEND_enableTEXTENTRY){
                     jccw.setEnablePane(false);
                     
                     
                }
                else if(newInterfaceproperties==diet.message.MessageChangeClientInterfaceProperties.enableRETURNANDSEND_enableTEXTENTRY){
                     jccw.setEnablePane(true);
                     
                }
                else if (newInterfaceproperties==diet.message.MessageChangeClientInterfaceProperties.clearTextEntryField){
                     jTextEntryPane.setText("" );
                    // System.exit(-444);
                }
                else if (newInterfaceproperties==diet.message.MessageChangeClientInterfaceProperties.clearMainTextWindow){
                     jccw.cleartextWindow(0);
                }
                else if (newInterfaceproperties==diet.message.MessageChangeClientInterfaceProperties.disableTextEntry){
                     jTextEntryPane.setEnabled(false);
                     jSENDButton.setEnabled(false);
                     jTextEntryPane.setBackground(Color.GRAY);
                }
                else if (newInterfaceproperties==diet.message.MessageChangeClientInterfaceProperties.enableTextEntry){
                     jTextEntryPane.setEnabled(true);
                     jSENDButton.setEnabled(true);
                     jTextEntryPane.setBackground(Color.WHITE);
                     jTextEntryPane.setFocusable(true);
                     jTextEntryPane.requestFocusInWindow();
                }
        } });
       
       
 }

    @Override
    public void setTextEntrytext(final String s) {
        
        
        SwingUtilities.invokeLater(
                new Runnable(){ public void run(){ 
                     jTextEntryPane.setText(s);
        } });

        
    }
    

    

    @Override
 public void setLabelAndTextEntryEnabled(int windowOfOwnText,String label, boolean textIsInRed, boolean setWindowEnabled){
     Vector jTextPanes = jccw.getTextPanes();
     Vector scrollPanes = jccw.getScrollPanes();
     Vector jLabels = jccw.getJLabels();
     if (windowOfOwnText >= jLabels.size())return;
     JLabel jls = (JLabel)jLabels.elementAt(windowOfOwnText);
     if (windowOfOwnText >= scrollPanes.size())return;
     JTextPane jtp = jTextEntryPane;
     SwingUtilities.invokeLater(new DoSetLabelAndTextEntryAndSendButtonEnabled(jtp,jSENDButton,jls,label, textIsInRed, setWindowEnabled));
 }

 
    @Override
 public void setLabel(int windowNumber,String label,boolean textIsInRed){
         Vector jTextPanes = jccw.getTextPanes();
         Vector scrollPanes = jccw.getScrollPanes();
         Vector jLabels = jccw.getJLabels();
         if (windowNumber >= jLabels.size())return;
         final JLabel jls = (JLabel)jLabels.elementAt(windowNumber);
         final String labl = label;
         final boolean textIsRed = textIsInRed;
         if (windowNumber >= scrollPanes.size())return;
         SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                jls.setText(labl);
                if(textIsRed){
                   jls.setForeground(Color.RED);
                }
                else{
                   jls.setForeground(Color.BLACK);
                }
                
            }
         });
    }
 
 
 
 
    @Override
 public void appendWithCaretCheck(String text, int windowNumber) {
     Vector jTextPanes = jccw.getTextPanes();
     Vector scrollPanes = jccw.getScrollPanes();
     if (windowNumber >= scrollPanes.size())return;
     JTextPane jtp = (JTextPane)jTextPanes.elementAt(windowNumber);
     JScrollPane jsp = (JScrollPane)scrollPanes.elementAt(windowNumber);
     SwingUtilities.invokeLater(new DoAppendTextWithCaretCheck(jtp,jsp,text));
  }


 public void appendWithCaretCheck(String text, int windowNumber, Object attributeset) {
     Vector jTextPanes = jccw.getTextPanes();
     Vector scrollPanes = jccw.getScrollPanes();
     if (windowNumber >= scrollPanes.size())return;
     JTextPane jtp = (JTextPane)jTextPanes.elementAt(windowNumber);
     JScrollPane jsp = (JScrollPane)scrollPanes.elementAt(windowNumber);
     AttributeSet attr=null;

     if(attributeset instanceof String){
        attr = jtp.getStyle((String)attributeset);
     }
     else if (attributeset instanceof AttributeSet){
         attr=(AttributeSet)attributeset;
     }

     SwingUtilities.invokeLater(new DoAppendTextWithCaretCheck(jtp,jsp,text,attr));
 }



    @Override
  public void setTextEntryField(String s){
           SwingUtilities.invokeLater(new DoSetTextEntryField(jTextEntryPane,s));
   }

    @Override
   public int getParticipantsOwnWindow(){
       return this.participantsOwnWindowForTextEntry;
   }

    @Override
public String getTextEnteredInField(){
      return jTextEntryPane.getText();
    }

public  void sendButtonPressed(ActionEvent e) {
      getClientEventHandler().sendButtonPressed();
    }

    void keyPressed(KeyEvent e) {
      getClientEventHandler().keyPressFilter(e);
    }

    void keyReleased(KeyEvent e) {
      this.getClientEventHandler().keyReleaseFilter(e);
  }





    class JCollectionOfChatWindows extends JPanel{
        private Vector scrollPanes = new Vector();
        private Vector jLabels = new Vector();
        private Vector jTextPanes = new Vector();
        BoxLayout blyout;
        String labelSpacePadding = "                                         ";
        boolean hasStatusWindow;
        int windowOfParticipant=0;
        Color enabledColor;
        
      
        
        
        public JCollectionOfChatWindows(int numberOfWindows,int mainWindowWidth,int mainWindowHeight,boolean isVertical,boolean hasStatusWindow,int windowOfParticipant, StyledDocumentStyleSettings styles){
            super();
            if (isVertical){
                 blyout= new BoxLayout(this, BoxLayout.Y_AXIS);
            }
            else {
                blyout= new BoxLayout(this, BoxLayout.X_AXIS);
                labelSpacePadding="";
            }
            this.hasStatusWindow=hasStatusWindow;
            this.setLayout(blyout);
            this.windowOfParticipant=windowOfParticipant;

            


            for(int i=0;i<numberOfWindows;i++){
                JTextPane jTextPane = new JTextPane();
                jTextPane.setEditable(false);
                jTextPane.setFocusable(false);
                jTextPane.setBackground(styles.getBackgroundColor());
                jTextPane.getCaret().setVisible(false);
                jTextPane.setCaretColor(styles.getCaretColor());
                styles.setStyles(jTextPane.getStyledDocument());
                JScrollPane scrollPane = new JScrollPane();
                scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                scrollPane.getViewport().add(jTextPane);
                scrollPane.getViewport().setPreferredSize(new Dimension(mainWindowWidth,mainWindowHeight));
                scrollPane.setPreferredSize(new Dimension(mainWindowWidth,mainWindowHeight));





                JLabel jlt = new JLabel("Network status ok"+labelSpacePadding);
                jlt.setFont(new java.awt.Font("Dialog", 0, 12));

                jTextPanes.addElement(jTextPane);
                scrollPanes.addElement(scrollPane);
                this.add(scrollPane);
                if(hasStatusWindow){
                    jLabels.addElement(jlt);
                    this.add(jlt);
                }
            }

        }


  public Vector getTextPanes(){
      return jTextPanes;
  }

  public Vector getScrollPanes(){
      return scrollPanes;
  }
  public Vector getJLabels(){
      return jLabels;
  }
  public void setEnablePane(boolean enable){
      
      JTextPane jtp = (JTextPane)jTextPanes.elementAt(windowOfParticipant);
      if(!enable & jtp.isEnabled()){
          this.enabledColor =  jtp.getBackground();
          jtp.setBackground(Color.DARK_GRAY);
          jtp.setEnabled(false);
          
          
      }
      if(enable & !jtp.isEnabled()){
          jtp.setBackground(enabledColor);
          jtp.setEnabled(true);
          
      }
      
  }
  public void cleartextWindow(int windowno){
      JTextPane jtp = (JTextPane)jTextPanes.elementAt(windowno);
      jtp.setText("");
  }


    }

   class JChatFrameKeyEventListener extends java.awt.event.KeyAdapter {

   JChatFrameKeyEventListener() {
   }

   public void keyPressed(KeyEvent e) {
     getClientEventHandler().keyPressFilter(e);
     System.out.println("EVENTLISTENER DETERMINES KEYPRESSED");
   }

   public void keyReleased(KeyEvent e) {
     getClientEventHandler().keyReleaseFilter(e);
     System.out.println("EVENTLISTENER DETERMINES KEYRELEASED");
   }
}

class JChatFrameSENDButtonActionListener implements java.awt.event.ActionListener {

 JChatFrameSENDButtonActionListener() {
 }

 public void actionPerformed(ActionEvent e) {
     System.out.println("EVENTLISTENER DETERMINES SEND BUTTON PRESSED");
     getClientEventHandler().sendButtonPressed();
 }
}












public class DoScrolling implements Runnable{
      JScrollPane jsp;

      public DoScrolling(JScrollPane jsp){
          this.jsp = jsp;
      }
      public void run(){
          jsp.validate();
          jsp.repaint();
          jsp.getVerticalScrollBar().setValue(jsp.getVerticalScrollBar().getMaximum());
          jsp.validate();
          jsp.repaint();
      }
  }


  public class DoSetTextEntryField implements Runnable{
      JTextPane jtp;
      String text;

      public DoSetTextEntryField(JTextPane jtp,String text){
          this.jtp=jtp;
          this.text = text;
      }

      public void run(){
          jtp.getDocument().removeDocumentListener(jtpDocumentListener);
          jtp.setText(text);
      
          jTextEntryPane.getDocument().addDocumentListener(jtpDocumentListener);
      }

}

  public class DoAppendTextWithCaretCheck implements Runnable{
          JTextPane jtp;
          String text;
          JScrollPane jsp;
          Object attrset;

          public DoAppendTextWithCaretCheck(JTextPane jtp, JScrollPane jsp,String text){
              this.jtp =jtp;
              this.jsp = jsp;
              this.text=text;

          }

           public DoAppendTextWithCaretCheck(JTextPane jtp, JScrollPane jsp,String text, Object attributeset){
              this.jtp =jtp;
              this.jsp = jsp;
              this.text=text;
              this.attrset=attributeset;
          }
          public void run(){
              //jtp.append(text);
              try{
                if(attrset instanceof String){
                    jtp.getStyledDocument().insertString(jtp.getDocument().getLength(), text, jtp.getStyledDocument().getStyle((String)attrset));
                }
                else if (attrset instanceof AttributeSet){
                    jtp.getStyledDocument().insertString(jtp.getDocument().getLength(), text, (AttributeSet)attrset);
                }
                else{
                    jtp.getStyledDocument().insertString(jtp.getDocument().getLength(), text, null);
                }
                












                 //System.exit(-999);
                
                }catch (Exception e){
                    System.err.println("Error inserting string to interface");
                    e.printStackTrace();
                   
                }
              SwingUtilities.invokeLater(new DoScrolling(jsp));
          }
}



public class DoSetLabelAndTextEntryAndSendButtonEnabled implements Runnable{
  JTextPane jtp = new JTextPane();
  JScrollPane jsp = new JScrollPane();
  boolean setEnabled;
  JLabel label;
   String text;
   boolean textIsInRed;
   JButton jSENDB;



public DoSetLabelAndTextEntryAndSendButtonEnabled(JTextPane jtp, JButton jSENDB, JLabel label, String text, boolean textIsInRed, boolean setEnabled){
      this.jtp=jtp;
      this.jsp=jsp;
      this.setEnabled = setEnabled;
      this.label=label;
      this.text=text;
      this.textIsInRed = textIsInRed;
      this.jSENDB = jSENDB;
  }
  public void run() {
      try{

         jtp.setEnabled(setEnabled);
         jtp.setEditable(setEnabled);
         jtp.setFocusable(setEnabled);
         jSENDB.setEnabled(setEnabled);

         if(textIsInRed){
            label.setForeground(Color.RED);
         }
         else{
            label.setForeground(Color.BLACK);
         }
         label.setText(text);
         if(setEnabled){
             jtp.requestFocus();
         }   
            
      }catch (Exception e){System.err.println("Error changing blocked status of window");}
  }
}







class InputDocumentListener implements DocumentListener{
    JTextPane jTextPaneSource;

   public InputDocumentListener(JTextPane jTextPaneSource){
       this.jTextPaneSource = jTextPaneSource;
   }

    public void insertUpdate(DocumentEvent e) {
      updateInsert(e);
    }
    public void removeUpdate(DocumentEvent e) {
      updateRemove(e);
    }
    public void changedUpdate(DocumentEvent e) {    
    }
    
    
    public void updateInsert(DocumentEvent e) {
      int offset = e.getOffset();
      int length = e.getLength();   
      try{  
          int documentlength = jTextEntryPane.getDocument().getLength();
          int insrtIndex = (documentlength-length)-offset;  //The length of document is increased by length on insertion   
          //System.err.println("DocLength: "+documentlength+" "+". Offset: "+offset+". Length: "+length+". Text: "+jTextAreaSource.getText().substring(offset,offset+length)+". InsertIndex: "+insrtIndex);
          getClientEventHandler().textEntryDocumentHasChangedInsert(jTextPaneSource.getText().substring(offset,offset+length),insrtIndex,length,jTextPaneSource.getText());
      }catch(Error e2){System.err.println("OFFSET ERROR "+offset+" , "+jTextPaneSource.getText().length()+" "+jTextPaneSource.getText()+" "+e2.getStackTrace());
      }     
   }
    
   public void updateRemove(DocumentEvent e){
       int offset = e.getOffset();
       int length = e.getLength();
       
       int documentlength = jTextEntryPane.getDocument().getLength();
       int insrtIndex = (documentlength+length)-offset; // The length of document is decreased by length on deletion
       //System.err.println("REMOVE: DocLength: "+documentlength+" "+". Offset: "+offset+". Length: "+length+". InsertIndex: "+insrtIndex);
       try{
           getClientEventHandler().wYSIWYGDocumentHasChangedRemove(insrtIndex,length);
       }catch(Exception e2){
           System.err.println("ERROR ATTEMPTING TO CAPTURE A DELETE");
       }
       
       
   } 
    
    
 }



   public void closeDown(){

       this.jTextEntryPane.setEnabled(false);
       this.jLabeldisplay.setEnabled(false);
       this.jTextEntryScrollPane.setEnabled(false);
       this.setVisible(false);
       super.dispose();
   }




}


