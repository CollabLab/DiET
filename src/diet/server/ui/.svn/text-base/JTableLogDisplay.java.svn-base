package diet.server.ui;

import java.util.Vector;

import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import diet.message.Message;


public class JTableLogDisplay   extends JTable {

  private JTableModelLogDisplay jtm;

  public JTableLogDisplay() {
    super();
    jtm = new JTableModelLogDisplay(this);
    this.setModel(jtm);
    setSize();
  }
  
  public JTableLogDisplay(Vector v) {
    super();
    jtm = new JTableModelLogDisplay(this);
    this.setModel(jtm);
    setSize();
    
    for(int i=0;i<v.size();i++){
        addMessage((Message)v.elementAt(i));
    }
  }

 public void addMessage(Message m){
    jtm.addMessage(m);
 }

 private void setSize(){
    TableColumnModel cm = this.getColumnModel();
     for(int i=0;i<6;i++){
        TableColumn cl = new TableColumn();
        cm.addColumn(cl);
        
    } 
    
    
    TableColumn column = null;
    for (int i = 0; i <6; i++) {
       column = this.getColumnModel().getColumn(i);
      if (i == 0 || i==4) {
          //column.setPreferredWidth(150); 
      } else {
          //column.setPreferredWidth(150);
          //column.setMinWidth(100);
          //column.setMaxWidth(100);
      }
  }

 }

}
