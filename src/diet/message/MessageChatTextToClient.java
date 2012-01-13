package diet.message;
import java.io.Serializable;

public class MessageChatTextToClient extends Message implements Serializable {

  private String text;
  private int windowNumber;
  private int type;
  private boolean prefixUsername;
  private int stylenumber;

  public MessageChatTextToClient(String email,String username,int type, String t, int windowNumber, boolean prefixUsername, int stylenumber) {
    super(email,username);
    setText(t);
    this.setWindowNumber(windowNumber);
    this.setPrefixUsername(prefixUsername);
    this.stylenumber=stylenumber;
  }

  public int getStylenumber(){
      return this.stylenumber;
  }

  public String getText() {
    return text;
  }

  public int getWindowNo(){
      return getWindowNumber();
  }
  public boolean prefixUsername(){
    return isPrefixUsername();
}
  
  public int getType(){
      return type;
  }
  public String getMessageClass(){
      return "ChatTextToClient";
  }

    public void setText(String text) {
        this.text = text;
    }

    public int getWindowNumber() {
        return windowNumber;
    }

    public void setWindowNumber(int windowNumber) {
        this.windowNumber = windowNumber;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isPrefixUsername() {
        return prefixUsername;
    }

    public void setPrefixUsername(boolean prefixUsername) {
        this.prefixUsername = prefixUsername;
    }

}
