package AnnotedText2NIF.IOContent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
	
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
		return getClass().getClassLoader().getResource(path).getPath();
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
