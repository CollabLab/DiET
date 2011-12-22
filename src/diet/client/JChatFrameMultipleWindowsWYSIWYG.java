package diet.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Vector;

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

public class JChatFrameMultipleWindowsWYSIWYG extends JChatFrame {

    JCollectionOfChatWindows jccw;
    BorderLayout borderLayout1 = new BorderLayout();
    BorderLayout borderLayout2 = new BorderLayout();
    BorderLayout borderLayout3 = new BorderLayout();
    int numberOfWindows =0;
    int windowOfOwnText;

    public JChatFrameMultipleWindowsWYSIWYG(ClientEventHandler clevh, int numberOfWindows, int numberOfRows, int numberOfColumns,boolean isVertical,boolean hasStatusWindow,int windowOfOwnText) {
        super(clevh);
            try {
                this.numberOfWindows=numberOfWindows;
                this.getContentPane().setLayout(borderLayout1);
                jccw = new JCollectionOfChatWindows(numberOfWindows,numberOfRows,numberOfColumns,isVertical,hasStatusWindow,windowOfOwnText);
                this.getContentPane().add(jccw,BorderLayout.NORTH);
                this.windowOfOwnText = windowOfOwnText;

               setVisible(true);
               this.pack();
               this.validate();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
    }

    
   public int getParticipantsOwnWindow(){
       return this.windowOfOwnText;
     } 
    
    
   public class DoUpdateTextAreaFromPositionTo implements Runnable{
       JTextArea jta = new JTextArea();
       JScrollPane jsp = new JScrollPane();
       String text = "";
       int offset;

       public DoUpdateTextAreaFromPositionTo(JTextArea jta, JScrollPane jsp, String text, int offset){
           this.jta=jta;
           this.jsp=jsp;
           this.text = text;
           this.offset = offset;
       }
       public void run() {
           try{
             jta.getDocument().remove(offset,jta.getText().length()-offset);
             jta.append(text);
           }catch (Exception e){
               System.err.println("WYSIWYG TEXT NOT PRINTED OFFSET WRONG "+offset+ "text was "+text);
           }
       }
   }




    @Override
    public void wYSIWYGUpdateDocumentInsert(int windowNumber,String replacementText,int offset,int length){
           Vector jTextAreas = jccw.getTextAreas();
           Vector scrollPanes = jccw.getScrollPanes();
           if(windowNumber>=jTextAreas.size())return;
           try{
             SwingUtilities.invokeLater(new DoUpdateTextAreaFromPositionTo((JTextArea)jTextAreas.elementAt(windowNumber),
                                        (JScrollPane)scrollPanes.elementAt(windowNumber),
                                        replacementText,
                                        offset));
             //SwingUtilities.invokeLater(new DoScrolling((JScrollPane)scrollPanes.elementAt(windowNumber)));
            }catch (Exception e){
               System.err.println("WYSIWYG TEXT NOT PRINTED OFFSET WRONG "+offset+ "text was "+replacementText);
           }
        }


    @Override
    public void setLabelAndTextEntryEnabled(int windowNumber,String label, boolean textIsInRed, boolean setEnabled){
       Vector jTextAreas = jccw.getTextAreas();
       Vector scrollPanes = jccw.getScrollPanes();
       Vector jLabels = jccw.getJLabels();
       if (windowNumber >= jLabels.size())return;
       JLabel jls = (JLabel)jLabels.elementAt(windowOfOwnText);

       if (windowOfOwnText >= scrollPanes.size())return;
       JTextArea jta = (JTextArea)jTextAreas.elementAt(windowOfOwnText);
       SwingUtilities.invokeLater(new DoSetLabelAndTextEntryEnabled(jta,jls,label, textIsInRed, setEnabled));   
   }

    @Override
    public void setLabel(int windowNumber,String label,boolean textIsInRed){
         Vector jTextAreas = jccw.getTextAreas();
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
    public void wYSIWYGChangeCursorAndSelection(int windowNumber,int startPos,int finishPos){
        Vector jTextAreas = jccw.getTextAreas();
        if(windowNumber>=jTextAreas.size())return;
        JTextArea jta = (JTextArea)jTextAreas.elementAt(windowNumber);
        SwingUtilities.invokeLater(new DoWYSIWYGChangeCursorAndSelection(jta,startPos,finishPos));
        //System.out.println("CURSORSHOULDBECHANGED");

     }

    @Override
     public void wYSIWYGsetCursorAndSelectionAreDisplayedWYSIWYG(int windowNumber, boolean cursorIsDisplayed, boolean selectionIsDisplayed){
         Vector jTextAreas = jccw.getTextAreas();
         if(windowNumber>=jTextAreas.size())return;
         JTextArea jta = (JTextArea)jTextAreas.elementAt(windowNumber);      
         SwingUtilities.invokeLater(new DoWYSIWYGsetCursorAndSelectionAreDisplayed(jta,cursorIsDisplayed,selectionIsDisplayed));
    }


    class JCollectionOfChatWindows extends JPanel{
        private Vector scrollPanes = new Vector();
        private Vector jLabels = new Vector();
        private Vector jTextAreas = new Vector();
        BoxLayout blyout;
        String labelSpacePadding = "                                         ";
        boolean hasStatusWindow;
        int windowOfParticipant=0;



        public JCollectionOfChatWindows(int numberOfWindows,int numberOfRows,int numberOfColumns,boolean isVertical,boolean hasStatusWindow,int windowOfParticipant){
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
                JTextArea jTexta = new JTextArea(numberOfRows,numberOfColumns);
                jTexta.setLineWrap(true);
                jTexta.setFocusable(false);
                jTexta.setEditable(false);
                //jTexta.setEditable(false);
                this.jTextAreas.addElement(jTexta);
                JScrollPane jscr = new JScrollPane();
                jscr.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                
                if(i==windowOfParticipant){
                     jTexta.getDocument().addDocumentListener(new WYSIWYGInputDocumentListener(jTexta));
                     jTexta.setFocusable(true);
                     jTexta.getCaret().addChangeListener(new WYSIWTGCaretChangeListener(jTexta));
                     jTexta.setEditable(true);
                     jTexta.setEnabled(true);
                }


                
                jscr.getViewport().add(jTexta);
                scrollPanes.addElement(jscr);
                this.add(jscr);


                JLabel jlt = new JLabel("Network status ok"+labelSpacePadding);
                jlt.setFont(new java.awt.Font("Dialog", 0, 12));
                if(hasStatusWindow){
                    jLabels.addElement(jlt);
                    this.add(jlt);
                }
            }

        }

        public Vector getTextAreas(){
            return jTextAreas;
        }

        public Vector getScrollPanes(){
            return scrollPanes;
        }
        public Vector getJLabels(){
            return jLabels;
        }

    }










  class WYSIWYGInputDocumentListener implements DocumentListener{
    JTextArea jTextAreaSource;

   public WYSIWYGInputDocumentListener(JTextArea jTextAreaSource){
       this.jTextAreaSource = jTextAreaSource;
   }

    public void insertUpdate(DocumentEvent e) {
      update(e);
    }
    public void removeUpdate(DocumentEvent e) {
      update(e);
    }
    public void changedUpdate(DocumentEvent e) {
      //Plain text components do not fire these events
    }
    public void update(DocumentEvent e) {
      int offset = e.getOffset();
      int length = e.getLength();
      try{
          getClientEventHandler().wYSIWYGDocumentHasChanged(jTextAreaSource.getText().substring(offset),offset);
      }catch(Exception e2){System.err.println("OFFSET ERROR "+offset+" , "+jTextAreaSource.getText().length()+" "+jTextAreaSource.getText()+" "+e2.getStackTrace());
      }
   }
 }






    class WYSIWTGCaretChangeListener implements ChangeListener{

          JTextArea jTextAreaSource;

          public WYSIWTGCaretChangeListener (JTextArea jTa){
            jTextAreaSource = jTa;
          }

          public void stateChanged(ChangeEvent e){
             //System.out.println("CaretEvent");
             Caret c = (Caret)e.getSource();
             getClientEventHandler().wYSIWYGCursorAndSelectionHasChanged(c.getDot(),c.getMark());

          }
  }




  public class DoWYSIWYGsetCursorAndSelectionAreDisplayed implements Runnable {
           JTextArea jta;
           boolean cursorIsDisplayed;
           boolean selectionIsDisplayed;

           public DoWYSIWYGsetCursorAndSelectionAreDisplayed(JTextArea jta, boolean cursorIsDisplayed, boolean selectionIsDisplayed){
               this.jta = jta;
               this.cursorIsDisplayed=cursorIsDisplayed;
               this.selectionIsDisplayed = selectionIsDisplayed;
           }
           public void run(){
               jta.getCaret().setSelectionVisible(selectionIsDisplayed);
               jta.getCaret().setVisible(cursorIsDisplayed);
               jta.validate();
               jta.repaint();
           }
     }



  public class DoWYSIWYGChangeCursorAndSelection implements Runnable {
              JTextArea jta;
              int startPos;
              int finishPos;

              public DoWYSIWYGChangeCursorAndSelection(JTextArea jta, int startPos, int finishPos){
                  this.jta = jta;
                  this.startPos=startPos;
                  this.finishPos = finishPos;
              }
              public void run(){
                  jta.setSelectionStart(startPos);
                  jta.setSelectionEnd(finishPos);
                  jta.validate();
                  jta.repaint();
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

        public class DoSetLabelAndTextEntryEnabled implements Runnable{
          JTextArea jta = new JTextArea();
          JScrollPane jsp = new JScrollPane();
          boolean setEnabled;
          JLabel label;
           String text;
           boolean textIsInRed;



       public DoSetLabelAndTextEntryEnabled(JTextArea jta, JLabel label, String text, boolean textIsInRed, boolean setEnabled){
              this.jta=jta;
              this.jsp=jsp;
              this.setEnabled = setEnabled;
              this.label=label;
              this.text=text;
              this.textIsInRed = textIsInRed;
          }
          public void run() {
              try{

            jta.setEnabled(setEnabled);
            jta.setEditable(setEnabled);
            jta.setFocusable(setEnabled);
            if(textIsInRed){
               label.setForeground(Color.RED);
            }
            else{
                label.setForeground(Color.BLACK);
            }
            label.setText(text);
              }catch (Exception e){System.err.println("Error changing blocked status of window");}
          }
      }



}


