package simpleTextProcessing;

import java.text.BreakIterator;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import IOContent.TextReader;
import edu.stanford.nlp.util.StringUtils;

public class TextConversion 
{
	
	public static LinkedList<String> splitIntoWords(String text)
	{
		return new LinkedList<String>(Arrays.asList(text.split(" ")));
	}
	
	public static LinkedList<Character> splitIntoCharacters(String text)
	{
		LinkedList<Character> chars = new LinkedList<Character>();
		for (char c : text.toCharArray()) chars.add(c);
		return chars;
	}
	
	public static LinkedList<String> splitIntoSentencesBI(String text)
	{
		BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
		LinkedList<String> sentences = new LinkedList<String>();
		
		iterator.setText(text);
		int start = iterator.first();
		for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) 
		{
		  System.out.println(text.substring(start,end));
		  sentences.add(text.substring(start,end));
		}
		
		return sentences;
	}
	
	/**
	 * This method split a given text by the dots into sentences.
	 * @param text
	 * @return list of sentences
	 */
	public static LinkedList<String> splitIntoSentencesViaSubs(String text)
	{
		LinkedList<String> sentences = new LinkedList<String>();
		int start = 0;
		int end = text.indexOf('.');
		
		for(int i = 0; i < text.length(); i++)
		{
			if((text.indexOf('.', start)) < text.length() && start < end)
			{
				
				sentences.add(text.substring(start,end));
//				System.out.println(text.substring(start,end+1));
				start = end+1;
			}
			
			if((text.indexOf('.', start)) < text.length())
			{
				end = text.indexOf('.', start+1);
			}
		}		
		return sentences;
	}
	
	
	public static String decompose(String text)
	{
		char[] chars_in = text.toCharArray();
		String out = "";
		String punctutations = "':,.!-()?;\"[]";//add all the ones you want.
		char old = ' ';
		char c = ' ';
		
		for (int k = 0 ; k < chars_in.length; k++) 
		{
			c = chars_in[k];
			
			if(Character.isAlphabetic(c) || Character.isDigit(c) || Character.isSpaceChar(c))	// Check Letter, Digit and Whitespace
			{
				out += c;
			}else if(punctutations.contains(""+c))												// Check punctuation 
			{
				if(c == '.' && (Character.isAlphabetic(old)))									// Check previous is a letter and current is a dot
				{
					if(Character.isUpperCase(old))												// Check previous value is a upper case letter to keep cases like C.E.O. 
					{
						out += c;
					}else{																		// DEFAUL add space+nonAlnum+space
						out += ' ';
						out += c;
						out += ' ';
					}
				}else if((c == '[' || c == ']') && (k+1) < chars_in.length)						// Check current is [ or ] and next is not out of bounds
				{
					if(c == '[' && (old == '['|| chars_in[k+1] == '['))							// Check current structure is like [[
					{
						out += c;
					}else if(c == ']' && (old == ']'|| chars_in[k+1] == ']')){					// Check current structure is like ]]
						out += c;
					}else{																		// DEFAUL add space+nonAlnum+space
						out += ' ';
						out += c;
						out += ' ';
					}
					
				}else{																			// DEFAUL add space+nonAlnum+space
					out += ' ';
					out += c;
					out += ' ';
				}
			}else{																				// Ignore all other signs
				//TODO count the error signs
				out += ' ';
			}
			old = c;
		}
		return new String(out).trim();
	}	
	
	public static String normalizer(String text)
	{
		return Normalizer.normalize(text, Form.NFD).replaceAll("[^A-Za-z0-9]", ".").replaceAll("\\.+", " ").replace(" .", ".");
	}
	
	public static void main(String[] args )
	{
		String source = "This is a test. This is a T.L.A.(a) [test]. Now<with a Dr. (in it.";
//		splitIntoSentencesBI(source);
		
		
		String str1 ="C:/Users/Subadmin/Desktop/Deep LSTM/epoch- 70 Final.txt";
		String str2 = "C:/Users/Subadmin/Desktop/Bsp1.txt";
		String text = decompose(TextReader.fileReader(str2));

		System.out.println(text);
		
//		String[] test = text.split("\\s");
//		
//		for(String s : test){
//			System.out.println(s);
//		}
		
	}
}
