package diet.server.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

import diet.message.Message;

public class JControlPanel  extends JFrame {

  JTableLogDisplay jtl = new JTableLogDisplay();

  public JControlPanel() throws HeadlessException {
    super();
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }


  public void addMessage(Message m){
      jtl.addMessage(m);
      //System.out.println("ADDING MESSAGE TO CONTROLPANEL");
 }
  public JControlPanel(GraphicsConfiguration gc) {
    super(gc);
  }

  public JControlPanel(String title) throws HeadlessException {
    super(title);
  }

  public JControlPanel(String title, GraphicsConfiguration gc) {
    super(title, gc);
  }

  private void jbInit() throws Exception {
    this.getContentPane().setLayout(borderLayout1);
    jPanel1.setMinimumSize(new Dimension(50, 10));
    jPanel1.setPreferredSize(new Dimension(100, 10));
    jRadioButton1.setText("MessagesFromServer");
    jRadioButton1.addActionListener(new
                                    JControlPanel_jRadioButton1_actionAdapter(this));
    jRadioButton2.setText("MessagesToServer");
    jRadioButton3.setText("MessageKeypress");
    jRadioButton4.setText("MessageErrorMessages");
    jRadioButton5.setText("jRadioButton5");
    jButton1.setText("jButton1");
    jButton1.addActionListener(new JControlPanel_jButton1_actionAdapter(this));

    this.getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);
    jScrollPane1.getViewport().add(jtl);

    //this.getContentPane().add(jPanel1, java.awt.BorderLayout.EAST);
    jPanel1.add(jRadioButton1);
    jPanel1.add(jRadioButton2);
    jPanel1.add(jRadioButton3);
    jPanel1.add(jRadioButton4);
    jPanel1.add(jRadioButton5);
    jPanel1.add(jButton1);
    this.setPreferredSize(new Dimension(750,250));
    this.pack();
    this.setVisible(true);
    
  }

  BorderLayout borderLayout1 = new BorderLayout();
  JScrollPane jScrollPane1 = new JScrollPane();
  //JTable jTable1 = new JTable();
  JPanel jPanel1 = new JPanel();
  JRadioButton jRadioButton1 = new JRadioButton();
  JRadioButton jRadioButton2 = new JRadioButton();
  JRadioButton jRadioButton3 = new JRadioButton();
  JRadioButton jRadioButton4 = new JRadioButton();
  JRadioButton jRadioButton5 = new JRadioButton();
  JButton jButton1 = new JButton();
  public void jRadioButton1_actionPerformed(ActionEvent e) {

  }

  public void jButton1_actionPerformed(ActionEvent e) {

  }
}

class JControlPanel_jButton1_actionAdapter
    implements ActionListener {
  private JControlPanel adaptee;
  JControlPanel_jButton1_actionAdapter(JControlPanel adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jButton1_actionPerformed(e);
  }
}

class JControlPanel_jRadioButton1_actionAdapter
    implements ActionListener {
  private JControlPanel adaptee;
  JControlPanel_jRadioButton1_actionAdapter(JControlPanel adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jRadioButton1_actionPerformed(e);
  }
}
