package IOContent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
	
/**
 * Simple file Reader for *.txt files.
 * @author TTurke
 *
 */
public class TextReader 
{
	
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
