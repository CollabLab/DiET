/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.server;

import java.util.Vector;

import diet.server.CbyC.DocChange;
import diet.server.CbyC.DocInsert;
import diet.server.CbyC.DocRemove;

/**
 *
 * @author Greg
 */
public class StringOfDocChangeInsertsCBYC extends StringOfDocChangeInserts{

    private Vector allDocChanges = new Vector();
    
    public StringOfDocChangeInsertsCBYC(Vector v) {
              for(int i=0;i<v.size();i++){
            DocChange dc = (DocChange)v.elementAt(i);
            if(i==0){
                //Conversation.printWSln("PARSED","PriorElement:" +dc.elementString);
            }  
            if(dc instanceof DocInsert){
                 DocInsert docI = (DocInsert)v.elementAt(i);
                if(!this.firstInsertHasBeenEncountered){
                    this.firstInsertHasBeenEncountered=true;
                    firstInsertOffset=docI.getOffs();
                }
                docI = (DocInsert)v.elementAt(i);
                insert(docI);
            }
            else if (dc instanceof DocRemove){
                DocRemove docR = (DocRemove)v.elementAt(i);
                remove(docR);
            }
        }
    
    }

    
    
    
    @Override
    public DocInsert getIns(int i) {
        return super.getIns(i);
    }

    @Override
    public Vector getSequence() {
        return super.getSequence();
    }

    @Override
    public int getSize() {
        return super.getSize();
    }

    @Override
    public String getString() {
        return super.getString();
    }

    @Override
    public Vector getSubSequence(int startIndex, int finishIndex) {
        return super.getSubSequence(startIndex, finishIndex);
    }

    @Override
    public void insert(DocInsert docI) {
        this.allDocChanges.addElement(docI);
        super.insert(docI);
        
    }

    @Override
    public void remove(DocRemove dR) {
        this.allDocChanges.addElement(dR);
        super.remove(dR);
    }

    public long getSendStart(){
        if(this.allDocChanges.size()==0)return -999999;
        DocChange dc = (DocChange)allDocChanges.firstElement();
        return dc.getTimeStampOfSend();
    }
    
    public long getSendFinish(){
        if(this.allDocChanges.size()==0)return -999999999;
        DocChange dc = (DocChange)allDocChanges.lastElement();
        return dc.getTimeStampOfSend();
    }
    
    public String getSendStartAsString(){
        long firstTime = this.getSendStart();
        if(firstTime<0)return "Nothing was sent";
        return ""+firstTime;
    }
    
    public String getSendFinishAsString(){
        long lastTime = this.getSendFinish();
        if(lastTime<0)return "Nothing was sent";
        return ""+lastTime;
    }
    
    
    
}
