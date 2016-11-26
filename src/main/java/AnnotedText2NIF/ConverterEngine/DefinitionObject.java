package AnnotedText2NIF.ConverterEngine;
import java.util.HashSet;
import java.util.Set;

/**
 * Klasse definiert eine Annotation in einem Text. Die Url ist ausschließlich auf en.wiki basierend. 
 * Type: Wiki_Mardown_Url
 * Form: [[Steve Jobs]]
 * Url: https://en.wikipedia.org/wiki/Steve_Jobs
 * @author TTurke
 *
 */
public class DefinitionObject 
{
	private int s = -1;
	private int e = -1;
	private int usz = 0;
	private String c = "";
	private Set<String> u = new HashSet<String>();
	
	
	public DefinitionObject(int start, int end, String content, Set<String> in_uris)
	{
		this.s = start;
		this.e = end;
		this.c = content;
		this.u = in_uris;
		this.usz = this.u.size();
			
				
	}

	public int getStartPos() {
		return s;
	}

	public int getEndPos() {
		return e;
	}
	
	public int getUrlAmount(){
		return usz;
	}

	public String getContent() {
		return c;
	}
	
	public Set<String> getEngWikiUrls(){
		return u;
	}
	
	//TODO evtl. erweiertung auf liste von urls sofern gewünscht
	
	public String showAllContent()
	{
		return "[ "+getStartPos()+" ][ "+getEndPos()+" ][ "+getContent()+" ][ "+getEngWikiUrls()+" ]";
	}
}
