package AnnotedText2NIF.IOContent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
	
/**
 * Simple file Reader for *.txt files.
 * @author TTurke
 *
 */
public class TextReader 
{
	/**
	 * This method returns the heading file of the project 
	 * @return
	 */
	public String getResourceDirectoryPath() 
	{
		return new File(getClass().getClassLoader().getResource("").getFile()).getParentFile().getParentFile()+"\\src\\main\\resources\\";
	}
	
	public String getResourceFileAbsolutePath(String path) throws IOException 
	{
		return new File(getClass().getClassLoader().getResource("").getFile()).getParentFile().getParentFile().getAbsolutePath()+"\\"+path.replace("/", "\\");
	}
	
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
