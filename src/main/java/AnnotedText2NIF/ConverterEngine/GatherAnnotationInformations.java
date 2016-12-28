package AnnotedText2NIF.ConverterEngine;
import java.io.IOException;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import AnnotedText2NIF.IOContent.TextReader;
import Engines.SimpleObjects.Specification;
import Engines.simpleTextProcessing.StanfordSegmentatorTokenizer;
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
	
	
	//TODO here we need to focus!!!
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
		LinkedList<Character> clean_sentence;
		LinkedList<Character> entity;
		int new_cur_index = 0;
		int new_cur_start_entity;
		int new_cur_end_entity;
		
		
		for (int k = 0; k < input.size(); k++) //for each sentence
		{
			
			//text as char array
			char[] current_sentence = StanfordSegmentatorTokenizer.formatCleaned(input.get(k)).toCharArray();
			
			//text specification object
			Specification spez = new Specification();
			
			//count index of start and end of an entity in the cleaned text
			new_cur_start_entity = -1;
			new_cur_end_entity = -1;
			
			//stores cleaned sentence
			clean_sentence = new LinkedList<Character>();
			
			//need regular and if an entity wouldn't closed or an separator exist
			entity = new LinkedList<Character>();			
			
			//check value for entity processing active
			boolean isActive = false;
			
			
			
			for (int i = 0; i < current_sentence.length; i++) //process current sentence
			{
				//actual character
				char c =  current_sentence[i];
				
				if(i > 0 && c == '[' && current_sentence[i-1] == '[')	
				{											//report entity reached
					if(isActive == true)	//if false entity
					{
						//speicher den inhalt der entity als normalen text in den cleaned ein da es keine entity ist
						//setze entity auf leer  und addiere die länge des hinzugefügten textes aus entity auf den index
						
					}else{
						isActive = true;
						new_cur_start_entity = new_cur_index;
					}
					
				}else if(isActive)	
				{	//Entity still processing
					
					if(i > 0 && c == ']' && current_sentence[i-1] == ']' )
					{				//report entity left
						
						isActive = false;
					}else if(i == current_sentence.length-1)
					{				//report sentence end
						if(createSFL(clean_sentence).length() > 0) cleaned_text +=  createSFL(clean_sentence);
					}else if(c == '|')	
					{				//check for separator
						
					}else if(Character.isAlphabetic(c) || 
							 Character.isDigit(c) || 
							 Character.isSpaceChar(c))		
					{				//check for alnum and whitespaces => check is not opposite
						
					}else{			//alles andere ignorieren
						
					}
					
				}else
				{											//Default => no entity reached
					if(c != '[' && c != ']')
					{
						spez.addCharToCleaned(c);
					}
					
				}	
			}
			
			//annotation add
			dobjs.add(new DefinitionObject(spez.getStart_entity(), spez.getEnd_entity(), spez.getEntity(), spez.getUrl()));
			
			//delete last whitespace between last word and sentence end dot
			spez.setCleaned(spez.getCleaned().substring(0, spez.getCleaned().lastIndexOf(".")-2)+".");
			
			//string combine for cleaned annotated text
			if(spez.getCleaned() != null)
			{
				if(k < input.size()-1)
				{
					cleaned_text += spez.getCleaned()+"\n";
				}else{
					cleaned_text += spez.getCleaned();
				}
			}	
		}
		
		//add annotated text
		addTo_Not_annot_text(TextConversion.cleanMultiSpaces(cleaned_text));
		System.out.println(getNot_annot_text());
		
		return dobjs;
	}
	
	/**
	 * Simple to String method for char arrays
	 * @param in
	 * @return
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
	//########################## GETTERS & SETTERS ################################
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
	
	/*
	 * EXAMPLE of USE
	 */
	public static void main(String[] args) throws IOException
	{
		StanfordSegmentatorTokenizer sst = StanfordSegmentatorTokenizer.create();
		LinkedList<String> sentences;
		
		TextReader tr = new TextReader();
		String infile_name = "epoch15.txt";
//		String infile_name = "Bsp1.txt";
		String path = tr.getResourceFileAbsolutePath(infile_name);
		String input = TextReader.fileReader(path);
		sentences = StanfordSegmentatorTokenizer.gatherSentences(input);
		GatherAnnotationInformations gai = new GatherAnnotationInformations();
		
		gai.gatherDefs(sentences);
		
//		LinkedList<DefinitionObject> dobjs = gai.gatherDefinitions(input);
//		
//		System.out.println(input);
//		System.out.println(gai.getNot_annot_text());
//		
//		for (int i = 0; i < dobjs.size(); i++) 
//		{
//			System.out.println("T: "+gai.getNot_annot_text().substring(dobjs.get(i).getStartPos(), dobjs.get(i).getEndPos()));
//			System.out.print("S: "+dobjs.get(i).getStartPos()+" | ");
//			System.out.print("E: "+dobjs.get(i).getEndPos()+" | ");
//			System.out.print("L: "+(dobjs.get(i).getEndPos()-dobjs.get(i).getStartPos())+" | ");
//			System.out.print("W: "+dobjs.get(i).getContent()+" | ");
//			System.out.println("U: "+dobjs.get(i).getEngWikiUrl());
//		}
		
	}
}
