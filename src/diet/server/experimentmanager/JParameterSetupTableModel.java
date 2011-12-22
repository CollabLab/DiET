/*
 * JParameterSetupTableModel.java
 *
 * Created on 08 January 2008, 16:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.server.experimentmanager;
import java.io.File;
import java.util.Vector;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Document;

import diet.parameters.Parameter;
import diet.parameters.ui.SavedExperimentsAndSettingsFile;
/**
 *
 * @author user
 */
public class JParameterSetupTableModel extends DefaultTableModel implements DocumentListener{

        
    /** Creates a new instance of JParameterSetupTableModel 
     Treats the Description parameter differently in order to display it at the top of the screen
     */
    
    
    JParameterSetupTable jpstp;
    File f;
    Vector expparameters=new Vector();
    
            
    public JParameterSetupTableModel(JParameterSetupTable jpstp, File f) {
        super();
        this.jpstp=jpstp;
        this.f=f;
        try{
          expparameters = SavedExperimentsAndSettingsFile.readParameterObjects(f);
     
        }catch(Exception e){
            System.err.println("COULDNT LOAD PARAMETERS");
            expparameters=new Vector();
        }  
         if(expparameters==null){
           System.out.println("NULL1---------------------------------------");
        }
        
    }
    
   

    public Object getValueAt(int row, int column) {
        Parameter p = (Parameter)expparameters.elementAt(row);
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
        if(expparameters==null){
            return 0;
        }
        else{
          return expparameters.size();
        }  
        
    }
    
    public int getColumnCount(){
        if(expparameters==null){
           System.out.println("NULL2");
        }   
        return 2;
    }
    
    public void setValueAt(Object newValue,int row, int column) {
        System.out.println(newValue.toString());
         System.out.println(newValue.getClass().toString());
       // System.exit(-12345);
        
        //this.jpstp
        Parameter p =(Parameter)this.expparameters.elementAt(row);
        if(column==1){
             boolean succ = p.checkValIsOK(newValue);
             
             if(succ)p.saveAndCheckValidity(newValue);//setValue(newValue);
        }
        else if(column==1){
             //p.setMutableInExperiment((Boolean)newValue);
        }
        JParameterSetupPanel jpsp = this.jpstp.getEnclosingJPanel();
       
        if(jpsp!=null){
            jpsp.informRecipientOfComponentChange();
        }
        this.fireTableCellUpdated(row, column);
    }
    
    
    public String getDescription(){
        Parameter p = getParameter("Description");
        if(p==null)return"";
        String desc = p.getDescription();
        if(desc==null) return "";
        return desc;
        
    }
    
    public Parameter getParameter(String name){
        for(int i=0;i<expparameters.size();i++){
            Parameter p = (Parameter)expparameters.elementAt(i);
            if(p.getID().equalsIgnoreCase(name))return p;
        }
        return null;
    }
    
    public Parameter getParameter(int n){
        if(n<expparameters.size()){
            return (Parameter)expparameters.elementAt(n);
        }
        return null;
        
    }
    
    
    public void changedUpdate(DocumentEvent e) {
        Document d = e.getDocument();
        try{
            String s = e.getDocument().getText(0, d.getLength());
            setDescription(s);
        }catch (Exception e2){
            System.err.println("ERROR CHANGING DESCRIPTION");
        }        
    }

    public void insertUpdate(DocumentEvent e) {
        Document d = e.getDocument();
        try{
            String s = e.getDocument().getText(0, d.getLength());
            setDescription(s);
        }catch (Exception e2){
            System.err.println("ERROR CHANGING DESCRIPTION");
        }     
    }

    public void removeUpdate(DocumentEvent e) {
         Document d = e.getDocument();
        try{
            String s = e.getDocument().getText(0, d.getLength());
            setDescription(s);
        }catch (Exception e2){
            System.err.println("ERROR CHANGING DESCRIPTION");
        }     
    }
    
    public void setDescription(String s){
        int index = this.jpstp.getSelectedRow();
        Parameter p = (Parameter)this.expparameters.elementAt(index);
        p.setDescription(s);        
        for(int i=0;i<this.expparameters.size();i++){
            Parameter sp = (Parameter)expparameters.elementAt(i);
            System.out.println(i+"-"+sp.getID()+"-"+sp.getDescription());
        }
        //
        if(p==null){
            System.err.println("Error cannot find parameter of description");
            return;
        }
    }
}
