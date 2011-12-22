package diet.message;

import java.io.Serializable;

import javax.swing.text.AttributeSet;

public class MessageWithinTurnDocumentSyncToClientRemove extends Message implements Serializable{

    private String textInWindow;
    private int offset;
    private int windowNumber;
    private int length;
    private AttributeSet attr;
  

    public MessageWithinTurnDocumentSyncToClientRemove(String email, String username, int windowNumber, int offset, int length) {
        super(email, username);
        this.setWindowNumber(windowNumber);
        this.setOffset(offset);
        this.setLength(length);
    }

   public AttributeSet getAttr(){
       return attr;
   }

    public int getOffset(){
        return offset;
    }
    public int getLength(){
        return length;
    }
    
    public String getTextToAppendToWindow(){
        return getTextInWindow();
    }
    public int getWindowNumber(){
        return windowNumber;
    }
    public String getMessageClass(){
   return "WYSIWYGDocSyncToClient";
}

    public String getTextInWindow() {
        return textInWindow;
    }

    public void setTextInWindow(String textInWindow) {
        this.textInWindow = textInWindow;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setWindowNumber(int windowNumber) {
        this.windowNumber = windowNumber;
    }

    public void setLength(int length) {
        this.length = length;
    }


}
