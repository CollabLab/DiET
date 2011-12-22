package diet.message;

import java.io.Serializable;


public class MessageStatusLabelDisplayAndBlocked extends Message implements Serializable {

    private int windowNumber;
    private String textToDisplay;
    private boolean labelIsRed;
    private boolean setScreenEnabled;



    public MessageStatusLabelDisplayAndBlocked(String email, String username,int windowNumber, String textToDisplay, boolean labelIsRed,boolean screenEnabled) {
        super(email, username);
        this.setTextToDisplay(textToDisplay);
        this.setLabelIsRed(labelIsRed);
        this.setSetScreenEnabled(screenEnabled);
        this.setWindowNumber(windowNumber);

    }


    public boolean isLabelRed(){
        return isLabelIsRed();
    }

    public boolean isScreenToBeEnabled(){
        return isSetScreenEnabled();
    }

    public String getTextToDisplay(){
        return textToDisplay;
    }

    public int getWindowNumber(){
        return windowNumber;
    }

    public boolean displayInAllWindows(){
        if (getWindowNumber()<0)return true;
        return false;
    }

    public void setWindowNumber(int windowNumber) {
        this.windowNumber = windowNumber;
    }

    public void setTextToDisplay(String textToDisplay) {
        this.textToDisplay = textToDisplay;
    }

    public boolean isLabelIsRed() {
        return labelIsRed;
    }

    public void setLabelIsRed(boolean labelIsRed) {
        this.labelIsRed = labelIsRed;
    }

    public boolean isSetScreenEnabled() {
        return setScreenEnabled;
    }

    public void setSetScreenEnabled(boolean setScreenEnabled) {
        this.setScreenEnabled = setScreenEnabled;
    }

}
