/*
 * JConversationParseTreesPanel.java
 *
 * Created on 07 October 2007, 21:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.server.conversationhistory.ui;
import java.awt.Color;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import edu.stanford.nlp.parser.ui.TreeJPanel;
import edu.stanford.nlp.trees.Tree;


public class JConversationParseTreesPanel extends JPanel{
    
   
    
    Vector treejpanels = new Vector();
    
    
    
    
    public JConversationParseTreesPanel() {
       this.setLayout(new BoxLayout(this,BoxLayout.X_AXIS));
       TreeJPanel tjt = new TreeJPanel();
       tjt.setBackground(Color.white);
       tjt.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
       this.add(tjt);
       treejpanels.addElement(tjt);      
       this.validate();
       this.setVisible(true);
    }
   
    
    public void displayTree(int treeNumber,Tree t2){
        if(treeNumber>=treejpanels.size())addTrees(treeNumber+1-treejpanels.size());
        TreeJPanel treeP = (TreeJPanel)treejpanels.elementAt(treeNumber);        
        treeP.setTree(t2);
        SwingUtilities.invokeLater(new UpdateTree(treeP,this,this));
    }
    
    public void addTrees(int n){
        for(int i=0;i<n;i++){
            TreeJPanel tjt = new TreeJPanel();
            tjt.setBackground(Color.WHITE);
            tjt.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            this.add(tjt);
            treejpanels.addElement(tjt);
        }
    }
    
   
    
    public class UpdateTree implements Runnable{
        
        JPanel jp1;
        JPanel jp2;
        JPanel jp3;
        
        public  UpdateTree(JPanel jp1, JPanel jp2,JPanel jp3){
           this.jp1 = jp1;
           this.jp2 = jp2;
           this.jp3 = jp3;
        }
        public void run(){
            jp3.validate();
            jp1.validate();
            jp2.validate();
            jp1.repaint();
            jp2.repaint();
            jp3.repaint();
        }
        
        
    }
    
    
}
