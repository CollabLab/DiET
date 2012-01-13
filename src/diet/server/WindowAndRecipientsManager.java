


package diet.server;
import java.util.Random;
import java.util.Vector;




/**
 * This class stores in which window participants see their own text and text of each other. It can also be used
 * to stop participants from receiving text from another, and allows configuring of whether the chat tools are initially enabled
 * when starting the experiment. It is recommended, however, that this be done explicitly in
 * the ConversationController. This class also needs to be overhauled. In particular, the definition of windowtype is unclear.
 * Future implementations will remove this class, replacing it with a simpler window manager that is integrated withing the
 * Participants class.
 * 
 * @author user
 */
public class WindowAndRecipientsManager {
    
    public static final int ONEWINDOWENABLED = 0;
    public static final int ONEWINDOWDISABLED =1;
    public static final int EACHOWNWINDOWATTOPENABLED =2;
    public static final int EACHOWNWINDOWATTOPDISABLED=3;
    public static final int EACHOWNWINDOWRANDOMENABLED =4;
    public static final int EACHOWNWINDOWRANDOMDISABLED=5;
    
    private Conversation c;
    private Participants ps;
    private WindowAndRecipientsManagerPermissions [][] permissionsArray = new WindowAndRecipientsManagerPermissions [30][30];
    private int arraySize;
    //Allow dynamic array
    
    /*
     *
     *                                     participant1 participant2 participant3 participant4 participant5  participants6
     *             participant1SendsTo        0,Bl         1,Bl         2,Bl        3,Bl          4,Bl          (null)
     *             participant2SendsTo        3,Bl         0,Bl         4,Bl        5,Bl          3,Bl          (null)
     *             participant3SendsTo        2,Bl         2,Bl         2,Bl        2,Bl          4,Bl          (null)
     *             participant4SendsTo        5,Bl         6,Bl         7,Bl        8,Bl          4,Bl          (null)
     *             participant5SendsTo        3,Bl         2,Bl         5,Bl        3,Bl          2,Bl          (null)
     *             participant6SendsTo                                                                           0,Bl
     */
    
    
    /**
     * 
     * @param c
     * @param ps
     * @param arraySize Maximum number of participants permitted in experiment (advisable to keep this much higher than number of actual participants)
     * @param arrayType Type of window 
     *        ONEWINDOWENABLED  = a single window. Text entry is enabled (If using WYSIWYG interface make sure it has floor control)
     *        ONEWINDOWDISABLED = a single window. Text entry is disabled (If using WYSIWYG interface make sure it has floor control)
     *        EACHOWNWINDOWATTOPENABLED = multiple windows, with participants own text displayed at top. Text entry is enabled
     *        EACHOWNWINDOWATTOPDISABLED = multiple windows, with participants own text displayed at top. Text entry is disabled
     *        EACHOWNWINDOWRANDOMENABLED = multiple windows, with random assigning of window numbers. Text entry is enabled.
     *        EACHOWNWINDOWRANDOMDISABLED = multiple windows, with random assigning of window numbers. Text entry is disabled.
     * 
     * 
     */
    public WindowAndRecipientsManager(Conversation c, Participants ps, int arraySize, String arrayType) {
        
        this.c = c;
        this.ps=ps;
        this.arraySize = arraySize;
        
        permissionsArray = new WindowAndRecipientsManagerPermissions [arraySize][arraySize];
        
        if(arrayType.equalsIgnoreCase("ONEWINDOWENABLED")){
            setupONEWINDOW(true);
        }
        else if(arrayType.equalsIgnoreCase("ONEWINDOWDISABLED")){
            setupONEWINDOW(false);
        }
        else if(arrayType.equalsIgnoreCase("EACHOWNWINDOWATTOPENABLED")){
            setupEACHOWNWINDOWATTOP(true);
        }
        else if(arrayType.equalsIgnoreCase("EACHOWNWINDOWATTOPDISABLED")){
            setupEACHOWNWINDOWATTOP(false);
        }
        else if(arrayType.equalsIgnoreCase("EACHOWNWINDOWRANDOMENABLED")){
            setupEACHOWNWINDOWRANDOM(true);
        }
        else if(arrayType.equalsIgnoreCase("EACHOWNWINDOWRANDOMDISABLED")){
            setupEACHOWNWINDOWRANDOM(false);
        }
        else{
            System.err.println("PERMISSIONSARRAY MUST BE INSTANTIATED WITH VALID PARAMETER");
            System.err.println("FOR EXAMPLE ONEWINDOWENABLED OR EACHOWNWINDOWENABLED");
            System.err.println("You entered "+arrayType+" "+ONEWINDOWDISABLED);
            System.exit(-147);
        }
        System.out.println("AFTER SETTING UP ARRAY IS------------------------------------------------");
        this.printArrayFull();
    }   
    
    
    
    public WindowAndRecipientsManager(Conversation c, Participants ps, int arraySize, int arrayType) {
        
        this.c = c;
        this.ps=ps;
        this.arraySize = arraySize;
        
        permissionsArray = new WindowAndRecipientsManagerPermissions [arraySize][arraySize];
        
        if(arrayType==ONEWINDOWENABLED){
            setupONEWINDOW(true);
        }
        else if(arrayType==ONEWINDOWDISABLED){
            setupONEWINDOW(false);
        }
        else if(arrayType==EACHOWNWINDOWATTOPENABLED){
            setupEACHOWNWINDOWATTOP(true);
        }
        else if(arrayType==EACHOWNWINDOWATTOPDISABLED){
            setupEACHOWNWINDOWATTOP(false);
        }
        else if(arrayType==EACHOWNWINDOWRANDOMENABLED){
            setupEACHOWNWINDOWRANDOM(true);
        }
        else if(arrayType==EACHOWNWINDOWRANDOMDISABLED){
            setupEACHOWNWINDOWRANDOM(false);
        }
        else{
            System.err.println("PERMISSIONSARRAY MUST BE INSTANTIATED WITH VALID PARAMETER");
            System.err.println("FOR EXAMPLE ONEWINDOWENABLED OR EACHOWNWINDOWENABLED");
            System.err.println("You entered "+arrayType+" "+ONEWINDOWDISABLED);
            System.exit(-148);
        }
        System.out.println("AFTER SETTING UP ARRAY IS------------------------------------------------");
        this.printArrayFull();
    }   
     
    private void setupONEWINDOW(boolean enabled){
        for(int i=0;i<arraySize;i++){
            for(int j=0;j<arraySize;j++){
               WindowAndRecipientsManagerPermissions perm = new WindowAndRecipientsManagerPermissions(enabled,0);
               this.permissionsArray[i][j] = perm;
            }   
        }
        
    }
   
    private void setupEACHOWNWINDOWATTOP(boolean enabled){
        
        for(int i=0;i<arraySize;i++){
             this.fillMatrix(i,0,enabled);
        }
        
    }
     
    private void setupEACHOWNWINDOWRANDOM(boolean enabled){
        Random r = new Random();
        for(int i=0;i<arraySize;i++){
            this.fillMatrix(i,r.nextInt(i+1),enabled);
        }
        
    }
    
    /**
     * Returns all the participants that a particular participant is permitted to send chat text to. This duplicates functionality
     * in ConversationCOntroller and will be simplified in next release.
     * @param p
     * @return
     */
    public Vector getPermittedChatTextRecipients(Participant p){
        //Returns Vector with first element vector of Participants, 2nd element is vector of Participants' names'
        //Default seetting is that all other participants are enabled.
        Vector participants = ps.getAllParticipants(); 
        Vector vRecipients = new Vector();
        Vector vRecipientsEmails = new Vector();
        Vector vRecipientsUsernames = new Vector();
        Vector vRecipientsWindowNumbers = new Vector();
        Vector v = new Vector();
        int pIndex = participants.indexOf(p);
        for(int i=0;i<participants.size();i++){
            //System.err.println("Getting permission for "+p.getUsername());
            Participant p2 = (Participant)participants.elementAt(i);
            if(p!=p2){//Doesn't need to be able to send to self'
                WindowAndRecipientsManagerPermissions perm = this.permissionsArray [pIndex][i];
                if(perm!=null){
                    if(perm.getSendingEnabled()){
                        vRecipients.addElement(p2);
                        vRecipientsEmails.addElement(p2.getParticipantID());
                        vRecipientsUsernames.addElement(p2.getUsername());
                        vRecipientsWindowNumbers.addElement(perm.getWindowNo());
                    }
                }
            }
        }
        v.addElement(vRecipients);
        v.addElement(vRecipientsEmails);
        v.addElement(vRecipientsUsernames);
        v.addElement(vRecipientsWindowNumbers);                
        return v;
     }     
        
    /**
     * Avoid using this method, use ConversationController instead
     * @param p1
     * @param p2
     */
    public void mutuallyBlock(Participant p1,Participant p2){
        Vector participants = ps.getAllParticipants(); 
        WindowAndRecipientsManagerPermissions perm1 = this.permissionsArray [participants.indexOf(p1)][participants.indexOf(p2)];
        WindowAndRecipientsManagerPermissions perm2 = this.permissionsArray [participants.indexOf(p2)][participants.indexOf(p1)];
        perm1.setSendingEnabled(false);
        perm2.setSendingEnabled(false);
    }
   
    /**
     * Avoid using this method, use ConversationController instead
     * @param p1
     * @param p2
     */
    public void mutuallyEnable(Participant p1,Participant p2){
        Vector participants = ps.getAllParticipants(); 
        WindowAndRecipientsManagerPermissions perm1 = this.permissionsArray [participants.indexOf(p1)][participants.indexOf(p2)];
        WindowAndRecipientsManagerPermissions perm2 = this.permissionsArray [participants.indexOf(p2)][participants.indexOf(p1)];
        perm1.setSendingEnabled(true);
        perm2.setSendingEnabled(true);
    }
    
    /**
     * Avoid using this method, use ConversationController instead
     * @param speaker
     * @param receiver
     */
    public void enforceReceivingOnly(Participant speaker, Participant receiver){
        Vector participants = ps.getAllParticipants(); 
        WindowAndRecipientsManagerPermissions perm1 = this.permissionsArray [participants.indexOf(speaker)][participants.indexOf(receiver)];
        WindowAndRecipientsManagerPermissions perm2 = this.permissionsArray [participants.indexOf(receiver)][participants.indexOf(speaker)];
        perm1.setSendingEnabled(true);
        perm2.setSendingEnabled(false);
    }
    
    /**
     * Returns the window number in which a participant sees text from another
     * @param speaker Participant
     * @param recipient Participant
     * @return window number in which recipient sees text from sender
     */
    public int getWindownumberInWhichASendsToB(Participant speaker, Participant recipient){
        Vector participants = ps.getAllParticipants(); 
        int p1idx = participants.indexOf(speaker);
        int p2idx = participants.indexOf(recipient);
        try{
          WindowAndRecipientsManagerPermissions perm = this.getPermission(p1idx,p2idx);
          return perm.getWindowNo();
        }catch (Exception e){
            System.out.println("ERROR LOOKING FOR WINDOW NUMBER IN WHICH PARTICIPANT "+speaker.getUsername()+" sends to "+recipient.getUsername()+" RETURNING DEFAULT VALUE OF 0");
            return 0;
        }
        
    } 
    
    
    private void fillMatrix(int indexOfCurrentUpdate,int ownWindowNumber, boolean sendingReceivingEnabled){
        //Assumes Participant has already been added to the list of participants
        //make backup copy of array
        //
        //if error then change it back
        //
        
        //participants.addElement(p);
        permissionsArray[indexOfCurrentUpdate][indexOfCurrentUpdate]=new WindowAndRecipientsManagerPermissions(sendingReceivingEnabled,ownWindowNumber);  
        
        //Fill in permissions for new entry
        for(int i=0;i<indexOfCurrentUpdate;i++){
            int lowestWindow = this.getLowestFreeWindowNumber(indexOfCurrentUpdate);
            WindowAndRecipientsManagerPermissions permW = new WindowAndRecipientsManagerPermissions(sendingReceivingEnabled,lowestWindow);
            this.permissionsArray [i][indexOfCurrentUpdate]=permW;            
        } 
        
        //printArray(); //This is a method in Deprecated
        
         for(int i=0;i<indexOfCurrentUpdate;i++){
            int lowestWindow = this.getLowestFreeWindowNumber(i);
            WindowAndRecipientsManagerPermissions permW = new WindowAndRecipientsManagerPermissions(sendingReceivingEnabled,lowestWindow);
            this.permissionsArray [indexOfCurrentUpdate][i]=permW;            
        } 
          
        //printArray(); //This is a method in Deprecated
         
    }
    
    
    
    private void addConnectingToAllInTheirNextFreeWindow(int ownWindowNumber, boolean sendingReceivingEnabled){
        //Assumes Participant has already been added to the list of participants
        //make backup copy of array
        //
        //if error then change it back
        //
        Vector participants = ps.getAllParticipants(); 
        //participants.addElement(p);
        permissionsArray[participants.size()-1][participants.size()-1]=new WindowAndRecipientsManagerPermissions(sendingReceivingEnabled,ownWindowNumber);  
        
        //Fill in permissions for new entry
        for(int i=0;i<participants.size()-1;i++){
            int lowestWindow = this.getLowestFreeWindowNumber(participants.size()-1);
            WindowAndRecipientsManagerPermissions permW = new WindowAndRecipientsManagerPermissions(sendingReceivingEnabled,lowestWindow);
            this.permissionsArray [i][participants.size()-1]=permW;            
        } 
        
        //printArray(); //This is a method in Deprecated
        
         for(int i=0;i<participants.size()-1;i++){
            int lowestWindow = this.getLowestFreeWindowNumber(i);
            WindowAndRecipientsManagerPermissions permW = new WindowAndRecipientsManagerPermissions(sendingReceivingEnabled,lowestWindow);
            this.permissionsArray [participants.size()-1][i]=permW;            
        } 
          
       // printArray();  //This is a method in Deprecated
         
    }
    
    
    public void printArrayFull(){
        System.out.println("PRINTING ARRAY--------------------------------------------------------------------------------");
        
        for(int i=0;i<arraySize;i++){
            for(int j=0;j<arraySize;j++){
                WindowAndRecipientsManagerPermissions pm = permissionsArray [i][j];
                if(pm==null){
                    System.out.print("| "+"X" );
                }
                else{
                  if(pm.getSendingEnabled()){
                      System.out.print("| "+this.permissionsArray [i][j].getWindowNo()+" ");
                  }
                  else{
                    System.out.print("| "+this.permissionsArray [i][j].getWindowNo()+"B");
                  }
                }
            }
            System.out.println("");
        }
       System.out.println(""); 
    }
    
    
    public void printArray2(){
        System.out.println("PRINTING ARRAY--------------------------------------------------------------------------------");
        Vector participants = ps.getAllParticipants(); 
        for(int i=0;i<participants.size();i++){
            for(int j=0;j<participants.size();j++){
                WindowAndRecipientsManagerPermissions pm = permissionsArray [i][j];
                if(pm==null){
                    System.out.print("| "+"X" );
                }
                else{
                  if(pm.getSendingEnabled()){
                      System.out.print("| "+this.permissionsArray [i][j].getWindowNo()+" ");
                  }
                  else{
                    System.out.print("| "+this.permissionsArray [i][j].getWindowNo()+"B");
                  }
                }
            }
            System.out.println("");
        }
       System.out.println(""); 
    }
    
    private int getLowestFreeWindowNumber(int column){
        //Vector participants = ps.getAllParticipants(); 
        boolean currentNumberSearchedForFound = true;
        int currentNumberSearchedFor =-1;
        while(currentNumberSearchedForFound){
            currentNumberSearchedFor++;
            currentNumberSearchedForFound = false;
            for(int i=0;i<arraySize;i++){
                WindowAndRecipientsManagerPermissions pw = (WindowAndRecipientsManagerPermissions) this.permissionsArray [i][column];
                if(pw==null){                    
                }
                else{
                   if(pw.getWindowNo()==currentNumberSearchedFor)currentNumberSearchedForFound=true;                     
                }
            }
           
        }
         return currentNumberSearchedFor;
    }
    
    public WindowAndRecipientsManagerPermissions getPermission(int x,int y) throws Exception{
       try{
         return this.permissionsArray[x][y];
       }
       catch (Exception e){
           System.err.println("ARRAY INDEX OUT OF BOUNDS QUERYING PERMISSIONS ARRAY "+x+" "+y+" ------------------------------------------");
           return null;
       }
       
   }

}
