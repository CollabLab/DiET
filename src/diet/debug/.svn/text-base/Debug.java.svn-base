/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.debug;

import diet.textmanipulationmodules.CyclicRandomTextGenerators.CyclicRandomParticipantIDGeneratorGROOP;
import java.util.Vector;

/**
 *
 * @author Greg
 */
public class Debug {

    static public CyclicRandomParticipantIDGeneratorGROOP groopLOGIN  = new CyclicRandomParticipantIDGeneratorGROOP();


    ///For the CBYC interface
    static  public boolean generateFakeCBYCTurns = false;
    static  public boolean generateOnlySplitUttCBYCTurns = false;
    static  public boolean generateOutputForSequencesInMainWindow = true; //This doesn't do anything yet!
    static  public boolean showEachStateChangeOnclient=false;//true;
    static  public boolean showEnqueuedDocInsertsInCapitals =false;
    static  public boolean showCounterAfterCBYCStatusMessage = false;
    static  public boolean showPrefixBeforeCBYCTurn = false;

    static public boolean trackTypeError=false; //created to track a bug with CBYC interface...
    static public boolean trackCBYCDyadError = false;
    
    static public boolean trackSeqADDDOCCHANGE = false;

    static public boolean showErrorsForMacIntegration = false;
    static public boolean showErrorsCalculatingDocInsertsSent = false;
    static public boolean showMazeGameCR = false;
    static public boolean showREMOVEERROR = false;

    static public boolean showEOFCRSTATES = false;

    static public boolean debugFIFOBufferForceSystemExit = false;
    static public boolean showFloorHolderAdvancedInfo = true;

    static public boolean inCRENDOFTURNJUMPTOMAZE6 = false;
    static public boolean trackLockedInterface = false;


    static public boolean serverProvidesParticipantName = false;
    static public boolean allowCHATCLIENTTOSENDDEBUGCOMMANDS = true;

    static public boolean debugWebpageDisplay = true;

    static public boolean generateFAKEIsTypingAndTurns = false;
    static public boolean bypassCLIENTGUIPRINTOUT = false;

    static public boolean doAUTOLOGIN = false;//true;//false;//true;

    static public boolean letQUEUESTACKUPONCLIENT = false;
    static public boolean generateVERBOSELISTOUTPUTFORCOLLABTASK = true;
    static public boolean verboseOUTPUT = false;
    static public boolean debugGROOP = true;
   
    static public long debugGROOPSpeedUpTime = 1;
    static public boolean debugBLOCKAGE = false;

    static public boolean debugPICTURELOADING = true;


    static public boolean debugGROOP3 = true;

    static public boolean debugREACT = true;
    static public boolean debugREACTChangeThread = true;

    static public boolean debugREACTDELAYRELEASE = false;
    static public boolean debugREACTDOAUTORESPONSEOFPARTICIPANT1 = false;


    static DebugWindow dbg;
    static public void printDBG(String s){
        if (dbg ==null){
            dbg=new DebugWindow();
        }
         dbg.append(s);
    }
    static public void printDBG_andclear(String s){
        if (dbg ==null){
            dbg=new DebugWindow();
        }
         dbg.setText(s);
    }



}
