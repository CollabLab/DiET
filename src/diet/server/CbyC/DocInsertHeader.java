/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.CbyC;

import javax.swing.text.AttributeSet;

/**
 *
 * @author Greg
 */
public class DocInsertHeader extends DocInsert{

    
    
    public DocInsertHeader(int offs, String str, Object attributeSetORStyle) {
        super(offs, str, attributeSetORStyle);
    }

    public DocInsertHeader(String sender, String apparentSender, String recipient, int offs, String str, Object a) {
        super(sender, apparentSender, recipient, offs, str, a);
    }

    public DocInsertHeader(String sender, String apparentSender, String recipient, int offs, String str, AttributeSet a, int rowNumber, int positionWithinRow) {
        super(sender, apparentSender, recipient, offs, str, a, rowNumber, positionWithinRow);
    }

    public DocInsertHeader(String sender, String apparentSender, String recipient, int offs, String str, Object a, String elementString, int elementStart, int elementFinish, int docSize) {
        super(sender, apparentSender, recipient, offs, str, a, elementString, elementStart, elementFinish, docSize);
    }
 
            
}
