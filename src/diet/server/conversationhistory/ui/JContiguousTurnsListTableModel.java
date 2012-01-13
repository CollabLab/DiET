/*
 * JContiguousTurnsListTableModel.java
 *
 * Created on 06 October 2007, 23:20
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.server.conversationhistory.ui;


import java.util.Vector;

import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import diet.server.conversationhistory.Conversant;
import diet.server.conversationhistory.ConversationHistory;
import diet.server.conversationhistory.LexiconEntry;
import diet.server.conversationhistory.turn.ContiguousTurn;

public class JContiguousTurnsListTableModel extends DefaultTableModel {

  private ConversationHistory c;
  private JTable jt;
  private TableModel thisTableModel;
  private Vector data = new Vector();

  public JContiguousTurnsListTableModel(JTable jt,ConversationHistory c) {
     super();
     this.c=c;
     this.jt = jt;
     this.thisTableModel = this;
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
        return "DRev";
     }    
     else if(column ==13){
         return "TaggedText";
     }
      else if(column ==14){
         return "ContgNo";
      }
      else if(column ==15){
         return "Inconcistency";
      }
     else{
         return " ";
     }
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
   
   
   public void addRow(ContiguousTurn ct){

        final ContiguousTurn t2 = ct;
        
        SwingUtilities.invokeLater(new Runnable(){public void run(){
         //fireTableRowsInserted(c.getContiguousTurns().size()-1,c.getContiguousTurns().size());
         //jt.getRowSorter().
            //((TableRowSorter)jt.getRowSorter()).setSortable(1,false);
            data.addElement(t2);
            //((TableRowSorter)jt.getRowSorter()).setModel(thisTableModel);
            
            ///jt.setRowSorter(new JDiETTableSorter(thisTableModel));
            fireTableDataChanged();
             //fireTableStructureChanged();
            //jt.setModel(thisTableModel);
          
            //((TableRowSorter)jt.getRowSorter()).setModel(thisTableModel);
              //((TableRowSorter)jt.getRowSorter()).setSortable(1,true);
           
            
            
         }  });
        
        
        //data.addElement(v);
        //refresh();
        
    }
   
       
       
       
       
   
   
  public Object getValueAt(int x, int y){
 // try{  
     //System.out.println("GET VALUE AT "+x+" "+y);
    if(x>=data.size())return " "; 
    ContiguousTurn t = (ContiguousTurn)data.elementAt(x);
    if(y==0){
       
       return t.getSender().getUsername();
    }
    else if(y==1){
       return t.getTypingOnset();
    }
    else if(y==2){
       return t.getTypingReturnPressed();
    }
    else if(y==3){
       return t.getTypingReturnPressed()-t.getTypingOnset();       
    }
    else if(y==4){
        long typingtime = t.getTypingReturnPressed()-t.getTypingOnset();
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
        //System.out.println("GETTINGRECIP1");
        Vector v = t.getRecipients();
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
    }
    else if (y==8){
        if (t.getTypingWasBlockedDuringTyping())return "BLOCKED";
        return "OK";
    }
    else if(y==9){
        return t.getKeypressDeletes();
    }
     else if(y==10){
        return t.getDocDeletes();
    }
    else if(y==11){
        return t.getDocInsertsBeforeTerminal();
    }
    else if(y==12){
        return t.getDocEditScore();
    }    
    
    else if(y==13){
        String returnText="";
        Vector v = t.getWordsAsLexicalEntries();
        for(int i=0;i<v.size();i++){
            LexiconEntry lxe= (LexiconEntry)v.elementAt(i);
            returnText = returnText+lxe.getWord()+" ("+lxe.getPartOfSpeech()+") ";
        }
        return returnText;
    }
    else if(y==14){
        return t.getNumberOfTurns();
    }
    else if(y==15){
      //  System.out.println("CONTIGUOUS1");
        String value ="";
        boolean hasSameRecipients = t.getTurnsHaveSameRecipients();
        boolean hasSameApparentOrigin = t.getTurnsHaveSameApparentOrigin();
        if (hasSameRecipients&hasSameApparentOrigin){
            value = "OK";
        }
        else if (hasSameRecipients&!hasSameApparentOrigin){
            value = "Diff. O";
        }
        else if(!hasSameRecipients&hasSameApparentOrigin){
            value = "Diff. Recip";
        }
        else {
            value = "Diff O&R";
        }
        //System.out.println("CONTIGUOUS2");
        return value;
        
    }
    
   return " ";
   // }catch (Exception e){
   //     return "UI ERROR "+x+", "+y;
   // }
  }


 

 
  
  public int getRowCount(){
     if(data==null)return 0;
     return data.size();
  }
  public int getColumnCount(){
     return 16;
  }

  
     
     
  
}