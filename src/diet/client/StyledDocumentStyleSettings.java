/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.client;

import java.awt.Color;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Vector;

import javax.swing.text.AttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import diet.server.Conversation;

/**
 *
 * @author Greg
 */
public class StyledDocumentStyleSettings implements Serializable{
    
    Color colorBackground = Color.WHITE;
    
    Color colorSelf = Color.BLACK;
    Color colorOther1 = Color.RED;
    Color colorOther2 = Color.BLUE;
    Color colorOther3 = Color.GREEN;
    Color colorOther4 = Color.cyan;
    Color colorOther5 = Color.BLUE;
    Color colorOther6 = Color.yellow;
    boolean underlineeditedText = true;
    boolean headerIsBold = true;
    boolean deletesPermitted = false;
    int fontSize =14;
    
    boolean systembeeponfloorclash = true;

    public StyledDocumentStyleSettings() {
    }
    
    public StyledDocumentStyleSettings(Color background,Color selfTextColor,Vector otherColors,boolean deletesPermitted,int fontSize){

        this.fontSize=fontSize;
        this.fontSize =14;
        this.colorBackground=background;
         colorSelf=selfTextColor;
         this.deletesPermitted=deletesPermitted;
         try{
            colorOther1 = (Color)otherColors.elementAt(0);
            colorOther2 = (Color)otherColors.elementAt(1);
            colorOther3 = (Color)otherColors.elementAt(2);
            colorOther4 = (Color)otherColors.elementAt(3);
            colorOther5 = (Color)otherColors.elementAt(4);
            colorOther6 = (Color)otherColors.elementAt(5);
         }catch (Exception e){
            
         }
      }
    public StyledDocumentStyleSettings(Color background,Color selfTextColor,Vector otherColors,boolean deletesPermitted,int fontSize, boolean beepOnFloorClash){
        this.fontSize=fontSize; 
        this.fontSize=14;
        this.colorBackground=background;
         colorSelf=selfTextColor;
         this.deletesPermitted=deletesPermitted;
         this.systembeeponfloorclash=beepOnFloorClash;
         try{
            colorOther1 = (Color)otherColors.elementAt(0);
            colorOther2 = (Color)otherColors.elementAt(1);
            colorOther3 = (Color)otherColors.elementAt(2);
            colorOther4 = (Color)otherColors.elementAt(3);
            colorOther5 = (Color)otherColors.elementAt(4);
            colorOther6 = (Color)otherColors.elementAt(5);
         }catch (Exception e){
            
         }
      }
    

   
    public void setStyles(StyledDocument doc){
        
        
        Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

        StyleConstants.setFontFamily(def, "SansSerif");       
        //Style regular = doc.addStyle("regular", def);
        Style regular = doc.addStyle("regular", null);
        StyleConstants.setFontSize(regular, fontSize);
        Style header = doc.addStyle("header", regular);
        StyleConstants.setBold(header, headerIsBold);
        Style headerself = doc.addStyle("hs", header);
        Style headerself_number = doc.addStyle("h0", header);
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
        Style normaltextself_number = doc.addStyle("n0", normaltext);
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
        Style editedtextself_number = doc.addStyle("e0", regular);
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
    
    public boolean getDeletesPermitted(){
        
        return this.deletesPermitted;
    }
    
    public Color getBackgroundColor(){
        return this.colorBackground;
    }
    
    public Color getCaretColor(){
        return this.colorSelf;
    }
    
    static public Color getColor(String colorName) {
        colorName = colorName.toUpperCase();
        try {
            // Find the field and value of colorName
            Field field = Class.forName("java.awt.Color").getField(colorName);
            return (Color)field.get(null);
        } catch (Exception e) {
            Conversation.printWSln("Main", "Bad color name: "+colorName);
            return null;
        }
    }

    public boolean performBeepOnFloorClash(){
        return this.systembeeponfloorclash;
    }
}
