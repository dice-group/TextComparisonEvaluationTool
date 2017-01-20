package Engines.simpleTextProcessing;

import java.util.HashMap;
import java.util.LinkedList;

import Engines.SimpleObjects.SentenceObject;

/**
 * This class handles the distribution processing e.g. collect and sum annotation distribution of a text.
 * @author TTurke
 *
 */
public class DistributionProcessing 
{
	
	/**
	 * This method use a list of sentence objects to create a HashMap to store the occurrence count of annotations in sentences, 
	 * and it sum the occurrence of sentences where similar count of annotations appear.
	 * @param sos
	 * @return annotation distribution as map
	 */
	public static HashMap<Integer, Integer> getAnnotDist(LinkedList<SentenceObject> sos)
	{
		HashMap<Integer, Integer> distribution = new HashMap<Integer, Integer>();
		
		for (int i = 0; i < sos.size(); i++) calcDistInteger(distribution, sos.get(i).getAnnot_count());
		return distribution;
	}
	
	/**
	 * This method calculate the distribution of words per sentence.
	 * @param sos
	 * @param sst
	 * @return Map of word count and the occurrence of this count value
	 */
	public static HashMap<Integer, Integer> getWPSDist(LinkedList<SentenceObject> sos, StanfordTokenizer st)
	{
		HashMap<Integer, Integer> distribution = new HashMap<Integer, Integer>();
		
		for (int i = 0; i < sos.size(); i++) calcDistInteger(distribution, st.gatherWords(sos.get(i).getSentence()).size());
		return distribution;
	}
	
	
	/**
	 * This class return a map of integer where each list element contain a key and a count value;
	 * @param sentences_raw
	 * @return Map of the syntactic error per sentence distribution
	 */
	public static HashMap<String, Integer> calcSimpleSynErrorDist(LinkedList<String> sentences_raw)
	{
		System.out.println("Simple syntax control for "+sentences_raw.size()+" sentences!");
		
		HashMap<String, Integer> syn_err_dist = new HashMap<String, Integer>(); 
		for (int i = 0; i < sentences_raw.size(); i++) 
		{
			char[] chars_in = sentences_raw.get(i).toCharArray();
			boolean first_char = TextConversion.scoreChar(chars_in[0], false);
			if(!first_char) calcDistString(syn_err_dist, "START_CHAR_ERROR");	
		}
		
		System.out.println("Simple syntax control done!");
		
		return syn_err_dist;
	}
	
	/**
	 * This method simply add a key to a given map and edit or create an entry into the map. 
	 * @param dist
	 * @param key
	 */
	public static void calcDistInteger(HashMap<Integer, Integer> dist, int key)
	{
		if(dist.size() < 1)
		{
			//fill empty map
			dist.put(key, 1);
		}else{
			if(dist.get(key) == null)
			{	
				//add new object
				dist.put(key, 1);
			}else{
				//raise count of known object
				dist.put(key, dist.get(key) + 1);
			}
		}
	}
	
	/**
	 * This method simply add a key to a given map and edit or create an entry into the map. 
	 * @param dist
	 * @param key
	 */
	public static void calcDistString(HashMap<String, Integer> dist, String key)
	{
		if(dist.size() < 1)
		{
			//fill empty map
			dist.put(key, 1);
		}else{
			if(dist.get(key) == null)
			{	
				//add new object
				dist.put(key, 1);
			}else{
				//raise count of known object
				dist.put(key, dist.get(key) + 1);
			}
		}
	}
	
	
	/**
	 * This method simply add a key to a given map and edit or create an entry into the map. 
	 * @param dist
	 * @param key
	 */
	public static void calcDistChar(HashMap<Character, Integer> dist, char key)
	{
		if(dist.size() < 1)
		{
			//fill empty map
			dist.put(key, 1);
		}else{
			if(dist.get(key) == null)
			{	
				//add new object
				dist.put(key, 1);
			}else{
				//raise count of known object
				dist.put(key, dist.get(key) + 1);
			}
		}
	}
	
	
}
