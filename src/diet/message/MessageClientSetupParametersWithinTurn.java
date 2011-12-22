package diet.message;
import java.io.Serializable;


public class MessageClientSetupParametersWithinTurn extends MessageClientSetupParameters implements Serializable{

    private boolean alignmentIsVertical;
    private int numberOfColumns;
    private int numberOfRows;
    private boolean windowIsEnabled;
    private String statusDisplay;
    private boolean statusIsInRed;
    private boolean participantHasStatusWindow;


    private long timeOfLastScroll; // This should be in a message from client to server



    public MessageClientSetupParametersWithinTurn(String servername, String servername2) {
        super(servername,servername2);

    }

    public MessageClientSetupParametersWithinTurn(String servername, String servername2, int numberOfRows, int numberOfColumns,boolean alignmentIsVertical,boolean windowIsEnabled, boolean participantHasStatusWindow, String statusDisplay, boolean statusIsInRed){
       super (servername,servername2);
       this.setNumberOfColumns(numberOfColumns);
       this.setNumberOfRows(numberOfRows);
       this.setWindowIsEnabled(windowIsEnabled);
       this.setStatusDisplay(statusDisplay);
       this.setStatusIsInRed(statusIsInRed);
       this.setAlignmentIsVertical(alignmentIsVertical);
       this.setParticipantHasStatusWindow(participantHasStatusWindow);
    }

    
    public int getNoOfCols(){
        return getNumberOfColumns();
    }
    public int getNoOfRows(){
        return getNumberOfRows();
    }
   
    public boolean getParticipantsWindowIsEnabled(){
        return isWindowIsEnabled();
    }
    public String getStatusDisplay(){
        return statusDisplay;
    }
    public boolean getStatusIsInRed(){
        return isStatusIsInRed();
    }
    public boolean getAlignmentIsVertical(){
        return this.isAlignmentIsVertical();
    }
    public boolean getParticipantHasStatusWindow(){
        return isParticipantHasStatusWindow();
    }
    public String getMessageClass(){
    return "ClientSetupSENDBUTTON";
}

    public boolean isAlignmentIsVertical() {
        return alignmentIsVertical;
    }

    public void setAlignmentIsVertical(boolean alignmentIsVertical) {
        this.alignmentIsVertical = alignmentIsVertical;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public void setNumberOfColumns(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    public boolean isWindowIsEnabled() {
        return windowIsEnabled;
    }

    public void setWindowIsEnabled(boolean windowIsEnabled) {
        this.windowIsEnabled = windowIsEnabled;
    }

    public void setStatusDisplay(String statusDisplay) {
        this.statusDisplay = statusDisplay;
    }

    public boolean isStatusIsInRed() {
        return statusIsInRed;
    }

    public void setStatusIsInRed(boolean statusIsInRed) {
        this.statusIsInRed = statusIsInRed;
    }

    public boolean isParticipantHasStatusWindow() {
        return participantHasStatusWindow;
    }

    public void setParticipantHasStatusWindow(boolean participantHasStatusWindow) {
        this.participantHasStatusWindow = participantHasStatusWindow;
    }

    public long getTimeOfLastScroll() {
        return timeOfLastScroll;
    }

    public void setTimeOfLastScroll(long timeOfLastScroll) {
        this.timeOfLastScroll = timeOfLastScroll;
    }

}
