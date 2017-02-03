package Engines.simpleTextProcessing;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import AnnotedText2NIF.IOContent.TextReader;

/**
 * This class if for the text cleaning at the beginning of the whole text processing.
 * @author TTurke
 *
 */
public class DevelishParenthesis 
{
	
	public static final String optimalRexSQBR = Pattern.quote("[[") + "([^\\[\\]]*)" + Pattern.quote("]]");
	public static final String optimalRexRDBR = Pattern.quote("(") + "([a-zA-Z0-9, ]*)" + Pattern.quote(")");
	public static final String optimalREXEntity = Pattern.quote("[[") + "([a-zA-Z0-9,| ]*)"+"(([(])([a-zA-Z0-9, ]*)([)])|([a-zA-Z0-9,| ]*))"+"([a-zA-Z0-9,| ]*)" + Pattern.quote("]]");
	
	public static final String punctutations = ",;.!?-'";
	private HashMap<Character, Integer> errors;

	//#############################################################################
	//############################# CONSTRUCTORS ##################################
	//#############################################################################
	
	/**
	 * Constructor
	 */
	public DevelishParenthesis(){
		 errors = new HashMap<Character, Integer>();
	}
	
	
	//#############################################################################
	//############################ USAGE METHODS ##################################
	//#############################################################################
	
	/**
	 * This method take a text and clean all errors and false opened or never closed square and round brackets.
	 * The error occurrence count will be stored as simple integer inside the error_count value.
	 * @param content
	 * @return cleaned String
	 */
	public String cleanErrorsAndParenthesis(String content)
	{		
		if(!content.isEmpty())
		{	
			LinkedList<Integer> out_ix = new LinkedList<Integer>();
			Stack<Integer> index_stack = new Stack<Integer>();
			String input = content;
			char[] input_chars;
			int index;
			
			Matcher matcher = Pattern.compile(optimalREXEntity).matcher(input);
			while (matcher.find()) input = input.replace(matcher.group(), StringUtils.leftPad("", matcher.group().length(), '*'));
			
			matcher = Pattern.compile(optimalRexRDBR).matcher(input);
			while (matcher.find()) input = input.replace(matcher.group(), StringUtils.leftPad("", matcher.group().length(), '*'));
			
			//now sort all into stack and clean them up
			input_chars = input.toCharArray();
			
			for (int i = 0; i < input_chars.length; i++) 
			{
				char current = input_chars[i];
				
				if(!Character.isAlphabetic(current) && !Character.isDigit(current) && !Character.isWhitespace(current) && current != '*' && !punctutations.contains(""+current))
				{
					DistributionProcessing.calcDist(errors, current);
					index_stack.push(i); 
					out_ix.add(i);
				}	
			}
			
			while(!index_stack.isEmpty())
			{
				index = index_stack.pop();
				if(index > 1 && Character.isAlphabetic(content.charAt(index-1)) && Character.isAlphabetic(content.charAt(index+1)))
				{
					content = content.substring(0,index)+" "+content.substring(index+1);
				}else{
					content = content.substring(0,index)+content.substring(index+1);
				}
				
			}

			return content;
		}
		return content;
	}
	
	//#############################################################################
	//###################### GETTERS, SETTERS & EDITS #############################
	//#############################################################################
	
	public HashMap<Character, Integer> getErrors() {
		return errors;
	}

	public void setErrors(HashMap<Character, Integer> errors) {
		this.errors = errors;
	}

	//#############################################################################
	//############################### EXAMPLE #####################################
	//#############################################################################

	/*
	 * EXAMPLE of USE
	 */
	public static void main(String[] args) throws IOException 
	{
		TextReader tr = new TextReader();
		String name = "BVFragment.txt";
		String input = TextReader.fileReader(tr.getResourceFileAbsolutePath(name));
		
		StanfordTokenizer st = new StanfordTokenizer();
		DevelishParenthesis dp = new DevelishParenthesis();		
		System.out.println(input);
		LinkedList<String> sentences_cleaned = st.sentencesAsStrings(st.gatherSentences(input, dp));
		
		for(String current : sentences_cleaned) System.out.println(current); 
		
		
	}
}
