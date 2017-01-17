package Engines.SimpleObjects;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import AnnotedText2NIF.IOContent.TextReader;
import Engines.simpleTextProcessing.DistributionProcessing;

/**
 * This class if for the text cleaning at the beginning of the whole text processing.
 * @author TTurke
 *
 */
public class DevelishParenthesis 
{
	
	public static final String optimalRexSQBR = Pattern.quote("[[") + "([^\\[\\]]*)" + Pattern.quote("]]");
	public static final String optimalRexRDBR = Pattern.quote("(") + "([^\\(\\)]*)" + Pattern.quote(")");
	public static final String punctutations = "':,.!-?;\"";
	private HashMap<Character, Integer> errors = new HashMap<Character, Integer>();

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
		errors = new HashMap<Character, Integer>();
		
		if(!content.isEmpty())
		{	
			LinkedList<Integer> out_ix = new LinkedList<Integer>();
			Stack<Integer> index_stack = new Stack<Integer>();
			String input = content;
			char[] input_chars;
			int index;
			
			Matcher matcher = Pattern.compile(optimalRexSQBR).matcher(input);
			while (matcher.find()) input = input.replace(matcher.group(), StringUtils.leftPad("", matcher.group().length(), '*'));
			
			matcher = Pattern.compile(optimalRexRDBR).matcher(input);
			while (matcher.find()) input = input.replace(matcher.group(), StringUtils.leftPad("", matcher.group().length(), '*'));
			
			//now sort all into stack and clean them up
			input_chars = input.toCharArray();
			
			System.out.println("Start error collecting!");
			for (int i = 0; i < input_chars.length; i++) 
			{
				char current = input_chars[i];
				
				if(!Character.isAlphabetic(current) && !Character.isDigit(current) && !Character.isWhitespace(current) && current != '*' && !punctutations.contains(""+current))
				{
					DistributionProcessing.calcDistChar(errors, current);
					index_stack.push(i); 
					out_ix.add(i);
				}	
			}
			
			System.out.println("Start error cleaning!");
			while(!index_stack.isEmpty())
			{
				index = index_stack.pop();
				content = content.substring(0,index)+content.substring(index+1);
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
		String name = "epoch70Final.txt";
		String input = TextReader.fileReader(tr.getResourceFileAbsolutePath(name));
		DevelishParenthesis dp = new DevelishParenthesis();
		
		System.out.println("INPUT: \n"+input);
		System.out.println("RESULT: \n"+dp.cleanErrorsAndParenthesis(input));
		System.out.println("ERRORS: \n"+dp.getErrors().keySet());
		System.out.println();
		for (char c : dp.getErrors().keySet())
		{
			System.out.println("ERROR SYMBOL >"+c+"<");
			System.out.println("VALUE: "+dp.getErrors().get(c));
		}
	}
}
