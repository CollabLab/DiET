package diet.message;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

import diet.parameters.LongParameter;
import diet.parameters.StringParameter;

public class Message  implements Serializable {

  private Date dateOfReceipt;
  private String email;
  private String username;

  
  public Message(String email, String username) {
      this.email=email;
      this.username=username;
  }

  public void timeStamp() {
    dateOfReceipt = new Date();
  }

  public Date getTimeStamp() {
    return dateOfReceipt;
  }

  public String getUsername() {
    return username;
  }

  public void overrideSetUsername(String newUserName) {
    this.username=newUserName;
  }

  public String getEmail(){
      return email;
  }
  public String getMessageClass(){
  return "Message";
}

 public Vector getAllParameters(){
    StringParameter sp1 = new StringParameter("Email",email); 
    StringParameter sp2 = new StringParameter("Username",username);
    LongParameter sp3 = new LongParameter("DateOfReceipt",dateOfReceipt.getTime());
    Vector v = new Vector();
    v.addElement(sp1);
    v.addElement(sp2);
    v.addElement(sp3);   
    return v; 
 } 
  
}
