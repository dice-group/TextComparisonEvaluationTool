package Engines.IO;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Engines.SimpleObjects.MetricVectorProcessing;
import Engines.SimpleObjects.TextInformations;

/**
 * This class handle the loading of property files
 * @author TTurke
 *
 */
public class PropertyReader 
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
	
	//TODO its working
	/**
	 * Simple mvp property file reader
	 * @param path
	 * @return Content String
	 */
	public static MetricVectorProcessing fileReader(String path, int nmg_count)
	{
		TextInformations ti = new TextInformations(path);
		Pattern pattern = null;
        Matcher matcher = null;
		BufferedReader br = null;
		boolean m1 = false, 
				m2 = false, 
				m3 = false, 
				m4 = false, 
				m5 = false, 
				m6 = false, 
				mgerbil = false;
		

		try {
			
			//Reader parts
			String sCurrentLine;
			br = new BufferedReader(new FileReader(path));
			
			//General variables
			pattern = Pattern.compile("\\[(.*?)\\]");
			ti = new TextInformations(path);
			HashMap<String, Double> metrics_GERBIL = new HashMap<String, Double>();
			HashMap<Integer, Integer> words_occurr_distr = new HashMap<Integer, Integer>();
			HashMap<Integer, Integer> symbol_sent_dist = new HashMap<Integer, Integer>();
			HashMap<Integer, Integer> annotation_dist = new HashMap<Integer, Integer>();
			HashMap<String, Integer> syn_error_dist = new HashMap<String, Integer>();
			HashMap<String, Integer> pos_tags_dist = new HashMap<String, Integer>();
			HashMap<Character, Integer> symbol_error_dist = new HashMap<Character, Integer>();
			
			
			while ((sCurrentLine = br.readLine()) != null)
			{ 
				matcher = pattern.matcher(sCurrentLine);
				
				if(!(sCurrentLine.length()>0))
				{
					continue;
				}else  if(sCurrentLine.equals("M_1") || m1)
				{
					if(sCurrentLine.equals("M_1"))
					{
						m1 = true;
						System.out.println("\nM_1");
						continue;
					}else{
						
						matcher.find();	//1st
						System.out.println(matcher.group());
				        char c = replaceSQBR(matcher.group()).charAt(0);
				        
				        matcher.find();	//2nd
				        int i = Integer.parseInt(replaceSQBR(matcher.group()));
				        
				        System.out.println("["+c+"]["+i+"]");
						symbol_error_dist.put(c, i);
					}
					
					
				}else if(sCurrentLine.equals("M_2") || m2)
				{
					if(sCurrentLine.equals("M_2"))
					{
						m2 = true;
						System.out.println("\nM_2");
						continue;
					}else{

						matcher.find();	//1st
						int i =  Integer.parseInt(replaceSQBR(matcher.group()));
								
						matcher.find();	//2st
						int j =  Integer.parseInt(replaceSQBR(matcher.group()));
						
						System.out.println("["+i+"]["+j+"]");
						symbol_sent_dist.put(i, j);
					}
					
					
				}else if(sCurrentLine.equals("M_3") || m3)
				{
					if(sCurrentLine.equals("M_3"))
					{
						m3 = true;
						System.out.println("\nM_3");
						continue;
					}else{
						
						matcher.find();	//1st
				        String s = replaceSQBR(matcher.group());
				        
				        matcher.find();	//2nd
				        int i = Integer.parseInt(replaceSQBR(matcher.group()));
						
				        System.out.println("["+s+"]["+i+"]");
						syn_error_dist.put(s, i);
					}
					
					
				}else if(sCurrentLine.equals("M_4") || m4)
				{
					if(sCurrentLine.equals("M_4"))
					{
						m4 = true;
						System.out.println("\nM_4");
						continue;
					}else{
						
						matcher.find();	//1st
						int i =  Integer.parseInt(replaceSQBR(matcher.group()));
								
						matcher.find();	//2st
						int j =  Integer.parseInt(replaceSQBR(matcher.group()));
						
						System.out.println("["+i+"]["+j+"]");
						words_occurr_distr.put(i, j);
					}
					
					
				}else if(sCurrentLine.equals("M_5") || m5)
				{
					if(sCurrentLine.equals("M_5"))
					{
						m5 = true;
						System.out.println("\nM_5");
						continue;
					}else{
						
						matcher.find();	//1st
				        String s = replaceSQBR(matcher.group());
				        
				        matcher.find();	//2nd
				        int i = Integer.parseInt(replaceSQBR(matcher.group()));
						
				        System.out.println("["+s+"]["+i+"]");
						pos_tags_dist.put(s, i);
					}
					
					
				}else if(sCurrentLine.equals("M_6") || m6)
				{
					if(sCurrentLine.equals("M_6"))
					{
						m6 = true;
						System.out.println("\nM_6");
						continue;
					}else{
						
						matcher.find();	//1st
						int i =  Integer.parseInt(replaceSQBR(matcher.group()));
								
						matcher.find();	//2st
						int j =  Integer.parseInt(replaceSQBR(matcher.group()));
						
						System.out.println("["+i+"]["+j+"]");
						annotation_dist.put(i, j);
					}
					
					
				}else if(sCurrentLine.equals("M_GERBIL") || mgerbil)
				{
					if(sCurrentLine.equals("M_GERBIL"))
					{
						mgerbil = true;
						System.out.println();
						continue;
					}else{

						matcher.find();	//1st
						String s =  replaceSQBR(matcher.group());
								
						matcher.find();	//2st
						double d =  Double.parseDouble(replaceSQBR(matcher.group()));
						
						System.out.println("["+s+"]["+d+"]");
						metrics_GERBIL.put(s, d);
					}
					
					
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
		return new MetricVectorProcessing(ti, nmg_count);
	}
	
	/**
	 * Just deleting square brackets
	 * @param s
	 * @return
	 */
	public static String replaceSQBR(String s)
	{
		return s.replace("[", "").replace("]", "");
	}
	
	public static void main(String[] args) throws IOException 
	{
		PropertieReader pr = new PropertieReader();
		String path = pr.getResourceFileAbsolutePath("24.01.2017_gold_nvp.content.prop");
		pr.fileReader(path,6);

	}
}
