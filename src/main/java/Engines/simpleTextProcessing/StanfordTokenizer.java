package Engines.simpleTextProcessing;

import java.io.IOException;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import AnnotedText2NIF.IOContent.TextReader;
import edu.stanford.nlp.simple.*;

/**
 * This class handle the everything about sentence creation and cleaning, word collection and part-of-speech tagging.
 * Its using parts from Stanford CoreNLP 3.7.0
 * @author TTurke
 *
 */
public class StanfordTokenizer 
{
	private double rawSentenceSize = Double.NaN;
	private static HashMap<String, Integer> syn_error_dist;
	private static HashMap<Integer, Integer> symbol_per_sent_dist;

	//#############################################################################
	//############################# CONSTRUCTORS ##################################
	//#############################################################################
	
	/**
	 * Constructor
	 */
	public StanfordTokenizer(){
		syn_error_dist = new HashMap<String, Integer>();
		symbol_per_sent_dist = new HashMap<Integer, Integer>();
	}
	
	//#############################################################################
	//############################ USAGE METHODS ##################################
	//#############################################################################
	
	/**
	 * This method split a given text into sentences and store only these who contain annotation entities.
	 * If an sentences does not contain annotation entities here we store an syntax error. 
	 * @param paragraph
	 * @param dp 
	 * @param tc 
	 * @return List of Sentence objects
	 */
	public LinkedList<Sentence> gatherSentences(String paragraph, DevelishParenthesis dp)
    {
		 Document doc = new Document(paragraph);
		 LinkedList<Sentence> sentenceList = new LinkedList<Sentence>();
		 List<Sentence> sents = doc.sentences();
		 rawSentenceSize = sents.size();
		 
		 for(int s = 0; s < sents.size(); s++)
		 {
			 String c1 = dp.cleanErrorsAndParenthesis(sents.get(s).text());
			 if(c1.length()<2 && c1.equals("."))
			 {
				 DistributionProcessing.calcDist(syn_error_dist, "ONLY_A_DOT_ERROR");
			 }else{
				 sentenceList.add(new Sentence(c1));
			 }
			 
			 DistributionProcessing.calcDist(symbol_per_sent_dist, sentenceList.getLast().length());
			 
			 //Only relevant for big gold texts
			 if((s > 0 && s % 999 == 0) || s == sents.size()-1) System.out.println((s+1)+" sentences processed!");
		 }
		 return sentenceList;
    }
	
	/**
	 * This method gather the words of a list of Sentences. 
	 * @param sentences
	 * @return List of words as String
	 */
	public static LinkedList<String> gatherWords(LinkedList<Sentence> sentences)
	{
		LinkedList<String> words = new LinkedList<String>();
		
		for (Sentence s : sentences) 
		{
			for(String word : s.words())
			{
				if(		!word.equals("-LSB-") 
					&& 	!word.equals("-RSB-") 
					&& 	!word.equals("-LRB-") 
					&& 	!word.equals("-RRB-")
					&& 	normalizer(word).length() > 0) words.add(word);
			}
		}
		return words;
	}
	
	/**
	 * This method gather the words of a Sentences. 
	 * @param sentence
	 * @return List of words as String
	 */
	public LinkedList<String> gatherWords(Sentence sentence)
	{
		LinkedList<String> words = new LinkedList<String>();
		
		for(String word : sentence.words())
		{
			if(		!word.equals("-LSB-") 
				&& 	!word.equals("-RSB-") 
				&& 	!word.equals("-LRB-") 
				&& 	!word.equals("-RRB-")
				&& 	normalizer(word).length() > 0) words.add(word);
		}
		return words;
	}
	
	/**
	 * This method convert list of Sentence Objects into a list of Sentence Strings.
	 * @param sents
	 * @return List of Sentences as String
	 */
	public LinkedList<String> sentencesAsStrings(LinkedList<Sentence> sents)
	{
		LinkedList<String> sentences = new LinkedList<String>();
		for(Sentence s : sents) sentences.add(s.text());
		return sentences;
	}
    
	 /**
     * This method use a list of tokens to gather the POS tags of a Text and store the tags inside a Map.
     * The map also contains a count of the tags occurrence inside the text.
     * 
     * The POS tags depend partially on the Penn Treebank Project.
     * Link: http://www.ling.upenn.edu/courses/Fall_2003/ling001/penn_treebank_pos.html
     * @param token
     * @return Map of POS-Tags as Keys and there occurrence count as value
     */
    public HashMap<String, Integer> countPosTagsOccourence(LinkedList<Sentence> sentence_objects)
	{
    	HashMap<String, Integer> POS_tag_distribution = new HashMap<String, Integer>();
    	
    	for(Sentence s : sentence_objects)
    	{
    		for(String pos_tag : s.posTags()) DistributionProcessing.calcDist(POS_tag_distribution, pos_tag);
    	}
    	
        return POS_tag_distribution;
    }	
	
    /**
     * This method split a huge list into 4 or 5 parts
     * @param list
     * @return List of desired smaller lists
     */
    public <T>  ArrayList<ArrayList<T>> splitIntoParts(ArrayList<T> list)
    {
    	int partitionSize = (int)list.size()/4;
    	ArrayList<ArrayList<T>> partitions = new ArrayList<ArrayList<T>>();
    	
    	for (int i = 0; i < list.size(); i += partitionSize) 
    	{
    	    partitions.add((ArrayList<T>) list.subList(i, Math.min(i + partitionSize, list.size())));
    	}
    	
    	return partitions;
    }
    
	/**
	 * This method normalize a text and replace all non all non alpha numerics with dots.
	 * @param text
	 * @return text with dots instead of whitespace
	 */
	public static String normalizer(String text)
	{
		return Normalizer.normalize(text, Form.NFD).replaceAll("[^A-Za-z0-9-]", "");
	}
	
	//#############################################################################
	//###################### GETTERS, SETTERS & EDITS #############################
	//#############################################################################
	
	public HashMap<String, Integer> getSyn_error_dist() {
		return syn_error_dist;
	}

	public HashMap<Integer, Integer> getSymbol_per_sent_dist() {
		return symbol_per_sent_dist;
	}
	
	public double getRawSentenceSize() {
		return rawSentenceSize;
	}

	//#############################################################################
	//############################### EXAMPLE #####################################
	//#############################################################################

	/*
	 * EXAMPLE of USE
	 */
	public static void main(String[] args) throws IOException 
	{ 
		
		TextReader tr = new TextReader();
		StanfordTokenizer st = new StanfordTokenizer();
		DevelishParenthesis dp = new DevelishParenthesis();
		String file_location = tr.getResourceFileAbsolutePath("BVFragment.txt");
		LinkedList<Sentence> sentenceList = st.gatherSentences(TextReader.fileReader(file_location), dp);
		
		
		System.out.println("ERRORS: "+dp.getErrors().size());
		System.out.println("TYPES: "+dp.getErrors().keySet());
		System.out.println("OCCU: "+dp.getErrors().values());
		System.out.println("SIZE: "+sentenceList.size()+"\n");
		
		for (Sentence s : sentenceList) 
		{
			System.out.println(s.text());
		}
		
		System.out.println("SIZE: "+sentenceList.size());
    }
}
