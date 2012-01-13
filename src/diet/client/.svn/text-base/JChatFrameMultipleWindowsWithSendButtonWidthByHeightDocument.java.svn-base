/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.client;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;

/**
 *
 * @author sre
 */
public class JChatFrameMultipleWindowsWithSendButtonWidthByHeightDocument extends DefaultStyledDocument{

    JChatFrameMultipleWindowsWithSendButtonWidthByHeight jcfmwwsbwbh;

    public JChatFrameMultipleWindowsWithSendButtonWidthByHeightDocument(JChatFrameMultipleWindowsWithSendButtonWidthByHeight jcfmwwsbwbh) {
       this.jcfmwwsbwbh = jcfmwwsbwbh;
    }




    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if(super.getLength()+str.length()>this.jcfmwwsbwbh.maxcharlength)return;

        super.insertString(offs, str, a);
    }

    @Override
    public void remove(int offs, int len) throws BadLocationException {
        super.remove(offs, len);
    }

    @Override
    public void replace(int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        int totallengthOfDocument = super.getLength();
        int totallengthOfProposedChanges = text.length()-offset;
        if(totallengthOfProposedChanges>totallengthOfDocument + this.jcfmwwsbwbh.maxcharlength)return;

        super.replace(offset, length, text, attrs);
    }



}
