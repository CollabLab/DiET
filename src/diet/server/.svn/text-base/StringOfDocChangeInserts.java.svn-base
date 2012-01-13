/*
 * StringOfDocChangeInserts.java
 *
 * Created on 26 October 2007, 23:11
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package diet.server;

import java.io.Serializable;
import java.util.Vector;

import diet.server.CbyC.DocChange;
import diet.server.CbyC.DocInsert;
import diet.server.CbyC.DocRemove;
import diet.utils.VectorToolkit;

/**
 * This class is still under construction. It was originally developed for a
 * demo, and needs radical overhaul. If attempting to write a WYSIWYG
 * intervention, it is advisable to look at ConversationControllerINTERCEPTUHH
 * and copy the relevant fragments. This class is only used by WYSIWYG
 * implementations.
 * 
 * @author user
 */
public class StringOfDocChangeInserts implements Serializable {

	Vector sequence2 = new Vector();
	boolean firstInsertHasBeenEncountered = false;
	int firstInsertOffset = 0;

	boolean errorInserting = false;
	boolean errorRemoving = false;

	public StringOfDocChangeInserts() {

	}

	public StringOfDocChangeInserts(Vector v) {
		// Conversation.printWSln("INSERTERROR","NEWNEWNEWNEWNEWNEWNEW");
		for (int i = 0; i < v.size(); i++) {
			DocChange dc = (DocChange) v.elementAt(i);
			if (i == 0) {
				// Conversation.printWSln("PARSED","PriorElement:"
				// +dc.elementString);
			}
			if (dc instanceof DocInsert) {
				DocInsert docI = (DocInsert) v.elementAt(i);
				if (!this.firstInsertHasBeenEncountered) {
					this.firstInsertHasBeenEncountered = true;
					firstInsertOffset = docI.getOffs();
				}
				docI = (DocInsert) v.elementAt(i);
				insert(docI);
			} else if (dc instanceof DocRemove) {
				DocRemove docR = (DocRemove) v.elementAt(i);
				remove(docR);
			}
		}
	}

	public void insert(DocInsert docI) {
		try {
			int offsetFrmLeft = firstInsertOffset + sequence2.size()
					- docI.getOffs();
			if (offsetFrmLeft < 0) {
				// Conversation.printWSln("InsertERROR","afirstInsertOffset"+firstInsertOffset+"SEQUENCESIZE:"+sequence2.size()+" STRING:"+docI.str+" DOCI.OFFSET:"+docI.offs+" OffsetFrmLeftPriorToSetting"+offsetFrmLeft+"elementStart:"+docI.elementStart+"  elementFinish:"+docI.elementFinish);
				offsetFrmLeft = 0;
				errorInserting = true;

			}
			if (offsetFrmLeft > sequence2.size()) {
				offsetFrmLeft = sequence2.size();
				// Conversation.printWSln("InsertERROR","bfirstInsertOffset"+firstInsertOffset+"SEQUENCESIZE:"+sequence2.size()+" STRING:"+docI.str+" DOCI.OFFSET:"+docI.offs+" OffsetFrmLeftPriorToSetting"+offsetFrmLeft+"elementStart:"+docI.elementStart+"  elementFinish:"+docI.elementFinish);
				errorInserting = true;
			}
			if (!errorInserting) {
				// Conversation.printWSln("InsertERROR","NORMAL:"+firstInsertOffset+"SEQUENCESIZE:"+sequence2.size()+" STRING:"+docI.str+" DOCI.OFFSET:"+docI.offs+" OffsetFrmLeftPriorToSetting"+offsetFrmLeft+"elementStart:"+docI.elementStart+"  elementFinish:"+docI.elementFinish);

			}
			sequence2.insertElementAt(docI, offsetFrmLeft);

		} catch (Exception e) {
			System.err.println("Error3 in StringOfDocChangeInserts");
			errorInserting = true;
		}

	}

	public void remove(DocRemove dR) {
		// Conversation.printWSln("PARSED",
		// "Going to remove from right"+dR.offs+" Length: "+dR.len);
		// Conversation.printWSln("SDSIMPLE","(SDIBEGINREMOVE)");
		try {
			int offsetFrmLeft = firstInsertOffset + (sequence2.size())
					- (dR.getOffs());
			if (offsetFrmLeft > sequence2.size() - 1) {
				// Conversation.printWSln("PARSED","(SDIRESETREMOVEOFFSET1)");
				offsetFrmLeft = sequence2.size() - 1;
				errorRemoving = true;
			}
			if (offsetFrmLeft < 0) {
				// Conversation.printWSln("PARSED","(SDIRESETREMOVEOFFSET2)");
				offsetFrmLeft = 0;
				errorRemoving = true;
			}
			int len = dR.getLen();
			if (offsetFrmLeft + len > sequence2.size()) {
				// Conversation.printWSln("PARSED","(SDIRESETREMOVEOFFSET3)");
				len = (sequence2.size() - 1) - offsetFrmLeft;
				errorRemoving = true;
			}
			Vector sublistToRemove = VectorToolkit.sublist(sequence2,
					offsetFrmLeft, offsetFrmLeft + len);
			// Conversation.printWSln("PARSED",
			// "Actually removing offset from left"+offsetFrmLeft+" Length: "+len);
			sequence2.removeAll(sublistToRemove);
			// sequence.replace(offsetFrmLeft,offsetFrmLeft+remove.getLen(),"");
		} catch (Exception e) {
			// Conversation.printWSln("SDSIMPLE","Error in DocumentChangeSequenceOffsetFromRight Removing offsetFrmRight3: "+dR.getOffs()+".."+dR.getLen()+".."+this.sequence2.size());
			e.printStackTrace();
			errorRemoving = true;
		}
		// Conversation.printWSln("SDSIMPLE","(ENDREMOVE)");

	}

	public String getString() {
		String s = "";
		for (int i = 0; i < sequence2.size(); i++) {
			DocInsert s2 = (DocInsert) sequence2.elementAt(i);
			s = s + s2.getStr();
		}
		String errorMessage = "";
		if (errorInserting && !this.errorRemoving)
			errorMessage = "(Verify:I)";
		if (!errorInserting && this.errorRemoving)
			errorMessage = "(Veri:R)";
		if (errorInserting && errorRemoving)
			errorMessage = "(Verify:IR)";
		return errorMessage + s;
	}

	static public String getSubSequenceString(Vector v) {
		String s = "";
		for (int i = 0; i < v.size(); i++) {
			DocInsert s2 = (DocInsert) v.elementAt(i);
			s = s + s2.getStr();
		}
		return s;
	}

	static public Vector getInsEquivalentOfString(String s) {
		Vector v = new Vector();
		for (int i = 0; i < s.length(); i++) {
			DocInsert dI = new DocInsert(0, "" + s.charAt(i), null);
			v.addElement(dI);
		}
		return v;
	}

	public int getSize() {
		return sequence2.size();
	}

	public DocInsert getIns(int i) {
		return (DocInsert) sequence2.elementAt(i);
	}

	public Vector getSequence() {
		return sequence2;
	}

	public Vector getSubSequence(int startIndex, int finishIndex) {
		Vector v = new Vector();
		for (int i = startIndex; i < finishIndex && i < sequence2.size(); i++) {
			Object o = sequence2.elementAt(i);
			v.addElement(o);
		}
		return v;
	}

}
