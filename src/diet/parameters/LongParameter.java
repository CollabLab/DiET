/*
 * LongParameter.java
 *
 * Created on 20 January 2008, 02:24
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.parameters;

/**
 *
 * @author user
 */
public class LongParameter extends Parameter{
     private Long value;
    private Long defaultValue;
    
    /** Creates a new instance of StringParameter */
    
    public LongParameter(){
        super();
    }
    
    public LongParameter(String id,long value) {
        super(id);
        this.value=(long)value;
    }
    public LongParameter(String id,long defaultValue,long value) {
        super(id);
        this.value=(long)value;
        this.defaultValue = (long)defaultValue;
        System.out.println("INT DEFAULTVALUE IS" +this.defaultValue);
    }
    
     public boolean checkValIsOK(Object val){
        long l;
        try{
           l=this.parseLong(val);
           return true;
        }catch (Exception e){
               return false;
        }
     }   
     
     private long parseLong(Object o) throws Exception{
         long i=0;
         if(o instanceof Integer){
              i = ((Long)o).intValue(); 
              return i;
         }   
         else if(o instanceof String){
              i = Long.parseLong((String)o);  
              return i;
         }
         throw (new Exception());
         
     }
     public boolean saveAndCheckValidity(Object val){
        if(!checkValIsOK(val))return false;
        try{
          this.value=this.parseLong(val);
          return true;
        } catch(Exception e){
            return false;
        }
    }
    
    public void setValue(Object o){
       // System.out.println("INTVALUE-SETTING");
        this.value=(Long)o;
    }
     
    public Long getValue(){
         //System.out.println("INTVALUE-GETTING");
        return value;
    }
    
    public void setDefaultValue(Object o){
        defaultValue=(Long)o;
    }
    public Long getDefaultValue(){
        return defaultValue;
    }
    
    
    
}