package Engines.SimpleObjects;

import java.util.LinkedList;

import edu.stanford.nlp.simple.Sentence;

/**
 * This simple class store a sentence, its count of containing annotations 
 * and the annotations as list if desired.
 * @author TTurke
 *
 */
public class SentenceObject 
{
	private int annot_count = 0;
	private Sentence sentence;
	private LinkedList<String> raw_annotations = new LinkedList<String>();
	
	public SentenceObject(Sentence sentence, int annot_count)
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

	public Sentence getSentence() {
		return sentence;
	}

	public void setSentence(Sentence sentence) {
		this.sentence = sentence;
	}

	public LinkedList<String> getRaw_annotations() {
		return raw_annotations;
	}

	public void setRaw_annotations(LinkedList<String> raw_annotations) {
		this.raw_annotations = raw_annotations;
	}
}
