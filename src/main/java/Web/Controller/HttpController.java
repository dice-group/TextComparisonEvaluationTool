package Web.Controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.jena.atlas.json.io.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;

import Web.Objects.ExperimentObjectGERBIL;

/**
 * This class contains http requests inspired by https://www.mkyong.com/java/how-to-send-http-request-getpost-in-java/
 * @author TTurke
 *
 */
public class HttpController 
{

		/**
		 * This method send a get request to GERBIL with the given content
		 * @param experiment
		 * @param url
		 * @throws Exception
		 */
		public String sendGetExperiment(ExperimentObjectGERBIL experiment, String url) throws Exception 
		{
			String urlParameters = experiment.retJSON().toString();
			String encParam = URLEncoder.encode(urlParameters, "utf-8");

			URL obj = new URL(url + "?experimentData=" + encParam);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			//add reuqest header
			con.setRequestMethod("GET");
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Get parameters : " + urlParameters);
			System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			return "http://gerbil.aksw.org/gerbil/experiment?id="+response.toString();
		}
		
		
		
		// HTTP GET request
		public void sendGetStatus() throws Exception {

			String url = "http://www.google.com/search?q=mkyong";

			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");

			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			//print result
			System.out.println(response.toString());

		}


		public static void main(String[] args) throws Exception 
		{
			HttpController http = new HttpController();
			String[] stray = new String[]{"AIDA","DBpedia Spotlight"};
			String[] data = new String[]{"DBpediaSpotlight"};
			ExperimentObjectGERBIL gerbilExp = new ExperimentObjectGERBIL("A2KB", "WEAK_ANNOTATION_MATCH", stray, data);
			String URL = "http://gerbil.aksw.org/gerbil/execute";

			System.out.println("\nTesting 1 - Send Http GET request");
			System.out.println("Status des Experiments: "+http.sendGetExperiment(gerbilExp, URL));
			
		}
}
