package diet.utils.postprocessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import diet.server.Conversation;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class POSEntropyCalc {

    MaxentTagger tagger;

    final static String taggerFileName = System.getProperty("user.dir")
	    + File.separator + "utils" + File.separator
	    + "bidirectional-distsim-wsj-0-18.tagger";
    static final File misspellingsFile = new File(System
	    .getProperty("user.dir")
	    + File.separator
	    + "fragmentFilters"
	    + File.separator
	    + "misspellings.txt");

    TreeMap<String, String> misspellings;

    File inputDir;

    public POSEntropyCalc(String inputDataDir) {
	try {

	    this.tagger = new MaxentTagger(taggerFileName);
	    loadMisspellings();

	    inputDir = new File(inputDataDir);

	} catch (Exception e) {
	    e.printStackTrace();
	}

    }

    private synchronized void loadMisspellings() throws IOException {
	misspellings = new TreeMap<String, String>();
	Conversation.printWSln("Main", "loading misspellings");
	BufferedReader in = new BufferedReader(new FileReader(
		misspellingsFile));
	String line;
	while ((line = in.readLine()) != null) {
	    String[] halves = getHalves(line);
	    misspellings.put(halves[0], halves[1]);
	}

    }

    private String[] getHalves(String whole) {
	String[] result = new String[2];
	for (int i = 0; i < whole.length(); i++) {
	    if (whole.substring(i, i+1).matches("\\s+")) {
		result[0] = whole.substring(0, i).trim();
		result[1] = whole.substring(i + 1, whole.length()).trim();
		return result;
	    }
	}
	return null;
    }

    final static String datafileExtension = "csv";
    
    public HashMap<OrderedPair, Integer> POSDensityMatrix=null;
    public HashMap<OrderedPair, Integer> POSLexDensityMatrix=null;
    
    public TreeMap<String, Integer> getOccurrencesOfTagsAfterTurnLength(int min) throws IOException
    {
	TreeMap<String, Integer> occurrences=new TreeMap<String, Integer>();
	
	
	File[] files = inputDir.listFiles();
	int totalTurns=0;
	for (File curFile : files) {
	    if (!curFile.getName().endsWith(datafileExtension))
		continue;
	    System.out.print("Processing file: " + curFile+" ...");
	    BufferedReader in = new BufferedReader(new FileReader(curFile));
	    String line = in.readLine();

	    while ((line = in.readLine()) != null) {
		String[] values = line.split("\\|", -1);
		if (values[0].equalsIgnoreCase("server")
			|| values[1].equalsIgnoreCase("server")
			|| values[2].equalsIgnoreCase("server")
			|| values[9].isEmpty())
		    continue;
		String curTurn = values[8];    
		
		String spellingCorrected = fixSpelling(curTurn);
		float distance=0.0f;
		
		if (spellingCorrected.trim().isEmpty()) {
		 //   System.out.println("EMPTY. SKIPPING THIS.");
		    continue;
		}
		List<ArrayList<? extends HasWord>> sentences = MaxentTagger
			.tokenizeText(new StringReader(spellingCorrected));
		if (sentences.isEmpty()) {
		   
		    continue;
		}
		totalTurns++;
		for (ArrayList<? extends HasWord> sent : sentences) {
		    ArrayList<TaggedWord> taggedSentence = tagger
		    .tagSentence(sent);
		    boolean lastSentence=(sent==sentences.get(sentences.size()-1));
		    if (lastSentence)
		    {		    
			taggedSentence.add(new TaggedWord("","EOT"));
		    }
		        
		    for (int i = 0; i < taggedSentence.size(); i++) {
			TaggedWord cur = taggedSentence.get(i);
			distance++;
			if (distance>=min)
			{
			    if (occurrences.containsKey(cur.tag()))
			    {
				occurrences.put(cur.tag(), occurrences.get(cur.tag())+1);
			    
			    
			    }else
			    {
				occurrences.put(cur.tag(), 1);
			    
			    }
			}		
		    }
		}
	    }
	}
	
	System.out.println("there were "+totalTurns+" turns in total.");
	
	
	return occurrences;
	
    }
    
    public TreeMap<String, Float> averageDistancesFromTurnBeginning() throws IOException
    {
	TreeMap<String, Float> sumDistances=new TreeMap<String, Float>();
	
	TreeMap<String, Float> counts=new TreeMap<String, Float>();
	File[] files = inputDir.listFiles();
	for (File curFile : files) {
	    if (!curFile.getName().endsWith(datafileExtension))
		continue;
	    System.out.print("Processing file: " + curFile+" ...");
	    BufferedReader in = new BufferedReader(new FileReader(curFile));
	    String line = in.readLine();

	    while ((line = in.readLine()) != null) {
		String[] values = line.split("\\|", -1);
		if (values[0].equalsIgnoreCase("server")
			|| values[1].equalsIgnoreCase("server")
			|| values[2].equalsIgnoreCase("server")
			|| values[9].isEmpty())
		    continue;
		String curTurn = values[8];    
		
		String spellingCorrected = fixSpelling(curTurn);
		float distance=0.0f;
		
		if (spellingCorrected.trim().isEmpty()) {
		 //   System.out.println("EMPTY. SKIPPING THIS.");
		    continue;
		}
		List<ArrayList<? extends HasWord>> sentences = MaxentTagger
			.tokenizeText(new StringReader(spellingCorrected));
		if (sentences.isEmpty()) {
		   
		    continue;
		}
		for (ArrayList<? extends HasWord> sent : sentences) {
		    ArrayList<TaggedWord> taggedSentence =tagger
		    .tagSentence(sent);
		    boolean lastSentence=(sent==sentences.get(sentences.size()-1));
		    if (lastSentence)
		    {		    
			taggedSentence.add(new TaggedWord("","EOT"));
		    }
		        
		    for (int i = 0; i < taggedSentence.size(); i++) {
			TaggedWord cur = taggedSentence.get(i);
			distance++;
			//if (cur.tag().equals("DT")) System.out.println("Turn was:"+spellingCorrected+"\nDT Dist: "+distance);
			if (sumDistances.containsKey(cur.tag()))
			{
			    sumDistances.put(cur.tag(), sumDistances.get(cur.tag())+distance);
			    counts.put(cur.tag(), counts.get(cur.tag())+1);
			    
			}else
			{
			    sumDistances.put(cur.tag(), distance);
			    counts.put(cur.tag(), 1.0f);
			}
			
			
			
			
		    }
		}
	    }
	}
	//System.out.println(sumDistances);
	//System.out.println(counts);
	TreeMap<String, Float> averages=new TreeMap<String, Float>();
	for(String tag: sumDistances.keySet())
	{
	    averages.put(tag, sumDistances.get(tag)/counts.get(tag));
	}
	return averages;
    }
    public void generatePOSLexDensityMatrices(int minDistance, int maxDistance)
	    throws IOException {
	POSDensityMatrix = new HashMap<OrderedPair, Integer>();
	POSLexDensityMatrix=  new HashMap<OrderedPair, Integer>();
	File[] files = inputDir.listFiles();
	for (File curFile : files) {
	    if (!curFile.getName().endsWith(datafileExtension))
		continue;
	    System.out.print("Processing file: " + curFile+" ...");
	    BufferedReader in = new BufferedReader(new FileReader(curFile));
	    String line = in.readLine();
	    TURNS:
	    while ((line = in.readLine()) != null) {
		String[] values = line.split("\\|", -1);
		if (values[0].equalsIgnoreCase("server")
			|| values[1].equalsIgnoreCase("server")
			|| values[2].equalsIgnoreCase("server")
			|| values[9].isEmpty())
		    continue;
		
		String curTurn = values[8];
		
		 
		    
		boolean debug=false;
		//System.out.println("Processing text: " + curTurn);
		String spellingCorrected = fixSpelling(curTurn);
		
		if (spellingCorrected.trim().isEmpty()) {
		 //   System.out.println("EMPTY. SKIPPING THIS.");
		    continue;
		}
		int distance=0;
		List<ArrayList<? extends HasWord>> sentences = MaxentTagger
			.tokenizeText(new StringReader(spellingCorrected));
		if (sentences.isEmpty()) {
		   
		    continue;
		}
		for (ArrayList<? extends HasWord> sent : sentences) {
		    
		    //Sentence<? extends HasWord> sentCorrected
		    ArrayList<TaggedWord> taggedSentence = tagger
			    .tagSentence(sent);
		    boolean lastSentence=(sent==sentences.get(sentences.size()-1));
		    if (lastSentence)
		    {		    
			taggedSentence.add(new TaggedWord("","EOT"));
		    }
		    if (taggedSentence.size() < 2)
			continue;
		    TaggedWord prev = taggedSentence.get(0);
		    //System.out.print(prev.word() + ":" + prev.tag() + ", ");
		    for (int i = 1; i < taggedSentence.size(); i++) {
			TaggedWord cur = taggedSentence.get(i);
			distance++;
			
			if (maxDistance>0&&distance>maxDistance) continue TURNS;
			if (distance<minDistance)
			{
			    prev=cur;
			
			    continue;
			}
			//System.out.print(cur.word() + ":" + cur.tag() + ", ");
			if (filter(cur.word())) continue;
			
			OrderedPair keyPOS;
			OrderedPair keyLex;
			keyPOS = new OrderedPair(prev.tag(), cur.tag());
			keyLex = new OrderedPair(prev.tag(), (misspellings.containsKey(cur.word())?misspellings.get(cur.word()):cur.word()));
			if (POSDensityMatrix.containsKey(keyPOS)) {
			    //System.out.println("putting "+key.tag1+","+key.tag2);
			    POSDensityMatrix.put(keyPOS,
				    POSDensityMatrix.get(keyPOS) + 1);
			} else {

			  //  System.out.println("putting "+key.tag1+","+key.tag2);
			    POSDensityMatrix.put(keyPOS, 1);
			}
			//POSLex doesn't make sense at end of turn.
			if (lastSentence && i==taggedSentence.size()-1) break;
			if (POSLexDensityMatrix.containsKey(keyLex)) {
			    //System.out.println("putting "+key.tag1+","+key.tag2);
			    POSLexDensityMatrix.put(keyLex,
				    POSLexDensityMatrix.get(keyLex) + 1);
			} else {

			  //  System.out.println("putting "+key.tag1+","+key.tag2);
			    POSLexDensityMatrix.put(keyLex, 1);
			}

			prev = cur;
		    }

		}
		//System.out.println();

	    }
	    System.out.println("done.");
	}
	

    }
    
    

    private static boolean filter(String word1) {
	String word=word1.trim();
	if (word.matches("[\\.\\?!,;]")) return false;
	if (word.startsWith("'")||word.startsWith("`")) return false;
	
	if (word.matches(".*\\W.*")) return true;
	
	
	if (word.matches(".*[a-zA-Z].*\\d.*")) return true;
	if (word.matches(".*\\d.*[a-zA-Z].*")) return true;
	
	return false;
    }
    

    protected String fixSpelling(String turn) {

	String split[] = turn.split(" ");
	String result = "";
	for (int i = 0; i < split.length; i++) {

	    String w = split[i];

	    String lowerCase = w.toLowerCase();
	    Pattern p=Pattern.compile("([\\\\/\\.\\?!,;\\(\\)]*)([^\\\\/\\.\\?!,;\\(\\)]+)([\\\\/\\.\\?!,;\\(\\)]*)");
	    Pattern p1=Pattern.compile("([\\\\/\\.\\?!,;\\(\\)]*)([^\\\\/\\.\\?!,;\\(\\)]+)([\\\\/\\.\\?!,;\\(\\)]+)([^\\\\/\\.\\?!,;\\(\\)]+)([\\\\/\\.\\?!,;\\(\\)]*)");
	    Matcher m=p.matcher(lowerCase);
	    Matcher m1=p1.matcher(lowerCase);
	    if (m.matches())
	    {
		if (misspellings.containsKey(m.group(2)))
		{
		    String replacement = misspellings.get(m.group(2));
		    result += m.group(1)+replacement +m.group(3)+ " ";
		}
		else
		{
		    result += m.group(1)+m.group(2) +m.group(3)+ " ";
		}
	    } else if(m1.matches())
	    {
		
		result += m1.group(1)+(misspellings.containsKey(m1.group(2))?misspellings.get(m1.group(2)):m1.group(2)) +m1.group(3)
		    + (misspellings.containsKey(m1.group(4))?misspellings.get(m1.group(4)):m1.group(4))
		    + m1.group(5)+
		    " ";
		
	    }else
	    {    
		if (misspellings.containsKey(lowerCase)) {
		    
		    String replacement = misspellings.get(lowerCase);
		
		    result += replacement + " ";
		} else
		{
		    if (!w.matches("[A-Z][a-z]*")) result += lowerCase + " ";
		    else result+=w+" ";
		}
	    }
	}
	return result.trim();
    }

    public HashMap<OrderedPair, Float> generateProbMatrix(
	    HashMap<OrderedPair, Integer> densities) {
	HashMap<OrderedPair, Float> probMatrix = new HashMap<OrderedPair, Float>();
	TreeSet<String> POSTags = getAllPreviousTags(densities);

	for (String tag : POSTags) {
	    float sum = sum(tag, densities);
	    TreeSet<String> adjacentTags = getAdjacentTags(tag, densities);
	    for (String adjacentTag : adjacentTags) {
		OrderedPair pair = new OrderedPair(tag, adjacentTag);
		probMatrix.put(pair, (float) densities.get(pair) / sum);

	    }
	}

	return probMatrix;
    }

    public TreeMap<String, Float> generateEntropyVector(
	    HashMap<OrderedPair, Float> probMatrix) {
	TreeMap<String, Float> entropies = new TreeMap<String, Float>();
	for (String tag : getAllPreviousTags(probMatrix)) {
	    entropies.put(tag, sumPLogs(tag, probMatrix));
	}

	return entropies;
    }

    private float sum(String tag, HashMap<OrderedPair, Integer> densities) {
	float result = 0.0f;
	for (OrderedPair pair : densities.keySet()) {
	    if (pair.first.equals(tag))
		result += (float) densities.get(pair);
	}
	return result;
    }

    private float sumPLogs(String tag, HashMap<OrderedPair, Float> probMatrix) {
	float result = 0.0f;
	for (OrderedPair pair : probMatrix.keySet()) {
	    if (pair.first.equals(tag)) {
		float value = probMatrix.get(pair);
		result += (value * (Math.log(value) / Math.log(2)));
	    }
	}
	return (result==0) ? 0.0f : (-1 * result);
    }

    private static TreeSet<String> getAdjacentTags(String tag,
	    HashMap<OrderedPair, ? extends Object> densities) {
	TreeSet<String> result = new TreeSet<String>();
	for (OrderedPair pair : densities.keySet()) {
	    if (pair.first.equals(tag))
		result.add(pair.second);
	}

	return result;
    }

    private static TreeSet<String> getAllAdjacentTags(
	    HashMap<OrderedPair, ? extends Object> densities) {
	TreeSet<String> result = new TreeSet<String>();
	for (OrderedPair pair : densities.keySet()) {
	    result.add(pair.second);
	}

	return result;
    }

    private static TreeSet<String> getAllPreviousTags(
	    HashMap<OrderedPair, ? extends Object> densities) {
	TreeSet<String> result = new TreeSet<String>();
	for (OrderedPair pair : densities.keySet()) {
	    if (!result.contains(pair.first))
		result.add(pair.first);
	}
	return result;
    }

    public static String printMatrix(HashMap<OrderedPair, ? extends Object> m) {
	String result = "";
	TreeSet<String> rowHeadings = getAllPreviousTags(m);
	TreeSet<String> columnHeadings = getAllAdjacentTags(m);
	result += "\t";
	for (String tag : columnHeadings) {
	    result += tag + "\t";
	}
	result += "\n";
	for (String tag : rowHeadings) {
	    result += tag + "\t";
	    for (String tag2 : columnHeadings) {
		if (m.get(new OrderedPair(tag, tag2)) == null)
		    result += "0\t";
		else {
		    String value = m.get(new OrderedPair(tag, tag2))
			    .toString();
		    result += value.substring(0, value.length() > 7 ? 7 : value
			    .length())
			    + "\t";
		}
	    }
	    result += "\n";

	}
	return result;

    }
    
    public static String printMatrixAsCSV(HashMap<OrderedPair, ? extends Object> m) {
	String result = "";
	TreeSet<String> rowHeadings = getAllPreviousTags(m);
	TreeSet<String> columnHeadings = getAllAdjacentTags(m);
	result += "|";
	for (String tag : columnHeadings) {
	    result += tag + "|";
	}
	result=result.substring(0, result.length()-1);
	result += "\n";
	for (String tag : rowHeadings) {
	    result += tag + "|";
	    for (String tag2 : columnHeadings) {
		if (m.get(new OrderedPair(tag, tag2)) == null)
		    result += "0|";
		else {
		    
		    result += m.get(new OrderedPair(tag, tag2))+ "|";
		}
	    }
	    result += "\n";

	}
	return result;

    }
    
    public static void main(String a[]){
	
	String inputDir = "D:\\UniWork\\Experiments(data and writing)\\Syntactic_Entropy\\data";
	POSEntropyCalc c = new POSEntropyCalc(inputDir);
	//System.out.println(c.fixSpelling("wt wud susie do"));
	//System.out.println(MaxentTagger.tokenizeText(new StringReader("making watever/")));
	
	//System.out.println(c.fixSpelling("fine...watever"));
	
	//try{
	//System.out.println("Occurrences:\n"+c.getOccurrencesOfTagsAfterTurnLength(9));
	//}catch(Exception e){e.printStackTrace();}
	
	
	HashMap<OrderedPair, Float> probsPOS = null;
	HashMap<OrderedPair, Float> probsLex = null;
	TreeMap<String, Float> entropiesPOS = null;
	TreeMap<String, Float> entropiesLex = null;
	try {
	    c.generatePOSLexDensityMatrices(10,-1);
	    probsPOS = c.generateProbMatrix(c.POSDensityMatrix);
	    entropiesPOS = c.generateEntropyVector(probsPOS);
	    probsLex = c.generateProbMatrix(c.POSLexDensityMatrix);
	    entropiesLex = c.generateEntropyVector(probsLex);

	} catch (Exception e) {
	    e.printStackTrace();
	}
	
	try{
	BufferedWriter out=new BufferedWriter(new FileWriter(new File("D:\\UniWork\\Experiments(data and writing)\\Syntactic_Entropy\\POS_entropies_MoreThan9.csv")));
	out.write("The Density Matrix:\n" + printMatrixAsCSV(c.POSDensityMatrix));
	out.newLine();

	out.write("\n\nThe Probability Matrix:\n" + printMatrixAsCSV(probsPOS));
	out.newLine();

	out.write("\n\nThe Entropies: " + entropiesPOS);
	out.newLine();
	out.close();
	//________________ Now pos-lex
	out=new BufferedWriter(new FileWriter(new File("D:\\UniWork\\Experiments(data and writing)\\Syntactic_Entropy\\POS_Lex_entropies_MoreThan9.csv")));
	out.write("The Density Matrix:\n" + printMatrixAsCSV(c.POSLexDensityMatrix));
	out.newLine();

	out.write("\n\nThe Probability Matrix:\n" + printMatrixAsCSV(probsLex));
	out.newLine();

	out.write("\n\nThe Entropies: " + entropiesLex);
	out.newLine();
	out.close();
	}catch(Exception e){e.printStackTrace();}
	
	
	
    }

}

class OrderedPair {
    public String first;
    public String second;

    public OrderedPair(String t1, String t2) {
	first = t1;
	second = t2;
    }

    public boolean equals(Object o) {
	if (o == this)
	    return true;
	if (!(o instanceof OrderedPair))
	    return false;
	OrderedPair other = (OrderedPair) o;

	return first.equals(other.first) && second.equals(other.second);
    }

    public int hashCode() {
	String whole = first + second;
	return whole.hashCode();
    }

}
