/*
 * JDiETTableSorter.java
 *
 * Created on 08 October 2007, 22:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.server.conversationhistory.ui;

import java.util.Comparator;

import javax.swing.JLabel;
import javax.swing.table.TableModel;

public class JDiETTableSorter implements Comparator {///extends TableRowSorter implements Comparator{
    
    TableModel m;
    ///RowFilter rf;
    
    
    public JDiETTableSorter() {
        super();
    }
    
   
    
    public JDiETTableSorter(TableModel m){
        ///super(m);
        this.m = m;
       
        
    }

public Comparator getComparator(int column){
    System.err.println("GETTING COMPARATOR");
    return this;
}    
    
 public void allRowsChanged(){
    //System.err.println("THE ROW COUNT IS IS ROWCOUNT ROWCOUNT ROWCOUNT...................................................."+this.getModelRowCount());
}

 
public void modelStructureChanged(){
     //System.err.println("STRUCTURECHANGEDTHE ROW COUNT IS IS ROWCOUNT ROWCOUNT ROWCOUNT...................................................."+this.getModelRowCount());

}

///public RowFilter getRowFilter(){
   /// System.err.println("JDiETTableSorter__GETTING ROWFILTER-----------------------------------");
    ///if(rf==null){
    ///    return super.getRowFilter();
    ///}
    ///return rf;
///}


///public void setRowFilter(RowFilter rf){
///    super.setRowFilter(rf);
///    this.rf = rf;
///    System.err.println("JDiETTableSorter__SETTING ROWFILTER-----------------------------------");
///}



        public int compare(Object o1, Object o2) {
            String a1 = " ";
            String a2=  " ";            
            if(o1 instanceof String){
               a1 = (String)o1;    
            }
            else if(o1 instanceof Long){
               a1 = Long.toString((Long)o1);    
            }
            else if (o1 instanceof Integer){
               a1 = Integer.toString((Integer)o1);
            }
            else if (o1 instanceof JLabel){
                a1 = ((JLabel)o1).getText();
            }
            else {
                a1 = (String)o1;
            }
            if(o2 instanceof String){
               a2 = (String)o2;    
            }
            else if(o2 instanceof Long){
               a2 = Long.toString((Long)o2);    
            }
            else if (o2 instanceof Integer){
               a2 = Integer.toString((Integer)o2);
            }
            else if (o2 instanceof JLabel){
                a2 = ((JLabel)o2).getText();
            }
            else {
                a2 = (String)o2;
            }
            try{
                Long b1 = Long.parseLong(a1);
                Long b2 = Long.parseLong(a2);
                return b1.compareTo(b2);
            }catch(Exception e){}
            
            return a1.compareTo(a2);
        }
}