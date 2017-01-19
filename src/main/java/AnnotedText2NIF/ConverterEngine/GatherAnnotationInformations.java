package AnnotedText2NIF.ConverterEngine;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import AnnotedText2NIF.IOContent.TextReader;
import Engines.simpleTextProcessing.DistributionProcessing;
import Web.Controller.URLControl;

/**
 * This class gather all informations about every annotations inside a text 
 * and store it in a list of definition objects.
 * @author TTurke
 *
 */
public class GatherAnnotationInformations 
{
	private String not_annot_text;
	public static final String real_prefix = "https://en.wikipedia.org/wiki/";
	public static final String dummy_prefix = "http://aksw.org/NOTINWIKI/";
	
	public static final String simpleRex = Pattern.quote("[[") + "(.*?)" + Pattern.quote("]]");				//allow inner brackets => [[outer text [[inner text]]
	public static final String optimalRex = Pattern.quote("[[") + "([^\\[\\]]*)" + Pattern.quote("]]");		//denie inner brackets => [[url_entity_text]] or [[url_text|entity_text]]
	
	private HashMap<String, String> urls; 
	private HashMap<String, Integer> syntax_error_dist;
	
	/**
	 * Constructor
	 */
	public GatherAnnotationInformations()
	{
		urls = new HashMap<String, String>();
		syntax_error_dist = new HashMap<String, Integer>();
	}
	
	//#############################################################################
	//############################ USAGE METHODS ##################################
	//#############################################################################
	
	/**
	 * This method return all known informations about the annotations 
	 * inside the text of the given path for text file.
	 * @param path
	 * @return List of Definition objects containing entities informations
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public LinkedList<DefinitionObject> getAnnotationsOfFile(String path, GatherAnnotationInformations gai) throws IOException, InterruptedException
	{
		String input = TextReader.fileReader(path);
		return gai.gatherDefsFast(input);
	}
	
	/**
	 * This method return all known informations about the annotations inside the text.
	 * It also report separator error at false entity constructions.
	 * The URL construction is pre-defined!
	 * @param input
	 * @return list of definition objects
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public LinkedList<DefinitionObject> gatherDefsFast(String resource) throws IOException, InterruptedException
	{
		LinkedList<DefinitionObject> dobjs =  new LinkedList<DefinitionObject>();
		String url = "",content = "", url_part = "";
		int begin = -1, end = -1, new_start = 0;
		String[] entity_container = null;
		
		String input;
		
		//delete whitespace at 0
		if(resource.indexOf(" ") == 0)
		{
			input = resource.substring(1);
		}else{
			input = resource;
		}
		
		Matcher matcher = Pattern.compile(optimalRex).matcher(input);
		
		while (matcher.find())
		{	
			
			if(!matcher.group().contains("|"))
			{
				//Handling easy
				begin = matcher.start()-new_start;
				end = matcher.end()-new_start;
				
				//Entity
				content = matcher.group().replace("[[", "").replace("]]", "");
				
				//Url
				url_part = content.replace(" ", "_");
				
				if(hasUrl(url_part))
				{
					url = getUrls().get(url_part);
				}else{
					if(URLControl.existURL(real_prefix+url_part))
					{
						url = real_prefix+url_part;
					}else{
						url = dummy_prefix+url_part;
						DistributionProcessing.calcDistString(syntax_error_dist, "URL_NOT_EX_ERROR");
					}
					addUrl(url_part, url);
				}
				
				//Definition object
				dobjs.add(new DefinitionObject(begin, end-4, content, url));
				
				//Replace text
				input = input.replace(matcher.group(), matcher.group().replace("[[", "").replace("]]", ""));
				
				//edit index down grade values
				new_start += 4;
				
			}else{
				
				entity_container = matcher.group().split("\\|");
				
				if(entity_container.length > 2)
				{
					
					//report error about to much separator entity
					DistributionProcessing.calcDistString(syntax_error_dist, "ENTITY_SEPA_ERROR");

					//Replace text
					input = input.replace(matcher.group(), entity_container[1]);
					
					//edit index down grade values
					new_start += matcher.group().replace(entity_container[1], "").length();
					
				}else{
					
					//Handling complex
					begin = matcher.start()-new_start;
					end = matcher.end()-matcher.group().substring(0, matcher.group().indexOf("|")+1).length()-2-new_start;
					
					//Url
					url_part = matcher.group().substring(2, matcher.group().indexOf("|")).replace(" ", "_");
					
					
					if(hasUrl(url_part))
					{
						url = getUrls().get(url_part);
					}else{
						if(URLControl.existURL(real_prefix+url_part))
						{
							url = real_prefix+url_part;
						}else{
							url = dummy_prefix+url_part;
							DistributionProcessing.calcDistString(syntax_error_dist, "URL_NOT_EX_ERROR");
						}
						addUrl(url_part, url);
					}
					
					//Entity
					content = matcher.group().substring(matcher.group().indexOf("|")+1, matcher.group().indexOf("]]"));
					
					//Definition object
					dobjs.add(new DefinitionObject(begin, end, content, url));
					
					//Replace text
					input = input.replace(matcher.group(), matcher.group().substring(matcher.group().indexOf("|")+1, matcher.group().indexOf("]]")));
					
					//edit index down grade values
					new_start += 5+url_part.length();
					
				}
			}
		}
		
		setNot_annot_text(input);
		return dobjs;
	}
	
	
	/**
	 * Simple to String method for char arrays
	 * @param in
	 * @return a String
	 */
	public static String createSFL(LinkedList<Character> in)
	{
		String out = "";
		
		for (int i = 0; i < in.size(); i++) 
		{
			out += in.get(i);
		}
		
		return out;
	}
	
	//#############################################################################
	//###################### GETTERS, SETTERS & EDITS #############################
	//#############################################################################
	
	public String getNot_annot_text() {
		return not_annot_text;
	}
	
	public void addTo_Not_annot_text(String in){
		if(this.not_annot_text != null){
			this.not_annot_text += "\n"+in;
		}else{
			this.not_annot_text = "";
			this.not_annot_text += in;
		}
		
	}

	public void setNot_annot_text(String not_annot_text) {
		this.not_annot_text = not_annot_text;
	}
	
	public HashMap<String, Integer> getSyntax_error_dist() {
		return syntax_error_dist;
	}

	public void setSyntax_error_dist(HashMap<String, Integer> syntax_error_dist) {
		this.syntax_error_dist = syntax_error_dist;
	}
	
	public void addSEDMap(HashMap<String, Integer> map) {
		this.syntax_error_dist.putAll(map);
	}
	
	public HashMap<String, String> getUrls() {
		return urls;
	}

	public void setUrls(HashMap<String, String> urls) {
		this.urls = urls;
	}
	
	public void addUrl(String key, String url) 
	{
		this.urls.put(key, url);
	}
	
	public boolean hasUrl(String key)
	{
		return urls.containsKey(key);
	}
	
	//#############################################################################
	//############################### EXAMPLE #####################################
	//#############################################################################

	/*
	 * EXAMPLE of USE
	 */
	public static void main(String[] args) throws IOException, InterruptedException
	{
		TextReader tr = new TextReader();
		String infile_name = "epoch15.txt";
		String path = tr.getResourceFileAbsolutePath(infile_name);
		String input = TextReader.fileReader(path);
		GatherAnnotationInformations gai = new GatherAnnotationInformations();
		
		LinkedList<DefinitionObject> dobjs = gai.gatherDefsFast(input);
		
		System.out.println(input);
		System.out.println(gai.getNot_annot_text());
		System.out.println("Separator Error: "+gai.getSyntax_error_dist().keySet()+"\n");
		
		for (int i = 0; i < dobjs.size(); i++) 
		{
			System.out.println("ELEMENT: "+(i+1));
			System.out.println("T: "+gai.getNot_annot_text().substring(dobjs.get(i).getStartPos(), dobjs.get(i).getEndPos()));
			System.out.print("S: "+dobjs.get(i).getStartPos()+" | ");
			System.out.print("E: "+dobjs.get(i).getEndPos()+" | ");
			System.out.print("L: "+(dobjs.get(i).getEndPos()-dobjs.get(i).getStartPos())+" | ");
			System.out.print("W: "+dobjs.get(i).getContent()+" | ");
			System.out.println("U: "+dobjs.get(i).getEngWikiUrl()+"\n");
		}	
	}
}
