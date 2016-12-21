package AnnotedText2NIF.ConverterEngine;
import java.io.IOException;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import AnnotedText2NIF.IOContent.TextReader;

/**
 * Diese Klasse sammelt alle Informationen bzgl. jeder Annotation aus einem Text, 
 * und Speichert diese in einer Liste von DefinitionObject(s).
 * @author TTurke
 *
 */
public class GatherAnnotationInformations 
{
	private String not_annot_text;
	public static final String prefix = "https://en.wikipedia.org/wiki/";
	
	//#############################################################################
	//############################ USAGE METHODS ##################################
	//#############################################################################
	
	/**
	 * This method return all known informations about the annotations 
	 * inside the text of the given path for text file.
	 * @param path
	 * @return
	 */
	public LinkedList<DefinitionObject> getAnnotationsOfFile(String path, GatherAnnotationInformations gai)
	{
		String input = TextReader.fileReader(path);
		return gai.gatherDefinitions(input);
	}

	/**
	 * This method return all known informations about the annotations inside the text.
	 * The url constuction is pre-defined!
	 * @param input
	 * @return list of definition objects
	 */
	public LinkedList<DefinitionObject> gatherDefinitions(String input)
	{
		LinkedList<DefinitionObject> dobjs =  new LinkedList<DefinitionObject>();
		String regex = Pattern.quote("[[") + "(.*?)" + Pattern.quote("]]");
		String url = "",content = "";
		int begin = -1, end = -1;
		
		Matcher matcher = Pattern.compile(regex).matcher(input);
		
		while (matcher.find())
		{	
			if(!matcher.group().contains("|"))
			{
				//Handling easy
				begin = matcher.start();
				end = matcher.end()-4;
				content = matcher.group().replace("[[", "").replace("]]", "");
				url = prefix+content;
				dobjs.add(new DefinitionObject(begin, end, content, url.replace(" ", "_")));
				
				//Replace text
				input = input.replace(matcher.group(), matcher.group().replace("[[", "").replace("]]", ""));
				
			}else{
				//Handling complex
				begin = matcher.start();
				end = matcher.end()-matcher.group().substring(0, matcher.group().indexOf("|")+1).length()-2;
				url = prefix+matcher.group().substring(2, matcher.group().indexOf("|"));
				content = matcher.group().substring(matcher.group().indexOf("|")+1, matcher.group().indexOf("]]"));
				dobjs.add(new DefinitionObject(begin, end, content, url.replace(" ", "_")));
				
				//Replace text
				input = input.replace(matcher.group(), matcher.group().substring(matcher.group().indexOf("|")+1, matcher.group().indexOf("]]")));
			}
			
			matcher = Pattern.compile(regex).matcher(input);
		}
		setNot_annot_text(input);
		return dobjs;
	}
	
	//#############################################################################
	//########################## GETTERS & SETTERS ################################
	//#############################################################################
	
	public String getNot_annot_text() {
		return not_annot_text;
	}

	public void setNot_annot_text(String not_annot_text) {
		this.not_annot_text = not_annot_text;
	}
	
	/*
	 * EXAMPLE of USE
	 */
	public static void main(String[] args) throws IOException
	{
		TextReader tr = new TextReader();
		String infile_name = "epoch15.txt";
//		String infile_name = "Bsp1.txt";
		String path = tr.getResourceFileAbsolutePath(infile_name);
		String input = TextReader.fileReader(path);
		GatherAnnotationInformations gai = new GatherAnnotationInformations();
		LinkedList<DefinitionObject> dobjs = gai.gatherDefinitions(input);
		
		System.out.println(input);
		System.out.println(gai.getNot_annot_text());
		
		for (int i = 0; i < dobjs.size(); i++) 
		{
			System.out.println("T: "+gai.getNot_annot_text().substring(dobjs.get(i).getStartPos(), dobjs.get(i).getEndPos()));
			System.out.print("S: "+dobjs.get(i).getStartPos()+" | ");
			System.out.print("E: "+dobjs.get(i).getEndPos()+" | ");
			System.out.print("L: "+(dobjs.get(i).getEndPos()-dobjs.get(i).getStartPos())+" | ");
			System.out.print("W: "+dobjs.get(i).getContent()+" | ");
			System.out.println("U: "+dobjs.get(i).getEngWikiUrl());
		}
		
	}
}
