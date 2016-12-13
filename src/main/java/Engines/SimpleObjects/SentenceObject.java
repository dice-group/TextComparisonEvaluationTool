package Engines.SimpleObjects;

import java.util.LinkedList;

/**
 * This simple class store a sentence, its count of containing annotations 
 * and the annotations as list if desired.
 * @author TTurke
 *
 */
public class SentenceObject 
{
	private int annot_count = 0;
	private String sentence;
	private LinkedList<String> raw_annotations = new LinkedList<String>();
	
	public SentenceObject(String sentence, int annot_count)
	{
		this.sentence = sentence;
		this.annot_count = annot_count;
	}

	public int getAnnot_count() {
		return annot_count;
	}

	public void setAnnot_count(int annot_count) {
		this.annot_count = annot_count;
	}

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public LinkedList<String> getRaw_annotations() {
		return raw_annotations;
	}

	public void setRaw_annotations(LinkedList<String> raw_annotations) {
		this.raw_annotations = raw_annotations;
	}
}
