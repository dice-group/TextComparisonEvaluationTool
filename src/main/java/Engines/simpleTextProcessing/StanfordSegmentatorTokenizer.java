package Engines.simpleTextProcessing;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import Engines.Enums.Language;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.process.DocumentPreprocessor;

/**
 * This class is created reflecting the use case of the Stanford CoreNLP compontent Tokenizer, POS-Tagger and CoreLabel(ing).
 * @author TTurke
 *
 */
public class StanfordSegmentatorTokenizer 
{
	private Language language;
	protected StanfordCoreNLP pipeline;
	protected final String punctutations = "':,.!-()?;\"[]|";
	
	
	/**
	 * This method creates a StanfordSegmentatorTokenizer with a pre-defined CoreNLP pipeline.
	 * Pipeline Properties:
	 * 		- annotators
	 * 		- tokenize
	 * 		- ssplit
	 * 		- pos
	 * 		- lemma
	 * 
	 * @return Tokenizer with pipeline
	 */
	public static StanfordSegmentatorTokenizer create() 
	{
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        return new StanfordSegmentatorTokenizer(pipeline);
    }

    
	/**
	 * Constructor
	 * @param pipeline
	 */
    public StanfordSegmentatorTokenizer(StanfordCoreNLP pipeline) 
	{
        this.pipeline = pipeline;
    }

    
    /**
     * This method gather the words of a text and store it inside a list of string. 
     * @param text
     * @param language
     * @return list of words
     */
    public List<String> gatherWords(String text, Language language)
	{
    	//keep the language
    	setLanguage(language);
    			
    	Annotation document = new Annotation(text);
    	pipeline.annotate(document);
    	        
    	List<CoreLabel> tokens = document.get(TokensAnnotation.class);
    	List<String> words = new ArrayList<String>();
    	int lastEnd = -1;
    	
    	for (CoreLabel token : tokens) 
    	{
    		if (!((token.beginPosition() <= lastEnd) && ".".equals(token.get(PartOfSpeechAnnotation.class)))) 
    		{
    			String tmp  = TextConversion.normalizer(formatCleaned(token.get(TextAnnotation.class))).trim();
    			if(tmp != " " && tmp.length() > 0) words.add(tmp); 
    			lastEnd = token.endPosition();
    		}
    	}
    	        
    	return words;
    }
    
    
    /**
     * This class return all tokens for a given text of a given language.
     * @param query
     * @param language
     * @return core label list
     */
    public List<CoreLabel> getTokens(String paragraph, Language language)
    {
    	setLanguage(language);
		
        Annotation document = new Annotation(paragraph);
        pipeline.annotate(document);

        List<CoreLabel> tokens = document.get(TokensAnnotation.class);
        
        return tokens;
    }
    
    
    /**
     * This method split a given text if its possible into single sentences.
     * @param query
     * @return List of sentence-Strings
     */
    public static LinkedList<String> gatherSentences(String paragraph)
    {
    	Reader reader = new StringReader(paragraph);
    	DocumentPreprocessor dp = new DocumentPreprocessor(reader);
    	LinkedList<String> sentenceList = new LinkedList<String>();
    	
    	for (List<HasWord> sentence : dp) sentenceList.add(Sentence.listToString(sentence).toString());
    	
    	return sentenceList;
    }        
    
    
    //TODO POS Tagging impl needed
    /**
     * This method collect all the necessary information from token for the Segment object.
     * Lemmatize can be added later if needed (need to update Segment and this class then)!
     * The POS tags are gathered from Penn Treebank Project.
     * Link: http://www.ling.upenn.edu/courses/Fall_2003/ling001/penn_treebank_pos.html
     * @param token
     * @return Segment object
     */
    private void countWordsAndPosTagsOccourence(CoreLabel token) 
	{
		// Word content of the token
        // String word = token.get(TextAnnotation.class);
		
		// Startposition of the token
		int start = token.beginPosition();
		
		// Lenght of the token
		int len = (token.endPosition() - token.beginPosition());
		
        // POS tag of the token
        String pos = token.get(PartOfSpeechAnnotation.class);
		
        // NER label of the token
        // String lemma = token.get(LemmaAnnotation.class);
        
        // Check Superlative and Comparative
        if(pos.contains("JJR"))
        {

        }else if(pos.contains("JJS"))
        {
        	
        }
    }	

    
    /**
     * This method replace square bracket markings by the symbol
     * @param in 
     * @return foramted String
     */
    public static String formatSBTags(String in)
    {
    	return in.replaceAll("-LSB-", "[").replaceAll("-RSB-", "]").replace("[ [ ", "[[").replace(" ] ]", "]]");
    }
    
    
    /**
     * This method clean a string from all square bracket part of speech tag labels and delete special types of whitespace's.
     * Finally the cleaned string will be trimmed and returned.
     * @param in
     * @return cleaned and trimmed string
     */
    public static String formatCleaned(String in)
    {
    	return in.replaceAll("-LSB-", "[").replaceAll("-RSB-", "]").replaceAll("LSB", "").replaceAll("RSB", "").replace("[ [ ", "[[").replace(" ] ]", "]]").trim();
    }
    

	public Language getLanguage() {return language;}

	public void setLanguage(Language language) {this.language = language;}
}