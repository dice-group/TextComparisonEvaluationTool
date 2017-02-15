package Engines.Run;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;

import AnnotedText2NIF.IOContent.TextReader;
import AnnotedText2NIF.IOContent.TextWriter;
import Engines.Enums.Annotators;
import Engines.Enums.ExpType;
import Engines.Enums.Matching;
import Engines.IO.PropReader;
import Engines.SimpleObjects.MetricVectorProcessing;
import Engines.SimpleObjects.TextInformations;
import Engines.SimpleObjects.Timestamp;
import Engines.simpleTextProcessing.CruelTextGenerator;

/**
 * This class start the whole process and return all necessary informations.
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
		String gold_name 			= "MichaGoldPreFormated.txt";						//Gold standard text
		String gold_path 			= tr.getResourceFileAbsolutePath(gold_name);
		String rating_out_path 		= gold_path.replace(gold_name, Timestamp.getLocalDateAsString(Timestamp.getCurrentTime())+"_rating");
		String gold_mvp_path		= "WikipediaDumpGold_mvp.content.prop";
		
		PropReader pr = new PropReader();
		
		String[] additional_files;
		String[] default_annotators;
		LinkedList<String> filenames;
		LinkedList<String> annotators;
		LinkedList<Double> ratings;
		LinkedList<TextInformations> experiments_infos;
		LinkedList<TextInformations> no_gold_exp_results = new LinkedList<TextInformations>();
		LinkedList<MetricVectorProcessing> mvps = new LinkedList<MetricVectorProcessing>();
		LinkedList<String> propetyFilesPaths = new LinkedList<String>();
		
		
		//TODO FURTHER PROGRAMMING: gold text properties are stored inside a content.prop text file maybe implement as XML structure => better to use
		//TODO FURTHER PROGRAMMING: maybe a semantic check up 
		
		additional_files 			= new String[1];
		additional_files[0] 		= gold_name;		//use if u need to process your goldfile
//		additional_files[0] 		= "GRU25_sample";
//		additional_files[1] 		= "GRU50_sample";
//		additional_files[2] 		= "GRU75_sample";
//		additional_files[3] 		= "LSTM25_sample";
//		additional_files[4] 		= "LSTM50_sample";
//		additional_files[5] 		= "LSTM75_sample";
//		additional_files[6] 		= "RNN25_sample";
//		additional_files[7] 		= "RNN50_sample";
//		additional_files[8] 		= "RNN75_sample";
//		additional_files[9] 		= "StackedOldRNN_25epoch";
//		additional_files[10] 		= "StackedOldRNN_50epoch";
//		additional_files[11] 		= "StackedOldRNN_75epoch";
//		additional_files[12] 		= "StakedOldLSTM_25";
//		additional_files[13] 		= "StakedOldLSTM_50";
//		additional_files[14] 		= "StakedOldLSTM_75";
		
		propetyFilesPaths.add("14.02.2017_mvp_GRU25_sample.content.prop");
		propetyFilesPaths.add("14.02.2017_mvp_GRU50_sample.content.prop");
		propetyFilesPaths.add("14.02.2017_mvp_GRU75_sample.content.prop");
		propetyFilesPaths.add("14.02.2017_mvp_LSTM25_sample.content.prop");
		propetyFilesPaths.add("14.02.2017_mvp_LSTM50_sample.content.prop");
		propetyFilesPaths.add("14.02.2017_mvp_LSTM75_sample.content.prop");
		propetyFilesPaths.add("14.02.2017_mvp_RNN25_sample.content.prop");
		propetyFilesPaths.add("14.02.2017_mvp_RNN50_sample.content.prop");
		propetyFilesPaths.add("14.02.2017_mvp_RNN75_sample.content.prop");
		
		//ATTENTION: always the GOLD TEXT need to be first element of the list! 
		filenames = new LinkedList<String>(Arrays.asList(additional_files));
		
		//The 4 default annotators
		default_annotators = new String[3];
//		default_annotators[0] 		= Annotators.AIDA.name();		//Funktioniert gerade nicht gut
		default_annotators[0] 		= Annotators.WAT.name();
		default_annotators[1] 		= Annotators.FOX.name();
		default_annotators[2] 		= "DBpedia Spotlight";
		
		annotators = new LinkedList<String>(Arrays.asList(default_annotators));
		
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
		
		System.out.println("\n#########################################################");
		System.out.println("########## PROCESSING FOR THE FOLLOWING FILES ###########");
		System.out.println("#########################################################");
		
		for(String s : additional_files) System.out.println(s);
		
		System.out.println("\n#################################################");
		
		/*
		 * TODO 1. Exclude GERBIL HTTP EXP ULOAD AND START FROM GATHERER!
		 * TODO 2. Parallelize the gatherer after excluding the GERBIL part!
		 * TODO 3. Implement GERBIL EXP upload and start then just keep going other stuff. (maybe)
		 * TODO 4. Allow to load non Goldtext property files for the rating also!
		 * The reason for the 3. "To do" depends on the fact that you can't progress huge files with alot of unknown references inside the *.ttl File.
		 * So you need to avoid the waiting time for there calculation and call the EXP page later to gather the desired informations.
		 * If we do that we will get a huge performance increase.
		 */
		
		/*
		 * HOW TO USE
		 */
		
		//If you have a mvp.class object of your Goldtext you should use this.
//		MetricVectorProcessing goldinf = PropReader.fileReader(pr.getResourceFileAbsolutePath(gold_mvp_path),6); // mvp.class object
//		experiments_infos = Pipeline.gather(goldinf, filenames, annotators, exp_type, matching_type);	
//		ratings = Pipeline.calculater(goldinf, null, experiments_infos, rating_out_path);
//		System.out.println(ratings);
		
		
		//If you need to process the Goldtext you should use this.
		experiments_infos = Pipeline.gather(null, filenames, annotators, exp_type, matching_type);				
		for(int i = 1; i < experiments_infos.size(); i++) no_gold_exp_results.add(experiments_infos.get(i));	//list of all NOT Goldtext information objects
		ratings = Pipeline.calculater(null, experiments_infos.getFirst(), no_gold_exp_results, rating_out_path);
		System.out.println(ratings);
		
		//If you have mvps for all texts and only want to rate them you should use this.
//		MetricVectorProcessing goldinf = PropReader.fileReader(pr.getResourceFileAbsolutePath(gold_mvp_path),6); // mvp.class object
//		for(String path : propetyFilesPaths) mvps.add(PropReader.fileReader(pr.getResourceFileAbsolutePath(path),6));
//		ratings = Pipeline.calculater(goldinf,mvps, rating_out_path);
//		System.out.println(ratings);
	}

}