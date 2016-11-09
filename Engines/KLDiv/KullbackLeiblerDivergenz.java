package KLDiv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.stanford.nlp.stats.Counter;
import edu.stanford.nlp.stats.Distribution;
import StanfordNLPDistributions.Distributions;

public class KullbackLeiblerDivergenz 
{
	public static final double log2 = Math.log(2); // log2(x) = ln(x) = (log(x)/log(2))
	
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
		        
		        kld += Math.abs(s1.get(key) * ln((s1.get(key)/s2.get(key))));
			}
			
			return kld;
		}
		return Double.NaN;
	}
	
	
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
		        
		        kld += Math.abs(s1.get(i) * ln((s1.get(i)/s2.get(i))));
			}
			return kld;
		}
		return Double.NaN;
	}
	
	/**
	 * 	If the argument is NaN or less than -1, then the result is NaN. 
	 *	If the argument is positive infinity, then the result is positive infinity. 
	 *	If the argument is negative one, then the result is negative infinity. 
	 *	If the argument is zero, then the result is a zero with the same sign as the argument.
	 *
	 * @param value
	 * @return the ln of value
	 */
	public static double ln(double value)
	{
		double result = -1.0;
		
		
		if(value == Double.NaN || value < -1)
		{
			result = Double.NaN;
		}else if(value == Double.POSITIVE_INFINITY)
		{
			result = Double.POSITIVE_INFINITY;
		}else if(value == -1)
		{
			result = Double.NEGATIVE_INFINITY;
		}else if(value == 0)
		{
			result = value;
		}else{
			result = (Math.log(value)/(Math.log(2)));
		}
		return result;
	}
	
	
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		
		Set<String> textKeys = new HashSet<String>();
		Map<String, Double> text1Values = new HashMap<String, Double>();
		Map<String, Double> text2Values = new HashMap<String, Double>();

		textKeys.addAll(Arrays.asList("av", "occ", "wc", "len", "err"));
		text1Values.put("av", 0.5);			text2Values.put("av", 0.5);
		text1Values.put("occ", 0.3);		text2Values.put("occ", 0.4);
		text1Values.put("wc", 0.3);			text2Values.put("wc", 0.3);
		text1Values.put("len", 0.7);		text2Values.put("len", 0.7);
		text1Values.put("err", 0.8);		text2Values.put("err", 0.9);
		
		System.out.println("Die KL-Divergenz beträgt: "+KLDivergenceMS(textKeys, text1Values, text2Values));
		
		//0.15293250129808114
	}

}
