package diet.message;
import java.io.Serializable;


public class MessageClientSetupParametersWYSIWYG extends MessageClientSetupParameters implements Serializable{

    private int windowType;
    private int windowOfOwnText = 0; // Check to see
    private boolean alignmentIsVertical;
    private int numberOfWindows;
    private int numberOfColumns;
    private int numberOfRows;
    private boolean windowIsEnabled;
    private String statusDisplay;
    private boolean statusIsInRed;
    private boolean participantHasStatusWindow;


    private long timeOfLastScroll; // This should be in a message from client to server



    public MessageClientSetupParametersWYSIWYG(String servername, String servername2) {
        super(servername,servername2);

    }

    public MessageClientSetupParametersWYSIWYG(String servername, String servername2, int numberOfRows, int numberOfColumns,boolean alignmentIsVertical,int numberOfWindows, int windowOfOwnText,boolean windowIsEnabled, boolean participantHasStatusWindow, String statusDisplay, boolean statusIsInRed){
       super (servername,servername2);

       this.setWindowType(getWindowType());
       this.setNumberOfWindows(numberOfWindows);
       this.setNumberOfColumns(numberOfColumns);
       this.setNumberOfRows(numberOfRows);
       this.setWindowOfOwnText(windowOfOwnText);
       this.setWindowIsEnabled(windowIsEnabled);
       this.setStatusDisplay(statusDisplay);
       this.setStatusIsInRed(statusIsInRed);
       this.setAlignmentIsVertical(alignmentIsVertical);
       this.setParticipantHasStatusWindow(participantHasStatusWindow);
    }

    public int getWindowType(){
        return windowType;
    }
    public int getNoOfWindows(){
        return getNumberOfWindows();
    }
    public int getNoOfCols(){
        return getNumberOfColumns();
    }
    public int getNoOfRows(){
        return getNumberOfRows();
    }
    public int getParticipantsTextWindow(){
        return getWindowOfOwnText();
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

    public void setWindowType(int windowType) {
        this.windowType = windowType;
    }

    public int getWindowOfOwnText() {
        return windowOfOwnText;
    }

    public void setWindowOfOwnText(int windowOfOwnText) {
        this.windowOfOwnText = windowOfOwnText;
    }

    public boolean isAlignmentIsVertical() {
        return alignmentIsVertical;
    }

    public void setAlignmentIsVertical(boolean alignmentIsVertical) {
        this.alignmentIsVertical = alignmentIsVertical;
    }

    public int getNumberOfWindows() {
        return numberOfWindows;
    }

    public void setNumberOfWindows(int numberOfWindows) {
        this.numberOfWindows = numberOfWindows;
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
