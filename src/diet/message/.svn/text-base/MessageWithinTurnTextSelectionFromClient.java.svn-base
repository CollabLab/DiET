package diet.message;

import java.io.Serializable;

public class MessageWithinTurnTextSelectionFromClient extends Message implements Serializable {

    private int startIndex;
    private int finishIndex;

    public MessageWithinTurnTextSelectionFromClient(String email, String username, int startIndex, int finishIndex) {
        super(email, username);
        this.setStartIndex(startIndex);
        this.setFinishIndex(finishIndex);
    }

    public int getStartIndex(){
        return startIndex;
    }

    public int getFinishIndex(){
        return finishIndex;
    }

    public int getCorrectedStartIndex(){
        if(getStartIndex()>getFinishIndex())return getFinishIndex();
        return getStartIndex();
    }
    public int getCorrectedFinishIndex(){
       if(getStartIndex()>getFinishIndex())return getStartIndex();
       return getFinishIndex();
    }
    public String getMessageClass(){
  return "WYSIWYGTextSelFrmClient";
}

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public void setFinishIndex(int finishIndex) {
        this.finishIndex = finishIndex;
    }

}
