/*
 * LongParameterDeprecated.java
 *
 * Created on 05 January 2008, 19:50
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.parameters;
import java.io.Serializable;


/*
 * StringParameter.java
 *
 * Created on 17 December 2007, 16:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */




public class LongParameterDeprecated extends Parameter implements Serializable {

    private Long value;
    private Long defaultValue;
    
    
    public LongParameterDeprecated(){
        super();
    }
    
     public LongParameterDeprecated(String id,long value) {
        super(id);
        this.value=value;
    }
    
    public LongParameterDeprecated(String id,long defaultValue,long value){
        super(id);
        this.defaultValue = new Long(defaultValue);
        this.value=value;
    }
    
   
    
    
    public boolean saveAndCheckValidity(Object val){
        return true;
    }
    
    
    
    public Long getValue(){
        if(2<5)System.exit(-1);
        return value;
    }
    
     public void setValue(Object o){
         if(2<5)System.exit(-1);
        this.value =(Long)o;
    }
    
    public void setDefaultValue(Object o){
        defaultValue=(Long)o;
    }
    public Long getDefaultValue(){
        return defaultValue;
    }
    public boolean checkValIsOK(Object val){
        return true;
    }
    
    
    
}