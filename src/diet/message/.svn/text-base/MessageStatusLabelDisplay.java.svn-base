package diet.message;

import java.io.Serializable;


public class MessageStatusLabelDisplay extends Message implements Serializable {

    private int windowNumber;
    private String textToDisplay;
    private boolean labelIsRed;
    



    public MessageStatusLabelDisplay(String email, String username,int windowNumber, String textToDisplay, boolean labelIsRed) {
        super(email, username);
        this.setTextToDisplay(textToDisplay);
        this.setLabelIsRed(labelIsRed);
        this.setWindowNumber(windowNumber);
    }


    public boolean isLabelRed(){
        return isLabelIsRed();
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

}
