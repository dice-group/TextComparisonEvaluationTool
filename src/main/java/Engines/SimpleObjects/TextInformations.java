package Engines.SimpleObjects;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;

import AnnotedText2NIF.ConverterEngine.DefinitionObject;

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
	private double error_symbol_per_sentence = 0.0;	//
	private double syntax_error_per_sentence = 0.0;	//
	private double word_per_sentence = 0.0;			//USED
	private double entity_per_sentence = 0.0;		//
	private double occurrence_single_annots = 0.0;	//
	private double occurrence_dual_annots = 0.0;	//
	
	private int sentence_count = 0;					//USED
	private int word_count = 0;						//USED
	private int symbol_count = 0;					//USED
	private int symbol_count_no_ws = 0;				//USED
	private int error_symbol_count = 0;				//USED
	
	private LinkedList<int[]> sorted_annot_dist = new LinkedList<int[]>();						//USED
	private LinkedList<int[]> sorted_wps_dist = new LinkedList<int[]>();						//USED
	private LinkedList<int[]> sorted_synerr_per_sen_dist = new LinkedList<int[]>();				//USED
	private LinkedList<PosTagObject> pos_tag_objs = new LinkedList<PosTagObject>();				//USED
	
	private HashMap<String, Double> metrics_GERBIL = new HashMap<String, Double>();
	private HashMap<String, Integer> words_distribution = new HashMap<String, Integer>();
	private HashMap<Integer, Integer> words_occurr_distr = new HashMap<Integer, Integer>();
	private HashMap<Integer, Integer> annotation_dist = new HashMap<Integer, Integer>();
	private HashMap<Integer, Integer> syn_error_dist = new HashMap<Integer, Integer>();
	
	
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
	//################## GETTERS, SETTERS, ADDERS and EDITS ##########################
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


	public double getError_symbol_per_sentence() {
		return error_symbol_per_sentence;
	}


	public void setError_symbol_per_sentence(double error_symbol_per_sentence) {
		this.error_symbol_per_sentence = error_symbol_per_sentence;
	}


	public double getSyntax_error_per_sentence() {
		return syntax_error_per_sentence;
	}


	public void setSyntax_error_per_sentence(double syntax_error_per_sentence) {
		this.syntax_error_per_sentence = syntax_error_per_sentence;
	}


	public double getWord_per_sentence() {
		return word_per_sentence;
	}


	public void setWord_per_sentence(double word_per_sentence) {
		this.word_per_sentence = word_per_sentence;
	}


	public double getEntity_per_sentence() {
		return entity_per_sentence;
	}


	public void setEntity_per_sentence(double entity_per_sentence) {
		this.entity_per_sentence = entity_per_sentence;
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


	public int getError_symbol_count() {
		return error_symbol_count;
	}


	public void setError_symbol_count(int error_symbol_count) {
		this.error_symbol_count = error_symbol_count;
	}

	public String getResource_name() {
		return resource_name;
	}

	public double getOccurrence_single_annots() {
		return occurrence_single_annots;
	}

	public void setOccurrence_single_annots(double occurrence_single_annots) {
		this.occurrence_single_annots = occurrence_single_annots;
	}

	public double getOccurrence_dual_annots() {
		return occurrence_dual_annots;
	}

	public void setOccurrence_dual_annots(double occurrence_dual_annots) {
		this.occurrence_dual_annots = occurrence_dual_annots;
	}

	public LinkedList<int[]> getSorted_annot_dist() {
		return sorted_annot_dist;
	}

	public void setSorted_annot_dist(LinkedList<int[]> sorted_annot_dist) {
		this.sorted_annot_dist = sorted_annot_dist;
	}

	public LinkedList<PosTagObject> getPos_tag_objs() {
		return pos_tag_objs;
	}

	public void setPos_tag_objs(LinkedList<PosTagObject> pos_tag_objs) {
		this.pos_tag_objs = pos_tag_objs;
	}

	public LinkedList<int[]> getSorted_wps_dist() {
		return sorted_wps_dist;
	}

	public void setSorted_wps_dist(LinkedList<int[]> sorted_wps_dist) {
		this.sorted_wps_dist = sorted_wps_dist;
	}

	public LinkedList<int[]> getSorted_synerr_per_sen_dist() {
		return sorted_synerr_per_sen_dist;
	}

	public void setSorted_synerr_per_sen_dist(LinkedList<int[]> sorted_synerr_per_sen_dist) {
		this.sorted_synerr_per_sen_dist = sorted_synerr_per_sen_dist;
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

	public HashMap<Integer, Integer> getSyn_error_dist() {
		return syn_error_dist;
	}

	public void setSyn_error_dist(HashMap<Integer, Integer> syn_error_dist) {
		this.syn_error_dist = syn_error_dist;
	}
	
	
}
