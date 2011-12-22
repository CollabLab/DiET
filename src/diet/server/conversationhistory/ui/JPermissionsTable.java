

package diet.server.conversationhistory.ui;

import java.awt.Color;

import javax.swing.JTable;

import diet.server.Conversation;


public class JPermissionsTable extends JTable implements JDiETTableRowFilter {

  private JPermissionsTableModel jpermm;
  private JPermissionsTableCellRenderer jpcr;
 /// private RowFilter rf;
///  private TableRowSorter sorter;
  

  public JPermissionsTable(Conversation c) {
    super();
    jpermm = new JPermissionsTableModel(this,c);
    this.setModel(jpermm);
    this.setVisible(true);
    this.setGridColor(Color.lightGray);
    this.setAutoCreateColumnsFromModel(true);
    
    jpcr = new JPermissionsTableCellRenderer(this); 
    this.setDefaultRenderer(Object.class,jpcr);
    
  ///  sorter = new JDiETTableSorter(jpermm);
  ///  this.setRowSorter(sorter);
    
  }


 

///public RowSorter <? extends RowSorter> getSorter(){
///      return sorter;
///  } 
 
 
 
 public String getColumnName(int column){
     return jpermm.getColumnName(column);
 }
public void refresh(){
     this.jpermm.refresh();
 }

///public void setRowSorter(RowSorter trs){
///     super.setRowSorter(trs);
///     if(trs instanceof TableRowSorter){
///         this.sorter = (TableRowSorter)trs;
///     } 
///     if(rf!=null){   
///       this.sorter.setRowFilter(rf);
///     }     
/// }

 public boolean setTableRowFilter(String filterText){
  ///   RowFilter<JLexiconTableModel, Object> rf2 = null;
  ///   rf =null;
  ///   sorter.setRowFilter(null);
     try {
             System.err.println("TABLE.SETROWFILTER "+filterText);
     ///        rf2 = RowFilter.regexFilter(filterText);                     
        } catch (java.util.regex.PatternSyntaxException e) {
             return false;
        }
    ///    sorter.setRowFilter(rf2);
    ///    rf = rf2;
        return true;
        
 }


}