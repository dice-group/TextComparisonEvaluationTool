package Web.Controller;

import java.io.IOException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import AnnotedText2NIF.IOContent.TextReader;
import Engines.SimpleObjects.SimpleRounding;

/**
 * This class handle the desired information collection from GERBIL JSONObject "@graph" 
 * @author TTurke
 *
 */
public class JSONCollector 
{

	/**
	 * This class collects the Informations and return it as Hashmap;
	 * @param js
	 * @return map of informations
	 */
	public static HashMap<String, Double> collectMetrics(JSONObject js)
	{
		int separator;
		double value;
		JSONArray jsa = js.getJSONArray("@graph");
		HashMap<String, Double>  map = new HashMap<String, Double>();
		
		if(jsa.length() > 0)
		{
			for (int i = 0; i < jsa.length(); i++) 
			{
				separator = -1;
				value = -1;
				
				/*
				 * TODO ATTENTION: 
				 * if the informations about dataset and annotator will be corrected in GERBIL just replace "gerbil:dataset" by "gerbil:annotator"!
				 */
				if(jsa.getJSONObject(i).has("gerbil:microF1") && !jsa.getJSONObject(i).has("subExperimentOf"))
				{
					value = SimpleRounding.round(jsa.getJSONObject(i).getJSONObject("gerbil:microF1").getDouble("@value"));
					separator = jsa.getJSONObject(i).get("gerbil:dataset").toString().lastIndexOf("/")+1;
					map.put(jsa.getJSONObject(i).get("gerbil:dataset").toString().substring(separator), value);
				}
			}
		}
		return map;
	}

	//#############################################################################
	//############################### EXAMPLE #####################################
	//#############################################################################

	/*
	 * EXAMPLE of USE
	 */
	public static void main(String[] args) throws JSONException, IOException 
	{
		JSONObject js = new JSONObject(TextReader.fileReader(new TextReader().getResourceFileAbsolutePath("GoldJSON.txt")));
		HashMap<String, Double> collection = JSONCollector.collectMetrics(js);
		
		
		for(String key : collection.keySet())
		{
			System.out.println("["+key+"]["+collection.get(key)+"]");
		}
		
	}
}
