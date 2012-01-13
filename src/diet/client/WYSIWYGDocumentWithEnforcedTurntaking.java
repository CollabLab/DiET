package diet.client;

import java.util.Vector;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.PlainDocument;

import diet.server.CbyC.DocInsert;
import diet.server.CbyC.DocRemove;



/*
 *This is a custom implementation of PlainDocument (the underlying component that controls what is displayed
 * in java text components. It intercepts any attempt to change the text in the client control window, sends
 * a request to the server asking if it is permitted (gains the conversational floor). If permission is granted
 * the text is displayed. This class is used in the WYSIWYGSinglewindow interface to regulate turn-taking. 
 * 
 * 
 * 
 * @author user
 */
public class WYSIWYGDocumentWithEnforcedTurntaking extends PlainDocument{
        
        public final static int typingunhindered =0;
        public final static int othertyping = 1;
        public final static int nooneelsetyping =2;
        private int state=2;
        private boolean allowInnerWorkings = false;
       
        private JChatFrameSingleWindowWYSIWYGWithEnforcedTurntaking jf;
        
        private Vector pendingInserts = new Vector();
        
        public WYSIWYGDocumentWithEnforcedTurntaking(JChatFrameSingleWindowWYSIWYGWithEnforcedTurntaking jf){
            super();
            this.jf = jf;
        }
        
        /**
         * Forces insert of text at the specified position in the text
         * @param offset
         * @param text
         * @param attr
         */
        public  void forceInsert(int offset,String text, AttributeSet attr){
            try{
              allowInnerWorkings = true;  
              super.insertString(offset,text,attr);
              //System.out.println(jf.getClientEventHandler().getCts().getUsername()+": String being ForceInserted has length "+text.length()+"AND IS:"+text+":");
            }
            catch (Exception e){
                System.out.println(jf.getClientEventHandler().getCts().getUsername()+": "+"------------------ERROR FORCING INSERT-----------------"+e.getCause().toString());
            }
            allowInnerWorkings = false;  
        }
        
        /**
         * Forces remove of text at the specified position in the text
         * @param offset
         * @param length
         */
        public  void forceRemove(int offset,int length){
            try{
               allowInnerWorkings = true;   
               super.remove(offset,length);
               System.out.println(jf.getClientEventHandler().getCts().getUsername()+": "+"String being ForceRemoved has offset: "+offset+" and length: "+length);
            }
            catch (Exception e){
                System.err.println("------------------ERROR FORCING REMOVING-----------------"+e.getCause().toString());
            }
            allowInnerWorkings = false;  
        }
        
        /**
         * Sets the state of the conversational floor 
         * @param newState
         * @param msg
         */
        public synchronized void setState(int newState,String msg){
            System.err.println(jf.getClientEventHandler().getCts().getUsername()+": "+"Setting state from "+state+" to "+newState);
            try{
                super.insertString(super.getLength(),msg,null);
            if (this.state==nooneelsetyping&newState==typingunhindered){
                this.state=newState;
                for(int i=0;i<pendingInserts.size();i++){
                    Object o = (Object)pendingInserts.elementAt(i);
                    if(o instanceof DocInsert){
                       DocInsert docII = (DocInsert)o;
                       jf.getClientEventHandler().textEntryDocumentHasChangedInsert(docII.getStr(),super.getLength()-docII.getOffs()-msg.length(),docII.getStr().length(), "THIS CLASS IS DEPRECATED:ID5");
                       super.insertString(docII.getOffs()+msg.length(),docII.getStr(),(AttributeSet)docII.getAttrSet());
                    //-----------------+msg.length() because it is added prior to the already enqueud utterances
                    }
                    else if(o instanceof DocRemove){
                       DocRemove docRR = (DocRemove)o;
                       jf.getClientEventHandler().wYSIWYGDocumentHasChangedRemove(super.getLength()-docRR.getOffs(),docRR.getLen());
                       super.remove(docRR.getOffs(),docRR.getLen());
                    //-----------------+msg.length() because it is added prior to the already enqueud utterances
                    }
               }
               pendingInserts.removeAllElements();
               
            }
            else if(newState==othertyping){
                pendingInserts.removeAllElements();
                this.state = newState;
            }
            else{
                this.state= newState;
               
            }
            }catch (Exception e){
                System.err.println("ERROR SETTING STATE "+e.getCause().toString());
            }
        }
        /**
         * Called when participant types text when no other participants are typing. A request is sent to the server to
         * be granted the conversational floor.
         * @param offSetFromRight
         * @param str
         */
        public void requestTypingUnhindered(int offSetFromRight,String str){
             //Call
             jf.getClientEventHandler().wYSIWYGrequestTypingUnhindered(offSetFromRight,str);
        }
        
        
       
        /**
         * 
         * Intercepts Swing's attempt to add String to the chat window. If participant has floor this is permitted.
         * If no other participant has the floor, a request is sent to the server to gain the conversational floor.
         * @param offs
         * @param str
         * @param a
         */
        public void insertString(int offs, String str, AttributeSet a){
              //System.err.println(jf.getClientEventHandler().getCts().getUsername()+": "+"insertString with offset"+offs+" "+str);
              try{
                if(state==typingunhindered){
                     if(str.endsWith("\n")){
                       //  super.insertString(offs,)
                     }
                     super.insertString(offs,str,a);
                     //System.out.println("String being sent has length "+str.length()+" String is:"+str+":");
                }
                else if(state==nooneelsetyping){
                    requestTypingUnhindered(super.getLength()-offs,str);
                    DocInsert docII = new DocInsert (offs,str,a);
                    pendingInserts.addElement(docII);
                }
                
              } catch(Exception e){
                  System.err.println("BAD LOCATION INSERTING");
              }
        }
        
        /**
         * Intercepts Swing's attempt to remove text from the chat window. If participant has floor this is permitted.
         * If no other participant has the floor, a request is sent to the server to gain the conversational floor.
         * @param offs
         * @param len
         */
         public void remove(int offs, int len){
             System.out.println("CALLING REMOVE");
              try{
                if(state==typingunhindered){
                    super.remove(offs,len);
                    System.out.println("String being REMOVED and SENT has offset: "+offs+" and length: "+len);
                }
                 else if(state==nooneelsetyping){
                    requestTypingUnhindered(super.getLength()-offs,"");                            
                    DocRemove docIR = new DocRemove (offs,len);
                    pendingInserts.addElement(docIR);
                
                    
                    
                    
                }
                
              } catch(Exception e){
                  System.err.println("BAD LOCATION INSERTING");
              }
        } 
        public void insertUpdate(AbstractDocument.DefaultDocumentEvent chng, AttributeSet attr){
                //System.out.println("CALLING INSERTUPDATE");
                //if(state==typingunhindered||allowInnerWorkings)
                    super.insertUpdate(chng, attr);              
        }
        public  void removeUpdate(AbstractDocument.DefaultDocumentEvent chng){
                //System.out.println("CALLING REMOVEUPDATE");
                //if(state==typingunhindered||allowInnerWorkings)
                    super.removeUpdate(chng);
        }
        
    }