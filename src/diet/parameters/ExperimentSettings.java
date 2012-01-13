/*
 * ExperimentSettings.java
 *
 * Created on 08 January 2008, 14:11
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.parameters;
import diet.debug.Debug;
import java.util.Vector;
/**
 *
 * @author user
 */
public class ExperimentSettings {

    /** Creates a new instance of ExperimentSettings */
    private Vector parameters = new Vector();
    private Vector pListeners = new Vector();


    public ExperimentSettings() {
    }

     public ExperimentSettings(Vector v) {
         parameters =v;
    }

    public void addParameters(Vector v){
        parameters.addAll(v);
    }

    public Vector getParameters(){
        return parameters;

    }

    public Object getV(String s){
        try{
          Parameter p = getParameter(s);
          return p.getValue();
        }catch(Exception e){
            System.err.println("COULD NOT FIND PARAMETER "+s);
        }
        return null;
    }


    public Parameter getParameter(String s){
        System.out.println(" LOOKING FOR PARAMETER "+s);
        for(int i=0;i<parameters.size();i++){

            Parameter p = (Parameter)parameters.elementAt(i);


            if(p.getID().equals(s)){
                System.out.println("FOUND PARAMETER: "+p.getID());
                return p;
            }
        }
        return null;
    }




    public void printParameters(){
        for(int i=0;i<parameters.size();i++){
            Parameter p = (Parameter)parameters.elementAt(i);
            System.out.println("////"+p.getID()+": "+p.getValue());
        }
    }

    public void addParameter(Parameter p){
        parameters.addElement(p);
    }
     public void addParameterListener (ParameterEventListener listener){
       pListeners.addElement (listener);
      }

      void removeParameterListener (ParameterEventListener listener){
        if (pListeners != null&&pListeners.contains(listener)){
          pListeners.removeElement (listener);
        }
    }
    public void generateParameterEvent(Parameter p){
        ParameterEvent pe= new ParameterEvent(p);
        for(int i=0;i<pListeners.size();i++){
            ParameterEventListener pel = (ParameterEventListener)pListeners.elementAt(i);
            if(Debug.verboseOUTPUT)System.out.println("PARAMETER IS"+p.getID());
            pel.parameterChanged(pe);
        }
    }

    public boolean changeParameterValue(String parameterName, Object value){
        try{
          boolean succ = false;
          Parameter p = getParameter(parameterName);
          if(p.checkValIsOK(value)){
              p.saveAndCheckValidity(value);
              return true;
          }
             return false;
        }catch (Exception e){
           if(value!=null){
              System.err.println("Could not find: "+value.toString());
           }
           else{
              System.err.println("COULD NOT FIND VALUE");

           }
           //System.exit(-500);
        }
        return false;
    }

    public boolean changeParameterDescription(String parameterName, String newDescription){
         try{
          boolean succ = false;
          Parameter p = getParameter(parameterName);
          p.setDescription(newDescription);
          return true;
        }catch(Exception e){
            return false;
        }
    }


    public void populateWithTestData(){
        IntParameter im = new IntParameter("TestingFreq",23,30);
        IntParameter im2 = new IntParameter("DialControl",15,30);
        StringParameter st3 = new StringParameter("Meaningful","Car","House");
        StringParameter st4 = new StringParameter("Joyful From","food","drink");
        Vector v = new Vector();
        v.addElement("White Russian");
        v.addElement("petrol");
        v.addElement("vodka");
        v.addElement("lemon juice");
        v.addElement("meat");
        StringParameterFixed st5 = new StringParameterFixed("Drinks","petrol",v,"meat");

        parameters.addElement(im);
        parameters.addElement(im2);
        parameters.addElement(st3);
        parameters.addElement(st4);
        parameters.addElement(st5);
    }



}
