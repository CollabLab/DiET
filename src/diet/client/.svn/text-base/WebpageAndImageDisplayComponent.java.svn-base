/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.client;

import diet.debug.Debug;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 *
 * @author Greg
 */
public class WebpageAndImageDisplayComponent {

    JFrame jf;
    URL ul;
    JEditorPane editorPane;
    JProgressBar jp ;
    
    public WebpageAndImageDisplayComponent(final String header,final String url,final int width,final int height,final boolean vScrollBar, final boolean progressBar, final boolean forceCourierFont){

       SwingUtilities.invokeLater(new Runnable(){
           public void run(){
             try{
                jf = new JFrame(header);
                jf.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE );
                editorPane = new JEditorPane();
                editorPane.setEditable(false);

                //editorPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
                if(forceCourierFont)editorPane.setFont(new Font("Courier New",Font.PLAIN,15));//System.exit(-5);

                if(url.equalsIgnoreCase("")){
                    //Do nothing
                }
                else if(url.startsWith("<html>"))
                 {
                    editorPane.setBackground(Color.black);
                    editorPane.setForeground(Color.BLACK);
                    editorPane = new JEditorPane("text/html",url);
                    
                }
                else{
                    try{
                       
                       ul = new URL(url);
                       editorPane.setPage(ul);


                    }catch (Exception e){

                        if(Debug.debugWebpageDisplay)editorPane.setText(e.getMessage()+"\n URL was:"+url);
                    }
                }


               JScrollPane editorScrollPane = new JScrollPane(editorPane);
               editorPane.setEditable(false);
               editorScrollPane.setPreferredSize(new Dimension(width, height));
               editorScrollPane.setMinimumSize(new Dimension(width, height));
               editorScrollPane.setMaximumSize(new Dimension(width,height));
               jf.setResizable(false);


               if(vScrollBar){
                  editorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
               }
               else{
                 editorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
               }

               
               
               jf.getContentPane().setLayout(new BorderLayout());
               jf.getContentPane().add(editorScrollPane,BorderLayout.NORTH);
               jf.getContentPane().add(new JPanel(),BorderLayout.CENTER);

               if(progressBar){
                   jp = new JProgressBar(JProgressBar.HORIZONTAL,0,100);

                   jf.getContentPane().add(jp,BorderLayout.SOUTH);
                   jp.setIndeterminate(false);
                   jp.setForeground(Color.red);
                   //jp.setValue(30);
                   //jp.setString("string");
                   jp.setStringPainted(true);
                   //jp.setName("String");
               }
               
               jf.pack();
               jf.setVisible(true);
               }catch (Exception e){
                    System.err.println("Attempted to read a bad URL: "+url);
                    e.printStackTrace();

               }
           }
       });
    }


        
    public void changeJProgressBar(final String text,final Color colorForeground, int value){
        if(value<0)value =0;
        if(value>100)value=100;
        final int valCorr=value;
        SwingUtilities.invokeLater(new Runnable(){
           public void run(){
             try{

        if(jp==null){
             jp = new JProgressBar(JProgressBar.HORIZONTAL,0,100);
             jf.getContentPane().add(jp,BorderLayout.SOUTH);
             jp.setIndeterminate(false);            
             jf.pack();
             jf.setVisible(true);
         }
         jp.setForeground(colorForeground);
         jp.setValue(valCorr);
         jp.setString(text);
         jp.setStringPainted(true);
        }catch (Exception e){
                    System.err.println("\n1"
                            + "\n2"
                            + "\n3"
                            + "\n4"
                            + "\n5"
                            + "\n6"
                            + "\n7"
                            + "\n8"
                            + "\n9"
                            + "\n10"
                            + "\n11Error displaying progressBar");
                    e.printStackTrace();

               }
           }
       });

    }
            
   
    
    public void closeDown(){
        jf.setVisible(false);
        editorPane=null;
        jf.dispose();
        
    }
    
    public void changeWebpage(final String ul, final String prepend, final String append){
        SwingUtilities.invokeLater(new Runnable(){
           public void run(){
           //changeJProgressBar(String ul,Color.black, 2);



               try{
          editorPane.setText("");

          if(ul.startsWith("<html>")){
                 editorPane.setBackground(Color.black);
                 editorPane.setForeground(Color.BLACK);

                 editorPane.setContentType("text/html");
                 editorPane.setText(ul);
          }
          else{
          try{
              editorPane.setPage(new URL(ul));
          }catch (Exception e){
              
          }
          }
          try{
             if(prepend!=null){
                 editorPane.getDocument().insertString(0, prepend, null);
             }
             if(append!=null){
                 editorPane.getDocument().insertString(editorPane.getDocument().getLength(), append, null);
             }
             }catch (Exception e){

             }
          jf.pack();
          jf.repaint();
        } catch (Exception e){
            System.err.println("Could not change webpage");   
        }  
    }
       });
    }



//Put the editor pane in a scroll pane.



    
}
