/*
 * JTurnsHorizontalTableModel.java
 *
 * Created on 07 October 2007, 12:39
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.server.conversationhistory.ui;
import java.awt.Color;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import diet.server.conversationhistory.Conversant;
import diet.server.conversationhistory.ConversationHistory;
import diet.server.conversationhistory.turn.Turn;

public class JTurnsHorizontalTableModel extends AbstractTableModel {

  private ConversationHistory c;
  private JTable jt;
  private TableModel thisTableModel;

  public JTurnsHorizontalTableModel(JTable jt,ConversationHistory c) {
     super();
     this.c=c;
     this.jt = jt;
     this.thisTableModel = this;
     
  }

  public void refresh(){
     //System.out.println("Refresh");
     SwingUtilities.invokeLater(new Runnable(){
         public void run(){
       ///  jt.setRowSorter(new JDiETTableSorter(thisTableModel));    
         //fireTableDataChanged();
         fireTableStructureChanged();
     }  });
    // jt.setRowSorter(new JDiETTableSorter(thisTableModel));
     //this.fireTableDataChanged();
     //this.fireTableStructureChanged();
  }

  public boolean isCellEditable(int x, int y){
    return false;
  }

   public String getColumnName(int column){

     if(column==0){
         return "Onset";
     }
     else if (column==1){
         return "Enter";
     }
     if (column<=c.getConversants().size()+1){
         Conversant c2 = (Conversant)c.getConversants().elementAt(column-2);
         return c2.getUsername();
     }
     else{
         return " ";
     }
   }

  public Object getValueAt(int x, int y){
    try{  
       //System.out.println("GET VALUE AT");
    Vector turns = c.getTurns();
    Vector conversants = c.getConversants();
    if(x>=turns.size())return " "; 
    if(y>=conversants.size()+2)return " ";//TOOLARGE "+x+ ","+y;
    Turn t = (Turn)turns.elementAt(x);
     if(y==0){
       return t.getTypingOnsetNormalized();
    }
    else if(y==1){
       String rtxts = "";
       for(int i=0;i<t.getRecipients().size();i++){
          // rtxts = rtxts + ((Conversant)t.getRecipients().elementAt(i)).getUsername();
       }
       return rtxts+t.getTypingReturnPressedNormalized();
    }
    else {
        Conversant c2 = (Conversant)conversants.elementAt(y-2);
        JLabel jl = new JLabel();
        if(t.getSender().getUsername().equalsIgnoreCase("server")){
            jl.setForeground(Color.BLACK);
            jl.setBackground(Color.LIGHT_GRAY);
            
                   
            if(t.getSender().equals(c2)){
                jl.setText(t.getTextString());
                return jl;
            }
        }
        
        if(t.getSender().equals(c2)){
            return t.getTextString();
        }
        else if(t.getApparentSender().equals(c2)){
            jl = new JLabel("APPARENT ORIGIN");
            jl.setForeground(Color.BLACK);
            jl.setBackground(Color.red);
            return  jl;
        }        
        else if(!t.getRecipients().contains(c2)){ 
            if(c2.getUsername().equalsIgnoreCase("server")){
                jl.setForeground(Color.BLACK);
                jl.setBackground(Color.LIGHT_GRAY);
                return jl;
            }  
            jl =  new JLabel("NOT RECEIVED");
            jl.setForeground(Color.WHITE);
            jl.setBackground(Color.black);
            return  jl;        
        }
        
        else{
           return " ";//"SENDERIS "+t.getApparentSender();
        }
        
    }    
    }catch (Exception e){
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
  
     return this.c.getTurns().size();
  }
  public int getColumnCount(){
     
      if(c.getConversants().size()==0)return 3;
      return 2+c.getConversants().size();
     
  }



}