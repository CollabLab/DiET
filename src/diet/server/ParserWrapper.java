package diet.server;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.Filter;


/**
 * Wrapping class for the Stanford Parser. If another parser is to be used this class can be adapted
 * @author user
 */
public class ParserWrapper {
    
    LexicalizedParser lp;
    //String parserFileLocation = "C:\\DiET\\Libraries\\stanford-parser-2007-08-19\\englishPCFG.ser.gz";
    String parserFileLocation;
    private boolean isResetting = false;
    
   
    
    public static void main(String args[])
    {
        ParserWrapper pw=new ParserWrapper(System.getProperty("user.dir")+File.separator+"utils"+File.separator+"englishPCFG.ser.gz");
        Vector v=pw.parseTextTimeOut("this is a goat with a pointy tail", 1000);
        Tree t=(Tree)v.elementAt(1);
        t.pennPrint();
    }
    /* Creates a new instance of ParserWrapper 
     * @param parserFileLocation the full pathname of the location of the parser (e.g. c:\...\englishPCFG.ser.gz)
     * 
     */
    public ParserWrapper(String parserFileLocation) {
        //lp = new LexicalizedParser("englishPCFG.ser.gz");
        this.parserFileLocation = parserFileLocation;  
        lp = new LexicalizedParser(parserFileLocation);
        lp.setOptionFlags(new String[]{"-maxLength", "100", "-retainTmpSubcategories"});
        
        
    }
    
    
    
    /**
     * Attempts a parse of the text. If the parser takes longer than a specified threshold, the parse
     * attempt is interrupted.
     * @param sent
     * @param timeout threshold in milliseconds
     * @return null | Vector containing parts of speech and parse tree
     */
    public Vector parseTextTimeOut(String sent, long timeout){
        Vector v = new Vector();
        long startTime=new Date().getTime();
        System.out.println("Trying to parse |"+sent+"|"+" with timeout "+timeout);
        if(this.isResetting){
            v.addElement(new Vector());
            Tree t2 = null;
            v.addElement(t2);
            System.out.println("Parser Bypassing: Is resetting");
            return v;
        }
        ParserJob pj = new ParserJob(lp,sent);
        pj.start();
        
        while(!pj.hasParsed&& startTime+timeout>=new Date().getTime()){
            try{ 
            }catch(Exception e){        
            }
        }
        if(pj.hasParsed){
            return pj.parsedOutput;
        }
        else{
            this.isResetting=true;
            ParserReset pr = new ParserReset(this,lp);
            pr.start();
            v.addElement(new Vector());
            Tree t2 = null;
            v.addElement(t2);
            System.out.println("Parser Timed out and Is resetting for: "+sent);
        }
        
        return v;
        
    }
    
    /**
     * Attempts a reset of the parser. This should be avoided as it is computationally expensive
     */
    public class ParserReset extends Thread{
        LexicalizedParser lp;
        ParserWrapper pw;
        
        public ParserReset(ParserWrapper pw, LexicalizedParser lp){
            this.pw=pw;
            this.lp=lp;
        }
        public void run(){
            try{
               lp.reset();
            }catch (Throwable t){
                lp = new LexicalizedParser(parserFileLocation);
                lp.setOptionFlags(new String[]{"-maxLength", "35", "-retainTmpSubcategories"});
                System.out.println("Parser: Had to create new instance");
            }   
            pw.isResetting=false;
        }
    }
    
    public class ParserJob extends Thread{
        LexicalizedParser lp;
        String sent;
        Vector parsedOutput = new Vector();
        boolean hasParsed = false;
        
        
        public ParserJob(LexicalizedParser lp,String sent){
            this.lp =lp;
            this.sent=sent;
        }
        public void run(){
            try{
              Vector parsedWords = new Vector();
              lp.parse(sent);
              Tree parse = lp.getBestParse();
              ArrayList st = parse.taggedYield();
              System.out.println("Parsing sentence "+st.toString());
              for(int i=0;i<st.size();i++){
                 TaggedWord tw = (TaggedWord)st.get(i);
                 parsedWords.addElement(tw);
              }
              parsedOutput.addElement(parsedWords);
              parsedOutput.addElement(parse);
              hasParsed=true;
            }catch (Exception e){
                e.printStackTrace();
                
            }  
        }
    }
    
   
    
  
    
    /**
     * Attempts parse of text with timeout threshold of 500ms
     * @param sent String to be parsed
     * @return Vector containing tagged text and parse tree
     */
    public Vector parseText(String sent){
        return this.parseTextTimeOut(sent,500);
    }
    
   
    
    
    /**
     * Returns the flattened String of a parse tree
     * @param t Tree to be flattened
     * @return flattened tree
     */
    private String getString(Tree t){
        if(t==null)return "";
        
        ArrayList st = t.taggedYield();
        System.out.println("Parsing sentence "+st.toString());
        String treeString ="";
        // System.out.println("Size of parse is "+st.length());
         for(int i=0;i<st.size();i++){
             TaggedWord tw = (TaggedWord)st.get(i);
             if(i!=0){
                 treeString = treeString + " "+tw.word();
             }
             else{
                 treeString = treeString+ tw.word();
             }
            // System.out.println(tw.word()+","+tw.tag());
         }
         
         return treeString;  
    }
    
    /**
     * Attempts to identify an SBAR in the parse tree and split at the boundary.
     * This method is buggy...needs to be rewritten
     * @param tOther
     * @return Vector containing first and second half of tree
     */
    public Vector splitIntoSBAR(Tree tOther){
        try{
        if(tOther==null)return null;
        Vector splitUtterance = new Vector();
        Tree t = tOther.deepCopy();
        final Tree sBAR= findSBAR(t);    
        Filter f = new Filter(){
            public boolean accept(Object o){
                if(o.equals(sBAR))return false;
                return true;
            }
        };
        if(sBAR!=null){
            t=t.prune(f);
            splitUtterance.addElement(getString(t));
            splitUtterance.addElement(getString(sBAR));
            System.out.println("IN SPLITINTOSBAR "+t+"----"+sBAR);
            return (splitUtterance);
        }
        else{
            return null;
        }
        }catch(Exception e){
            return null;
        }
    }
    
    /**
     * Attempts to find SBAR in parse tree
     * @param t Tree to be searched for SBAR
     * @return null | SBAR subtree
     */
    private Tree findSBAR(Tree t){
        
        if(t.nodeString().equalsIgnoreCase("SBAR")){
            System.out.println("TREE FOUND SBAR");            
            return t;
        }        
        else if (t.children().length!=0){
            System.out.println("TREE NODE IS CALLED - - +"+t.nodeString());
            return findSBAR(t.lastChild());  
        }
        
        return null;
    }
    
    
    
    
    
   
}
        
        
 