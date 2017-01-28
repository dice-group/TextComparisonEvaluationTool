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

public class PropReader 
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
		boolean m1 = false; 
		boolean m2 = false; 
		boolean m3 = false; 
		boolean m4 = false; 
		boolean m5 = false; 
		boolean m6 = false; 
		boolean mgerbil = false;
		

		try {
			
			//Reader parts
			String sCurrentLine;
			br = new BufferedReader(new FileReader(path));
			
			//General variables
			
			String optimalRexSQBR = Pattern.quote("[") +"(.*?)"+ Pattern.quote("]");
			pattern = Pattern.compile(optimalRexSQBR);
			ti = new TextInformations(path);
			HashMap<String, Double> metrics_GERBIL = new HashMap<String, Double>();
			HashMap<Integer, Double> words_occurr_distr = new HashMap<Integer, Double>();
			HashMap<Integer, Double> symbol_sent_dist = new HashMap<Integer, Double>();
			HashMap<Integer, Double> annotation_dist = new HashMap<Integer, Double>();
			HashMap<String, Double> syn_error_dist = new HashMap<String, Double>();
			HashMap<String, Double> pos_tags_dist = new HashMap<String, Double>();
			HashMap<Character, Double> symbol_error_dist = new HashMap<Character, Double>();
			
			
			while ((sCurrentLine = br.readLine()) != null)
			{ 
				matcher = pattern.matcher(sCurrentLine);
				
				if(!(sCurrentLine.length()>0))
				{
					m1 = false;
					m2 = false; 
					m3 = false; 
					m4 = false; 
					m5 = false; 
					m6 = false; 
					mgerbil = false;
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
				        char c = matcher.group().charAt(1);
				        
				        matcher.find();	//2nd
				        double d = Double.parseDouble(replaceSQBR(matcher.group()));
				        
				        System.out.println("["+c+"]["+d+"]");
						symbol_error_dist.put(c, d);
					}
					
					
				}else if(sCurrentLine.equals("M_2") || m2)
				{
					if(sCurrentLine.equals("M_2"))
					{
						m2 = true;
						System.out.println("\nM_2 Done");
						continue;
					}else{

						matcher.find();	//1st
						int i =  Integer.parseInt(replaceSQBR(matcher.group()));
								
						matcher.find();	//2st
						double d  =  Double.parseDouble(replaceSQBR(matcher.group()));
						
						System.out.println("["+i+"]["+d+"]");
						symbol_sent_dist.put(i, d);
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
				        double d = Double.parseDouble(replaceSQBR(matcher.group()));
						
				        System.out.println("["+s+"]["+d+"]");
						syn_error_dist.put(s, d);
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
						double d =  Double.parseDouble(replaceSQBR(matcher.group()));
						
						System.out.println("["+i+"]["+d+"]");
						words_occurr_distr.put(i, d);
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
				        double d = Double.parseDouble(replaceSQBR(matcher.group()));
						
				        System.out.println("["+s+"]["+d+"]");
						pos_tags_dist.put(s, d);
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
						double d =  Double.parseDouble(replaceSQBR(matcher.group()));
						
						System.out.println("["+i+"]["+d+"]");
						annotation_dist.put(i, d);
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
		PropReader pr = new PropReader();
		String path = pr.getResourceFileAbsolutePath("27.01.2017_mvp_epoch70Final.content.prop");
		PropReader.fileReader(path,6);

	}
}
