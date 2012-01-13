/*
 * JExperimentParametersTableModel.java
 *
 * Created on 13 January 2008, 14:36
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.parameters.ui;
import diet.debug.Debug;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import diet.parameters.ExperimentSettings;
import diet.parameters.Fixed;
import diet.parameters.Parameter;
import diet.parameters.ParameterEvent;
import diet.parameters.ParameterEventListener;
/**
 *
 * @author user
 */
public class JExperimentParametersTableModel extends  AbstractTableModel implements ParameterEventListener {
    
    /** Creates a new instance of JExperimentParametersTableModel */
    private JTable jt;
    private ExperimentSettings expSett;
    private Hashtable pendingSettings = new Hashtable();
    
    public JExperimentParametersTableModel(JTable jt,ExperimentSettings expSett) {
        this.jt=jt;
        this.expSett=expSett;
        expSett.addParameterListener(this);
    }
 
    
     public boolean isCellEditable(int x, int y){  
       if(y==2||y==3)return true;
       return false;
     }

   public String getColumnName(int column){

     if(column==0){
         return "Parameter name";
     }
     else if (column==1){
         return "Current value";
     }
     else if(column==2){
         return "New value";
     }
     else if(column==3){
         return "Set";
     }
     return " ";
  }

  public Object getValueAt(int x, int y){
    Vector params = expSett.getParameters();
    //expSett.printParameters();
    if(x>=params.size())return " ";
    Parameter p = (Parameter)params.elementAt(x);
    if(y==0){
        return p.getID();
    }
    if(y==1){
        return p.getValue();
    }
    if(y==2){
        return this.pendingSettings.get(p);     
    }
    if(y==4){
        return new JExperimentSetParameterButton(this,p,"SET");
    }
    return null;
 }
    
  
  public void setValueAt(Object value, int row, int col) {
        //System.out.println("SETTINGVALUE AT "+value.toString());
        //Parameter p = (Parameter)expSett.getParameters().elementAt(row);
        //this.pendingSettings.put(p,value);
        
        
}
  
  public void setValueOfParameter(Parameter p,JExperimentSetParameterButton jespb){
     int index = this.expSett.getParameters().indexOf(p);
     System.out.println("INDEXOFP is"+index);
     Object pendingSetting = pendingSettings.get(p);
     if(pendingSetting==null)return;
     if(p instanceof Fixed){
         Fixed pFixed = (Fixed)p;
         if(pFixed.indexOfPermittedValue(pendingSetting)<0){
             pFixed.addToPermittedValues(pendingSetting);
         }  
     }
      
      boolean successfulchange = p.saveAndCheckValidity(pendingSetting);
      System.out.println("TABLEMODELSETTING "+p.getValue()+" to"+pendingSetting);
      SwingUtilities.invokeLater(new Runnable(){
          
          public void run(){
              fireTableDataChanged();
              fireTableStructureChanged();
          }
      });
      System.out.println("THE PVALUE IS:"+p.getValue());
  }
  
  public void parameterChanged(ParameterEvent e){
      if(Debug.verboseOUTPUT)System.out.println("REPAINTING AS PARAMETERLISTENER "+e.toString());
      repaintRowOfParameter((Parameter)e.getSource());
  }
  
 public void repaintRowOfParameter(Parameter p){
     final int index = this.expSett.getParameters().indexOf(p);
      SwingUtilities.invokeLater(new Runnable(){
          
          public void run(){
              fireTableRowsUpdated(index,index);
              //fireTableDataChanged();
              //fireTableStructureChanged();
          }
      });
 } 
  
 public Class getColumnClass(int column){
     if(column==3)return new JButton().getClass();
     
     //return ParameterUIFactory.getUIComponentForExperimentManagerNewParameterValue((Parameter)this.expSett.getParameters().elementAt(column)).getClass();
     return "".getClass();
 } 

 /* public Class getColumnClass(int column) {
               Class returnValue;
               if ((column >= 0) && (column < getColumnCount())) {
                 returnValue = getValueAt(0, column).getClass();
               } else {
                 returnValue = Object.class;
               }
               return returnValue;
  }
  */
  public int getRowCount(){
  
     return this.expSett.getParameters().size();
  }
  public int getColumnCount(){
     return 4;
  }
    
  public boolean setPendingSetting(Parameter  p,Object o){
      boolean isValidated = p.checkValIsOK(o);
      if(isValidated){
         this.pendingSettings.put(p,o);
         System.out.println("THING IS VALID AND IS BEING PUT: "+o.getClass().toString());
      }
      else{
        System.out.println("THING IS NOT VALID AS PARAMETER: "+o.getClass().toString());
      }  
       this.repaintRowOfParameter(p);
       return isValidated;
  }
  
  public Object getPendingSetting(Parameter p){
      return pendingSettings.get(p);
  }
  
  public Vector getExpSettings(){
      return this.expSett.getParameters();
  }
    
    
}
