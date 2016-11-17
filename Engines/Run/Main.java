package Run;

import java.util.HashMap;
import java.util.LinkedList;
import IOContent.TextReader;
import SimpleObjects.*;
import internalEngineParts.WordFrequencyEngine;
import simpleTextProcessing.StanfordSegmentatorTokenizer;
import simpleTextProcessing.TextConversion;

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
	 */
	public static void main(String[] args) 
	{
		//Edit input for using other files
//		String str1 ="C:/Users/Subadmin/Desktop/Dropbox/BA AKSW/Deep LSTM/epoch- 70 Final.txt";
		String str = "C:/Users/Subadmin/Desktop/Dropbox/BA AKSW/Deep LSTM/Testtexte bad/Bsp1.txt";
		String text = TextConversion.decompose(TextReader.fileReader(str));
		
		//create set and map
		WordFrequencyEngine wfe = new WordFrequencyEngine();	
		
		//init pipeline
		StanfordSegmentatorTokenizer.create();
		
		//get sentences
		LinkedList<String> sentences = StanfordSegmentatorTokenizer.gatherSentences(text);
		
		//calculate word frequency
		for(String s : sentences) wfe.gatherWordFrequency(s);
		
		//calculate word frequency percentage
		HashMap<String, Double> percentage = wfe.wordAppearancePercentage(wfe.getMap());
		
		//sort calculation for presentation
		LinkedList<Triple> triples_sorted = FrequencySorting.sortByPTL(percentage, wfe.getMap());
		
		//present
		for(Triple t : triples_sorted) System.out.println(t.retString());
		
		System.out.println("Different words:\t\t\t"+wfe.word_count);
		System.out.println("Maps are equal? \t\t\t"+wfe.sizeEqualityMaps(percentage, wfe.getMap()));
		System.out.println("Set of keys reflect map keys?\t\t"+wfe.sizeEqualitySetMap(wfe.getMap(), wfe.getSet()));

	}

}
