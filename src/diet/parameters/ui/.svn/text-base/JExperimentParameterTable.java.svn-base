/*
 * JExperimentParameterTable.java
 *
 * Created on 13 January 2008, 14:35
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.parameters.ui;
import java.awt.Color;

import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableColumn;

import diet.parameters.ExperimentSettings;
/**
 *
 * @author user
 */
public class JExperimentParameterTable extends JTable{
    
    JExperimentParametersTableModel jeptm;
    
    /** Creates a new instance of JExperimentParameterTable */
    public JExperimentParameterTable(ExperimentSettings expSettings) {
         super();
         jeptm = new JExperimentParametersTableModel(this,expSettings);
         
    this.setModel(jeptm);
    this.setGridColor(Color.lightGray);
    //this.setAutoResizeMode(AUTO_RESIZE_ALL_COLUMNS);
    this.setDefaultEditor(new Object().getClass(),new JExperimentParameterCellEditor(this));
    this.setDefaultRenderer(new Object().getClass(),new JExperimentParameterTableCellRenderer(this));
    this.setColumnWidths();
    
    
    
    }
    
    public void setColumnWidths(){
        TableColumn column = this.getColumnModel().getColumn(3);
        column.setPreferredWidth(80);
        column.setMaxWidth(80);
       
        
    }
    
    
    public void editingStopped(ChangeEvent e){
        System.out.println("EDITINGSTOPPEDTABLE");
        super.editingStopped(e);
        
        
        
    }
}
