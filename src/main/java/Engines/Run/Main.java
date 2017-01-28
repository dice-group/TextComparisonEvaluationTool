package Engines.Run;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;

import AnnotedText2NIF.IOContent.TextReader;
import AnnotedText2NIF.IOContent.TextWriter;
import Engines.Enums.Annotators;
import Engines.Enums.ExpType;
import Engines.Enums.Matching;
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
		String gold_name 			= "GoldTextWikipedia.txt";						//Gold standard text
		String fragment_name 		= "BVFragment.txt";								//Bottom value text
		String gold_path 			= tr.getResourceFileAbsolutePath(gold_name);
		String fragment_path 		= gold_path.replace(gold_name, fragment_name);
		String rating_out_path 		= gold_path.replace(gold_name, Timestamp.getLocalDateAsString(Timestamp.getCurrentTime())+"_rating");
		
		String[] additional_files;
		String[] default_annotators;
		LinkedList<String> filenames;
		LinkedList<String> annotators;
		LinkedList<Double> ratings;
		LinkedList<TextInformations> experiments_infos;
		LinkedList<TextInformations> no_gold_exp_results = new LinkedList<TextInformations>();
		
		
		//If file is not created, just create a new one!
		if (!new File(fragment_path).exists()) 
			System.out.println("The file dont exist! Create new at "
					+TextWriter.fileWriter(CruelTextGenerator.createRandomFragment(TextReader.fileReader(gold_path)), fragment_path)); 
		
		//TODO if gold is loaded inside a content.prop file just gather informations from there need to implemented maybe as XML
		
		additional_files 			= new String[4];
//		additional_files[0] 		= gold_name;
		additional_files[0] 		= fragment_name;
		additional_files[1] 		= "epoch15.txt";
		additional_files[2] 		= "epoch30.txt";
		additional_files[3] 		= "epoch70Final.txt";
		
		//ATTENTION: always the GOLD TEXT need to be first element of the list! 
		filenames = new LinkedList<String>(Arrays.asList(additional_files));
		
		//The 4 default annotators
		default_annotators = new String[5];
		default_annotators[0] 		= Annotators.AIDA.name();
		default_annotators[1] 		= Annotators.WAT.name();
		default_annotators[2] 		= Annotators.FOX.name();
		default_annotators[3] 		= "TagMe 2";
		default_annotators[4] 		= "DBpedia Spotlight";
		
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
		
		experiments_infos = Pipeline.gather(filenames, annotators, exp_type, matching_type);
		
		//get non gold text exps
		for(int i = 1; i < experiments_infos.size(); i++) no_gold_exp_results.add(experiments_infos.get(i));
		
		ratings = Pipeline.calculater(experiments_infos.getFirst(), experiments_infos, rating_out_path);
		
		System.out.println(ratings);
	}

}
