package Engines.KLDiv;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import Engines.Distances.CosDistance;

/**
 * This class calculate the Kullback-Leibler divergence in various ways.
 * @author TTurke
 *
 */
public class KullbackLeiblerDivergenz 
{	
	
	//##################################################################################
	//################################# USAGE METHODS ##################################
	//##################################################################################
	
	/**
	 * This method takes 2 Maps <T, Double> of comparable distributions of 2 texts 
	 * and calculate the Kullback-Leibler divergence 
	 * @param s1
	 * @param s2
	 * @return divergence decimal
	 */
	public static <T> double EasyKLDivergenceTD(Map<T,Double> s1, Map<T,Double> s2)
	{
		Set<T> all_keys = createFullKeySetT2D(s1, s2);
		double kld = 0.0;
		
		for(T key : all_keys)
		{
			if(s1.get(key) == null || s2.get(key) == null || Math.abs(s1.get(key)) < CosDistance.epsilon || Math.abs(s2.get(key)) < CosDistance.epsilon) {continue;}
	        kld += (s1.get(key) * Math.log((s1.get(key)/s2.get(key))));
		}
		return kld;
	}
	
	/**
	 * This method calculate the full key set for 2 Maps <T, Double>
	 * @param s1
	 * @param s2
	 * @return key set <T>
	 */
	public static <T> Set<T> createFullKeySetT2D(Map<T,Double> s1, Map<T,Double> s2)
	{
		Set<T> all_keys = new HashSet<T>(); 
		
		for(T key : s1.keySet()) all_keys.add(key);
		for(T key : s2.keySet()) all_keys.add(key);
		
		return all_keys;
	}
	
	//##################################################################################
	//#################################### EXAMPLE #####################################
	//##################################################################################
	
	/*
	 * EXAMPLES [WORKS]
	 */
	public static void main(String[] args) 
	{
		Map<String, Double> text1Values = new HashMap<String, Double>();
		Map<String, Double> text2Values = new HashMap<String, Double>();

		//EXAMPLE 1
		text1Values.put("av", 0.12);		text2Values.put("av", 0.149);
		text1Values.put("occ", 0.08);		text2Values.put("occ", 0.079);
		text1Values.put("wc", 0.6);			text2Values.put("wc", 0.3);
		text1Values.put("len", 0.17);		//text2Values.put("len", 0.17);
		text1Values.put("err", 0.03);		text2Values.put("err", 0.032);
		
		//Bsp result ist -1.8053105026064107 [WORKS]
		System.out.println("Die KL-Divergenz Easy TD beträgt:\t"+EasyKLDivergenceTD(text2Values, text1Values));
		
		
		//NEW EXAMPLE 2
		text1Values = new HashMap<String, Double>();
		text2Values = new HashMap<String, Double>();
		
		//Bsp result ist 0.19 und mein result ist 0.19274475702175753 [WORKS]
		text1Values.put("a", 0.2);		text2Values.put("a", 0.5);
		text1Values.put("b", 0.8);		text2Values.put("b", 0.5);
		
		System.out.println("Die KL-Divergenz Easy TD beträgt:\t"+EasyKLDivergenceTD(text1Values, text2Values));
		
		//NEW EXAMPLE 3
		text1Values = new HashMap<String, Double>();
		text2Values = new HashMap<String, Double>();
		
		//Bsp result ist 0.001547 und mein result ist 0.0015468691570075686 [WORKS]
		text1Values.put("a", 0.005);		text2Values.put("a", 0.01);
		text1Values.put("b", 0.995);		text2Values.put("b", 0.99);
		
		System.out.println("Die KL-Divergenz Easy TD beträgt:\t"+EasyKLDivergenceTD(text1Values, text2Values));
		
		//Bsp result 0.0 [WORKS]
		text1Values.put("a", 0.000);		text2Values.put("a", 0.000);
		text1Values.put("b", 0.000);		text2Values.put("b", 0.000);
				
		System.out.println("Die KL-Divergenz Easy TD beträgt:\t"+EasyKLDivergenceTD(text1Values, text2Values));
				
				
		
	}
}
