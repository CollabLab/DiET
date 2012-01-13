/*
 * JTurnsListTable.java
 *
 * Created on 06 October 2007, 16:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.server.conversationhistory.ui;

import java.awt.Color;
import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

import diet.server.conversationhistory.ConversationHistory;
import diet.server.conversationhistory.turn.Turn;

public class JTurnsListTable extends JTable {///implements JDiETTableRowFilter{

  private JTurnsListTableModel jtltm;
 /// private TableRowSorter sorter;
 /// private RowFilter rf;
  

  public JTurnsListTable(ConversationHistory c) {
    super();
    jtltm = new JTurnsListTableModel(this,c);
    this.setModel(jtltm);
    this.setGridColor(Color.lightGray);
    setSize();
    
  ///   sorter = new JDiETTableSorter(jtltm);
    //this.setAutoResizeMode(AUTO_RESIZE_ALL_COLUMNS);
    //this.setAutoCreateRowSorter(true);
   /// this.setRowSorter(sorter);
    
  }

  /**
   * Called by ExperimentManager to instantiate Table of pre-existing experiment.
   * @param vTurns Turns from pre-existing experiment
   */
  public JTurnsListTable(Vector vTurns){
       super();
       jtltm = new JTurnsListTableModel(this,null);
       this.setModel(jtltm);
       
       this.setGridColor(Color.lightGray);
       setSize();
       for(int i=0;i<vTurns.size();i++){
           Turn t = (Turn)vTurns.elementAt(i);
           jtltm.addRow(t);
       }
  }
  

 private void setSize(){
    TableColumn column = null;
    for (int i = 0; i <11; i++) {
      column = this.getColumnModel().getColumn(i);
      if(i==1||i==2||i==3||i==4||i==8||i==9||i==10||i==11||i==12){
          column.setPreferredWidth(75);
          column.setMinWidth(75);
      }
      else if (i==0||i==5){
          column.setPreferredWidth(140);
          //column.setMinWidth(140);
      }
      else if (i == 6) {
          column.setPreferredWidth(150);
          //column.setMinWidth(150);
      }  
       else if (i == 7) {
          column.setPreferredWidth(150); 
          //column.setMinWidth(150);
      }  
      else if(i==10){
           column.setPreferredWidth(180);
           //column.setMinWidth(180);
      }
      else {
          //column.setPreferredWidth(100);
          //column.setMinWidth(100);
          //column.setMaxWidth(100);
      }
  }

 }
 
 public String getColumnName(int column){
     return jtltm.getColumnName(column);
 }
public void addRow(Turn t){
     this.jtltm.addRow(t);
 }

///public void setRowSorter(RowSorter trs){
   ///  super.setRowSorter(trs);
  ///   if(trs instanceof TableRowSorter){
     ///    this.sorter = (TableRowSorter)trs;
  ///   } 
    /// if(rf!=null){   
    ///   this.sorter.setRowFilter(rf);
   ///  }     
 ///}

 ///public boolean setTableRowFilter(String filterText){
///     RowFilter<JLexiconTableModel, Object> rf2 = null;
///     rf =null;
///     sorter.setRowFilter(null);
///     try {
///             System.err.println("TABLE.SETROWFILTER "+filterText);
///             rf2 = RowFilter.regexFilter(filterText);                     
///        } catch (java.util.regex.PatternSyntaxException e) {
///             return false;
///        }
///        sorter.setRowFilter(rf2);
///        rf = rf2;
///        return true;
        
 ///}




}
