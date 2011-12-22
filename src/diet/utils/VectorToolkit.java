/*
 * VectorToolkit.java
 *
 * Created on 28 October 2007, 13:43
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.utils;


import diet.task.react.Move;
import java.util.Random;
import java.util.Vector;
/**
 *
 * @author user
 */
public class VectorToolkit {
    
    /** Creates a new instance of VectorToolkit */
    public VectorToolkit() {
    }
    
    public static Vector appendVector2ToVector1(Vector v1, Vector v2){
        for(int i=0;i<v2.size();i++){
            v1.addElement(v2.elementAt(i));
        }
        return v1;
    }
    public static boolean vectorOfStringsContains(Vector v, String s){
        for(int i=0;i<v.size();i++){
            String thing = (String)v.elementAt(i);
            if(thing.equalsIgnoreCase(s))return true;
        }
        return false;
    }
    public static String createStringRepresentation(Vector v, String divisor){
        String s = "";
        for(int i=0;i<v.size();i++){
            String s2 = (String)v.elementAt(i);
            s=s+s2+divisor;
        }
        return s;
        
    }
   
    public static Vector sublist(Vector v,int lowest,int highest){
        Vector v2 = new Vector();
        for(int i=lowest;i<highest;i++){
            v2.addElement(v.elementAt(i));
        }
        return v2;
    }
    public static Vector getCopy(Vector input){
        Vector v = new Vector();
        for(int i=0;i<input.size();i++){
            v.addElement(input.elementAt(i));
        }
        return v;
    }
    public static void list(Vector v){
        try{
        for(int i=0;i<v.size();i++){
            String s =(String)v.elementAt(i);
            System.err.println(s);
        }
        }catch (Exception e){
            System.err.println("EXCEPTION PRINTING ");
            e.printStackTrace();
        }
    }

    static Random r = new Random();

    public static Vector randomSubset(Vector v,int size){
        Vector copyORIGINAL = getCopy(v);
        Vector subset = new Vector();
        while (subset.size()<size){
            int idx = r.nextInt(copyORIGINAL.size());
            Object o = copyORIGINAL.elementAt(idx);
            copyORIGINAL.remove(o);
            subset.add(o);
            if(copyORIGINAL.size()<=0)return subset;
        }
        return subset;
    }


    public static  Move[] flattenVofV_AndTurnToArray(Vector vINPUT){
        Vector vFLATTENED = new Vector();
        for(int i=0;i<vINPUT.size();i++){
            Object o = vINPUT.elementAt(i);
            if(o instanceof Vector){
                Vector vo = (Vector)o;
                for(int j=0;j<vo.size();j++){
                    Object o2 = vo.elementAt(j);
                    vFLATTENED.addElement(o2);
                }
            }
            else{
                vFLATTENED.addElement(o);
            }
        }
        //return vFLATTENED;
        Move[] mArray = new Move[vFLATTENED.size()];
        for(int i=0;i<vFLATTENED.size();i++){
            Object o = vFLATTENED.elementAt(i);
            mArray[i]=(Move)o;

        }
        return mArray;

    }
}
