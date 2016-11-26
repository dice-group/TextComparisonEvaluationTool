package AnnotedText2NIF.ConverterEngine;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import AnnotedText2NIF.IOContent.TextReader;

/**
 * Diese Klasse sammelt alle Informationen bzgl. jeder Annotation aus einem Text, 
 * und Speichert diese in einer Liste von DefinitionObject(s).
 * @author TTurke
 *
 */
public class GatherAnnotationInformations 
{
	/**
	 * This method just return a en.wiki link for a given annotation
	 * @param Annotation
	 * @return url
	 */
	public static Set<String> returnEnWikiUrl(String Annotation)
	{
		Set<String> us = new HashSet<String>();
		String prefix = "https://en.wikipedia.org/wiki/";
		
		if(Annotation.contains("|"))
		{
			String [] uris = Annotation.split("\\|");
			
			for(String cur : uris)
			{
				us.add(prefix+cur.replace(" ", "_"));
			}
		
			
		}else{
			
			us.add(prefix+Annotation.replace(" ", "_"));
			
		}	
		
		return us;
	}
	
	/**
	 * This method gather all interval informations of all annotations inside the given text.
	 * @param input
	 * @return list of intervalls
	 */
	public static ArrayList<int[]> returnAnnotationRanges(String input)
	{
		ArrayList<int[]> output = new ArrayList<int[]>();
		String start = "[[";
		String end = "]]";
		int endIndex = input.indexOf(end);
		int nextStartItem = -1;
		
		for (int beginIndex = input.indexOf(start); beginIndex >= 0; beginIndex = input.indexOf(start, beginIndex + 1))
		{
			//Neues Array
			int[] coords =  new int[]{-1,-1};
			
			//Startindex
			coords[0] = beginIndex+2;
			
			//Next Startindex
			nextStartItem = input.indexOf(start, beginIndex + 1);
			
			//Endindex
			if(endIndex >= 0 && endIndex > beginIndex)
			{
				coords[1] = endIndex;
			}
			
			
			//Prüfe auf Doppelstart
			if(nextStartItem > coords[0] && nextStartItem < coords[1])
			{
				//Neue Runde
				continue;
			}else{
				
				//Next Endindex
				endIndex = input.indexOf(end, endIndex + 1);
				
				//Alles Einfügen
				if(coords[0] > 0 && coords[1] > 0 && coords[1] > coords[0])
				{
					output.add(coords);
				}
			}
		}
		return output;
	}
	
	/**
	 * This method return all known informations about the annotations inside the text.
	 * The url constuction is pre-defined!
	 * @param input
	 * @return list of DefinitionObject(s)
	 */
	public static ArrayList<DefinitionObject> getAnnotationDefs(String input)
	{
		ArrayList<DefinitionObject> output = new ArrayList<DefinitionObject>();
		
		for(int[] coords : returnAnnotationRanges(input)) 
		{
			output.add(new DefinitionObject(coords[0], coords[1], input.substring(coords[0], coords[1]), returnEnWikiUrl(input.substring(coords[0], coords[1]))));			
		}
		
		return output;
	}
	
	/*
	 * EXAMPLE of USE
	 */
	public static void main(String[] args) 
	{
		String input = TextReader.fileReader("C:/Users/Subadmin/Desktop/Test1.txt");
		
		for(DefinitionObject defObj : getAnnotationDefs(input))
		{
			System.out.println(defObj.showAllContent());
		}
	}

}
