package AnnotedText2NIF.IOContent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import Engines.SimpleObjects.ResultObject;

/**
 * This class handle all file creation and filling for the program
 * @author TTurke
 *
 */
public class TextWriter 
{
	/**
	 * This method write the given text to a file in the parent folder with a given name
	 * @param writeable
	 * @param path
	 */
	public static String fileWriter(String writeable, String path) 
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
			
		} catch (IOException ioe) { ioe.printStackTrace(); }
		
		return path;
	}
	
	/**
	 * This method create and return a file by given content, path, name and type.
	 * @param writeable
	 * @param location
	 * @param name
	 * @param type
	 * @return content file
	 */
	public static File createContentFile(String writeable, String location, String name, String type) 
	{
		String path = location+"/"+name+"."+type;
		File file = new File(path);
		
		try {

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
		
		return file;
	}
	
	/**
	 * This method write all ratings to separate files!
	 * @param ros
	 */
	public static void writeRating(LinkedList<ResultObject> ros)
	{
		int m = 0;
		
		for (ResultObject ro : ros) 
		{
			m = 1;
			try {
				
				File file = new File(ro.getRatingPath());

				if (!file.exists()) 
				{
					file.createNewFile();
				}
				
				BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
				
				bw.write(ro.getResource());
				bw.newLine();
				
				for (int k = 0; k < ro.getDistance_vector().size(); k++) 
				{
					if(k < 6){
						bw.write("M_"+(k+1)+": "+ro.getDistance_vector().get(k));
					}else{
						bw.write("GERBIL_"+m+": "+ro.getDistance_vector().get(k));
						m++;
					}
					bw.newLine();
					
				}
				
				if(ro.getRating() == 0.0){
					bw.write("Rating: "+ro.getRating());
					bw.newLine();
					bw.write("THIS IS A PERFECT MATCH!");
				}else{
					bw.write("\nRating: "+ro.getRating());
				}
				
				bw.close();
				
				System.out.println(file.getAbsolutePath());
				
			} catch (IOException ioe) { ioe.printStackTrace(); }
		}
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
}
