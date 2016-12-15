package Engines.Run;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import AnnotedText2NIF.ConverterEngine.DefinitionObject;
import AnnotedText2NIF.ConverterEngine.GatherAnnotationInformations;
import AnnotedText2NIF.IOContent.TextReader;
import Engines.SimpleObjects.*;
import Engines.internalEngineParts.WordFrequencyEngine;
import Engines.simpleTextProcessing.DistributionProcessing;
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
		final String URL = "http://gerbil.aksw.org/gerbil/execute";
		String resourceFileAbsolutePath = tr.getResourceFileAbsolutePath(filename);
		String text_raw = TextReader.fileReader(resourceFileAbsolutePath);
		
		List<String> words;
		LinkedList<String> sentences_raw;
		LinkedList<String> sentences_cleaned = new LinkedList<String>();
		LinkedList<SentenceObject> sos = new LinkedList<SentenceObject>();
		LinkedList<int[]> annotation_sorted = new LinkedList<int[]>();
		LinkedList<int[]> wps_sorted = new LinkedList<int[]>();
		LinkedList<int[]> syn_err_per_sen = new LinkedList<int[]>();
		LinkedList<DefinitionObject> dobjs = new LinkedList<DefinitionObject>();
		LinkedList<Triple> triples_sorted = new LinkedList<Triple>();
		LinkedList<DefinitionObject> text_annotations = new LinkedList<DefinitionObject>();
		LinkedList<PosTagObject> pos_tags = new LinkedList<PosTagObject>();
		
		TextInformations text_info = new TextInformations(filename);
		HashMap<String, Double> percentage;
		
		
		
		//Initiate pipeline --> Just Load ONCE! It takes very much time to initiate it! Remind that for usage!!!
		StanfordSegmentatorTokenizer sst = StanfordSegmentatorTokenizer.create();
		
		//create set and map
		WordFrequencyEngine wfe = new WordFrequencyEngine();	
		
		System.out.println("DISTRIBUTION ORDERED BY KEYVALUE (most left vertical list)");
		
		/*
		 * GENERAL
		 * TODO alle simple Metriken im text_info Objekt speichern und alle von Gerbil (nur) als KL-Div Wert!!!!!
		 * TODO GERBIL Annotatoren auswählen (4 stk.) und als default GERBIL Metriken nutzen.
		 * 
		 * IMPL
		 * TODO Verteilung symbolische Fehler pro Satz 			(m2)	auf raw text	 => Errors like words are crossed by non alnum chars or not closed brackets
		 * TODO GERBIL an- & einbinden
		 * TODO Word splitter bauen um full random text zu generieren! Dient als bottom value geg. Gold und NN Texte
		 * TODO Impl cos abstand 2er Vektoren/arrays 
		 * TODO Converter für BASE64 zu UTF-8
		 * 
		 * JUNIT
		 * TODO Junit Test für Wortzähler
		 * TODO Junit Test für KL-Div
		 * TODO Junit Test für quadratischen Fehler/MSE
		 * TODO Junit Test für cos Abstand
		 */
		
		
		
		
		
		//get sentences
		sentences_raw = StanfordSegmentatorTokenizer.gatherSentences(text_raw);
		text_info.setSentence_count(sentences_raw.size());
		
		//gather words
		words = sst.gatherWords(text_raw, language);
		
		/* M5: POS-Tags Distribution over all Sentences */
		//gather, sort and store part of speech labels
		pos_tags = wfe.appearancePercentage(FrequencySorting.sortPosTagMap(sst.countPosTagsOccourence(sst.getTokens())), sst.getTokens().size());
		text_info.setPos_tag_objs(pos_tags);
		
//		for (PosTagObject tag : pos_tags) System.out.println("["+tag.getPOS_Tag()+"]\t\t["+tag.getTag_ouccurrence()+"]\t\t["+tag.getTag_oucc_percentage()+"]");
		
		for (int i = 0; i < sentences_raw.size(); i++) 
		{	
			//store all cleaned sentences
			//TODO anzahl Sätze für Bsp1.txt ist 7 aber ich erhalte nur 6
			sentences_cleaned.add(TextConversion.decompose(sst.formatCleaned(sentences_raw.get(i))));
			
			//gather text annotations 
			dobjs = GatherAnnotationInformations.getAnnotationDefs(sentences_cleaned.getLast());
			
			//store sentence objects and annotations
			if(dobjs.size() > 0)
			{
				sos.add(new SentenceObject(sentences_cleaned.getLast(), dobjs.size()));	
				text_annotations.addAll(dobjs);
			}
			
			//additional output
//			System.out.println("ERRC: "+TextConversion.error_signs+" | ERRS: "+TextConversion.errors+"\n");
		}
		
		/* M6: Entity Distribution over all Sentence*/
		//process, sort and store annotation distribution
		annotation_sorted = FrequencySorting.sortDist(DistributionProcessing.getAnnotDist(sos));
		text_info.setSorted_annot_dist(annotation_sorted);
		
		System.out.println("########  [Entities] / [Sentences] ########");
		for (int i = 0; i < annotation_sorted.size(); i++) {
			System.out.println("["+annotation_sorted.get(i)[0]+"]["+annotation_sorted.get(i)[1]+"]");
		}
		
		/* M4: Word Distribution over all Sentences */
		wps_sorted = FrequencySorting.sortDist(DistributionProcessing.getWPSDist(sos, sst, language));
		text_info.setSorted_wps_dist(wps_sorted);
		
		System.out.println("######## [Words] / [Sentences] ########");
		for (int i = 0; i < wps_sorted.size(); i++) {
			System.out.println("["+wps_sorted.get(i)[0]+"]["+wps_sorted.get(i)[1]+"]");
		}
		
		/*M3: Syntactic error Distribution over all Sentence */
		syn_err_per_sen = DistributionProcessing.calcSimpleSynErrorDist(sentences_cleaned, language);
		
		System.out.println("######## [Syntaxerrors] / [Sentences] ########");
		for (int i = 0; i < syn_err_per_sen.size(); i++) {
			System.out.println("["+syn_err_per_sen.get(i)[0]+"]["+syn_err_per_sen.get(i)[1]+"]");
		}
		
		text_info.setSorted_synerr_per_sen_dist(syn_err_per_sen);
		
		//add annotations to text_info
		text_info.addSthToAll_Annotations(text_annotations); 
		
		//calculate word frequency
		wfe.gatherWordFrequencyByList(words);
		
		/* M1: Symbol Average over all Sentences */
		text_info.setSymbol_count(text_raw.length());
		text_info.setSymbol_count_no_ws(text_raw.replaceAll(" ", "").length());
		text_info.setSymbol_per_sentence(text_raw.length()/sentences_raw.size());
		text_info.setSymbol_per_sentence_no_ws(text_raw.replaceAll(" ", "").length()/sentences_raw.size());
		
		//calculate word frequency percentage
		percentage = wfe.appearancePercentage(wfe.getMap(), words.size());
		
		/* M4: Word Distribution over all Sentences */
		text_info.setWord_per_sentence(SimpleRounding.round((1.0*words.size())/sentences_raw.size()));
		text_info.setWord_count(words.size());
		
		//sort calculation for presentation
		triples_sorted = FrequencySorting.sortByPTL(percentage, wfe.getMap());
		text_info.setWord_distribution(triples_sorted);
		
//		System.out.println("\n\n######################### INFO ##########################\t\t\t\n");
//		System.out.println("Resource:\t\t\t"+text_info.getResource_name());
//		System.out.println("Date and Time:\t\t\t"+text_info.getLocalDateAsString(text_info.getGeneration_date()));
//		System.out.println("Words count:\t\t\t"+text_info.getWord_count());
//		System.out.println("Sentence count:\t\t\t"+text_info.getSentence_count());
//		System.out.println("Symbol count:\t\t\t"+text_info.getSymbol_count());
//		System.out.println("Symbol count nws:\t\t"+text_info.getSymbol_count_no_ws());
//		System.out.println("Symbol average / Sentence:\t"+text_info.getSymbol_per_sentence());
//		System.out.println("Symbol avg nws / Sentence:\t"+text_info.getSymbol_per_sentence_no_ws());
//		System.out.println("Word per Sentence:\t\t"+text_info.getWord_per_sentence());
	}

}
