package Engines.internalEngineParts;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import org.apache.commons.lang3.StringUtils;

import Engines.simpleTextProcessing.*;

public class WordFrequencyEngine 
{

	private HashMap<String, Integer> map = null;
	private HashSet<String> set = null;
	public static int word_count = 0;
	
	public WordFrequencyEngine()
	{
		map = new HashMap<String, Integer>();
		set = new HashSet<String>();
	}

	
	public void gatherWordFrequency(String text)
	{
		LinkedList<String> words = TextConversion.splitIntoWords(TextConversion.normalizer(text));
		
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
	
	public boolean sizeEqualitySetMap(HashMap<?, ?> map, HashSet<?> set)
	{
		return (map.size() == set.size());
	}
	
	public boolean sizeEqualityMaps(HashMap<?, ?> map1 , HashMap<?, ?> map2)
	{
		return (map1.size() == map2.size());
	}
	
	public static int getElementCount(HashMap<String, Integer> hashmap)
	{
		int i = 0;
		
		for(String elem : hashmap.keySet())
		{
			i += hashmap.get(elem);
		}
		return i;
	}
	
	public HashMap<String, Double> wordAppearancePercentage(HashMap<String, Integer> hashmap)
	{
		word_count = getElementCount(hashmap);
		double percantage;
		HashMap<String, Double> perc_map = new HashMap<String, Double>();
		
		for(String elem : hashmap.keySet())
		{
			percantage = ( (hashmap.get(elem)*1.0) / (word_count*1.0)* 100.0);
			perc_map.put(elem, percantage);
			
//			System.out.println(elem+" | "+hashmap.get(elem)+" | "+percantage);
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
