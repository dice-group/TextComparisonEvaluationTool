package Web.Controller;

import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

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
	 * @throws InterruptedException 
	 * @throws IOException
	 */
	public static boolean existURL(String url_string) throws IOException, InterruptedException
	{	
		/*
		 * TODO ATTENTION: 
		 * control the url is an entity not a placeholder or a page with a list of entities
		 */
		URL url;
		int responseCode;
		boolean conExCeptionThrown = true, exist = false;
		
		while(conExCeptionThrown)
		{
			try 
			{
				url = new URL(url_string);
				HttpURLConnection huc = (HttpURLConnection) url.openConnection();
				huc.setRequestMethod("HEAD");
				responseCode = huc.getResponseCode();
				
				if (responseCode == 200) 
				{
					huc.disconnect();
					exist = true;
					
				} else 
				{
					huc.disconnect();
					exist = false;
				}
				
				conExCeptionThrown = false;
				
			} catch (SocketTimeoutException | ConnectException exception) 
			{
				System.out.println("Socket or Connect Timeout Exception was thrown!");
				Thread.sleep(TimeUnit.MINUTES.toMillis(1));
				continue;
			}
		}
		
		return exist;
	}
	
	//#############################################################################
	//############################### EXAMPLE #####################################
	//#############################################################################
	
	/*
	 * EXAMPLE of USE
	 */
	public static void main(String[] args) throws IOException, InterruptedException 
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
