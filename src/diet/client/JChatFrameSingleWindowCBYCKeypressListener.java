/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.client;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author Greg
 */
public class JChatFrameSingleWindowCBYCKeypressListener implements KeyListener{

                
    CBYCDocumentWithEnforcedTurntaking cbycDoc;
    
    
    public JChatFrameSingleWindowCBYCKeypressListener(CBYCDocumentWithEnforcedTurntaking cbycDoc) {
        this.cbycDoc=cbycDoc;
        
    }

    
    
    
    public void keyPressed(KeyEvent e) {
        if(cbycDoc.getState()==CBYCDocumentWithEnforcedTurntaking.othertyping && cbycDoc.performBeepOnFloorClash()){
            Toolkit.getDefaultToolkit().beep(); 
            //diet.server.Conversation.printWSln("BEEP","KEYPRESSBEEP");
        }
    }

    public void keyReleased(KeyEvent e) {
        
    }

    public void keyTyped(KeyEvent e) {
        
    }

}
