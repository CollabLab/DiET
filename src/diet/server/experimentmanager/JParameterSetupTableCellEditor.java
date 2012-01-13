/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.experimentmanager;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;

import diet.nextversionpreview.matisse.JListEditor;
import diet.parameters.Fixed;
import diet.parameters.Parameter;
import diet.parameters.StringListParameter;

/**
 *
 * @author user
 */
public class JParameterSetupTableCellEditor extends DefaultCellEditor{
    
    /**
     * Creates a new instance of JExperimentParameterCellEditor
     */
    Component editingComponent;
    //JFrame editingComponentJFrame;
    JParameterSetupTable jpst;
    Parameter pBeingEdited;
    int row=0;
    int col=0;
    
    public JParameterSetupTableCellEditor(JParameterSetupTable jpst) {
        super(new JTextField());
        this.jpst=jpst;
    }
    
    public Component getTableCellEditorComponent(JTable table,Object value,boolean isSelected,int row,int column){
          this.row=row;
          this.col=column;
          Parameter p = (Parameter)jpst.getModel().expparameters.elementAt(row);
          pBeingEdited =p;
          
        
          System.out.println("GETTINGEDITINGCOMPONENT");
         
          if (p instanceof StringListParameter){
              JListEditor jl = new JListEditor(this,(StringListParameter)p);
              editingComponent = jl;
              //JFrame jf = new JFrame();
              //jf.getContentPane().add(jl);
              //jf.pack();
              //jf.setVisible(true);
              return new JLabel("EDITING");
              
          }
          
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
    
    public Object getCellEditorValue(){
       Object o = super.getCellEditorValue();
       System.out.println("STOP EDITING");
        Object value=null;
       boolean isValid = false;
       try{
           
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
            else if(this.editingComponent instanceof JListEditor){
                System.out.println("HERE");
                JListEditor jle = (JListEditor)this.editingComponent;
                System.out.println("HERE1");
                value = jle.getUpdatedList();
                System.out.println("VALUESIZE "+jle.getUpdatedList().size());
                if(jle.updateParameter()){
                    System.out.println("HERE2");
                    value = jle.getUpdatedList();
                    System.out.println("HERE3");
                    System.out.println("HERE4");
                    
                }
                
              
                
                
            }
            if(value!=null) {
                System.out.println("VALUE IS---"+value);
                //UNCOMMENTBELOW
                //jpst.getModel().setValueAt(value, row, col);
            }
                    
                    
       }catch(Exception e){
            System.err.println("ERROR CHANGING PARAMETER ");
            if(e!=null){
                System.err.println("ERROR MESSAGE IS: "+e.getMessage());
            }
       }
        
        //row=null;
        //col=null;
        editingComponent=null;
       // return true;
        return value;
       
    }
    
    public boolean stopCellEditing2(){
        
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
            else if(this.editingComponent instanceof JListEditor){
                System.out.println("HERE");
                JListEditor jle = (JListEditor)this.editingComponent;
                System.out.println("HERE1");
                value = jle.getUpdatedList();
                System.out.println("VALUESIZE "+jle.getUpdatedList().size());
                if(jle.updateParameter()){
                    System.out.println("HERE2");
                    value = jle.getUpdatedList();
                    System.out.println("HERE3");
                    System.out.println("HERE4");
                    
                }
                
              
                
                
            }
            if(value!=null) {
                System.out.println("VALUE IS---"+value);
                //UNCOMMENTBELOW
                jpst.getModel().setValueAt(value, row, col);
            }
                    
                    
       }catch(Exception e){
            System.err.println("ERROR CHANGING PARAMETER ");
            if(e!=null){
                System.err.println("ERROR MESSAGE IS: "+e.getMessage());
            }
       }
        
        //row=null;
        //col=null;
        editingComponent=null;
       // return true;
        return super.stopCellEditing();
       
    }
}
