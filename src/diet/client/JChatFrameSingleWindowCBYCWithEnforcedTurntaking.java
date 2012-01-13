package diet.client;

import diet.debug.Debug;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Caret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.Style;

import diet.server.CbyC.DocInsert;


/**
 * Prototype of WYSIWYG chat interface with a single window that only permits one participant typing at any one moment.
 * This requires intercepting the underlying Document of the JTextArea in tandem with server-side management of the
 * conversational floor. See also {@link WYSIWYGDocumentWithEnforcedTurnTaking}.
 * 
 * @author user
 */
public class JChatFrameSingleWindowCBYCWithEnforcedTurntaking extends JChatFrame {

    JPanel mainPanel = new JPanel();
    BorderLayout borderLayout1 = new BorderLayout();
    BorderLayout borderLayout2 = new BorderLayout();
    BorderLayout borderLayout3 = new BorderLayout();
    private JScrollPane scrollPane;
    private JLabel jLabel;
    public JTextPaneCBYC jTextPane;
    BoxLayout blyout;
    BoxLayout blyout2;
    String labelSpacePadding = "                                         ";
    boolean hasStatusWindow;
    CBYCInputDocumentListener wDL;
    CBYCCaretChangeListener cCL;
    CBYCDocumentWithEnforcedTurntaking docII;
    public JProgressBar jpb = new JProgressBar(JProgressBar.VERTICAL,JProgressBar.VERTICAL,JProgressBar.VERTICAL);
       

    

    public JChatFrameSingleWindowCBYCWithEnforcedTurntaking(ClientEventHandler clevh, int width, int height,boolean hasStatusWindow,StyledDocumentStyleSettings styles) {
        super(clevh);
          try {
     
            this.getContentPane().setLayout(borderLayout1);
            this.getContentPane().add(mainPanel,BorderLayout.NORTH);
            blyout= new BoxLayout(this, BoxLayout.Y_AXIS);          
            this.hasStatusWindow=hasStatusWindow;
            blyout2 = new BoxLayout(mainPanel,BoxLayout.Y_AXIS);
            mainPanel.setLayout(blyout2);
            jTextPane = new JTextPaneCBYC();
            
            
            //jTextPane.setPreferredSize(new Dimension(numberOfRows,numberOfColumns));
            //jTextPane.setMaximumSize(new Dimension(numberOfRows,numberOfColumns));
            //jTextPane.setLineWrap(true);
            jTextPane.setFocusable(true);
            jTextPane.setEditable(false);
            docII = new CBYCDocumentWithEnforcedTurntaking(this,styles);
            jTextPane.setDocument(docII);
            wDL = new CBYCInputDocumentListener(jTextPane);
            jTextPane.getDocument().addDocumentListener(wDL);
            jTextPane.setFocusable(true);
    
            cCL = new CBYCCaretChangeListener(jTextPane);
            jTextPane.getCaret().addChangeListener(cCL);
            //this.setResizable(false);

            jTextPane.setBackground(styles.getBackgroundColor());
            jTextPane.getCaret().setVisible(true);
            jTextPane.setCaretColor(styles.getCaretColor());
            
            scrollPane = new JScrollPane();
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane.getViewport().add(jTextPane);
            scrollPane.getViewport().setPreferredSize(new Dimension(width,height));
            scrollPane.setPreferredSize(new Dimension(width,height));
            mainPanel.add(scrollPane);
            jLabel = new JLabel("Network status ok"+labelSpacePadding);
            jLabel.setFont(new java.awt.Font("Dialog", 0, 12));
            jLabel.setPreferredSize(new Dimension(width,20));
            jLabel.setMinimumSize(new Dimension(width,20));
            
            mainPanel.add(jLabel);
            
            jTextPane.addKeyListener(new JChatFrameKeyEventListener());
            mainPanel.addKeyListener(new JChatFrameKeyEventListener());
            
            
            setVisible(true);
            this.pack();
            this.validate();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
    }

    
    
    int selectionStart;
    int selectionEnd;
    

    public void setEditable(boolean editable){
        if(!editable){
            selectionStart=jTextPane.getSelectionStart();
            selectionEnd=jTextPane.getSelectionEnd();
        }
        if(editable){
            jTextPane.setSelectionStart(selectionStart);
            jTextPane.setSelectionEnd(selectionEnd);
        }
        this.jTextPane.setEditable(editable);
        jTextPane.getCaret().setVisible(editable);
        
    }
   
   
    
    @Override
    public void cBYCUpdateDocumentInsert(int windowNumber,String txt,int offst, int length,final Object a){
           
           if(windowNumber>=1) {
               System.err.println("ERROR IN WINDOW-----------------------------------------------------------------");
               System.err.println("IN cBYCUpdateDocumentFromPositionTo-------------------------------------------");
               System.err.println("WINDOW "+windowNumber+" was addressed");
               return;
           }
           final int offset = offst;
           final String text = txt;
           try{
             SwingUtilities.invokeLater(new Runnable(){
                 public void run(){
                       try{
                        
                        jTextPane.getCaret().removeChangeListener(cCL);
                        jTextPane.getDocument().removeDocumentListener(wDL);
                           
                         int documentlength = jTextPane.getDocument().getLength();
                         int insrtIndex = documentlength-offset;
                         //if(insrtIndex>=jTextArea.getText().length())insrtIndex = jTextArea.getText().length()-1;
                         if(insrtIndex<0)insrtIndex=0;
                         
                         //jTextArea.insert(text,insrtIndex);
                         ((CBYCDocumentWithEnforcedTurntaking)jTextPane.getDocument()).forceInsert(insrtIndex,text,a);
                         
                          //jTextArea.getDocument().remove(offset,jTextArea.getText().length()-offset);
                         //jTextArea.append(text);
                         
                         
                         
                          jTextPane.getCaret().setDot(docII.getLength());
                       }catch (Error e){
                           System.out.println("ERROR..Bad location in CBYCGUPDATEDOCUMENT: "+text+" Offset: "+offset);
                       }finally{
                         jTextPane.getDocument().addDocumentListener(wDL);
                         jTextPane.getCaret().addChangeListener(cCL);
                       }  
                 }
             });
            }catch (Exception e){
               System.err.println("CBYC TEXT NOT PRINTED OFFSET WRONG "+offset+ "text was "+txt);
           }
     }


    @Override
     public void cBYCUpdateDocumentRemove(int windowNumber,int offst, int lngth){
           if(windowNumber>=1) {
               System.err.println("ERROR IN WINDOW-----------------------------------------------------------------");
               System.err.println("IN cBYCUpdateDocumentRemove-------------------------------------------");
               System.err.println("WINDOW "+windowNumber+" was addressed");
               return;
           }
           final int offset = offst;
           final int length = lngth;
           
           try{
             SwingUtilities.invokeLater(new Runnable(){
                 public void run(){
                       try{
                        
                        //System.out.println("Attempting to remove: offsetFrmRight: "+offset+"  length:"+length+" "+"  DocLength"+
                         //       jTextArea.getDocument().getLength()+" / "+jTextArea.getText().length());   
                        jTextPane.getCaret().removeChangeListener(cCL);
                        jTextPane.getDocument().removeDocumentListener(wDL);
                           
                         int documentlength = jTextPane.getDocument().getLength();
                         int insrtIndex = documentlength-offset;
                         int lngth2 = length;
                         //if(insrtIndex>=jTextArea.getText().length())insrtIndex = jTextArea.getText().length()-1;
                         if(insrtIndex<0)insrtIndex=0;
                         
                         if(insrtIndex>documentlength)insrtIndex=documentlength;
                         if(insrtIndex+lngth2>documentlength)lngth2 = documentlength-insrtIndex;
                         
                         //jTextArea.getDocument().remove(insrtIndex,length);
                         

                         //jTextArea.getDocument().remove(insrtIndex,length);
                         //jTextArea.append(text);
                         ((CBYCDocumentWithEnforcedTurntaking)jTextPane.getDocument()).forceRemove(insrtIndex,length);
                         jTextPane.getCaret().setDot(docII.getLength());
                         
                         
                       }catch (Exception e){
                           System.out.println("ERROR..Bad location in CBYCUPDATEDOCUMENT REMOVE Offset: "+offset+" Length "+length+" DocSize: "+jTextPane.getText().length());
                       }  
                       finally{
                          jTextPane.getDocument().addDocumentListener(wDL);
                         jTextPane.getCaret().addChangeListener(cCL);
                       }
                 }
             });
            }catch (Exception e){
               System.err.println("CBYC TEXT NOT REMOVED OFFSET WRONG "+offset+ "length: "+lngth);
           }         
         
     }    
    public boolean isTextEntryEnabled()
    {
    	return this.jTextPane.isEnabled();
    }
    @Override
    public void setLabelAndTextEntryEnabled(int windowOfOwnText,String txt, boolean txtIsInRed, boolean setEnabld){

        CBYCDocumentWithEnforcedTurntaking cbyc = (CBYCDocumentWithEnforcedTurntaking)this.jTextPane.getDocument();
        if(Debug.showEOFCRSTATES)txt = txt + " STATE:"+cbyc.getState();

       if(windowOfOwnText>=1) {
               System.err.println("ERROR IN WINDOW-----------------------------------------------------");
               System.err.println("IN setLabelAndTextEntryEnabled--------------------------------------------");
               System.err.println("WINDOW "+windowOfOwnText+" was addressed");
               return;
        }
       final boolean setEnabled = setEnabld;
       final boolean textIsInRed = txtIsInRed;
       final String label = txt;
        SwingUtilities.invokeLater(new Runnable (){
        public void run(){
           try{
        	if (!jTextPane.isEnabled()&&setEnabled)
        	{
        		jTextPane.setEnabled(setEnabled);
        		jTextPane.setEditable(setEnabled);
        		jTextPane.setFocusable(setEnabled);
        		jTextPane.requestFocusInWindow();
        	}
        	else if (!setEnabled)
        	{
        		jTextPane.setEnabled(setEnabled);
        		jTextPane.setEditable(setEnabled);
        		jTextPane.setFocusable(setEnabled);
        		mainPanel.requestFocusInWindow();
        		System.out.println("giving keyboard focus to main panel");
        	}
        	else
        	{
        		jTextPane.setEnabled(setEnabled);
        		jTextPane.setEditable(setEnabled);
        		jTextPane.setFocusable(setEnabled);        		
        	}
        	
            if(textIsInRed){
               jLabel.setForeground(Color.RED);
            }
            else{
                jLabel.setForeground(Color.BLACK);
            }
            jLabel.setText(label);
              }catch (Exception e){System.err.println("Error changing blocked status of window");}
          }
        });
       
   }

 @Override
 public void setLabel(int windowNumber,String label,boolean textIsInRed){

        CBYCDocumentWithEnforcedTurntaking cbyc = (CBYCDocumentWithEnforcedTurntaking)this.jTextPane.getDocument();
        if(Debug.showEOFCRSTATES)label = label + " STATE:"+cbyc.getState();

         if (windowNumber >1)return;


         final String labl = label;
         final boolean textIsRed = textIsInRed;
        
         SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                jLabel.setText(labl);
                if(textIsRed){
                   jLabel.setForeground(Color.RED);
                }
                else{
                   jLabel.setForeground(Color.BLACK);
                }
                pack();
                validate();
            }
         });
    }


   


    @Override
    public void cBYCChangeCursorAndSelection(int windowNumber,int strtPos,int fnshPos){
        if(windowNumber>=1) {
               System.err.println("ERROR IN WINDOW-----------------------------------------------------");
               System.err.println("IN setLabelAndTextEntryEnabled--------------------------------------------");
               System.err.println("WINDOW "+windowNumber+" was addressed");
               return;
           }
        final int innerStartPos = strtPos;
        final int innerFinishPos = fnshPos;
        SwingUtilities.invokeLater(new Runnable(){
        
            
            public void run(){
                //System.err.println("UPDATING WITH--------------------------------------"+innerStartPos+" "+innerFinishPos);
                try{
                  jTextPane.getCaret().removeChangeListener(cCL);
                  jTextPane.getDocument().removeDocumentListener(wDL);
                   
                   int startPos = jTextPane.getText().length()-innerFinishPos;
                   int finishPos = jTextPane.getText().length()-innerStartPos;
                   
                   //int startPos = innerStartPos;
                   //int finishPos = innerFinishPos;
                   
                   if(startPos<0)startPos=0;
                   if(startPos>jTextPane.getText().length())startPos = jTextPane.getText().length()-1;
                   if(finishPos<0)startPos=0;
                   if(finishPos>jTextPane.getText().length())finishPos = jTextPane.getText().length()-1;
                   
                 
                  jTextPane.setSelectionStart(startPos);
                  jTextPane.setSelectionEnd(finishPos);
                  jTextPane.validate();
                  jTextPane.repaint();                  
                  
                  
               }catch (Error e){
                   System.out.println("ERROR..Bad location in CBYCCHANGECURSOR: "+innerStartPos+": "+innerFinishPos);
               }finally{
                  jTextPane.getDocument().addDocumentListener(wDL);
                  jTextPane.getCaret().addChangeListener(cCL);
                }                
            }
        });
        //System.out.println("CURSORSHOULDBECHANGED");

     }

    @Override
     public void cBYCsetCursorAndSelectionAreDisplayedCBYC(int windowNumber, boolean cursrIsDisplayed, boolean selIsDisplayed){
         if(windowNumber>=1)return;
         final boolean selectionIsDisplayed = selIsDisplayed;
         final boolean cursorIsDisplayed = cursrIsDisplayed;
         SwingUtilities.invokeLater(new Runnable(){
           public void run(){
               jTextPane.getCaret().setSelectionVisible(selectionIsDisplayed);
               jTextPane.setVisible(cursorIsDisplayed);
               jTextPane.validate();
               jTextPane.repaint();
           }    
         });      

    }

    @Override
    public void cBYCChangeInterceptionStatus(int status,final DocInsert prefixDI,final long id){
        final int state = status;
        
        SwingUtilities.invokeLater(new Runnable(){
           public void run(){
               try{
                 jTextPane.getDocument().removeDocumentListener(wDL);
                 jTextPane.getCaret().removeChangeListener(cCL);    
                 docII.setState(state,prefixDI,id);
                 
               }
               catch (Exception e){                  
               }
               finally{
                 jTextPane.getDocument().addDocumentListener(wDL);
                 if(docII.deletesAllowed==docII.neither||docII.deletesAllowed==docII.deletesENDOFTURNONLY)jTextPane.getCaret().setDot(docII.getLength());
                 jTextPane.getCaret().addChangeListener(cCL);
               }
           }    
         });   
    }
    


   public  JTextPane getJTextPane(){
       return this.jTextPane;
   }



  class CBYCInputDocumentListener implements DocumentListener{
    JTextPane jTextPaneSource;

   public CBYCInputDocumentListener(JTextPane jTextPaneSource){
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
      
      
      
      DefaultStyledDocument dsd = ((DefaultStyledDocument)e.getDocument());
      Style sty = dsd.getLogicalStyle(offset);
      Element ee = ((DefaultStyledDocument)e.getDocument()).getParagraphElement(offset);
      String eeString="";
      try{
        eeString = dsd.getText(ee.getStartOffset(),ee.getEndOffset()-ee.getStartOffset() );
      }catch (Exception exc){
          eeString = "BADLOCATION";
      }
      try{  
          int documentlength = jTextPane.getDocument().getLength();
          int insrtIndex = (documentlength-length)-offset;  //The length of document is increased by length on insertion   
          //System.err.println("DocLength: "+documentlength+" "+". Offset: "+offset+". Length: "+length+". Text: "+jTextAreaSource.getText().substring(offset,offset+length)+". InsertIndex: "+insrtIndex);
          
          String insertedText = "BADLOCATION";
          try{
              insertedText=dsd.getText(offset, length);
          }
          catch(Exception eee){}
          System.err.println("SHOULD BE INSERTING "+insertedText);
          ////getClientEventHandler().cBYCDocumentHasChangedInsert(insertedText,insrtIndex,sty,eeString,ee.getStartOffset(),ee.getEndOffset());
      }catch(Error e2){System.err.println("OFFSET ERROR "+offset+" , "+jTextPaneSource.getText().length()+" "+jTextPaneSource.getText()+" "+e2.getStackTrace());
      }     
   }
    
   public void updateRemove(DocumentEvent e){
       int offset = e.getOffset();
       int length = e.getLength();
       
       
       
      DefaultStyledDocument dsd = ((DefaultStyledDocument)e.getDocument());
      Style sty = dsd.getLogicalStyle(offset);
      Element ee = ((DefaultStyledDocument)e.getDocument()).getParagraphElement(offset);
      String eeString="";
      try{
        eeString = dsd.getText(ee.getStartOffset(),ee.getEndOffset() );
      }catch (Exception exc){
          eeString = "BADLOCATION";
      }
       
       int documentlength = jTextPane.getDocument().getLength();
       int insrtIndex = (documentlength+length)-offset; // The length of document is decreased by length on deletion
       //System.err.println("REMOVE: DocLength: "+documentlength+" "+". Offset: "+offset+". Length: "+length+". InsertIndex: "+insrtIndex);
       try{
           //getClientEventHandler().cBYCDocumentHasChangedRemove(insrtIndex,length,eeString,ee.getStartOffset(),ee.getEndOffset());
       }catch(Exception e2){
           System.err.println("ERROR ATTEMPTING TO CAPTURE A DELETE");
       }
       
       
   } 
    
    
 }






    class CBYCCaretChangeListener implements ChangeListener{

          JTextPane jTextPaneSource;

          public CBYCCaretChangeListener (JTextPane jTp){
            jTextPaneSource = jTp;
          }

          public void stateChanged(ChangeEvent e){
             //System.out.println("CaretEvent");
             Caret c = (Caret)e.getSource();
            
             //if(c.getDot()!=c.getMark()){
             getClientEventHandler().cBYCCursorAndSelectionHasChanged(jTextPane.getText().length()-c.getDot(),jTextPaneSource.getText().length()-c.getMark());
              // getClientEventHandler().wYSIWYGCursorAndSelectionHasChanged(c.getDot(),c.getMark());
             //}
             
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

}


