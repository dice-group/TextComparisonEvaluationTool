package Engines.SimpleObjects;

import java.util.HashMap;
import java.util.Map;

import Engines.Enums.Distributions;
import Engines.internalEngineParts.WordFrequencyEngine;
import Engines.simpleTextProcessing.DistributionProcessing;

public class Corpus {

	public static double epsilon = 0.00000001;
	//Non_Gerbil metrics
	private HashMap<Integer, Double> symbol_sent_dist;
	private HashMap<Character, Double> symbol_error_dist;
	private HashMap<String, Double> syntactic_error_dist;
	private HashMap<Integer, Double> word_occurrence_dist;
	private HashMap<String, Double> pos_tags_dist;
	private HashMap<Integer, Double> annotated_entity_dist;
	private HashMap<String, Double> gerbil_metrics;
	
	public Corpus(MetricVectorProcessing gold_corpus)
	{
		this.symbol_error_dist = gold_corpus.symbol_error_dist;			/*M_1*/
		this.symbol_sent_dist = gold_corpus.symbol_sent_dist;			/*M_2*/
		this.syntactic_error_dist = gold_corpus.syntactic_error_dist;	/*M_3*/
		this.word_occurrence_dist = gold_corpus.word_occurrence_dist;	/*M_4*/
		this.pos_tags_dist = gold_corpus.pos_tags_dist;					/*M_5*/
		this.annotated_entity_dist = gold_corpus.annotated_entity_dist;	/*M_6*/
		this.gerbil_metrics = gold_corpus.gerbil_metrics;				/*M_GERBIL*/
	}
		
	
	/**
	 * This method is the general corpus probability distribution calculation.
	 * @param distribution
	 * @return HashMap with the percentages
	 */
	public static <T> HashMap<T, Double> calcCorpProbDist(HashMap<T, Double> corpus, HashMap<T, Integer> distribution)
	{
		HashMap<T, Double> acceptable;
		
		for(T key : corpus.keySet()) 
			if(!distribution.containsKey(key) || (distribution.containsKey(key) && distribution.get(key) == 0)) 
				DistributionProcessing.calcDist(distribution, key);
		
		acceptable = WordFrequencyEngine.calcProbabilityDistribution(distribution);
		
		return acceptable;
	}
	
	/**
	 * This add to missing keys or change key with zero value to a minimum value.
	 * @param corpus
	 * @param distribution
	 * @return corrected map
	 */
	public static <T> HashMap<T, Double> calcCorpGERBILProbDist(HashMap<T, Double> corpus, HashMap<T, Double> distribution)
	{
		for(T key : corpus.keySet()) 
			if(!distribution.containsKey(key) || (distribution.containsKey(key) && (Math.abs(distribution.get(key)) < epsilon))) 
				distribution.put(key, epsilon);
		return distribution;
	}
	
	
	/**
	 * This method calculate the desired corpus probability distribution.
	 * @param dist
	 * @param corpus
	 * @param distribution
	 * @return corrected map
	 */
	public static <T> HashMap<T, Double> createCorpProbDist(Distributions dist, Corpus corpus, HashMap<T, Integer> distribution)
	{		
		//TODO ATTENTION: this need to be handled with caution. Only use if you know your types of T matches!
		
		if(dist == Distributions.SymbolCount)
		{
			return calcCorpProbDist((HashMap<T, Double>)corpus.getSymbol_sent_dist(), distribution);
		}else if(dist == Distributions.SymbolErr)
		{
			return calcCorpProbDist((HashMap<T, Double>)corpus.getSymbol_error_dist(), distribution);
		}else if(dist == Distributions.SyntaxErr)
		{
			return calcCorpProbDist((HashMap<T, Double>)corpus.getSyntactic_error_dist(), distribution);
		}else if(dist == Distributions.WordOccur)
		{
			return calcCorpProbDist((HashMap<T, Double>)corpus.getWord_occurrence_dist(), distribution);
		}else if(dist == Distributions.PosTags)
		{
			return calcCorpProbDist((HashMap<T, Double>)corpus.getPos_tags_dist(), distribution);
		}else if(dist == Distributions.AnnotEntity)
		{
			return calcCorpProbDist((HashMap<T, Double>)corpus.getAnnotated_entity_dist(), distribution);
		}else{
			System.err.println("Unknown distribution type");
			return null;
		}
	}
	
	/**
	 * This method calculate the desired corpus probability distribution for the GERBIL metrics.
	 * @param dist
	 * @param corpus
	 * @param distribution
	 * @return corrected GERBIL map
	 */
	public static HashMap<String, Double> createCorpProbDistGERBIL(Corpus corpus, HashMap<String, Double> distribution)
	{		
		return calcCorpGERBILProbDist(corpus.gerbil_metrics, distribution);
	}
	
	public HashMap<Integer, Double> getSymbol_sent_dist() {
		return symbol_sent_dist;
	}



	public HashMap<Character, Double> getSymbol_error_dist() {
		return symbol_error_dist;
	}



	public HashMap<String, Double> getSyntactic_error_dist() {
		return syntactic_error_dist;
	}



	public HashMap<Integer, Double> getWord_occurrence_dist() {
		return word_occurrence_dist;
	}



	public HashMap<String, Double> getPos_tags_dist() {
		return pos_tags_dist;
	}



	public HashMap<Integer, Double> getAnnotated_entity_dist() {
		return annotated_entity_dist;
	}



	public HashMap<String, Double> getGerbil_metrics() {
		return gerbil_metrics;
	}
	
	/*
	 * EXAMPLE of USE
	 */
	public static <T> void main(String[] args)
	{
		Map<String, Double> gold = new HashMap<String, Double>();
		Map<String, Integer> text2Values = new HashMap<String, Integer>();
		Map<String, Double> text3Values = new HashMap<String, Double>();

		//EXAMPLE 1
		gold.put("av", 0.12);		text2Values.put("av", 14);			//text3Values.put("av", 0.12);
		gold.put("occ", 0.08);		text2Values.put("occ", 12);			text3Values.put("occ", 0.0);
		gold.put("wc", 0.6);			text2Values.put("wc", 0);			text3Values.put("wc", 0.212);
		gold.put("len", 0.17);		//text2Values.put("len", 0.17);		text3Values.put("len", 0.12);
		gold.put("err", 0.03);		text2Values.put("err", 5);			text3Values.put("err", 0.10);
		
		Map<String, Double> result = (Map<String, Double>) Corpus.calcCorpProbDist((HashMap<T, Double>)gold, (HashMap<T, Integer>)text2Values);
		System.out.println();
		for(String key : result.keySet()) System.out.println("["+key+"]["+result.get(key)+"]");
		
		Map<String, Double> gresult = (Map<String, Double>) calcCorpGERBILProbDist((HashMap<T, Double>)gold, (HashMap<T, Double>)text3Values);
		System.out.println();
		for(String key : gresult.keySet()) System.out.println("["+key+"]["+gresult.get(key)+"]");
	}
}
