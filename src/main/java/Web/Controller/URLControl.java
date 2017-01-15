package Web.Controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class is used to control the existence of an URL.
 * @author TTurke
 *
 */
public class URLControl 
{
	/**
	 * This method allow to check the existence of an URL
	 * @param url_string
	 * @return boolean for existence
	 * @throws IOException
	 */
	public static boolean existURL(String url_string) throws IOException
	{
		/*
		 * TODO ATTENTION: 
		 * control the url is an entity not a placeholder or a page with a list of entities
		 */
		URL url = new URL(url_string);
		HttpURLConnection huc = (HttpURLConnection) url.openConnection();
		huc.setRequestMethod("HEAD");

		int responseCode = huc.getResponseCode();
		
		if (responseCode == 200) 
		{
			return true;
			
		} else 
		{
			return false;
		}
	}
	
	//#############################################################################
	//############################### EXAMPLE #####################################
	//#############################################################################
	
	/*
	 * EXAMPLE of USE
	 */
	public static void main(String[] args) throws IOException 
	{
		
		String url1 = "https://en.wikipedia.org/wiki/trade_political_democratic_provincial_site";
		String url2 = "https://en.wikipedia.org/wiki/south_africa";
		String url3 = "https://en.wikipedia.org/wiki/Nassau";
		String url4 = "https://en.wikipedia.org/wiki/archaeo_grace_season";
		
		String[] uris = new String[]{url1, url2, url3, url4};
		
		for (int i = 0; i < uris.length; i++) 
		{
			System.out.println(URLControl.existURL(uris[i]));
		}
		
		
	}

}
