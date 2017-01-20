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
	public ArrayList<Sentence> gatherSentences(String paragraph, DevelishParenthesis dp, TextConversion tc)
    {
		 Document doc = new Document(paragraph);
		 ArrayList<Sentence> sentenceList = new ArrayList<Sentence>();
		 List<Sentence> sents = doc.sentences();
		 
		 rawSentenceSize = sents.size();
		 System.out.println("Raw sentences: "+sents.size());
		 
		 for(int s = 0; s < sents.size(); s++)
		 {
			 String c1 = dp.cleanErrorsAndParenthesis(sents.get(s).text());
			 tc.setErrors(dp.getErrors());
			 String c2 = tc.decompose(c1);
			 dp.setErrors(tc.getErrors());
			 
			 
			 if(c2.contains("[[") && c2.contains("]]"))
			 {
				 //English sentences in general has subject, predicate and object => min. 3 words
				 if(gatherWords(sents.get(s)).size() > 2)
				 {
					 sentenceList.add(new Sentence(c2));
				 }else{
					 //filter to short sequences
					 DistributionProcessing.calcDistString(syn_error_dist, "LESS_THAN_2_WORDS_SEQUENCE");
				 }
			 }else{
				 //filter not annotated sentences
				 DistributionProcessing.calcDistString(syn_error_dist, "NOT_ANNOTATED_SENTENCE");
			 } 
			 
			 //Only relevant for big gold texts
			 if((s > 0 && s % 1000 == 0) || s == sents.size()-1) System.out.println((s)+" sentences processed!");
		 }
		 return sentenceList;
    }
	
	/**
	 * This method gather the words of a list of Sentences. 
	 * @param sentences
	 * @return List of words as String
	 */
	public static LinkedList<String> gatherWords(ArrayList<Sentence> sentences)
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
	public LinkedList<String> sentencesAsStrings(ArrayList<Sentence> sents)
	{
		LinkedList<String> sentences = new LinkedList<String>();
		for(Sentence s : sents)
		{
			sentences.add(s.text());
			DistributionProcessing.calcDistInteger(symbol_per_sent_dist, sentences.getLast().length());
		}
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
    public HashMap<String, Integer> countPosTagsOccourence(ArrayList<Sentence> sentenceList) 
	{
    	HashMap<String, Integer> POS_tag_distribution = new HashMap<String, Integer>();
    	
    	for(Sentence s : sentenceList)
    	{
    		for(String pos_tag : s.posTags()) DistributionProcessing.calcDistString(POS_tag_distribution, pos_tag);
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
		TextConversion tc = new TextConversion();
		String file_location = tr.getResourceFileAbsolutePath("BVFragment.txt");
		ArrayList<Sentence> sentenceList = st.gatherSentences(TextReader.fileReader(file_location), dp, tc);
		
		
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
