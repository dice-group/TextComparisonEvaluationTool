package AnnotedText2NIF.IOContent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
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
	 * @throws IOException 
	 */
	public static String fileWriter(String writeable, String path) throws IOException
	{
		File file = new File(path);

		if (!file.exists()) 
		{
			file.createNewFile();
		}
		
		Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.getAbsoluteFile()), "UTF-8"));
		out.write(writeable);
		out.close();
		return path;
	}
	
	/**
	 * This method stores the raw content of the Gold Standard Text
	 * @param mvp
	 * @param path
	 * @return content file path
	 */
	public static String writeMVP(MetricVectorProcessing mvp, String path)
	{
		try {
			
			File file = new File(path);

			if (!file.exists()) 
			{
				file.createNewFile();
			}
			
			Writer bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.getAbsoluteFile()), "UTF-8"));

			bw.write("M_1");
			((BufferedWriter) bw).newLine();
			for(Character key: mvp.symbol_error_dist.keySet())
			{
				bw.write("["+key+"]["+mvp.symbol_error_dist.get(key)+"]");
				((BufferedWriter) bw).newLine();
			}
			((BufferedWriter) bw).newLine();
			((BufferedWriter) bw).newLine();
			
			//####################################################################
			
			bw.write("M_2");
			((BufferedWriter) bw).newLine();
			for(Integer key: mvp.symbol_sent_dist.keySet())
			{
				bw.write("["+key+"]["+mvp.symbol_sent_dist.get(key)+"]");
				((BufferedWriter) bw).newLine();
			}
			
			((BufferedWriter) bw).newLine();
			((BufferedWriter) bw).newLine();
			
			//####################################################################
			
			
			bw.write("M_3");
			((BufferedWriter) bw).newLine();
			for(String key: mvp.syntactic_error_dist.keySet())
			{
				bw.write("["+key+"]["+mvp.syntactic_error_dist.get(key)+"]");
				((BufferedWriter) bw).newLine();
			}
			((BufferedWriter) bw).newLine();
			((BufferedWriter) bw).newLine();
			
			//####################################################################
			
			bw.write("M_4");
			((BufferedWriter) bw).newLine();
			for(Integer key: mvp.word_occurrence_dist.keySet())
			{
				bw.write("["+key+"]["+mvp.word_occurrence_dist.get(key)+"]");
				((BufferedWriter) bw).newLine();
			}
			((BufferedWriter) bw).newLine();
			((BufferedWriter) bw).newLine();
			
			//####################################################################
			
			bw.write("M_5");
			((BufferedWriter) bw).newLine();
			for(String key: mvp.pos_tags_dist.keySet())
			{
				bw.write("["+key+"]["+mvp.pos_tags_dist.get(key)+"]");
				((BufferedWriter) bw).newLine();
			}
			((BufferedWriter) bw).newLine();
			((BufferedWriter) bw).newLine();
			
			//####################################################################
			
			bw.write("M_6");
			((BufferedWriter) bw).newLine();
			
			for(Integer key: mvp.annotated_entity_dist.keySet())
			{
				bw.write("["+key+"]["+mvp.annotated_entity_dist.get(key)+"]");
				((BufferedWriter) bw).newLine();
			}
			((BufferedWriter) bw).newLine();
			((BufferedWriter) bw).newLine();
			
			//####################################################################
			
			bw.write("M_GERBIL");
			((BufferedWriter) bw).newLine();
			for(String key: mvp.gerbil_metrics.keySet())
			{
				bw.write("["+key+"]["+mvp.gerbil_metrics.get(key)+"]");
				((BufferedWriter) bw).newLine();;
			}
			((BufferedWriter) bw).newLine();

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
			
			Writer bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.getAbsoluteFile()), "UTF-8"));
			
//			BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
			
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
				bw.write("REMIND: Its the distance vector!");
				((BufferedWriter) bw).newLine();
				
				for (int k = 0; k < ro.getDistance_vector().size(); k++) 
				{
					if(k < 6){
						bw.write("M_"+(k+1)+": "+ro.getDistance_vector().get(k));
						((BufferedWriter) bw).newLine();
					}else{
						bw.write("GERBIL_"+m+": "+ro.getDistance_vector().get(k));
						((BufferedWriter) bw).newLine();
						m++;
					}
					
				}
				
				if(ro.getRating() == 0.0){
					((BufferedWriter) bw).newLine();
					bw.write("The final rating: "+ro.getRating());
					((BufferedWriter) bw).newLine();
					bw.write("PERFECT MATCH!");
				}else{
					((BufferedWriter) bw).newLine();
					bw.write("\nDistance: "+(1-ro.getRating()));
					((BufferedWriter) bw).newLine();
					bw.write("\nSimilarity: "+ro.getRating());
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
