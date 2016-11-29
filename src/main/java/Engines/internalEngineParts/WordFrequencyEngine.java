package Engines.internalEngineParts;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import Engines.Enums.Language;
import Engines.simpleTextProcessing.*;

/**
 * This class handle the word occurrence, frequency and the frequency percentage calculation. 
 * @author TTurke
 *
 */
public class WordFrequencyEngine 
{

	private HashMap<String, Integer> map = null;
	private HashSet<String> set = null;
	
	public WordFrequencyEngine()
	{
		map = new HashMap<String, Integer>();
		set = new HashSet<String>();
	}

	/**
	 * This method count word frequency inside a given text and store the informations inside the objects
	 * global map variable.
	 * @param text
	 * @param sst
	 * @param language
	 */
	public void gatherWordFrequencyByText(String text, StanfordSegmentatorTokenizer sst, Language language)
	{
		List<String> words = sst.gatherWords(text, language);
		
		boolean was_added = false;
		
		if(words.isEmpty() && words.size() < 1)
		{
			System.err.println("No text given! NullPointer in class WordFrequencyEngine.gatherWordFrequency(input)!");
			System.exit(0);
		}
		
		for(String current : words)
		{
			if(!StringUtils.isBlank(current) && !current.contains("RSB") && !current.contains("LSB"))
			{
				was_added = set.add(current); 
			}
			
			if(was_added && !StringUtils.isBlank(current) && !current.contains("RSB") && !current.contains("LSB"))
			{
				map.put(current, 1);
			}else{
				
				if(!StringUtils.isBlank(current) && !current.contains("RSB") && !current.contains("LSB"))
				{
					map.put(current, map.getOrDefault(current, 0) + 1);
				}
			}
		}	
	}
	
	/**
	 * This method count word frequency inside a given list of words and store the informations inside the objects
	 * global map variable.
	 * @param words
	 */
	public void gatherWordFrequencyByList(List<String> words)
	{		
		boolean was_added = false;
		
		if(words.isEmpty() && words.size() < 1)
		{
			System.err.println("No text given! NullPointer in class WordFrequencyEngine.gatherWordFrequency(input)!");
			System.exit(0);
		}
		
		for(String current : words)
		{
			if(!StringUtils.isBlank(current) && !current.contains("RSB") && !current.contains("LSB"))
			{
				was_added = set.add(current); 
			}
			
			if(was_added && !StringUtils.isBlank(current) && !current.contains("RSB") && !current.contains("LSB"))
			{
				map.put(current, 1);
			}else{
				
				if(!StringUtils.isBlank(current) && !current.contains("RSB") && !current.contains("LSB"))
				{
					map.put(current, map.getOrDefault(current, 0) + 1);
				}
			}
		}	
	}
	
	/**
	 * This method check size equality between a HashMap and HashSet depending on there KeySet's.
	 * @param map
	 * @param set
	 * @return true or false
	 */
	public boolean sizeEqualitySetMap(HashMap<?, ?> map, HashSet<?> set)
	{
		return (map.size() == set.size());
	}
	
	/**
	 * This method check size equality between two HashMap's depending on there KeySet's.
	 * @param map1
	 * @param map2
	 * @return true or false
	 */
	public boolean sizeEqualityMaps(HashMap<?, ?> map1 , HashMap<?, ?> map2)
	{
		return (map1.size() == map2.size());
	}
	
	/**
	 * This mehtod count the words in a HashMap by the value of a key.
	 * @param hashmap
	 * @return element count
	 */
	public static int getElementCount(HashMap<String, Integer> hashmap)
	{
		int i = 0;
		
		for(String elem : hashmap.keySet())
		{
			i += hashmap.get(elem);
		}
		return i;
	}
	
	/**
	 * This method calculate the word appearance percentage in a text by using a HashMap with the word count and the number of words in the text.
	 * @param hashmap
	 * @return HashMap with the percentages
	 */
	public HashMap<String, Double> wordAppearancePercentage(HashMap<String, Integer> hashmap, int word_count)
	{
		double percantage;
		HashMap<String, Double> perc_map = new HashMap<String, Double>();
		
		for(String elem : hashmap.keySet())
		{
			percantage = ( (hashmap.get(elem)*1.0) / (word_count*1.0)* 100.0);
			perc_map.put(elem, percantage);
		}
		return perc_map;
	}
	
	//################### GETTERS AND SETTERS ###################
	
	public HashMap<String, Integer> getMap() {
		return map;
	}

	public void setMap(HashMap<String, Integer> map) {
		this.map = map;
	}

	public HashSet<String> getSet() {
		return set;
	}

	public void setSet(HashSet<String> set) {
		this.set = set;
	}
}
