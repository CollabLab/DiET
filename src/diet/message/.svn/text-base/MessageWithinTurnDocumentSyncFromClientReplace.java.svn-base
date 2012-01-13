package diet.message;

import java.io.Serializable;

import javax.swing.text.AttributeSet;

public class MessageWithinTurnDocumentSyncFromClientReplace extends Message implements Serializable{

    private String textInWindow;
    private int offset;
    private int length;
    private AttributeSet attr;

    public MessageWithinTurnDocumentSyncFromClientReplace(String email, String username, String textAppended, int offset,int length,AttributeSet a) {
        this(email, username, textAppended, offset,length);
        attr=a;
    }
    
    
    public MessageWithinTurnDocumentSyncFromClientReplace(String email, String username, String textAppended, int offset,int length) {
        super(email, username);
        this.setTextInWindow(textAppended);
        this.setOffset(offset);
        this.setLength(length);
    }

    public AttributeSet getAttr(){
        return attr;
    }
    
    public String getTextInWindow(){
        return textInWindow;
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
    public String getMessageClass(){
    return "WYSIWYGDocSyncFromClient";
}

    public void setTextInWindow(String textInWindow) {
        this.textInWindow = textInWindow;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setLength(int length) {
        this.length = length;
    }

}
