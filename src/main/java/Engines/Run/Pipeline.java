package Engines.Run;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

import org.json.JSONObject;

import AnnotedText2NIF.ConverterEngine.AnnotedTextToNIFConverter;
import AnnotedText2NIF.ConverterEngine.GatherAnnotationInformations;
import AnnotedText2NIF.IOContent.TextReader;
import AnnotedText2NIF.IOContent.TextWriter;
import Engines.SimpleObjects.MetricVectorProcessing;
import Engines.SimpleObjects.ResultObject;
import Engines.SimpleObjects.SentenceObject;
import Engines.SimpleObjects.TextInformations;
import Engines.internalEngineParts.WordFrequencyEngine;
import Engines.simpleTextProcessing.DevelishParenthesis;
import Engines.simpleTextProcessing.DistributionProcessing;
import Engines.simpleTextProcessing.StanfordTokenizer;
import Engines.simpleTextProcessing.TextConversion;
import Web.Controller.HttpController;
import Web.Controller.JSONCollector;
import Web.Objects.ExperimentObjectGERBIL;
import edu.stanford.nlp.simple.Sentence;

public class Pipeline 
{
	
	public static LinkedList<TextInformations> gather(LinkedList<String> filenames, LinkedList<String> annotators, String exp_type, String matching_type) throws Exception
	{
		//*************************************************************************************************
				//*************************************************************************************************
				//****************************************** PIPELINE *********************************************
				//*************************************************************************************************
				//*************************************************************************************************
				
				System.out.println("############# PIPE IS PREPARING  #############");
				
				//GENERAL SETUP (VARIABLES)

				//Initiate pipeline --> Just Load ONCE! It takes very much time to initiate it! Remind that for usage!!!
				
				TextConversion tc;
				DevelishParenthesis dp;
				GatherAnnotationInformations gai;	

				StanfordTokenizer st;
				TextReader tr = new TextReader();
				
				LinkedList<String> nameNIFFile = new LinkedList<String>();
				LinkedList<String> resourceFilesAbsolutePaths = new LinkedList<String>();
				LinkedList<String> texts_raws = new LinkedList<String>();
				
				//All file experiment informations and there NIF files 
				LinkedList<TextInformations> experiments_results = new LinkedList<TextInformations>();

				
				//*************************************************************************************************************************************************
				
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
					System.out.println("Usable senteces===> ["+text_info.getCanBeUsed()+"]");
					
					//get sentences and gather words
					sentences_cleaned = st.sentencesAsStrings(sentence_objects);
					words = StanfordTokenizer.gatherWords(sentence_objects);
					
					System.out.println("GENERATING NIF FILE AND CALCULATION SYN ERR DIST STARTED!");
					/* M_3 */
					gai.setSyntax_error_dist(st.getSyn_error_dist());
					gai.addSEDMap(DistributionProcessing.calcSimpleSynErrorDist(sentences_cleaned));
					file = new File(attnifc.getNIFFileBySentences(sentence_objects, out_file_path, gai));
					text_info.setNif_path(file.getAbsolutePath());
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
					
					//*************************************************************************************************************************************************
					//LOCAL PRESENTATION
					
					//General
					System.out.println("\n\n######################### INFO ##########################\n");
					
					System.out.println("Resource:\t\t\t"+text_info.getResource_name());
					System.out.println("Date and Time:\t\t\t"+text_info.getGeneration_date());
					
					if((k+1) < filenames.size()) System.out.println("\n######################### NEXT ##########################\n");
				}
				
				return experiments_results;
	}
	
	
	/**
	 * This method calculates the arithmetical mean (named rating) of all texts towards the the gold text.
	 * @param experiments_results
	 * @param rating_path
	 */
	public static void calculater(TextInformations gold_exp_result, LinkedList<TextInformations> experiments_results, String rating_path)
	{
		LinkedList<Double> ratings = new LinkedList<Double>();
		
		MetricVectorProcessing current_mvp = null;
		MetricVectorProcessing gold_mvp = new MetricVectorProcessing(gold_exp_result, 6);
		
		ArrayList<Double> current_dist_vec;
		LinkedList<ResultObject> ros = new LinkedList<ResultObject>();
		LinkedList<MetricVectorProcessing> mvps = new LinkedList<MetricVectorProcessing>();
				
		System.out.println("\n\n############## CALCULATION STARTED ###############\n");
				
		for(int cal = 0; cal < experiments_results.size(); cal++)
		{
			current_mvp = new MetricVectorProcessing(experiments_results.get(cal), 6);
			
			//calculate the differences between gold's and the vector's metrics
			current_dist_vec = MetricVectorProcessing.calcDistanceVector(gold_mvp, current_mvp);
			mvps.add(current_mvp);
			
			//TODO change to arithmetical mean
			ratings.add(MetricVectorProcessing.rate(current_dist_vec, gold_mvp.getZero_vector()));
			ros.add(new ResultObject(ratings.getLast(), current_dist_vec, rating_path+"_"+(cal+1)+".txt", experiments_results.get(cal).getResource_name()));
			
			System.out.println("File: ["+current_mvp.getName()+"] | Rating: ["+ratings.getLast()+"]");
		}
		
		System.out.println("\n\n############## STORING THE RESULTS ###############\n");
		
		TextWriter.writeMVP(gold_mvp, rating_path.replace("_rating", "_gold_nvp")+".content.prop");
		for(int mvs = 1; mvs < mvps.size(); mvs++) TextWriter.writeMVP(current_mvp, rating_path.replace("_rating", "_mvp_")+mvps.get(mvs).getName().replace(".txt", ".content.prop"));
		TextWriter.writeRating(ros);
	}
	
}
