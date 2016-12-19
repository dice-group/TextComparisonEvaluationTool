package Web.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import AnnotedText2NIF.IOContent.TextReader;
import Engines.Enums.Annotators;
import Engines.Enums.ExpType;
import Engines.Enums.Matching;
import Web.Objects.ExperimentObjectGERBIL;

/**
 * This class provides HTTP request methods for interaction with GERBIL.
 * @author TTurke
 *
 */
public class HttpController 
{
		private String experiment_result_url;
		private String execute_url;
		public static final String upload_url = "http://gerbil.aksw.org/gerbil/file/upload";
		private ExperimentObjectGERBIL gerbilExp;
		private JSONObject jsonObject;
		
		//#############################################################################
		//############################ CONSTRUCTOR'S ##################################
		//#############################################################################
		
		/**
		 * This constructor starts the whole process (the Experiment) and save all necessary values for further progress.
		 * @param exp_type
		 * @param matching_type
		 * @param annotators
		 * @param datasets
		 * @param exe_url
		 * @throws Exception
		 */
		public HttpController(String exp_type, String matching_type, String[] annotators, String[] datasets, String exe_url) throws Exception
		{
			gerbilExp = new ExperimentObjectGERBIL(exp_type, matching_type, annotators, datasets);
			this.execute_url = exe_url;
			this.jsonObject = doExperiment(gerbilExp, exe_url);
			System.out.println(this.jsonObject);
		}
		
		
		/**
		 * This constructor starts the whole process (the Experiment) and save all necessary values for further progress.
		 * @param exe_url
		 * @param exoGERBIL
		 * @throws Exception
		 */
		public HttpController(String exe_url, ExperimentObjectGERBIL exoGERBIL) throws Exception
		{
			gerbilExp = exoGERBIL;
			this.execute_url = exe_url;
			this.jsonObject = doExperiment(gerbilExp, exe_url);
			System.out.println(this.jsonObject);
		}
	
		//#############################################################################
		//############################ USAGE METHODS ##################################
		//#############################################################################
		
		/**
		 * This method send a get request to GERBIL to start an experiment depending on the given experiment object.
		 * @param experiment
		 * @param url
		 * @return Experiment result page URL as String
		 * @throws Exception
		 */
		public String sendGetExperiment(ExperimentObjectGERBIL experiment, String url) throws Exception 
		{
			String urlParameters = experiment.retJSON().toString();
			String encParam = URLEncoder.encode(urlParameters, "utf-8");

			URL obj = new URL(url + "?experimentData=" + encParam);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			//add method and properties
			con.setRequestMethod("GET");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			System.out.println("CONNECTION: "+con);

			if(con.getResponseCode() == 200)
			{
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

				interpretResponseCode(con.getResponseCode());
				
				return "http://gerbil.aksw.org/gerbil/experiment?id="+response.toString();
			}else{
				System.err.println("RESPONDE CODE ERROR: "+con.getResponseCode());
				return null;
			}
		}
		
		
		/**
		 * This method gather the desired elements from experiment.
		 * ATTENTION: 	Keep in mind that this can take long time caused 
		 * 				by the calculation time of the GERBIL Experiment.
		 *  
		 * @param exp_url
		 * @return
		 * @throws Exception
		 */
		public Elements sendGetStatus(String exp_url) throws Exception 
		{
			URL obj = new URL(exp_url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");
			if(con.getResponseCode() == 200)
			{
				return getDesiredElemJSOUP(exp_url);
			}else{
				return null;
			}
		}

		
		/**
		 * This method gather the desired JSONObject from HTML Domain
		 * @param http
		 * @return Elements
		 * @throws IOException
		 */
		public Elements getDesiredElemJSOUP(String http) throws IOException
		{			
			String elem_name = "body script[type=application/ld+json]";
			Document doc = Jsoup.connect(http).get();
			return doc.select(elem_name);
		}
		
		
		/**
		 * This method generate a JSONObject by a given "well-formed JSON" String inside a HTML DOM Element
		 * @param ele
		 * @return JSONObject
		 */
		public JSONObject getJSON(Elements ele)
		{
			return new JSONObject(ele.first().data());
		}
		
		
		/**
		 * This method check the status of a GERBIL Experiment.
		 * It will return false if its finished. During waiting the Program is in pause mode.
		 * @param http
		 * @param millis
		 * @return false if Experiment finished
		 * @throws IOException
		 * @throws InterruptedException
		 */
		public boolean stillRunning(String http, long millis) throws IOException, InterruptedException
		{
			//resultTable
			String elem_name = "body table[id=resultTable] tbody tr td[style=text-align: center]";
			boolean still_running = Jsoup.connect(http).get().select(elem_name).toString().contains("is still running");
			
			System.out.println("STATUS: Running");
			
			while(still_running)
			{
				System.out.print(".");
				try {
					still_running = Jsoup.connect(http).get().select(elem_name).toString().contains("is still running");
					Thread.sleep(TimeUnit.SECONDS.toMillis(millis));
				} catch (SocketTimeoutException exception) 
				{
					continue;
				}
			}
			
			System.out.println("\nSTATUS: DONE");
			return !still_running;
		}
		
		
		/**
		 * This method return the result of the experiment it starts as JSONObject as fast as the informations are present. 
		 * @param exoGERBIL
		 * @param exe_url
		 * @return Results as JSONObject
		 * @throws Exception
		 */
		public JSONObject doExperiment(ExperimentObjectGERBIL exoGERBIL, String exe_url) throws Exception
		{
			//Report update value
			int millis = 5;
			if(exoGERBIL.getExperiment_annotator().size() > 5) millis = exoGERBIL.getExperiment_annotator().size();
			
			//Send stuff and start experiment
			String exp_url = sendGetExperiment(exoGERBIL, exe_url);
			
			//Save experiment result URL
			setExperiment_result_url(exp_url);
			
			if(exp_url != null)
			{
				System.out.println("Status des Experiments: "+exp_url);
			}else{
				System.err.println("ERROR: No experiment executed!");
				System.exit(0);
			}

			//Check the Experiment status
			stillRunning(exp_url, millis);
			
			//If its finished gather the desired element
			Elements http_elems = sendGetStatus(exp_url);
			
			//Return the element as JSONObject
			return getJSON(http_elems);
		}
		
		
		/**
		 * This method interpret the response code and alert info string to console
		 * @param code
		 */
		public static void interpretResponseCode(int code)
		{
			if(code == 200)
			{
				System.out.println("STATUS: OK ["+code+"]");
			}else{
				System.out.println("STATUS: ERROR ["+code+"]");
			}
		}
		
		
		/**
		 * This method upload a file and execute the experiment by filename, 
		 * URL for upload data and execution of the experiment, by given 
		 * ExperimentObjectGERBIL containing experiment setup.
		 * 
		 * @param filename
		 * @param upload_url
		 * @param exe_url
		 * @param exoGERBIL
		 * @return result as JSONObject
		 * @throws Exception
		 */
		public static JSONObject run(String filename, String upload_url, String exe_url, ExperimentObjectGERBIL exoGERBIL) throws Exception
		{
			TextReader tr = new TextReader();
			String file_location = tr.getResourceFileAbsolutePath(filename);
			
			//Dataset upload
			if(upload_url != null)
			{
				System.out.println("####### UPLOAD: #######");
				int code = UploadController.uploadFile(upload_url, file_location);
				interpretResponseCode(code);
				
			}else{
				System.out.println("####### UPLOAD: #######");
				interpretResponseCode(UploadController.uploadFile(upload_url, file_location));
			}
			
			//Create the controller and start experiment
			System.out.println("####### EXECUTION: #######");
			HttpController http = new HttpController(exe_url, exoGERBIL);
			
			//Return result JSON
			return http.getJsonObject();
		}
		
		
		/**
		 * This method execute an experiment, by given URL and 
		 * ExperimentObjectGERBIL containing experiment setup.
		 * 
		 * @param exe_url
		 * @param exoGERBIL
		 * @return result as JSONObject
		 * @throws Exception
		 */
		public static JSONObject run(String exe_url, ExperimentObjectGERBIL exoGERBIL) throws Exception
		{
			//Create the controller and start experiment
			System.out.println("####### EXECUTION: #######");
			HttpController http = new HttpController(exe_url, exoGERBIL);
			
			//Return result JSON
			return http.getJsonObject();
		}
		
		//#############################################################################
		//########################## GETTERS & SETTERS ################################
		//#############################################################################
		
		public String getExperiment_result_url() {
			return experiment_result_url;
		}

		public void setExperiment_result_url(String experiment_result_url) {
			this.experiment_result_url = experiment_result_url;
		}

		public String getExecute_url() {
			return execute_url;
		}

		public void setExecute_url(String execute_result_url) {
			this.execute_url = execute_result_url;
		}

		public ExperimentObjectGERBIL getGerbilExp() {
			return gerbilExp;
		}

		public void setGerbilExp(ExperimentObjectGERBIL gerbilExp) {
			this.gerbilExp = gerbilExp;
		}

		public JSONObject getJsonObject() {
			return jsonObject;
		}
		
		public void setJsonObject(JSONObject jsonObject) {
			this.jsonObject = jsonObject;
		}

		/*
		 * EXAMPLE of USE
		 */
		public static void main(String[] args) throws Exception 
		{
			String filename = "default1.ttl";
			String upload_url = HttpController.upload_url;
			String exp_type = ExpType.A2KB.name();
			String matching_type = Matching.WEAK_ANNOTATION_MATCH.name();
			String exe_url = "http://gerbil.aksw.org/gerbil/execute";
			
			String[] annotators = new String[]{Annotators.AIDA.name(), Annotators.Dexter.name(), Annotators.FOX.name()};
			
			//Keep in mind that uploaded files need to pre described see down here
			String[] datasets = new String[]{"DBpediaSpotlight", ExperimentObjectGERBIL.createUploadDataDesc(filename)};
			ExperimentObjectGERBIL exoGERBIL = new ExperimentObjectGERBIL(exp_type, matching_type, annotators, datasets);
			
			JSONObject jsobj_with_upload = run(filename, upload_url, exe_url, exoGERBIL);	//here you upload your own dataset
//			JSONObject jsobj_without_upload = run(exe_url, exoGERBIL);						//here you use a existing dataset from GERBIL
			
			//Presenting output
			System.out.println(jsobj_with_upload.toString());
//			System.out.println(jsobj_without_upload.toString());
		}
}
