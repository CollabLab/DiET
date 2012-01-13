/*
 * StringParameterFixed.java
 *
 * Created on 18 December 2007, 16:54
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.parameters;
import java.io.Serializable;
import java.util.Vector;

/**
 *
 * @author user
 */
public class StringParameterFixed extends Parameter implements Serializable, Fixed{
    
    
    private Vector permittedValues = new Vector();
    private String defaultValue;
    private String value;
    
    
    
    /** Creates a new instance of StringParameterFixed */
   
    public StringParameterFixed(){
        super();
    }
    
    public StringParameterFixed(String id,String value) {
        super(id);
        this.value=value;
    }
    
     public StringParameterFixed(String id,String defaultValue,String value) {
        super(id);
        this.value=value;
        this.defaultValue=defaultValue;
    }
     
    public StringParameterFixed(String id,Vector permittedValues,String value) {
        super(id);
        this.value=value;
        this.defaultValue=value;
        this.permittedValues=permittedValues;
    }   
     
    public StringParameterFixed(String id,int defaultValIndex, Vector permittedValues,int valueIndex) {
        super(id);
        this.permittedValues=permittedValues;
        this.defaultValue=(String)permittedValues.elementAt(defaultValIndex);
        this.value=(String)permittedValues.elementAt(valueIndex);
    }
    
    public StringParameterFixed(String id,String defaultValue,Vector permittedValues,String value) {
        super(id);
        this.value=value;
        this.defaultValue=defaultValue;
        this.permittedValues=permittedValues;
    } 
     
    public int indexOfPermittedValue(Object o){
        if(o==null)return -1;
        if(!(o instanceof String))return -1;
        if (permittedValues.contains(o)){
            return permittedValues.indexOf(o);
        }
        else{
            for(int i=0;i<permittedValues.size();i++){
                System.out.println("SHOULDBESTRING "+permittedValues.elementAt(i).getClass().toString());
                String s2 = (String)permittedValues.elementAt(i);
                
                if(s2.equalsIgnoreCase(""+o.toString()))return i;
            }
        }
        return -1;
    } 
    
    public void removePermittedValue(String value){
      int index =  indexOfPermittedValue(value);
      if(index>=0)permittedValues.removeElementAt(index);
      
    }
    
    public Vector getPermittedValues(){
        return permittedValues;
    }
    public void setPermittedValues(Vector v){
        this.permittedValues=v;
    }
    
    public void addToPermittedValues(Object value){
        if(indexOfPermittedValue(value)<0){
            permittedValues.addElement(value);
        }
    }
    
    public void addToPermittedValues(String value, int position){
        if (position<0)position =0;
        if(position>permittedValues.size())position = permittedValues.size();
        permittedValues.insertElementAt(value,position);
    }
    
     public boolean checkValIsOK(Object val){
        if(val instanceof String)return true;
        return false;
    } 
    
     public boolean saveAndCheckValidity(Object val){
        if(!checkValIsOK(val))return false;
        this.value=(String)val;
        System.out.println("PARAMETERSETTING FROM"+value+" to"+val);
        return true;
    }
    
    public String getValue(){
        return this.value;
    }
    public void setValue(Object o){
        this.value=(String)o;
    }
    
    public String getDefaultValue(){
        return this.defaultValue;
    }
    public void setDefaultValue(Object o){
        this.defaultValue=(String)o;
    }
}
