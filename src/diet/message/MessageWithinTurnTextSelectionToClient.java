package diet.message;

import java.io.Serializable;

public class MessageWithinTurnTextSelectionToClient extends Message implements Serializable {

    private int startIndex;
    private int finishIndex;
    private int windowNumber;

    public MessageWithinTurnTextSelectionToClient(String email, String username, int windowNumber,int startIndex, int finishIndex) {
        super(email, username);
        this.setStartIndex(startIndex);
        this.setFinishIndex(finishIndex);
        this.setWindowNumber(windowNumber);
    }

    public int getStartIndex(){
        return startIndex;
    }

    public int getFinishIndex(){
        return finishIndex;
    }

    public int getWindowNumber(){
        return windowNumber;
    }
    public String getMessageClass(){
  return "WYSIWYGTextSelToClient";
}

    public void setWindowNumber(int windowNumber) {
        this.windowNumber = windowNumber;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public void setFinishIndex(int finishIndex) {
        this.finishIndex = finishIndex;
    }

}
