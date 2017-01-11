package Engines.SimpleObjects;

import java.io.IOException;
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
	public static final String optimalRexRDBR = Pattern.quote("(") + "([^\\(\\)]*)" + Pattern.quote(")");
	public static final String punctutations = "':,.!-?;\"|";
	private int error_count = 0;

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
			
			for (int i = 0; i < input_chars.length; i++) 
			{
				char current = input_chars[i];
				
				if(!Character.isAlphabetic(current) && !Character.isDigit(current) && !Character.isWhitespace(current) && current != '*' && !punctutations.contains(""+current)){
					addOneToErrorCount();
					index_stack.push(i); 
				}	
			}
			
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
	
	public int getErrorCount() {
		return error_count;
	}
	
	public void setErrorCount(int error_count) {
		this.error_count = error_count;
	}

	public void addOneToErrorCount() {
		this.error_count++;
	}

	//#############################################################################
	//############################### EXAMPLE #####################################
	//#############################################################################

	public static void main(String[] args) throws IOException 
	{
		TextReader tr = new TextReader();
		String name = "epoch70Final.txt";
		String input = tr.fileReader(tr.getResourceFileAbsolutePath(name));
		DevelishParenthesis dp = new DevelishParenthesis();
		
		String text = "she ]]wrote many [[of her songs for) herself ((and as such made)) no (particular) effor<t (to make them easy to sing, melodically, as she < herself had [[absolute pitch|perfect pitch]] aha ]].";
		System.out.println("INPUT: \n "+input);
		System.out.println("RESULT: \n"+dp.cleanErrorsAndParenthesis(input));
		System.out.println("ERRORS: \n"+dp.getErrorCount());
	}

}
