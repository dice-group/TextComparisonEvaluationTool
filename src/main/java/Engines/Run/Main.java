package Engines.Run;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import org.json.JSONObject;

import AnnotedText2NIF.ConverterEngine.DefinitionObject;
import AnnotedText2NIF.ConverterEngine.GatherAnnotationInformations;
import AnnotedText2NIF.IOContent.TextReader;
import Engines.SimpleObjects.*;
import Engines.internalEngineParts.WordFrequencyEngine;
import Engines.simpleTextProcessing.DistributionProcessing;
import Engines.simpleTextProcessing.StanfordSegmentatorTokenizer;
import Engines.simpleTextProcessing.TextConversion;
import Web.Controller.HttpController;
import Web.Objects.ExperimentObjectGERBIL;
import Engines.Enums.Annotators;
import Engines.Enums.ExpType;
import Engines.Enums.Language;
import Engines.Enums.Matching;

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
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception 
	{
		//################# GENERAL Setup #################
		//Single items
		TextReader tr = new TextReader();
		Language language = Language.EN;
		String filename = "Bsp1.txt";	//TODO do it for various files 
		String resourceFileAbsolutePath = tr.getResourceFileAbsolutePath(filename);
		String text_raw = TextReader.fileReader(resourceFileAbsolutePath);
		
		//Multiple items
		LinkedList<String> words;
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
		
		
		//TODO hier NIF-Converter einbauen
		
		//################# GERBIL Setup #################
		//Single item for the experiment
		String exp_type = ExpType.A2KB.name();
		String matching_type = Matching.WEAK_ANNOTATION_MATCH.name();

		//Multiple items for the experiment
		LinkedList<String> filenames = new LinkedList<String>(Arrays.asList("bsp1.ttl"));	//TODO sollte die obere FILE-Liste nach dem NIF konvertieren wiederspiegeln nur halt *.ttl
		LinkedList<String> annotators = new LinkedList<String>(Arrays.asList(Annotators.AIDA.name(), Annotators.Dexter.name(), Annotators.FOX.name()));
		LinkedList<String> datasets = new LinkedList<String>(Arrays.asList("DBpediaSpotlight"));
		
		//Keep in mind that uploaded files need to pre-described see down here
		for (String file : filenames)  datasets.add(ExperimentObjectGERBIL.createUploadDataDesc(file));
		
		//Setup object complete
		ExperimentObjectGERBIL exoGERBIL = new ExperimentObjectGERBIL(exp_type, matching_type, annotators, datasets);
		
		
		
			
		
		System.out.println("DISTRIBUTION ORDERED BY KEYVALUE (most left vertical list)");
		
		/*
		 * GENERAL
		 * TODO alle simple Metriken im text_info Objekt speichern und alle von Gerbil (nur) als KL-Div Wert!!!!!
		 * TODO GERBIL Annotatoren auswählen (4 stk.) und als default GERBIL Metriken nutzen.
		 * 
		 * IMPL
		 * TODO Verteilung symbolische Fehler pro Satz 			(m2)	auf raw text	 => Errors like words are crossed by non alnum chars or not closed brackets
		 * TODO GERBIL JSON relevanten content erhalten impl
		 * TODO GERBIL Schnittstelle an main anbinden
		 * TODO Word splitter bauen um full random text zu generieren! Dient als bottom value geg. Gold und NN Texte
		 * TODO Impl cos abstand 2er Vektoren/arrays 
		 * TODO Converter für BASE64 zu UTF-8
		 * TODO NIF genration überarbeiten -> 
		 * 		1.	itsrdf:taIdentRef	-> 	nur 1 url in   
		 * 		2.	nif:isString 		-> 	das markup muss aus dem text raus INDIZE ÄNDERN SICH AUCH DADURCH!!! (dann müssen aber auch die  NIF anchor angepasst werden)
		 *   	3.	wahrscheinlich muss das https raus und durch http ersetzt werden
		 * TODO NIF generation für dataset einbinden
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
			System.out.println("["+annotation_sorted.get(i)[0]+"]\t\t["+annotation_sorted.get(i)[1]+"]");
		}
		
		
		/* M4: Word Distribution over all Sentences */
		wps_sorted = FrequencySorting.sortDist(DistributionProcessing.getWPSDist(sos, sst, language));
		text_info.setSorted_wps_dist(wps_sorted);
		
		System.out.println("######## [Word amount] / [Sentences] ########");
		for (int i = 0; i < wps_sorted.size(); i++) {
			System.out.println("["+wps_sorted.get(i)[0]+"]\t\t["+wps_sorted.get(i)[1]+"]");
		}
		
		
		/*M3: Syntactic error Distribution over all Sentence */
		syn_err_per_sen = DistributionProcessing.calcSimpleSynErrorDist(sentences_cleaned, language);
		text_info.setSorted_synerr_per_sen_dist(syn_err_per_sen);
		
		System.out.println("######## [Syntaxerrors] / [Sentences] ########");
		for (int i = 0; i < syn_err_per_sen.size(); i++) System.out.println("["+syn_err_per_sen.get(i)[0]+"]\t\t["+syn_err_per_sen.get(i)[1]+"]");
		
		
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
		text_info.setWord_per_sentence(SimpleRounding.round((1.0*words.size())/sentences_cleaned.size()));
		text_info.setWord_count(words.size());
		triples_sorted = FrequencySorting.sortByPTL(percentage, wfe.getMap());
		text_info.setWord_distribution(triples_sorted);
		
		System.out.println("######## [Word occurrence] / [Sentences] ########");
		for (int i = 0; i < triples_sorted.size(); i++) System.out.println("["+triples_sorted.get(i).getKey()+"]\t\t["+triples_sorted.get(i).getCount()+"]");
		
		//GERBIL
		//Start process
		JSONObject jsobj_with_upload = HttpController.run(filenames, exoGERBIL);	//here you upload your own dataset
//		JSONObject jsobj_without_upload = run(exoGERBIL);			//here you use a existing dataset from GERBIL
		
		//Presenting output
		System.out.println(jsobj_with_upload.toString());
//		System.out.println(jsobj_without_upload.toString());
		
		
		//TODO danach die finalen Kalkulationen einbinden und das Ergebnis interpretieren!
		
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
