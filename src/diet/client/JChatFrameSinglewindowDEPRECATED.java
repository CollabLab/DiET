package diet.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

public class JChatFrameSinglewindowDEPRECATED extends JChatFrame  {
  JLabel jLabel1 = new JLabel();
  JScrollPane jScrollPane1 = new JScrollPane();
  JScrollPane jScrollPane2 = new JScrollPane();
  JTextArea jEnterTextArea = new JTextArea();
  JButton jSendButton = new JButton();
  JTextArea jChatPane = new JTextArea();
  ConnectionToServer cts;
  JPanel jPanel1 = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();



  public JChatFrameSinglewindowDEPRECATED(ClientEventHandler clevh) throws HeadlessException {
    super(clevh);
    try {
        jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void setConnectionToServer(ConnectionToServer cts) {
    this.cts = cts;
  }

  private void jbInit() throws Exception {
    jLabel1.setFont(new java.awt.Font("Dialog", 0, 12));
    jLabel1.setText("Network Status : OK");
    this.getContentPane().setLayout(borderLayout1);
    jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.
                                              HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPane1.setBorder(BorderFactory.createLoweredBevelBorder());
    jScrollPane1.setMinimumSize(new Dimension(615, 25));
    jScrollPane1.setPreferredSize(new Dimension(615, 200));
    jScrollPane2.setHorizontalScrollBarPolicy(JScrollPane.
                                              HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPane2.setBounds(new Rectangle(7, 6, 526, 64));
    jScrollPane1.setFocusable(false);
    this.jChatPane.setLineWrap(true);
    jEnterTextArea.setEnabled(false);
    jEnterTextArea.setText("");
    jEnterTextArea.setLineWrap(true);
    jEnterTextArea.addKeyListener(new JChatFrameKeyEventListener());
    jSendButton.setBounds(new Rectangle(538, 6, 73, 64));
    jSendButton.setEnabled(false);
    jSendButton.setRequestFocusEnabled(false);
    jSendButton.setFocusable(false);
    jSendButton.setToolTipText("");
    jSendButton.setText("SEND");
    jSendButton.addActionListener(new JChatFrameSENDButtonActionListener());
    jChatPane.setFocusable(false);
    this.addKeyListener(new JChatFrameKeyEventListener());
    jPanel1.setLayout(null);
    jPanel1.setMinimumSize(new Dimension(615, 80));
    jPanel1.setPreferredSize(new Dimension(615, 80));
    this.setResizable(false);
    this.getContentPane().add(jLabel1, BorderLayout.WEST);
    this.getContentPane().add(jScrollPane1, BorderLayout.NORTH);
    this.getContentPane().add(jPanel1, BorderLayout.SOUTH);
    jScrollPane1.getViewport().add(jChatPane, null);
    jPanel1.add(jScrollPane2, null);
    jPanel1.add(jSendButton, null);
    jScrollPane2.getViewport().add(jEnterTextArea, null);
    this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    this.pack();
    this.validate();
    this.setVisible(true);
  }

  public String getTextEnteredInField(){
      return jEnterTextArea.getText();
  }

  public void setTextEntryField(String text){
      jEnterTextArea.setText(text);
  }



  void sendButtonPressed(ActionEvent e) {
    getClientEventHandler().sendButtonPressed();
  }

  public void sendActionReturnPressed() {
    getClientEventHandler().returnPressed();
  }

  void keyPressed(KeyEvent e) {
    getClientEventHandler().keyPressFilter(e);
  }

  void keyReleased(KeyEvent e) {
    this.getClientEventHandler().keyReleaseFilter(e);
  }


  public void setTextEntryAndSendEnabled(boolean enable){
    System.out.println("Setting up send enabled1");
    this.jSendButton.setEnabled(enable);
    System.out.println("Setting up send enabled2");
    this.jEnterTextArea.setEnabled(enable);
    this.jEnterTextArea.setEditable(enable);
    System.out.println("Setting up send enabled3");
 }


  public void appendWithCaretCheck(String s) {
    jChatPane.append(s);
    jChatPane.validate();
    jScrollPane1.repaint();
    jScrollPane1.validate();

    jScrollPane1.getVerticalScrollBar().setValue(jScrollPane1.
                                                 getVerticalScrollBar().
                                                 getMaximum());
    jScrollPane1.getVerticalScrollBar().setUnitIncrement(2);
  jChatPane.validate();
  jChatPane.repaint();
  jScrollPane1.repaint();
  jScrollPane1.validate();
  this.validate();
  this.repaint();
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
    getClientEventHandler().sendButtonPressed();
  }
}




}



