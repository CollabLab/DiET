/*
 * JLexiconTable.java
 *
 * Created on 06 October 2007, 14:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.server.conversationhistory.ui;
import java.awt.Color;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

import diet.server.conversationhistory.ConversationHistory;


public class JLexiconTable extends JTable {///implements JDiETTableRowFilter{

  private JLexiconTableModel jltm;
  ///TableRowSorter sorter;
  ///RowFilter rf;
  

  public JLexiconTable(ConversationHistory c) {
    super();
    jltm = new JLexiconTableModel(this,c);
    this.setModel(jltm);
    this.setGridColor(Color.lightGray);
    setSize();
    //this.setAutoResizeMode(AUTO_RESIZE_ALL_COLUMNS);
    //this.setAutoCreateRowSorter(true);
    /// sorter = new JDiETTableSorter(jltm);
    ///this.setRowSorter(sorter);
  }


 private void setSize(){
    TableColumn column = null;
    for (int i = 0; i <7; i++) {
       column = this.getColumnModel().getColumn(i);
       
      if(i==1||i==2||i==3||i==4){
           column.setPreferredWidth(80);
      } 
      if (i == 6|| i==7) {
          column.setPreferredWidth(300); //sport column is bigger
      } else {
          //column.setPreferredWidth(100);
          //column.setMinWidth(100);
          //column.setMaxWidth(100);
      }
  }

 }
 
 public String getColumnName(int column){
     return jltm.getColumnName(column);
 }
public void refresh(){
     this.jltm.refresh();
 }

 ///public void setRowSorter(RowSorter trs){
 ///    super.setRowSorter(trs);
///     if(trs instanceof TableRowSorter){
///         this.sorter = (TableRowSorter)trs;
///     } 
///     if(rf!=null){   
///       this.sorter.setRowFilter(rf);
///     }     
/// }

 ///public boolean setTableRowFilter(String filterText){
    /// RowFilter<JLexiconTableModel, Object> rf2 = null;
  ///   rf =null;
  ///   sorter.setRowFilter(null);
   ///  try {
     ///        System.err.println("TABLE.SETROWFILTER "+filterText);
       ///      rf2 = RowFilter.regexFilter(filterText);                     
       /// } catch (java.util.regex.PatternSyntaxException e) {
      ///       return false;
     ///   }
     ///   sorter.setRowFilter(rf2);
     ///   rf = rf2;
      ///  return true;
        
 ///}
   


}
