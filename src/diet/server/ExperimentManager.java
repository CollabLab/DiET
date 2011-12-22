package diet.server;

import java.io.File;
import java.util.Vector;

import bsh.Interpreter;
import diet.client.ConnectionToServer;
import diet.debug.Debug;
import diet.parameters.ExperimentSettings;
import diet.parameters.IntParameter;
import diet.parameters.ui.SavedExperimentsAndSettingsFile;
import diet.server.experimentmanager.EMUI;

public class ExperimentManager extends Thread {

	String expDataDirectory = "";
	// Conversation c;// = new Conversation("FRAG");
	Vector vNames = new Vector();
	Vector vAllActiveNames = new Vector();
	ConnectionListener cl;
	EMUI emui;
	ExperimentSettings generalSett;

	GroupAssignment ga = new GroupAssignment();
	public Vector allConversations = new Vector();

	public EMUI getEMUI() {
		return emui;
	}

	boolean newExperiment;

	// public Vector activeRunningExperiments = new Vector();
	// public Vector experimentsWaitingForLogins = new Vector();

	public ExperimentManager(EMUI em, ExperimentSettings generalSett) {
		this.emui = em;
		this.generalSett = generalSett;
		IntParameter portNo = (IntParameter) generalSett.getParameter("Port Number");
		cl = new ConnectionListener(this, portNo.getValue());
		cl.start();
		setDefaultParticipantNames();
	}

	public ExperimentManager(int portNumber, String experimentName,int numberOfClients) {
		cl = new ConnectionListener(this, portNumber);
		cl.start();
		setDefaultParticipantNames();

		String separatorChar = File.separator;
		String realPathname = System.getProperty("user.dir") + separatorChar
				+ "data" + separatorChar + "Interventions" + separatorChar
				+ "Templates" + separatorChar + experimentName;
		System.err.println(":" + realPathname);
		Vector v = SavedExperimentsAndSettingsFile
				.readParameterObjects(new File(realPathname));
		final ExperimentSettings expSett = new ExperimentSettings(v);
		expSett.changeParameterValue("Experiment Data Folder", System
				.getProperty("user.dir")
				+ File.separator
				+ "data"
				+ File.separator
				+ "Saved experimental data");


		this.createAndActivateNewExperiment(expSett);

		for (int i = 0; i < numberOfClients; i++) {
			ConnectionToServer cts = new ConnectionToServer("localhost",
					portNumber);
			cts.start();
		}

	}

	public static void main(String[] args) {
		int portNumber = Integer.parseInt(args[0]);
		int numberOfClients = Integer.parseInt(args[1]);
		String s = "";
		for (int i = 2; i < args.length; i++) {
			if (i != 2)
				s = s + " ";
			s = s + args[i];
		}
		ExperimentManager em = new ExperimentManager(portNumber, s,
				numberOfClients);
	}

        public synchronized boolean isParticipantNameAlreadyUsed(String name){
            return ga.isParticipantNameAlreadyUsed(name);
        }


	public synchronized boolean activateParticipant(ParticipantConnection participConnection, String participantID,String username) {
		// Called by participantconnection during verification of login details
		// And then adds itself to the conversation
		boolean succ = this.ga.assignParticipantToConversation(allConversations, participantID, username, participConnection);
		if(!succ)return false;
                this.vAllActiveNames.addElement(username);
                return true;
	}


        public synchronized boolean reactivateParticipant(ParticipantConnection participConnection, Participant p) {
            return ga.reConnectParticipant(participConnection,p);
        }

	private String nextFreeName() {
		for (int i = 0; i < vNames.size(); i++) {
			String s = (String) vNames.elementAt(i);
			if (!vAllActiveNames.contains(s)) {
			    vAllActiveNames.addElement(s);
                            return s;
			}
		}
		return "";
	}

	public synchronized void startParticipantandJoinConversationDEPRECATED(
			ParticipantConnection participC, String email, String username) {

	}

	public void setDefaultParticipantNames() {
		vNames.addElement("Participant1");
		vNames.addElement("Participant2");
		vNames.addElement("Participant3");
		vNames.addElement("Participant4");
		vNames.addElement("Participant5");
		vNames.addElement("Participant6");
		vNames.addElement("Participant7");
		vNames.addElement("Participant8");
		vNames.addElement("Participant9");
		vNames.addElement("Participant10");
                vNames.addElement("Participant11");
                vNames.addElement("Participant12");
                vNames.addElement("Participant13");
                vNames.addElement("Participant14");
                vNames.addElement("Participant15");
                vNames.addElement("Participant16");
                vNames.addElement("Participant17");
                vNames.addElement("Participant18");
                vNames.addElement("Participant19");
                 vNames.addElement("Participant21");
                  vNames.addElement("Participant22");
                   vNames.addElement("Participant23");
                    vNames.addElement("Participant24");
                     vNames.addElement("Participant25");
 vNames.addElement("Participant26");
 vNames.addElement("Participant27");
  vNames.addElement("Participant28");
   vNames.addElement("Participant29");
    vNames.addElement("Participant30");
     vNames.addElement("Participant31");
      vNames.addElement("Participant32");
	}

	public synchronized Object findPrevioususernameOrParticipantFromPreviousLoginsAndCheckParticipantIDISOK(String participantID) {
		//Returns a Participant (reactivating a prior login)
                //          String (suggested name of participant)
                //          Boolean (whether true or false, indicates invalid login)
                boolean isParticipantIDOK = ga.isParticipantIDOK(allConversations, participantID); 
                if(!isParticipantIDOK){
                    return false;
                }//else{System.exit(-5);}
                Participant p = ga.findUsernameForParticipantID(participantID);
                if(Debug.serverProvidesParticipantName||Debug.doAUTOLOGIN) {
                    return nextFreeName();
                }
                if(p!=null)return p;
                return "";
	}



	public void createAndActivateNewExperiment(ExperimentSettings expSett) {
		vAllActiveNames.removeAllElements();
		Conversation c = new Conversation(this, expSett);
		c.start();
		allConversations.addElement(c);
	}

	public void createAndActivateNewExperiment(Interpreter i) {
		Conversation c = new Conversation(this, i);
		c.start();
		allConversations.addElement(c);
	}

	public void connectUIWithExperimentManager(Conversation c,
			ConversationUIManager convUI) {
		if (emui != null) {
			this.emui.displayConversationUI(c, convUI);
		}
	}

	public ExperimentSettings getGeneralSetttings() {
		return this.generalSett;
	}

	public int getMinParticipantIDLength() {
		IntParameter minL = (IntParameter) generalSett
				.getParameter("Min ParticipantID Size");
		if (minL == null) {
			// Conversation.printWSln("main",
			// "Could not find parameter for minimum participant ID length");
			return 4;
		}

		return minL.getValue();
	}

}
