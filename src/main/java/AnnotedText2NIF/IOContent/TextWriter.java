package AnnotedText2NIF.IOContent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.LinkedList;

import Engines.SimpleObjects.MetricVectorProcessing;
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
	 * This method stores the raw content of the Gold Standard Text
	 * @param mvp
	 * @param path
	 * @return content file path
	 */
	public static String writeGoldMVP(MetricVectorProcessing mvp, String path)
	{
		try {
			
			File file = new File(path);

			if (!file.exists()) 
			{
				file.createNewFile();
			}
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
			
			bw.write("M_1");
			bw.newLine();
			bw.write(mvp.symbol_average);
			bw.newLine();
			bw.newLine();
			
			//####################################################################
			
			bw.write("M_2");
			bw.newLine();
			for(Character key: mvp.symbol_error_dist.keySet())
			{
				bw.write("["+key+"]["+mvp.symbol_error_dist.get(key)+"]");
				bw.newLine();
			}
			bw.newLine();
			bw.newLine();
			
			//####################################################################
			
			bw.write("M_3");
			bw.newLine();
			for(String key: mvp.syntactic_error_dist.keySet())
			{
				bw.write("["+key+"]["+mvp.syntactic_error_dist.get(key)+"]");
				bw.newLine();
			}
			bw.newLine();
			bw.newLine();
			
			//####################################################################
			
			bw.write("M_4");
			bw.newLine();
			for(Integer key: mvp.word_occurrence_dist.keySet())
			{
				bw.write("["+key+"]["+mvp.word_occurrence_dist.get(key)+"]");
				bw.newLine();
			}
			bw.newLine();
			bw.newLine();
			
			//####################################################################
			
			bw.write("M_5");
			bw.newLine();
			for(String key: mvp.pos_tags_dist.keySet())
			{
				bw.write("["+key+"]["+mvp.pos_tags_dist.get(key)+"]");
				bw.newLine();
			}
			bw.newLine();
			bw.newLine();
			
			//####################################################################
			
			bw.write("M_6");
			bw.newLine();
			
			for(Integer key: mvp.annotated_entity_dist.keySet())
			{
				bw.write("["+key+"]["+mvp.annotated_entity_dist.get(key)+"]");
				bw.newLine();
			}
			bw.newLine();
			bw.newLine();
			
			//####################################################################
			
			bw.write("M_GERBIL");
			bw.newLine();
			for(String key: mvp.gerbil_metrics.keySet())
			{
				bw.write("["+key+"]["+mvp.gerbil_metrics.get(key)+"]");
				bw.newLine();
			}
			bw.newLine();

			//close the buffered writer
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
				


				Writer bw = new BufferedWriter(new OutputStreamWriter( new FileOutputStream(file.getAbsoluteFile()), "UTF-8"));
				
				bw.write(ro.getResource());
				((BufferedWriter) bw).newLine();
				
				for (int k = 0; k < ro.getDistance_vector().size(); k++) 
				{
					if(k < 6){
						if(k == 0) System.out.println("Average: "+ro.getDistance_vector().get(k));
						bw.write("M_"+(k+1)+": "+ro.getDistance_vector().get(k));
					}else{
						bw.write("GERBIL_"+m+": "+ro.getDistance_vector().get(k));
						m++;
					}
					((BufferedWriter) bw).newLine();
					
				}
				
				if(ro.getRating() == 0.0){
					bw.write("Rating: "+ro.getRating());
					((BufferedWriter) bw).newLine();
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
