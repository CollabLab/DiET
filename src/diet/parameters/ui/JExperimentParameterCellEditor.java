/*
 * JExperimentParameterCellEditor.java
 *
 * Created on 13 January 2008, 16:44
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.parameters.ui;
import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;

import diet.parameters.Fixed;
import diet.parameters.Parameter;
        
/**
 *
 * @author user
 */
public class JExperimentParameterCellEditor extends DefaultCellEditor{
    
    /**
     * Creates a new instance of JExperimentParameterCellEditor
     */
    Component editingComponent;
    JExperimentParameterTable jept;
    Parameter pBeingEdited;
    
    public JExperimentParameterCellEditor(JExperimentParameterTable jept) {
        super(new JTextField());
        this.jept=jept;
    }
    
    public Component getTableCellEditorComponent(JTable table,Object value,boolean isSelected,int row,int column){
          Parameter p = (Parameter)jept.jeptm.getExpSettings().elementAt(row);
          pBeingEdited =p;
          if(column==3){
              return new JExperimentSetParameterButton(jept.jeptm,p,"set");
          }
        
          System.out.println("GETTINGEDITINGCOMPONENT");
         
        
          
          if (p instanceof Fixed){
              Fixed f = (Fixed)p;
              //Vector v = (f.getPermittedValues());
              //v.addElement()
              JComboBox jc = new JComboBox(f.getPermittedValues());
              
              jc.setEditable(true);
              jc.setSelectedIndex(f.indexOfPermittedValue(value));
              editingComponent = jc;
              pBeingEdited =p;
              return jc;
          }

        
        Component comp = super.getTableCellEditorComponent(table, value,isSelected, row, column);
        editingComponent = comp;
        return comp;
       
    }
    
    public boolean stopCellEditing(){
        System.out.println("STOP EDITING");
        boolean isValid = false;
       try{
            Object value=null;
            if(this.editingComponent instanceof JComboBox){
                if (((JComboBox)editingComponent).getEditor().getItem()!=null){
                    value = ((JComboBox)editingComponent).getEditor().getItem();
                }
                else{
                 value = ((JComboBox)editingComponent).getSelectedItem();
                } 
            }
            else if(this.editingComponent instanceof JTextField){
                value = ((JTextField)editingComponent).getText();
                
            }
            if(value!=null) {
                System.out.println("VALUE IS---"+value);
                //UNCOMMENTBELOW
                isValid = jept.jeptm.setPendingSetting(this.pBeingEdited,value);
            }
                    
                    
       }catch(Exception e){
            System.err.println("ERROR CHANGING PARAMETER ");
       }
        
        
        editingComponent=null;
        return super.stopCellEditing();
       
    }
}
