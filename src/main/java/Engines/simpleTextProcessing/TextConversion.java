package Engines.simpleTextProcessing;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * This class contain a text decomposer which try to filter all not desired types of characters out of a String.
 * @author TTurke
 *
 */
public class TextConversion 
{
	public static final String punctutations = "':,.!-()?;[]\\|";
	public static final String sentence_separator = ".?!";
	private HashMap<Character, Integer> errors = new HashMap<Character, Integer>();
	
	//#############################################################################
	//############################ USAGE METHODS ##################################
	//#############################################################################
	
	/**
	 * This method split a text or sentence by whitespace into words (for bottom value text creation).
	 * @param text
	 * @return a list of words
	 */
	public static LinkedList<String> splitIntoWords(String text)
	{
		return new LinkedList<String>(Arrays.asList(text.split(" ")));
	}
	
	/**
	 * This method scores a character by its char type
	 * @param ch
	 * @param is_last
	 * @return
	 */
	public static boolean scoreChar(char ch, boolean is_last)
	{
		//1st character check up
		if(Character.isAlphabetic(ch) && !is_last)
		{
			if(Character.isUpperCase(ch))
			{
				return true;
			}else{
				return false;
			}
		}else if(Character.isDigit(ch)  && !is_last)
		{
			return true;
		}else if(sentence_separator.contains(""+ch) && is_last) //last character check up
		{
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * This method decompose a text into characters, clean and store the error occurrences.
	 * The result is a text without error symbols in it. 
	 * That means no cryptic words. Keep in mind that this method is just for English text!
	 * @param text
	 * @return cleaned text
	 */
	public String decompose(String text)
	{
		char[] chars_in = text.toCharArray();
		System.out.println("Chars to pass: "+chars_in.length);
		String out = "";
		char old = ' ';
		char c = ' ';
		
		for (int k = 0 ; k < chars_in.length; k++) 
		{
			c = chars_in[k];
			
			if(Character.isAlphabetic(c) || Character.isDigit(c) || Character.isSpaceChar(c))						// Check Letter, Digit and Whitespace
			{
				out += c;
			}else if(punctutations.contains(""+c))																	// Check punctuation 
			{
				if(c == '.')																						// Check previous is a letter and current is a dot
				{
					if(	(k+1) < chars_in.length && 
						Character.isUpperCase(old) && Character.isAlphabetic(old) && 
						Character.isUpperCase(chars_in[k+1]) && Character.isAlphabetic(chars_in[k+1]))				// Check previous and next value is a upper case letter to keep cases like C.E.O. 
					{
						out += c;
					}else if(	(k+1) < chars_in.length && 
								Character.isLowerCase(old) && Character.isAlphabetic(old) && 						// Check previous and next value is a lower case letter to fix cases like rai.se => raise
								Character.isLowerCase(chars_in[k+1]) && Character.isAlphabetic(chars_in[k+1]))
					{
						DistributionProcessing.calcDistChar(errors, c);
					}else{																							// DEFAUL add space+nonAlnum+space
						out += c;
						out += ' ';
					}
				}else if((c == '[' || c == ']') && (k+1) < chars_in.length)											// Check current is [ or ] and next is not out of bounds
				{
					if(c == '[' && (old == '['|| chars_in[k+1] == '['))												// Check current structure is like [[
					{
						out += c;
					}else if(c == ']' && (old == ']'|| chars_in[k+1] == ']')){										// Check current structure is like ]]
						out += c;
					}else if(	(k+1) < chars_in.length && 
							Character.isLowerCase(old) && Character.isAlphabetic(old) && 							// Check previous and next value is a lower case letter to fix cases like rai.se => raise
							Character.isLowerCase(chars_in[k+1]) && Character.isAlphabetic(chars_in[k+1]))
					{
					DistributionProcessing.calcDistChar(errors, c);
					}else{																							// DEFAUL add space+nonAlnum+space
						out += ' ';
						out += c;
						out += ' ';
					}
					
				}else if(c == '|'&& (k+1) < chars_in.length)														// Check annotation separator [1st_Annot | 2nd_Annot]
				{
					if(	(Character.isAlphabetic(old) || Character.isSpaceChar(old) || Character.isDigit(old)) && 
						(Character.isAlphabetic(chars_in[k+1])|| Character.isSpaceChar(chars_in[k+1])) || Character.isDigit(chars_in[k+1]))
					{
						out += c;
					}else{
						DistributionProcessing.calcDistChar(errors, c);
					}
					
				}else{																								// DEFAUL add space+nonAlnum+space
					out += c;
				}
			}else{																									// Ignore all other signs
				DistributionProcessing.calcDistChar(errors, c);
			}
			old = c;
			
			if((k % 100000) == 0 && k > 0) System.out.println("Chars Passed: "+k);
		}
		return new String(out).trim();
	}	
	
	/**
	 * Just clean up multiple whitespace character in a String
	 * @param in
	 * @return
	 */
	public static String cleanMultiSpaces(String in)
	{
		return in.trim().replaceAll(" +", " ");
	}
	
	/**
	 * This method normalize a text and replace all non all non alpha numerics with dots.
	 * @param text
	 * @return text with dots instead of whitespace
	 */
	public static String normalizer(String text)
	{
		return Normalizer.normalize(text, Form.NFD).replaceAll("[^A-Za-z0-9]", ".").replaceAll("\\.+", " ").replace(" .", ".");
	}
	
	//##################################################################################
	//########################### GETTERS AND SETTERS ##################################
	//##################################################################################
	
	public HashMap<Character, Integer> getErrors() {
		return errors;
	}

	public void setErrors(HashMap<Character, Integer> errors) {
		this.errors = errors;
	}
}
