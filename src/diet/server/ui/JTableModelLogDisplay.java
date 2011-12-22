package diet.server.ui;

import java.util.Vector;

import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import diet.message.Message;
import diet.message.MessageChatTextFromClient;
import diet.message.MessageInfoGenerator;

public class JTableModelLogDisplay extends AbstractTableModel {

   private Vector data = new Vector();
   private long startTime;
   private JTable jt;


  public JTableModelLogDisplay(JTable jt) {
     super();
     this.jt=jt;
     System.out.println("CREATED TABLEMODEL");
  }

  public void addMessage(Message m){
    if(m.getTimeStamp()!=null){  
      if (data.size()==0)startTime=m.getTimeStamp().getTime();
    }  
    data.addElement(m);
     //System.out.println("ADDED MESSAGE");
     SwingUtilities.invokeLater(new Runnable(){public void run(){fireTableDataChanged();fireTableStructureChanged();}  });
     //this.fireTableDataChanged();
     //this.fireTableStructureChanged();
  }

  public boolean isCellEditable(int x, int y){
    return false;
  }

    public Object getValueAt(int x, int y){
    Message m = (Message)data.elementAt(x);
    if(y==0){
       return m.getEmail();
    }
    else if(y==1){
       return m.getUsername();
    }
    else if(y==2){
        String className =  m.getMessageClass();
        if(className.equalsIgnoreCase("Message")){
            return m.getClass().toString();
        }
        else{
            return className;
        }
    }
    else if(y==3){
       //System.out.println("A "+startTime+":"+m.getTimeStamp());
       if(m.getTimeStamp()!=null){
         return ""+(( m.getTimeStamp().getTime())- startTime );
       }
       return "NO TIMESTAMP";
    }
    else if(y==4 & m instanceof MessageChatTextFromClient){
      MessageChatTextFromClient m2 = (MessageChatTextFromClient)m;
      //System.out.println("B" +startTime+":"+m2.getTypingOnset());
      return ""+( m2.getTypingOnset()- startTime);
    }
    else if(y==5 & m instanceof MessageChatTextFromClient){
          MessageChatTextFromClient m2 = (MessageChatTextFromClient)m;
          return m2.getText();
    }
    else{
        Vector v = MessageInfoGenerator.getAllNamesAndMethodsForUI(m);
        if(v.size()>0&&v.size()>y-3){
            return (String)v.elementAt(y-4);
        }
      return " ";
    }
  }

  public int getRowCount(){
     return data.size();
  }
  public int getColumnCount(){
     return 19;
  }

  public String getColumnName(int column){
      if(column==0){
          return "email";
      }
      else if (column ==1){
          return "username";
      }
      else if (column ==2){
          return "Type";
      }
      else if (column ==3){
          return "Info";
      }
      return " ";
  }

  
  
}
