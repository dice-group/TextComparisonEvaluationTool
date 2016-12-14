package Engines.Distances;

/**
 * This class calculate the quadratic error of 2 decimal values. 
 * @author TTurke
 *
 */
public class QuadError 
{
	/**
	 * The calculation of the quadratic error.
	 * @param a
	 * @param b
	 * @return error value as decimal
	 */
	public static double calcQE(double a, double b)
	{
		return Math.sqrt(Math.abs(Math.pow(a, 2) - Math.pow(b, 2)));
	}
	
	
	
	/*
	 * EXAMPLE
	 */
	public static void main(String[] args) 
	{
		
		System.out.println(QuadError.calcQE(2.68888, 2.7));
	}

}
