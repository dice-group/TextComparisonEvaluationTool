package Engines.IO;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
		HashMap<String, Double> metrics_GERBIL = new HashMap<String, Double>();
		HashMap<Integer, Double> words_occurr_distr = new HashMap<Integer, Double>();
		HashMap<Integer, Double> symbol_sent_dist = new HashMap<Integer, Double>();
		HashMap<Integer, Double> annotation_dist = new HashMap<Integer, Double>();
		HashMap<String, Double> syn_error_dist = new HashMap<String, Double>();
		HashMap<String, Double> pos_tags_dist = new HashMap<String, Double>();
		HashMap<Character, Double> symbol_error_dist = new HashMap<Character, Double>();
		
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
		

		try 
		{
//			System.out.println("Called: "+path);
			//Reader parts
			String sCurrentLine;
			br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF8"));
			
			//General variables
			String optimalRexSQBR = Pattern.quote("[") +"(.*?)"+ Pattern.quote("]");
			pattern = Pattern.compile(optimalRexSQBR);
			ti = new TextInformations(path);
			
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
//						System.out.println("\nM_1");
						continue;
					}else{
						
						matcher.find();	//1st
				        char c = matcher.group().charAt(1);
				        
				        matcher.find();	//2nd
				        double d = Double.parseDouble(replaceSQBR(matcher.group()));
				        
//				        System.out.println("["+c+"]["+d+"]");
						symbol_error_dist.put(c, d);
					}
					
					
				}else if(sCurrentLine.equals("M_2") || m2)
				{
					if(sCurrentLine.equals("M_2"))
					{
						m2 = true;
//						System.out.println("\nM_2 Done");
						continue;
					}else{

						matcher.find();	//1st
						int i =  Integer.parseInt(replaceSQBR(matcher.group()));
								
						matcher.find();	//2st
						double d  =  Double.parseDouble(replaceSQBR(matcher.group()));
						
//						System.out.println("["+i+"]["+d+"]");
						symbol_sent_dist.put(i, d);
					}
					
					
				}else if(sCurrentLine.equals("M_3") || m3)
				{
					if(sCurrentLine.equals("M_3"))
					{
						m3 = true;
//						System.out.println("\nM_3");
						continue;
					}else{
						
						matcher.find();	//1st
				        String s = replaceSQBR(matcher.group());
				        
				        matcher.find();	//2nd
				        double d = Double.parseDouble(replaceSQBR(matcher.group()));
						
//				        System.out.println("["+s+"]["+d+"]");
						syn_error_dist.put(s, d);
					}
					
					
				}else if(sCurrentLine.equals("M_4") || m4)
				{
					if(sCurrentLine.equals("M_4"))
					{
						m4 = true;
//						System.out.println("\nM_4");
						continue;
					}else{
						
						matcher.find();	//1st
						int i =  Integer.parseInt(replaceSQBR(matcher.group()));
								
						matcher.find();	//2st
						double d =  Double.parseDouble(replaceSQBR(matcher.group()));
						
//						System.out.println("["+i+"]["+d+"]");
						words_occurr_distr.put(i, d);
					}
					
					
				}else if(sCurrentLine.equals("M_5") || m5)
				{
					if(sCurrentLine.equals("M_5"))
					{
						m5 = true;
//						System.out.println("\nM_5");
						continue;
					}else{
						
						matcher.find();	//1st
				        String s = replaceSQBR(matcher.group());
				        
				        matcher.find();	//2nd
				        double d = Double.parseDouble(replaceSQBR(matcher.group()));
						
//				        System.out.println("["+s+"]["+d+"]");
						pos_tags_dist.put(s, d);
					}
					
					
				}else if(sCurrentLine.equals("M_6") || m6)
				{
					if(sCurrentLine.equals("M_6"))
					{
						m6 = true;
//						System.out.println("\nM_6");
						continue;
					}else{
						
						matcher.find();	//1st
						int i =  Integer.parseInt(replaceSQBR(matcher.group()));
								
						matcher.find();	//2st
						double d =  Double.parseDouble(replaceSQBR(matcher.group()));
						
//						System.out.println("["+i+"]["+d+"]");
						annotation_dist.put(i, d);
					}
					
					
				}else if(sCurrentLine.equals("M_GERBIL") || mgerbil)
				{
					if(sCurrentLine.equals("M_GERBIL"))
					{
						mgerbil = true;
//						System.out.println("\nM_GERBIL");
						continue;
					}else{

						matcher.find();	//1st
						String s =  replaceSQBR(matcher.group());
								
						matcher.find();	//2st
						double d =  Double.parseDouble(replaceSQBR(matcher.group()));
						
//						System.out.println("["+s+"]["+d+"]");
						metrics_GERBIL.put(s, d);
					}
				}
			}

		} catch (UnsupportedEncodingException e)
	    {
			System.out.println(e.getMessage());
	    }
	    catch (IOException e)
	    {
			System.out.println(e.getMessage());
	    }
	    catch (Exception e)
	    {
			System.out.println(e.getMessage());
	    } finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		ti.setAnnotation_dist(annotation_dist);
		ti.setMetrics_GERBIL(metrics_GERBIL);
		ti.setWords_occurr_distr(words_occurr_distr);
		ti.setSymbol_sent_dist(symbol_sent_dist);
		ti.setSyn_error_dist(syn_error_dist);
		ti.setPos_tags_dist(pos_tags_dist);
		ti.setSymbol_error_dist(symbol_error_dist);
		
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
		PropReader.fileReader(pr.getResourceFileAbsolutePath("GoldTextWikipedia.txt.content.prop"),6);

	}
}
