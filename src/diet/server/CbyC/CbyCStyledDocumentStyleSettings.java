/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server.CbyC;

import java.awt.Color;

import javax.swing.text.AttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

/**
 *
 * @author Greg
 */
public class CbyCStyledDocumentStyleSettings {
    Color colorSelf = Color.BLACK;
    Color colorOther1 = Color.magenta;
    Color colorOther2 = Color.GREEN;
    Color colorOther3 = Color.ORANGE;
    Color colorOther4 = Color.cyan;
    Color colorOther5 = Color.magenta;
    Color colorOther6 = Color.yellow;
    boolean underlineeditedText = true;
    boolean headerIsBold = true;
    
    public CbyCStyledDocumentStyleSettings(StyledDocument doc){
        
        /*
         * Self normal text. Self-edited. Self header.
         * Other normal text. Other-edited. Other header
         * 
         */
         setStyles(doc);
    }
    
    public void setStyles(StyledDocument doc){
        
        
        Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

        Style regular = doc.addStyle("regular", def);
        StyleConstants.setFontFamily(def, "SansSerif");
        StyleConstants.setFontSize(def, 12);

        Style header = doc.addStyle("header", regular);
        StyleConstants.setBold(header, headerIsBold);
        Style headerself = doc.addStyle("hs", header);
        Style headerother1 = doc.addStyle("h1", header);
        Style headerother2 = doc.addStyle("h2", header);
        Style headerother3 = doc.addStyle("h3", header);
        Style headerother4 = doc.addStyle("h4", header);
        Style headerother5 = doc.addStyle("h5", header);
        Style headerother6 = doc.addStyle("h6", header);
        StyleConstants.setForeground(headerself, colorSelf);
        StyleConstants.setForeground(headerother1, colorOther1);
        StyleConstants.setForeground(headerother2, colorOther2);
        StyleConstants.setForeground(headerother3, colorOther3);
        StyleConstants.setForeground(headerother4, colorOther4);
        StyleConstants.setForeground(headerother5, colorOther5);
        StyleConstants.setForeground(headerother6, colorOther6);
        
        
        Style normaltext = doc.addStyle("normaltext", regular);
        StyleConstants.setBold(normaltext, false);
        Style normaltextself = doc.addStyle("ns", normaltext);
        Style normaltextother1 = doc.addStyle("n1", normaltext);
        Style normaltextother2 = doc.addStyle("n2", normaltext);
        Style normaltextother3 = doc.addStyle("n3", normaltext);
        Style normaltextother4 = doc.addStyle("n4", normaltext);
        Style normaltextother5 = doc.addStyle("n5", normaltext);
        Style normaltextother6 = doc.addStyle("n6", normaltext);
        StyleConstants.setForeground(normaltextself, colorSelf);
        StyleConstants.setForeground(normaltextother1, colorOther1);
        StyleConstants.setForeground(normaltextother2, colorOther2);
        StyleConstants.setForeground(normaltextother3, colorOther3);
        StyleConstants.setForeground(normaltextother4, colorOther4);
        StyleConstants.setForeground(normaltextother5, colorOther5);
        StyleConstants.setForeground(normaltextother6, colorOther6);
        
        
        
        Style editedtext = doc.addStyle("editedtext", regular);
        StyleConstants.setUnderline(editedtext,underlineeditedText );
        Style editedtextself = doc.addStyle("es", regular);
        Style editedother1 = doc.addStyle("e1", editedtext);
        Style editedother2 = doc.addStyle("e2", editedtext);
        Style editedother3 = doc.addStyle("e3", editedtext);
        Style editedother4 = doc.addStyle("e4", editedtext);
        Style editedother5 = doc.addStyle("e5", editedtext);
        Style editedother6 = doc.addStyle("e6", editedtext);
        StyleConstants.setForeground(editedtextself, colorSelf);
        StyleConstants.setForeground(editedother1, colorOther1);
        StyleConstants.setForeground(editedother2, colorOther2);
        StyleConstants.setForeground(editedother3, colorOther3);
        StyleConstants.setForeground(editedother4, colorOther4);
        StyleConstants.setForeground(editedother5, colorOther5);
        StyleConstants.setForeground(editedother6, colorOther6);
    }
            
    static public String getAttributeSetAsString(AttributeSet a){
        Style s = (Style)a;
        String name =s.getName().substring(0, 2);
        return name;
    }
}
