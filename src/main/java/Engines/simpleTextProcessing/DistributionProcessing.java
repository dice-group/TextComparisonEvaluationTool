package Engines.simpleTextProcessing;

import java.util.HashMap;
import java.util.LinkedList;

import Engines.Enums.Language;
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
		LinkedList<String> words;
		
		for (int i = 0; i < sos.size(); i++) 
		{
			int key = sst.gatherWords(sos.get(i).getSentence(), language).size();
			
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
}
