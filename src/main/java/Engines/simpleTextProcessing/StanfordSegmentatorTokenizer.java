package Engines.simpleTextProcessing;

import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import Engines.Enums.Language;
import AnnotedText2NIF.IOContent.TextReader;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
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

    
    
    //TODO hier soll das Sentence splitting der paragraph vorgenommen werden sowie das word counting und das pos tagging
    public void proceed(String query, Language language)
	{
    	//TODO check necessarily
    	
    	//keep the language
		setLanguage(language);
		
        Annotation document = new Annotation(query);
        pipeline.annotate(document);

        List<CoreLabel> tokens = document.get(TokensAnnotation.class);

        int lastEnd = -1;
		
        
		
        for (CoreLabel token : tokens) 
		{
            if (!((token.beginPosition() <= lastEnd) && ".".equals(token.get(PartOfSpeechAnnotation.class)))) 
            {          	
            	//TODO hier werden die Tokens verarbeitet
            	
            	
                lastEnd = token.endPosition();
            }
        }
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
    	//TODO POS Tagging impl needed
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

    
    
    

	public Language getLanguage() {return language;}

	public void setLanguage(Language language) {this.language = language;}



	/*
	 * Working Example for the Tokenizer
	 */
	public static void main(String[] args) throws Exception 
	{
		//TODO Export to main.class needed
		
//		String str ="C:/Users/Subadmin/Desktop/Dropbox/BA AKSW/Deep LSTM/epoch- 70 Final.txt";
		String str = "C:/Users/Subadmin/Desktop/Dropbox/BA AKSW/Deep LSTM/Testtexte bad/Bsp1.txt";
		String textRAW = TextReader.fileReader(str);
		
		String textExample = "Another ex-Golden Stater, Paul Stankowski a bigger guy from Oxnard, is contending"
				+ "for a berth on the U.S. Ryder Cup team after winning his first PGA Tour"
				+ "event last year and staying within three strokes of the lead through"
				+ "three rounds of last month's U.S. Open. Mike is high, Mark is higher and Lars is the highest.";

		
		StanfordSegmentatorTokenizer sst = StanfordSegmentatorTokenizer.create();
		LinkedList<String> sentences = gatherSentences(textRAW);
		
		System.out.println(sentences.size());
		
		for(int i = 0; i < sentences.size(); i++)
		{
			System.out.println((i+1)+". Sentence: "+sentences.get(i).replaceAll("-LSB-", "[").replaceAll("-RSB-", "]").replace("[ [ ", "[[").replace(" ] ]", "]]").trim());
		}
		
	}

}