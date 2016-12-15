package Engines.simpleTextProcessing;

import java.util.HashMap;
import java.util.LinkedList;

import Engines.Enums.Language;
import Engines.SimpleObjects.FrequencySorting;
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
		
		for (int i = 0; i < sos.size(); i++) 
		{
			int key = sos.get(i).getAnnot_count();
			
			if(distribution.size() < 1)
			{
				//fill empty map
				distribution.put(key, 1);
			}else{
				if(distribution.get(key) == null && key > 0)
				{	
					//add new object
					distribution.put(key, 1);
				}else{
					if(key > 0)
					{
						//raise count of known object
						distribution.put(key, distribution.get(key) + 1);
					}
				}
			}
		}
		
		return distribution;
	}
	
	/**
	 * This method calculate the distribution of words per sentence.
	 * @param sos
	 * @param sst
	 * @param language
	 * @return Map of word count and the occurrence of this count value
	 */
	public static HashMap<Integer, Integer> getWPSDist(LinkedList<SentenceObject> sos, StanfordSegmentatorTokenizer sst, Language language)
	{
		HashMap<Integer, Integer> distribution = new HashMap<Integer, Integer>();
		
		for (int i = 0; i < sos.size(); i++) 
		{
			int key = sst.gatherWords(sos.get(i).getSentence(), language).size();
			
			calcDist(distribution, key);
			
//			if(distribution.size() < 1)
//			{
//				//fill empty map
//				distribution.put(key, 1);
//			}else{
//				if(distribution.get(key) == null && key > 0)
//				{	
//					//add new object
//					distribution.put(key, 1);
//				}else{
//					if(key > 0)
//					{
//						//raise count of known object
//						distribution.put(key, distribution.get(key) + 1);
//					}
//				}
//			}
		}
		
		return distribution;
	}
	
	
	/**
	 * This class return a sorted list of integer array where each list element contain a key and a count value;
	 * The key is the number of errors inside a single sentence and the count value 
	 * is the occurrence of this error count over all sentences.
	 * @param sentences_raw
	 * @param language
	 * @return Sorted List of the syntactic error per sentence distribution
	 */
	public static LinkedList<int[]> calcSimpleSynErrorDist(LinkedList<String> sentences_raw, Language language)
	{
		HashMap<Integer, Integer> syn_err_dist = new HashMap<Integer, Integer>(); 
		
		for (int i = 0; i < sentences_raw.size(); i++) 
		{
			char[] chars_in = sentences_raw.get(i).toCharArray();
			boolean first_char = TextConversion.scoreChar(chars_in[0], false);
			boolean last_char = TextConversion.scoreChar(chars_in[chars_in.length-1], true);
			int key = 0;
			
			if(!first_char) key++;
			if(!last_char) key++;
			
			calcDist(syn_err_dist, key);	
		}
		return FrequencySorting.sortDist(syn_err_dist);
	}
	
	/**
	 * This method simply add a key to a given map and edit or create an entry into the map. 
	 * @param dist
	 * @param key
	 */
	public static void calcDist(HashMap<Integer, Integer> dist, int key)
	{
		if(dist.size() < 1)
		{
			//fill empty map
			dist.put(key, 1);
		}else{
			if(dist.get(key) == null && key > 0)
			{	
				//add new object
				dist.put(key, 1);
			}else{
				if(key > 0)
				{
					//raise count of known object
					dist.put(key, dist.get(key) + 1);
				}
			}
		}
	}
	
}
