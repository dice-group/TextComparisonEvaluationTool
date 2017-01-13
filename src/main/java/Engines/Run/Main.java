package Engines.Run;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import org.json.JSONObject;

import AnnotedText2NIF.ConverterEngine.AnnotedTextToNIFConverter;
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
	/*
	 * GENERAL
	 * TODO (Vektor = )alle simple Metriken im text_info Objekt speichern und alle von Gerbil (nur) als KL-Div Wert!!!!!
	 * 
	 * IMPL
	 * TODO GERBIL JSON relevanten content erhalten impl
	 * TODO Impl cos abstand 2er Vektoren/arrays
	 * TODO unterscheide die url und entity structure errors
	 * TODO unterscheide die symbolischen errors
	 * 
	 * JUNIT
	 * TODO Junit Test für Wortzähler
	 * TODO Junit Test für KL-Div
	 * TODO Junit Test für quadratischen Fehler/MSE
	 * TODO Junit Test für cos Abstand
	 * 
	 * SIMPLE STUFF
	 * TODO delete useless vars in TextInformations
	 * TODO control all necesarry informations are stored
	 * TODO check documentations about correctness (author, description, param, return)
	 */
	
	public static void pipeline(Language language, LinkedList<String> filenames, LinkedList<String> annotators, String exp_type, String matching_type) throws Exception
	{
		
		//*************************************************************************************************
		//*************************************************************************************************
		//****************************************** PIPELINE *********************************************
		//*************************************************************************************************
		//*************************************************************************************************
		
		//GENERAL SETUP (VARIABLES)
		//TODO GoldText laden und bottom value text generieren
		//TODO bottom value text und gold text first
		
		//Initiate pipeline --> Just Load ONCE! It takes very much time to initiate it! Remind that for usage!!!
		StanfordSegmentatorTokenizer sst = StanfordSegmentatorTokenizer.create();
		
		//All file experiment informations and there NIF files 
		LinkedList<TextInformations> experiments_results = new LinkedList<TextInformations>();
		LinkedList<File> experiments_NifFiles = new LinkedList<File>();
		
		TextReader tr = new TextReader();
		DevelishParenthesis dp = new DevelishParenthesis();
		GatherAnnotationInformations gai = new GatherAnnotationInformations();	
		
		LinkedList<String> nameNIFFile = new LinkedList<String>();
		LinkedList<String> resourceFilesAbsolutePaths = new LinkedList<String>();
		LinkedList<String> texts_raws = new LinkedList<String>();
		
		//*************************************************************************************************************************************************
		//FOR EACH FILE
		for(String filename : filenames)
		{
			nameNIFFile.add(filename.replace(".txt", ".ttl"));
			resourceFilesAbsolutePaths.add(tr.getResourceFileAbsolutePath(filename));
			texts_raws.add(TextReader.fileReader(resourceFilesAbsolutePaths.getLast()));
			
			String out_file_path = tr.getResourceFileAbsolutePath(filename).replace(filename, nameNIFFile.getLast());
			String text_cleaned;
			
			//Multiple items
			LinkedList<String> words;
			LinkedList<String> sentences_cleaned = new LinkedList<String>();
			LinkedList<SentenceObject> sos = new LinkedList<SentenceObject>();
			LinkedList<int[]> annotation_sorted = new LinkedList<int[]>();
			LinkedList<int[]> wps_sorted = new LinkedList<int[]>();
			LinkedList<int[]> syn_err_per_sen = new LinkedList<int[]>();
			LinkedList<DefinitionObject> dobjs = new LinkedList<DefinitionObject>();
			LinkedList<Triple> triples_sorted = new LinkedList<Triple>();
			LinkedList<PosTagObject> pos_tags = new LinkedList<PosTagObject>();
			TextInformations text_info = new TextInformations(filename);
			HashMap<String, Double> percentage;
			
			//create set and map
			WordFrequencyEngine wfe = new WordFrequencyEngine();

			//Multiple items for the experiment
			LinkedList<String> datasets = new LinkedList<String>(/*Arrays.asList("DBpediaSpotlight")*/);
			
			//Keep in mind that uploaded files need to pre-described see down here
			datasets.add(ExperimentObjectGERBIL.createUploadDataDesc(nameNIFFile.getLast()));
			
			//Setup object complete
			ExperimentObjectGERBIL exoGERBIL = new ExperimentObjectGERBIL(exp_type, matching_type, annotators, datasets);
			
			
			//*************************************************************************************************************************************************
			//CLEANING
			/* M2: symbolische Fehler im Text [STORED] */ 
			text_cleaned = TextConversion.decompose(StanfordSegmentatorTokenizer.formatCleaned(dp.cleanErrorsAndParenthesis(texts_raws.getLast())));
			System.out.println("Cleaned TEXT: \n"+text_cleaned);
			
			// TODO als map abspeichern besser für die auswertung (klammer_fehler, symbolische_fehler)
			text_info.setError_symbol_count(dp.getErrorCount()+TextConversion.error_signs);
			System.out.println("ERROR SIGNS: \n"+text_info.getError_symbol_count());
			
			//*************************************************************************************************************************************************
			//PROCESSING
			System.out.println("DISTRIBUTION ORDERED BY KEYVALUE (most left vertical list)");
			
			//get sentences [STORED]
			sentences_cleaned = StanfordSegmentatorTokenizer.gatherSentences(text_cleaned);
			text_info.setSentence_count(sentences_cleaned.size());
			
			//gather words [NOT STORED]
			words = sst.gatherWords(text_cleaned, language);
			
			/* M5: POS-Tags Distribution over all Sentences [STORED] */
			//gather, sort and store part of speech labels
			//TODO als map abspeichern besser für die auswertung
			pos_tags = wfe.appearancePercentage(FrequencySorting.sortPosTagMap(sst.countPosTagsOccourence(sst.getTokens())), sst.getTokens().size());
			text_info.setPos_tag_objs(pos_tags);
			
//			for (PosTagObject tag : pos_tags) System.out.println("["+tag.getPOS_Tag()+"]\t\t["+tag.getTag_ouccurrence()+"]\t\t["+tag.getTag_oucc_percentage()+"]");
			
			
			for (int i = 0; i < sentences_cleaned.size(); i++) 
			{	
				//gather text annotations and store sentence objects
				dobjs = gai.gatherDefsFast(StanfordSegmentatorTokenizer.formatCleaned(sentences_cleaned.get(i)));
				if(dobjs.size() > 0) sos.add(new SentenceObject(sentences_cleaned.get(i), dobjs.size()));
			}
			
			
			/* M6: Entity Distribution over all Sentence [STORED] */
			//process, sort and store annotation distribution
			//TODO als map abspeichern besser für die auswertung
			annotation_sorted = FrequencySorting.sortDist(DistributionProcessing.getAnnotDist(sos));
			text_info.setSorted_annot_dist(annotation_sorted);
			
//			System.out.println("########  [Entities] / [Sentences] ########");
//			for (int i = 0; i < annotation_sorted.size(); i++) {
//				System.out.println("["+annotation_sorted.get(i)[0]+"]\t\t["+annotation_sorted.get(i)[1]+"]");
//			}
			
			/* M4: Word Distribution over all Sentences */
			//TODO als map abspeichern besser für die auswertung
			wps_sorted = FrequencySorting.sortDist(DistributionProcessing.getWPSDist(sos, sst, language));
			text_info.setSorted_wps_dist(wps_sorted);
			
//			System.out.println("######## [Word amount] / [Sentences] ########");
//			for (int i = 0; i < wps_sorted.size(); i++) {
//				System.out.println("["+wps_sorted.get(i)[0]+"]\t\t["+wps_sorted.get(i)[1]+"]");
//			}
			
			
			/*M3: Syntactic error Distribution over all Sentence */
			//TODO als map abspeichern besser für die auswertung (url, entity_separator, sentence_start_big_char)
			syn_err_per_sen = DistributionProcessing.calcSimpleSynErrorDist(sentences_cleaned, language);
			text_info.setSorted_synerr_per_sen_dist(syn_err_per_sen);
			
//			System.out.println("######## [Syntaxerrors] / [Sentences] ########");
//			for (int i = 0; i < syn_err_per_sen.size(); i++) System.out.println("["+syn_err_per_sen.get(i)[0]+"]\t\t["+syn_err_per_sen.get(i)[1]+"]");
			
			//calculate word frequency
			wfe.gatherWordFrequencyByList(words);
			
			/* M1: Symbol Average over all Sentences */
			text_info.setSymbol_count(text_cleaned.length());
			text_info.setSymbol_count_no_ws(text_cleaned.replaceAll(" ", "").length());
			text_info.setSymbol_per_sentence(text_cleaned.length()/sentences_cleaned.size());
			text_info.setSymbol_per_sentence_no_ws(text_cleaned.replaceAll(" ", "").length()/sentences_cleaned.size());
			
			//calculate word frequency percentage
			//TODO als map abspeichern besser für die auswertung
			percentage = wfe.appearancePercentage(wfe.getMap(), words.size());
			text_info.setWord_per_sentence(SimpleRounding.round((1.0*words.size())/sentences_cleaned.size()));
			text_info.setWord_count(words.size());
			triples_sorted = FrequencySorting.sortByPTL(percentage, wfe.getMap());
			text_info.setWord_distribution(triples_sorted);
			
//			System.out.println("######## [Word occurrence] / [Sentences] ########");
//			for (int i = 0; i < triples_sorted.size(); i++) System.out.println("["+triples_sorted.get(i).getKey()+"]\t\t["+triples_sorted.get(i).getCount()+"]");
			
			
			//Generate NIF file and store it
			File file = new File(AnnotedTextToNIFConverter.getNIFFileBySentences(sentences_cleaned, out_file_path));
			
			//GERBIL
			//Start process
			JSONObject jsobj_with_upload = HttpController.run(new LinkedList<String>(Arrays.asList(file.getName())), exoGERBIL);
			
			//Presenting output
			System.out.println(jsobj_with_upload.toString());
			
			//TODO get infos from JSON and create a map for it
			
			System.out.println(jsobj_with_upload.getDouble("microF1"));
			
			
			
			
			
			
			
			
			
			
			
			
			//*************************************************************************************************************************************************
			//CALCULATION
			
			//TODO compare current to gold standart
			
			//*************************************************************************************************************************************************
			//STORE ALL RESULTS
			experiments_results.add(text_info);
			experiments_NifFiles.add(file);
			
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
		
		//*************************************************************************************************************************************************
		//PRESENTATION
		/* TODO 	
		 * hier werden alle inhalte zu Vektoren umgewandelt und dann schrittweise via KL-Div oder QuadError verarbeitet 
		 * am ende erhält man eine Zahl welche mit dem Cosinus abstand über dem ergebnisvektor berechnet wird.
		 */ 	
	}
	
	
	
	/**
	 * Process pipeline
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception 
	{	
		Language language = Language.EN;
		String exp_type = ExpType.A2KB.name();
		String matching_type = Matching.WEAK_ANNOTATION_MATCH.name();
		LinkedList<String> filenames = new LinkedList<String>(Arrays.asList("epoch70Final.txt"));
		LinkedList<String> annotators = new LinkedList<String>(Arrays.asList(Annotators.AIDA.name(), Annotators.Dexter.name(), Annotators.FOX.name()));
		
		Main.pipeline(language, filenames, annotators, exp_type, matching_type);
	}

}
