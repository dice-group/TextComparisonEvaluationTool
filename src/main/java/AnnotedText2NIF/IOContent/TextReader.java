package AnnotedText2NIF.IOContent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;
	
/**
 * Simple file Reader for text files.
 * @author TTurke
 *
 */
public class TextReader 
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
			br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF8"));
			while ((sCurrentLine = br.readLine()) != null) str += sCurrentLine;

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
}
