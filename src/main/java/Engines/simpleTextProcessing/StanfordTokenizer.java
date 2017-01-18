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

public class StanfordTokenizer 
{
	/**
	 * This method split a given text into sentences.
	 * @param paragraph
	 * @return List of Sentence objects
	 */
	public static ArrayList<Sentence> gatherSentences(String paragraph)
    {
		 Document doc = new Document(paragraph);
		 ArrayList<Sentence> sentenceList = (ArrayList<Sentence>) doc.sentences();
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
	public static LinkedList<String> gatherWords(Sentence sentence)
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
	public static LinkedList<String> sentencesAsStrings(ArrayList<Sentence> sents)
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
    public static HashMap<String, Integer> countPosTagsOccourence(ArrayList<Sentence> sentenceList) 
	{
    	HashMap<String, Integer> POS_tag_distribution = new HashMap<String, Integer>();
    	
    	for(Sentence s : sentenceList)
    	{
    		for(String pos_tag : s.posTags()) DistributionProcessing.calcDistString(POS_tag_distribution, pos_tag);
    	}
    	
        return POS_tag_distribution;
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void main(String[] args) throws IOException 
	{ 
		
		TextReader tr = new TextReader();
		String file_location = tr.getResourceFileAbsolutePath("BeispielCheck.txt");
		ArrayList<Sentence> sentenceList = gatherSentences(TextReader.fileReader(file_location));
		
		int check = 0;
		
		System.out.println("SIZE: "+sentenceList.size()+"\n");
		
		for (Sentence s : sentenceList) 
		{
			List<String> words = s.words();
			System.out.println("TEXT: "+s.text());
			System.out.println("WORDS: "+words);
			System.out.println("TAGS: "+s.posTags());
			System.out.println();
			check += words.size();
		}
		
		LinkedList<String> word_list = gatherWords(sentenceList);
		
		System.out.println("Check: "+check);
		System.out.println("Other: "+word_list.size());
		System.out.println("Difference: "+(check -word_list.size()));
		
//        // Create a document. No computation is done yet.
//        Document doc = new Document("add your text here! It can contain multiple sentences.");
//        for (Sentence sent : doc.sentences()) {  // Will iterate over two sentences
//            // We're only asking for words -- no need to load any models yet
//            System.out.println("The second word of the sentence '" + sent + "' is " + sent.word(1));
//            // When we ask for the lemma, it will load and run the part of speech tagger
//            System.out.println("The third lemma of the sentence '" + sent + "' is " + sent.lemma(2));
//            // When we ask for the parse, it will load and run the parser
//            System.out.println("The parse of the sentence '" + sent + "' is " + sent.parse());
//            // ...
//        }
    }
}
