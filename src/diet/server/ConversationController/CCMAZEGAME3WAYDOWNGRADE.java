package diet.server.ConversationController;
import java.io.File;
import java.util.Date;
import java.util.Vector;

import diet.message.MessageChatTextFromClient;
import diet.parameters.ExperimentSettings;
import diet.parameters.IntParameter;
import diet.server.Conversation;
import diet.server.DocChangesOutgoingSequenceFIFO;
import diet.server.Participant;
import diet.textmanipulationmodules.AcknowledmentDegrader.Degrader;




public class CCMAZEGAME3WAYDOWNGRADE extends DefaultConversationController{
    
   
   
    private Degrader degrader;
    
    private DocChangesOutgoingSequenceFIFO chOut = new DocChangesOutgoingSequenceFIFO(null,10,new Date().getTime()); //THIS WILL BE RESET ON FIRST RECEIPT OF TURN
    
    IntParameter pCountdownToIntervention;//=new IntParameter("Turns till Next intervention",0,10);
    IntParameter pCountdownToInterventionMax;//=new IntParameter("Turns between interventions",10,20);
    IntParameter parserTimeOut;// = new IntParameter("Parser Timeout",500,1000);
    IntParameter maxLengthOfParseAttempt;// = new IntParameter("Parser Max Length (chars)",70,70);
    IntParameter minDelayBetweenTurns;// = new IntParameter("Min inter-turn delay",250,250);
    IntParameter maxDelayBetweenTurns;// = new IntParameter("Max inter-turn delay",600,600);
    IntParameter avgTypingTimePerChar;// = new IntParameter("Avg typing time per char",20,20);
    
    boolean isProcessingIntervention = false;

        
    public void initialize(Conversation c,ExperimentSettings expSettings){
        super.initialize(c, expSettings);
        pCountdownToIntervention = (IntParameter)expSettings.getParameter("Turns till Next intervention");      pCountdownToInterventionMax=new IntParameter("Turns between interventions",10,20);
        pCountdownToInterventionMax = (IntParameter)expSettings.getParameter("Turns between interventions");
        parserTimeOut = (IntParameter)expSettings.getParameter("Parser Timeout");
        maxLengthOfParseAttempt = (IntParameter)expSettings.getParameter("Parser Max Length (chars)");
        minDelayBetweenTurns = (IntParameter)expSettings.getParameter("Min inter-turn delay");
        maxDelayBetweenTurns = (IntParameter)expSettings.getParameter("Max inter-turn delay");
        avgTypingTimePerChar = (IntParameter)expSettings.getParameter("Avg typing time per char");
        try{
        degrader=new Degrader(System.getProperty("user.dir")+File.separator+"degradings");
        }catch(Exception e)
        {
            System.out.println("Can't load in degrading files");
            e.printStackTrace();
        }
    }
    
    
    
    @Override
    public void processLoop(){
            if(!isProcessingIntervention)c.resetFromIsTypingtoNormalChatAllAllowedParticipants(super.getIsTypingTimeOut());
    }

    //int countdownToIntervention = 0;
    //int countdownToInterventionMax = 0;
    
    public String getDirectorOrMatcher(Participant p)
    {
        if (p.getParticipantID().equalsIgnoreCase("director")||p.getParticipantID().startsWith("direct"))
        {
            return "P";
        }
        else if(p.getParticipantID().equalsIgnoreCase("m1"))
        {
            return "M1";
        }
        else return "M2";
    }
    
    @Override
    public void processChatText(Participant sender, MessageChatTextFromClient mct)
    {
        Vector allOthers = c.getParticipants().getAllOtherParticipants(sender);
        Participant other1=(Participant)allOthers.elementAt(0);
        Participant other2=(Participant)allOthers.elementAt(1);
        
        if (getDirectorOrMatcher(sender).equalsIgnoreCase("P"))
        {
            c.relayTurnToAllOtherParticipants(sender, mct);
        }
        else
        {
            String chatText=mct.getText();
            String degraded=degrader.degrade(chatText);
            if (degraded==null)
            {
                c.relayTurnToAllOtherParticipants(sender, mct);
            }
            else
            {
                if (pCountdownToIntervention.getValue()>0)
                {
                    pCountdownToIntervention.setValue(pCountdownToIntervention.getValue()-1);
                    c.getExperimentSettings().generateParameterEvent(pCountdownToIntervention);
                    c.relayTurnToAllOtherParticipants(sender, mct);
                    
                }
                else
                {
                    
                    if (getDirectorOrMatcher(other1).equalsIgnoreCase("P"))
                    {
                        c.sendArtificialTurnFromApparentOriginToRecipient(sender, other1, chatText);
                        c.sendArtificialTurnFromApparentOriginToRecipient(sender, other2, degraded);
                    
                    
                    }
                    else
                    {
                        c.sendArtificialTurnFromApparentOriginToRecipient(sender, other2, chatText);
                        c.sendArtificialTurnFromApparentOriginToRecipient(sender, other1, degraded);
                    }
                    pCountdownToIntervention.setValue(this.pCountdownToInterventionMax.getValue());
                    c.getExperimentSettings().generateParameterEvent(pCountdownToIntervention);
                    
                }
            }
            
            
        }
      
        
       
    }
    
   
    public void reloadDegFiles(){
       try{
           degrader.reloadFiles();
       }catch (Exception e){
           System.err.println("Error reloading files");
       }
        
    }
    
     public Degrader getDegrader(){
        if(degrader==null){
            System.out.println("DEGRADER");
            System.out.println("IS NULL");
        }
        return degrader;
    }
    
    
    
    
    
    
}
    
    