package AnnotedText2NIF.ConverterEngine;

/**
 * Klasse definiert eine Annotation in einem Text. Die Url ist ausschlieﬂlich auf en.wiki basierend. 
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
	private String c = "";
	private String u = "";
	
	
	public DefinitionObject(int start, int end, String content, String in_uri)
	{
		this.s = start;
		this.e = end;
		this.c = content;
		this.u = in_uri;
	}

	public int getStartPos() {
		return s;
	}

	public int getEndPos() {
		return e;
	}

	public String getContent() {
		return c;
	}
	
	public String getEngWikiUrl(){
		return u;
	}
	
	public String showAllContent()
	{
		return "[ "+getStartPos()+" ][ "+getEndPos()+" ][ "+getContent()+" ][ "+getEngWikiUrl()+" ]";
	}
}
