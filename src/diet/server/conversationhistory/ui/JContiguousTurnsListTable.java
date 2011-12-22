/*
 * JContiguousTurnsListTable.java
 *
 * Created on 06 October 2007, 23:20
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.server.conversationhistory.ui;

import java.awt.Color;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import diet.server.conversationhistory.ConversationHistory;
import diet.server.conversationhistory.turn.ContiguousTurn;


public class JContiguousTurnsListTable extends JTable {///implements JDiETTableRowFilter{

  private JContiguousTurnsListTableModel jctltm;
  ///private TableRowSorter  sorter;
  ///private RowFilter rf;
  

  public JContiguousTurnsListTable(ConversationHistory c) {
    super();
  
    jctltm = new JContiguousTurnsListTableModel(this,c);
    this.setModel(jctltm);
    this.setGridColor(Color.lightGray);
    setSize();
    //this.setAutoResizeMode(AUTO_RESIZE_ALL_COLUMNS);
    //this.setAutoCreateRowSorter(true);
    ///sorter = new JDiETTableSorter(jctltm);
    //sorter.setSortsOnUpdates(true);
    //sorter.set
    /*sorter.setComparator(0,new JTableComparator());
    sorter.setComparator(1,new JTableComparator());
    sorter.setComparator(2,new JTableComparator());
    sorter.setComparator(3,new JTableComparator());
    sorter.setComparator(4,new JTableComparator());
    sorter.setComparator(5,new JTableComparator());
    sorter.setComparator(6,new JTableComparator());
    sorter.setComparator(7,new JTableComparator());
    sorter.setComparator(8,new JTableComparator());
    sorter.setComparator(9,new JTableComparator());
    sorter.setComparator(10,new JTableComparator());
    sorter.setComparator(11,new JTableComparator());
    sorter.setComparator(12,new JTableComparator());*/
    //this.setFillsViewportHeight(true);
    
    
    
   /// this.setRowSorter(sorter);
   /// System.err.println("Setting up ");
    
  }

  ///public RowSorter <? extends RowSorter> getSorter(){
  ///    return sorter;
  ///}

  
 private void setSize(){
    TableColumn column = null;
    for (int i = 0; i <11; i++) {
      column = this.getColumnModel().getColumn(i);
      if(i==1||i==2||i==3||i==4||i==8||i==9){
          column.setPreferredWidth(80);
      }
      else if (i==0||i==5){
          column.setPreferredWidth(180);
      }
      else if (i == 6) {
          column.setPreferredWidth(280); 
      }
       else if (i == 7) {
          column.setPreferredWidth(200); 
      } 
      else if(i==13){
           column.setPreferredWidth(250);         
      }
      else 
          column.setPreferredWidth(45);       
          //column.setPreferredWidth(100);
          //column.setMinWidth(100);
          //column.setMaxWidth(100);
      }
 

 }
 
 public String getColumnName(int column){
     return jctltm.getColumnName(column);
 }


 public void createDefaultColumnsFromModel2(){
     //System.out.println("CREATING DEFAULT MODELS FROM COLUMN, CREATING DEFAULT MODELS FROM COLUMN");
     if(this.jctltm!=null){
         this.setModel(jctltm);
     ///    this.setRowSorter(sorter);
     ///    if(sorter!=null)sorter.modelStructureChanged();
     } 
     super.createDefaultColumnsFromModel();
 }
 
 public TableColumnModel createDefaultColumnModel2(){
     //System.out.println("CREATING DEFAULT MODELS FROM COLUMN, CREATING DEFAULT MODELS FROM COLUMN");
     if(this.jctltm!=null){
         this.setModel(jctltm);
     
      
    ///  sorter.modelStructureChanged();
     }
      return super.createDefaultColumnModel();
 }
 
 public TableColumnModel getColumnModel2(){
     //System.out.println("CREATING DEFAULT MODELS FROM COLUMN, CREATING DEFAULT MODELS FROM COLUMN");
     if(this.jctltm!=null){
         this.setModel(jctltm);
     ///    if(sorter!=null)sorter.modelStructureChanged();
     }
     return super.getColumnModel();
 }
 
 
 public void addRow(ContiguousTurn t){
     this.jctltm.addRow(t);
 }
 
 public void refresh(){
     this.jctltm.refresh();
 }
 
 
/// public void setRowSorter(RowSorter trs){
   ///  super.setRowSorter(trs);
    /// if(trs instanceof TableRowSorter){
    ///     this.sorter = (TableRowSorter)trs;
     ///} 
    /// if(rf!=null){   
    ///   this.sorter.setRowFilter(rf);
    /// }     
/// }

/// public boolean setTableRowFilter(String filterText){
///     RowFilter<JLexiconTableModel, Object> rf2 = null;
   ///  rf =null;
  ///   sorter.setRowFilter(null);
    /// try {
      ///       System.err.println("TABLE.SETROWFILTER "+filterText);
          ///   rf2 = RowFilter.regexFilter(filterText);                     
       /// } catch (java.util.regex.PatternSyntaxException e) {
             ///return false;
        ///}
       /// sorter.setRowFilter(rf2);
       /// rf = rf2;
        ///return true;
        
 ///}
 
 
 
 
}
