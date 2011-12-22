//Replace the remove method with a standard implementation in VectorToolkit
//There is a concurrentmodification error.
//This needs to be replaced in StringOfDocChangeInserts
//There is something strange with the "ENTERS", as it might be that as the info is sent
//before
//SDIPRIOR INSERT IS OFF BY 1 because of the implicit enter
//   does this apply for edits of prior? (yes)
//   does this apply for last edit of current (no)
//       This is because in the current turn there isn't an added enter.
//       
//   IF IT's AN EDIT SEQUENCE
//       IF LAST TURN IS ENTER THEN (ADD 1 FROM THE RIGHT)
//


//CHANGE SO IT CAN'T REMOVE OR INSERT INTO HEADER TEXT
//CHANGE SO THAT


//Scenario...to test both the speaker's text and the final text

//TRY THE FOLLOWING AT BEGINNING/MIDDLE/END OF TURN
//Edit (DEL MULTI SELECT) of self same turn during typing
//Edit (DEL MULTI SELECT) of self same turn new floor
//Edit (DEL MULTI SELECT) of self following turn with ENTER during typing (person types and presses enter and continues)
//Edit (DEL MULTI SELECT) of self following turn with ENTER new floor   (person types and presses enter and continues)

//Edit (INS) of self same turn during typing
//Edit (INS) of self same turn new floor
//Edit (INS) of self following turn with ENTER during typing (person types and presses enter and continues)
//Edit (INS) of self following turn with ENTER new floor   (person types and presses enter and continues)

//Edit (DEL MULTI SELECT INS) of self same turn during typing
//Edit (DEL MULTI SELECT INS) of self same turn new floor
//Edit (DEL MULTI SELECT INS) of self following turn with ENTER during typing (person types and presses enter and continues)
//Edit (DEL MULTI SELECT INS) of self following turn with ENTER new floor   (person types and presses enter and continues)

//Same as above for prior

/*
 * StringOfDocChangeInserts.java
 *
 * Created on 26 October 2007, 23:11
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.server;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

import diet.server.CbyC.DocChange;
import diet.server.CbyC.DocInsert;
import diet.server.CbyC.DocRemove;

/**
 * This class is still under construction. It was originally developed for a demo, and needs
 * radical overhaul. If attempting to write a WYSIWYG intervention, it is advisable to look at
 * ConversationControllerINTERCEPTUHH and copy the relevant fragments. This class is only used by WYSIWYG
 * implementations.
 * 
 * @author user
 */
public class StringOfDocChangeInsertsOfPriorText implements Serializable {
    
    Vector sequence2 = new Vector();
    boolean errorRemoving = false;
    boolean errorInserting = false;
    
    public StringOfDocChangeInsertsOfPriorText(String originalText,DocChange dc) {
        sequence2 = this.getInsEquivalentOfStringDEPRECATED(originalText);
        //Conversation.printWSln("SDI","");
        //Conversation.printWSln("SDI","STRINGOFDOCCHANGES: ("+originalText+")");
       
            if(dc instanceof DocInsert){
                DocInsert docI = (DocInsert)dc;
                //Conversation.printWSln("SDI","INSERT "+docI.offs+".."+docI.str);
                insertDEPRECATED(docI);
            }
            else if (dc instanceof DocRemove){
                DocRemove docR = (DocRemove)dc;
                //Conversation.printWSln("SDI","REMOVE "+docR.offs+".."+docR.len);
                removeDEPRECATED(docR);
            }
        
        System.err.println("(END) "+originalText);
    }
    
    
    public void insertDEPRECATED(DocInsert docI){
        System.err.print("("+docI.getStr()+")");
        try{
          int offsetFrmLeft = sequence2.size()-docI.getOffs();
          if(offsetFrmLeft<0){
              offsetFrmLeft=0;
               //Conversation.printWSln("SDI","(RESETINSERTOFFSET1: "+docI.getStr());
          }
          if(offsetFrmLeft>sequence2.size()){
              //Conversation.printWSln("SDI","(RESETINSETOFFSET2: "+docI.getStr());
              offsetFrmLeft=sequence2.size()-1;
          }
     
          
          sequence2.insertElementAt(docI,offsetFrmLeft);
          
        }catch(Exception e){
            //Conversation.printWSln("SDI","Error in StringOfDocChangeInserts");
        }  
          
        //Conversation.printWSln("SDI","(ENDINSERT)");  
    }
    
    
    public void removeDEPRECATED(DocRemove dR){
        //Conversation.printWSln("SDI","(BEGINREMOVE)");
        try{
          int offsetFrmLeft = sequence2.size()-dR.getOffs();
          if(offsetFrmLeft<0){
              //Conversation.printWSln("SDI","(RESETREMOVEOFFSET1)");
              offsetFrmLeft=0;
          }
          if(offsetFrmLeft>sequence2.size()){
              //Conversation.printWSln("SDI","(RESETREMOVEOFFSET2)");
              offsetFrmLeft=sequence2.size()-1;
          }
          int len = dR.getLen();
          if(offsetFrmLeft+len>=sequence2.size()){
              //Conversation.printWSln("SDI","(RESETREMOVEOFFSET3)");
              len = (sequence2.size()-1)-offsetFrmLeft;
          }  
          //sequence2.replace(sequence.length()-offsetFrmRight,sequence.length()-offsetFrmRight+length,"");
          //for(int i=offsetFrmLeft;i)
          //Conversation.printWSln("SDI","(REMOVEOFFS)");
          List sublistToRemove = sequence2.subList(offsetFrmLeft,offsetFrmLeft+len);
          sequence2.removeAll(sublistToRemove);
          //sequence.replace(offsetFrmLeft,offsetFrmLeft+remove.getLen(),"");
        }catch (Exception e){
            //Conversation.printWSln("SDI","Error in DocumentChangeSequenceOffsetFromRight Removing offsetFrmRight3: "+dR.getOffs()+" "+dR.getLen());
            e.printStackTrace();
        }   
         //Conversation.printWSln("SDI","(ENDREMOVE)");
    }
     
   
    
    public String getStringDEPRECATED(){
        String s ="";
        for(int i=0;i<sequence2.size();i++){
            DocInsert s2 = (DocInsert)sequence2.elementAt(i);
            s=s+s2.getStr();
        }
        return s;
    }
    
    static public String getSubSequenceStringDEPRECATED(Vector v){
        String s ="";
        for(int i=0;i<v.size();i++){
            DocInsert s2 = (DocInsert)v.elementAt(i);
            s=s+s2.getStr();
        }
        return s;
    }
    
    static public Vector getInsEquivalentOfStringDEPRECATED(String s){
        Vector v = new Vector();
        for(int i=0;i<s.length();i++){
            DocInsert dI = new DocInsert(0,""+s.charAt(i),null);
            v.addElement(dI);
        }
        return v;
    }
    
    public int getSizeDEPRECATED(){
        return sequence2.size();
    }
    public DocInsert getInsDEPRECATED(int i){
        return (DocInsert)sequence2.elementAt(i);
    }
    public Vector getSequenceDEPRECATED(){
        return sequence2;
    }
    
    public Vector getSubSequenceDEPRECATED(int startIndex, int finishIndex){
        Vector v = new Vector();
        for(int i=startIndex;i<finishIndex&&i<sequence2.size();i++){
            Object o = sequence2.elementAt(i);
            v.addElement(o);
        }
        return v;
    }
    
    
}
