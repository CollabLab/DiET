/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.task.react;

import diet.debug.Debug;
import diet.utils.VectorToolkit;
import java.util.Date;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author sre
 */
public class SetB extends Set {



      public Vector moves = new Vector();

     

      boolean allowRing = true;

      public SetB(ReactTaskController rtc){
          super(rtc);
          initMoves();
      }




      public int sizeOfSet =2; //This is not the size of moves - it's used to determine each condition - see the vector allConditions below

      public void setSize(int i){
          if(i<2)i=2;
          this.sizeOfSet=i;
      }


    


public void initConditions(){
    allConditions = new Vector();
    Object simultaneous_congruent     =  new MoveAND(rtc.getGame(),rtc.pA, ""+x1,rtc.pB,""+x1);
    allConditions.addElement(simultaneous_congruent);

    Object simultaneous_complementary =  new MoveAND(rtc.getGame(),rtc.pA, ""+x2,rtc.pB,""+x3);
    allConditions.addElement(simultaneous_complementary);

    Object sequential_complementary1 =  new Vector();
           ((Vector)sequential_complementary1).addElement(new MoveONLYFirstHalf(rtc.getGame(),rtc.pA,""+x4));
           ((Vector)sequential_complementary1).addElement(new MoveONLYSecondHalf(rtc.getGame(),rtc.pB,""+x5));
    allConditions.addElement(sequential_complementary1);
    
     Object sequential_complementary2 =  new Vector();
           ((Vector)sequential_complementary2).addElement(new MoveONLYFirstHalf(rtc.getGame(),rtc.pB,""+x6));
           ((Vector)sequential_complementary2).addElement(new MoveONLYSecondHalf(rtc.getGame(),rtc.pA,""+x7));
    allConditions.addElement(sequential_complementary2);
    
    Object sequential_congruent1 =  new Vector();
           ((Vector)sequential_congruent1).addElement(new MoveONLYFirstHalf(rtc.getGame(),rtc.pA,""+x8));
           ((Vector)sequential_congruent1).addElement(new MoveONLYSecondHalf(rtc.getGame(),rtc.pB,""+x8));
    allConditions.addElement(sequential_congruent1);

    Object sequential_congruent2 =  new Vector();
           ((Vector)sequential_congruent2).addElement(new MoveONLYFirstHalf(rtc.getGame(),rtc.pB,""+x9));
           ((Vector)sequential_congruent2).addElement(new MoveONLYSecondHalf(rtc.getGame(),rtc.pA,""+x9));
    allConditions.addElement(sequential_congruent2);

    

   
}

Vector allConditions = new Vector();


 int x1=1;
 int x2=2;
 int x3=3;
 int x4=4;
 int x5=5;
 int x6=6;
 int x7=7;
 int x8=8;
 int x9=9;





/*
 * simultaneous congruent
 *     MoveAND(x1,x1)
 *
 *
 * simultaneous complementary
 *     MoveAND(x2,x3)
 *
 *
 * sequential congruent
*      MoveONLY(rtc.getGame(),rtc.pA,x8)
 *     MoveONLY(rtc.getGame(),rtc.pB,x8)
 *
 *
*      MoveONLY(rtc.getGame(),rtc.pB,x9)
 *     MoveONLY(rtc.getGame(),rtc.pA,x9)
 *
 *
 * sequential complementary
*      MoveONLY(rtc.getGame(),rtc.pA,x4)
 *     MoveONLY(rtc.getGame(),rtc.pB,x5)
 *
 *
*      MoveONLY(rtc.getGame(),rtc.pB,x6)
 *     MoveONLY(rtc.getGame(),rtc.pA,x7)
 *
 */






     public void initMoves(){
         this.initConditions();

     }

      Random r = new Random();
     
      public Move[] getSet(int ignored){
         
          if(Debug.debugREACT)System.err.println("BEFORETOOLKITCALL");
          
          Move[] allMovesFLATTENED = VectorToolkit.flattenVofV_AndTurnToArray(allConditions);
          Vector allMovesFlat = new Vector();
          for(int j=0;j<allMovesFLATTENED.length;j++){
              allMovesFlat.addElement(allMovesFLATTENED[j]);
          }
          
         
          
          //sizeOfSet;
          
          //if(i>allMovesFlat.size())i=allMovesFlat.size();
          int startIndex = r.nextInt(allMovesFlat.size());
          
          
          Vector mves = new Vector();
          for(int i=startIndex;i<allMovesFlat.size() && mves.size()<sizeOfSet;i++){
              mves.addElement(allMovesFlat.elementAt(i));
          }
          
          int remainingitems = sizeOfSet - mves.size();
          
          
          
          for(int i=0;i<allMovesFlat.size()&&mves.size()<sizeOfSet;i++){
              mves.addElement(allMovesFlat.elementAt(i));
          }
          
          Move[] newSET = new Move[mves.size()];
          String sequence = "UNSETB";
          // if(rtc.getGame()==null)System.exit(-34534);
          
          
          for(int j=0;j<mves.size();j++){
              try{
                  newSET[j] =    ((Move)mves.elementAt(j)).getResetCopy();
                  rtc.cC.getC().saveDataToFile("NEWSET(B1)"+j, "server", "server", new Date().getTime(), new Date().getTime(), "", newSET[j].getInfoAsVector());
              }catch (Exception e){
                  //rtc.getGame().rtc.cC.getC().saveDataToFile("NEWSET", "server", "server", new Date().getTime(), new Date().getTime(), sequence, null);
                  e.printStackTrace();
                 
              }
          }
          if(Debug.debugREACT)System.err.println("AFTERTOOLKITCALL2B");
          
          
          
          return newSET;

       }


    public void swap(){
        
    }



}
