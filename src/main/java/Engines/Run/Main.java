package Engines.Run;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import AnnotedText2NIF.IOContent.TextReader;
import Engines.SimpleObjects.*;
import Engines.internalEngineParts.WordFrequencyEngine;
import Engines.simpleTextProcessing.StanfordSegmentatorTokenizer;
import Engines.simpleTextProcessing.TextConversion;

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
		//Edit input for using other files
		
		final String test_classes = "target\\test-classes\\";
		final String resources = "src\\main\\resources\\";
		String filename = "Bsp1.txt";
		TextReader tr = new TextReader();

		/*
		 * TODO Anzahl Zeichen pro Satz				(m1)	auf raw text
		 * TODO Anzahl Fehler pro Satz 				(m2)	auf raw text
		 * TODO Anzahl syntaktischer Fehler pro Satz (m3)	auf raw text
		 * TODO Anzahl Worte pro Satz 				(m4)	auf cleaned text
		 * TODO Anzahl POS-Tag verteilung pro Satz 	(m5)	auf cleaned text
		 * TODO Anzahl Entities pro Satz 			(m6)	auf cleaned text
		 */
		
		
		String text_raw = TextReader.fileReader(tr.getResourceFileAbsolutePath(test_classes+filename));
		String text_cleaned = TextConversion.decompose(TextReader.fileReader(tr.getResourceFileAbsolutePath(test_classes+filename)));
		
		//create set and map
		WordFrequencyEngine wfe = new WordFrequencyEngine();	
		
		//init pipeline
		StanfordSegmentatorTokenizer.create();
		
		//get sentences
		LinkedList<String> sentences = StanfordSegmentatorTokenizer.gatherSentences(text_cleaned);
		
		//calculate word frequency
		for(String s : sentences) wfe.gatherWordFrequency(s);
		
		//calculate word frequency percentage
		HashMap<String, Double> percentage = wfe.wordAppearancePercentage(wfe.getMap());
		
		//sort calculation for presentation
		LinkedList<Triple> triples_sorted = FrequencySorting.sortByPTL(percentage, wfe.getMap());
		
		//present
		for(Triple t : triples_sorted) System.out.println(t.retString());
		
		System.out.println("Different words:\t\t\t"+wfe.word_count);
//		System.out.println("Maps are equal? \t\t\t"+wfe.sizeEqualityMaps(percentage, wfe.getMap()));
//		System.out.println("Set of keys reflect map keys?\t\t"+wfe.sizeEqualitySetMap(wfe.getMap(), wfe.getSet()));

	}

}
