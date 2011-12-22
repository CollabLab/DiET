/*
 * StringParameter.java
 *
 * Created on 17 December 2007, 16:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.parameters;
import java.io.Serializable;
/**
 *
 * @author user
 */


public class StringParameter extends Parameter implements Serializable{

    private String value;
    private String defaultValue;
    
    /** Creates a new instance of StringParameter */
    
    public StringParameter(){
        super();
    }
    
    public StringParameter(String id,String value) {
        super(id);
        this.value=value;
    }
    
     public StringParameter(String id,String defaultValue,String value) {
        super(id);
         this.defaultValue=defaultValue;
        this.value=value;
       
        System.err.println(this.value);
    }
    
    public boolean checkValIsOK(Object val){
        
        if(val instanceof String)return true;        
        return false;
    } 
    
    public void setValue(Object o){
       System.err.println("CALLINGSETVALUE");
       this.value=(String)o;
    }
    
     public String getValue(){
        System.out.println("STRINGPARAMETER-GETTING VALUE: "+value);
        return this.value;
        
    }
    
    public boolean saveAndCheckValidity(Object val){
        System.out.println("STRINGPARAMETER-SETTING VALUE"+value+" to"+val);
        if(!checkValIsOK(val))return false;
        this.value=(String)val;
        System.out.println("PARAMETERSETTING FROM"+value+" to"+val);
         System.err.println("--"+this.value);
        return true;
    }
    
  
    
    public void setDefaultValue(Object o){
        System.out.println("STRINGPARAMETER-SETTINGDEFAULTVALUE");
        defaultValue=(String)o;
    }
    public String getDefaultValue(){
        System.out.println("STRINGPARAMETER-GETTINGDEFAULTVALUE");
        return defaultValue;
    }
    
    
    
}
