package diet.client;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Caret;


public class JChatFrameSingleWindowWYSIWYGDeprecated extends JChatFrame {

    JPanel mainPanel = new JPanel();
    BorderLayout borderLayout1 = new BorderLayout();
    BorderLayout borderLayout2 = new BorderLayout();
    BorderLayout borderLayout3 = new BorderLayout();
    private JScrollPane scrollPane;
    private JLabel jLabel;
    private JTextArea jTextArea;
    BoxLayout blyout;
    BoxLayout blyout2;
    String labelSpacePadding = "                                         ";
    boolean hasStatusWindow;
    WYSIWYGInputDocumentListener wDL;
    WYSIWYGCaretChangeListener cCL;
       
    

    public JChatFrameSingleWindowWYSIWYGDeprecated(ClientEventHandler clevh, int numberOfRows, int numberOfColumns,boolean hasStatusWindow) {
        super(clevh);
          try {
            this.getContentPane().setLayout(borderLayout1);
            this.getContentPane().add(mainPanel,BorderLayout.NORTH);
            blyout= new BoxLayout(this, BoxLayout.Y_AXIS);          
            this.hasStatusWindow=hasStatusWindow;
            blyout2 = new BoxLayout(mainPanel,BoxLayout.Y_AXIS);
            mainPanel.setLayout(blyout2);
            jTextArea = new JTextArea(numberOfRows,numberOfColumns);
            jTextArea.setLineWrap(true);
            jTextArea.setFocusable(true);
            jTextArea.setEditable(true);
            wDL = new WYSIWYGInputDocumentListener(jTextArea);
            jTextArea.getDocument().addDocumentListener(wDL);
            jTextArea.setFocusable(true);
            
            
            
            cCL = new WYSIWYGCaretChangeListener(jTextArea);
            jTextArea.getCaret().addChangeListener(cCL);
            

               
            scrollPane = new JScrollPane();
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.getViewport().add(jTextArea);
            mainPanel.add(scrollPane);
            jLabel = new JLabel("Network status ok"+labelSpacePadding);
            jLabel.setFont(new java.awt.Font("Dialog", 0, 12));
            if(hasStatusWindow){
               mainPanel.add(jLabel);
            }
            setVisible(true);
            this.pack();
            this.validate();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
    }

    @Override
    public void appendWithCaretCheck(String s, int windowNumber){
        
        if(windowNumber>=1) {
               System.err.println("ERROR IN WINDOW-----------------------------------------------------------------");
               System.err.println("IN wYSIWYGUpdateDocumentFromPositionTo-------------------------------------------");
               System.err.println("WINDOW "+windowNumber+" was addressed");
               return;
           }
            final String text =s;
            SwingUtilities.invokeLater(new Runnable(){
                 public void run(){
                       try{
                        jTextArea.getCaret().removeChangeListener(cCL);
                        jTextArea.getDocument().removeDocumentListener(wDL);
                      
                         jTextArea.append(text);
                         
                         jTextArea.getDocument().addDocumentListener(wDL);
                         jTextArea.getCaret().addChangeListener(cCL);
                       }catch(Exception e){
                           System.err.println("ERROR APPENDING TEXT "+text);
                       }
                 }      
            });
               
    }

    @Override
    public void wYSIWYGUpdateDocumentInsert(int windowNumber,String txt,int offst, int length){
           
           if(windowNumber>=1) {
               System.err.println("ERROR IN WINDOW-----------------------------------------------------------------");
               System.err.println("IN wYSIWYGUpdateDocumentFromPositionTo-------------------------------------------");
               System.err.println("WINDOW "+windowNumber+" was addressed");
               return;
           }
           final int offset = offst;
           final String text = txt;
           try{
             SwingUtilities.invokeLater(new Runnable(){
                 public void run(){
                       try{
                        
                        jTextArea.getCaret().removeChangeListener(cCL);
                        jTextArea.getDocument().removeDocumentListener(wDL);
                           
                         int documentlength = jTextArea.getDocument().getLength();
                         int insrtIndex = documentlength-offset;
                         //if(insrtIndex>=jTextArea.getText().length())insrtIndex = jTextArea.getText().length()-1;
                         if(insrtIndex<0)insrtIndex=0;
                         
                         jTextArea.insert(text,insrtIndex);
                         
                         //jTextArea.getDocument().remove(offset,jTextArea.getText().length()-offset);
                         //jTextArea.append(text);
                         
                         
                         
                         
                       }catch (Error e){
                           System.out.println("ERROR..Bad location in WYSIWYGUPDATEDOCUMENT: "+text+" Offset: "+offset);
                       }finally{
                         jTextArea.getDocument().addDocumentListener(wDL);
                         jTextArea.getCaret().addChangeListener(cCL);
                       }  
                 }
             });
            }catch (Exception e){
               System.err.println("WYSIWYG TEXT NOT PRINTED OFFSET WRONG "+offset+ "text was "+txt);
           }
     }

     @Override
     public void wYSIWYGUpdateDocumentRemove(int windowNumber,int offst, int lngth){
           if(windowNumber>=1) {
               System.err.println("ERROR IN WINDOW-----------------------------------------------------------------");
               System.err.println("IN wYSIWYGUpdateDocumentRemove-------------------------------------------");
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
                        //        jTextArea.getDocument().getLength()+" / "+jTextArea.getText().length());   
                        jTextArea.getCaret().removeChangeListener(cCL);
                        jTextArea.getDocument().removeDocumentListener(wDL);
                           
                         int documentlength = jTextArea.getDocument().getLength();
                         int insrtIndex = documentlength-offset;
                         int lngth2 = length;
                         //if(insrtIndex>=jTextArea.getText().length())insrtIndex = jTextArea.getText().length()-1;
                         if(insrtIndex<0)insrtIndex=0;
                         
                         if(insrtIndex>documentlength)insrtIndex=documentlength;
                         if(insrtIndex+lngth2>documentlength)lngth2 = documentlength-insrtIndex;
                         
                         //jTextArea.getDocument().remove(insrtIndex,length);
                         

                         jTextArea.getDocument().remove(insrtIndex,length);
                         //jTextArea.append(text);
                         
                         
                         
                         
                       }catch (Exception e){
                           System.out.println("ERROR..Bad location in WYSIWYGUPDATEDOCUMENT REMOVE Offset: "+offset+" Length "+length+" DocSize: "+jTextArea.getText().length());
                       }  
                       finally{
                          jTextArea.getDocument().addDocumentListener(wDL);
                         jTextArea.getCaret().addChangeListener(cCL);
                       }
                 }
             });
            }catch (Exception e){
               System.err.println("WYSIWYG TEXT NOT REMOVED OFFSET WRONG "+offset+ "length: "+lngth);
           }         
         
     }    
    
    @Override
    public void setLabelAndTextEntryEnabled(int windowOfOwnText,String txt, boolean txtIsInRed, boolean setEnabld){

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

            jTextArea.setEnabled(setEnabled);
            jTextArea.setEditable(setEnabled);
            jTextArea.setFocusable(setEnabled);
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
                
            }
         });
    }





    @Override
    public void wYSIWYGChangeCursorAndSelection(int windowNumber,int strtPos,int fnshPos){
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
                //System.err.println("--------------------------------------");
                //System.err.println("--------------------------------------");
                //System.err.println("--------------------------------------");
                //System.err.println("--------------------------------------");
                //System.err.println("--------------------------------------");
                //System.err.println("--------------------------------------");
                try{
                  jTextArea.getCaret().removeChangeListener(cCL);
                  jTextArea.getDocument().removeDocumentListener(wDL);
                   
                   int startPos = jTextArea.getText().length()-innerFinishPos;
                   int finishPos = jTextArea.getText().length()-innerStartPos;
                   
                   //int startPos = innerStartPos;
                   //int finishPos = innerFinishPos;
                   
                   if(startPos<0)startPos=0;
                   if(startPos>jTextArea.getText().length())startPos = jTextArea.getText().length()-1;
                   if(finishPos<0)startPos=0;
                   if(finishPos>jTextArea.getText().length())finishPos = jTextArea.getText().length()-1;
                   
                 
                  jTextArea.setSelectionStart(startPos);
                  jTextArea.setSelectionEnd(finishPos);
                  jTextArea.validate();
                  jTextArea.repaint();                  
                  
                  
               }catch (Error e){
                   System.out.println("ERROR..Bad location in WYSIWYGCHANGECURSOR: "+innerStartPos+": "+innerFinishPos);
               }finally{
                  jTextArea.getDocument().addDocumentListener(wDL);
                  jTextArea.getCaret().addChangeListener(cCL);
                }                
            }
        });
        System.out.println("CURSORSHOULDBECHANGED");

     }

     @Override
     public void wYSIWYGsetCursorAndSelectionAreDisplayedWYSIWYG(int windowNumber, boolean cursrIsDisplayed, boolean selIsDisplayed){
         if(windowNumber>=1)return;
         final boolean selectionIsDisplayed = selIsDisplayed;
         final boolean cursorIsDisplayed = cursrIsDisplayed;
         SwingUtilities.invokeLater(new Runnable(){
           public void run(){
               jTextArea.getCaret().setSelectionVisible(selectionIsDisplayed);
               jTextArea.setVisible(cursorIsDisplayed);
               jTextArea.validate();
               jTextArea.repaint();
           }    
         });      

    }


    






  class WYSIWYGInputDocumentListener implements DocumentListener{
    JTextArea jTextAreaSource;

   public WYSIWYGInputDocumentListener(JTextArea jTextAreaSource){
       this.jTextAreaSource = jTextAreaSource;
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
          int documentlength = jTextArea.getDocument().getLength();
          int insrtIndex = (documentlength-length)-offset;  //The length of document is increased by length on insertion   
          //System.err.println("DocLength: "+documentlength+" "+". Offset: "+offset+". Length: "+length+". Text: "+jTextAreaSource.getText().substring(offset,offset+length)+". InsertIndex: "+insrtIndex);
          getClientEventHandler().textEntryDocumentHasChangedInsert(jTextAreaSource.getText().substring(offset,offset+length),insrtIndex,length,jTextAreaSource.getText());
      }catch(Error e2){System.err.println("OFFSET ERROR "+offset+" , "+jTextAreaSource.getText().length()+" "+jTextAreaSource.getText()+" "+e2.getStackTrace());
      }     
   }
    
   public void updateRemove(DocumentEvent e){
       int offset = e.getOffset();
       int length = e.getLength();
       
       int documentlength = jTextArea.getDocument().getLength();
       int insrtIndex = (documentlength+length)-offset; // The length of document is decreased by length on deletion
       //System.err.println("REMOVE: DocLength: "+documentlength+" "+". Offset: "+offset+". Length: "+length+". InsertIndex: "+insrtIndex);
       try{
           getClientEventHandler().wYSIWYGDocumentHasChangedRemove(insrtIndex,length);
       }catch(Exception e2){
           System.err.println("ERROR ATTEMPTING TO CAPTURE A DELETE");
       }
       
       
   } 
    
    
 }






    class WYSIWYGCaretChangeListener implements ChangeListener{

          JTextArea jTextAreaSource;

          public WYSIWYGCaretChangeListener (JTextArea jTa){
            jTextAreaSource = jTa;
          }

          public void stateChanged(ChangeEvent e){
             //System.out.println("CaretEvent");
             Caret c = (Caret)e.getSource();
             //if(c.getDot()!=c.getMark()){
              getClientEventHandler().wYSIWYGCursorAndSelectionHasChanged(jTextAreaSource.getText().length()-c.getDot(),jTextAreaSource.getText().length()-c.getMark());
              // getClientEventHandler().wYSIWYGCursorAndSelectionHasChanged(c.getDot(),c.getMark());
             //}
             
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


