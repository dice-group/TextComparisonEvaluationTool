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
	public static final String real_prefix = "http://en.wikipedia.org/wiki/";
	public static final String dummy_prefix = "http://aksw.org/NOTINWIKI";
	
	public static final String simpleRex = Pattern.quote("[[") + "(.*?)" + Pattern.quote("]]");				//allow inner brackets => [[outer text [[inner text]]
	public static final String optimalRex = Pattern.quote("[[") + "([^\\[\\]]*)" + Pattern.quote("]]");		//denie inner brackets => [[url_entity_text]] or [[url_text|entity_text]]
	
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

	
	//TODO store url error if we need to create a dummy
	/**
	 * This method return all known informations about the annotations inside the text.
	 * The url constuction is pre-defined!
	 * @param input
	 * @return list of definition objects
	 */
	public LinkedList<DefinitionObject> gatherDefinitions(String input)
	{
		LinkedList<DefinitionObject> dobjs =  new LinkedList<DefinitionObject>();
		String url = "",content = "";
		int begin = -1, end = -1;
		
		Matcher matcher = Pattern.compile(optimalRex).matcher(input);
		
		while (matcher.find())
		{	
			if(!matcher.group().contains("|"))
			{
				//Handling easy
				begin = matcher.start();
				end = matcher.end()-4;
				content = matcher.group().replace("[[", "").replace("]]", "");
				url = (real_prefix+content).replace(" ", "_");
				
				//TODO check url exist then create object => exist set url if not set dummy with content
				
				dobjs.add(new DefinitionObject(begin, end, content, url.replace(" ", "_")));
				
				//Replace text
				input = input.replace(matcher.group(), matcher.group().replace("[[", "").replace("]]", ""));
				
			}else{
				//Handling complex
				begin = matcher.start();
				end = matcher.end()-matcher.group().substring(0, matcher.group().indexOf("|")+1).length()-2;
				url = (real_prefix+matcher.group().substring(2, matcher.group().indexOf("|")).replace(" ", "_"));
				
				//TODO check url exist then create object => exist set url if not set dummy with content
				
				content = matcher.group().substring(matcher.group().indexOf("|")+1, matcher.group().indexOf("]]"));
				dobjs.add(new DefinitionObject(begin, end, content, url.replace(" ", "_")));
				
				//Replace text
				input = input.replace(matcher.group(), matcher.group().substring(matcher.group().indexOf("|")+1, matcher.group().indexOf("]]")));
			}
			
			matcher = Pattern.compile(optimalRex).matcher(input);
		}
		setNot_annot_text(input);
		return dobjs;
	}
	
	
	
	//TODO ermögliche das nicht dauernd der text mehrfach durchlaufen werden muss
	public LinkedList<DefinitionObject> gatherDef(String input)
	{
		LinkedList<DefinitionObject> dobjs =  new LinkedList<DefinitionObject>();
		String url = "",content = "", url_part = "";
		int begin = -1, end = -1;
		
		int begin_substract = 0;
		int end_substract = 0;
		
		int new_start = 0;
		
		Matcher matcher = Pattern.compile(optimalRex).matcher(input);
		
		while (matcher.find())
		{	
			System.out.println("BS: "+begin_substract+"| ES: "+end_substract);
			
			if(!matcher.group().contains("|"))
			{
				//Handling easy
				begin = matcher.start()-new_start;
				end = matcher.end()-new_start;
				
				//Entity
				content = matcher.group().replace("[[", "").replace("]]", "");
				
				//Url
				url_part = content.replace(" ", "_");
				url = real_prefix+url_part;
				
				//TODO check url exist then create object => exist set url if not set dummy with content
				
				//Definition object
				dobjs.add(new DefinitionObject(begin, end-4, content, url));
				
				//Replace text
				input = input.replace(matcher.group(), matcher.group().replace("[[", "").replace("]]", ""));
				
				//edit index down grade values
				new_start += 4;
				
			}else{
				//Handling complex
				begin = matcher.start()-new_start;
				end = matcher.end()-matcher.group().substring(0, matcher.group().indexOf("|")+1).length()-2-new_start;
				
				//Url
				url_part = matcher.group().substring(2, matcher.group().indexOf("|")).replace(" ", "_");
				url = real_prefix+url_part;
				
				//Entity
				content = matcher.group().substring(matcher.group().indexOf("|")+1, matcher.group().indexOf("]]"));
				
				//TODO check url exist then create object => exist set url if not set dummy with content
				
				//Definition object
				dobjs.add(new DefinitionObject(begin, end, content, url));
				
				//Replace text
				input = input.replace(matcher.group(), matcher.group().substring(matcher.group().indexOf("|")+1, matcher.group().indexOf("]]")));
				
				//edit index down grade values
				new_start += 5+url_part.length();	
			}
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
				{												//report entity reached
					if(isActive == true)	//if false entity
					{
						spez.addStringToCleaned(spez.getEntity());
						spez.setIndex(spez.getIndex()+spez.getEntity().length());
						spez.setEntity("");
						
						
						//speicher den inhalt der entity in den cleaned ein da es keine entity ist
						//setze entity auf leer  
						//addiere die länge des hinzugefügten textes aus entity auf den index
						
					}else{
						isActive = true;
						new_cur_start_entity = new_cur_index;
					}
					
				}else 
					
					
					
				if(isActive)	
				{												//Entity still processing
					
					if(i == current_sentence.length-1)
					{				//report sentence end
						if(createSFL(clean_sentence).length() > 0) cleaned_text +=  createSFL(clean_sentence);
						isActive = false;
						
						
						
						
						
					}else{
						
					}
					
					
					//INSERT BLOCK für oberes if else
					
					if(i > 0 && c == ']' && current_sentence[i-1] == ']' )
					{				//report entity left
						
						isActive = false;
						//hier das DefinitionObject bauen
						
					}else if(c == '|')	
					{				//check for separator
						
					}else if(Character.isAlphabetic(c) || 
							 Character.isDigit(c) || 
							 Character.isSpaceChar(c))		
					{				//check for alnum and whitespaces => check is not opposite
						
					}else{			//alles andere ignorieren
						
					}
					
					
					
					
					
					
					
					
				}
				
				
				
				
				
				
				else
				{												//Default => no entity reached
					if(c != '[' && c != ']')
					{
						spez.addCharToCleaned(c);
						spez.count();
					}
					
				}	
				
				System.out.println("INDEX: "+spez.getIndex()+" AND Cleaned SIZE: "+spez.getCleaned().length());
				System.out.println("Text: "+spez.getCleaned());
				
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
		System.out.println("SIZE all: "+getNot_annot_text().length());
		
		
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
//		StanfordSegmentatorTokenizer sst = StanfordSegmentatorTokenizer.create();
//		LinkedList<String> sentences;
		
		TextReader tr = new TextReader();
//		String infile_name = "easycheck.txt";
		String infile_name = "epoch15.txt";
		String path = tr.getResourceFileAbsolutePath(infile_name);
		String input = TextReader.fileReader(path);
//		sentences = StanfordSegmentatorTokenizer.gatherSentences(input);
		GatherAnnotationInformations gai = new GatherAnnotationInformations();
		
//		String desired = "the fon the dease socor sevelation of the historication and par parrence "
//				+ "in may sattern seassing concerned sighter in the parture to one of the anceas "
//				+ "maying and party, penar commanian for order of anganian south sumachal in "
//				+ "rabering lear presical decile and conserval she pare mate in the manga "
//				+ "of the and of the are suroate of a sange as the canar can panada o the reagural "
//				+ "of the randhon of sear legrear.";
//		
//		gai.gatherDefs(sentences);
//		System.out.println("DESIRED!");
//		System.out.println("SIZE: "+desired.length());
//		System.out.println("TEXT: \n"+desired);
		
		
//		LinkedList<DefinitionObject> dobjs = gai.gatherDefinitions(input);
		LinkedList<DefinitionObject> dobjs = gai.gatherDef(input);
		
		
		System.out.println(input);
		System.out.println(gai.getNot_annot_text());
		
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
		
/*
ELEMENT: 1
T: dease socor sevelation of the historication
S: 12 | E: 55 | L: 43 | W: dease socor sevelation of the historication | U: http://en.wikipedia.org/wiki/dease_socor_sevelation_of_the_historication

ELEMENT: 2
T: par parrence
S: 60 | E: 72 | L: 12 | W: par parrence | U: http://en.wikipedia.org/wiki/par_parrence

ELEMENT: 3
T: may sattern seassing concerned sighter
S: 76 | E: 114 | L: 38 | W: may sattern seassing concerned sighter | U: http://en.wikipedia.org/wiki/may_sattern_seassing_concerned_sighter

ELEMENT: 4
T: anceas maying and party
S: 144 | E: 167 | L: 23 | W: anceas maying and party | U: http://en.wikipedia.org/wiki/anceas_maying_and_party

ELEMENT: 5
T: anganian south sumachal
S: 200 | E: 223 | L: 23 | W: anganian south sumachal | U: http://en.wikipedia.org/wiki/anganian_south_sumachal

ELEMENT: 6
T: rabering lear presical decile
S: 227 | E: 256 | L: 29 | W: rabering lear presical decile | U: http://en.wikipedia.org/wiki/rabering_lear_presical_decile

ELEMENT: 7
T: conserval she pare mate
S: 261 | E: 284 | L: 23 | W: conserval she pare mate | U: http://en.wikipedia.org/wiki/conserval_she_pare_mate

ELEMENT: 8
T: the manga
S: 288 | E: 297 | L: 9 | W: the manga | U: http://en.wikipedia.org/wiki/the_manga

ELEMENT: 9
T: canar can panada
S: 348 | E: 364 | L: 16 | W: canar can panada | U: http://en.wikipedia.org/wiki/canar_can_panada

ELEMENT: 10
T: randhon of sear legrear
S: 387 | E: 410 | L: 23 | W: randhon of sear legrear | U: http://en.wikipedia.org/wiki/randhon_of_sear_legrear
*/
		
	}
}
