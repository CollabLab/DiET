/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package client;

import java.awt.Dimension;
import java.io.File;
import java.util.Random;
import java.util.Vector;

import diet.message.MessageTask;
import diet.parameters.StringParameter;
import diet.server.Conversation;
import diet.server.Participant;
import diet.task.TaskController;
import diet.task.mazegame.message.MessageChangeGateStatus;
import diet.task.mazegame.message.MessageCursorUpdate;
import diet.task.mazegame.message.MessageNewMazeGame;
import diet.task.mazegame.message.MessageNextMaze;

/**
 *
 * @author user
 * This class should actually be part of diet.task.mazegame.server
 * However, due to legacy support it has to be in this package as it uses non-public fields
 * of other classes in this package.
 */
public class MazeGameController2WAY extends TaskController{

    Conversation c;
    SetupIO sIO;
    SetupIOMazeGameMoveWriting sIOWriting;
    MazeGameController2WAYUI mgcUI;
    Participant participant1;
    Participant participant2;
    //Participant pMatcher2;
    
    Vector pDirectorMazes = new Vector();
    Vector pMatcher1Mazes = new Vector();
    //Vector pMatcher2Mazes = new Vector();
    
    MazeWrap pDirectorMaze, pMatcher1Maze;//, pMatcher2Maze;
    Dimension pDirectorPosition, pMatcher1Position;//, pMatcher2Position;
    
    int mazeNumber =0;
    int moveNo=0;
    
    int participant1MoveNo=0;
    int participant2MoveNo=0;

    boolean participant1IsOnSwitch;
    boolean participant2IsOnSwitch;

    int participant1NoOfTimesOnSwitch=0;
    int participant2NoOfTimesOnSwitch=0;
    
    public MazeGameController2WAY(Conversation c){
        this.c=c;
        //sIO = new SetupIO("C:\\svndiet\\chattool\\mazegame",c.getDirectoryNameContainingAllSavedExperimentalData().toString());
        sIO = new SetupIO(System.getProperty("user.dir")+File.separator+"mazegame",c.getDirectoryNameContainingAllSavedExperimentalData().toString());
        sIOWriting = new SetupIOMazeGameMoveWriting(c.getDirectoryNameContainingAllSavedExperimentalData().toString());
        mgcUI = new MazeGameController2WAYUI(this);
        
        
        
    }
    
     public void initialize(){
           
        
     }
    
    
    public void participantJoinedConversation(Participant pJoined){
        //If the program gets to the end of this method 
        //The director and matchers should have been established
        Vector v = (Vector)c.getParticipants().getAllActiveParticipants().clone();
        v = (Vector)c.getParticipants().getAllParticipants().clone();
        if(v.size()<2){
            return;
        }
        else if(v.size()==2){
            c.getExpManager().getEMUI().println("Main", "Mazegame controller is sending mazes to both clients");
            
        }
        else{
            c.getExpManager().getEMUI().println("Main", "There are too many clients logged in. The max. number is 2");
            c.getExpManager().getEMUI().println("Main", "The maze game now has an indterminate state.");
            c.getExpManager().getEMUI().println("Main", "IT IS ADVISABLE TO CLOSE THIS EXPERIMENT AND RE-RUN / RESTART");
            return;
        }
        
   
        participant1 = (Participant) v.elementAt(0);
        participant2 = (Participant) v.elementAt(1);
        loadAndSendMazesToClients();
        sIO.saveClientMazesOfTwoClientsByName(pDirectorMazes, pMatcher1Mazes);
    }
    
   
    
    
    public void loadAndSendMazesToClients(){
        try{
          System.err.println("MESSAGE0");
          //Check to see if the experiment is actually re-starting a previous one
          //If so, need to load the old mazes
          StringParameter spr = (StringParameter)c.getExperimentSettings().getParameter("Recovery");
          if(spr!=null){
              Vector v = sIO.getAllMazesFromExperimentDirectory2WAY(spr.getValue());
              this.pDirectorMazes=(Vector)v.elementAt(0);
              this.pMatcher1Mazes=(Vector)v.elementAt(1);
              //this.pMatcher2Mazes=(Vector)v.elementAt(2);
          }
          else{
             //System.exit(-12312324); 
             loadRandomMazesFromFile();
          }   
          System.err.println("MESSAGE1");
          initializeMazesToFirstMaze();
          
          System.err.println("MESSAGE2");
          sendMazesToClients();
          System.err.println("MESSAGE3");
          mgcUI.initializeJTabbedPane(participant2.getUsername(),pMatcher1Mazes,participant1.getUsername(),this.pDirectorMazes);//,pMatcher2.getUsername(),pMatcher2Mazes);
        }catch(Exception e){
            System.err.println(e.getMessage().toString());
            System.out.println("STACKTRACE");
            e.printStackTrace();
            System.exit(-123423);
        }  
    }
    
    public void loadRandomMazesFromFile(){
        System.err.println("HEREA");
        Vector v = this.sIO.getRandomPairOfMazeVectors();
        System.err.println("HEREB");
        Vector client1Mazes = (Vector)v.elementAt(0);
        System.err.println("HEREC");
        Vector client2Mazes = (Vector)v.elementAt(1);
        System.err.println("HERED");
        Random r = new Random();
        System.err.println("HEREE");
        int i = r.nextInt(2);
        System.err.println("HEREF");
        this.pDirectorMazes=(Vector)v.elementAt(0);
        this.pMatcher1Mazes=(Vector)v.elementAt(1);
        

    }
    
    public Vector cloneVectorOfMazes(Vector v){
            Vector v2 = new Vector();
            for(int i=0;i<v.size();i++){
                Maze m = (Maze)v.elementAt(i);
                Maze m2 = m.getClone();
                v2.addElement(m2); 
            }
            return v2;
    }
    
    public void initializeMazesToFirstMaze(){
        pDirectorMaze = new MazeWrap((Maze) pDirectorMazes.elementAt(0));
        pMatcher1Maze = new MazeWrap((Maze) pMatcher1Mazes.elementAt(0));
        mazeNumber=0;
        pDirectorPosition = new Dimension(0,0);
        pMatcher1Position = new Dimension(0,0);
    }
    
    public void sendMazesToClients(){
         Game gDirector = new Game(pDirectorMazes);
         Game gMatcher1 = new Game(pMatcher1Mazes);
         
         //expIOW.saveMazes(client1mazes, client2mazes, client3mazes);
         c.sendTaskMoveToParticipant(participant1, new MessageNewMazeGame("server","server",gDirector));
         c.sendTaskMoveToParticipant(participant2, new MessageNewMazeGame("server","server",gMatcher1));
         

    }
   
    synchronized public boolean isAonOthersSwitch(String a){
        if(this.participant1.getUsername().equalsIgnoreCase(a)){
            //is participant 1 on participant 2s switch?
            return this.participant1IsOnSwitch;
        }
        else if(this.participant2.getUsername().equalsIgnoreCase(a)){
            return this.participant2IsOnSwitch;
        }
        else{
            Conversation.printWSln("Main", "Error in working out whether A is on Other's switch because "+a+" is not (1) "+participant1.getUsername()+" or(2)"+participant2.getUsername());
            return false;
        }
    }

    synchronized public boolean isOtherSOnAswitch(String a){
        try{
            if(this.participant1.getUsername().equalsIgnoreCase(a)){
                //is participant 1 on participant 2s switch?
                return this.participant2IsOnSwitch;
            }
            else if(this.participant2.getUsername().equalsIgnoreCase(a)){
                return this.participant1IsOnSwitch;
            }
            else{
                Conversation.printWSln("Main", "Error in working out whether Other is On A's switch because "+a+" is not (1) "+participant1.getUsername()+" or(2)"+participant2.getUsername());
           
                return false;
            }
        }catch (Exception e){
             Conversation.printWSln("Main", "Error in working out whether Other is On A's switch");
             return false;
        }
    }


    synchronized public int getSwitchTraversalCount(String participantname){
      try{
        if(this.participant1.getUsername().equalsIgnoreCase(participantname)){
            //is participant 1 on participant 2s switch?
            return this.participant1NoOfTimesOnSwitch;
        }
        else if(this.participant2.getUsername().equalsIgnoreCase(participantname)){
            return this.participant2NoOfTimesOnSwitch;
        }
        else{
            Conversation.printWSln("Main", "Error in working out traversal count (1) "+participantname);
            return -1;
        }
        }
      catch (Exception e){
            Conversation.printWSln("Main", "Error in working out traversal count (2) "+e.getMessage());
            return -1;
      }
    }

synchronized public int getOthersSwitchTraversalCount(String participantname){
      try{
        if(this.participant1.getUsername().equalsIgnoreCase(participantname)){
            //is participant 1 on participant 2s switch?
            return this.participant2NoOfTimesOnSwitch;
        }
        else if(this.participant2.getUsername().equalsIgnoreCase(participantname)){
            return this.participant1NoOfTimesOnSwitch;
        }
        else{
            Conversation.printWSln("Main", "Error in working out switch traversal");
            return -1;
        }
        }
      catch (Exception e){
            Conversation.printWSln("Main", "Error in working out switch traversal");
            return -1;
      }
    }

    public int getOthersMoveNo(String participantname){
        try{
            if(this.participant1.getUsername().equalsIgnoreCase(participantname)){
                //is participant 1 on participant 2s switch?
                return this.participant2MoveNo;
             }
             else if(this.participant2.getUsername().equalsIgnoreCase(participantname)){
                return this.participant1MoveNo;
        }
        else{
            Conversation.printWSln("Main", "Error in working out Others Move No");
            return -1;
            }
      }
      catch (Exception e){
            Conversation.printWSln("Main", "Error in working out Others Move No");
            return -1;

    }
    }


    synchronized public void processTaskMove(MessageTask mt, Participant origin){
        //System.exit(-2345);
     try{   
        if(origin==participant1){
          if (mt instanceof MessageCursorUpdate){
             System.out.println("Server detects client1 sending cursor update");
             
             MessageCursorUpdate mcu = (MessageCursorUpdate)mt;
             this.pDirectorMaze.getMaze().moveTo(mcu.newPos);
             this.mgcUI.movePositionClient2(mcu.newPos);
             if(pMatcher1Maze.getMaze().isASwitch(pDirectorMaze.getMaze().getMinBounds(),mcu.newPos)){
                 mcu.setASwitch(true);
             }
             else{
                 mcu.setASwitch(false);
             }
             if(pMatcher1Maze.getMaze().isASwitch(pDirectorMaze.getMaze().getMinBounds(),mcu.newPos)){
                System.out.println("Server detects client1 as being on client 2 and 3 switch");
                MessageChangeGateStatus mscg = new MessageChangeGateStatus("server","server",true,participant2.getUsername());
                //mscg.setRecipient(pMatcher1.getUsername()+"|"+pMatcher2.getUsername());
                //mcu.setASwitch(true);
                c.sendTaskMoveToParticipant(participant2,mscg);
                
                this.mgcUI.changeGateStatusClient1(true);
                
                mcu.setASwitch(true);
                this.sIOWriting.saveMessage(mcu);
                this.sIOWriting.saveMessage(mscg);
                this.participant1IsOnSwitch=true;
                this.participant1NoOfTimesOnSwitch++;
                }
                else {
                    System.out.println("Server detects client1 as NOT being on switch");
                    MessageChangeGateStatus mscg = new MessageChangeGateStatus("server","server",false,participant2.getUsername());
                    //mscg.setRecipient(pMatcher1.getUsername()+"|"+pMatcher2.getUsername());
                    //mcu.setASwitch(false);
                    c.sendTaskMoveToParticipant(participant2,mscg);
                    
                    this.mgcUI.changeGateStatusClient1(false);
                    
                    mcu.setASwitch(false);
                    this.sIOWriting.saveMessage(mcu);
                    this.sIOWriting.saveMessage(mscg);

                }
             
           }

          else {
            System.out.println("experiment in progress , was expecting username or cursor");
            System.out.println("don't know: " + mt.getClass().toString());
            }   

        //messages.addElement(m);
        ////expIOW.saveMessage(m);

       }
        else if(origin == participant2 ) {
       if (mt instanceof MessageCursorUpdate){
           System.out.println("Server detects client2 sending cursor update");
           MessageCursorUpdate mcu = (MessageCursorUpdate)mt;
           pMatcher1Maze.getMaze().moveTo(mcu.newPos);
           this.mgcUI.movePositionClient1(mcu.newPos);
           if(pDirectorMaze.getMaze().isASwitch(pMatcher1Maze.getMaze().getMinBounds(),mcu.newPos)){
                 mcu.setASwitch(true);
             }
             else{
                 mcu.setASwitch(false);
           }
           //MazeSquare current3=((Maze)client3Maze).getCurrent();
           if(pDirectorMaze.getMaze().isASwitch(pMatcher1Maze.getMaze().getMinBounds(),mcu.newPos)){
              //
               
              System.out.println("Server detects client2 and client3 as being on client1's switch");
              MessageChangeGateStatus mscg = new MessageChangeGateStatus("server","server",true,participant1.getUsername());
              c.sendTaskMoveToParticipant(participant1,mscg);
              this.mgcUI.changeGateStatusClient2(true);    
              //mcu.setASwitch(true);
              this.sIOWriting.saveMessage(mcu);
              this.sIOWriting.saveMessage(mscg);

              this.participant2IsOnSwitch=true;
              this.participant2NoOfTimesOnSwitch++;

          }
          else {
            System.out.println("Server detects client2 or client3 as NOT being on  cl1's switch");
            MessageChangeGateStatus mscg = new MessageChangeGateStatus("server","server",false,participant1.getUsername());
            c.sendTaskMoveToParticipant(participant1,mscg);
            this.mgcUI.changeGateStatusClient2(false);
            
            //mcu.setASwitch(false);
            this.sIOWriting.saveMessage(mcu);
            this.sIOWriting.saveMessage(mscg);
          }
       }

      else {
          System.out.println("experiment in progress2 , was expecting username or cursor update");
          System.out.println("don't know: " + mt.getClass().toString());
      }
      
      //expIOW.saveMessage(m);

   }
      
   if(pDirectorMaze.getMaze().isCompleted()&&pMatcher1Maze.getMaze().isCompleted()){
      System.out.println("Maze number"+ mazeNumber+ " completed");
      moveToNextMaze();
      
        

   }
     }catch (Exception e){
         System.out.println("STACKTRACE");
         System.err.println("ERROR IN MAZEGAME TASKMOVE HANDLER");
         e.printStackTrace();
     }      

     moveNo++;
     if(origin==this.participant1){
         participant1MoveNo++;
     }
     else if(origin == this.participant2){
         participant2MoveNo++;
     }
     
    }
  
 synchronized public boolean moveToMazeNo(int mazeNo){
   if (mazeNumber>=pDirectorMazes.size()-1 ||mazeNumber>=pMatcher1Mazes.size()-1) 
      {
       System.out.println("MoveToMazeNo returning false "+mazeNumber);
       return false;
   }

   pDirectorMaze = new MazeWrap((Maze) pDirectorMazes.elementAt(mazeNo));
   pMatcher1Maze = new MazeWrap((Maze) pMatcher1Mazes.elementAt(mazeNo));
   mazeNumber=mazeNo;
   pDirectorPosition = new Dimension(0,0);
   pMatcher1Position = new Dimension(0,0);
   System.out.println("move to next maze EXPERIMENT IS TRUE");
   MessageNextMaze mnm= new MessageNextMaze("server","server",mazeNo);
   c.sendTaskMoveToParticipant(participant1,mnm);
   c.sendTaskMoveToParticipant(participant2,mnm);
   this.mgcUI.moveToMazeNo(mazeNo);
   this.sIOWriting.saveMessage(mnm);
   return true;
} 
    
    
  public boolean moveToNextMaze(){
   if (mazeNumber>=pDirectorMazes.size()-1 ||mazeNumber>=pMatcher1Mazes.size()-1) 
      {
       System.out.println("MoveToNextMaze returning false "+mazeNumber);
       return false;
   }

   pDirectorMaze = new MazeWrap((Maze) pDirectorMazes.elementAt(pDirectorMazes.indexOf(pDirectorMaze.getMaze())+1));
   pMatcher1Maze = new MazeWrap((Maze) pMatcher1Mazes.elementAt(pMatcher1Mazes.indexOf(pMatcher1Maze.getMaze())+1));
   mazeNumber++;
   pDirectorPosition = new Dimension(0,0);
   pMatcher1Position = new Dimension(0,0);
    System.out.println("move to next maze EXPERIMENT IS TRUE");
    MessageNextMaze mnm= new MessageNextMaze("server","server",mazeNumber);
   c.sendTaskMoveToParticipant(participant1,mnm);
   c.sendTaskMoveToParticipant(participant2,mnm);
   this.mgcUI.moveToMazeNo(mazeNumber);
   this.sIOWriting.saveMessage(mnm);
   return true;
}

   public String getDirectorOrMatcher(Participant p){
       if(p==participant1)return "D";
       if(p==participant2)return "M1";
       return "";
   }
    
    
    @Override
    public void closeDown(){
        try{
          this.sIOWriting.closeDown();
        }catch(Exception e){
            System.err.println("ERROR CLOSING DOWN MAZEGAME CONTROLLER");
        }  
    }
    
    public int getMazeNo(){
        return this.mazeNumber;
    }
    public int getMoveNo(){
        return this.moveNo;
    }
    public int getParticipantsMoveNo(Participant p){
        if(p==participant1){
            return this.participant1MoveNo;
        }
        else if(p==participant2){
            return this.participant2MoveNo;
        }
        return -9999999;
    }
    
}
