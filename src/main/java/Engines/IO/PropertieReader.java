package Engines.IO;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLDecoder;

public class PropertieReader 
{
	
	/**
	 * This method return the full absolute path of the given file path
	 * @param path
	 * @return full file path
	 * @throws IOException
	 */
	public String getResourceFileAbsolutePath(String path) throws IOException 
	{
		return URLDecoder.decode(getClass().getClassLoader().getResource(path).getPath());
	}
	
	/**
	 * Simple file reader
	 * @param path
	 * @return Content String
	 */
	public static String fileReader(String path)
	{
		BufferedReader br = null;
		String str = "";

		try {

			String sCurrentLine;
			br = new BufferedReader(new FileReader(path));
			
			while ((sCurrentLine = br.readLine()) != null)
			{ 
				if()
				{
					str += sCurrentLine;
				}
			}
			

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return str;
	}
	
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub

	}
}
