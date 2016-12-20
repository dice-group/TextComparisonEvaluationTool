package AnnotedText2NIF.ConverterEngine;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
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
	
	//#############################################################################
	//############################ CONSTRUCTOR'S ##################################
	//#############################################################################
	
	public GatherAnnotationInformations(){}
	
	//#############################################################################
	//############################ USAGE METHODS ##################################
	//#############################################################################
	
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
			
			if(uris.length > 0 && uris.length < 3)	//only 1 or 2 elements allowed inside a annotation
			{
				if(uris[0].substring(0,1).equals(" "))
				{
					if(uris[0].substring(uris[0].length()-1).equals(" "))
					{
						us.add(prefix+uris[0].substring(1,uris[0].length()-1).replace(" ", "_"));
					}else{
						us.add(prefix+uris[0].substring(1).replace(" ", "_"));
					}
				}else{
					
					if(uris[0].substring(uris[0].length()-1).equals(" "))
					{
						us.add(prefix+uris[0].substring(0,uris[0].length()-1).replace(" ", "_"));
					}else{
						us.add(prefix+uris[0].replace(" ", "_"));
					}
				}	
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
	public static LinkedList<int[]> returnAnnotationRanges(String input)
	{
		LinkedList<int[]> output = new LinkedList<int[]>();
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
	public static LinkedList<DefinitionObject> getAnnotationDefs(String input)
	{
		LinkedList<DefinitionObject> output = new LinkedList<DefinitionObject>();
		
		for(int[] coords : returnAnnotationRanges(input)) 
		{
			if(returnEnWikiUrl(input.substring(coords[0], coords[1])).size() != 0)
			{
				output.add(new DefinitionObject(coords[0], coords[1], input.substring(coords[0], coords[1]), returnEnWikiUrl(input.substring(coords[0], coords[1]))));
			}			
		}
		
		return output;
	}
	
	/**
	 * This method return all known informations about the annotations 
	 * inside the text of the given path for text file.
	 * @param path
	 * @return
	 */
	public static LinkedList<DefinitionObject> getAnnotationsOfFile(String path)
	{
		String input = TextReader.fileReader(path);
		return getAnnotationDefs(input);
	}

	//TODO in den NIF generator einbauen!!!!
	/**
	 * This method store annotation style free text 
	 * in the local not_annot_text variable, and return the start position and length of
	 * the annotated entities inside it as list of integers.
	 * @param input
	 * @return list start position and length 
	 */
	public LinkedList<int[]> gatherAnnotationCoords(String input)
	{
		LinkedList<int[]> output = new LinkedList<int[]>();
		
		String regex = Pattern.quote("[[") + "(.*?)" + Pattern.quote("]]");
		String edited = input;
		int begin = -1, end = -1;
		
		Matcher matcher = Pattern.compile(regex).matcher(edited);
//		System.out.println("raw: "+edited);
		
		while (matcher.find())
		{	
			
			if(!matcher.group().contains("|"))
			{
//				//Handling easy
//				System.out.printf("EASY: Position [%d,%d]%n", matcher.start(), matcher.end());
//				System.out.println("des: "+matcher.group());
//				System.out.println("rep: "+matcher.group().replace("[[", "").replace("]]", ""));
//				System.out.println("del: "+matcher.group().substring(0, 2)+"..."+matcher.group().substring(matcher.group().indexOf("]]"))); 
//				System.out.println("len: "+(matcher.group().indexOf("]]") - 2));
				
				begin = matcher.start();
				end = matcher.end()-4;
				
				output.add(new int[]{begin, end-begin});
				edited = edited.replace(matcher.group(), matcher.group().replace("[[", "").replace("]]", ""));
				
			}else{
				//Handling complex
//				System.out.printf("COMPLEX: Position [%d,%d]%n", matcher.start(), matcher.end());
//				System.out.println("des: "+matcher.group());
//				System.out.println("rep: "+matcher.group().substring(matcher.group().indexOf("|")+1, matcher.group().indexOf("]]")));
//				System.out.println("del: "+matcher.group().substring(0, matcher.group().indexOf("|")+1)+" ..."+matcher.group().substring(matcher.group().indexOf("]]")));
//				System.out.println("len: "+(matcher.group().indexOf("]]") - matcher.group().indexOf("|")));
				
				begin = matcher.start();
				end = matcher.end()-matcher.group().substring(0, matcher.group().indexOf("|")+1).length()-2;
				
				output.add(new int[]{begin, end-begin});
				
				edited = edited.replace(matcher.group(), matcher.group().substring(matcher.group().indexOf("|")+1, matcher.group().indexOf("]]")));
			}
			
			matcher = Pattern.compile(regex).matcher(edited);
		}
		
//		System.out.println("edited: "+edited);
		
//		for (int i = 0; i < output.size(); i++) 
//		{
//			System.out.println("Start: "+output.get(i)[0]);
//			System.out.println("Ende: "+(output.get(i)[0]+output.get(i)[1]));
//			System.out.println("Länge: "+output.get(i)[1]);
//			System.out.println("Content: "+edited.substring(output.get(i)[0], output.get(i)[0]+ output.get(i)[1])+"\n");
//		}
		setNot_annot_text(edited);
		
		return output;
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
	public static void main(String[] args)
	{
		String test = "the team [[lions]] improved on their [[1963 dallas cowboys season|previous output]] of 4a10, winning five [[low]] games [[well|done]]. ";
		GatherAnnotationInformations gai = new GatherAnnotationInformations();
		LinkedList<int []> entities = gai.gatherAnnotationCoords(test);
		
		System.out.println(test);
		System.out.println(gai.getNot_annot_text());
		
		for (int i = 0; i < entities.size(); i++) 
		{
			System.out.print("S: "+entities.get(i)[0]+" | ");
			System.out.print("E: "+(entities.get(i)[0]+entities.get(i)[1])+" | ");
			System.out.print("L: "+entities.get(i)[1]+" | ");
			System.out.println("W: "+gai.getNot_annot_text().substring(entities.get(i)[0], (entities.get(i)[1]+ entities.get(i)[0])));
		}
		
	}
}
