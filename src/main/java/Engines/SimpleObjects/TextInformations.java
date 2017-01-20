package Engines.SimpleObjects;

import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * This class contain all relevant informations about a processed text.
 * 
 * @author TTurke
 *
 */
public class TextInformations 
{
	//Store creation and resource info
	private String resource_name = "";
	private LocalDateTime generation_date;
	
	private double canBeUsed = Double.NaN;
	private HashMap<String, Double> metrics_GERBIL = new HashMap<String, Double>();				//USED
	private HashMap<String, Integer> words_distribution = new HashMap<String, Integer>();		//USED
	private HashMap<Integer, Integer> words_occurr_distr = new HashMap<Integer, Integer>();		//USED
	private HashMap<Integer, Integer> symbol_sent_dist = new HashMap<Integer, Integer>();		//USED
	private HashMap<Integer, Integer> annotation_dist = new HashMap<Integer, Integer>();		//USED
	private HashMap<String, Integer> syn_error_dist = new HashMap<String, Integer>();			//USED
	private HashMap<String, Integer> pos_tags_dist = new HashMap<String, Integer>();			//USED
	HashMap<Character, Integer> symbol_error_dist = new HashMap<Character, Integer>();			//USED
	
	/**
	 * This constructor store the original resource file name
	 * @param resource_name
	 */
	public TextInformations(String resource_name)
	{
		this.resource_name = resource_name;
		this.generation_date = Timestamp.getCurrentTime();
	}
	
	//##################################################################################
	//#################### METHODS FOR RESOURCE USE AND TIME ###########################
	//##################################################################################
	
	public LocalDateTime getGeneration_date() {
		return generation_date;
	}

	public String getResource_name() {
		return resource_name;
	}
	
	
	//##################################################################################
	//################## GETTERS, SETTERS, ADDERS and EDITS ############################
	//##################################################################################
	
	public double getCanBeUsed() {
		return canBeUsed;
	}

	public void setCanBeUsed(double canBeUsed) {
		this.canBeUsed = canBeUsed;
	}

	public HashMap<String, Double> getMetrics_GERBIL() {
		return metrics_GERBIL;
	}
	
	public HashMap<Integer, Integer> getAnnotation_dist() {
		return annotation_dist;
	}

	public void setAnnotation_dist(HashMap<Integer, Integer> annotation_dist) {
		this.annotation_dist = annotation_dist;
	}

	public void setMetrics_GERBIL(HashMap<String, Double> metrics_GERBIL) {
		this.metrics_GERBIL = metrics_GERBIL;
	}

	public HashMap<String, Integer> getWords_distribution() {
		return words_distribution;
	}

	public void setWords_distribution(HashMap<String, Integer> words_distribution) {
		this.words_distribution = words_distribution;
	}

	public HashMap<Integer, Integer> getWords_occurr_distr() {
		return words_occurr_distr;
	}

	public void setWords_occurr_distr(HashMap<Integer, Integer> words_occurr_distr) {
		this.words_occurr_distr = words_occurr_distr;
	}

	public HashMap<String, Integer> getSyn_error_dist() {
		return syn_error_dist;
	}

	public void setSyn_error_dist(HashMap<String, Integer> syn_error_dist) {
		this.syn_error_dist = syn_error_dist;
	}

	public HashMap<String, Integer> getPos_tags_dist() {
		return pos_tags_dist;
	}

	public void setPos_tags_dist(HashMap<String, Integer> pos_tags_dist) {
		this.pos_tags_dist = pos_tags_dist;
	}

	public HashMap<Character, Integer> getSymbol_error_dist() {
		return symbol_error_dist;
	}

	public void setSymbol_error_dist(HashMap<Character, Integer> symbol_error_dist) {
		this.symbol_error_dist = symbol_error_dist;
	}

	public HashMap<Integer, Integer> getSymbol_sent_dist() {
		return symbol_sent_dist;
	}

	public void setSymbol_sent_dist(HashMap<Integer, Integer> symbol_sent_dist) {
		this.symbol_sent_dist = symbol_sent_dist;
	}	
}
