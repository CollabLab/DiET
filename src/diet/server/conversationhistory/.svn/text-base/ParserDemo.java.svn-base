package diet.server.conversationhistory;


class ParserDemo {
  public static void main(String[] args) {
    

      
   /* LexicalizedParser lp = new LexicalizedParser("englishPCFG.ser.gz");
    lp.setOptionFlags(new String[]{"-maxLength", "80", "-retainTmpSubcategories"});

    String[] sent = { "Take", "something", "further", "further", "beyond", "." };
    Tree parse = (Tree) lp.apply("on where earth is where I want to be is again where and where where again");
    parse.pennPrint();
    System.out.println();

    */
    
    //TreeJPanel tjp = new TreeJPanel();
    //tjp.setTree(parse); 
    //JFrame jf = new JFrame();
    //jf.getContentPane().add(tjp);
    //jf.setVisible(true);
    //jf.pack();
    //jf.validate();
    
/*    Sentence st = parse.taggedYield();
    System.out.println("Sentence "+st.toString());
    //TaggedWord tw = (TaggedWord)st.get(0);
    //System.out.println(tw.tag()+" "+tw.value());
    
    PartOfSpeechLexicon partOfSpeechLex = new PartOfSpeechLexicon();
    for(int i=0;i<st.size();i++){
        TaggedWord t = (TaggedWord)st.get(i);
        System.out.println();
        System.out.println("Searching for "+t.word()+","+t.tag());
        partOfSpeechLex.searchAndUpdate(t);
    }
    
    
    ConversationHistory c = new ConversationHistory("Test Conversation","",null);  
    ConversationUIManager convUIManager = new ConversationUIManager(c,null);
    c.setConversationUIManager(convUIManager);
    
      
    
    Vector v = new Vector();
    v.addElement("Participant2");
    v.addElement("Participant3");
    v.addElement("Participant4");
    
    long l1 = new Date().getTime();
  
    for(int i=0;i<100;i=i+20){
    try{    
    //java.lang.Thread.sleep(3000);
    }catch(Exception e){}
    
    c.saveMessage(i,20,"Participant1","Participname1","Hello I am here",v,false,new Vector(),null,0);
    c.saveMessage(i+1,20,"Participant2","Participant1",i+"And I am also in here",v,false,new Vector(),null,1);
    c.saveMessage(i+2,20,"Participant3","Participant1",i+"Bearing in mind this is supposed to be false",v,false,new Vector(),null,2);
    c.saveMessage(i+3,20,"Participname1","Participname1",i+"This is the third line",v,false,new Vector(),null,3);
    c.saveMessage(i+4,20,"Participname4","Participname2","This is the middle section",v,false,new Vector(),null,4);
    c.saveMessage(i+5,20,"Participname1","Participname1",i+"simply carnivalistic is the third line",v,false,new Vector(),null,5);
    c.saveMessage(i+6,20,"Participname1","Participname2","This and beyond that all third line",v,false,new Vector(),null,6);
/*    c.saveMessage(i+6,20,"Participname1","Participname2","This and beyond that all third line is not where that which aint wot it seemd cos at da end of " +
            "da days is wheres its gonna be wotcha think then me little babes cos is were we was gonna bei in da makeup cos is where me thing" +
            "is de thing cos oi is here when it wos likie that me thing is all where it was wot do ya think when is like that that that thrt ,,,,sdfdsf and more " +
            "of it cos isnt there when it aint and never wever weather fang tooth claw renaissance man and carpet and futh maybe futile maybe handed and maybe" 
           ,v,false,new Vector(),6);
    c.saveMessage(i+7,20,"Participname1","Participname1","This is the third line pinkle pnkl wer ewrwq ewq qewrq qerq ewrewqr qwer q ewrqw " +
            "qwerqw qerew erwerq rerwer werew ewr w rew we  q ewr qr qwer qewrwerewq rerqwe ewqrewq er qewrwq ewqr wqrew wq ewrqw ewq rqwer qer" +
            "qwerweq rreqewr qewrqwer qewr qwer qerweqer qwerew qer weqr qwerqwer qerewrqewr ewrqerqwe ewrqerq eqrewqr erwqrerwqer ewrwerqer ewqrwqe reqwr " +
            "qwerewrewqr ewr qwer qer qwer wqer qewr qwer qewrr qer qewr qewr qwer qwer qwer qer qer qer qwer qewr qewr qewr qwer qewr qewr qewr qer " +
            "qe rqewr qwerq ewrrewqewrq eqrwqer qerqewr ewqr ewqrqwe rewq rewq rewr qwerewr qewr qewr qewr qewrqewrq rqewr qewr qqewr qewr qewr" +
            "qer qwer qer qewrereqwr q",v,false,new Vector(),7); 
    c.saveMessage(i+8,20,"Participname3","Participname4","This is the third line",v,false,new Vector(),null,8);
    c.saveMessage(i+9,20,"Participname1","Participname1","And of course that is what is third line",v,false,new Vector(),null,9);
    c.saveMessage(i+10,20,"Participname2","Participname1","This is the where of line",v,false,new Vector(),null,10);
    c.saveMessage(i+11,20,"Participant5","Participant5","This is the why line",v,false,new Vector(),null,11);
    c.saveMessage(i+12,20,"Participant3","Participname1","This is the third line",v,false,new Vector(),null,12);
    c.saveMessage(i+13,20,"Participant2","Participname2","This is the third line",v,false,new Vector(),null,13);
    c.saveMessage(i+14,20,"Participant1","Participname1",i+"This is the third line",v,false,new Vector(),null,14);
    c.saveMessage(i+15,20,"Participant5","Participname1","And here were other singers standing in the line",v,false,new Vector(),null,15);

    }
       long l2 = new Date().getTime();
    
    
    
    
    
    
    
    
    
    System.out.println("Time of operating is "+(l2-l1));
    
    /*
    
    
    TreebankLanguagePack tlp = new PennTreebankLanguagePack();
    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
    GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
    Collection tdl = gs.typedDependenciesCollapsed();
    System.out.println(tdl);
    System.out.println();

  
    TreePrint tp = new TreePrint("penn,typedDependenciesCollapsed");
    tp.printTree(parse);
    
   */
    
    
  }

}
