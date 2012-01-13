/*
 * JExperimentParameterTableCellRenderer.java
 *
 * Created on 13 January 2008, 17:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.parameters.ui;

/**
 *
 * @author user
 */
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import diet.parameters.Parameter;


/**
 *
 * @author user
 */
public class JExperimentParameterTableCellRenderer extends DefaultTableCellRenderer{
    
    JExperimentParameterTable jept;
    
    /** Creates a new instance of JTurnsHorizontalTableCellRenderer */
    public JExperimentParameterTableCellRenderer(JExperimentParameterTable jept) {
        super();
        this.jept=jept;
        
    }
    
    public Component getTableCellRendererComponent(JTable tabl, Object value, boolean isSelected, boolean hasFocus,int row, int column) {
        
        if(value instanceof Component){
            return (Component)value;
        }
       
       /* if(column==2){
          Parameter p = (Parameter)jept.jeptm.getExpSettings().elementAt(row);
          if (p instanceof Fixed){
              
              Fixed f = (Fixed)p;
              JComboBox jc = new JComboBox(f.getPermittedValues());
              jc.setSelectedIndex(f.indexOfPermittedValue((String)value));
              //System.out.println("INDEXINDEXINDEX OF PERMITTEDVALUE IS "+f.indexOfPermittedValue((String)value)+"  VALUE IS: "+value.toString());
              return jc;
          }
        }
        
        */
        
        if(column==3){
            //return new JExperimentSetParameterButton(jept.jeptm,(Parameter)jept.jeptm.getExpSettings().elementAt(row),"SET");
            Parameter p = (Parameter)jept.jeptm.getExpSettings().elementAt(row);
            Object o = jept.jeptm.getPendingSetting(p);
            JButton jb = new JButton("set");
            if(!p.checkValIsOK(o)){
                jb.setEnabled(false);
            }
            
            return  jb;
            
            
            
           // JCheckBox jb = new JCheckBox();
           // jb.setSelected(isSelected);
           // return jb;
        }
        
        if (value==null)value="";
        
        return super.getTableCellRendererComponent(tabl,value.toString(),isSelected,hasFocus,row,column);
        
        
        
        
      
  }
}    