package IOContent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class handle all file creation and filling for the program
 * @author TTurke
 *
 */
public class TextWriter 
{
	/**
	 * Simple file writer
	 * @param writeable
	 * @param path
	 */
	public static void fileWriter(String writeable, String path) 
	{
		try {
			
			File file = new File(path);

			if (!file.exists()) 
			{
				file.createNewFile();
			}
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
			bw.write(writeable);
			bw.close();
			
			System.out.println(path);
			System.out.println("FILE CREATED AND FILLED!");
			
		} catch (IOException ioe) { ioe.printStackTrace(); }
	}

	/**
	 * This method return the source folder path
	 * @return path String
	 */
	public static String programFolderPath()
	{
		return TextWriter.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	}
	
	/**
	 * Simple file deleter
	 * @param path
	 */
	public static void deleteUniqueFile(String path)
	{
		try{

    		File file = new File(path);

    		if(file.delete()){
    			System.out.println("File deleted!");
    		}else{
    			System.out.println("Delete failed!");
    		}

    	}catch(Exception e){

    		e.printStackTrace();

    	}
	}
	
	/**
	 * This method create a xml-file path by a given name pointing to the directory of the program
	 * @param filename
	 * @throws IOException 
	 */
	public static String createFilePathByName(String filename) throws IOException
	{
		return  new File(".").getCanonicalPath()+"\\"+filename+".xml";
		
	}
	
	/**
	 * This method write the given text to a xml-file in the parent folder with a given name
	 * @param name
	 * @param text
	 * @throws IOException
	 */
	public static void writeToProgramFolder(String name, String text) throws IOException
	{
		fileWriter(text,createFilePathByName(name));
	}
}
