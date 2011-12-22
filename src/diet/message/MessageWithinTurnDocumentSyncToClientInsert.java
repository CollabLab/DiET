package diet.message;

import java.io.Serializable;

public class MessageWithinTurnDocumentSyncToClientInsert extends Message implements Serializable{

    private String textInWindow;
    private int offset;
    private int windowNumber;
    private int length;
    private Object attr;

    public MessageWithinTurnDocumentSyncToClientInsert(String email, String username, int windowNumber, String textAppended, int offset, int length,Object attributeSetORString) {
        this(email, username, windowNumber, textAppended, offset, length);
        this.attr=attributeSetORString;
    }
    
    
    public MessageWithinTurnDocumentSyncToClientInsert(String email, String username, int windowNumber, String textAppended, int offset, int length) {
        super(email, username);
        this.setWindowNumber(windowNumber);
        this.setTextInWindow(textAppended);
        this.setOffset(offset);
        this.setLength(length);
    }
 
    public Object getAttr(){
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
