package diet.utils;

import java.util.regex.Pattern;

public class POSTagRegex {

	public Pattern regex;
	public String endInTagOrWord;
	public String type;
	
	public POSTagRegex(String regex, String endInTagOrWord, String type)
	{
		this.regex=Pattern.compile(regex);
		this.endInTagOrWord=endInTagOrWord;
		this.type=type;
	}
	
}
