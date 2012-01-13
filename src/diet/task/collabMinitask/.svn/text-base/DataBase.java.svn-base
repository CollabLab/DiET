/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package diet.task.collabMinitask;

import diet.debug.Debug;
import diet.server.Conversation;
import diet.server.Participant;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 *
 * @author sre
 */
public class DataBase {

    Random r = new Random();

    Vector moonObjects = new Vector();
   
    static Vector alphabetsFIRST = new Vector();
    static Vector alphabetsSECOND = new Vector();

    static Hashtable participantsAlphabets = new Hashtable();

    boolean secondStage = false;

    static{
        initializeAlphabets();
        if(5>2&&Debug.debugGROOP){
            for(int i=0;i<alphabetsFIRST.size();i++){
                Vector v  = (Vector)alphabetsFIRST.elementAt(i);
                Vector v2 = DataBase.sortVectorAlphabetically(v);
                //System.err.println("------------------------------------------------------------");
                for(int j=0;j<v2.size();j++){
                    String s =(String)v2.elementAt(j);
                    System.err.println(s);
                }
            }
        }
    }

    public DataBase(boolean setSecondStage){
        Vector allObjects = new Vector();
        String[] s1 = {"two 100lbs tanks of OXYGEN",                  "OXYGEN",       "O",  "1"};   allObjects.addElement(s1);
        String[] s2 = {"5 gallons of WATER",                          "WATER",        "W",  "2"};   allObjects.addElement(s2);
        String[] s3 = {"stellar MAP",                                 "MAP",          "M",  "3"};   allObjects.addElement(s3);
        String[] s4 = {"FOOD concentrate",                            "FOOD",         "F",  "4"};   allObjects.addElement(s4);
        String[] s5 = {"solar powered RADIO transmitter and receiver","RADIO",        "R",  "5"};   allObjects.addElement(s5);
        String[] s6 = {"first aid KIT",                               "KIT",          "K",  "6"};   allObjects.addElement(s6);
        String[] s7 = {"PARACHUTE",                                   "PARACHUTE",    "P",  "7"};   allObjects.addElement(s7);
        String[] s8 = {"SIGNAL flares",                               "SIGNAL",       "S",  "8"};   allObjects.addElement(s8);
        String[] s9 = {"two .45 caliber GUNS",                        "GUNS",         "G",  "9"};   allObjects.addElement(s9);
        String[] s10 = {"portable HEATER",                            "HEATER",       "H",  "10"};  allObjects.addElement(s10);
        String[] s11 = {"magnetic COMPASS",                           "COMPASS",      "C",  "11"};  allObjects.addElement(s11);
        String[] s12 = {"cigarette LIGHTER",                          "LIGHTER",      "L",  "12"};  allObjects.addElement(s12);
        moonObjects = allObjects;
        this.secondStage = setSecondStage;
        

        //Vector v = new Vector();
        //Vector v2= new Vector();

    }

     ///CARPET, $CAR, $PET, 2CAR, CAR, CARNIVAL
     //B]ACK, BACK, BANGLE, BB]B,BEE, BBBBB
     //ANONYMOUS, AN@ENEMY, ANEMONE, ANOTHER
     //QUICKER, QUICKEN, QUICK)SAND, QUIKC, QUIKCLY
     //IAMBIC, IAMYESIAM, IAM_IAM, IAMYES
     //NOTING, NOTHHING, NOTEVEN, KNOW
     //

    static private void initializeAlphabets(){
        Vector vSorted;
        List l;
        if(true){
           String[] alphabet1 = {"AAAA","BBB","CCCC","DD","E","FF","GGGGGG","GG","HHHH","THIS","THEN", "THAT", "BETTER", "DONATE", "HAIR"};                                  l= Arrays.asList(alphabet1); vSorted = new Vector(l); alphabetsFIRST.addElement(vSorted);
           String[] alphabet2 = {"QQQ","WWWW","EEE","R","TT","YY","UUUU","NOT","PPPP!","LLL","THAT", "THATONE", "WORSE", "NOWHEY", "NOWAY"};                              l= Arrays.asList(alphabet2); vSorted = new Vector(l); alphabetsFIRST.addElement(vSorted);
           String[] alphabet3 = {"NNNNN","OOOO","LLL","PP","QQ","QQQQ","RANGLE","RANF","RANKEDIN","THEN", "THAT", "FATTEST", "TIME", "ERASE"};                        l= Arrays.asList(alphabet3); vSorted = new Vector(l); alphabetsFIRST.addElement(vSorted);
           String[] alphabet4 = {"SOMEHAIR","SOME","SOMEMORE","SOMEWHERE","SOMEHOW","SOMESOME","SOWHAT","HOWSO", "SOMESUCH", "WHATABOUT"};          l= Arrays.asList(alphabet4); vSorted = new Vector(l); alphabetsFIRST.addElement(vSorted);
           String[] alphabet5= {"IAMBIC","IAMYESIAM","IAMYES","YESIAMYES","IMIM","BETWEEN","FLUFFY","IMPOSED","TRAVEL", "THRIVE", "RENTER"};   l= Arrays.asList(alphabet5); vSorted = new Vector(l); alphabetsFIRST.addElement(vSorted);
           String[] alphabet6= {"CARPET","CAR","PET","CARS2","CAR","CARNIVAL","IMPOSING","SOFTHOSE","MESHING", "CAVE", "MOVIE","CODE", "MONEY"};           l= Arrays.asList(alphabet6); vSorted = new Vector(l); alphabetsFIRST.addElement(vSorted);
           String[] alphabet12= {"WHATWHEN","WHERENOT","BAE","BEA","BEANHURRY","BEEN","BEER","BEEN","WHEREFORE","SAUSAGE", "HORRID", "EQUITY" };                                 l= Arrays.asList(alphabet12); vSorted = new Vector(l); alphabetsFIRST.addElement(vSorted);
           String[] alphabet13= {"WOULD","NOWHERE","WHERE","WHY","WHEN","THERE","WERE","WENT","WOULD","WEARE", "BACON", "JUICE", "WATCH","RULES"};                                 l= Arrays.asList(alphabet13); vSorted = new Vector(l); alphabetsFIRST.addElement(vSorted);
           String[] alphabet14= {"COLOUR","COLLAPSE","COLUMN","VERTICAL","LEFT","RIGHT","WRITE","WIELD","WORTHWHILE", "TECH", "WORSE", "UPCOME"};                                 l= Arrays.asList(alphabet14); vSorted = new Vector(l); alphabetsFIRST.addElement(vSorted);
           String[] alphabet15= {"WERE","WENT","WHEN","WING","WAFT","SOUND","SERVICE","SMILE","SERVE", "OHH", "HOHO", "GRAPPLE", "DOYOU", "AREAS"};                                 l= Arrays.asList(alphabet15); vSorted = new Vector(l); alphabetsFIRST.addElement(vSorted);
           String[] alphabet16= {"ASSOC","SOCCER","SERF","SEE","QUIK","QUIKCLY","HOLDINGSIDE","HELDIN", "BREATHE", "DEVELOP","CAPACITY", "THINNER"};                               l= Arrays.asList(alphabet16); vSorted = new Vector(l); alphabetsFIRST.addElement(vSorted);
           String[] alphabet17= {"FASTER","FASTEST","ALERT","ALERTED","QUIK","QUIKCLY","HOLDINGSIDE","HELDIN", "BRAIN", "MULTI", "AWARE", "THINKSO"};                              l= Arrays.asList(alphabet17); vSorted = new Vector(l); alphabetsFIRST.addElement(vSorted);
           String[] alphabet18= {"MOREOVER","MOROSE","ALWAYSHAPPY","MUCUS","LOVE","HERE","HERETIC","HEAVY", "MUCK", "MAYBE","LIKE", "OFFER", "HANDSOFF"};                              l= Arrays.asList(alphabet18); vSorted = new Vector(l); alphabetsFIRST.addElement(vSorted);
           String[] alphabet19= {"NELLYPHANT","DOMINAL","LIZARD","SCRATCHING","ARGUING","INCLII","INTEL","CAMOUF", "MOREOF", "BACKG", "COAT", "WATER"};                              l= Arrays.asList(alphabet19); vSorted = new Vector(l); alphabetsFIRST.addElement(vSorted);



        }
        if(true){
          String[] alphabet7= {"AAAAAA","BANGLE","BB)EE","BB)BBBB","BE)B","BEE)P","A)E","BLAUGH","NEVER","SOMETHING", "CARE", "HOSPITAL"};                      l= Arrays.asList(alphabet7); vSorted = new Vector(l); alphabetsSECOND.addElement(vSorted);
          String[] alphabet8= {"NOTING","N)OTHING","NOTEVEN","KNOWNOW","NOWGO","NOT","WRYYFUL","PLAYFUL","N))VERSOME", "BASICALLY", "BULLET", "S)PER","SOUP"};                                          l= Arrays.asList(alphabet8); vSorted = new Vector(l); alphabetsSECOND.addElement(vSorted);
          String[] alphabet9= {"WELL","MONEY","CASH","DOSH","WIELD","WELDED","WESID","WELLSAID","WELLINSIDE", "THEY", "THAT", "THEN", "WELCOME", "RESULT"};                                 l= Arrays.asList(alphabet9); vSorted = new Vector(l); alphabetsSECOND.addElement(vSorted);
          String[] alphabet10= {"FIRST","SECOND","THIRD","FOURTH","FIFTH","SIXTH","SEVENTH","EIHTH","NINTH", "TENTH", "ELEVENTH","TWELFTH", "THIRTH", "F)ORT"};                                 l= Arrays.asList(alphabet10); vSorted = new Vector(l); alphabetsSECOND.addElement(vSorted);
          String[] alphabet11= {"ONE","TWO","THREE","F)R","F)VE","SIX","S)VEN","EIGHT","NINE","TEN", "ELEVEN", "TWELVE", "THIRT","FOURT"};                                 l= Arrays.asList(alphabet11); vSorted = new Vector(l); alphabetsSECOND.addElement(vSorted);
          String[] alphabet20= {"AAAA","BBBB","CCCC","DDDD","EEEE","FFFF","GGGG","HHHH","IIII","JJJJ","KKKK","LLLL","MMMM","NNNN"};                                 l= Arrays.asList(alphabet20); vSorted = new Vector(l); alphabetsSECOND.addElement(vSorted);

        }
    }



    static private void checkParticipantHasAlphabet(Participant p){
        Vector v = (Vector)DataBase.participantsAlphabets.get(p);
        if(v==null)v = new Vector();
        DataBase.participantsAlphabets.put(p, v);
    }

    public Vector getRandomAlphabetTask(Participant pA, Participant pB){
        if(Debug.debugGROOP)System.err.println("GROOPENTERING");


        System.err.println("ALPH_LOOKING ATA ");
        checkParticipantHasAlphabet(pA);checkParticipantHasAlphabet(pB);
        Vector vAlphabetsCopy;
        if(!secondStage){
            vAlphabetsCopy = (Vector)alphabetsFIRST.clone();
        }else{
            vAlphabetsCopy=(Vector)alphabetsSECOND.clone();
        }
        boolean hasFoundNovelAlphabet=false;
        System.err.println("ALPH_LOOKING ATB "+vAlphabetsCopy.size());
        while(vAlphabetsCopy.size()>0){
            Vector v= (Vector)vAlphabetsCopy.elementAt(r.nextInt(vAlphabetsCopy.size()));
            System.err.println("ALPH_LOOKING AT "+v.size());
            Vector p1sAlphabets = (Vector)DataBase.participantsAlphabets.get(pA);
            Vector p2sAlphabets = (Vector)DataBase.participantsAlphabets.get(pB);
            if(   ( !p1sAlphabets.contains(v) )&& (!p2sAlphabets.contains(v))    ){
                p1sAlphabets.addElement(v);
                p2sAlphabets.addElement(v);
                System.err.println("ALPH_RETURNING "+v.size());
                if(Debug.debugGROOP)System.err.println("GROOPEXITING1");
                return v;
            }
            vAlphabetsCopy.removeElement(v);
        }
        System.err.println("HAD TO REUSE THE VECTOR");
        Conversation.printWSln("Main", "HAD TO REUSE THE VECTOR");
        
        if(!secondStage){
             if(Debug.debugGROOP)System.err.println("GROOPEXITING2");
            return (Vector)alphabetsFIRST.elementAt(r.nextInt(alphabetsFIRST.size()));
        }else{
             if(Debug.debugGROOP)System.err.println("GROOPEXITING3");
            return (Vector)alphabetsSECOND.elementAt(r.nextInt(alphabetsSECOND.size()));
        }
    }
    




    public boolean isASublistOfB(Vector a,Vector b){
        for(int i=0;i<a.size();i++){
            Object oa=a.elementAt(i);
            Object ob=b.elementAt(i);
            if(oa!=ob)return false;
        }
        return true;
    }




    public Object getObjectWithName(String s,Vector objectsToSearchThrough){
        System.err.println("SEARCHING FOR "+s);
        for(int i=0;i<objectsToSearchThrough.size();i++){
             Object o = objectsToSearchThrough.elementAt(i);
             
             if(o instanceof String){
                 String s2 = (String)o;
                 if(s2.equalsIgnoreCase(s))return s2;
                 System.err.println("SEARCHING FORFOUND "+s2);
             }
            else if(o instanceof String[]){
                System.err.println("SEARCHING FORFOUNDARRAY");
                String[] arr = (String[])objectsToSearchThrough.elementAt(i);
                if(arr[0].equalsIgnoreCase(s)) return arr;
                if(arr[1].equalsIgnoreCase(s)) return arr;
                if(arr[2].equalsIgnoreCase(s)) return arr;
            }
        }
        return null;
    }

    public int getRankingDeprecated(String name,Vector allObjectsSearchedThrough ){
        int score = -9;
        for(int i=0;i<allObjectsSearchedThrough.size();i++){
             String[] arr = (String[])allObjectsSearchedThrough.elementAt(i);
             if(arr[0].equalsIgnoreCase(name)) score = i;
             if(arr[1].equalsIgnoreCase(name)) score = i;
             if(arr[2].equalsIgnoreCase(name)) score = i;
        }
        return score;
    }


    public Vector getJumbled(Vector v2){
        Vector v = (Vector)v2.clone();
        Random r = new Random();
        Vector jumbled = new Vector();
        while(v.size()>0){
            Object o = (Object)v.elementAt(r.nextInt(v.size()));
            jumbled.addElement(o);
            v.remove(o);
        }
        return jumbled;
    }


     public Vector getSortedVector(Vector v){
         Vector output = new Vector();
         Vector v2 = (Vector)v.clone();
         while(v2.size()>0){
             Object o = getMinimum(v2);
             output.addElement(o);
             v2.removeElement(o);
         }
         return output;
     }
     private String[] getMinimum(Vector v){
         String [] minSoFar = (String[])v.elementAt(0);
         for(int i=1;i<v.size();i++){
             String[] ss = (String[])v.elementAt(i);
             int ssVal = Integer.parseInt(ss[3]);
             if(ssVal<Integer.parseInt(minSoFar[3]))minSoFar=ss;
         }
         return minSoFar;
     }


     public static Vector sortVectorAlphabetically(Vector v){
       try{
         String[] a = new String[v.size()];
         for(int i=0;i<v.size();i++){
             a[i]=(String)v.elementAt(i);
         }
          String[] aSorted = sortStringBubble(a);
          List l= Arrays.asList(aSorted);
          Vector vSorted = new Vector(l);
          return vSorted;
        }catch (Exception e){
            e.printStackTrace();
        }
        return new Vector();
     }





     private static String[] sortStringBubble( String  x [ ] )
      {
            int j;
            boolean flag = true;  // will determine when the sort is finished
            String temp;
            while ( flag )
            {
                  flag = false;
                  for ( j = 0;  j < x.length - 1;  j++ )
                  {
                          if ( x [ j ].compareToIgnoreCase( x [ j+1 ] ) > 0 )
                          {                                             // ascending sort
                                      temp = x [ j ];
                                      x [ j ] = x [ j+1];     // swapping
                                      x [ j+1] = temp;
                                      flag = true;
                           }
                   }
            }
             return x;
      }

}



