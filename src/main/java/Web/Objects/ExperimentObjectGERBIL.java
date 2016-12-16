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
	//{"type":"A2KB","matching":"WEAK_ANNOTATION_MATCH","annotator":["DBpedia Spotlight"],"dataset":["DBpediaSpotlight"]}
	//String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
	
	private String experiment_type;
	private String experiment_matching;
	private LinkedList<String> experiment_annotator = new LinkedList<String>();
	private LinkedList<String> experiment_dataset = new LinkedList<String>();
	
	/**
	 * Constructor
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
	
	
	public static void main(String [] args)
	{
		// DESIRED: {"type":"A2KB","matching":"WEAK_ANNOTATION_MATCH","annotator":["DBpedia Spotlight"],"dataset":["DBpediaSpotlight"]}
		String[] stray = new String[]{"AIDA","DBpedia Spotlight"};
		String[] data = new String[]{"DBpediaSpotlight"};
		ExperimentObjectGERBIL gerbilExp = new ExperimentObjectGERBIL("A2KB", "WEAK_ANNOTATION_MATCH", stray, data);
		System.out.println(gerbilExp.retJSON().toString());
	}
	
}
