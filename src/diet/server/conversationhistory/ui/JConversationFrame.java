


package diet.server.conversationhistory.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import diet.server.Conversation;
import diet.server.conversationhistory.ConversationHistory;
import diet.server.conversationhistory.turn.ContiguousTurn;
import diet.server.conversationhistory.turn.Turn;

/**
 *
 * @author user
 */
public class JConversationFrame extends JFrame{
    
        JLexiconTable jlt;
        JTurnsListTable jtlt;
        JContiguousTurnsListTable jctlt;
        JTurnsHorizontalTable jtht;
        JPermissionsTable jperm;
                
        ConversationHistory cH;
        
        JCheckBox jCheckBox1 = new JCheckBox("Scroll");
        JCheckBox jCheckBox2 = new JCheckBox("Scroll");
        JCheckBox jCheckBox3 = new JCheckBox("Scroll");
        JCheckBox jCheckBox4 = new JCheckBox("Scroll");
        JCheckBox jCheckBox5 = new JCheckBox("Scroll");
        
        JTextField jEnterTextField1 = new JTextField();
        JTextField jEnterTextField2 = new JTextField();
        JTextField jEnterTextField3 = new JTextField();
        JTextField jEnterTextField4 = new JTextField();
        JTextField jEnterTextField5 = new JTextField();
        
         
    public JConversationFrame(ConversationHistory cH,Conversation c) {
        super(cH.getConversationIdentifier());
        this.setLayout(new BorderLayout());

        JScrollPane js1 = new JScrollPane();
        js1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        jlt = new JLexiconTable(cH);
        js1.getViewport().add(jlt);

        
        JScrollPane js2 = new JScrollPane();
        js2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        jtlt = new JTurnsListTable(cH);
        js2.getViewport().add(jtlt);

        
        JScrollPane js3 = new JScrollPane();
        js3.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        jctlt = new JContiguousTurnsListTable(cH);
        js3.getViewport().add(jctlt);

        
        JScrollPane js4 = new JScrollPane();
        js4.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        jtht = new JTurnsHorizontalTable(cH);
        js4.getViewport().add(jtht);

        JScrollPane js5 = new JScrollPane();
        js5.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        jperm = new JPermissionsTable(c);
        js5.getViewport().add(jperm);
        
        JPanel jPanel1 = new JPanel();
        jPanel1.setLayout(new BorderLayout());        
        JPanel jPanel1SouthPanel = new JPanel();
        jPanel1SouthPanel.setLayout(new BorderLayout());
        jPanel1SouthPanel.add(jCheckBox1,BorderLayout.EAST);
        jPanel1SouthPanel.add(jEnterTextField1,BorderLayout.CENTER);
        jPanel1SouthPanel.add(new JLabel("Query: "),BorderLayout.WEST);
        jPanel1.add(jPanel1SouthPanel,BorderLayout.SOUTH);
        jPanel1.add(js1,BorderLayout.CENTER);
        
        
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout());
        JPanel jPanel2SouthPanel = new JPanel();
        jPanel2SouthPanel.setLayout(new BorderLayout());
        jPanel2SouthPanel.add(jCheckBox2,BorderLayout.EAST);
        jPanel2SouthPanel.add(jEnterTextField2,BorderLayout.CENTER);
        jPanel2SouthPanel.add(new JLabel("Query: "),BorderLayout.WEST);
        jPanel2.add(jPanel2SouthPanel,BorderLayout.SOUTH);
        jPanel2.add(js2,BorderLayout.CENTER);     
        
        
        
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new BorderLayout());
        JPanel jPanel3SouthPanel = new JPanel();
        jPanel3SouthPanel.setLayout(new BorderLayout());
        jPanel3SouthPanel.add(jCheckBox3,BorderLayout.EAST);
        jPanel3SouthPanel.add(jEnterTextField3,BorderLayout.CENTER);
        jPanel3SouthPanel.add(new JLabel("Query: "),BorderLayout.WEST);
        jPanel3.add(jPanel3SouthPanel,BorderLayout.SOUTH);
        jPanel3.add(js3,BorderLayout.CENTER);     
        
        
        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new BorderLayout());
        JPanel jPanel4SouthPanel = new JPanel();
        jPanel4SouthPanel.setLayout(new BorderLayout());
        jPanel4SouthPanel.add(jCheckBox4,BorderLayout.EAST);
        jPanel4SouthPanel.add(jEnterTextField4,BorderLayout.CENTER);
        jPanel4SouthPanel.add(new JLabel("Query: "),BorderLayout.WEST);
        jPanel4.add(jPanel4SouthPanel,BorderLayout.SOUTH);
        jPanel4.add(js4,BorderLayout.CENTER);     
        
        JPanel jPanel5 = new JPanel();
        jPanel5.setLayout(new BorderLayout());
        JPanel jPanel5SouthPanel = new JPanel();
        jPanel5SouthPanel.setLayout(new BorderLayout());
        jPanel5SouthPanel.add(jCheckBox5,BorderLayout.EAST);
        jPanel5SouthPanel.add(jEnterTextField5,BorderLayout.CENTER);
        jPanel5SouthPanel.add(new JLabel("Query: "),BorderLayout.WEST);
        jPanel5.add(jPanel5SouthPanel,BorderLayout.SOUTH);
        jPanel5.add(js5,BorderLayout.CENTER);     
        
        JTabbedPane jtp = new JTabbedPane();
        jtp.addTab("Turns",jPanel4);
        jtp.addTab("Turns detailed",jPanel2);
        jtp.addTab("Lexicon",jPanel1);      
        jtp.addTab("Contiguous Turns",jPanel3);
        jtp.addTab("Permissions",jPanel5);
        
        this.getContentPane().add(jtp,BorderLayout.CENTER);
        
        //---------------------
        
       /// jEnterTextField1.getDocument().addDocumentListener(new JFilterChanger(jlt,jEnterTextField1));
       /// jEnterTextField2.getDocument().addDocumentListener(new JFilterChanger(jtlt,jEnterTextField2));
      ///  jEnterTextField3.getDocument().addDocumentListener(new JFilterChanger(jctlt,jEnterTextField3));
      ///  jEnterTextField4.getDocument().addDocumentListener(new JFilterChanger(jtht,jEnterTextField4));
      ///  jEnterTextField5.getDocument().addDocumentListener(new JFilterChanger(this.jperm,jEnterTextField5));
                
        
        
        //-------------------
        
        this.setPreferredSize(new Dimension(800,400));
        this.validate();
        this.pack();
        this.setVisible(true);
                
        
       
    }
    
    public class JFilterChanger implements DocumentListener{
         JDiETTableRowFilter jTableDiET;
         JTextField jEnterTextField;
         public JFilterChanger (JDiETTableRowFilter jTableDiET, JTextField jEnterTextField){
         this.jTableDiET = jTableDiET;
         this.jEnterTextField = jEnterTextField;
      }   
        
      public void changedUpdate(DocumentEvent e) {
           displayIsValid();
     }
  
      public void insertUpdate(DocumentEvent e) {
           displayIsValid();         
     }
 
     public void removeUpdate(DocumentEvent e) {
           displayIsValid();
     }    
     
     public void displayIsValid(){
         
         boolean isValid = jTableDiET.setTableRowFilter(jEnterTextField.getText());
         if(isValid||jEnterTextField.getText().length()==0){
             jEnterTextField.setBackground(Color.WHITE);
             jEnterTextField.repaint();
         }
         else{
             jEnterTextField.setBackground(Color.RED);
             jEnterTextField.repaint();
         }
     }
     
   }
    
    
    public void updateBothContiguousTurnsAndSingleTurns(Turn t,ContiguousTurn ct){
        jtht.refresh();
        jctlt.addRow(ct);
        jtlt.addRow(t);
        jlt.refresh(); 
        jperm.refresh();
    }
    
    public void updateOnlySingleTurns(Turn t){
        jtht.refresh();
        //Excluding contiguous turns
        jtlt.addRow(t);
        jlt.refresh();  
        jperm.refresh();
    }
}
