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
public class CBYCDocumentMeso extends DefaultStyledDocument{

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        String msg = "";
        if(offs>super.getLength()){
            offs=super.getLength();
             msg = "DOCUMENTMESOERROR"+ "INSERT_2"+ "Offset: "+offs+  "String:"+str;
              try{System.err.println(msg);
            ((CBYCDocumentWithEnforcedTurntaking)this).jf.getClientEventHandler().getCts().sendErrorMessage("ERROR IN THE CLIENT FROM:"+
                    ((CBYCDocumentWithEnforcedTurntaking)this).jf.getClientEventHandler().getCts().getUsername()+ " " +msg);
            }catch(Exception e){}
        }
        if(offs<0){
            offs=0;
            msg = "DOCUMENTMESOERROR"+ "INSERT_2"+ "Offset: "+offs+ "String:"+str;
             try{System.err.println(msg);
            ((CBYCDocumentWithEnforcedTurntaking)this).jf.getClientEventHandler().getCts().sendErrorMessage("ERROR IN THE CLIENT FROM:"+
                    ((CBYCDocumentWithEnforcedTurntaking)this).jf.getClientEventHandler().getCts().getUsername()+ " " +msg);
            }catch(Exception e){}
        }
        super.insertString(offs, str, a);
        
    }

    @Override
    public void remove(int offs, int len) throws BadLocationException {
        String msg="";
        if(offs+len>super.getLength()){
            offs=super.getLength()-len;
            msg = "DOCUMENTMESOERROR"+ "REMOVE_1"+ "Offset: "+offs+ "Length:"+len;
            try{System.err.println(msg);
            ((CBYCDocumentWithEnforcedTurntaking)this).jf.getClientEventHandler().getCts().sendErrorMessage("ERROR IN THE CLIENT FROM:"+
                    ((CBYCDocumentWithEnforcedTurntaking)this).jf.getClientEventHandler().getCts().getUsername()+ " " +msg);
            }catch(Exception e){}
        }
        if(offs<0){
            offs=0;
            msg = "DOCUMENTMESOERROR"+ "REMOVE_2"+ "Offset: "+offs+ "Length:"+len;
            try{System.err.println(msg);
            ((CBYCDocumentWithEnforcedTurntaking)this).jf.getClientEventHandler().getCts().sendErrorMessage("ERROR IN THE CLIENT FROM:"+
                    ((CBYCDocumentWithEnforcedTurntaking)this).jf.getClientEventHandler().getCts().getUsername()+ " " +msg);
            }catch(Exception e){}
        }
        super.remove(offs, len);
        
    }

    @Override
    public void replace(int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        String msg ="";
        if(offset+length>super.getLength()){
            offset=super.getLength()-length;
            msg = "DOCUMENTMESOERROR"+ "REPLACE_1"+ "Offset: "+offset+ "Length:"+length+ "String:"+text;
             try{System.err.println(msg);
            ((CBYCDocumentWithEnforcedTurntaking)this).jf.getClientEventHandler().getCts().sendErrorMessage("ERROR IN THE CLIENT FROM:"+
                    ((CBYCDocumentWithEnforcedTurntaking)this).jf.getClientEventHandler().getCts().getUsername()+ " " +msg);
            }catch(Exception e){}

        }
        if(offset<0){
            offset=0;
            msg = "DOCUMENTMESOERROR"+ "REPLACE_2"+ "Offset: "+offset+ "Length:"+length+ "String:"+text;
             try{System.err.println(msg);
            ((CBYCDocumentWithEnforcedTurntaking)this).jf.getClientEventHandler().getCts().sendErrorMessage("ERROR IN THE CLIENT FROM:"+
                    ((CBYCDocumentWithEnforcedTurntaking)this).jf.getClientEventHandler().getCts().getUsername()+ " " +msg);
            }catch(Exception e){}
        }
        super.replace(offset, length, text, attrs);
        

        
    }



}
