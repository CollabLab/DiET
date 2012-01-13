/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * JInterfacePanel.java
 *
 * Created on Nov 29, 2011, 10:38:57 AM
 */

package diet.task.react.ui;

import diet.task.react.ClientTaskReactEventHandler;
import diet.task.react.MessageChangeColourOfButton;
import diet.task.react.ReactTaskController;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.util.Date;
import java.util.Hashtable;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

/**
 *
 * @author sre
 */
public class JInterfacePanel extends javax.swing.JPanel {

    ReactTaskController rtc;
    ClientTaskReactEventHandler ctreh;

    JButton[][] jButtons = new JButton[5][5] ;

    //Border inactive = BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.background"), 3);
    //Border active = BorderFactory.createLineBorder(Color.RED, 5);

    //JButton activeBorderButton = null;

    JTextArea jt;

    Hashtable borders = new Hashtable();


    public long lastMouseSelectBySelf ;
    public long lastMouseReleaseBySelf ;


    public long lastIncomingSelectByOther;
    public long lastIncomingReleaseByOther;

    int lastSelectByOtherX = -1;
    int lastSelectByOtherY =-1;
    String lastSelectByOTher = "";

    public Border getBorder(Color c){
        Border brd = (Border)borders.get(c);
        if(brd!=null)return brd;
        brd = BorderFactory.createLineBorder(c, 8);
        borders.put(c, brd);
        return brd;
    }




     /** Creates new form JInterfacePanel */
    public JInterfacePanel(ClientTaskReactEventHandler ctreh) {
        initComponents();
        this.ctreh = ctreh;
       initButtons();
       //this.pack();
       this.setVisible(true);
       //this.setUniqueBorderSelected(0, 0,true);
       //System.exit(-4);
       initCardSpace();
       //setDisableAndShowMessage("TESTMESSAGE");
       //this.reEnableShowGrid();

       Thread t = new Thread(){
           public void run(){
               Random r = new Random();
               while(2<4){
                   try{
                      Thread.sleep(1000);
                      JButton jb = jButtons[r.nextInt(5)][r.nextInt(5)];
                      jb.setText(""+r.nextInt(600));
                      System.out.println("RANDMCHANGE");
                      jb.setSelected(true);
                      jb.setBackground(Color.red);
                      jb.repaint();
                   }catch (Exception e){

                   }
               }
           }
       };
       //t.start();

    }


    public JInterfacePanel(ReactTaskController rtc) {
        this.rtc=rtc;
        initComponents();
        initButtons();
        this.setVisible(true);
        initCardSpace();
    }


    /** Creates new form JInterfacePanel */
    public JInterfacePanel() {
        initComponents();
        initButtons();
        this.setVisible(true);
        initCardSpace();
    }





        public void initCardSpace(){

       CardLayout cl = (CardLayout)this.getLayout();
       JPanel jp = new JPanel();
       jp.setLayout(new BorderLayout());
       jt = new JTextArea("");
       Font curFont = jt.getFont();
       jt.setFont(new Font(curFont.getFontName(), curFont.getStyle(), 30));
       jp.add(jt, BorderLayout.CENTER);
       jp.setVisible(true);
       this.add(jp, "MESSAGE");
   }


    public void initButtons(){
        jButtons[0][0]= jButton00;   jButtons[1][0]= jButton10;   jButtons[2][0]= jButton20;      jButtons[3][0]= jButton30;    jButtons[4][0] = jButton40;
        jButtons[0][1]= jButton01;   jButtons[1][1]= jButton11;   jButtons[2][1]= jButton21;      jButtons[3][1]= jButton31;    jButtons[4][1] = jButton41;
        jButtons[0][2]= jButton02;   jButtons[1][2]= jButton12;   jButtons[2][2]= jButton22;      jButtons[3][2]= jButton32;    jButtons[4][2] = jButton42;
        jButtons[0][3]= jButton03;   jButtons[1][3]= jButton13;   jButtons[2][3]= jButton23;      jButtons[3][3]= jButton33;    jButtons[4][3] = jButton43;
        jButtons[0][4]= jButton04;   jButtons[1][4]= jButton14;   jButtons[2][4]= jButton24;      jButtons[3][4]= jButton34;    jButtons[4][4] = jButton44;

        for(int i=0;i<jButtons.length;i++){
            JButton[] jb = jButtons[i];
            for(int j=0;j<jb.length;j++){
                jButtons[i][j].setBorder(this.getBorder(Color.DARK_GRAY));
            }
        }

    }







    public void setUniqueBorderSelected(final int x,final int y, final Color c, final int selectionSTATUS){
        final JInterfacePanel ip =this;
        SwingUtilities.invokeLater( new Runnable(){public void run(){
                 JButton jb = jButtons[x][y];    
                 if (selectionSTATUS==MessageChangeColourOfButton.senderHasSELECTED){
                     ip.lastIncomingSelectByOther = new Date().getTime();
                     jb.setSelected(true);
                     ip.lastSelectByOTher = jb.getText();
                     ip.lastSelectByOtherX=x;
                     ip.lastSelectByOtherY=y;
                 }
                 else if (selectionSTATUS==MessageChangeColourOfButton.senderHasDESELECTED){
                     ip.lastIncomingReleaseByOther = new Date().getTime();
                 }
                 jb.setBorder(ip.getBorder(c));




            }
        }
        );
    }


    public void setUniqueForegroundSelected(final int x,final int y, final Color c, final int selectionSTATUS){
       
        final JInterfacePanel ip =this;
        SwingUtilities.invokeLater( new Runnable(){public void run(){
                 JButton jb = jButtons[x][y];
                 if (selectionSTATUS==MessageChangeColourOfButton.senderHasSELECTED){
                     ip.lastIncomingSelectByOther = new Date().getTime();
                     jb.setSelected(true);
                     ip.lastSelectByOTher = jb.getText();
                     ip.lastSelectByOtherX=x;
                     ip.lastSelectByOtherY=y;
                 }
                 else if (selectionSTATUS==MessageChangeColourOfButton.senderHasDESELECTED){
                     ip.lastIncomingReleaseByOther = new Date().getTime();
                 }
                 jb.setForeground(c);
                 
            }
        }
        );
    }


    
    public void setUniqueBackgroundSelected(final int x,final int y, final Color c, final int selectionSTATUS){
        

        final JInterfacePanel ip =this;
        SwingUtilities.invokeLater( new Runnable(){public void run(){
                 JButton jb = jButtons[x][y];
                 
                 if (selectionSTATUS==MessageChangeColourOfButton.senderHasSELECTED){
                     ip.lastIncomingSelectByOther = new Date().getTime();
                     jb.setSelected(true);
                     ip.lastSelectByOTher = jb.getText();
                     ip.lastSelectByOtherX=x;
                     ip.lastSelectByOtherY=y;
                 }
                 else if (selectionSTATUS==MessageChangeColourOfButton.senderHasDESELECTED){
                     ip.lastIncomingReleaseByOther = new Date().getTime();
                 }
                 jb.setBackground(c);
                
            }
        }
        );
    }


     public void changeColourOfWord(String word, final Color foreground, final Color background){
       
       final JInterfacePanel ip =this;
       Dimension d = getDimensionForName(word);
       if(d.width<0|d.height<0){
            if(ctreh!=null)ctreh.cts.sendErrorMessage("ERROR..DIMENSION WIDTH IS"+d.width+"...."+d.height);
            return;
       }
       final JButton jb = jButtons[d.width][d.height];
       jb.setBackground(Color.pink);//System.exit(-5);
       SwingUtilities.invokeLater( new Runnable(){public void run(){
               jb.setForeground(foreground);
               jb.setBackground(background);
             }
        }
        );
       
    }


      public void changeColourOfButtonXY(int x, int y, final Color foreground, final Color background, final boolean ignoreXYAndCHANGEALL){
          //System.exit(-4);
          final JInterfacePanel ip =this;
          if(x<0|x<0){
            if(ctreh!=null)ctreh.cts.sendErrorMessage("ERROR..DIMENSION WIDTH IS"+x+"...."+x);
            return;
       }
       final JButton jb = jButtons[x][y];
       jb.setBackground(Color.pink);//System.exit(-5);
       SwingUtilities.invokeLater( new Runnable(){public void run(){
               //jb.setOpaque(true);
               jb.setForeground(foreground);
               jb.setBackground(background);

               if(!ignoreXYAndCHANGEALL) return;

               for(int i=0;i<jButtons.length;i++){
                    for(int j=0;j<jButtons[i].length;j++){
                    JButton jbb = jButtons[i][j];
                    if(jbb!=null){
                        jbb.setForeground(foreground);
                        jbb.setBackground(background);
                    }

                }
            }




               

             }
        }
        );

    }







     public void setDisableAndShowMessage(final String message){
         final JInterfacePanel ip =this;
         final JTextArea jml = jt;
         SwingUtilities.invokeLater( new Runnable(){public void run(){

                  CardLayout cl = (CardLayout)ip.getLayout();
                  jt.setText(message);

                  cl.show(ip,"MESSAGE");

                  //focusONSQUARE(0,0);
                  //setUniqueForegroundSelected(0,0, true);
             }
        }
        );
     }





     public void reEnableShowGrid(){
           final JInterfacePanel ip =this;
         //final JLabel jml = jl;
         SwingUtilities.invokeLater( new Runnable(){public void run(){
              CardLayout cl = (CardLayout)ip.getLayout();
              cl.show(ip,"card2");
          }
        }
        );
     }


     public String getButtonname(int x,int y){
         JButton jb = jButtons[x][y];
         return jb.getText();
     }


     public Dimension getDimensionForName(String s){
          for(int i=0;i<jButtons.length;i++){
                JButton[] jb = jButtons[i];
                for(int j=0;j<jb.length;j++){
                    JButton jbb = jButtons[i][j];
                    if(jbb!=null){
                        if (jbb.getText()!=null){
                            if(jButtons[i][j].getText().equalsIgnoreCase(s))return new Dimension (i,j);
                        }
                    }

                }
            }
           return new Dimension(-1,-1);
     }


     public void changeButtonSet(final String[][] names){
         SwingUtilities.invokeLater( new Runnable(){public void run(){
            for(int i=0;i<jButtons.length;i++){
                JButton[] jb = jButtons[i];
                for(int j=0;j<jb.length;j++){
                    //jButtons[i][j].setName("NAMESET");
                    jButtons[i][j].setText(names[i][j]);
                    //System.exit(-5);
                }
            }

         }
        }
        );
     }


















    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton03 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton43 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton04 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton40 = new javax.swing.JButton();
        jButton32 = new javax.swing.JButton();
        jButton23 = new javax.swing.JButton();
        jButton02 = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jButton24 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton42 = new javax.swing.JButton();
        jButton01 = new javax.swing.JButton();
        jButton33 = new javax.swing.JButton();
        jButton44 = new javax.swing.JButton();
        jButton41 = new javax.swing.JButton();
        jButton00 = new javax.swing.JButton();
        jButton34 = new javax.swing.JButton();
        jButton30 = new javax.swing.JButton();
        jButton31 = new javax.swing.JButton();

        setLayout(new java.awt.CardLayout());

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton03.setBackground(new java.awt.Color(0, 0, 0));
        jButton03.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jButton03.setForeground(new java.awt.Color(255, 255, 255));
        jButton03.setText("03");
        jButton03.setBorder(new javax.swing.border.LineBorder(javax.swing.UIManager.getDefaults().getColor("Button.background"), 5, true));
        jButton03.setMaximumSize(new java.awt.Dimension(100, 100));
        jButton03.setMinimumSize(new java.awt.Dimension(100, 100));
        jButton03.setPreferredSize(new java.awt.Dimension(100, 100));
        jButton03.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton03clicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton03MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton03MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton03MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton03MouseReleased(evt);
            }
        });
        jButton03.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton03gridactionperformed(evt);
            }
        });
        jPanel1.add(jButton03, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, -1, -1));

        jButton14.setBackground(new java.awt.Color(0, 0, 0));
        jButton14.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jButton14.setForeground(new java.awt.Color(255, 255, 255));
        jButton14.setText("14");
        jButton14.setBorder(new javax.swing.border.LineBorder(javax.swing.UIManager.getDefaults().getColor("Button.background"), 5, true));
        jButton14.setMaximumSize(new java.awt.Dimension(100, 100));
        jButton14.setMinimumSize(new java.awt.Dimension(100, 100));
        jButton14.setPreferredSize(new java.awt.Dimension(100, 100));
        jButton14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton14MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton14MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton14MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton14MouseReleased(evt);
            }
        });
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14gridactionperformed(evt);
            }
        });
        jPanel1.add(jButton14, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 0, -1, -1));

        jButton43.setBackground(new java.awt.Color(0, 0, 0));
        jButton43.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jButton43.setForeground(new java.awt.Color(255, 255, 255));
        jButton43.setText("43");
        jButton43.setBorder(new javax.swing.border.LineBorder(javax.swing.UIManager.getDefaults().getColor("Button.background"), 5, true));
        jButton43.setMaximumSize(new java.awt.Dimension(100, 100));
        jButton43.setMinimumSize(new java.awt.Dimension(100, 100));
        jButton43.setPreferredSize(new java.awt.Dimension(100, 100));
        jButton43.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton43clicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton43MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton43MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton43MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton43MouseReleased(evt);
            }
        });
        jButton43.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton43gridactionperformed(evt);
            }
        });
        jPanel1.add(jButton43, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 100, -1, -1));

        jButton12.setBackground(new java.awt.Color(0, 0, 0));
        jButton12.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jButton12.setForeground(new java.awt.Color(255, 255, 255));
        jButton12.setText("12");
        jButton12.setBorder(new javax.swing.border.LineBorder(javax.swing.UIManager.getDefaults().getColor("Button.background"), 5, true));
        jButton12.setMaximumSize(new java.awt.Dimension(100, 100));
        jButton12.setMinimumSize(new java.awt.Dimension(100, 100));
        jButton12.setPreferredSize(new java.awt.Dimension(100, 100));
        jButton12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton12clicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton12MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton12MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton12MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton12MouseReleased(evt);
            }
        });
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12gridactionperformed(evt);
            }
        });
        jPanel1.add(jButton12, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 200, -1, -1));

        jButton20.setBackground(new java.awt.Color(0, 0, 0));
        jButton20.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jButton20.setForeground(new java.awt.Color(255, 255, 255));
        jButton20.setText("20");
        jButton20.setBorder(new javax.swing.border.LineBorder(javax.swing.UIManager.getDefaults().getColor("Button.background"), 5, true));
        jButton20.setMaximumSize(new java.awt.Dimension(100, 100));
        jButton20.setMinimumSize(new java.awt.Dimension(100, 100));
        jButton20.setPreferredSize(new java.awt.Dimension(100, 100));
        jButton20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton20clicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton20MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton20MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton20MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton20MouseReleased(evt);
            }
        });
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20gridactionperformed(evt);
            }
        });
        jPanel1.add(jButton20, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 400, -1, -1));

        jButton10.setBackground(new java.awt.Color(0, 0, 0));
        jButton10.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jButton10.setForeground(new java.awt.Color(255, 255, 255));
        jButton10.setText("10");
        jButton10.setBorder(new javax.swing.border.LineBorder(javax.swing.UIManager.getDefaults().getColor("Button.background"), 5, true));
        jButton10.setMaximumSize(new java.awt.Dimension(100, 100));
        jButton10.setMinimumSize(new java.awt.Dimension(100, 100));
        jButton10.setPreferredSize(new java.awt.Dimension(100, 100));
        jButton10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton10clicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton10MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton10MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton10MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton10MouseReleased(evt);
            }
        });
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10gridactionperformed(evt);
            }
        });
        jPanel1.add(jButton10, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 400, -1, -1));

        jButton04.setBackground(new java.awt.Color(0, 0, 0));
        jButton04.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jButton04.setForeground(new java.awt.Color(255, 255, 255));
        jButton04.setText("04");
        jButton04.setBorder(new javax.swing.border.LineBorder(javax.swing.UIManager.getDefaults().getColor("Button.background"), 5, true));
        jButton04.setMaximumSize(new java.awt.Dimension(100, 100));
        jButton04.setMinimumSize(new java.awt.Dimension(100, 100));
        jButton04.setPreferredSize(new java.awt.Dimension(100, 100));
        jButton04.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton04MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton04MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton04MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton04MouseReleased(evt);
            }
        });
        jButton04.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton04gridactionperformed(evt);
            }
        });
        jPanel1.add(jButton04, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jButton21.setBackground(new java.awt.Color(0, 0, 0));
        jButton21.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jButton21.setForeground(new java.awt.Color(255, 255, 255));
        jButton21.setText("21");
        jButton21.setBorder(new javax.swing.border.LineBorder(javax.swing.UIManager.getDefaults().getColor("Button.background"), 5, true));
        jButton21.setMaximumSize(new java.awt.Dimension(100, 100));
        jButton21.setMinimumSize(new java.awt.Dimension(100, 100));
        jButton21.setPreferredSize(new java.awt.Dimension(100, 100));
        jButton21.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton21clicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton21MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton21MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton21MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton21MouseReleased(evt);
            }
        });
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21gridactionperformed(evt);
            }
        });
        jPanel1.add(jButton21, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 300, -1, -1));

        jButton13.setBackground(new java.awt.Color(0, 0, 0));
        jButton13.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jButton13.setForeground(new java.awt.Color(255, 255, 255));
        jButton13.setText("13");
        jButton13.setBorder(new javax.swing.border.LineBorder(javax.swing.UIManager.getDefaults().getColor("Button.background"), 5, true));
        jButton13.setMaximumSize(new java.awt.Dimension(100, 100));
        jButton13.setMinimumSize(new java.awt.Dimension(100, 100));
        jButton13.setPreferredSize(new java.awt.Dimension(100, 100));
        jButton13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton13clicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton13MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton13MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton13MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton13MouseReleased(evt);
            }
        });
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13gridactionperformed(evt);
            }
        });
        jPanel1.add(jButton13, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 100, -1, -1));

        jButton40.setBackground(new java.awt.Color(0, 0, 0));
        jButton40.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jButton40.setForeground(new java.awt.Color(255, 255, 255));
        jButton40.setText("40");
        jButton40.setBorder(new javax.swing.border.LineBorder(javax.swing.UIManager.getDefaults().getColor("Button.background"), 5, true));
        jButton40.setMaximumSize(new java.awt.Dimension(100, 100));
        jButton40.setMinimumSize(new java.awt.Dimension(100, 100));
        jButton40.setPreferredSize(new java.awt.Dimension(100, 100));
        jButton40.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton40clicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton40MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton40MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton40MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton40MouseReleased(evt);
            }
        });
        jButton40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton40gridactionperformed(evt);
            }
        });
        jPanel1.add(jButton40, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 400, -1, -1));

        jButton32.setBackground(new java.awt.Color(0, 0, 0));
        jButton32.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jButton32.setForeground(new java.awt.Color(255, 255, 255));
        jButton32.setText("32");
        jButton32.setBorder(new javax.swing.border.LineBorder(javax.swing.UIManager.getDefaults().getColor("Button.background"), 5, true));
        jButton32.setMaximumSize(new java.awt.Dimension(100, 100));
        jButton32.setMinimumSize(new java.awt.Dimension(100, 100));
        jButton32.setPreferredSize(new java.awt.Dimension(100, 100));
        jButton32.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton32clicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton32MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton32MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton32MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton32MouseReleased(evt);
            }
        });
        jButton32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton32gridactionperformed(evt);
            }
        });
        jPanel1.add(jButton32, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 200, -1, -1));

        jButton23.setBackground(new java.awt.Color(0, 0, 0));
        jButton23.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jButton23.setForeground(new java.awt.Color(255, 255, 255));
        jButton23.setText("23");
        jButton23.setBorder(new javax.swing.border.LineBorder(javax.swing.UIManager.getDefaults().getColor("Button.background"), 5, true));
        jButton23.setMaximumSize(new java.awt.Dimension(100, 100));
        jButton23.setMinimumSize(new java.awt.Dimension(100, 100));
        jButton23.setPreferredSize(new java.awt.Dimension(100, 100));
        jButton23.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton23clicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton23MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton23MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton23MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton23MouseReleased(evt);
            }
        });
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23gridactionperformed(evt);
            }
        });
        jPanel1.add(jButton23, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 100, -1, -1));

        jButton02.setBackground(new java.awt.Color(0, 0, 0));
        jButton02.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jButton02.setForeground(new java.awt.Color(255, 255, 255));
        jButton02.setText("02");
        jButton02.setBorder(new javax.swing.border.LineBorder(javax.swing.UIManager.getDefaults().getColor("Button.background"), 5, true));
        jButton02.setMaximumSize(new java.awt.Dimension(100, 100));
        jButton02.setMinimumSize(new java.awt.Dimension(100, 100));
        jButton02.setPreferredSize(new java.awt.Dimension(100, 100));
        jButton02.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton02clicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton02MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton02MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton02MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton02MouseReleased(evt);
            }
        });
        jButton02.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton02gridactionperformed(evt);
            }
        });
        jPanel1.add(jButton02, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, -1, -1));

        jButton22.setBackground(new java.awt.Color(0, 0, 0));
        jButton22.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jButton22.setForeground(new java.awt.Color(255, 255, 255));
        jButton22.setText("22");
        jButton22.setBorder(new javax.swing.border.LineBorder(javax.swing.UIManager.getDefaults().getColor("Button.background"), 5, true));
        jButton22.setMaximumSize(new java.awt.Dimension(100, 100));
        jButton22.setMinimumSize(new java.awt.Dimension(100, 100));
        jButton22.setPreferredSize(new java.awt.Dimension(100, 100));
        jButton22.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton22clicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton22MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton22MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton22MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton22MouseReleased(evt);
            }
        });
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22gridactionperformed(evt);
            }
        });
        jPanel1.add(jButton22, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 200, -1, -1));

        jButton24.setBackground(new java.awt.Color(0, 0, 0));
        jButton24.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jButton24.setForeground(new java.awt.Color(255, 255, 255));
        jButton24.setText("24");
        jButton24.setBorder(new javax.swing.border.LineBorder(javax.swing.UIManager.getDefaults().getColor("Button.background"), 5, true));
        jButton24.setMaximumSize(new java.awt.Dimension(100, 100));
        jButton24.setMinimumSize(new java.awt.Dimension(100, 100));
        jButton24.setPreferredSize(new java.awt.Dimension(100, 100));
        jButton24.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton24MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton24MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton24MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton24MouseReleased(evt);
            }
        });
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24gridactionperformed(evt);
            }
        });
        jPanel1.add(jButton24, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 0, -1, -1));

        jButton11.setBackground(new java.awt.Color(0, 0, 0));
        jButton11.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jButton11.setForeground(new java.awt.Color(255, 255, 255));
        jButton11.setText("11");
        jButton11.setBorder(new javax.swing.border.LineBorder(javax.swing.UIManager.getDefaults().getColor("Button.background"), 5, true));
        jButton11.setMaximumSize(new java.awt.Dimension(100, 100));
        jButton11.setMinimumSize(new java.awt.Dimension(100, 100));
        jButton11.setPreferredSize(new java.awt.Dimension(100, 100));
        jButton11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton11clicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton11MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton11MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton11MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton11MouseReleased(evt);
            }
        });
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11gridactionperformed(evt);
            }
        });
        jPanel1.add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 300, -1, -1));

        jButton42.setBackground(new java.awt.Color(0, 0, 0));
        jButton42.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jButton42.setForeground(new java.awt.Color(255, 255, 255));
        jButton42.setText("42");
        jButton42.setBorder(new javax.swing.border.LineBorder(javax.swing.UIManager.getDefaults().getColor("Button.background"), 5, true));
        jButton42.setMaximumSize(new java.awt.Dimension(100, 100));
        jButton42.setMinimumSize(new java.awt.Dimension(100, 100));
        jButton42.setPreferredSize(new java.awt.Dimension(100, 100));
        jButton42.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton42clicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton42MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton42MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton42MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton42MouseReleased(evt);
            }
        });
        jButton42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton42gridactionperformed(evt);
            }
        });
        jPanel1.add(jButton42, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 200, -1, -1));

        jButton01.setBackground(new java.awt.Color(0, 0, 0));
        jButton01.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jButton01.setForeground(new java.awt.Color(255, 255, 255));
        jButton01.setText("01");
        jButton01.setBorder(new javax.swing.border.LineBorder(javax.swing.UIManager.getDefaults().getColor("Button.background"), 5, true));
        jButton01.setMaximumSize(new java.awt.Dimension(100, 100));
        jButton01.setMinimumSize(new java.awt.Dimension(100, 100));
        jButton01.setPreferredSize(new java.awt.Dimension(100, 100));
        jButton01.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton01clicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton01MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton01MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton01MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton01MouseReleased(evt);
            }
        });
        jButton01.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton01gridactionperformed(evt);
            }
        });
        jPanel1.add(jButton01, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 300, -1, -1));

        jButton33.setBackground(new java.awt.Color(0, 0, 0));
        jButton33.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jButton33.setForeground(new java.awt.Color(255, 255, 255));
        jButton33.setText("33");
        jButton33.setBorder(new javax.swing.border.LineBorder(javax.swing.UIManager.getDefaults().getColor("Button.background"), 5, true));
        jButton33.setMaximumSize(new java.awt.Dimension(100, 100));
        jButton33.setMinimumSize(new java.awt.Dimension(100, 100));
        jButton33.setPreferredSize(new java.awt.Dimension(100, 100));
        jButton33.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton33clicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton33MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton33MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton33MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton33MouseReleased(evt);
            }
        });
        jButton33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton33gridactionperformed(evt);
            }
        });
        jPanel1.add(jButton33, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 100, -1, -1));

        jButton44.setBackground(new java.awt.Color(0, 0, 0));
        jButton44.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jButton44.setForeground(new java.awt.Color(255, 255, 255));
        jButton44.setText("44");
        jButton44.setBorder(new javax.swing.border.LineBorder(javax.swing.UIManager.getDefaults().getColor("Button.background"), 5, true));
        jButton44.setMaximumSize(new java.awt.Dimension(100, 100));
        jButton44.setMinimumSize(new java.awt.Dimension(100, 100));
        jButton44.setPreferredSize(new java.awt.Dimension(100, 100));
        jButton44.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton44MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton44MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton44MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton44MouseReleased(evt);
            }
        });
        jButton44.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton44gridactionperformed(evt);
            }
        });
        jPanel1.add(jButton44, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 0, -1, -1));

        jButton41.setBackground(new java.awt.Color(0, 0, 0));
        jButton41.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jButton41.setForeground(new java.awt.Color(255, 255, 255));
        jButton41.setText("41");
        jButton41.setBorder(new javax.swing.border.LineBorder(javax.swing.UIManager.getDefaults().getColor("Button.background"), 5, true));
        jButton41.setMaximumSize(new java.awt.Dimension(100, 100));
        jButton41.setMinimumSize(new java.awt.Dimension(100, 100));
        jButton41.setPreferredSize(new java.awt.Dimension(100, 100));
        jButton41.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton41clicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton41MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton41MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton41MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton41MouseReleased(evt);
            }
        });
        jButton41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton41gridactionperformed(evt);
            }
        });
        jPanel1.add(jButton41, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 300, -1, -1));

        jButton00.setBackground(new java.awt.Color(0, 0, 0));
        jButton00.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jButton00.setForeground(new java.awt.Color(255, 255, 255));
        jButton00.setText("00");
        jButton00.setBorder(new javax.swing.border.LineBorder(javax.swing.UIManager.getDefaults().getColor("Button.background"), 5, true));
        jButton00.setMaximumSize(new java.awt.Dimension(100, 100));
        jButton00.setMinimumSize(new java.awt.Dimension(100, 100));
        jButton00.setPreferredSize(new java.awt.Dimension(100, 100));
        jButton00.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton00clicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton00MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton00MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton00MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton00MouseReleased(evt);
            }
        });
        jButton00.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton00gridactionperformed(evt);
            }
        });
        jPanel1.add(jButton00, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 400, -1, -1));

        jButton34.setBackground(new java.awt.Color(0, 0, 0));
        jButton34.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jButton34.setForeground(new java.awt.Color(255, 255, 255));
        jButton34.setText("34");
        jButton34.setBorder(new javax.swing.border.LineBorder(javax.swing.UIManager.getDefaults().getColor("Button.background"), 5, true));
        jButton34.setMaximumSize(new java.awt.Dimension(100, 100));
        jButton34.setMinimumSize(new java.awt.Dimension(100, 100));
        jButton34.setPreferredSize(new java.awt.Dimension(100, 100));
        jButton34.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton34MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton34MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton34MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton34MouseReleased(evt);
            }
        });
        jButton34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton34gridactionperformed(evt);
            }
        });
        jPanel1.add(jButton34, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 0, -1, -1));

        jButton30.setBackground(new java.awt.Color(0, 0, 0));
        jButton30.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jButton30.setForeground(new java.awt.Color(255, 255, 255));
        jButton30.setText("30");
        jButton30.setBorder(new javax.swing.border.LineBorder(javax.swing.UIManager.getDefaults().getColor("Button.background"), 5, true));
        jButton30.setMaximumSize(new java.awt.Dimension(100, 100));
        jButton30.setMinimumSize(new java.awt.Dimension(100, 100));
        jButton30.setPreferredSize(new java.awt.Dimension(100, 100));
        jButton30.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton30clicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton30MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton30MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton30MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton30MouseReleased(evt);
            }
        });
        jButton30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton30gridactionperformed(evt);
            }
        });
        jPanel1.add(jButton30, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 400, -1, -1));

        jButton31.setBackground(new java.awt.Color(0, 0, 0));
        jButton31.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jButton31.setForeground(new java.awt.Color(255, 255, 255));
        jButton31.setText("31");
        jButton31.setBorder(new javax.swing.border.LineBorder(javax.swing.UIManager.getDefaults().getColor("Button.background"), 5, true));
        jButton31.setMaximumSize(new java.awt.Dimension(100, 100));
        jButton31.setMinimumSize(new java.awt.Dimension(100, 100));
        jButton31.setPreferredSize(new java.awt.Dimension(100, 100));
        jButton31.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton31clicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButton31MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButton31MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton31MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jButton31MouseReleased(evt);
            }
        });
        jButton31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton31gridactionperformed(evt);
            }
        });
        jPanel1.add(jButton31, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 300, -1, -1));

        add(jPanel1, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void jButton00clicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton00clicked
        // TODO add your handling code here:
        //System.exit(-5);
}//GEN-LAST:event_jButton00clicked

    private void jButton00MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton00MouseEntered
        // TODO add your handling code here:
        this.handleGUIEvent(evt,0,0);
}//GEN-LAST:event_jButton00MouseEntered

    private void jButton00MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton00MouseExited
        // TODO add your handling code here:
        this.handleGUIEvent(evt,0,0);
}//GEN-LAST:event_jButton00MouseExited

    private void jButton00MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton00MousePressed
        // TODO add your handling code here:
        this.buttonSelected2(jButton00,0,0);



    }//GEN-LAST:event_jButton00MousePressed

    private void jButton00MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton00MouseReleased
        // TODO add your handling code here:
        this.buttonReleased(jButton00,0,0);
}//GEN-LAST:event_jButton00MouseReleased

    private void jButton00gridactionperformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton00gridactionperformed
        // TODO add your handling code here:
        // System.exit(-5);
        //JButton jb = (JButton)evt.getSource();
        //jb.setBackground(Color.red);
        //Toolkit.getDefaultToolkit().
}//GEN-LAST:event_jButton00gridactionperformed

    private void jButton10clicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton10clicked
        // TODO add your handling code here:
        //System.exit(-5);
}//GEN-LAST:event_jButton10clicked

    private void jButton10MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton10MouseEntered
        // TODO add your handling code here:
        this.handleGUIEvent(evt,1,0);
}//GEN-LAST:event_jButton10MouseEntered

    private void jButton10MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton10MouseExited
        // TODO add your handling code here:
        this.handleGUIEvent(evt,1,0);
}//GEN-LAST:event_jButton10MouseExited

    private void jButton10MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton10MousePressed
        // TODO add your handling code here:
        this.buttonSelected2(jButton10,1,0);
}//GEN-LAST:event_jButton10MousePressed

    private void jButton10MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton10MouseReleased
        // TODO add your handling code here:
        this.buttonReleased(jButton10,1,0);
}//GEN-LAST:event_jButton10MouseReleased

    private void jButton10gridactionperformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10gridactionperformed
        // TODO add your handling code here:
        // System.exit(-5);
        //JButton jb = (JButton)evt.getSource();
        //jb.setBackground(Color.red);
        //Toolkit.getDefaultToolkit().
}//GEN-LAST:event_jButton10gridactionperformed

    private void jButton20clicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton20clicked
        // TODO add your handling code here:
        //System.exit(-5);
}//GEN-LAST:event_jButton20clicked

    private void jButton20MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton20MouseEntered
        // TODO add your handling code here:
        this.handleGUIEvent(evt,2,0);
}//GEN-LAST:event_jButton20MouseEntered

    private void jButton20MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton20MouseExited
        // TODO add your handling code here:
        this.handleGUIEvent(evt,2,0);
}//GEN-LAST:event_jButton20MouseExited

    private void jButton20MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton20MousePressed
        // TODO add your handling code here:
        this.buttonSelected2(((JButton)evt.getSource()),2,0);
}//GEN-LAST:event_jButton20MousePressed

    private void jButton20MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton20MouseReleased
        // TODO add your handling code here:
        this.buttonReleased(((JButton)evt.getSource()),2,0);
}//GEN-LAST:event_jButton20MouseReleased

    private void jButton20gridactionperformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20gridactionperformed
        // TODO add your handling code here:
        // System.exit(-5);
        //JButton jb = (JButton)evt.getSource();
        //jb.setBackground(Color.red);
        //Toolkit.getDefaultToolkit().
}//GEN-LAST:event_jButton20gridactionperformed

    private void jButton30clicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton30clicked
        // TODO add your handling code here:
        //System.exit(-5);
}//GEN-LAST:event_jButton30clicked

    private void jButton30MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton30MouseEntered
        // TODO add your handling code here:
        this.handleGUIEvent(evt,3,0);
}//GEN-LAST:event_jButton30MouseEntered

    private void jButton30MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton30MouseExited
        // TODO add your handling code here:
        this.handleGUIEvent(evt,3,0);
}//GEN-LAST:event_jButton30MouseExited

    private void jButton30MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton30MousePressed
        // TODO add your handling code here:
        this.buttonSelected2(((JButton)evt.getSource()),3,0);
}//GEN-LAST:event_jButton30MousePressed

    private void jButton30MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton30MouseReleased
        // TODO add your handling code here:
        this.buttonReleased(((JButton)evt.getSource()),3,0);
}//GEN-LAST:event_jButton30MouseReleased

    private void jButton30gridactionperformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30gridactionperformed
        // TODO add your handling code here:
        // System.exit(-5);
        //JButton jb = (JButton)evt.getSource();
        //jb.setBackground(Color.red);
        //Toolkit.getDefaultToolkit().
}//GEN-LAST:event_jButton30gridactionperformed

    private void jButton40clicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton40clicked
        // TODO add your handling code here:
        //System.exit(-5);
}//GEN-LAST:event_jButton40clicked

    private void jButton40MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton40MouseEntered
        // TODO add your handling code here:
        this.handleGUIEvent(evt,4,0);
}//GEN-LAST:event_jButton40MouseEntered

    private void jButton40MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton40MouseExited
        // TODO add your handling code here:
        this.handleGUIEvent(evt,4,0);
}//GEN-LAST:event_jButton40MouseExited

    private void jButton40MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton40MousePressed
        // TODO add your handling code here:
        this.buttonSelected2(((JButton)evt.getSource()),4,0);
}//GEN-LAST:event_jButton40MousePressed

    private void jButton40MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton40MouseReleased
        // TODO add your handling code here:
        this.buttonReleased(((JButton)evt.getSource()),4,0);
}//GEN-LAST:event_jButton40MouseReleased

    private void jButton40gridactionperformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton40gridactionperformed
        // TODO add your handling code here:
        // System.exit(-5);
        //JButton jb = (JButton)evt.getSource();
        //jb.setBackground(Color.red);
        //Toolkit.getDefaultToolkit().
}//GEN-LAST:event_jButton40gridactionperformed

    private void jButton01clicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton01clicked
        // TODO add your handling code here:
        //System.exit(-5);
}//GEN-LAST:event_jButton01clicked

    private void jButton01MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton01MouseEntered
        // TODO add your handling code here:
        this.handleGUIEvent(evt,0,1);
}//GEN-LAST:event_jButton01MouseEntered

    private void jButton01MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton01MouseExited
        // TODO add your handling code here:
        this.handleGUIEvent(evt,0,1);
}//GEN-LAST:event_jButton01MouseExited

    private void jButton01MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton01MousePressed
        // TODO add your handling code here:
        this.buttonSelected2(((JButton)evt.getSource()),0,1);
}//GEN-LAST:event_jButton01MousePressed

    private void jButton01MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton01MouseReleased
        // TODO add your handling code here:
        this.buttonReleased(((JButton)evt.getSource()),0,1);
}//GEN-LAST:event_jButton01MouseReleased

    private void jButton01gridactionperformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton01gridactionperformed
        // TODO add your handling code here:
        // System.exit(-5);
        //JButton jb = (JButton)evt.getSource();
        //jb.setBackground(Color.red);
        //Toolkit.getDefaultToolkit().
}//GEN-LAST:event_jButton01gridactionperformed

    private void jButton11clicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton11clicked
        // TODO add your handling code here:
        //System.exit(-5);
}//GEN-LAST:event_jButton11clicked

    private void jButton11MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton11MouseEntered
        // TODO add your handling code here:
        this.handleGUIEvent(evt,1,1);
}//GEN-LAST:event_jButton11MouseEntered

    private void jButton11MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton11MouseExited
        // TODO add your handling code here:
        this.handleGUIEvent(evt,1,1);
}//GEN-LAST:event_jButton11MouseExited

    private void jButton11MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton11MousePressed
        // TODO add your handling code here:
        this.buttonSelected2(((JButton)evt.getSource()),1,1);
}//GEN-LAST:event_jButton11MousePressed

    private void jButton11MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton11MouseReleased
        // TODO add your handling code here:
        this.buttonReleased(((JButton)evt.getSource()),1,1);
}//GEN-LAST:event_jButton11MouseReleased

    private void jButton11gridactionperformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11gridactionperformed
        // TODO add your handling code here:
        // System.exit(-5);
        //JButton jb = (JButton)evt.getSource();
        //jb.setBackground(Color.red);
        //Toolkit.getDefaultToolkit().
}//GEN-LAST:event_jButton11gridactionperformed

    private void jButton21clicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton21clicked
        // TODO add your handling code here:
        //System.exit(-5);
}//GEN-LAST:event_jButton21clicked

    private void jButton21MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton21MouseEntered
        // TODO add your handling code here:
        this.handleGUIEvent(evt,2,1);
}//GEN-LAST:event_jButton21MouseEntered

    private void jButton21MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton21MouseExited
        // TODO add your handling code here:
        this.handleGUIEvent(evt,2,1);
}//GEN-LAST:event_jButton21MouseExited

    private void jButton21MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton21MousePressed
        // TODO add your handling code here:
        this.buttonSelected2(((JButton)evt.getSource()),2,1);
}//GEN-LAST:event_jButton21MousePressed

    private void jButton21MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton21MouseReleased
        // TODO add your handling code here:
        this.buttonReleased(((JButton)evt.getSource()),2,1);
}//GEN-LAST:event_jButton21MouseReleased

    private void jButton21gridactionperformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21gridactionperformed
        // TODO add your handling code here:
        // System.exit(-5);
        //JButton jb = (JButton)evt.getSource();
        //jb.setBackground(Color.red);
        //Toolkit.getDefaultToolkit().
}//GEN-LAST:event_jButton21gridactionperformed

    private void jButton31clicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton31clicked
        // TODO add your handling code here:
        //System.exit(-5);
}//GEN-LAST:event_jButton31clicked

    private void jButton31MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton31MouseEntered
        // TODO add your handling code here:
        this.handleGUIEvent(evt,3,1);
}//GEN-LAST:event_jButton31MouseEntered

    private void jButton31MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton31MouseExited
        // TODO add your handling code here:
        this.handleGUIEvent(evt,3,1);
}//GEN-LAST:event_jButton31MouseExited

    private void jButton31MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton31MousePressed
        // TODO add your handling code here:
        this.buttonSelected2(((JButton)evt.getSource()),3,1);
}//GEN-LAST:event_jButton31MousePressed

    private void jButton31MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton31MouseReleased
        // TODO add your handling code here:
        this.buttonReleased(((JButton)evt.getSource()),3,1);
}//GEN-LAST:event_jButton31MouseReleased

    private void jButton31gridactionperformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton31gridactionperformed
        // TODO add your handling code here:
        // System.exit(-5);
        //JButton jb = (JButton)evt.getSource();
        //jb.setBackground(Color.red);
        //Toolkit.getDefaultToolkit().
}//GEN-LAST:event_jButton31gridactionperformed

    private void jButton41clicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton41clicked
        // TODO add your handling code here:
        //System.exit(-5);
}//GEN-LAST:event_jButton41clicked

    private void jButton41MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton41MouseEntered
        // TODO add your handling code here:
        this.handleGUIEvent(evt,4,1);
}//GEN-LAST:event_jButton41MouseEntered

    private void jButton41MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton41MouseExited
        // TODO add your handling code here:
        this.handleGUIEvent(evt,4,1);
}//GEN-LAST:event_jButton41MouseExited

    private void jButton41MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton41MousePressed
        // TODO add your handling code here:
        this.buttonSelected2(((JButton)evt.getSource()),4,1);
}//GEN-LAST:event_jButton41MousePressed

    private void jButton41MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton41MouseReleased
        // TODO add your handling code here:
        this.buttonReleased(((JButton)evt.getSource()),4,1);
}//GEN-LAST:event_jButton41MouseReleased

    private void jButton41gridactionperformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton41gridactionperformed
        // TODO add your handling code here:
        // System.exit(-5);
        //JButton jb = (JButton)evt.getSource();
        //jb.setBackground(Color.red);
        //Toolkit.getDefaultToolkit().
}//GEN-LAST:event_jButton41gridactionperformed

    private void jButton02clicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton02clicked
        // TODO add your handling code here:
        //System.exit(-5);
}//GEN-LAST:event_jButton02clicked

    private void jButton02MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton02MouseEntered
        // TODO add your handling code here:
        this.handleGUIEvent(evt,0,2);
}//GEN-LAST:event_jButton02MouseEntered

    private void jButton02MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton02MouseExited
        // TODO add your handling code here:
        this.handleGUIEvent(evt,0,2);
}//GEN-LAST:event_jButton02MouseExited

    private void jButton02MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton02MousePressed
        // TODO add your handling code here:
        this.buttonSelected2(((JButton)evt.getSource()),0,2);
}//GEN-LAST:event_jButton02MousePressed

    private void jButton02MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton02MouseReleased
        // TODO add your handling code here:
        this.buttonReleased(((JButton)evt.getSource()),0,2);
}//GEN-LAST:event_jButton02MouseReleased

    private void jButton02gridactionperformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton02gridactionperformed
        // TODO add your handling code here:
        // System.exit(-5);
        //JButton jb = (JButton)evt.getSource();
        //jb.setBackground(Color.red);
        //Toolkit.getDefaultToolkit().
}//GEN-LAST:event_jButton02gridactionperformed

    private void jButton12clicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton12clicked
        // TODO add your handling code here:
        //System.exit(-5);
}//GEN-LAST:event_jButton12clicked

    private void jButton12MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton12MouseEntered
        // TODO add your handling code here:
        this.handleGUIEvent(evt,1,2);
}//GEN-LAST:event_jButton12MouseEntered

    private void jButton12MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton12MouseExited
        // TODO add your handling code here:
        this.handleGUIEvent(evt,1,2);
}//GEN-LAST:event_jButton12MouseExited

    private void jButton12MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton12MousePressed
        // TODO add your handling code here:
        this.buttonSelected2(((JButton)evt.getSource()),1,2);
}//GEN-LAST:event_jButton12MousePressed

    private void jButton12MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton12MouseReleased
        // TODO add your handling code here:
        this.buttonReleased(((JButton)evt.getSource()),1,2);
}//GEN-LAST:event_jButton12MouseReleased

    private void jButton12gridactionperformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12gridactionperformed
        // TODO add your handling code here:
        // System.exit(-5);
        //JButton jb = (JButton)evt.getSource();
        //jb.setBackground(Color.red);
        //Toolkit.getDefaultToolkit().
}//GEN-LAST:event_jButton12gridactionperformed

    private void jButton22clicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton22clicked
        // TODO add your handling code here:
        //System.exit(-5);
}//GEN-LAST:event_jButton22clicked

    private void jButton22MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton22MouseEntered
        // TODO add your handling code here:
        this.handleGUIEvent(evt,2,2);
    }//GEN-LAST:event_jButton22MouseEntered

    private void jButton22MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton22MouseExited
        // TODO add your handling code here:
        this.handleGUIEvent(evt,2,2);
}//GEN-LAST:event_jButton22MouseExited

    private void jButton22MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton22MousePressed
        // TODO add your handling code here:
        this.buttonSelected2(((JButton)evt.getSource()),2,2);
}//GEN-LAST:event_jButton22MousePressed

    private void jButton22MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton22MouseReleased
        // TODO add your handling code here:
        this.buttonReleased(((JButton)evt.getSource()),2,2);
}//GEN-LAST:event_jButton22MouseReleased

    private void jButton22gridactionperformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22gridactionperformed
        // TODO add your handling code here:
        // System.exit(-5);
        //JButton jb = (JButton)evt.getSource();
        //jb.setBackground(Color.red);
        //Toolkit.getDefaultToolkit().
}//GEN-LAST:event_jButton22gridactionperformed

    private void jButton32clicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton32clicked
        // TODO add your handling code here:
        //System.exit(-5);
}//GEN-LAST:event_jButton32clicked

    private void jButton32MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton32MouseEntered
        // TODO add your handling code here:
        this.handleGUIEvent(evt,3,2);
}//GEN-LAST:event_jButton32MouseEntered

    private void jButton32MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton32MouseExited
        // TODO add your handling code here:
        this.handleGUIEvent(evt,3,2);
}//GEN-LAST:event_jButton32MouseExited

    private void jButton32MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton32MousePressed
        // TODO add your handling code here:
        this.buttonSelected2(((JButton)evt.getSource()),3,2);
}//GEN-LAST:event_jButton32MousePressed

    private void jButton32MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton32MouseReleased
        // TODO add your handling code here:
        this.buttonReleased(((JButton)evt.getSource()),3,2);
}//GEN-LAST:event_jButton32MouseReleased

    private void jButton32gridactionperformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton32gridactionperformed
        // TODO add your handling code here:
        // System.exit(-5);
        //JButton jb = (JButton)evt.getSource();
        //jb.setBackground(Color.red);
        //Toolkit.getDefaultToolkit().
}//GEN-LAST:event_jButton32gridactionperformed

    private void jButton42clicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton42clicked
        // TODO add your handling code here:
        //System.exit(-5);
}//GEN-LAST:event_jButton42clicked

    private void jButton42MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton42MouseEntered
        // TODO add your handling code here:
        this.handleGUIEvent(evt,4,2);
}//GEN-LAST:event_jButton42MouseEntered

    private void jButton42MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton42MouseExited
        // TODO add your handling code here:
        this.handleGUIEvent(evt,4,2);
}//GEN-LAST:event_jButton42MouseExited

    private void jButton42MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton42MousePressed
        // TODO add your handling code here:
        this.buttonSelected2(((JButton)evt.getSource()),4,2);
}//GEN-LAST:event_jButton42MousePressed

    private void jButton42MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton42MouseReleased
        // TODO add your handling code here:
        this.buttonReleased(((JButton)evt.getSource()),4,2);
}//GEN-LAST:event_jButton42MouseReleased

    private void jButton42gridactionperformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton42gridactionperformed
        // TODO add your handling code here:
        // System.exit(-5);
        //JButton jb = (JButton)evt.getSource();
        //jb.setBackground(Color.red);
        //Toolkit.getDefaultToolkit().
}//GEN-LAST:event_jButton42gridactionperformed

    private void jButton03clicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton03clicked
        // TODO add your handling code here:
        //System.exit(-5);
}//GEN-LAST:event_jButton03clicked

    private void jButton03MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton03MouseEntered
        // TODO add your handling code here:
        this.handleGUIEvent(evt,0,3);
}//GEN-LAST:event_jButton03MouseEntered

    private void jButton03MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton03MouseExited
        // TODO add your handling code here:
        this.handleGUIEvent(evt,0,3);
}//GEN-LAST:event_jButton03MouseExited

    private void jButton03MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton03MousePressed
        // TODO add your handling code here:
        this.buttonSelected2(((JButton)evt.getSource()),0,3);
}//GEN-LAST:event_jButton03MousePressed

    private void jButton03MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton03MouseReleased
        // TODO add your handling code here:
        this.buttonReleased(((JButton)evt.getSource()),0,3);
}//GEN-LAST:event_jButton03MouseReleased

    private void jButton03gridactionperformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton03gridactionperformed
        // TODO add your handling code here:
        // System.exit(-5);
        //JButton jb = (JButton)evt.getSource();
        //jb.setBackground(Color.red);
        //Toolkit.getDefaultToolkit().
}//GEN-LAST:event_jButton03gridactionperformed

    private void jButton13clicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton13clicked
        // TODO add your handling code here:
        //System.exit(-5);
}//GEN-LAST:event_jButton13clicked

    private void jButton13MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton13MouseEntered
        // TODO add your handling code here:
        this.handleGUIEvent(evt,1,3);
}//GEN-LAST:event_jButton13MouseEntered

    private void jButton13MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton13MouseExited
        // TODO add your handling code here:
        this.handleGUIEvent(evt,1,3);
}//GEN-LAST:event_jButton13MouseExited

    private void jButton13MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton13MousePressed
        // TODO add your handling code here:
        this.buttonSelected2(((JButton)evt.getSource()),1,3);
}//GEN-LAST:event_jButton13MousePressed

    private void jButton13MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton13MouseReleased
        // TODO add your handling code here:
        this.buttonReleased(((JButton)evt.getSource()),1,3);
}//GEN-LAST:event_jButton13MouseReleased

    private void jButton13gridactionperformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13gridactionperformed
        // TODO add your handling code here:
        // System.exit(-5);
        //JButton jb = (JButton)evt.getSource();
        //jb.setBackground(Color.red);
        //Toolkit.getDefaultToolkit().
}//GEN-LAST:event_jButton13gridactionperformed

    private void jButton23clicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton23clicked
        // TODO add your handling code here:
        //System.exit(-5);
}//GEN-LAST:event_jButton23clicked

    private void jButton23MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton23MouseEntered
        // TODO add your handling code here:
        this.handleGUIEvent(evt,2,3);
}//GEN-LAST:event_jButton23MouseEntered

    private void jButton23MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton23MouseExited
        // TODO add your handling code here:
        this.handleGUIEvent(evt,2,3);
}//GEN-LAST:event_jButton23MouseExited

    private void jButton23MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton23MousePressed
        // TODO add your handling code here:
        this.buttonSelected2(((JButton)evt.getSource()),2,3);
}//GEN-LAST:event_jButton23MousePressed

    private void jButton23MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton23MouseReleased
        // TODO add your handling code here:
        this.buttonReleased(((JButton)evt.getSource()),2,3);
}//GEN-LAST:event_jButton23MouseReleased

    private void jButton23gridactionperformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23gridactionperformed
        // TODO add your handling code here:
        // System.exit(-5);
        //JButton jb = (JButton)evt.getSource();
        //jb.setBackground(Color.red);
        //Toolkit.getDefaultToolkit().
}//GEN-LAST:event_jButton23gridactionperformed

    private void jButton33clicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton33clicked
        // TODO add your handling code here:
        //System.exit(-5);
}//GEN-LAST:event_jButton33clicked

    private void jButton33MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton33MouseEntered
        // TODO add your handling code here:
        this.handleGUIEvent(evt,3,3);
}//GEN-LAST:event_jButton33MouseEntered

    private void jButton33MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton33MouseExited
        // TODO add your handling code here:
        this.handleGUIEvent(evt,3,3);
}//GEN-LAST:event_jButton33MouseExited

    private void jButton33MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton33MousePressed
        // TODO add your handling code here:
        this.buttonSelected2(((JButton)evt.getSource()),3,3);
}//GEN-LAST:event_jButton33MousePressed

    private void jButton33MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton33MouseReleased
        // TODO add your handling code here:
        this.buttonReleased(((JButton)evt.getSource()),3,3);
}//GEN-LAST:event_jButton33MouseReleased

    private void jButton33gridactionperformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton33gridactionperformed
        // TODO add your handling code here:
        // System.exit(-5);
        //JButton jb = (JButton)evt.getSource();
        //jb.setBackground(Color.red);
        //Toolkit.getDefaultToolkit().
}//GEN-LAST:event_jButton33gridactionperformed

    private void jButton43clicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton43clicked
        // TODO add your handling code here:
        //System.exit(-5);
}//GEN-LAST:event_jButton43clicked

    private void jButton43MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton43MouseEntered
        // TODO add your handling code here:
        this.handleGUIEvent(evt,4,3);
}//GEN-LAST:event_jButton43MouseEntered

    private void jButton43MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton43MouseExited
        // TODO add your handling code here:
        this.handleGUIEvent(evt,4,3);
}//GEN-LAST:event_jButton43MouseExited

    private void jButton43MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton43MousePressed
        // TODO add your handling code here:
        this.buttonSelected2(((JButton)evt.getSource()),4,3);
}//GEN-LAST:event_jButton43MousePressed

    private void jButton43MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton43MouseReleased
        // TODO add your handling code here:
        this.buttonReleased(((JButton)evt.getSource()),4,3);
}//GEN-LAST:event_jButton43MouseReleased

    private void jButton43gridactionperformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton43gridactionperformed
        // TODO add your handling code here:
        // System.exit(-5);
        //JButton jb = (JButton)evt.getSource();
        //jb.setBackground(Color.red);
        //Toolkit.getDefaultToolkit().
}//GEN-LAST:event_jButton43gridactionperformed

    private void jButton04MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton04MouseEntered
        // TODO add your handling code here:
        this.handleGUIEvent(evt,0,4);
}//GEN-LAST:event_jButton04MouseEntered

    private void jButton04MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton04MouseExited
        // TODO add your handling code here:
        this.handleGUIEvent(evt,0,4);
}//GEN-LAST:event_jButton04MouseExited

    private void jButton04MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton04MousePressed
        // TODO add your handling code here:
        this.buttonSelected2(((JButton)evt.getSource()),0,4);
}//GEN-LAST:event_jButton04MousePressed

    private void jButton04MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton04MouseReleased
        // TODO add your handling code here:
        this.buttonReleased(((JButton)evt.getSource()),0,4);
}//GEN-LAST:event_jButton04MouseReleased

    private void jButton04gridactionperformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton04gridactionperformed
        // TODO add your handling code here:
        // System.exit(-5);
        //JButton jb = (JButton)evt.getSource();
        //jb.setBackground(Color.red);
        //Toolkit.getDefaultToolkit().
}//GEN-LAST:event_jButton04gridactionperformed

    private void jButton14MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton14MouseEntered
        // TODO add your handling code here:
        this.handleGUIEvent(evt,1,4);
}//GEN-LAST:event_jButton14MouseEntered

    private void jButton14MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton14MouseExited
        // TODO add your handling code here:
        this.handleGUIEvent(evt,1,4);
}//GEN-LAST:event_jButton14MouseExited

    private void jButton14MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton14MousePressed
        // TODO add your handling code here:
        this.buttonSelected2(((JButton)evt.getSource()),1,4);
}//GEN-LAST:event_jButton14MousePressed

    private void jButton14MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton14MouseReleased
        // TODO add your handling code here:
        this.buttonReleased(((JButton)evt.getSource()),1,4);
}//GEN-LAST:event_jButton14MouseReleased

    private void jButton14gridactionperformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14gridactionperformed
        // TODO add your handling code here:
        // System.exit(-5);
        //JButton jb = (JButton)evt.getSource();
        //jb.setBackground(Color.red);
        //Toolkit.getDefaultToolkit().
}//GEN-LAST:event_jButton14gridactionperformed

    private void jButton24MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton24MouseEntered
        // TODO add your handling code here:
        this.handleGUIEvent(evt,2,4);
}//GEN-LAST:event_jButton24MouseEntered

    private void jButton24MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton24MouseExited
        // TODO add your handling code here:
        this.handleGUIEvent(evt,2,4);
}//GEN-LAST:event_jButton24MouseExited

    private void jButton24MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton24MousePressed
        // TODO add your handling code here:
        this.buttonSelected2(((JButton)evt.getSource()),2,4);
}//GEN-LAST:event_jButton24MousePressed

    private void jButton24MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton24MouseReleased
        // TODO add your handling code here:
        this.buttonReleased(((JButton)evt.getSource()),2,4);
}//GEN-LAST:event_jButton24MouseReleased

    private void jButton24gridactionperformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24gridactionperformed
        // TODO add your handling code here:
        // System.exit(-5);
        //JButton jb = (JButton)evt.getSource();
        //jb.setBackground(Color.red);
        //Toolkit.getDefaultToolkit().
}//GEN-LAST:event_jButton24gridactionperformed

    private void jButton34MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton34MouseEntered
        // TODO add your handling code here:
        this.handleGUIEvent(evt,3,4);
}//GEN-LAST:event_jButton34MouseEntered

    private void jButton34MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton34MouseExited
        // TODO add your handling code here:
        this.handleGUIEvent(evt,3,4);
}//GEN-LAST:event_jButton34MouseExited

    private void jButton34MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton34MousePressed
        // TODO add your handling code here:
        this.buttonSelected2(((JButton)evt.getSource()),3,4);
    }//GEN-LAST:event_jButton34MousePressed

    private void jButton34MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton34MouseReleased
        // TODO add your handling code here:
        this.buttonReleased(((JButton)evt.getSource()),3,4);
}//GEN-LAST:event_jButton34MouseReleased

    private void jButton34gridactionperformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton34gridactionperformed
        // TODO add your handling code here:
        // System.exit(-5);
        //JButton jb = (JButton)evt.getSource();
        //jb.setBackground(Color.red);
        //Toolkit.getDefaultToolkit().
}//GEN-LAST:event_jButton34gridactionperformed

    private void jButton44MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton44MouseEntered
        // TODO add your handling code here:
        this.handleGUIEvent(evt,4,4);
}//GEN-LAST:event_jButton44MouseEntered

    private void jButton44MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton44MouseExited
        // TODO add your handling code here:
        this.handleGUIEvent(evt,4,4);
}//GEN-LAST:event_jButton44MouseExited

    private void jButton44MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton44MousePressed
        // TODO add your handling code here:
        this.buttonSelected2(((JButton)evt.getSource()),4,4);
    }//GEN-LAST:event_jButton44MousePressed

    private void jButton44MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton44MouseReleased
        // TODO add your handling code here:
        this.buttonReleased(((JButton)evt.getSource()),4,4);
}//GEN-LAST:event_jButton44MouseReleased

    private void jButton44gridactionperformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton44gridactionperformed
        // TODO add your handling code here:
        // System.exit(-5);
        //JButton jb = (JButton)evt.getSource();
        //jb.setBackground(Color.red);
        //Toolkit.getDefaultToolkit().
}//GEN-LAST:event_jButton44gridactionperformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton00;
    private javax.swing.JButton jButton01;
    private javax.swing.JButton jButton02;
    private javax.swing.JButton jButton03;
    private javax.swing.JButton jButton04;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton40;
    private javax.swing.JButton jButton41;
    private javax.swing.JButton jButton42;
    private javax.swing.JButton jButton43;
    private javax.swing.JButton jButton44;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables





         public void buttonSelected2(JButton jb, int x, int y){
         long newSelBySelf = new Date().getTime();
         String buttontext =jb.getText();
         if(buttontext==null)buttontext="";

         if(ctreh!=null)this.ctreh.buttonSelected2(buttontext, x, y, newSelBySelf, this.lastMouseSelectBySelf, this.lastMouseReleaseBySelf,this.lastIncomingSelectByOther,this.lastIncomingReleaseByOther, this.lastSelectByOTher,this.lastSelectByOtherX,this.lastSelectByOtherY);
         this.lastMouseSelectBySelf=newSelBySelf;
     }

         
     public void buttonReleased(JButton jb, int x, int y){
         long newRelBySelf = new Date().getTime();
          String buttontext =jb.getText();
         if(buttontext==null)buttontext="";
         if(ctreh!=null)this.ctreh.buttonReleased(buttontext, x, y, newRelBySelf, this.lastMouseSelectBySelf, this.lastMouseReleaseBySelf,this.lastIncomingSelectByOther,this.lastIncomingReleaseByOther,  this.lastSelectByOTher,this.lastSelectByOtherX,this.lastSelectByOtherY);
         this.lastMouseReleaseBySelf=newRelBySelf;
         //jb.setBackground(Color.black);
         //this.ctreh.buttonReleased(jb.getText(), x, y, new Date().getTime());
     }


    public void handleGUIEvent(MouseEvent e, int x,int y){


         //System.err.println(e.getModifiers())
           int id4 = e.getID();
           JButton jb = (JButton)e.getSource();
           if(id4==MouseEvent.MOUSE_ENTERED){
               //jb.setBorder(active);
                String buttontext =jb.getText();
               if(buttontext==null)buttontext="";
               if(ctreh!=null)ctreh.mouseEntered(buttontext, x, y, new Date().getTime());
           }


           //this.setUniqueBorderSelected(x, y, true);
          // JButton jbSource = (JButton) e.getSource();
           //if(jbSource==jButton00){x=0;y=0;}


    }

}
