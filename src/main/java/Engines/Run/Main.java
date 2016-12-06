package Engines.Run;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

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
//		final String test_classes = "src\\test\\resources\\";
//		final String test_classes = "src/test/resources/";
//		final String resources = "src\\main\\resources\\";
		TextReader tr = new TextReader();
		Language language = Language.EN;
		
		//Edit input for using other files 
		//EDIT HERE for your file name
		String filename = "Bsp1.txt";

		//AND EDIT HERE if you want the resource folder instead of test-classes
		TextInformations text_info = new TextInformations(filename);

		/*
		 * Anzahl Zeichen pro Satz						(m1)	auf raw text		DONE
		 * TODO Anzahl symbolische Fehler pro Satz 		(m2)	auf raw text		
		 * TODO Anzahl syntaktischer Fehler pro Satz	(m3)	auf cleaned text		
		 * Anzahl Worte pro Satz 						(m4)	auf cleaned text	DONE
		 * TODO Anzahl POS-Tag verteilung pro Satz 		(m5)	auf cleaned text	
		 * TODO Anzahl Entities pro Satz 				(m6)	auf cleaned text	
		 * 
		 * TODO Word splitter bauen um full random text zu generieren! Dient als bottom value geg. Gold und NN Texte
		 * TODO Converter für BASE64 zu UTF-8
		 * TODO Junit Test für Wortzähler und KL-Div
		 * TODO Impl MSE/Quadratischer Fehler 
		 * TODO Impl cos abstand 2er Vektoren 
		 * 
		 */
		
		
		
		String resourceFileAbsolutePath = tr.getResourceFileAbsolutePath(filename);
		System.out.println(resourceFileAbsolutePath);
		String text_raw = TextReader.fileReader(resourceFileAbsolutePath);
//		String text_cleaned = TextConversion.decompose(TextReader.fileReader(tr.getResourceFileAbsolutePath(test_classes+filename)));
		
		//create set and map
		WordFrequencyEngine wfe = new WordFrequencyEngine();	
		
		//init pipeline
		//Just Load ONCE! It takes very much time to init it! Remind that for usage!!!
		StanfordSegmentatorTokenizer sst = StanfordSegmentatorTokenizer.create();
		
		//get sentences
		LinkedList<String> sentences = StanfordSegmentatorTokenizer.gatherSentences(text_raw);
		text_info.setSentence_count(sentences.size());
		
		//gather words
		List<String> words = sst.gatherWords(text_raw, language);
		
		for (int i = 0; i < sentences.size(); i++) 
		{
			System.out.println(sst.formatCleaned(sentences.get(i)));
			System.out.println(TextConversion.decompose(sst.formatCleaned(sentences.get(i))));
			System.out.println("ERRC: "+TextConversion.error_signs+" | ERRS: "+TextConversion.errors);
		}
		
		//calculate word frequency
		wfe.gatherWordFrequencyByList(words);
		
		/* M1 */
		text_info.setSymbol_count(text_raw.length());
		text_info.setSymbol_count_no_ws(text_raw.replaceAll(" ", "").length());
		text_info.setSymbol_per_sentence(text_raw.length()/sentences.size());
		text_info.setSymbol_per_sentence_no_ws(text_raw.replaceAll(" ", "").length()/sentences.size());
		
		//calculate word frequency percentage
		HashMap<String, Double> percentage = wfe.wordAppearancePercentage(wfe.getMap(), words.size());
		
		/* M4 */
		text_info.setWord_per_sentence(SimpleRounding.round((1.0*words.size())/sentences.size()));
		text_info.setWord_count(words.size());
		
		//sort calculation for presentation
		LinkedList<Triple> triples_sorted = FrequencySorting.sortByPTL(percentage, wfe.getMap());
		
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
