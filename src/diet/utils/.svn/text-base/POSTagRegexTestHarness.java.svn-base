package diet.utils;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class POSTagRegexTestHarness {

	public static void main(String a[]) {
		try {
			String taggerFile = System.getProperty("user.dir") + File.separator
					+ "utils" + File.separator
					+ "bidirectional-distsim-wsj-0-18.tagger";
			System.out.println("Regex: ");
			Scanner in = new Scanner(System.in);
			String regex = in.next();
			Pattern p = Pattern.compile(regex);
			MaxentTagger tagger = new MaxentTagger(taggerFile);
			System.out.print("Enter string: ");
			in = new Scanner(System.in);
			in.useDelimiter("\n");
			String sent = in.next();
			System.out.println("Sentence just read:" + sent);
			//Sentence<Word> sente=Sentence.toSentence(sent);
			//System.out.println("There were "+sente.size()+"words.");
			List<ArrayList<? extends HasWord>> sentences=MaxentTagger.tokenizeText(new StringReader(sent));
			System.out.println("String has sentences: "+sentences.size());
			for(ArrayList<? extends HasWord> sente:sentences)
			{
				System.out.println(sente);
			}
			ArrayList<TaggedWord> tagged = tagger.tagSentence(sentences.get(sentences.size()-1));
			//Sentence<TaggedWord> tagged=tagger.tagSentence(sente);
			System.out.println(tagged.toString());
			String tagSequence=getTagSequence(tagged);
			System.out.println("Tag Sequence: " + tagSequence);
			System.out.println("Tagged Sentence: "+tagger.tagString(sent));
			long before=new Date().getTime();
			Matcher m = p.matcher(tagSequence);

			System.out.println("There are " + m.groupCount() + " groups");
			boolean match = m.matches();
			if (match) {
				long after=new Date().getTime();
				System.out.println("Matched. It took "+(after-before)+" ms. Groups are as follows:");
				String group1 = m.group(1);
				
				String frag = findFrag(m.group(3), group1, tagged);
				for (int i = 1; i <= m.groupCount(); i++)
				{
					System.out.println(i+": "+m.group(i));
				}
				System.out.println("\nFrag is: "+frag);
			} else
				System.out.println("not matched");
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
	}
	
	public static String getTagSequence(ArrayList<TaggedWord> lastSent) {
		String result="";
		for(TaggedWord word:lastSent )
		{
			result+=word.tag()+" ";
		}
		return result.trim();
		
		//(.*)(((JJ|NN)\s+)*NN[SP]*)(\s+.+VB[DGNPZ]*)(\s+.+(IN|DT|TO))(.*)
	}
	private static String findFrag(String group, ArrayList<TaggedWord> tagged) {
		if (group==null) return null;
		String[] split=group.trim().split(" ");
		int i=0;
		int matched=0;
		String fragSoFar="";
		for(TaggedWord tw:tagged)
		{
			if (tw.tag().equalsIgnoreCase(split[i]))
			{
				fragSoFar+=(tw.word()+" ");
				matched++;
				if (matched==split.length) return fragSoFar.trim();
				else i++;
				
			} else {matched=0;i=0;fragSoFar="";}
		}
		return null;
		
	}
	protected static String findFrag(String group3, String group1,
			ArrayList<TaggedWord> tagged) {
		if (group3 == null || group3.equals(""))
			return null;

		String[] split3 = group3.trim().split(" ");
		int group1Length;
		if (group1 == null || group1.equals(""))
			group1Length = 0;
		else
			group1Length = group1.trim().split(" ").length;

		int group3Length = split3.length;
		List<TaggedWord> fragSubList = tagged.subList(group1Length,
				group1Length + group3Length);
		String result = "";
		for (TaggedWord w : fragSubList)
			result += (w.word() + " ");
		return result.trim();

	}

}
