package Engines.Run;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import org.json.JSONObject;

import AnnotedText2NIF.ConverterEngine.AnnotedTextToNIFConverter;
import AnnotedText2NIF.ConverterEngine.GatherAnnotationInformations;
import AnnotedText2NIF.IOContent.TextReader;
import AnnotedText2NIF.IOContent.TextWriter;
import Engines.SimpleObjects.*;
import Engines.internalEngineParts.WordFrequencyEngine;
import Engines.simpleTextProcessing.CruelTextGenerator;
import Engines.simpleTextProcessing.DevelishParenthesis;
import Engines.simpleTextProcessing.DistributionProcessing;
import Engines.simpleTextProcessing.StanfordTokenizer;
import Engines.simpleTextProcessing.TextConversion;
import Web.Controller.HttpController;
import Web.Controller.JSONCollector;
import Web.Objects.ExperimentObjectGERBIL;
import edu.stanford.nlp.simple.Sentence;
import Engines.Enums.Annotators;
import Engines.Enums.ExpType;
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
	 * My tasks
	 * JUNIT
	 * TODO Junit Test für Wortzähler
	 * TODO Junit Test für KL-Div
	 * TODO Junit Test für quadratischen Fehler/MSE
	 * TODO Junit Test für cos Abstand
	 * 
	 * SIMPLE STUFF
	 * TODO check documentations about correctness (author, description, parameters, return)
	 */
	
	public static void pipeline(LinkedList<String> filenames, LinkedList<String> annotators, String exp_type, String matching_type, String rating_path) throws Exception
	{
		//*************************************************************************************************
		//*************************************************************************************************
		//****************************************** PIPELINE *********************************************
		//*************************************************************************************************
		//*************************************************************************************************
		
		System.out.println("############# PIPE IS PREPARING  #############");
		
		//GENERAL SETUP (VARIABLES)
		
		//Initiate pipeline --> Just Load ONCE! It takes very much time to initiate it! Remind that for usage!!!
		
		
		double rating = Double.NaN;
		TextConversion tc;
		DevelishParenthesis dp;
		GatherAnnotationInformations gai;	
		TextInformations gold_info;
		MetricVectorProcessing gold_mvp,current_mvp = null;
		StanfordTokenizer st;
		ArrayList<Double> current_dist_vec;
		TextReader tr = new TextReader();
		
		LinkedList<String> nameNIFFile = new LinkedList<String>();
		LinkedList<String> resourceFilesAbsolutePaths = new LinkedList<String>();
		LinkedList<String> texts_raws = new LinkedList<String>();
		
		//All file experiment informations and there NIF files 
		LinkedList<TextInformations> experiments_results = new LinkedList<TextInformations>();
		LinkedList<File> experiments_NifFiles = new LinkedList<File>();
		LinkedList<ResultObject> ros = new LinkedList<ResultObject>();
		LinkedList<MetricVectorProcessing> mvps = new LinkedList<MetricVectorProcessing>();
		
		//*************************************************************************************************************************************************
		//FOR EACH FILE
		for(int k = 0; k < filenames.size(); k++)
		{	
			File file;
			String out_file_path;
			TextInformations text_info;
			JSONObject jsobj;
			
			dp = new DevelishParenthesis();
			gai = new GatherAnnotationInformations();
			tc = new TextConversion();
			st = new StanfordTokenizer();
			
			nameNIFFile.add(filenames.get(k).replace(".txt", ".ttl"));
			resourceFilesAbsolutePaths.add(tr.getResourceFileAbsolutePath(filenames.get(k)));
			texts_raws.add(TextReader.fileReader(resourceFilesAbsolutePaths.getLast()));
			
			out_file_path = tr.getResourceFileAbsolutePath(filenames.get(k)).replace(filenames.get(k), nameNIFFile.getLast());
			text_info = new TextInformations(filenames.get(k));
			
			//Multiple items
			LinkedList<String> words;
			LinkedList<String> sentences_cleaned = new LinkedList<String>();
			LinkedList<Sentence> sentence_objects;
			LinkedList<SentenceObject> sos = new LinkedList<SentenceObject>();
			HashMap<String, Integer> pos_tags_dist;
			HashMap<Integer, Integer> word_occurr_dist, annotation_dist;
			
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
			
			
			System.out.println("\n\n###############################################");
			System.out.println("FILE ["+filenames.get(k)+"] STARTED!");
			System.out.println("###############################################");
			
			//*************************************************************************************************************************************************
			//CLEANING
			System.out.println("CLEANING STARTED!");
			
			/* M_2 */ 
			sentence_objects = st.gatherSentences(texts_raws.getLast(), dp, tc);	//Clean Step 3
			text_info.setSymbol_error_dist(tc.getErrors());							//store errors
			System.out.println("===> ["+sentence_objects.size()+" sentences to check!]");
			
			//*************************************************************************************************************************************************
			//PROCESSING
			System.out.println("PROCESSING STARTED!");
			
			// TODO should we use it?
			/* M_0 CURRENTLY NOT USED*/
			text_info.setCanBeUsed((sentence_objects.size() * 100.0)/(st.getRawSentenceSize()));
			System.out.println("Usable senteces===> ["+SimpleRounding.round(text_info.getCanBeUsed())+"%]");
			
			//get sentences and gather words
			sentences_cleaned = st.sentencesAsStrings(sentence_objects);
			words = StanfordTokenizer.gatherWords(sentence_objects);
			
			System.out.println("GENERATING NIF FILE AND CALCULATION SYN ERR DIST STARTED!");
			/* M_3 */
			gai.setSyntax_error_dist(st.getSyn_error_dist());
			gai.addSEDMap(DistributionProcessing.calcSimpleSynErrorDist(sentences_cleaned));
			file = new File(attnifc.getNIFFileBySentences(sentence_objects, out_file_path, gai));
			sos = AnnotedTextToNIFConverter.getSos();	//store this for 2 other metrics
			text_info.setSyn_error_dist(gai.getSyntax_error_dist());
			
			System.out.println("CALCULATION WF STARTED!");
			//calculate word frequency
			wfe.gatherWordFrequencyByList(words);
			
			System.out.println("CALCULATION POS DIST STARTED!");
			/* M_5 */
			pos_tags_dist = st.countPosTagsOccourence(sentence_objects);
			text_info.setPos_tags_dist(pos_tags_dist);
			
			System.out.println("CALCULATION ENTITY DIST STARTED!");
			/* M_6 */
			annotation_dist = DistributionProcessing.getAnnotDist(sos);
			text_info.setAnnotation_dist(annotation_dist);
			
			System.out.println("CALCULATION WPS DIST STARTED!");
			/* M_4 */
			word_occurr_dist = DistributionProcessing.getWPSDist(sos,st);
			text_info.setWords_occurr_distr(word_occurr_dist);
			
			/* M_1 */
			text_info.setSymbol_sent_dist(st.getSymbol_per_sent_dist());
			
			/* 
			 * [NOT in USE currently because to BIG for big files]
			 * M_7: Word Distribution over the text [STORED] 
			 */
			text_info.setWords_distribution(wfe.getMap());	//For the whole text

			System.out.println("CALCULATION GERBIL METRICS STARTED!");
			/* M_GERBIL [STORED] */
			jsobj = HttpController.run(new LinkedList<String>(Arrays.asList(file.getName())), exoGERBIL);
			text_info.setMetrics_GERBIL(JSONCollector.collectMetrics(jsobj));	//Storing
			
			//*************************************************************************************************************************************************
			//STORE ALL RESULTS
			experiments_results.add(text_info);
			experiments_NifFiles.add(file);
			
			
			//*************************************************************************************************************************************************
			//LOCAL PRESENTATION
			
			//General
			System.out.println("\n\n######################### INFO ##########################\n");
			
			System.out.println("Resource:\t\t\t"+text_info.getResource_name());
			System.out.println("Date and Time:\t\t\t"+text_info.getGeneration_date());
			
			if((k+1) < filenames.size()) System.out.println("\n######################### NEXT ##########################\n");
		}
		
		//*************************************************************************************************************************************************
		//CALCULATION
		
		System.out.println("\n\n############## FINAL CALCULATION STARTED! ###############\n");
		gold_info = experiments_results.get(0);
		gold_mvp = new MetricVectorProcessing(gold_info, 6);
		
		for(int cal = 0; cal < experiments_results.size(); cal++)
		{
			current_mvp = new MetricVectorProcessing(experiments_results.get(cal), 6);
			
			//calculate the differences between gold's and the vector's metrics
			current_dist_vec = MetricVectorProcessing.calcDistanceVector(gold_mvp, current_mvp);
			mvps.add(current_mvp);

			//then do cos_distance
			rating = MetricVectorProcessing.rate(current_dist_vec, gold_mvp.getZero_vector());
			System.out.println("File: ["+current_mvp.getName()+"] | Rating: ["+rating+"]");
			ros.add(new ResultObject(rating, current_dist_vec, rating_path+"_"+(cal+1)+".txt", experiments_results.get(cal).getResource_name()));
		}
		
		System.out.println("STORING RESULTS\n");
		TextWriter.writeMVP(gold_mvp, rating_path.replace("_rating", "_gold_nvp")+".content.prop");
		for(int mvs = 1; mvs < mvps.size(); mvs++) TextWriter.writeMVP(current_mvp, rating_path.replace("_rating", "_mvp_")+mvps.get(mvs).getName().replace(".txt", ".content.prop"));
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
		TextReader tr = new TextReader();
		
		String exp_type 			= ExpType.A2KB.name();
		String matching_type 		= Matching.WEAK_ANNOTATION_MATCH.name();
		String gold_name 			= "GoldTextWikipedia.txt";						//Gold standard text
		String fragment_name 		= "BVFragment.txt";								//Bottom value text
		String gold_path 			= tr.getResourceFileAbsolutePath(gold_name);
		String fragment_path 		= gold_path.replace(gold_name, fragment_name);
		String rating_out_path 		= gold_path.replace(gold_name, Timestamp.getLocalDateAsString(Timestamp.getCurrentTime())+"_rating");
		
		//If file is not created, just create a new one!
		if (!new File(fragment_path).exists()) 
			System.out.println("The file dont exist! Create new at "
					+TextWriter.fileWriter(CruelTextGenerator.createRandomFragment(TextReader.fileReader(gold_path)), fragment_path)); 
		
		//TODO if gold is loaded inside a content.prop file just gather informations from there need to implemented maybe as XML
		
//		String[] additional_files 	= new String[1];
		String[] additional_files 	= new String[4];
//		additional_files[0] 		= gold_name;
		additional_files[0] 		= fragment_name;
		additional_files[1] 		= "epoch15.txt";
		additional_files[2] 		= "epoch30.txt";
		additional_files[3] 		= "epoch70Final.txt";
		
		//ATTENTION: always the GOLD TEXT need to be first element of the list! 
		LinkedList<String> filenames = new LinkedList<String>(Arrays.asList(additional_files));
		
		//The 4 default annotators
		String[] default_annotators = new String[5];
		default_annotators[0] 		= Annotators.AIDA.name();
		default_annotators[1] 		= Annotators.WAT.name();
		default_annotators[2] 		= Annotators.FOX.name();
		default_annotators[3] 		= "TagMe 2";
		default_annotators[4] 		= "DBpedia Spotlight";
		
		LinkedList<String> annotators = new LinkedList<String>(Arrays.asList(default_annotators));
		
		System.out.println("#########################################################");
		System.out.println("############# THESE METRICS ARE WE CHECKING #############");
		System.out.println("#########################################################");
		
		System.out.println("[Distribution] M_1: Symbol error cleaning and collection over all sentences");
		System.out.println("[Distribution] M_2: Symbol average over all clean sentences");
		System.out.println("[Distribution] M_3: Syntactic error collection over all sentences");
		System.out.println("[Distribution] M_4: Words over all sentences");
		System.out.println("[Distribution] M_5: POS-Tags collection over all sentences");
		System.out.println("[Distribution] M_6: Entities over all sentences");
		System.out.println("[Values] GERBIL METRICS: "+annotators);
		
		Main.pipeline(filenames, annotators, exp_type, matching_type, rating_out_path);
	}

}
