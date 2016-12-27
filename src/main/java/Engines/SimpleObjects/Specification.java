package Engines.SimpleObjects;

class Spezification
{
	private int index = -1;
	private int start_entity = -1;
	private int end_entity = -1;
	private String url;
	private String entity;
	private String cleaned = "";
	
	public Spezification(){};
	
	public Spezification(int i, int s, int e)
	{
		this.index = i;
		this.start_entity = s;
		this.end_entity = e;
	}
	
	public void spledit(String entity)
	{
		if(entity.contains("|"))
		{
			String[] elems = entity.split("|");
			
			//#########################################################
			
			if(elems[0].startsWith(" "))
			{
				elems[0] = elems[0].substring(1, elems[0].length()-1);
			}
			
			if(elems[0].endsWith(" "))
			{
				elems[0] = elems[0].substring(0, elems[0].length()-2);
			}
			
			//#########################################################
			
			if(elems[1].startsWith(" "))
			{
				elems[1] = elems[1].substring(1, elems[1].length()-1);
			}
			
			if(elems[1].endsWith(" "))
			{
				elems[1] = elems[1].substring(0, elems[1].length()-2);
			}
			
			//#########################################################
			
			this.url = elems[0];
			this.entity = elems[1];
			this.index += elems[1].length();
			this.start_entity = this.index;
			this.end_entity = this.start_entity + elems[1].length();
			this.cleaned += this.entity + " ";
		}
	}
	
	public void setIxByAdlText(String in) { this.index += in.length(); }

	public int getIndex() { return index; }

	public void setIndex(int index) { this.index = index; }

	public int getStart_entity() { return start_entity; }

	public void setStart_entity(int start_entity) { this.start_entity = start_entity; }

	public int getEnd_entity() { return end_entity; }

	public void setEnd_entity(int end_entity) { this.end_entity = end_entity; }

	public String getUrl() { return url; }

	public void setUrl(String url) { this.url = url; }

	public String getEntity() { return entity; }

	public void setEntity(String entity) { this.entity = entity; }

	public String getCleaned() { return cleaned; }

	public void setCleaned(String cleaned) { this.cleaned = cleaned; }
	
	/*
	 * EXAMPLE of USE
	 */
	public static void main(String [] args)
	{
		
	}
}
