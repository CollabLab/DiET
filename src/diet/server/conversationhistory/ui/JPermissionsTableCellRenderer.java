/*
 * JTurnsHorizontalTableCellRenderer.java
 *
 * Created on 07 October 2007, 14:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.server.conversationhistory.ui;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


/**
 *
 * @author user
 */
public class JPermissionsTableCellRenderer extends DefaultTableCellRenderer{
    
    JPermissionsTable jperm;
    
    /** Creates a new instance of JTurnsHorizontalTableCellRenderer */
    public JPermissionsTableCellRenderer(JPermissionsTable jperm) {
        super();
        this.jperm=jperm;
        
    }
    
    public Component getTableCellRendererComponent(JTable tabl, Object value, boolean isSelected, boolean hasFocus,int row, int column) {
         JLabel jl = new JLabel("LABELCASTINGERROR");
     
         if(value instanceof JLabel){
             jl = (JLabel)value;
             jl.setOpaque(true);
             if(isSelected){
                 jl.setForeground(Color.white);
                 jl.setBackground(Color.BLACK);
             }
             return jl;
         }
         else{
             return super.getTableCellRendererComponent(tabl,value,isSelected,hasFocus,row,column);
         }    
  }
}    