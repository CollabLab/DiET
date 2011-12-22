package diet.client;

import java.awt.Toolkit;
import java.util.Date;
import java.util.Vector;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.Style;

import diet.debug.Debug;
import diet.server.Conversation;
import diet.server.CbyC.DocChange;
import diet.server.CbyC.DocInsert;
import diet.server.CbyC.DocRemove;
import diet.utils.VectorToolkit;



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
public class CBYCDocumentWithEnforcedTurntaking extends CBYCDocumentMeso{


        public final static int typingunhindered =0;
        public final static int othertyping = 1;
        public final static int nooneelsetyping =2;
        private int state=2;
        //private boolean allowInnerWorkings = false;

        public JChatFrameSingleWindowCBYCWithEnforcedTurntaking jf;

        private Vector pendingInserts = new Vector();

        public StyledDocumentStyleSettings wtsdss;

        private boolean requestSent = false;

        //public boolean deletesAllowed = false;

        final int deletesANYWHERE = 2;
        final int deletesENDOFTURNONLY = 1;
        final int neither =0;

        public int deletesAllowed;


        public CBYCDocumentWithEnforcedTurntaking(JChatFrameSingleWindowCBYCWithEnforcedTurntaking jf, StyledDocumentStyleSettings styles){
            super();
            this.jf = jf;
            wtsdss = styles;
            styles.setStyles(this);
            //// this.deletesAllowed=wtsdss.getDeletesPermitted();
            if (wtsdss.getDeletesPermitted()) deletesAllowed=this.deletesENDOFTURNONLY;
            else deletesAllowed = this.neither;

            //if(deletesAllowed==this.deletesNoBackspace)jf.jTextPane.getCaret().


            //Color c = jf.jTextPane.getForeground();
            //jf.jTextPane.setForeground(Color.red);
            String s = "Character-by-character chat tool interface. Version 9.1\n"+
                       "Remember: It is not necessary to press RETURN/ENTER";
            try{
              //super.insertString(0, s, null);
            }catch(Exception e){
            }
        }

        /**
         * Forces insert of text at the specified position in the text
         * @param offset
         * @param text
         * @param attr
         */
        public synchronized void forceInsert(int offset,String text, Object a){

            try{


              if(a==null){
                  super.insertString(offset,text,null);
                  //////System.err.println(text+"INCLIENTDOCUMENT1 IS null");
              }
              else if(a instanceof Style){
                  super.insertString(offset,text,(Style)a);
                  //////System.err.println(text+"INCLIENTDOCUMENT2 IS "+a.toString());
              }
              else if(a instanceof String){
                  super.insertString(offset,text,super.getStyle((String)a));
                  //////System.err.println(text+"INCLIENTDOCUMENT3 IS "+a.toString());
              }

              ////////System.out.println(jf.getClientEventHandler().getCts().getUsername()+": String being ForceInserted has length "+text.length()+"AND IS:"+text+":");
            }
            catch (Exception e){
                e.printStackTrace();
                ////////System.out.println(jf.getClientEventHandler().getCts().getUsername()+": "+"------------------ERROR FORCING INSERT-----------------"+e.getCause().toString());
            }

        }

        /**
         * Forces remove of text at the specified position in the text
         * @param offset
         * @param length
         */
        public  synchronized void forceRemove(int offset,int length){
            try{

               super.remove(offset,length);
               //////System.out.println(jf.getClientEventHandler().getCts().getUsername()+": "+"String being ForceRemoved has offset: "+offset+" and length: "+length);
            }
            catch (Exception e){
                //////System.err.println("------------------ERROR FORCING REMOVING-----------------"+e.getCause().toString());
            }

        }



     public void sortsPendingInsertsAndRemoves(){
         boolean isStringOfIns = false;
         boolean isRemoveAndOneIns_OrOneRemove = false;


         synchronized(pendingInserts){
             boolean foundDocRemove = false;
             Vector newV = VectorToolkit.getCopy(pendingInserts);
             for(int i=0;i<newV.size();i++){
                 DocChange dc = (DocChange)newV.elementAt(i);
                 if(dc instanceof DocRemove){
                     foundDocRemove =true;
                 }
                 if(Debug.showEnqueuedDocInsertsInCapitals && dc instanceof DocInsert){
                     DocInsert di = (DocInsert)dc;
                     di.str=di.str.toUpperCase();
                 }


             }
             if(!foundDocRemove)isStringOfIns = true;

             if(!isStringOfIns){
                 if(pendingInserts.size()==2){
                     DocChange dc1 = (DocChange)pendingInserts.elementAt(0);
                     DocChange dc2 = (DocChange)pendingInserts.elementAt(1);
                     if(dc1 instanceof DocRemove && dc2 instanceof DocInsert){
                         isRemoveAndOneIns_OrOneRemove=true;
                     }
                 }
                 else if(pendingInserts.size()==1){
                     DocChange dc1 = (DocChange)pendingInserts.elementAt(0);
                     if(dc1 instanceof DocRemove){
                         isRemoveAndOneIns_OrOneRemove=true;
                     }
                 }

             }
             if(!isStringOfIns&&!isRemoveAndOneIns_OrOneRemove){
                 ////Conversation.printWSln("PENDINGINSERTS", Boolean.toString(isStringOfIns)+"--"+Boolean.toString(isRemoveAndOneIns_OrOneRemove));
                 pendingInserts = new Vector();
                 for(int i = newV.size()-1;i>=0;i--){
                     DocChange dc = (DocChange)newV.elementAt(i);
                     if(dc instanceof DocInsert){
                         pendingInserts.insertElementAt(dc, 0);
                     }
                     else{
                         break;
                     }
                 }
             }

         }



         //OK: DocRemove followed by one (not more) inserts
         //OK: String of ONLY DocInserts
         /////All others, take the last one

         try{
         int increment = 0;
         for(int i=0;i<pendingInserts.size();i++){

             DocChange dc = (DocChange)pendingInserts.elementAt(i);
             if(dc instanceof DocInsert){
                 dc.offs=dc.offs+increment;
                 DocInsert di = (DocInsert)dc;
                 increment = increment+di.getStr().length();

             }
             else if(dc instanceof DocRemove){
                 DocRemove dr = (DocRemove)dc;
                 //increment = increment - dr.getLen();
             }
             //////System.err.println("PENDINGINSERTLOOP");
         }
         //////System.err.println("EXITINGPENDINGINSERT");
        }catch (Exception e){
            e.printStackTrace();
        }
     }




        /**
         * Sets the state of the conversational floor
         * @param newState
         * @param msg
         */
    public synchronized void setState(int newState,DocInsert di,long id){
      requestSent = false;
      int oldState = this.state;

      if(newState == this.othertyping){
          jf.jTextPane.getCaret().setDot(super.getLength());
      }

      //Conversation.printWSln("FL",jf.getClientEventHandler().getCts().getUsername()+"CLIENTRECEIVED "+id+" "+newState);
      if(Debug.showEachStateChangeOnclient){
          Conversation.printWSln(jf.getClientEventHandler().getCts().getUsername()+"client","--------------");
          Conversation.printWSln(jf.getClientEventHandler().getCts().getUsername()+"client", "oldState:"+oldState+"  new state:"+newState+" header: "+di.str+" ID:"+id);
          for(int i=0;i<this.pendingInserts.size();i++){
              DocChange dc = (DocChange)pendingInserts.elementAt(i);
              if(dc instanceof DocInsert){
                  DocInsert dih = (DocInsert)dc;
                  Conversation.printWSln(jf.getClientEventHandler().getCts().getUsername()+"client",dih.getStr());
              }
              else{
                  Conversation.printWSln(jf.getClientEventHandler().getCts().getUsername()+"client","DOCREMOVE");
              }
          }
      }

      try{
           //////System.err.println(jf.getClientEventHandler().getCts().getUsername()+": "+"Setting state from "+state+" to "+newState);
           if(di!=null&&di.getStr()!=null&&di.getStr().length()>0){
                super.insertString(super.getLength(),di.getStr(),super.getStyle((String)di.getAttrSet()));
           }
           if (newState==nooneelsetyping){
                jf.getClientEventHandler().cBYCStateHasChanged(oldState,newState,id);
                //Conversation.printWSln("FL",jf.getClientEventHandler().getCts().getUsername()+"CLIENTRECEIVED AND SETTING STATE TO NOONE TYPING "+id+" "+newState);

           }
           if (oldState == typingunhindered && newState==othertyping){
                jf.getClientEventHandler().cBYCStateHasChanged(oldState,newState,id);

           }
           if(oldState == nooneelsetyping & newState == othertyping && this.pendingInserts.size()>0){
                //Perform beep if there is a floor clash
                if(this.wtsdss.performBeepOnFloorClash()){
                    Toolkit.getDefaultToolkit().beep();
                }
           }
           else if (oldState==nooneelsetyping&newState==typingunhindered){

                sortsPendingInsertsAndRemoves();
                for(int i=0;i<pendingInserts.size();i++){
                    Object o = (Object)pendingInserts.elementAt(i);
                    if(o instanceof DocInsert){
                       DocInsert docII = (DocInsert)o;
                       docII.a=super.getStyle("ns");
                       Element e = this.getElementForOffset(docII.getOffs()+di.getStr().length());
                       String eString = "error";
                       try{
                         eString = super.getText(e.getStartOffset(), e.getEndOffset()-e.getStartOffset());
                       }catch(Exception e2){
                          eString = "error";
                       }
                       docII.elementStart=e.getStartOffset();
                       docII.elementFinish=e.getEndOffset();
                       docII.elementString=eString;
                       jf.getClientEventHandler().cBYCDocumentHasChangedInsert(docII.getStr(),super.getLength()-docII.getOffs()-di.getStr().length(),(Style)docII.getAttrSet(),docII.elementString,docII.elementStart,docII.elementFinish,super.getLength());
                       super.insertString(docII.getOffs()+di.getStr().length(),docII.getStr(),(Style)docII.getAttrSet());

                       //-----------------+msg.length() because it is added prior to the already enqueud utterances
                    }
                    else if(o instanceof DocRemove){
                       DocRemove docRR = (DocRemove)o;
                       Element e = this.getElementForOffset(docRR.getOffs());
                       String eString = "error";
                       try{
                         eString = super.getText(e.getStartOffset(), e.getEndOffset()-e.getStartOffset());
                       }catch(Exception e2){
                          eString = "error";
                       }
                       docRR.elementStart=e.getStartOffset();
                       docRR.elementFinish=e.getEndOffset();
                       docRR.elementString=eString;
                       //debug jf.getClientEventHandler().cBYCDocumentHasChangedRemove(super.getLength()-docRR.getOffs(),docRR.getLen(),"!"+docRR.elementString,docRR.elementStart,docRR.elementFinish);
                       jf.getClientEventHandler().cBYCDocumentHasChangedRemove(super.getLength()-docRR.getOffs(),docRR.getLen(),docRR.elementString,docRR.elementStart,docRR.elementFinish,super.getLength());
                       super.remove(docRR.getOffs(),docRR.getLen());
                       //-----------------+msg.length() because it is added prior to the already enqueud utterances
                    }
               }
            }


           //////System.err.println("ACTUALLY SETTING STATE");
            }catch (Exception e){
                  this.jf.getClientEventHandler().getCts().sendErrorMessage(e);
            }
            this.state=newState;
            pendingInserts.removeAllElements();
        }



        public synchronized Element getElementForOffset(int offset){
            //////System.err.println("LOOKING FOR OFFSET OF "+offset);
            Element ee = getParagraphElement(offset);
            try{
              String eeString = getText(ee.getStartOffset(),ee.getEndOffset()-ee.getStartOffset() );
            }catch (Exception exc){
               //////System.err.println("ERROR LOOKING FOR OFFSET OF "+offset);
            }

            return ee;
        }


   public synchronized boolean checkOKToEditInsert(int position,boolean isLookingToDelete){
       //if(2<5)return true;
       //if(deletesAllowed == this.deletesENDOFTURNONLY) return true;


       try{
       if(position>=super.getLength()){
           return true;
       }
       Element elem = super.getCharacterElement(position);
       AttributeSet as = elem.getAttributes();
       AttributeSet asr = as.getResolveParent();
       String asrstyle = asr.toString();

       if(asrstyle.startsWith("NamedStyle:h")){
           String s = super.getText(position, 1);
           String sprior = super.getText(position-1, 1);
           if(s.equalsIgnoreCase("\n")){
               Element elemPrior = super.getCharacterElement(position-1);
               AttributeSet asPrior = elemPrior.getAttributes();
               AttributeSet asrPrior = asPrior.getResolveParent();
               String asrstylePrior = asrPrior.toString();
               if(asrstylePrior.startsWith("NamedStyle:h")){
                   if(!isLookingToDelete&&sprior.equalsIgnoreCase("\n")){
                       //Conversation.printWSln("CHECKISOK", "Style is(0): "+asrstyle+"...character is:"+super.getText(position-1, 1));
                       return true;
                   }

                   //Conversation.printWSln("CHECKISOK", "Style is(1): "+asrstyle+"...character is:"+super.getText(position-1, 1));
                   return false;
               }
               //Conversation.printWSln("CHECKISOK", "Style is(2): "+asrstyle+"...character is:"+super.getText(position-1, 1));

               return true;
           }

           //Conversation.printWSln("CHECKISOK", "Style is(3): "+asrstyle+"...character is:"+super.getText(position, 1));
           return false;
       }
       else{
            //Conversation.printWSln("CHECKISOK", "Style is(4):"+asrstyle);
       }
       }catch(Exception e){ }
       return true;
   }


   public synchronized boolean checkOKToEditRemove(int position, int length){
       //if(2<5)return true;
      // if(deletesAllowed == this.deletesENDOFTURNONLY) return true;
       for(int i=position;i<position+length;i++){
           boolean positionIsOK = this.checkOKToEditInsert(i,true);
           if(!positionIsOK){
               return false;
           }
       }
       return true;
   }




        /**
         *
         * Intercepts Swing's attempt to add String to the chat window. If participant has floor this is permitted.
         * If no other participant has the floor, a request is sent to the server to gain the conversational floor.
         * @param offs
         * @param str
         * @param a
         */
    @Override
        public synchronized void insertString(int offs, String str, AttributeSet a){
             Conversation.printWSln("CLIENTDEBUGGING", "Attempting insert" +offs+ "String:"+ str);

            if(deletesAllowed==this.neither){
                if(offs<super.getLength()){
                    jf.jTextPane.getCaret().setDot(super.getLength());
                    Conversation.printWSln("CLIENTDEBUGGING", "BLOCKING insert and moving CARET" +offs+ "String:"+ str);
                    return;
                }
             }
             else if(deletesAllowed==this.deletesENDOFTURNONLY){
                if(offs<super.getLength()) {
                    jf.jTextPane.getCaret().setDot(super.getLength());
                    Conversation.printWSln("CLIENTDEBUGGING", "BLOCKING insert and moving CARET" +offs+ "String:"+ str);
                    return;
                }

             }

            Date d = new Date();
            if(!this.checkOKToEditInsert(offs,false))return ;

            a=super.getStyle("ns");
            System.err.println(jf.getClientEventHandler().getCts().getUsername()+": "+"insertString with offset"+offs+" "+str);
              try{
                if(state==typingunhindered){
                     if(str.endsWith("\n")){
                       //  super.insertString(offs,)
                     }
                     //////System.err.println("IS IN TYPING UNHINDERED STATE SO CAN WRITE IMMEDIATELY "+str);
                     sendInsertInfo(offs,str,a);
                     super.insertString(offs,str,a);
                     if(deletesAllowed==this.deletesENDOFTURNONLY)jf.jTextPane.getCaret().setDot(super.getLength());
                }
                else if(state==nooneelsetyping){
                    Element e = this.getElementForOffset(offs);
                    String eString = super.getText(e.getStartOffset(), e.getEndOffset()-e.getStartOffset());
                    //debug jf.getClientEventHandler().cBYCrequestTypingUnhindered(super.getLength()-offs,str,"~"+eString,e.getStartOffset(),e.getEndOffset());
                    if(!requestSent){
                        jf.getClientEventHandler().cBYCrequestTypingUnhindered(super.getLength()-offs,str,eString,e.getStartOffset(),e.getEndOffset());
                        System.err.print("SENDINGREQUEST");
                    }
                    else{
                        System.err.print("REQUESTHASBEENSENT");
                    }
                    DocInsert docII = new DocInsert (offs,str,a);
                    docII.elementString="*";
                    pendingInserts.addElement(docII);
                    //////System.err.println("ADDING "+str+" to pending inserts");
                    requestSent=true;
                }

              } catch(Exception e){
                  //////System.err.println("BAD LOCATION INSERTING "+str+" "+offs);
                  e.printStackTrace();
              }
             Date d2 = new Date();
             System.err.println("Insert Timer:"+(d2.getTime()-d.getTime()));
        }

        public synchronized void sendInsertInfo(int offs, String str,AttributeSet a){
            Date d = new Date();
            Element e = this.getElementForOffset(offs);


            String eeString="";
            try{

               eeString = getText(e.getStartOffset(),e.getEndOffset()-e.getStartOffset() );
            }catch (Exception exc){
               eeString = "BADLOCATION";
               //////System.err.println("STARTOFFSET: "+e.getStartOffset()+" ENDOFFSET: "+e.getEndOffset()+" SIZE: "+jf.jTextPane.getText().length());
               exc.printStackTrace();
            }
            int length = str.length();
            int documentlength = super.getLength();
            int insrtIndex = documentlength-offs;

            //debug jf.getClientEventHandler().cBYCDocumentHasChangedInsert(str,insrtIndex,(Style)a,"^"+eeString,e.getStartOffset(),e.getEndOffset());
            jf.getClientEventHandler().cBYCDocumentHasChangedInsert(str,insrtIndex,(Style)a,eeString,e.getStartOffset(),e.getEndOffset(),super.getLength());
            Date d2 = new Date();
            System.err.println("Timer INSERTINFO"+str+ (d2.getTime()-d.getTime()));

            if(deletesAllowed==this.deletesENDOFTURNONLY)jf.jTextPane.getCaret().setDot(super.getLength());
        }



        @Override
        public synchronized void replace(int offs,int len,String s,AttributeSet a){
            Conversation.printWSln("CLIENTDEBUGGING", "Attempting replace" +offs+" Length: "+len+" String:"+ s);
            //if(s.equalsIgnoreCase("\n"))return;
            if(deletesAllowed==this.neither){
                if(offs<super.getLength())return;   //Won't let it do any deletes
                if(s==null||s.length()==0)return;   //If string is nothing, then won't let it do anything..
            }
            else if(deletesAllowed==this.deletesENDOFTURNONLY){
                if(offs+len<super.getLength()) {
                    jf.jTextPane.getCaret().setDot(super.getLength());
                    Conversation.printWSln("CLIENTDEBUGGING", "BLOCKING IN REPLACE OFFSET "+offs+" length: "+len+" "+ s);
                    return;
                }

             }

            Date d = new Date();
            if(!checkOKToEditRemove(offs,len))return;
            if(state==typingunhindered){
                    try{
                      super.replace(offs, len, s, a);
                      //////System.out.println("String being REPLACED: "+s+"OFFSET"+offs+" and length: "+len);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
            }
            else if(state==nooneelsetyping){
                   System.err.println("CALLING REPLACE AND NO-ONE ELSE IS TYPING...... "+s);
                   if(len!=0){
                     DocRemove dr = new DocRemove(offs,len);
                     pendingInserts.addElement(dr);
                   }
                   if(s!=null||!s.equals("")){ //this.insertString(offs, s, a);
                     try{
                        this.insertString(offs, s, a);
                        System.err.println("CALLING INSERT STRING...... "+s);
                     }catch (Exception e){e.printStackTrace();}
                   }
            }
             Date d2 = new Date();
             System.err.println("Replace Timer:"+(d2.getTime()-d.getTime()));
        }


        /**
         * Intercepts Swing's attempt to remove text from the chat window. If participant has floor this is permitted.
         * If no other participant has the floor, a request is sent to the server to gain the conversational floor.
         * @param offs
         * @param len
         */
         public synchronized void remove(int offs, int len){
             Conversation.printWSln("CLIENTDEBUGGING", "Attempting remove" +offs+" Length: "+len);
             if(deletesAllowed==this.neither){
                return;
             }

             else if(deletesAllowed==this.deletesENDOFTURNONLY){
                if(offs+len<super.getLength()) {
                    jf.jTextPane.getCaret().setDot(super.getLength());
                     Conversation.printWSln("CLIENTDEBUGGING", "BLOCKING remove" +offs+" Length: "+len);
                    return;
                }


 }
             if(!checkOKToEditRemove(offs,len))return;
             //////System.err.println("CALLING REMOVE");
              try{
                if(state==typingunhindered){
                    sendRemoveInfo(offs, len);
                    super.remove(offs,len);
                    //////System.out.println("String being REMOVED and SENT has offset: "+offs+" and length: "+len);
                }
                 else if(state==nooneelsetyping){
                    Element e = this.getElementForOffset(offs);
                    String eString = super.getText(e.getStartOffset(), e.getEndOffset()-e.getStartOffset());
                    //////System.err.println("ADDING REMOVE TO PENDING INSERTS");
                    //Debug jf.getClientEventHandler().cBYCrequestTypingUnhindered(super.getLength()-offs,"","$"+eString,e.getStartOffset(),e.getEndOffset());
                    if(!requestSent)jf.getClientEventHandler().cBYCrequestTypingUnhindered(super.getLength()-offs,"",eString,e.getStartOffset(),e.getEndOffset());
                    DocRemove docIR = new DocRemove (offs,len);
                    docIR.elementString="&";
                    pendingInserts.addElement(docIR);
                    requestSent=true;
                }

              } catch(Exception e){
                  //////System.err.println("BAD LOCATION INSERTING");
              }
        }

       public void sendRemoveInfo(int offs, int length){

            Element e = this.getElementForOffset(offs);


            String eeString="";
            try{
               eeString = getText(e.getStartOffset(),e.getEndOffset()-e.getStartOffset() );
            }catch (Exception exc){
               eeString = "BADLOCATION";
            }

            int documentlength = super.getLength();
            int removeIndex = documentlength-offs;

            try{

              //////System.err.println("GETTING REMOVEDTEXT  "+" offset"+offs+" LENGTH: "+length);
              //////System.err.println("GETTING REMOVEDTEXT2  OFFSET:"+offs+" LENGTH: "+length);
            }
            catch(Exception eee){}
            ///Debug jf.getClientEventHandler().cBYCDocumentHasChangedRemove(removeIndex, length, "%"+eeString, e.getStartOffset(), e.getEndOffset());
            jf.getClientEventHandler().cBYCDocumentHasChangedRemove(removeIndex, length, eeString, e.getStartOffset(), e.getEndOffset(),super.getLength());
            //////System.err.println("SENDING REMOVEINFO"+offs);
        }



        public void insertUpdate(AbstractDocument.DefaultDocumentEvent chng, AttributeSet attr){
                    super.insertUpdate(chng, attr);

        }
        public  void removeUpdate(AbstractDocument.DefaultDocumentEvent chng){
                    super.removeUpdate(chng);
        }
        public int getState(){
            return this.state;
        }
        public boolean performBeepOnFloorClash(){
        	System.out.println("Checking whether to beep: "+this.wtsdss.performBeepOnFloorClash());

            return this.wtsdss.performBeepOnFloorClash();
        }
    }