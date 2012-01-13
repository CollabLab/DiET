/*
 * JLexiconTableModel.java
 *
 * Created on 06 October 2007, 14:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.server.conversationhistory.ui;

import java.util.Vector;

import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import diet.server.conversationhistory.Conversant;
import diet.server.conversationhistory.ConversationHistory;
import diet.server.conversationhistory.LexiconEntry;
import diet.server.conversationhistory.PartOfSpeechLexicon;
import diet.server.conversationhistory.turn.Turn;

public class JLexiconTableModel extends AbstractTableModel {

  private PartOfSpeechLexicon psl;
  private TableModel thisTableModel;
  private JTable jt;

  public JLexiconTableModel(JTable jt,ConversationHistory c) {
     super();
     this.psl = c.getLexicon();    
     this.thisTableModel = this;
     this.jt = jt;
  }

  public void refresh(){
    // System.out.println("Refresh");
      
     SwingUtilities.invokeLater(new Runnable(){public void run(){
        // jt.setRowSorter(new JDiETTableSorter(thisTableModel));
         
         fireTableDataChanged();
         ///jt.setRowSorter(new JDiETTableSorter(thisTableModel));    
     }  });
     //this.fireTableDataChanged();
     //this.fireTableStructureChanged();
  }

  public boolean isCellEditable(int x, int y){
    return false;
  }

   public String getColumnName(int column){
       
     if(column==0){
         return "Word";
     }
     else if(column==1){
         return "Part Of Speech";
     }
     else if (column==2){
         return "Occurrences";
     }
     else if(column ==3){
         return "Turns containing";
     }
     else if(column ==4){
         return "Turn of last use";
     }
     else if(column ==5){
         return "Origin";
     }
     else if(column ==6){
         return "Recipients";
     }
     else{
         return"";
     }
   }

  public Object getValueAt(int x, int y){
      
    Vector vle = (Vector)psl.getLexicon();
    if(x>=vle.size())return null;
    LexiconEntry le = (LexiconEntry)vle.elementAt(x);
    if(y==0){
       return le.getWord();
    }
    else if(y==1){
       return le.getPartOfSpeech();
    }
    else if(y==2){
       return le.getOccurrences();
    }
    else if(y==3){
       return le.getTurnsContainingWord().size();       
    }
    else if(y==4){
        return (((Turn)le.getTurnsContainingWord().elementAt(le.getTurnsContainingWord().size()-1)).getTurnNo());
    }
    else if(y==5){
        Vector v = le.getConversantsWhoTypedWord();
        String names ="";
          //      System.out.println("GETTINGRECIP3");
        for(int i=0;i<v.size();i++){
            Conversant c = (Conversant)v.elementAt(i);
            //        System.out.println("GETTINGRECIP4");
            if(i==(v.size()-1)){
                names=names+c.getUsername();
            }
            else{
                 names = names+c.getUsername()+", ";
            }
        }    
        //Vector v = le.getConversantsWhoTypedWord();
        //String names ="";
        //for(int i=0;i<v.size();i++){
        //    Conversant c = (Conversant)v.elementAt(i);
        //    names = names+", "+c.getUsername();
        //}
        return names;
    }
    else if(y==6){
        Vector v = le.getConversantsWhoReceivedWord();
        String names ="";
          //      System.out.println("GETTINGRECIP3");
        for(int i=0;i<v.size();i++){
            Conversant c = (Conversant)v.elementAt(i);
            //        System.out.println("GETTINGRECIP4");
            if(i==(v.size()-1)){
                names=names+c.getUsername();
            }
            else{
                 names = names+c.getUsername()+", ";
            }
           
        }
            //    System.out.println("GETTINGRECIP5");        
        return names;
        
        
        
        
        
        
        
        
        
        
        
       // Vector v = le.getConversantsWhoReceivedWord();
       // String names ="";
       // for(int i=0;i<v.size();i++){
       //     Conversant c = (Conversant)v.elementAt(i);
       //     names = names+", "+c.getUsername();
       // }
       // return names;
    }
    
   return " ";
  }

  
  public Class getColumnClass(int column) {
             try{
               Class returnValue;
               if ((column >= 0) && (column < getColumnCount())) {
                 returnValue = getValueAt(0, column).getClass();
               } else {
                 returnValue = Object.class;
               }
               return returnValue;
             }catch(Exception e){
                 return Object.class;
             }  
  }
  
  
  public int getRowCount(){
     return this.psl.getLexicon().size();
  }
  public int getColumnCount(){
     return 7;
  }



}
