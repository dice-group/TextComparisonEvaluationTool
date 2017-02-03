package Engines.simpleTextProcessing;

import java.text.Normalizer;
import java.text.Normalizer.Form;

/**
 * This class contain a text decomposer which try to filter all not desired types of characters out of a String.
 * @author TTurke
 *
 */
public class TextConversion 
{
	//#############################################################################
	//############################ USAGE METHODS ##################################
	//#############################################################################
	
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
}
