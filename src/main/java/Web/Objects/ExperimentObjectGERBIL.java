package Web.Objects;

import java.util.Arrays;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * This class provide the generation of a JSONObject containing all information for a GERBIL experiment.
 * @author TTurke
 *
 */
public class ExperimentObjectGERBIL 
{
	
	private String experiment_type;
	private String experiment_matching;
	private LinkedList<String> experiment_annotator = new LinkedList<String>();
	private LinkedList<String> experiment_dataset = new LinkedList<String>();
	
	//#############################################################################
	//############################ CONSTRUCTOR'S ##################################
	//#############################################################################
	
	/**
	 * Constructor
	 * Attention: 
	 * If you want to use uploaded files convert there description with the method createUploadDataDesc(full_filename) at first!
	 * @param et
	 * @param em
	 * @param ea
	 * @param ed
	 */
	public ExperimentObjectGERBIL(String et, String em, String ea, String ed)
	{
		this.experiment_type = et;
		this.experiment_matching = em;
		this.experiment_annotator = new LinkedList<String>(Arrays.asList(ea));
		this.experiment_dataset = new LinkedList<String>(Arrays.asList(ed));
	}
	
	/**
	 * Constructor
	 *  Attention: 
	 * If you want to use uploaded files convert there description with the method createUploadDataDesc(full_filename) at first!
	 * After that you need to add them to the dataset array you'll input in this constructor! 
	 * @param et
	 * @param em
	 * @param ea
	 * @param ed
	 */
	public ExperimentObjectGERBIL(String et, String em, LinkedList<String> ea, LinkedList<String> ed)
	{
		this.experiment_type = et;
		this.experiment_matching = em;
		this.experiment_annotator = ea;
		this.experiment_dataset = ed;
	}
	
	/**
	 * Constructor
	 * Attention: 
	 * If you want to use uploaded files convert there description with the method createUploadDataDesc(full_filename) at first!
	 * After that you need to add them to the dataset list you'll input in this constructor! 
	 * @param et
	 * @param em
	 * @param ea
	 * @param ed
	 */
	public ExperimentObjectGERBIL(String et, String em, String[] ea, String[] ed)
	{
		this.experiment_type = et;
		this.experiment_matching = em;
		this.experiment_annotator = new LinkedList<String>(Arrays.asList(ea));
		this.experiment_dataset = new LinkedList<String>(Arrays.asList(ed));;
	}
	
	//#############################################################################
	//############################ USAGE METHODS ##################################
	//#############################################################################
	
	/**
	 * This class generate a JSONObject from given 
	 * @return Experiments setup as JSONObject
	 */
	public JSONObject retJSON()
	{
		JSONObject obj = new JSONObject();
		obj.put("type", getExperiment_type());
		
		obj.put("matching", getExperiment_matching());
		
		JSONArray annotators = new JSONArray();
		
		for (int i = 0; i < getExperiment_annotator().size(); i++)
		{
			annotators.put(getExperiment_annotator().get(i));
		}
		
		obj.put("annotator", annotators);
		
		obj.put("dataset", getExperiment_dataset());
			
		return obj;
	}
	
	/**
	 * This method return the correct description form for JSON dataset's identification of uploaded files.
	 * @param full_file_name
	 * @return dataset identification of uploaded files
	 */
	public static String createUploadDataDesc(String full_file_name)
	{
		return "NIFDS_"+full_file_name+"("+full_file_name+")";
	}
	
	/**
	 * This method return the correct description form for JSON dataset's identification of uploaded files.
	 * Attention: This is only used if the upload also get the unique name!
	 * @param unique_name
	 * @param full_file_name
	 * @return dataset identification of uploaded files
	 */
	public static String createUploadDataDesc(String unique_name,String full_file_name)
	{
		return "NIFDS_"+unique_name+"("+full_file_name+")";
	}

	//#############################################################################
	//########################## GETTERS & SETTERS ################################
	//#############################################################################

	public String getExperiment_type() {
		return experiment_type;
	}

	public void setExperiment_type(String experiment_type) {
		this.experiment_type = experiment_type;
	}

	public String getExperiment_matching() {
		return experiment_matching;
	}

	public void setExperiment_matching(String experiment_matching) {
		this.experiment_matching = experiment_matching;
	}

	public LinkedList<String> getExperiment_annotator() {
		return experiment_annotator;
	}

	public void setExperiment_annotator(LinkedList<String> experiment_annotator) {
		this.experiment_annotator = experiment_annotator;
	}

	public LinkedList<String> getExperiment_dataset() {
		return experiment_dataset;
	}

	public void setExperiment_dataset(LinkedList<String> experiment_dataset) {
		this.experiment_dataset = experiment_dataset;
	}
	
	public void addToExperiment_dataset(LinkedList<String> another_experiment_datasets) {
		this.experiment_dataset.addAll(another_experiment_datasets);
	}
	
	public void addToExperiment_dataset(String[] another_experiment_datasets) {
		this.experiment_dataset.addAll(Arrays.asList(another_experiment_datasets));
	}
	
	public void addToExperiment_dataset(String another_experiment_dataset) {
		this.experiment_dataset.add(another_experiment_dataset);
	}

	
	/*
	 * EXAMPLE of USE
	 */
	public static void main(String [] args)
	{
		String[] stray = new String[]{"AIDA","DBpedia Spotlight"};
		String[] data = new String[]{"DBpediaSpotlight"};
		ExperimentObjectGERBIL gerbilExp = new ExperimentObjectGERBIL("A2KB", "WEAK_ANNOTATION_MATCH", stray, data);
		gerbilExp.addToExperiment_dataset(createUploadDataDesc("default1.ttl"));
		System.out.println(gerbilExp.retJSON().toString());
	}
	
}
