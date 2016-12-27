package AnnotedText2NIF.ConverterEngine;
import java.io.IOException;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import AnnotedText2NIF.IOContent.TextReader;
import Engines.simpleTextProcessing.TextConversion;

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
	
	
	
	/**
	 * This method return all known informations about the annotations inside the text.
	 * The url constuction is pre-defined!
	 * @param input
	 * @return list of definition objects
	 */
	public LinkedList<DefinitionObject> gatherDefs(LinkedList<String> input)
	{
		LinkedList<DefinitionObject> dobjs =  new LinkedList<DefinitionObject>();
		String cleaned_text = "";
		char[] clean_sentence;
		char[] entity;
		int new_cur_index;
		int new_cur_start_entity;
		int new_cur_end_entity;
		
		
		for (int k = 0; k < input.size(); k++) //für jeden satz
		{
			
			//text as char array
			char[] current_sentence = input.get(k).toCharArray();
			
			//text verwaltungs objekt
			Spezification spez = new Spezification();
			
			//Zähle neuen index für start und ende einer entity im gesäuberten text
			new_cur_index = 0;
			new_cur_start_entity = -1;
			new_cur_end_entity = -1;
			
			//speicher objekt für den gesäuberten text
			clean_sentence = new char[current_sentence.length];
			
			//false eine enity nie geschlossen wurde oder ein separator eingebaut wurde
			entity = new char[current_sentence.length];			
			
			//check für entity processing active
			boolean isActive = false;
			
			
			
			for (int i = 0; i < current_sentence.length; i++) 
			{
				//actual character
				char c =  current_sentence[i];
				
				if(i > 0 && c == '[' && current_sentence[i-1] == '[')	//report entity reached
				{
					if(isActive == true)	//if false entity
					{
						//speicher den inhalt der entity als normalen text in den cleaned ein da es keine entity ist
						//setze entity auf leer  und addiere die länge des hinzugefügten textes aus entity auf den index
						
					}else{
						isActive = true;
						new_cur_start_entity = new_cur_index;
					}
					
				}
				
				
				
				if(i > 0 && c == '[' && current_sentence[i-1] == '[' && isActive)	//report entity left
				{
					isActive = false;
					cleaned_text += clean_sentence + "\n";
				}
				
				//Es wird gerade eine entity bearbeitet
				if(isActive)
				{
					if(c == '|')	
					{				//check for separator
						
					}else if(Character.isAlphabetic(c) || 
							 Character.isDigit(c) || 
							 Character.isSpaceChar(c))		
					{				//check for alnum and whitespaces => check is not opposite
						
					}else{			//alles andere ignorieren
						
					}
				}
				
				
				
				
				
			}
			
			//annotation add
			
			//string combine for cleaned annotated text
		}
		
		//add annotated text
		setNot_annot_text(cleaned_text);
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
