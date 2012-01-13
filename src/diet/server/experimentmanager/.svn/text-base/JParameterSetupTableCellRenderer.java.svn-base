/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.experimentmanager;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author user
 */
public class JParameterSetupTableCellRenderer extends DefaultTableCellRenderer{

JParameterSetupTable jpst;

public JParameterSetupTableCellRenderer(JParameterSetupTable jpst){
    super();
    this.jpst=jpst;
}


public Component getTableCellRendererComponent(JTable tabl, Object value, boolean isSelected, boolean hasFocus,int row, int column) {
        
        //if(value==null)return new JLabel("EMPTY");
        //if(tabl==null)return new JLabel("EMPTY2");
        //if(super==null)return new JLabel("EMPTY3");
        Component c = super.getTableCellRendererComponent(tabl,value.toString(),isSelected,hasFocus,row,column);
        
        
    
        if(value instanceof Component){
            return (Component)value;
        }
        
         if(value instanceof Boolean){
            JCheckBox jc = new JCheckBox();
            jc.setEnabled((Boolean)value);
            jc.setHorizontalAlignment(SwingConstants.CENTER);
            jc.setBackground(c.getBackground());
            return jc;
        }
        
        
        
        return super.getTableCellRendererComponent(tabl,value.toString(),isSelected,hasFocus,row,column);
        
        
        
        
      
        }
}    