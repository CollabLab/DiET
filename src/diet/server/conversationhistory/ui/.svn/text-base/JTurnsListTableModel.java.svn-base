/*
 * JTurnsListTableModel.java
 *
 * Created on 06 October 2007, 16:22
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
import diet.server.conversationhistory.turn.Turn;

public class JTurnsListTableModel extends AbstractTableModel {

  private ConversationHistory c;
  private Vector data = new Vector();
  TableModel thisTableModel;
  JTable jt;

  public JTurnsListTableModel(JTable jt,ConversationHistory c) {
     super();
     this.c=c;
     this.jt = jt;
     this.thisTableModel = this;
  }

  public void addRow(Turn t){

        final Turn t2 = t;
        
        SwingUtilities.invokeLater(new Runnable(){public void run(){
         //fireTableRowsInserted(c.getContiguousTurns().size()-1,c.getContiguousTurns().size());
         //jt.getRowSorter().
            //((TableRowSorter)jt.getRowSorter()).setSortable(1,false);
            data.addElement(t2);
            //((TableRowSorter)jt.getRowSorter()).setModel(thisTableModel);
            
           /// jt.setRowSorter(new JDiETTableSorter(thisTableModel));
            fireTableDataChanged();
             //fireTableStructureChanged();
            //jt.setModel(thisTableModel);
          
            //((TableRowSorter)jt.getRowSorter()).setModel(thisTableModel);
              //((TableRowSorter)jt.getRowSorter()).setSortable(1,true);
           
            
            
         }  });
        
        
        //data.addElement(v);
        //refresh();
        
    }

  public boolean isCellEditable(int x, int y){
    return false;
  }

   public String getColumnName(int column){
     if(column==0){
         return "Sender";
     }
     else if(column==1){
         return "Onset";
     }
     else if (column==2){
         return "Enter";
     }
     else if(column ==3){
         return "E - O";
     }
     else if(column ==4){
         return "Speed";
     }
     else if(column ==5){
         return "App. Orig.";
     }
     else if(column ==6){
         return "Text";
     }
     else if(column ==7){
         return "Recipients";
     }
     else if(column ==8){
         return "Blocked";
     }
     else if(column ==9){
         return "KDels";
     }
     else if(column==10){
        return "DDels";
    }
    else if(column==11){
        return "DIns";
    }
    else if(column==12){
        return "DDels*N";
    } 
    else if(column==13){
        return "DIns*N";
    }    
    else if(column ==14){
         return "TaggedText";
    }
    else{
         return " ";
     }
   }

  public Object getValueAt(int x, int y){
    try{  
       //System.out.println("GET VALUE AT");
    
    if(x>=data.size())return " "; 
    Turn t = (Turn)data.elementAt(x);
    if(y==0){
       
       return t.getSender().getUsername();
    }
    else if(y==1){
       return t.getTypingOnsetNormalized();
    }
    else if(y==2){
       return t.getTypingReturnPressedNormalized();
    }
    else if(y==3){
       return t.getTypingReturnPressedNormalized()-t.getTypingOnsetNormalized();       
    }
    else if(y==4){
        long typingtime = t.getTypingReturnPressedNormalized()-t.getTypingOnsetNormalized();
        if(typingtime<=0)return 0;
        return ((long)t.getTextString().length())*1000/typingtime;    
    }
    else if(y==5){
        return t.getApparentSender().getUsername();
    }
    else if(y==6){
        return t.getTextString();
    }
    else if(y==7){
        Vector v = t.getRecipients();
        String names ="";
        for(int i=0;i<v.size();i++){
            Conversant c = (Conversant)v.elementAt(i);
            if(i==(v.size()-1)){
                names=names+c.getUsername();
            }
            else{
                 names = names+c.getUsername()+", ";
            }                        
        }
        return names;
    }
    else if (y==8){
        if (t.getTypingWasBlockedDuringTyping())return "BLOCKED";
        return "OK";
    }
    else if(y==9){
       
        return t.getKeypressDeletes();
    }
    else if(y==10){
         System.out.println("GETTING DOCDELETES FOR: "+t.getTextString());
        return t.getDocDeletes();
    }
    else if(y==11){
        return t.getDocInsertsBeforeTerminal();
    }
    else if(y==12){
        return t.getDocDelsScore();
    }
     else if(y==13){
        return t.getDocInsScore();
    }
    
    else if(y==14){
        String returnText="";
        Vector v = t.getWordsAsLexicalEntries();
        for(int i=0;i<v.size();i++){
            LexiconEntry lxe= (LexiconEntry)v.elementAt(i);
            returnText = returnText+lxe.getWord()+" ("+lxe.getPartOfSpeech()+") ";
        }
        return returnText;
    }
    
   return " ";
    }catch (Exception e){
        e.printStackTrace();
        return "UI ERROR";
    }
  }

  public Class getColumnClass(int column) {
               Class returnValue;
               if ((column >= 0) && (column < getColumnCount())) {
                 returnValue = getValueAt(0, column).getClass();
               } else {
                 returnValue = Object.class;
               }
               return returnValue;
  }
  
  public int getRowCount(){
    if(data==null)return 0;
     return this.data.size();
  }
  public int getColumnCount(){
     return 15;
  }



}