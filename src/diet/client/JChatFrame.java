package diet.client;

import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import diet.server.CbyC.DocInsert;


public class JChatFrame extends JFrame {

    private boolean textEntryAndSendEnabled = true;
    private Vector textComponents = new Vector();
    private ClientEventHandler clEventHandler;

    //public static final int "Formulate and revise then send" = 0;
    //static public final int WYSIWYG_MULTIPLE_SEPARATE =1;
    //static public final int WYSIWYG_SINGLE =2;
    //static public final int WYSIWYG_SINGLE_KEYPRESS_INTERCEPTED =3;
    
    /**
     * abstract description of all chat tool window instances.
     * 
     * @param clevh
     * @throws java.awt.HeadlessException
     */
    public JChatFrame(ClientEventHandler clevh) throws HeadlessException {
        super(clevh.getCts().getUsername());
        clEventHandler = clevh;
         this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    }

    

    public void setEditable(boolean editable){
        System.err.println("Error with setEditable..should use subclass");
        
    }
    
    
    
    /**
     * Appends text to particular window number, making sure that the caret position is at the turn-terminal position
     * @param s
     * @param windownumber
     */
    public void appendWithCaretCheck(String s, int windownumber){
        System.err.println("Error with appendText...should use subclass");
    }


    public void appendWithCaretCheck(String s, int windownumber, Object attributeset){
        System.err.println("Error with appendText...should use subclass");
    }


    /**
     * Sets text and colour of the text (black/red) in the status bar window. Also determines whether text entry is subsequently
     * permitted.
     * @param windowOfOwnText
     * @param label
     * @param textIsInRed
     * @param setBlocked
     */
    public void  setLabelAndTextEntryEnabled(int windowOfOwnText,String label, boolean textIsInRed, boolean setBlocked){
        System.err.println("error with enabling send button and text entry....should be subclassed");
    }
    /**
     * Sets String in Status window (black/red)
     * @param windowNumber
     * @param label
     * @param textIsInRed
     */
    public void setLabel(int windowNumber,String label,boolean textIsInRed){
        System.err.println("error with setting label.....should be subclassed");
    }    
    /**
     *  
     * @return the text entered in the text entry window of the chat window
     */
    public String getTextEnteredInField(){
        return new String("Error in JChatFrame, getTextEnteredInField should be overriden");
    }

    /**
     * Sets the text entered in the text entry window
     * @param s
     */
    public void setTextEntryField(String s){
        System.err.println("Error Set TextEntryField should be subclassed");
    }

    public ClientEventHandler getClientEventHandler(){
        return this.clEventHandler;
    }

    /**
     * Adds String to the WYSIWYG chat window at the specified position
     * @param windowNumber
     * @param replacementText
     * @param offset
     * @param length
     */
    public void wYSIWYGUpdateDocumentInsert(int windowNumber, String replacementText,int offset,int length){
             System.err.println("Error...setting WYSIWYG Text in superclass");
    }
    /**
     * Removes String fromo the WYSIWYG chat window at the specified position
     * @param windowNumber
     * @param offset
     * @param length
     */
    public void wYSIWYGUpdateDocumentRemove(int windowNumber,int offset,int length){
             System.err.println("Error...setting WYSIWYG Text in superclass");
    }
    
    /**
     * Changes the position of the cursor and the selected text in the WYSIWYG chat window
     * @param windowNumber
     * @param startPos
     * @param finishPos
     */
    public void wYSIWYGChangeCursorAndSelection(int windowNumber,int startPos,int finishPos){
                 System.err.println("Error...changing WYSIWYG Cursor in superclass");
    }

    /**
     * Determines whether the display of cursor position and text highlighting is permitted in chat window
     * @param windowNumber
     * @param cursorIsDisplayed
     * @param selectionIsDisplayed
     */
    public void wYSIWYGsetCursorAndSelectionAreDisplayedWYSIWYG(int windowNumber, boolean cursorIsDisplayed, boolean selectionIsDisplayed){
        System.err.println("Error...setting WYSIWYG Cursor in superclass");
    }

    /**
     * Relays whether request for conversational floor has been permitted / client has lost floor
     * (e.g. through timeout determined by server) in order to block / allow text entry in the chat window / 
     * @param status
     * @param msg
     */
    public void wWYSIWYGChangeInterceptStatus(int status, String msg){
        System.err.println("Error...setting intercept status in superclass");
    }

    
    public void cBYCChangeInterceptionStatus(int status,DocInsert prefix,long id){
        System.err.println("Error...setting intercept status in superclass");
    }
    
     public void cBYCUpdateDocumentInsert(int windowNumber, String replacementText,int offset,int length,Object a){
             System.err.println("Error...setting CBYC Text in superclass");
    }
    
    public void cBYCUpdateDocumentRemove(int windowNumber,int offset,int length){
             System.err.println("Error...setting CBYC Text in superclass");
    }
    
    public void cBYCChangeCursorAndSelection(int windowNumber,int startPos,int finishPos){
                 System.err.println("Error...changing CBYC Cursor in superclass");
    }
   
    public void cBYCsetCursorAndSelectionAreDisplayedCBYC(int windowNumber, boolean cursrIsDisplayed, boolean selIsDisplayed){
                 System.err.println("Error..settingCBYC Cursor");
    }
    
    
    public void withinTurnUpdateDocumentInsert(int windowNumber, String replacementText,int offset,int length,Object attributeSetOrString){
             System.err.println("Error...setting Within Turn Insert Text in superclass");
    }
    
    
    
    public void withinTurnUpdateDocumentRemove(int windowNumber,int offset,int length){
             System.err.println("Error...setting Within Turn Remove Text in superclass");
    }
    
    /**
     * Changes the position of the cursor and the selected text in the WYSIWYG chat window
     * @param windowNumber
     * @param startPos
     * @param finishPos
     */
    public void withinTurnChangeCursorAndSelection(int windowNumber,int startPos,int finishPos){
                 System.err.println("Error...changing Within Turn Change Cursor in superclass");
    }

    /**
     * Determines whether the display of cursor position and text highlighting is permitted in chat window
     * @param windowNumber
     * @param cursorIsDisplayed
     * @param selectionIsDisplayed
     */
    public void withinTurnSetCursorAndSelectionAreDisplayed(int windowNumber, boolean cursorIsDisplayed, boolean selectionIsDisplayed){
        System.err.println("Error...setting Within Turn Cursor settings in superclass");
    }

    /**
     * Relays whether request for conversational floor has been permitted / client has lost floor
     * (e.g. through timeout determined by server) in order to block / allow text entry in the chat window / 
     * @param status
     * @param msg
     */
    
    
      public void setTextEntrytext(String s){
          
      }
    
    
    
    
    /**
     * Returns the window number of the window in which the participant's own text is displayed
     * @return window number of participant's text
     */
    public int getParticipantsOwnWindow(){
       return 0;
     }

    public JChatFrame(GraphicsConfiguration gc) {
        super(gc);
    }

    public JChatFrame(String title) throws HeadlessException {
        super(title);
    }

    public JChatFrame(String title, GraphicsConfiguration gc) {
        super(title, gc);
    }
    
    public void changeInterfaceProperties(int newInterfaceproperties){
        
    }
    
    
    public void closeDown(){
        this.clEventHandler=null;
        super.dispose();
    }
}
