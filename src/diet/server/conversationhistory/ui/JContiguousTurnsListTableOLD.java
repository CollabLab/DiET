/*
 * JContiguousTurnsListTableOLD.java
 *
 * Created on 06 October 2007, 23:20
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.server.conversationhistory.ui;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

import diet.server.conversationhistory.ConversationHistory;


public class JContiguousTurnsListTableOLD extends JTable {

  private JContiguousTurnsListTableModelOLD jctltm;
  

  public JContiguousTurnsListTableOLD(ConversationHistory c) {
    super();
  
    jctltm = new JContiguousTurnsListTableModelOLD(this,c);
    this.setModel(jctltm);
    setSize();
    //this.setAutoResizeMode(AUTO_RESIZE_ALL_COLUMNS);
    //this.setAutoCreateRowSorter(true);
    ///TableRowSorter <TableModel> sorter = new TableRowSorter <TableModel>(jctltm);
    //sorter.setSortsOnUpdates(true);
    
    
    ///this.setRowSorter(sorter);
    System.err.println("Setting up ");
    
  }


 private void setSize(){
    TableColumn column = null;
    for (int i = 0; i <11; i++) {
      column = this.getColumnModel().getColumn(i);
      if(i==1||i==2||i==3||i==4||i==8||i==9){
          column.setPreferredWidth(50);
      }
      else if (i==0||i==5){
          column.setPreferredWidth(180);
      }
      else if (i == 6) {
          column.setPreferredWidth(150); 
      }    
      else if(i==10){
           column.setPreferredWidth(250);         
      }
      else 
          column.setPreferredWidth(100);       
          //column.setPreferredWidth(100);
          //column.setMinWidth(100);
          //column.setMaxWidth(100);
      }
 

 }
 
 public String getColumnName(int column){
     return jctltm.getColumnName(column);
 }
 public void refresh(){
     this.jctltm.refresh();
 }

}
