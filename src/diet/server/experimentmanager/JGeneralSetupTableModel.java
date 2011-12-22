/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.experimentmanager;

import java.io.File;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import diet.parameters.IntParameter;
import diet.parameters.Parameter;
import diet.parameters.ui.SavedExperimentsAndSettingsFile;

/**
 *
 * @author user
 */
public class JGeneralSetupTableModel extends DefaultTableModel implements TableModelSetValue{
    
    JTable jt;
    File f;
    Vector parameters=new Vector();
    JGeneralSetupPanel jgs;
    
            
    public JGeneralSetupTableModel(JGeneralSetupPanel jgs,JTable jt, File f) {
        super();
        this.jt=jt;
        this.f=f;
        this.jgs=jgs;
        try{
          parameters = SavedExperimentsAndSettingsFile.readParameterObjects(f);
     
        }catch(Exception e){
            System.err.println("COULDNT LOAD PARAMETERS");
            parameters=new Vector();
        }  
         if(parameters==null){
           System.out.println("NULL1---------------------------------------");
        }
        
    }
    
   

    public Object getValueAt(int row, int column) {
        Parameter p = (Parameter)parameters.elementAt(row);
        if(column==0){
            return p.getID();
        }  
        else if(column ==1){
            return p.getValue();
        }  
        return "";
    }
    
    public String getColumnName(int column){
        if(column==0)return "Parameter Name";
        if(column==1)return "Value";
        return "";
    }

    public int getRowCount(){
        if(parameters==null){
            return 0;
        }
        else{
          return parameters.size();
        }  
        
    }
    
    
            

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        try{
          System.err.println("SETTING VALUE"+aValue.toString());
          Parameter p = this.getParameter(row);
          if(column==1){
              if(p instanceof IntParameter && aValue instanceof String && p.getID().equalsIgnoreCase("Port Number")){
                 int possVal = Integer.parseInt((String)aValue);
                 p.setValue(possVal);
                 JOptionPane.showMessageDialog(jt, "You will need to restart this program for this to take effect\n " +
                         "To change this port number, choose SAVE and then restart program");
             }
              else{
                  int possVal = Integer.parseInt((String)aValue);
                  p.setValue(possVal);
              }
        }
        this.jgs.informRecipientOfComponentChange();
        //super.setValueAt(aValue, row, column);
        }catch (Exception e){
            JOptionPane.showMessageDialog(jt, "Error updating Parameter. Check correct use of numerals / characters");

        }
    }
    
    public int getColumnCount(){
        if(parameters==null){
           System.out.println("NULL2");
        }   
        return 2;
    }
    
    public boolean isCellEditable(int row,int column){
        if(column==0)return false;
        return true;
    }

    public void setValue(int row, int column, Object newValue) {
        
        
    }

    
    
    
    public Parameter getParameter(String name){
        for(int i=0;i<parameters.size();i++){
            Parameter p = (Parameter)parameters.elementAt(i);
            if(p.getID().equalsIgnoreCase(name))return p;
        }
        return null;
    }
    
    public Parameter getParameter(int n){
        if(n<parameters.size()){
            return (Parameter)parameters.elementAt(n);
        }
        return null;
        
    }
}
