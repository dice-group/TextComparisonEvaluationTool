package Engines.internalEngineParts;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

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
	 * This method calculate the probability distribution of a simple distribution.
	 * @param distribution
	 * @return HashMap with the percentages
	 */
	public static <T> HashMap<T, Double> calcProbabilityDistribution(HashMap<T, Integer> distribution)
	{
		int count = 0;
		double percantage;
		HashMap<T, Double> prop_dist = new HashMap<T, Double>();
		
		//get the key values sum 
		for(Integer i : distribution.values()) count += i;
		
		//calc propability distribution
		for(T elem : distribution.keySet())
		{
			percantage = ((distribution.get(elem)*1.0) / (count*1.0)* 100.0);
			prop_dist.put(elem, percantage);
		}
		
		return prop_dist;
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
