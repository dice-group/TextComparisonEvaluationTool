package Engines.SimpleObjects;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
	
	private double symbol_per_sentence = 0.0;		//USED
	private double symbol_per_sentence_no_ws = 0.0;	//USED
	private double word_per_sentence = 0.0;			//USED
	
	private int sentence_count = 0;					//USED
	private int word_count = 0;						//USED
	private int symbol_count = 0;					//USED
	private int symbol_count_no_ws = 0;				//USED
	
	private HashMap<String, Double> metrics_GERBIL = new HashMap<String, Double>();			//USED
	private HashMap<String, Integer> words_distribution = new HashMap<String, Integer>();	//USED
	private HashMap<Integer, Integer> words_occurr_distr = new HashMap<Integer, Integer>();	//USED
	private HashMap<Integer, Integer> annotation_dist = new HashMap<Integer, Integer>();	//USED
	private HashMap<String, Integer> syn_error_dist = new HashMap<String, Integer>();		//USED
	private HashMap<String, Integer> pos_tags_dist = new HashMap<String, Integer>();		//USED
	HashMap<Character, Integer> symbol_error_dist = new HashMap<Character, Integer>();		//USED
	
	/**
	 * This constructor store the original resource file name
	 * @param resource_name
	 */
	public TextInformations(String resource_name)
	{
		this.resource_name = resource_name;
		this.generation_date = Timestamp.getCurrentTime();
	}
	
	/**
	 * Simply return a String of a given local date object
	 * @param ld
	 * @return Date as String
	 */
	public String getLocalDateAsString(LocalDateTime ld)
	{
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		return dtf.format(ld);
	}
	
	//##################################################################################
	//################## GETTERS, SETTERS, ADDERS and EDITS ############################
	//##################################################################################

	public double getSymbol_per_sentence() {
		return symbol_per_sentence;
	}


	public double getSymbol_per_sentence_no_ws() {
		return symbol_per_sentence_no_ws;
	}
	
	
	public int getSymbol_count() {
		return symbol_count;
	}

	
	public void setSymbol_count(int symbol_count) {
		this.symbol_count = symbol_count;
	}

	
	public int getSymbol_count_no_ws() {
		return symbol_count_no_ws;
	}

	
	public void setSymbol_count_no_ws(int symbol_count_no_ws) {
		this.symbol_count_no_ws = symbol_count_no_ws;
	}

	
	public void setSymbol_per_sentence_no_ws(double symbol_per_sentence_no_ws) {
		this.symbol_per_sentence_no_ws = symbol_per_sentence_no_ws;
	}

	
	public LocalDateTime getGeneration_date() {
		return generation_date;
	}


	public void setSymbol_per_sentence(double symbol_per_sentence) {
		this.symbol_per_sentence = symbol_per_sentence;
	}

	
	public double getWord_per_sentence() {
		return word_per_sentence;
	}


	public void setWord_per_sentence(double word_per_sentence) {
		this.word_per_sentence = word_per_sentence;
	}


	public int getSentence_count() {
		return sentence_count;
	}


	public void setSentence_count(int sentence_count) {
		this.sentence_count = sentence_count;
	}


	public int getWord_count() {
		return word_count;
	}


	public void setWord_count(int word_count) {
		this.word_count = word_count;
	}
	

	public String getResource_name() {
		return resource_name;
	}
	
	
	//#############################################################################
	//############################### METRICS #####################################
	//#############################################################################
	

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
	
	
}
