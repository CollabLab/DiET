/*
 * ParameterFactory.java
 *
 * Created on 05 January 2008, 19:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.parameters;

import java.util.Vector;

import diet.utils.VectorToolkit;

/**
 *
 * @author user
 */
public class ParameterFactory {
    
    /** Creates a new instance of ParameterFactory */
    public ParameterFactory() {
    }
    
    
    static public String generateString(Parameter p){
        String line ="";
        line = line + p.getID();
        String desc = p.getDescription();
        if(desc==null){
            line = line + "| ";
        }
        else{
            line =  line +"|"+p.getDescription();
        }
        
       
        
        if(p instanceof BooleanParameter){
            line = "BOOLEAN"+"|"+line+"|";
            if(((BooleanParameter)p).getValue()==Boolean.TRUE){
                line = line + "TRUE"+"|";
            }
            else{
                line = line + "FALSE"+"|";
            }
        }
        else if (p instanceof IntParameter){
            line = "INT"+"|"+line+"|";
            IntParameter pi = (IntParameter)p;
            line = line +  pi.getValue()+"|";
            if(pi.getDefaultValue()==null){
                line = line +  pi.getValue()+"|";
            }
            else{
               line = line + pi.getDefaultValue()+"|";
            }   
        }
        else if(p instanceof LongParameter){
            line = "LONG"+"|"+line+"|";
            LongParameter li = (LongParameter)p;
            line = line + li.getValue()+"|";
            if(li.getDefaultValue()==null){
                line = line +  li.getValue()+"|";
            }
            else{
               line = line + li.getDefaultValue()+"|";
            }        
        }
         else if(p instanceof StringParameter){
            line = "STRING"+"|"+line+"|";
            StringParameter sp = (StringParameter)p;
            line = line + sp.getValue()+"|";
           if(sp.getDefaultValue()==null){
                line = line +  sp.getValue()+"|";
            }
            else{
               line = line + sp.getDefaultValue()+"|";
            }        
        }
        else if(p instanceof StringListParameter){
            line = "STRINGLIST"+"|"+line+"|";
            StringListParameter sli = (StringListParameter)p;
            line = line + sli.getDefltval()+"|"; 
            line = line + VectorToolkit.createStringRepresentation(sli.getValue(), "|");
        }
        else if(p instanceof StringParameterFixed){
            line = "STRINGFIXED"+"|"+line+"|";
            StringParameterFixed spf = (StringParameterFixed)p;
            line = line + spf.getValue()+"|";
            line = line + spf.getDefaultValue()+"|";
            line = line + VectorToolkit.createStringRepresentation(spf.getPermittedValues(), "|");
            
        }
        return line.replaceAll("[\\n]", "");
    }
    
    static public Parameter getParameterFromString(String s){
        Parameter p=null; 
        try{
        String[] sp = s.split("[|]");
        String type = sp[0].trim(); //TYPE
        
        String id = sp[1].trim(); //NAME
        String desc = sp[2].trim(); //DESC
        
        
        
        if(type.equalsIgnoreCase("BOOLEAN")){
            p = new BooleanParameter();
            if(sp[3].equalsIgnoreCase("TRUE")){
               p.setValue(true);    
            }
            else{
                p.setValue(true);
            }
        }
        else if(type.equalsIgnoreCase("INT")){
            int value = Integer.parseInt(sp[3]);
            int defvalue;
            if(sp[4]==null||sp[4].equalsIgnoreCase("")){
                defvalue=value;
            }
            else{
                defvalue = Integer.parseInt(sp[4]);
            }
            
            p = new IntParameter(id,defvalue,value);        
            
        }
        else if(type.equalsIgnoreCase("LONG")){
            long value = Long.parseLong(sp[3]);
            long defvalue;
            if(sp[4]==null||sp[4].equalsIgnoreCase("")){
                defvalue=value;
            }
            else{
                defvalue = Long.parseLong(sp[4]);
            }
            p = new LongParameter(id,defvalue,value);
        }
        else if(type.equalsIgnoreCase("STRING")){
            if(sp[4]==null||sp[4].equalsIgnoreCase("")){
                p = new StringParameter(id,sp[3],sp[3]);
            }
            else{
                p = new StringParameter(id,sp[4],sp[3]);
            }
            
            
        }
        else if(type.equalsIgnoreCase("STRINGLIST")){
            Vector stringV = new Vector();
            String defvalue = sp[3];
            for(int i=4;i<sp.length;i++){
                String vall = sp[i].trim();
                stringV.addElement(vall);
            }
            p = new StringListParameter(id,stringV);
            ((StringListParameter)p).setDefltval(defvalue);
        }
        else if(type.equalsIgnoreCase("STRINGFIXED")){
            Vector stringV = new Vector();
            String value = sp[3];
            String defvalue = sp[4];
            for(int i=5;i<sp.length;i++){
                String vall = sp[i].trim();
                stringV.addElement(vall);
            }
            p = new StringParameterFixed(id,defvalue,stringV,value);
        }
        
        p.setID(id);
        p.setDescription(desc);
        
        return p;
     
    }catch(Exception e){
        System.err.println("ERROR PARSING PARAMETER STRING: "+s);
        e.printStackTrace();
        Parameter p2 = new StringParameter("ERROR PARSING STRING:",s);
        return p2;
    }
}
    
}
