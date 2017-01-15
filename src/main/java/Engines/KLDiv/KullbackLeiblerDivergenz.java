package Engines.KLDiv;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class KullbackLeiblerDivergenz 
{	
	static double epsilon = 0.00000001;
	
	/**
	 * This method takes 2 Maps (for exam. HashMap) of comparable distributions of 2 texts and a set of key names depending on 
	 * for exam. distribution step labels.
	 * @param sharedKeys
	 * @param s1
	 * @param s2
	 * @return kl divergence
	 */
	public static double KLDivergenceMS(Set<String> sharedKeys, Map<String,Double> s1, Map<String,Double> s2)
	{
		if(sharedKeys.size()>= s1.keySet().size() && sharedKeys.size()>= s2.keySet().size())
		{			
			double kld = 0.0;
			
			for(String key : sharedKeys)
			{
				if(s1.get(key) == null || s2.get(key) == null) {continue;}
				if (s1.get(key) == 0) {continue;}
		        if (s2.get(key) == 0.0000) {continue;}
		        
		        kld += (s1.get(key) * Math.log((s1.get(key)/s2.get(key))));
			}
			
			return kld;
		}
		return Double.NaN;
	}
	
	/**
	 * This method takes 2 lists of comparable distributions of 2 texts.
	 * @param s1
	 * @param s2
	 * @return the kl divergence
	 */
	public static double KLDivergencePreOrderedLists(List<Double> s1, List<Double> s2)
	{
		// both lists have the containing values sorted the same like having 
		// a invisible shared key list over both of them
		
		if(s2.size() >=  s1.size())
		{			
			double kld = 0.0;
			
			for(int i = 0; i < s1.size(); i++)
			{
				if (s1.get(i) == 0) {continue;}
		        if (s2.get(i) == 0.0) {continue;}
		        
		        kld += s1.get(i) * Math.log((s1.get(i)/s2.get(i)));
			}
			return kld;
		}
		return Double.NaN;
	}
	
	
	//TODO KL Div kritisch => value kicken oder halten?
	public static double KLDivergenceMSIN(Map<String,Integer> s1, Map<String,Integer> s2)
	{
		Set<String> all_keys = new HashSet<String>(); 
				
		for(String key : s1.keySet()) all_keys.add(key);
		for(String key : s2.keySet()) all_keys.add(key);
		
		double kld = 0.0, v1 = Double.NaN, v2 = Double.NaN;
		
		for(String key : all_keys)
		{
	        if(s1.containsKey(key))
	        {
	        	if (s1.get(key) == 0) 
	        	{
	        		v1 = epsilon;
	        	}else
	        	{
	        		v1 = s1.get(key);
	        	}
	        	
	        }else{
	        	v1 = epsilon;
	        }
	        
	        if(s2.containsKey(key))
	        {
	        	 if (s2.get(key) == 0) 
	        	 {
	        		 v2 = epsilon;
	        	 }else
	        	 {
	        		 v2 = s2.get(key);
	        	 }
	        	
	        }else{
	        	v2 = epsilon;
	        }

	        System.out.println("Key: "+key);
	        System.out.println("From: "+v1);
	        System.out.println("To: "+v2);
	        System.out.println("KL = "+(v1 * Math.log(v1/v2)));
	        System.out.println();
	        
	        kld += (v1 * Math.log(v1/v2));
	        
	        //Reset
	        v1 = Double.NaN;
	        v2 = Double.NaN;
		}
		
		return kld;
	}
	
	
	/*
	 * EXAMPLES [WORKS]
	 */
	public static void main(String[] args) 
	{
		Set<String> textKeys = new HashSet<String>();
		Map<String, Double> text1Values = new HashMap<String, Double>();
		Map<String, Double> text2Values = new HashMap<String, Double>();
		Map<String, Integer> textValuesA = new HashMap<String, Integer>();
		Map<String, Integer> textValuesB = new HashMap<String, Integer>();

		//EXAMPLE 1
		textKeys.addAll(Arrays.asList("av", "occ", "wc", "len", "err"));
		text1Values.put("av", 5.0);			text2Values.put("av", 5.0);
		text1Values.put("occ", 3.0);		text2Values.put("occ", 4.0);
		text1Values.put("wc", 3.0);			text2Values.put("wc", 3.0);
		text1Values.put("len", 7.0);		text2Values.put("len", 0.0);
		text1Values.put("err", 8.0);		text2Values.put("err", 9.0);
		
		//EXAMPLE 1
		textValuesA.put("av", 5);		textValuesB.put("av", 5);
		textValuesA.put("occ", 3);		textValuesB.put("occ", 4);
		textValuesA.put("wc", 3);		textValuesB.put("wc", 3);
		textValuesA.put("len", 0);		textValuesB.put("len", 0);
		textValuesA.put("err", 8);		textValuesB.put("err", 9);
		
		System.out.println("Die KL-Divergenz SDO beträgt: "+KLDivergenceMS(textKeys, text1Values, text2Values));
		System.out.println();
		System.out.println("Die KL-Divergenz SIN beträgt: "+KLDivergenceMSIN(textValuesA, textValuesB));
		
		
		//NEW EXAMPLE 2
		textKeys = new HashSet<String>();
		text1Values = new HashMap<String, Double>();
		text2Values = new HashMap<String, Double>();
		
		//Bsp result ist 0.19 und mein result ist 0.19274475702175753 [WORKS]
		textKeys.addAll(Arrays.asList("a", "b"));
		text1Values.put("a", 0.2);		text2Values.put("a", 0.5);
		text1Values.put("b", 0.8);		text2Values.put("b", 0.5);
		
		System.out.println("Die KL-Divergenz beträgt: "+KLDivergenceMS(textKeys, text1Values, text2Values));
		
		//NEW EXAMPLE 3
		textKeys = new HashSet<String>();
		text1Values = new HashMap<String, Double>();
		text2Values = new HashMap<String, Double>();
		
		//Bsp result ist 0.001547 und mein result ist 0.0015468691570075686 [WORKS]
		textKeys.addAll(Arrays.asList("a", "b"));
		text1Values.put("a", 0.005);		text2Values.put("a", 0.01);
		text1Values.put("b", 0.995);		text2Values.put("b", 0.99);
		
		System.out.println("Die KL-Divergenz beträgt: "+KLDivergenceMS(textKeys, text1Values, text2Values));
		
		//0.15293250129808114
	}
}
