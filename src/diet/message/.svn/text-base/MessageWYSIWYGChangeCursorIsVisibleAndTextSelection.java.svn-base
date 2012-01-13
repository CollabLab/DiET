package diet.message;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class MessageWYSIWYGChangeCursorIsVisibleAndTextSelection extends  Message {

    private boolean cursorIsVisible;
    private boolean textSelectionIsEnabled;
    private int windowNumber;

    public MessageWYSIWYGChangeCursorIsVisibleAndTextSelection(String email, String username, int windowNumber, boolean cursorIsVisible, boolean textSelectionIsEnabled) {
        super(email, username);
        this.setWindowNumber(windowNumber);
        this.setCursorIsVisible(cursorIsVisible);
        this.setTextSelectionIsEnabled(textSelectionIsEnabled);
    }

    public boolean getCursorIsVisible(){
        return isCursorIsVisible();
    }

    public boolean getTextSelectionIsEnabled(){
        return isTextSelectionIsEnabled();
    }

    public int getWindowNumber(){
        return windowNumber;
    }
    public String getMessageClass(){
    return "WYSIWYGSetCursorAndSelectionVisible";
}

    public boolean isCursorIsVisible() {
        return cursorIsVisible;
    }

    public void setCursorIsVisible(boolean cursorIsVisible) {
        this.cursorIsVisible = cursorIsVisible;
    }

    public boolean isTextSelectionIsEnabled() {
        return textSelectionIsEnabled;
    }

    public void setTextSelectionIsEnabled(boolean textSelectionIsEnabled) {
        this.textSelectionIsEnabled = textSelectionIsEnabled;
    }

    public void setWindowNumber(int windowNumber) {
        this.windowNumber = windowNumber;
    }

}
