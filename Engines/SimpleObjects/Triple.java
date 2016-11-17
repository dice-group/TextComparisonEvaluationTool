package SimpleObjects;

/**
 * This class contain key, value (%) and count informations of a word as a triple.
 * @author TTurke
 *
 */
public class Triple 
{
	private String key;
	private double value;
	private int count;
	
	/**
	 * Constructor
	 * @param k
	 * @param v
	 * @param c
	 */
	public Triple(String k, double v, int c)
	{
		this.key = k;
		this.value = v;
		this.count = c;
	}

	/**
	 * This method return a string depending on key length
	 * @return content string
	 */
	public String retString()
	{
		String out = "";
		
		if(key.length() < 8)
		{
			return key+"\t\t\t"+count+"\t\t"+value;
		}
		
		if(key.length() < 16)
		{
			return key+"\t\t"+count+"\t\t"+value;
		}
		
		if(key.length() > 15)
		{
			return key+"\t"+count+"\t\t"+value;
		}
		
		return out;
	}

	public String getKey() {
		return key;
	}

	public double getValue() {
		return value;
	}

	public int getCount() {
		return count;
	}
	
	
}
