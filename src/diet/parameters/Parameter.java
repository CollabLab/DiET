/*
 * Parameter.java
 *
 * Created on 17 December 2007, 16:03
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
public abstract class Parameter implements Serializable{
    
    /** Creates a new instance of Parameter */
    
    private String id;
    private String description;
    private boolean mutableInExperiment=true;
    
    
    public Parameter() {
    }
    
    public Parameter(String id){
        this.id=id;
        System.out.println("CREATING: "+id);
    }
    
    public boolean isMutableInExperiment(){
        return mutableInExperiment;
    }
     public void setMutableInExperiment(boolean mutable){
        this.mutableInExperiment=mutable;
    }
    
   

    public String getDescription() {
        return description;
    }

     public void setDescription(String description) {
        this.description = description;
     }
     
     //abstract public Object getDefaultValue2();
      
     //abstract void setDefaultValue2(Object o);
    
     abstract public Object getValue();

     abstract public boolean saveAndCheckValidity(Object o); 
     
     abstract public void setValue(Object o);
     
     abstract public boolean checkValIsOK(Object val);
    
    
    public String getID(){
        return id;
    }
    public void setID(String s){
        id =s;
    }
    
}
