/*
 * JConversationParseTreesFrame.java
 *
 * Created on 07 October 2007, 18:04
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.server.conversationhistory.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import diet.server.conversationhistory.ConversationHistory;
import edu.stanford.nlp.parser.ui.TreeJPanel;
import edu.stanford.nlp.trees.Tree;


public class JConversationParseTreesFrame extends JFrame{
    
   
    JPanel horizontalPanel = new JPanel();
    Vector treejpanels = new Vector();
    Vector labels = new Vector();
    ConversationHistory c;
    
    
    
    
    public JConversationParseTreesFrame(ConversationHistory c) {
       super (c.getConversationIdentifier());
       this.c =c;
       horizontalPanel.setLayout(new BoxLayout(horizontalPanel,BoxLayout.X_AXIS));
       TreeJPanel tjt = this.makeDefaultTreeJPanel();       
       //horizontalPanel.add(tjt);
       //treejpanels.addElement(tjt);
       
       this.getContentPane().add(horizontalPanel);
       this.pack();
       this.setVisible(true);
    }
   
    public TreeJPanel makeDefaultTreeJPanel(){
        int width = 250;
        int height = 190;
        Color c = Color.WHITE;
        TreeJPanel tjp = new TreeJPanel();
        tjp.setPreferredSize(new Dimension(width,height));
        tjp.setBackground(c);
        tjp.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        return tjp;
    }
    
    public void displayTree(int treeNumber,Tree t,String sender){
        if(treeNumber>=treejpanels.size())addTreesAndLabels(treeNumber+1-treejpanels.size());
        TreeJPanel treeP = (TreeJPanel)treejpanels.elementAt(treeNumber);        
        treeP.setTree(t);
        JLabel jl = (JLabel)labels.elementAt(treeNumber);
        jl.setText(sender);
        
        SwingUtilities.invokeLater(new UpdateTree(treeP,horizontalPanel,this));
    }
    
    public void addTrees(int n){
        for(int i=0;i<n;i++){
            TreeJPanel tjt = makeDefaultTreeJPanel();
            horizontalPanel.add(tjt);
            treejpanels.addElement(tjt);
        }
    }
    
  public void displayTrees(Vector trees,Vector uNames){
      if(trees.size()>treejpanels.size())addTreesAndLabels(trees.size()-treejpanels.size());
      for(int i=0;i<trees.size();i++){
          //System.out.println("GOING ROUND LOOP");
          Tree t = (Tree)trees.elementAt(i);
          if(t==null){
              String sName = (String)uNames.elementAt(i);
              JLabel jl = (JLabel)labels.elementAt(i);
               jl.setText(sName);
          }
          else{
            String sName = (String)uNames.elementAt(i);
            TreeJPanel treeP = (TreeJPanel)treejpanels.elementAt(i);
            JLabel jl = (JLabel)labels.elementAt(i);
            treeP.setTree(t);
            jl.setText(sName);
          }
      }
      SwingUtilities.invokeLater(new UpdateTree(horizontalPanel,horizontalPanel,this));
      
  }  
    
   public void displayTree(int treeNumber,Tree t2){
       if(treeNumber>=treejpanels.size())addTreesAndLabels(treeNumber+1-treejpanels.size());
       TreeJPanel treeP = (TreeJPanel)treejpanels.elementAt(treeNumber);        
       treeP.setTree(t2);
       SwingUtilities.invokeLater(new UpdateTree(treeP,horizontalPanel,this));
   } 
   
   public void addTreesAndLabels(int n){
       for(int i=0;i<n;i++){
         
        JPanel containerForLabelAndTreeJPanel = new JPanel();
        containerForLabelAndTreeJPanel.setLayout(new BorderLayout());
        JLabel jl = new JLabel(" ");  
        jl.setHorizontalTextPosition(SwingConstants.CENTER);
        jl.setHorizontalAlignment(SwingConstants.CENTER);
        TreeJPanel jtp = this.makeDefaultTreeJPanel();
        this.treejpanels.addElement(jtp);
        containerForLabelAndTreeJPanel.add(jtp,BorderLayout.CENTER);
        containerForLabelAndTreeJPanel.add(jl,BorderLayout.SOUTH);
        this.labels.addElement(jl);
        horizontalPanel.add(containerForLabelAndTreeJPanel );        
        }
   }
   
   
   
    
    
    public class UpdateTree implements Runnable{
        
        JPanel jp1;
        JPanel jp2;
        JFrame jf;
        
        public  UpdateTree(JPanel jp1, JPanel jp2,JFrame jf){
           this.jp1 = jp1;
           this.jp2 = jp2;
           this.jf=jf;
        }
        public void run(){
            jf.validate();
            jf.pack();
            jp1.validate();
            jp2.validate();
            jp1.repaint();
            jp2.repaint();
            jf.repaint();
        }
        
        
    }
    
    
}
