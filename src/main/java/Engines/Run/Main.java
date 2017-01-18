package Engines.Run;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import org.json.JSONObject;

import AnnotedText2NIF.ConverterEngine.AnnotedTextToNIFConverter;
import AnnotedText2NIF.ConverterEngine.DefinitionObject;
import AnnotedText2NIF.ConverterEngine.GatherAnnotationInformations;
import AnnotedText2NIF.IOContent.TextReader;
import AnnotedText2NIF.IOContent.TextWriter;
import Engines.SimpleObjects.*;
import Engines.internalEngineParts.WordFrequencyEngine;
import Engines.simpleTextProcessing.CruelTextGenerator;
import Engines.simpleTextProcessing.DistributionProcessing;
import Engines.simpleTextProcessing.StanfordTokenizer;
import Engines.simpleTextProcessing.TextConversion;
import Web.Controller.HttpController;
import Web.Controller.JSONCollector;
import Web.Objects.ExperimentObjectGERBIL;
import edu.stanford.nlp.simple.Sentence;
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
	 * JUNIT
	 * TODO Junit Test für Wortzähler
	 * TODO Junit Test für KL-Div
	 * TODO Junit Test für quadratischen Fehler/MSE
	 * TODO Junit Test für cos Abstand
	 * 
	 * SIMPLE STUFF
	 * TODO check documentations about correctness (author, description, parameters, return)
	 */
	
	public static void pipeline(Language language, LinkedList<String> filenames, LinkedList<String> annotators, String exp_type, String matching_type, String rating_path) throws Exception
	{
		//*************************************************************************************************
		//*************************************************************************************************
		//****************************************** PIPELINE *********************************************
		//*************************************************************************************************
		//*************************************************************************************************
		
		//GENERAL SETUP (VARIABLES)

		//Initiate pipeline --> Just Load ONCE! It takes very much time to initiate it! Remind that for usage!!!
		TextConversion tc = new TextConversion();
		
		//All file experiment informations and there NIF files 
		LinkedList<TextInformations> experiments_results = new LinkedList<TextInformations>();
		LinkedList<File> experiments_NifFiles = new LinkedList<File>();
		
		TextReader tr = new TextReader();
		DevelishParenthesis dp = new DevelishParenthesis();
		GatherAnnotationInformations gai = new GatherAnnotationInformations();	
		
		LinkedList<String> nameNIFFile = new LinkedList<String>();
		LinkedList<String> resourceFilesAbsolutePaths = new LinkedList<String>();
		LinkedList<String> texts_raws = new LinkedList<String>();
		TextInformations gold_info;
		MetricVectorProcessing gold_nvp;
		MetricVectorProcessing current_nvp;
		ArrayList<Double> current_dist_vec;
		LinkedList<ResultObject> ros = new LinkedList<ResultObject>();
		double rating = Double.NaN;
		
		
		
		//*************************************************************************************************************************************************
		//FOR EACH FILE
		for(int k = 0; k < filenames.size(); k++)
		{	
			System.out.println("FILE ["+filenames.get(k)+"] STARTED!");
			nameNIFFile.add(filenames.get(k).replace(".txt", ".ttl"));
			resourceFilesAbsolutePaths.add(tr.getResourceFileAbsolutePath(filenames.get(k)));
			texts_raws.add(TextReader.fileReader(resourceFilesAbsolutePaths.getLast()));
			
			String out_file_path = tr.getResourceFileAbsolutePath(filenames.get(k)).replace(filenames.get(k), nameNIFFile.getLast());
			String text_cleaned, text_half_cleaned;
			File file;
			
			//Multiple items
			LinkedList<String> words;
			LinkedList<String> sentences_cleaned = new LinkedList<String>();
			ArrayList<Sentence> sentence_objects;
			LinkedList<SentenceObject> sos = new LinkedList<SentenceObject>();
			LinkedList<DefinitionObject> dobjs = new LinkedList<DefinitionObject>();
			TextInformations text_info = new TextInformations(filenames.get(k));
			HashMap<String, Integer> pos_tags_dist;
			HashMap<Integer, Integer> word_occurr_dist, annotation_dist;
			HashMap<Character, Integer> symbol_error_dist;
			
//			HashMap<String, Double> percentage;
//			LinkedList<int[]> annotation_sorted = new LinkedList<int[]>();
//			LinkedList<int[]> wps_sorted = new LinkedList<int[]>();
//			LinkedList<int[]> syn_err_per_sen = new LinkedList<int[]>();
//			LinkedList<Triple> triples_sorted = new LinkedList<Triple>();
//			LinkedList<PosTagObject> pos_tags = new LinkedList<PosTagObject>();
			
			//create set and map
			WordFrequencyEngine wfe = new WordFrequencyEngine();
			
			//init NIF class
			AnnotedTextToNIFConverter attnifc = new AnnotedTextToNIFConverter();

			//Multiple items for the experiment
			LinkedList<String> datasets = new LinkedList<String>(/*Arrays.asList("DBpediaSpotlight")*/);
			
			//Keep in mind that uploaded files need to pre-described see down here
			datasets.add(ExperimentObjectGERBIL.createUploadDataDesc(nameNIFFile.getLast()));
			
			//Setup object complete
			ExperimentObjectGERBIL exoGERBIL = new ExperimentObjectGERBIL(exp_type, matching_type, annotators, datasets);
			
			
			//*************************************************************************************************************************************************
			//CLEANING
			System.out.println("CLEANING STARTED!");
			
			/* M_2: symbolische Fehler im Text [STORED] */ 
			System.out.println("Part 1");
			text_half_cleaned = dp.cleanErrorsAndParenthesis(texts_raws.getLast());	//Clean Step 1
			tc.setErrors(dp.getErrors());	//collect errors
			System.out.println("Part 2");
			text_cleaned = tc.decompose(text_half_cleaned);	//Clean Step 2
			symbol_error_dist = tc.getErrors();	//collect errors
			text_info.setSymbol_error_dist(symbol_error_dist);	//store errors
			
			//*************************************************************************************************************************************************
			//PROCESSING
			System.out.println("PROCESSING STARTED!");
//			System.out.println("DISTRIBUTION ORDERED BY KEYVALUE (most left vertical list)");
			
			//get sentences and gather words
			sentence_objects = StanfordTokenizer.gatherSentences(text_cleaned);
			sentences_cleaned = StanfordTokenizer.sentencesAsStrings(sentence_objects);
			words = StanfordTokenizer.gatherWords(sentence_objects);
			
			System.out.print("GENERATING NIF FILE ");
			System.out.println("AND CALCULATION SYN ERR DIST STARTED!");
			/* M_3: Syntactic error Distribution over all Sentence [STORED] */
			gai.setSyntax_error_dist(DistributionProcessing.calcSimpleSynErrorDist(sentences_cleaned, language));
			file = new File(attnifc.getNIFFileBySentences(sentences_cleaned, out_file_path, gai));
			text_info.setSyn_error_dist(gai.getSyntax_error_dist());
			
			System.out.println("CALCULATION WF STARTED!");
			//calculate word frequency
			wfe.gatherWordFrequencyByList(words);
			
			System.out.println("CALCULATION POS DIST STARTED!");
			/* M_5: POS-Tags Distribution over all Sentences [STORED] */
			pos_tags_dist = StanfordTokenizer.countPosTagsOccourence(sentence_objects);
			text_info.setPos_tags_dist(pos_tags_dist);
			
			System.out.println("URL CONTROL STARTED!");
			/*
			 * ATTENTION: 
			 * This part takes time because of the URL real time control
			 */
			for (int i = 0; i < sentence_objects.size(); i++) 
			{	
				//gather text annotations and store sentence objects
				dobjs = gai.gatherDefsFast(sentence_objects.get(i).text());
				if(dobjs.size() > 0) sos.add(new SentenceObject(sentence_objects.get(i), dobjs.size()));
			}
			
			System.out.println("CALCULATION ENTITY DIST STARTED!");
			/* M_6: Entity Distribution over all Sentence [STORED] */
			annotation_dist = DistributionProcessing.getAnnotDist(sos);
			text_info.setAnnotation_dist(annotation_dist);
			
			System.out.println("CALCULATION WPS DIST STARTED!");
			/* M_4: Word Distribution over all Sentences [STORED] */
			word_occurr_dist = DistributionProcessing.getWPSDist(sos, language);
			text_info.setWords_occurr_distr(word_occurr_dist);
			
			//TODO soll das auch eine Distribution werden?
			/* M_1: Symbol Average over all Sentences [STORED] */
			text_info.setSymbol_count(text_cleaned.length());
			
			/* General Informations [STORED] */
			text_info.setSymbol_count_no_ws(text_cleaned.replaceAll(" ", "").length());
			text_info.setSymbol_per_sentence(text_cleaned.length()/sentences_cleaned.size());
			text_info.setSymbol_per_sentence_no_ws(text_cleaned.replaceAll(" ", "").length()/sentences_cleaned.size());
			
			/* 
			 * [NOT in USE currently because to BIG for big files]
			 * M_7: Word Distribution over the text [STORED] 
			 */
			text_info.setWord_count(words.size());
			text_info.setWords_distribution(wfe.getMap());	//Storing
			text_info.setWord_per_sentence(SimpleRounding.round((1.0*words.size())/sentences_cleaned.size()));

			System.out.println("CALCULATION GERBIL METRICS STARTED!");
			//TODO später für gold text ausschließen da wir den aus Zeitgründen separat vorher berechnen müssen
			/* M_GERBIL [STORED] */
			JSONObject jsobj = HttpController.run(new LinkedList<String>(Arrays.asList(file.getName())), exoGERBIL);
			text_info.setMetrics_GERBIL(JSONCollector.collectMetrics(jsobj));	//Storing
			
			//*************************************************************************************************************************************************
			//STORE ALL RESULTS
			experiments_results.add(text_info);
			experiments_NifFiles.add(file);
			
			
			//*************************************************************************************************************************************************
			//LOCAL PRESENTATION
			
			
			//M_5
//			pos_tags = wfe.appearancePercentage(FrequencySorting.sortPosTagMap(pos_tags_dist), sst.getTokens().size());
//			for (PosTagObject tag : pos_tags) System.out.println("["+tag.getPOS_Tag()+"]\t\t["+tag.getTag_ouccurrence()+"]\t\t["+tag.getTag_oucc_percentage()+"]");
			
			//M_6
//			annotation_sorted = FrequencySorting.sortDist(DistributionProcessing.getAnnotDist(sos));
//			System.out.println("########  [Entities] / [Sentences] ########");
//			for (int i = 0; i < annotation_sorted.size(); i++) {
//				System.out.println("["+annotation_sorted.get(i)[0]+"]\t\t["+annotation_sorted.get(i)[1]+"]");
//			}
			
			//M_4
//			wps_sorted = FrequencySorting.sortDist(word_occurr_dist);
//			System.out.println("######## [Word amount] / [Sentences] ########");
//			for (int i = 0; i < wps_sorted.size(); i++) {
//				System.out.println("["+wps_sorted.get(i)[0]+"]\t\t["+wps_sorted.get(i)[1]+"]");
//			}
			
			//M_3
//			syn_err_per_sen = FrequencySorting.sortDist(syn_error_dist);
//			System.out.println("######## [Syntaxerrors] / [Sentences] ########");
//			for (int i = 0; i < syn_err_per_sen.size(); i++) System.out.println("["+syn_err_per_sen.get(i)[0]+"]\t\t["+syn_err_per_sen.get(i)[1]+"]");
			
			//M_7 calculate word frequency percentage
//			percentage = wfe.appearancePercentage(wfe.getMap(), words.size());
//			triples_sorted = FrequencySorting.sortByPTL(percentage, wfe.getMap());
//			System.out.println("######## [Word occurrence] / [Sentences] ########");
//			for (int i = 0; i < triples_sorted.size(); i++) System.out.println("["+triples_sorted.get(i).getKey()+"]\t\t["+triples_sorted.get(i).getCount()+"]");
			
			//General
//			System.out.println("\n\n######################### INFO ##########################\t\t\t\n");
//			System.out.println("Resource:\t\t\t"+text_info.getResource_name());
//			System.out.println("Date and Time:\t\t\t"+text_info.getLocalDateAsString(text_info.getGeneration_date()));
//			System.out.println("Words count:\t\t\t"+text_info.getWord_count());
//			System.out.println("Sentence count:\t\t\t"+text_info.getSentence_count());
//			System.out.println("Symbol count:\t\t\t"+text_info.getSymbol_count());
//			System.out.println("Symbol count nws:\t\t"+text_info.getSymbol_count_no_ws());
//			System.out.println("Symbol average / Sentence:\t"+text_info.getSymbol_per_sentence());
//			System.out.println("Symbol avg nws / Sentence:\t"+text_info.getSymbol_per_sentence_no_ws());
//			System.out.println("Word per Sentence:\t\t"+text_info.getWord_per_sentence());
		}
		
		//*************************************************************************************************************************************************
		//CALCULATION
		
		System.out.println("FINAL CALCULATION STARTED!");
		gold_info = experiments_results.get(0);
		gold_nvp = new MetricVectorProcessing(gold_info, 6);
		
		
		for(int cal = 0; cal < experiments_results.size(); cal++)
		{
			//get current metrics
			current_nvp = new MetricVectorProcessing(experiments_results.get(cal), 6);
			
			//calculate the differences between gold's and the vector's metrics
			current_dist_vec = MetricVectorProcessing.calcDistanceVector(gold_nvp, current_nvp);
			
			//then do cos_distance
			rating = MetricVectorProcessing.rate(current_dist_vec, gold_nvp.getZero_vector());
			ros.add(new ResultObject(rating, current_dist_vec, rating_path+"_"+(cal+1)+".txt"));
		}
		
		System.out.println("STORING RESULTS\n");
		TextWriter.writeRating(ros);
	}
	
	
	//##################################################################################
	//#################################### EXAMPLE #####################################
	//##################################################################################
	
	/**
	 * RUN IT!
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception 
	{	
		Language language = Language.EN;
		TextReader tr = new TextReader();
		
		String exp_type = ExpType.A2KB.name();
		String matching_type = Matching.WEAK_ANNOTATION_MATCH.name();
		String gold_name = "GoldTextWikipedia.txt";								//Gold standard text
		String fragment_name = "BVFragment.txt";								//Bottom value text
		String gold_path = tr.getResourceFileAbsolutePath(gold_name);
		String fragment_path = gold_path.replace(gold_name, fragment_name);
		String rating_out_path = gold_path.replace(gold_name, Timestamp.getLocalDateAsString(Timestamp.getCurrentTime())+"_rating");
		
		//If file is not created, just create a new one!
		if (!new File(fragment_path).exists()) 
		{
			System.out.println("the file do not exist!");
			TextWriter.fileWriter(CruelTextGenerator.createRandomFragment(TextReader.fileReader(gold_path)), fragment_path);
		}else{
			System.out.println("the file already exist!");
		}

		
//		String[] additional_files = new String[5];
		String[] additional_files = new String[1];
//		additional_files[0] = gold_name;
		additional_files[0] = "epoch70Final.txt";
//		additional_files[1] = fragment_name;
//		additional_files[2] = "epoch15.txt";
//		additional_files[3] = "epoch30.txt";
//		additional_files[4] = "epoch70Final.txt";
		
		//ATTENTION: always the GOLD TEXT need to be first element of the list! 
		LinkedList<String> filenames = new LinkedList<String>(Arrays.asList(additional_files));
		
		//The 4 default annotators
		String[] default_annotators = new String[5];
		default_annotators[0] = Annotators.AIDA.name();
		default_annotators[1] = Annotators.WAT.name();
		default_annotators[2] = Annotators.FOX.name();
		default_annotators[3] = "TagMe 2";
		default_annotators[4] = "DBpedia Spotlight";
		
		LinkedList<String> annotators = new LinkedList<String>(Arrays.asList(default_annotators));
		
		
		Main.pipeline(language, filenames, annotators, exp_type, matching_type, rating_out_path);
	}

}
