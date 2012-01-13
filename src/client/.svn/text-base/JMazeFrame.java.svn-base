/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

/**
 *
 * @author user
 */
//import diet.task.mazegame.client.*;
//import diet.task.mazegame.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

public class JMazeFrame
    extends JFrame {
  JButton jButton1 = new JButton();
  JLabel jLabel1 = new JLabel("You Have received a new message");
  JMazePanel3 jPanel1 = new JMazePanel3(this);
  LabelTimer labTime ;
  boolean enteringCode = false;
  BorderLayout borderLayout1 = new BorderLayout();
  Vector keyPresses = new Vector();
  
 
  ClientMazeGameComms cms;
  


  public JMazeFrame() throws HeadlessException {
    super("Maze");
    try {
      jbInit();
      labTime = new LabelTimer(jLabel1);
      labTime.start();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void triggerNewMessage(){
    labTime.displayText("you have a new message");
  }

  

  public void setComms(ClientMazeGameComms cm) {
    cms = cm;
  }

  private void jbInit() throws Exception {
    this.setSize(new Dimension(600, 600));
    this.addComponentListener(new JMazeFrame_this_componentAdapter(this));
    this.addWindowStateListener(new JMazeFrame_this_windowStateAdapter(this));
    jButton1.setOpaque(false);
    jButton1.setText("open gate");
    jButton1.addActionListener(new JMazeFrame_jButton1_actionAdapter(this));
    jButton1.setFocusable(false);
    this.getContentPane().setLayout(borderLayout1);
    jPanel1.setOpaque(true);
    jPanel1.setPreferredSize(new Dimension(500, 500));
    jPanel1.setMinimumSize(new Dimension(500, 500));
    this.getContentPane().setBackground(Color.white);
    this.setCursor(null);
    this.addKeyListener(new JMazeFrame_this_keyAdapter(this));
    jLabel1.setOpaque(true);
    jLabel1.setText("");
    this.getContentPane().add(jPanel1, BorderLayout.CENTER);
    this.getContentPane().add(jLabel1, BorderLayout.SOUTH);

    this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    this.setVisible(true);
    this.pack();
  }

  public void keyPressFilter(KeyEvent e) {
   
    int i = e.getKeyCode();
    System.out.println("pressFILTER " + i);

    if (i == 17) {
      enteringCode = true;
      //System.out.println("press"+e.getKeyCode());

    }
    if (enteringCode) {
      //System.out.println("press"+e.getKeyCode());
      if (i == 37 || i == 38 || i == 39 ||
          i == 40) {
        jPanel1.moveCursor(i);

      }
    }
  }
 
public Vector getKeyPresses(){
   return keyPresses;
}

  public void keyReleaseFilter(KeyEvent e) {
    
    if (e.getKeyCode() == 17) {
      enteringCode = false;
      //System.out.println("go"+e.getKeyCode());

    }

     }

  String askUserName() {
    System.out.println("entering ask username " + new Date().getTime());
    String answer = "";
    while (answer.equalsIgnoreCase("")) {
      answer = (String) JOptionPane.showInputDialog("Choose a nickname");
    }
    System.out.println("leaving ask username " + new Date().getTime());
    return answer;
  }

  void jButton1_actionPerformed(ActionEvent e) {
    jPanel1.maz.gatesOpen = !jPanel1.maz.gatesOpen;

    jPanel1.paintComponent(jPanel1.getGraphics());
  }

  void this_keyPressed(KeyEvent e) {
    keyPressFilter(e);
  }

  void this_keyReleased(KeyEvent e) {
    keyReleaseFilter(e);
  }

  void this_windowStateChanged(WindowEvent e) {
    jPanel1.reCalculateWidthsandSizes();
    System.out.println("resize");
  }

  void this_componentResized(ComponentEvent e) {
    jPanel1.reCalculateWidthsandSizes();
    System.out.println("resize");
  }

}

class JMazeFrame_jButton1_actionAdapter
    implements java.awt.event.ActionListener {
  JMazeFrame adaptee;

  JMazeFrame_jButton1_actionAdapter(JMazeFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jButton1_actionPerformed(e);
  }
}

class JMazeFrame_this_keyAdapter
    extends java.awt.event.KeyAdapter {
  JMazeFrame adaptee;

  JMazeFrame_this_keyAdapter(JMazeFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void keyPressed(KeyEvent e) {
    adaptee.this_keyPressed(e);
  }

  public void keyReleased(KeyEvent e) {
    adaptee.this_keyReleased(e);
  }
}

class JMazeFrame_this_windowStateAdapter
    implements java.awt.event.WindowStateListener {
  JMazeFrame adaptee;

  JMazeFrame_this_windowStateAdapter(JMazeFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void windowStateChanged(WindowEvent e) {
    adaptee.this_windowStateChanged(e);
  }
}

class JMazeFrame_this_componentAdapter
    extends java.awt.event.ComponentAdapter {
  JMazeFrame adaptee;

  JMazeFrame_this_componentAdapter(JMazeFrame adaptee) {
    this.adaptee = adaptee;
  }

  public void componentResized(ComponentEvent e) {
    adaptee.this_componentResized(e);
  }
}

class LabelTimer extends Thread {
  JLabel label;
  String t1;
  String t2;
  boolean isOperating=true;
  long mostrecent = 0;

  public LabelTimer(JLabel jl){
    super("");
    label = jl;

  }

  public synchronized void displayText(String t){
     System.out.println("runA");
     label.setText(t);
     System.out.println("runB");
     mostrecent= new Date().getTime();
  }

  public void run(){
    try{
    while(isOperating){
      if(mostrecent==0)sleep(100);
      else {
          sleep(3500);
          label.setText("");
          mostrecent=0;
        }

    }

    } catch (Exception e){
        System.out.println("JLabel error" + e.getMessage());
    }

  }

}



