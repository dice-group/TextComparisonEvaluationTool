package Engines.Run;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import AnnotedText2NIF.ConverterEngine.DefinitionObject;
import AnnotedText2NIF.ConverterEngine.GatherAnnotationInformations;
import AnnotedText2NIF.IOContent.TextReader;
import Engines.SimpleObjects.*;
import Engines.internalEngineParts.WordFrequencyEngine;
import Engines.simpleTextProcessing.StanfordSegmentatorTokenizer;
import Engines.simpleTextProcessing.TextConversion;
import Engines.Enums.Language;

/**
 * This class start the whole process and return all necessary informations.
 * (maybe add a structured data output)  
 * @author TTurke
 *
 */
public class Main 
{
	/**
	 * Process pipeline
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException 
	{
		
		TextReader tr = new TextReader();
		Language language = Language.EN;
		
		//EDIT HERE for your file name
		String filename = "Bsp1.txt";
		String resourceFileAbsolutePath = tr.getResourceFileAbsolutePath(filename);
		String text_raw = TextReader.fileReader(resourceFileAbsolutePath);
		
		List<String> words;
		LinkedList<String> sentences_raw;
		LinkedList<String> sentences_cleaned = new LinkedList<String>();
		
		TextInformations text_info = new TextInformations(filename);
		HashMap<String, Double> percentage;
		LinkedList<Triple> triples_sorted;
		LinkedList<DefinitionObject> text_annotations = new LinkedList<DefinitionObject>();
		
		//Initiate pipeline --> Just Load ONCE! It takes very much time to initiate it! Remind that for usage!!!
		StanfordSegmentatorTokenizer sst = StanfordSegmentatorTokenizer.create();
		
		//create set and map
		WordFrequencyEngine wfe = new WordFrequencyEngine();	
		
		
		
		/*
		 * TODO Anzahl symbolische Fehler pro Satz 		(m2)	auf raw text	 => Errors like words are crossed by non alnum chars or not closed brackets
		 * TODO Anzahl syntaktischer Fehler pro Satz	(m3)	auf cleaned text => Sentence start is big alphabetic char, sentence end is a punctutations;	
		 * TODO Anzahl POS-Tag verteilung pro Satz 		(m5)	auf cleaned text => Which tag occurs how much inside a sentence 
		 * TODO Anzahl Entities pro Satz 				(m6)	auf cleaned text => splitting in numerous and single annotation types desired?
		 * 
		 * TODO Word splitter bauen um full random text zu generieren! Dient als bottom value geg. Gold und NN Texte
		 * TODO Converter für BASE64 zu UTF-8
		 * TODO Junit Test für Wortzähler und KL-Div
		 * TODO Impl MSE/Quadratischer Fehler 
		 * TODO Impl cos abstand 2er Vektoren 
		 * 
		 */
		
		
		
		
		
		//get sentences
		sentences_raw = StanfordSegmentatorTokenizer.gatherSentences(text_raw);
		text_info.setSentence_count(sentences_raw.size());
		
		//gather words
		words = sst.gatherWords(text_raw, language);
		
		
		for (int i = 0; i < sentences_raw.size(); i++) 
		{
//			System.out.println(sst.formatCleaned(sentences_raw.get(i)));
			
			//store all cleaned sentences
			sentences_cleaned.add(TextConversion.decompose(sst.formatCleaned(sentences_raw.get(i))));
			System.out.println(sentences_cleaned.getLast());
			
			//get text annotations 
			text_annotations.addAll(GatherAnnotationInformations.getAnnotationDefs(sentences_cleaned.getLast()));
			System.out.println("Last added annotation: "+text_annotations.getLast().showAllContent());
			
			//additional output
			System.out.println("Annotations count: "+text_annotations.size());
			System.out.println("ERRC: "+TextConversion.error_signs+" | ERRS: "+TextConversion.errors+"\n");
		}
		
		//calculate word frequency
		wfe.gatherWordFrequencyByList(words);
		
		/* M1 */
		text_info.setSymbol_count(text_raw.length());
		text_info.setSymbol_count_no_ws(text_raw.replaceAll(" ", "").length());
		text_info.setSymbol_per_sentence(text_raw.length()/sentences_raw.size());
		text_info.setSymbol_per_sentence_no_ws(text_raw.replaceAll(" ", "").length()/sentences_raw.size());
		
		//calculate word frequency percentage
		percentage = wfe.wordAppearancePercentage(wfe.getMap(), words.size());
		
		/* M4 */
		text_info.setWord_per_sentence(SimpleRounding.round((1.0*words.size())/sentences_raw.size()));
		text_info.setWord_count(words.size());
		
		//sort calculation for presentation
		triples_sorted = FrequencySorting.sortByPTL(percentage, wfe.getMap());
		
		//present occurrence
		System.out.println("\n\n#################### Word occurrence ####################\t\t\t\n");
		for(Triple t : triples_sorted) System.out.println(t.retString());
		
		System.out.println("\n\n######################### INFO ##########################\t\t\t\n");
		System.out.println("Resource:\t\t\t"+text_info.getResource_name());
		System.out.println("Date and Time:\t\t\t"+text_info.getLocalDateAsString(text_info.getGeneration_date()));
		System.out.println("Words count:\t\t\t"+text_info.getWord_count());
		System.out.println("Sentence count:\t\t\t"+text_info.getSentence_count());
		System.out.println("Symbol count:\t\t\t"+text_info.getSymbol_count());
		System.out.println("Symbol count nws:\t\t"+text_info.getSymbol_count_no_ws());
		System.out.println("Symbol average / Sentence:\t"+text_info.getSymbol_per_sentence());
		System.out.println("Symbol avg nws / Sentence:\t"+text_info.getSymbol_per_sentence_no_ws());
		System.out.println("Word per Sentence:\t\t"+text_info.getWord_per_sentence());
	}

}
