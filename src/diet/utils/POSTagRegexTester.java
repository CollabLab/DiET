package diet.utils;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.Vector;
import java.util.regex.Matcher;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class POSTagRegexTester {

	Vector<POSTagRegex> regexes = new Vector<POSTagRegex>();
	MaxentTagger tagger;

	public POSTagRegexTester(String regexFile, String misspellingsFile) throws Exception {
		loadRegexes(new File(regexFile));
		this.loadMisspellings(misspellingsFile);
		String taggerFile = System.getProperty("user.dir") + File.separator
				+ "utils" + File.separator
				+ "left3words-distsim-wsj-0-18.tagger";
		tagger = new MaxentTagger(taggerFile);

	}

	private void loadRegexes(File regexFile) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(regexFile));
		String regex;
		while ((regex = reader.readLine()) != null) {
			if (regex.startsWith("%")) continue;
			String[] split=regex.split(" ");
			regexes.add(new POSTagRegex(split[2], split[0], split[1]));
		}
		reader.close();
	}

	public int processTextFile(String fName, String outputFile)
			throws Exception {
		File f = new File(fName);
		BufferedReader reader = new BufferedReader(new FileReader(f));
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				outputFile)));
		String line1;
		int matchedCount=0;
		while ((line1 = reader.readLine()) != null) {
			
			String line=this.fixSpelling(line1);
			System.out.println("Processing. Line after spelling correction: "+line);
			List<ArrayList<? extends HasWord>> sentences = MaxentTagger
					.tokenizeText(new StringReader(line));
			boolean matched = false;
			
			Outerloop:
			for (ArrayList<? extends HasWord> sent : sentences) {

				ArrayList<TaggedWord> tagged = tagger.tagSentence(sent);
				ArrayList<TaggedWord> soFar = new ArrayList<TaggedWord>();
				for (TaggedWord tw : tagged) {
					soFar.add(tw);

					
					
					for (POSTagRegex regex : this.regexes) {
						String tagSequence = getTagSequence(soFar, regex.endInTagOrWord);
						Matcher m = regex.regex.matcher(tagSequence);
						if (m.matches()) {
							writer.write(getString(soFar));
							writer.write("|");
							String frag=this.findFrag(m.group(3), m.group(1), tagged);
							
							writer.write(frag+"|"+this.regexes.indexOf(regex)+"|"+m.group(m.groupCount()));
							writer.newLine();
							writer.flush();
							matched = true;
							matchedCount++;
							break Outerloop;
						}

					}
				}
				
			}
			if (!matched) {
				writer.write(tagger.tagString(line));
				writer.write("|");
				writer.write("No match");
				writer.newLine();
				writer.flush();
				
			}
			
		}
		return matchedCount;
	}
	private static String getString(ArrayList<TaggedWord> s)
	{
		String result="";
		for(TaggedWord tw:s)
		{
			result+=tw.word()+"_"+tw.tag()+" ";
		}
		return result.trim();
	}

	private static String findFrag(String group3, String group1, ArrayList<TaggedWord> tagged) {
		if (group3==null||group3.equals("")) return null;
				
		String[] split3=group3.trim().split(" ");
		int group1Length;
		if (group1==null||group1.equals("")) group1Length=0;
		else group1Length=group1.trim().split(" ").length;		
		
		int group3Length=split3.length;
		List<TaggedWord> fragSubList=tagged.subList(group1Length, group1Length+group3Length);
		String result="";
		for (TaggedWord w: fragSubList) result+=(w.word()+" ");
		return result.trim();
		
	}
	
	private String getTagSequence(ArrayList<TaggedWord> lastSent, String endInTag) {
		String result = "";
		
		for (int i=0;i<lastSent.size();i++) {
			TaggedWord word = lastSent.get(i);
			if (i==lastSent.size()-1&&!endInTag.equalsIgnoreCase("EndsInTag")) result += word.word();
			else result+= (word.tag()+ " ");
		}
		return result.trim();
	}
	TreeMap<String, String> misspellings=new TreeMap<String, String>();
	private void loadMisspellings(String fileName) throws IOException
	{
		BufferedReader in=new BufferedReader(new FileReader(new File(fileName)));
		String line;
		while((line=in.readLine())!=null)
		{
			String[] halves=getHalves(line);
			misspellings.put(halves[0], halves[1]);
		}
		
	}
	private String[] getHalves(String whole)
	{
		String[] result=new String[2];
		for(int i=0;i<whole.length();i++)
		{
			if (whole.charAt(i)==' ')
			{
				result[0]=whole.substring(0, i);
				result[1]=whole.substring(i+1, whole.length());
				return result;
			}
		}
		return null;
	}
	private String fixSpelling(String s)
	{
		String split[]=s.split(" ");
		String result="";
		for(String w:split)
		{
			if (misspellings.containsKey(w.toLowerCase())) result+=misspellings.get(w.toLowerCase())+" ";
			else result+=w+" ";
		}
		return result.trim();
		
	}
	private ArrayList<Word> fixSpelling(ArrayList<? extends HasWord> s)
	{
		ArrayList<Word> result=new ArrayList<Word>();
		
		for(HasWord w:s)
		{
			if (misspellings.containsKey(w.word())) result.add(new Word(misspellings.get(w.word())));
			else result.add(new Word(w.word()));
		}
		return result;
		
	}

	public static void main(String a[]) throws Exception {
		String regexFile = System.getProperty("user.dir")
				+ File.separator + "fragmentFilters" + File.separator
				+ "constBoundaryRegexes.txt";
		String misspellingsFile=System.getProperty("user.dir")
		+ File.separator + "fragmentFilters" + File.separator
		+ "misspellings.txt";
		POSTagRegexTester p=new POSTagRegexTester(regexFile, misspellingsFile);
		int matched=p.processTextFile("D:\\UniWork\\Experiments(data and writing)\\Pilots\\Within-Constituent_Regex_Test\\transcript3.txt",
				"D:\\UniWork\\Experiments(data and writing)\\Pilots\\Within-Constituent_Regex_Test\\regexTestResults3Boundary.csv");
		System.out.println(matched+" matches.");

	}

}
