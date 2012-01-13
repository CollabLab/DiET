/*
 * JParameterSetupTable.java
 *
 * Created on 08 January 2008, 16:53
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.server.experimentmanager;
import java.io.File;

import javax.swing.JTable;
import javax.swing.table.TableColumn;


/**
 *
 * @author user
 */
public class JParameterSetupTable extends JTable{
    
    /**
     * Creates a new instance of JParameterSetupTable
     */
    JParameterSetupTableModel jpstmp;
    JParameterSetupPanel jpspEnclosingPanel;
    JParameterSetupTableCellRenderer jpstc;
    
    
    public JParameterSetupTable(File f) {
        super();
        jpstmp = new JParameterSetupTableModel(this,f);
        this.setModel(jpstmp);
        this.setVisible(true);
        System.out.println("CREATING TABLE");
        //this.setPreferredSize(new Dimension(900,900));
        //this.setBorder(BorderFactory.createEtchedBorder());
        this.setRowSelectionInterval(0, 0);
        this.setRowSelectionAllowed(true);
        this.setColumnSelectionAllowed(false);
        //this.setCellSelectionEnabled(false);
       
        this.setColumnWidths();
        jpstc = new JParameterSetupTableCellRenderer(this);
        this.setDefaultRenderer(new Object().getClass(), jpstc);
        this.setDefaultEditor(new Object().getClass(), new JParameterSetupTableCellEditor(this));
    }
    
    public void setColumnWidths(){
        TableColumn column = this.getColumnModel().getColumn(0);
        column.setPreferredWidth(250);
        column = this.getColumnModel().getColumn(1);
        column.setPreferredWidth(350);
        
    }
    
    public void addReferenceToEnclosingPanel(JParameterSetupPanel jpspEnclosingPanel){
        this.jpspEnclosingPanel = jpspEnclosingPanel;
    }
    
    public JParameterSetupPanel getEnclosingJPanel(){
        return this.jpspEnclosingPanel;
    }
    
    public JParameterSetupTableModel getModel(){
        return jpstmp;
    }
    
    
}
