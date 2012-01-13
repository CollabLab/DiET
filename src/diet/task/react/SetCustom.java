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
public class SetCustom extends Set {



      public Vector moves = new Vector();

     
      int x1=6;
      int x2=9;
      int x3=2;
      int x4=5;
      int x5=1;
      int x6=7; 
      int x7=3;
      int x8=8;
      int x9=4;


      boolean allowRing = true;

      public SetCustom(ReactTaskController rtc, int x1, int x2, int x3, int x4, int x5, int x6, int x7, int x8, int x9){
          super(rtc);
          this.x1=x1;
          this.x2=x2;
          this.x3=x3;
          this.x4=x4;
          this.x5=x5;
          this.x6=x6;
          this.x7=x7;
          this.x8=x8;
          this.x9=x9;
          initMoves();
      }




      public int sizeOfSet =1; //This is not the size of moves - it's used to determine each condition - see the vector allConditions below

      public void setSize(int i){
          this.sizeOfSet=i;
      }


    


public void initConditions(){
    allConditions = new Vector();
    Object simultaneous_congruent     =  new MoveAND(rtc.getGame(),rtc.pA, ""+x1,rtc.pB,""+x1);
    allConditions.addElement(simultaneous_congruent);

    Object simultaneous_complementary =  new MoveAND(rtc.getGame(),rtc.pA, ""+x2,rtc.pB,""+x3);
    allConditions.addElement(simultaneous_complementary);

    Object sequential_congruent1 =  new Vector();
           ((Vector)sequential_congruent1).addElement(new MoveONLYFirstHalf(rtc.getGame(),rtc.pA,""+x8));
           ((Vector)sequential_congruent1).addElement(new MoveONLYSecondHalf(rtc.getGame(),rtc.pB,""+x8));
    allConditions.addElement(sequential_congruent1);

    Object sequential_congruent2 =  new Vector();
           ((Vector)sequential_congruent2).addElement(new MoveONLYFirstHalf(rtc.getGame(),rtc.pB,""+x9));
           ((Vector)sequential_congruent2).addElement(new MoveONLYSecondHalf(rtc.getGame(),rtc.pA,""+x9));
    allConditions.addElement(sequential_congruent2);

    Object sequential_complementary1 =  new Vector();
           ((Vector)sequential_complementary1).addElement(new MoveONLYFirstHalf(rtc.getGame(),rtc.pA,""+x4));
           ((Vector)sequential_complementary1).addElement(new MoveONLYSecondHalf(rtc.getGame(),rtc.pB,""+x5));
    allConditions.addElement(sequential_complementary1);

    Object sequential_complementary2 =  new Vector();
           ((Vector)sequential_complementary2).addElement(new MoveONLYFirstHalf(rtc.getGame(),rtc.pB,""+x6));
           ((Vector)sequential_complementary2).addElement(new MoveONLYSecondHalf(rtc.getGame(),rtc.pA,""+x7));
    allConditions.addElement(sequential_complementary2);
}

Vector allConditions = new Vector();


 

 
 /*
  * 
  * 
  * 
  * 
  * PILOT1 and 2
  *  
  * 
  * 
  * int x1=4;        SIMCONGRUENTA , SIMCONGRUENTB (AND)
 int x2=3;           SIMCOMPA        
 int x3=1;           SIMCOMPB
 int x4=2;           SEQCOMPA1
 int x5=8;           SEQCOMPBA2
 int x6=5;           SEQCOMPAB1
 int x7=7;           SEQCOMPB(2)
 int x8=6;           SEQCONGA          SEQCONGB
 int x9=9;           SEQCONGA(1)       SEQCONGB(2)
  * 
  * 
  * 
  * 
  * 
  * 
  * 
  * 
  * 
  * 
  * 
  * 
  * 
  * 
  * 
  * 
  * 
  * 
  * 
  * 
  * PILOT3
  *  int x1=3;
 int x2=9;
 int x3=2;
 int x4=6;
 int x5=8;
 int x6=1;
 int x7=5;
 int x8=4;
 int x9=7;
  * 
  * 
  * 
  * 
  * 
  * 
  * PILOT4
  * int x1=6;
 int x2=9;
 int x3=2;
 int x4=5;
 int x5=1;
 int x6=7;
 int x7=3;
 int x8=8;
 int x9=4;
  * 
  * 
  * 
  * 
  */
 
 
 
 



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
