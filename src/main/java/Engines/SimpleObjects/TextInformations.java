package Engines.SimpleObjects;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

/**
 * This class contain all relevant informations about a processed text.
 * 
 * @author TTurke
 *
 */
public class TextInformations 
{
	private String resource_name = "";
	private LocalDateTime generation_date;
	
	private double symbol_per_sentence = 0.0;
	private double symbol_per_sentence_no_ws = 0.0;	//no whitespace's
	private double error_symbol_per_sentence = 0.0;
	private double syntax_error_per_sentence = 0.0;
	private double word_per_sentence = 0.0;
	private double entity_per_sentence = 0.0;
	
	private int sentence_count = 0;
	private int word_count = 0;
	private int symbol_count = 0;
	private int symbol_count_no_ws = 0;
	private int error_symbol_count = 0;
	private int syntax_error_count = 0;
	private int entity_count = 0;
	
	LinkedList<Character> error_symbols = new LinkedList<Character>();
	LinkedList<String> pos_tags_spread_per_sentence = new LinkedList<String>();
	
	/**
	 * This constructor store the original resource file name
	 * @param resource_name
	 */
	public TextInformations(String resource_name)
	{
		this.resource_name = resource_name;
		this.generation_date = getCurrentTime();
	}
	
	/**
	 * Simply return generation date of the class object
	 * @return LoclaDateTime
	 */
	public LocalDateTime getCurrentTime()
	{
		LocalDateTime localDate = LocalDateTime.now();
		return localDate;
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
	//########################### GETTERS & SETTERS ####################################
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


	public int getSyntax_error_count() {
		return syntax_error_count;
	}


	public void setSyntax_error_count(int syntax_error_count) {
		this.syntax_error_count = syntax_error_count;
	}


	public int getEntity_count() {
		return entity_count;
	}


	public void setEntity_count(int entity_count) {
		this.entity_count = entity_count;
	}


	public LinkedList<Character> getError_symbols() {
		return error_symbols;
	}


	public void setError_symbols(LinkedList<Character> error_symbols) {
		this.error_symbols = error_symbols;
	}


	public LinkedList<String> getPos_tags_spread_per_sentence() {
		return pos_tags_spread_per_sentence;
	}


	public void setPos_tags_spread_per_sentence(LinkedList<String> pos_tags_spread_per_sentence) {
		this.pos_tags_spread_per_sentence = pos_tags_spread_per_sentence;
	}


	public String getResource_name() {
		return resource_name;
	}
	
	
}
