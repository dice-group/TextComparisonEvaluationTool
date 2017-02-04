package Engines.KLDiv;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import Engines.Distances.CosDistance;
import Engines.IO.PropReader;
import Engines.SimpleObjects.MetricVectorProcessing;

/**
 * This class calculate the Kullback-Leibler divergence in various ways.
 * @author TTurke
 *
 */
public class KullbackLeiblerDivergenz 
{	
	public static final double epsilon = 0.00000001;
	public static final double maximum = 1.00000001;
	
	//##################################################################################
	//################################# USAGE METHODS ##################################
	//##################################################################################
	
	/**
	 * This method takes 2 Maps <T, Double> of comparable distributions of 2 texts and calculate the Kullback-Leibler divergence.
	 * The 1st map should be the map containing the map with the desired key corpus. 
	 * Otherwise the method will reject the calculation.
	 * 
	 * @param s1
	 * @param s2
	 * @return divergence decimal
	 */
	public static <T> double EasyKLDivergenceTD(Map<T,Double> s1, Map<T,Double> s2)
	{
		if(isProbDist(s1) && isProbDist(s2) && haveAllowedKeyCorpus(s1, s2))
		{
			double kld = 0.0;
			
			for(T key : s1.keySet())
			{
				if(!s2.containsKey(key))
				{
					System.err.println("The 2nd map didn't apply the 1st map corpus! Key: "+key);
					return Double.NaN;
				}
				
				if(Math.abs(s2.get(key)) < CosDistance.epsilon)
				{
					System.err.println("The 2nd key value is smaller then desired error epsilon ("+epsilon+")! Key: "+key);
					System.out.println("Its needed to preserve Q(x) exist if P(x) > 0 !");
					return Double.NaN;
				}
				
				if(Math.abs(s1.get(key)) < CosDistance.epsilon) {continue;}
		        kld += (s1.get(key) * Math.log((s1.get(key)/s2.get(key))));
		        
//		        System.out.println("[Key: "+key+" | S1: "+s1.get(key)+" | S2: "+s2.get(key)+" | CALC: "+(s1.get(key) * Math.log((s1.get(key)/s2.get(key))))+" | KLD: "+kld);
			}
			return kld;
			
		}else{
			
			System.err.println("Distribution setup error has occurred in KL-Div calculation!");
			return 1.0;
		}
		
		
	}
	
	/**
	 * Check given distribution is a probability distribution. 
	 * @param dist
	 * @return true if it is a probability distribution
	 */
	public static <T> boolean isProbDist(Map<T,Double> dist)
	{
		double sum = 0.00000000;
		
		for(T key : dist.keySet()) sum += dist.get(key);
		
		if(Math.abs(sum) <  maximum){
			return true;
		}else{
			System.out.println("The map isn't a probability distribution! Sum Value ="+Math.abs(sum)+"!");
			return false;
		}
	}
	
	/**
	 * Check the keys corpus is equal. 
	 * If the merged set of the keys of both maps are bigger there own key set size, 
	 * the method will check that the 1st (map desired as the necessary corpus map) has lower value count than the 2nd map.
	 * This is necessary because the KL-Div need to perserve the Situation P(x) > 0 and Q(x) MUST exist!
	 * @param dist_1
	 * @param dist_2
	 * @return true if the keys corpora are equal
	 */
	public static <T> boolean haveAllowedKeyCorpus(Map<T,Double> dist_1, Map<T,Double> dist_2)
	{
		
		Set<T> allKeys = createFullKeySetT2D(dist_1, dist_2);
		
		if(dist_1.keySet().size() != dist_2.keySet().size() && dist_1.keySet().size() != allKeys.size())
		{
			
			if(dist_1.keySet().size() < dist_2.keySet().size() && dist_1.keySet().size() != allKeys.size())
			{
				return true;
			}else
			{
				System.out.println(	 "The 1st map (desired corpus) keyset is bigger then the 2nd map!\n"
									+"This wont work and is not allowed for the KL-Div calculation, \nbecause it ignore, if P(x) > 0 then Q(x) MUST exist!");
				System.out.println("1st key set: "+dist_1.keySet());
				System.out.println("2nd key set: "+dist_2.keySet());
				return false;
			}
	
		}else{
			return true;
		}
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
	public static void main(String[] args) throws IOException 
	{
		PropReader pr = new PropReader();

		String gold_mvp_path		= "WikipediaDumpGold_mvp.content.prop";
		String Frag_mvp_path		= "04.02.2017_mvp_BVFragment.content.prop";
		String eps15_mvp_path		= "04.02.2017_mvp_epoch15.content.prop";
		String eps30_mvp_path		= "04.02.2017_mvp_epoch30.content.prop";
		String eps70_mvp_path		= "04.02.2017_mvp_epoch70Final.content.prop";
		
		MetricVectorProcessing mvp_gold  = PropReader.fileReader(pr.getResourceFileAbsolutePath(gold_mvp_path),6);
		MetricVectorProcessing mvp_Frag  = PropReader.fileReader(pr.getResourceFileAbsolutePath(Frag_mvp_path),6);
		MetricVectorProcessing mvp_15Eps = PropReader.fileReader(pr.getResourceFileAbsolutePath(eps15_mvp_path),6);
		MetricVectorProcessing mvp_30Eps = PropReader.fileReader(pr.getResourceFileAbsolutePath(eps30_mvp_path),6);
		MetricVectorProcessing mvp_70Eps = PropReader.fileReader(pr.getResourceFileAbsolutePath(eps70_mvp_path),6);

		ArrayList<Double> v1 = MetricVectorProcessing.calcDistanceVector(mvp_gold, mvp_Frag);
		ArrayList<Double> v2 = MetricVectorProcessing.calcDistanceVector(mvp_gold, mvp_15Eps);
		ArrayList<Double> v3 = MetricVectorProcessing.calcDistanceVector(mvp_gold, mvp_30Eps);
		ArrayList<Double> v4 = MetricVectorProcessing.calcDistanceVector(mvp_gold, mvp_70Eps);
		
		System.out.println("Dist-V: "+v1+" \t\t| \tRating: "+MetricVectorProcessing.rate(v1));
		System.out.println("Dist-V: "+v2+" \t| \tRating: "+MetricVectorProcessing.rate(v2));
		System.out.println("Dist-V: "+v3+" \t| \tRating: "+MetricVectorProcessing.rate(v3));
		System.out.println("Dist-V: "+v4+" \t\t| \tRating: "+MetricVectorProcessing.rate(v4));
		
		
		
		
//		Map<String, Double> text1Values = new HashMap<String, Double>();
//		Map<String, Double> text2Values = new HashMap<String, Double>();
//		
//		//EXAMPLE 1
//		text1Values.put("av", 0.12);		text2Values.put("av", 0.149);
//		text1Values.put("occ", 0.08);		text2Values.put("occ", 0.079);
//		text1Values.put("wc", 0.6);			text2Values.put("wc", 0.3);
//		text1Values.put("len", 0.17);		//text2Values.put("len", 0.17);
//		text1Values.put("err", 0.03);		text2Values.put("err", 0.032);
//		
//		//Bsp result ist -1.8053105026064107 [WORKS]
//		System.out.println("Die KL-Divergenz Easy TD beträgt:\t"+EasyKLDivergenceTD(text2Values, text1Values));
//		
//		
//		//NEW EXAMPLE 2
//		text1Values = new HashMap<String, Double>();
//		text2Values = new HashMap<String, Double>();
//		
//		//Bsp result ist 0.19 und mein result ist 0.19274475702175753 [WORKS]
//		text1Values.put("a", 0.2);		text2Values.put("a", 0.5);
//		text1Values.put("b", 0.8);		text2Values.put("b", 0.5);
//		
//		System.out.println("Die KL-Divergenz Easy TD beträgt:\t"+EasyKLDivergenceTD(text1Values, text2Values));
//		
//		//NEW EXAMPLE 3
//		text1Values = new HashMap<String, Double>();
//		text2Values = new HashMap<String, Double>();
//		
//		//Bsp result ist 0.001547 und mein result ist 0.0015468691570075686 [WORKS]
//		text1Values.put("a", 0.005);		text2Values.put("a", 0.01);
//		text1Values.put("b", 0.995);		text2Values.put("b", 0.99);
//		
//		System.out.println("Die KL-Divergenz Easy TD beträgt:\t"+EasyKLDivergenceTD(text1Values, text2Values));
//		
//		//Bsp result 0.0 [WORKS]
//		text1Values.put("a", 0.000);		text2Values.put("a", 0.000);
//		text1Values.put("b", 0.000);		text2Values.put("b", 0.000);
//				
//		System.out.println("Die KL-Divergenz Easy TD beträgt:\t"+EasyKLDivergenceTD(text1Values, text2Values));
				
				
		
	}
}
