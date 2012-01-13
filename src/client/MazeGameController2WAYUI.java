/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import java.awt.Dimension;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author user
 */
public class MazeGameController2WAYUI {
    MazeGameController2WAY mgc;
    JTabbedPane jtp = new JTabbedPane();
    //JExperimentInProgressSuperPanel directorMF;// = new JExperimentFrameGameInProgressMaze();
    JExperimentInProgressSuperPanel client1MF;// matcher1MF = new JMazeFrame();
    JExperimentInProgressSuperPanel client2MF; //matcher2MF = new JMazeFrame();
    
    public MazeGameController2WAYUI(MazeGameController2WAY mgc){
        this.mgc=mgc;
        jtp.setVisible(true);
        JFrame jf = new JFrame();
        jf.getContentPane().add(jtp);
        jf.pack();
        jf.setVisible(true);
        
    }
    
    public void initializeJTabbedPane(
            final String client1Name,final Vector client1Mazes,
            final String client2Name, final Vector client2Mazes){
        try{
           SwingUtilities.invokeLater(new Runnable(){ 
              public void run(){
                  
                  Vector matcher1MazesClone = mgc.cloneVectorOfMazes(client1Mazes);
                  Vector matcher2MazesClone = mgc.cloneVectorOfMazes(client2Mazes);
                  
                  
                  //directorMF = new JExperimentInProgressSuperPanel(directorName,directorMazesClone);
                  client1MF = new JExperimentInProgressSuperPanel(client1Name,matcher1MazesClone);
                  client2MF = new JExperimentInProgressSuperPanel(client2Name,matcher2MazesClone);
                  //jtp.insertTab(directorName, null, directorMF, null, 0);
                  jtp.insertTab(client1Name, null, client1MF, null, 0);
                  jtp.insertTab(client2Name, null, client2MF, null, 1);
              }    
           }); 
        }catch(Exception e){
            System.err.println("COULD NOT INITIALIZE TABBED PANE OF MAZES ON SERVER SIDE");
        }    
    }    
    
    public void moveToMazeNo(final int i){
        try{
            SwingUtilities.invokeLater(new Runnable(){
               public void run(){
                  //directorMF.changeToMazeNo(i);
                  client1MF.changeToMazeNo(i);
                  client2MF.changeToMazeNo(i);
               } 
            });   
        }catch (Exception e){
            System.err.println("ERROR ON SERVER UPDATING DISPLAY AND MOVING MAZES");
        }
    }   
    
   
    public void movePositionClient1(final Dimension newPos){
        try{
           SwingUtilities.invokeLater(new Runnable(){ 
            public void run(){
                client1MF.updateCursor(newPos);
                jtp.repaint();
             }    
           }); 
        }catch(Exception e){
            System.err.println("ERROR PAINTING MAZES ON SERVER SIDE");
        }    
        
        
    }
    public void movePositionClient2(final Dimension newPos){
        try{
           SwingUtilities.invokeLater(new Runnable(){ 
            public void run(){
                client2MF.updateCursor(newPos);
                jtp.repaint();
             }    
           }); 
        }catch(Exception e){
            System.err.println("ERROR PAINTING MAZES ON SERVER SIDE");
        }    
        
    }
    
    public void changeGateStatusClient1(final boolean open){
         try{
           SwingUtilities.invokeLater(new Runnable(){ 
            public void run(){
                client1MF.changeGateStatus(open);
                jtp.repaint();
             }    
           }); 
        }catch(Exception e){
            System.err.println("ERROR PAINTING MAZES ON SERVER SIDE");
        }   
    }
    public void changeGateStatusClient2(final boolean open){
        try{
           SwingUtilities.invokeLater(new Runnable(){ 
            public void run(){
                client2MF.changeGateStatus(open);
                jtp.repaint();
             }    
           }); 
        }catch(Exception e){
            System.err.println("ERROR PAINTING MAZES ON SERVER SIDE");
        }   
    }
    
    /**
     * 
     */
    public void closeDown(){
         jtp.setVisible(false);
         jtp=null;
    }
}
