package diet.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeSet;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Word;

public class Dictionary {
	
	private TreeSet<String> content;
	
	public Dictionary(String dictionaryFile) throws IOException
	{
		content=new TreeSet<String>();
		BufferedReader reader=new BufferedReader(new FileReader(new File(dictionaryFile)));
		String curWord;
		while((curWord=reader.readLine())!=null)
		{
			content.add(curWord);
		}
		
	}
	
	public boolean hasWord(String w)
	{
		String lowerCaseTrimmed=w.toLowerCase().trim();
		return content.contains(lowerCaseTrimmed);
	}
	public void addWord(String w)
	{
		content.add(w);
	}

}