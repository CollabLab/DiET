/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.task.react;

import diet.debug.Debug;
import diet.server.Participant;
import diet.utils.VectorToolkit;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author sre
 */
public class SetAReversed extends Set{



      public Vector moves = new Vector();

     

     

      boolean allowRing = true;

      public SetAReversed(ReactTaskController rtc){
          super(rtc);
          initMoves();
      }




      public int sizeOfSet =1; //This is not the size of moves - it's used to determine each condition - see the vector allConditions below

      public void setSize(int i){
          this.sizeOfSet=i;
      }


    


public void initConditions(){
    allConditions = new Vector();
    Object simultaneous_congruent     =  new MoveAND(rtc.getGame(),rtc.pB, ""+x1,rtc.pA,""+x1);
    allConditions.addElement(simultaneous_congruent);

    Object simultaneous_complementary =  new MoveAND(rtc.getGame(),rtc.pB, ""+x2,rtc.pA,""+x3);
    allConditions.addElement(simultaneous_complementary);

    Object sequential_congruent1 =  new Vector();
           ((Vector)sequential_congruent1).addElement(new MoveONLYFirstHalf(rtc.getGame(),rtc.pB,""+x8));
           ((Vector)sequential_congruent1).addElement(new MoveONLYSecondHalf(rtc.getGame(),rtc.pA,""+x8));
    allConditions.addElement(sequential_congruent1);

    Object sequential_congruent2 =  new Vector();
           ((Vector)sequential_congruent2).addElement(new MoveONLYFirstHalf(rtc.getGame(),rtc.pA,""+x9));
           ((Vector)sequential_congruent2).addElement(new MoveONLYSecondHalf(rtc.getGame(),rtc.pB,""+x9));
    allConditions.addElement(sequential_congruent2);

    Object sequential_complementary1 =  new Vector();
           ((Vector)sequential_complementary1).addElement(new MoveONLYFirstHalf(rtc.getGame(),rtc.pB,""+x4));
           ((Vector)sequential_complementary1).addElement(new MoveONLYSecondHalf(rtc.getGame(),rtc.pA,""+x5));
    allConditions.addElement(sequential_complementary1);

    Object sequential_complementary2 =  new Vector();
           ((Vector)sequential_complementary2).addElement(new MoveONLYFirstHalf(rtc.getGame(),rtc.pA,""+x6));
           ((Vector)sequential_complementary2).addElement(new MoveONLYSecondHalf(rtc.getGame(),rtc.pB,""+x7));
    allConditions.addElement(sequential_complementary2);
}

Vector allConditions = new Vector();


 int x1=4;
 int x2=3;
 int x3=1;
 int x4=2;
 int x5=8;
 int x6=5;
 int x7=7;
 int x8=6;
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


      public Move[] getSet(int i){
         
          if(Debug.debugREACT)System.err.println("BEFORETOOLKITCALL");
          Move[] mvsNEWSET = (Move[])VectorToolkit.flattenVofV_AndTurnToArray(VectorToolkit.randomSubset(allConditions,this.sizeOfSet));
           //if(Debug.debugREACT)System.err.println("AFTERTOOLKITCALL");

          String sequence = "UNSET";
          // if(rtc.getGame()==null)System.exit(-34534);
          
          Move[] newSETCOPY = new Move[mvsNEWSET.length];
          for(int j=0;j<mvsNEWSET.length;j++){
              try{
                  newSETCOPY[j] = mvsNEWSET[j].getResetCopy();
                  rtc.cC.getC().saveDataToFile("NEWSET"+i, "server", "server", new Date().getTime(), new Date().getTime(), "", mvsNEWSET[j].getInfoAsVector());
              }catch (Exception e){
                  //rtc.getGame().rtc.cC.getC().saveDataToFile("NEWSET", "server", "server", new Date().getTime(), new Date().getTime(), sequence, null);
                  e.printStackTrace();
                 
              }
          }
          if(Debug.debugREACT)System.err.println("AFTERTOOLKITCALL2");
          
          
          
          return newSETCOPY;

       }


    public void swap(){
        
    }



}
