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
	
	public static char[] puncs = ",;.!?-'".toCharArray();
	public static final LinkedList<Character> punctuations = new LinkedList<Character>();
	
	
	private HashMap<Character, Integer> errors;

	//#############################################################################
	//############################# CONSTRUCTORS ##################################
	//#############################################################################
	
	/**
	 * Constructor
	 */
	public DevelishParenthesis(){
		 errors = new HashMap<Character, Integer>();
		 for(char c : puncs) punctuations.add(c);
	}
	
	
	//#############################################################################
	//############################ USAGE METHODS ##################################
	//#############################################################################
	
	/**
	 * This method check a character is a letter or a digit or a spacing or a *-placeholder or a punctuation
	 * @param c
	 * @return true if allowed
	 */
	public static boolean isAllowedChar(char c)
	{
		if(Character.isLetter(c) || Character.isDigit(c) || Character.isWhitespace(c) || c == '*' || punctuations.contains(c))
		{
			return true;
		}else{
			return false;
		}
	}
	
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
				
				if(isAllowedChar(current))
				{
					continue;
				}else{
					DistributionProcessing.calcDist(errors, current);
					index_stack.push(i); 
					out_ix.add(i);
				}
			}
			
			while(!index_stack.isEmpty())
			{
				
				
				String part1, part2;
				index = index_stack.pop();
				
				if(index > 1 )
				{
					part1 = content.substring(0,index);
					
					if(index < content.length()-1)
					{
						part2 = content.substring(index+1,content.length());
						
						if(Character.isLetter(content.charAt(index-1)) && Character.isLetter(content.charAt(index+1)))
						{
							content = part1+" "+part2;
						}else{
							content = part1+part2;
						}	
					}else{
						content = part1;
					}
				}else{
					content = content.substring(1);
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
		
		LinkedList<String> sentences_cleaned = st.sentencesAsStrings(st.gatherSentences(input, dp));
		
		for(String current : sentences_cleaned) System.out.println("Output: "+current); 
		
		
	}
}
