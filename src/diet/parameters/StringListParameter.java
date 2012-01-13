/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.parameters;

import java.io.Serializable;
import java.util.Vector;

/**
 *
 * @author user
 */
public class StringListParameter extends Parameter implements Serializable{
    
    
    private Vector value = new Vector();
    private String defltval;

     

    /** Creates a new instance of StringParameter */
    
    public StringListParameter(){
        super();
    }
    
        
    public StringListParameter(String id,Vector value) {
        super(id);
        this.value.addAll(value);
    }
    
     public StringListParameter(String id,String value) {
        super(id);
        this.value.addElement(value);
        System.err.println(value);
    }
    
    public boolean checkValIsOK(Object val){   
        if(val instanceof Vector)return true;        
        return false;
    } 
    
    public void setValue(Object o){
       System.err.println("CALLINGSETVALUE");
       this.value=(Vector)o;
    }
    
     public Vector getValue(){
        System.out.println("STRINGLISTPARAMETER-GETTING VALUE: "+value);
        return this.value;
        
    }
     
     public String getDefltval() {
        return defltval;
    }

    public void setDefltval(String defltval) {
        this.defltval = defltval;
    }

    public void changetheDefaultValueAddingNewValIfNecessary(String s){
        if(value.contains(s)){
            this.defltval=s;
        }
        for(int i=0;i<value.size();i++){
            String s2 = (String)value.elementAt(i);
            if(s.equals(s2)){
                defltval=s;
                return;
            }
        }
        value.addElement(s);
        defltval=s;
    }
    
    
    
    public boolean saveAndCheckValidity(Object val){
        if(!checkValIsOK(val))return false;
        this.value=(Vector)val;
        return true;
    }
    
    
    public int getIndexOfString(String s){
        for(int i=0;i<value.size();i++){
            String stored = (String)value.elementAt(i);
            if(stored.equals(s))return i;
        }
        return -1;
    }
    
    public void addNewString(String s,boolean allowDuplicates){
        
        if(allowDuplicates){
            value.addElement(s);
            return;
        }
        else{
            int i=getIndexOfString(s);
            if(i<=0)value.addElement(s);
        }
        return;      
    }
  
    public String getStringAt(int i){
        if(i>=0&i<value.size()){
            return (String)value.elementAt(i);
        }
        return null;
    }
 
    public void mergeVector(Vector v,boolean allowDuplicates){
        if(allowDuplicates){
            value.addAll(v);
        }else{
            for(int i=0;i<v.size();i++){
                String s = (String)v.elementAt(i);
                int index = getIndexOfString(s);
                if(index>=0){
                    value.addElement(s);
                }
            }
        }
    }
    
    
}
