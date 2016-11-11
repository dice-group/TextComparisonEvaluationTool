package simpleTextProcessing;

import java.text.BreakIterator;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Set;

import IOContent.TextReader;

public class TextConversion 
{
	public static int error_signs = -1;
	public static Set<Character> errors;
	
	/**
	 * This method split a text or sentence by whitespace into words.
	 * @param text
	 * @return a list of words
	 */
	public static LinkedList<String> splitIntoWords(String text)
	{
		return new LinkedList<String>(Arrays.asList(text.split(" ")));
	}
	
	/**
	 * This method split a given text by the dots into sentences.
	 * The used technique are substrings.
	 * @param text
	 * @return a list of sentences
	 */
	public static LinkedList<String> splitIntoSentencesViaSubs(String text)
	{
		LinkedList<String> sentences = new LinkedList<String>();
		int end = text.indexOf('.');
		
		for(int start = 0; start < text.length(); start++)
		{
			
			if((text.indexOf('.', start)) < text.length() && start < end)
			{
				sentences.add(text.substring(start,end+1));
				if(end+2 > 0 && end+2 > start) start = end+2;
			}
			
			if(text.indexOf('.', start) < text.length() && text.indexOf('.', start) > 0)
			{
				end = text.indexOf('.', start+1);
				if(end < 0) end = text.length()-1;
			}
		}		
		return sentences;
	}
	
	/**
	 * This method decompose a text into characters and clean the error occurrences.
	 * The result is (hopefully) a (desired) text without error symbols in it. 
	 * That means no cryptic words. Keep in mind that this method is just for English text!
	 * @param text
	 * @return cleaned text
	 */
	public static String decompose(String text)
	{
		char[] chars_in = text.toCharArray();
		String out = "";
		String punctutations = "':,.!-()?;\"[]|";//add all the ones you want.
		char old = ' ';
		char c = ' ';
		error_signs = 0;
		errors = new HashSet<Character>();
		
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
						error_signs++;
						errors.add(c);
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
					error_signs++;
					errors.add(c);
					}else{																							// DEFAUL add space+nonAlnum+space
						out += ' ';
						out += c;
						out += ' ';
					}
					
				}else if(c == '|'&& (k+1) < chars_in.length)														// Check annotation separator [1st_Annot | 2nd_Annot]
				{
					if(Character.isAlphabetic(old) && Character.isAlphabetic(chars_in[k+1]))						// Check previous and next character is alphabetic depending on current
					{
						out += c;
					}else{																							// DEFAUL add space+nonAlnum+space
						error_signs++;
						errors.add(c);
						out += ' ';
					}
					
				}else{																								// DEFAUL add space+nonAlnum+space
					out += c;
					out += ' ';
				}
			}else{																									// Ignore all other signs
				error_signs++;
				errors.add(c);
				out += ' ';
			}
			old = c;
		}
		return new String(out).trim();
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
	
	//TODO das auftreten der error symbole sollte je symbol gezählt werden
	//TODO sentence creator bauen
	
	/*
	 * EXAMPLE/TESTCASE
	 */
	public static void main(String[] args )
	{		
		String str ="C:/Users/Subadmin/Desktop/BA AKSW/Deep LSTM/epoch- 70 Final.txt";
//		String str = "C:/Users/Subadmin/Desktop/BA AKSW/Testtexte bad/Bsp1.txt";
		String textRAW = TextReader.fileReader(str);
		String text = decompose(textRAW);

//		System.out.println("Input Text: "+textRAW);
//		System.out.println("Output Text: "+text);
		
		LinkedList<String> sentences = splitIntoSentencesViaSubs(text);
		System.out.println("Sentences Count: "+sentences.size());
		for(int k = 0; k < sentences.size(); k++) System.out.println((k+1)+" Sentence: "+sentences.get(k));
		
//		System.out.println("Count Errors: "+error_signs);
		
//		System.out.print("Error Signs: ");
//		for (Character s : errors) System.out.print(s+" ");
	}
}
