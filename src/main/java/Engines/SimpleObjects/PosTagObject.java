package Engines.SimpleObjects;

/**
 * This simple object carry a part of speech tag and its occurrence count.
 * @author TTurke
 *
 */
public class PosTagObject 
{
	private String POS_Tag;
	private int tag_ouccurrence = 0;
	
	public PosTagObject(String tag, int occurrence)
	{
		this.POS_Tag = tag;
		this.tag_ouccurrence = occurrence;
	}

	public String getPOS_Tag() {
		return POS_Tag;
	}

	public void setPOS_Tag(String pOS_Tag) {
		POS_Tag = pOS_Tag;
	}

	public int getTag_ouccurrence() {
		return tag_ouccurrence;
	}

	public void setTag_ouccurrence(int tag_ouccurrence) {
		this.tag_ouccurrence = tag_ouccurrence;
	}	
}
