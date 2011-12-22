/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.task.collabMinitaskProceduralComms;

import diet.server.Participant;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author sre
 */
public class MovesFactory {


     Random r = new Random();
     Vector wordsAND = new Vector();
     Vector wordsXOR = new Vector();
     Vector wordsONLY = new Vector();

     Participant a;
     Participant b;


    
    Vector randomWords = new Vector();

     



     public MovesFactory(Participant a, Participant b){
         this.a=a;
         this.b=b;

 randomWords.addElement("beyond");
 randomWords.addElement("box");
 randomWords.addElement("carp");
 randomWords.addElement("carpet");
 randomWords.addElement("chuckle");
 randomWords.addElement("come");
 randomWords.addElement("dog");
 randomWords.addElement("fast");
 randomWords.addElement("fluff");
 randomWords.addElement("gone");
 randomWords.addElement("good");
 randomWords.addElement("grid");
 randomWords.addElement("group");
 randomWords.addElement("hair");
 randomWords.addElement("here");
 randomWords.addElement("hurry");
 randomWords.addElement("imp");
 randomWords.addElement("individual");
 //randomWords.addElement("it");
 randomWords.addElement("light");
 randomWords.addElement("lolly");
 randomWords.addElement("lovely");

 randomWords.addElement("measure");
 randomWords.addElement("morose");

 randomWords.addElement("needed");
 randomWords.addElement("nope");
 randomWords.addElement("not");


 randomWords.addElement("other");
 randomWords.addElement("others");
 randomWords.addElement("nothing");

  
  //randomWords.addElement("outer");
  randomWords.addElement("pet");
  randomWords.addElement("pit");
  
         randomWords.addElement("pig");
         randomWords.addElement("sausage");
         randomWords.addElement("scream");
         randomWords.addElement("seat");

         randomWords.addElement("something");
         randomWords.addElement("spangle");
                 randomWords.addElement("speed");
                 randomWords.addElement("star");
                 randomWords.addElement("strangle");
randomWords.addElement("tonic");

         randomWords.addElement("threat");
         randomWords.addElement("train");
         randomWords.addElement("trample");

         randomWords.addElement("vile");
         randomWords.addElement("wangle");


         randomWords.addElement("yes");
         randomWords.addElement("you");






         wordsAND.addElement("apple");
         wordsAND.addElement("banana");
         wordsAND.addElement("cherry");
         wordsAND.addElement("dfruit");
         wordsAND.addElement("everything");
         wordsAND.addElement("raspberry");
         wordsAND.addElement("tomato");


         wordsXOR.addElement("cake");
         wordsXOR.addElement("pie");
         wordsXOR.addElement("pastry");
         wordsXOR.addElement("dough");
         wordsXOR.addElement("pizza");
         wordsXOR.addElement("bread");
         wordsXOR.addElement("flour");



         wordsONLY.addElement("car");
         wordsONLY.addElement("bike");
         wordsONLY.addElement("motor");
         wordsONLY.addElement("caravan");
         wordsONLY.addElement("boat");
         wordsONLY.addElement("lorry");
         wordsONLY.addElement("truck");
         wordsONLY.addElement("yacht");
         wordsONLY.addElement("plane");
         wordsONLY.addElement("horse");



         for(int i=0;i<randomWords.size();i++){
             String s = (String)randomWords.elementAt(i);
             int choice = i % 3;
             if(choice==0){
                 wordsONLY.addElement(s);
             }
             else if(choice ==1){
                 wordsXOR.addElement(s);
             }
             else if (choice ==2){
                 wordsAND.addElement(s);
             }
             
         }



     }


     public Vector createRandomMoves(int number){
         return this.createRandomMoves(number, true, true, true);
     }

     public Vector createRandomMovesOLD(int number){
        number = 1;
        // number = 100;
         Vector wordsONLY_copy = (Vector)wordsONLY.clone();
         Vector wordsAND_copy = (Vector)wordsAND.clone();
         Vector wordsXOR_copy= (Vector)wordsXOR.clone();

         Vector createdMoves = new Vector();

         for(int i=0;i<number;i++){
            int rChoiceOfMoveType = 2;//r.nextInt(3);  ///?
            boolean wordTypeFound = false;


            if(rChoiceOfMoveType==0){
                 int rChoiceOfWord = r.nextInt(wordsONLY_copy.size());
                 String wrd = (String)wordsONLY_copy.elementAt(rChoiceOfWord);
                 int recipient = r.nextInt(2);
                 
                 if(recipient==0){
                      Move mve = new MoveONLY(a,wrd);
                      createdMoves.addElement(mve);
                 }
                 else{
                      Move mve = new MoveONLY(b,wrd);
                      createdMoves.addElement(mve);
                 }
                 wordsONLY_copy.removeElement(wrd);
             }
             else if(rChoiceOfMoveType == 1){
                 int rChoiceOfWord = r.nextInt(wordsXOR_copy.size());
                 String wrd = (String)wordsXOR_copy.elementAt(rChoiceOfWord);
                 Move mve = new MoveXOR(wrd);
                 createdMoves.addElement(mve);
                 wordsXOR_copy.removeElement(wrd);
             }
              else if(rChoiceOfMoveType == 2){
                 int rChoiceOfWord = r.nextInt(wordsAND_copy.size());
                 String wrd = (String)wordsAND_copy.elementAt(rChoiceOfWord);       
                 Move mve = new MoveAND(a,b,wrd);
                 createdMoves.addElement(mve);
                 wordsAND_copy.removeElement(wrd);
             }
         }
         return sortMoves(createdMoves);
     }



public Vector createRandomMoves(int number, boolean onlyALLOWED, boolean xorALLOWED, boolean andALLOWED){
        //number = 20;
        // number = 100;
         Vector wordsONLY_copy = (Vector)wordsONLY.clone();
         Vector wordsAND_copy = (Vector)wordsAND.clone();
         Vector wordsXOR_copy= (Vector)wordsXOR.clone();

         Vector createdMoves = new Vector();

         while (createdMoves.size()<number){
             System.err.println("..."+wordsONLY_copy.size()+"-----"+wordsXOR_copy.size()+"----"+wordsAND_copy.size());
             int mveType= r.nextInt(3);
             if(mveType==0&&onlyALLOWED&&wordsONLY_copy.size()>0){
                    int rChoiceOfWord = r.nextInt(wordsONLY_copy.size());
                    String wrd = (String)wordsONLY_copy.elementAt(rChoiceOfWord);
                    int recipient = r.nextInt(2);
                    if(recipient==0){
                         Move mve = new MoveONLY(a,wrd);
                         createdMoves.addElement(mve);
                    }
                    else{
                         Move mve = new MoveONLY(b,wrd);
                        createdMoves.addElement(mve);
                    }
                    wordsONLY_copy.remove(wrd);
             }
             else if (mveType==1&&xorALLOWED&&wordsXOR_copy.size()>0){
                   int rChoiceOfWord = r.nextInt(wordsXOR_copy.size());
                   String wrd = (String)wordsXOR_copy.elementAt(rChoiceOfWord);
                   Move mve = new MoveXOR(wrd);
                   createdMoves.addElement(mve);
                   wordsXOR_copy.removeElement(wrd);
             }
             else if (mveType==2&&andALLOWED&&wordsAND_copy.size()>0){
                 int rChoiceOfWord = r.nextInt(wordsAND_copy.size());
                 String wrd = (String)wordsAND_copy.elementAt(rChoiceOfWord);
                 Move mve = new MoveAND(a,b,wrd);
                 createdMoves.addElement(mve);
                 wordsAND_copy.removeElement(wrd);
             }
         }
         return sortMoves(createdMoves);
     }


     public Vector sortMoves(Vector vMoves){
         Vector vSorted = new Vector();
         Vector vMovesCopy = (Vector)vMoves.clone();
         while(vMovesCopy.size()>0){
             Object o = findSmallestValue(vMovesCopy);
             vMovesCopy.remove(o);
             vSorted.addElement(o);
         }


         return vSorted;
     }

     public Move findSmallestValue(Vector v){
         if (v.size()==0)return null;
         Move smallestValue = (Move)v.elementAt(0);
         for(int i=0;i<v.size();i++){
             Move comparedMove = (Move)v.elementAt(i);
             if(comparedMove.getName().compareToIgnoreCase(smallestValue.getName())<0){
                 smallestValue = comparedMove;
             }
         }
         return smallestValue;
     }

}



/*
 *
 * 8 times ONLY
 * 8 times OR
 * 8 times AND
 *
 *
 * 15 times 2 of each
 * 10 times 3 of each
 *
 *
 *
 */