/*
 * IntParameter.java
 *
 * Created on 18 December 2007, 16:55
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




public class IntParameter extends Parameter implements Serializable {

    private Integer value;
    private Integer defaultValue;
    
    /** Creates a new instance of StringParameter */
    
    public IntParameter(){
        super();
    }
    
    public IntParameter(String id,int value) {
        super(id);
        this.value=value;
        this.defaultValue=value;
    }
    public IntParameter(String id,int defaultValue,int value) {
        super(id);
        this.value=value;
        this.defaultValue = defaultValue;
        System.out.println("INT DEFAULTVALUE IS" +this.defaultValue);
    }
    
     public boolean checkValIsOK(Object val){
        int i;
        try{
           i=this.parseInteger(val);
           return true;
        }catch (Exception e){
               return false;
        }
     }   
     
     private int parseInteger(Object o) throws Exception{
         int i=0;
         if(o instanceof Integer){
              i = ((Integer)o).intValue(); 
              return i;
         }   
         else if(o instanceof String){
              i = Integer.parseInt((String)o);  
              return i;
         }
         throw (new Exception());
         
     }
     public boolean saveAndCheckValidity(Object val){
        if(!checkValIsOK(val))return false;
        if(val instanceof Integer){
            value=(Integer)val;
            return true;
        }
        try{
          this.value=this.parseInteger(val);
          return true;
        } catch(Exception e){
            return false;
        }
    }
    
    public void setValue(Object o){
        //System.out.println("INTVALUE-SETTING");
        this.value=(Integer)o;
    }
     
    public Integer getValue(){
        // System.out.println("INTVALUE-GETTING");
        return value;
    }
    
    public void setDefaultValue(Object o){
        defaultValue=(Integer)o;
    }
    public Integer getDefaultValue(){
        return defaultValue;
    }
    
    
    
}