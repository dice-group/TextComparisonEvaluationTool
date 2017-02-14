package Engines.Distances;

/**
 * This class calculate the euclidean distance of 2 decimal values. 
 * @author TTurke
 *
 */
public class EuclideanDistance 
{
	//##################################################################################
		//################################# USAGE METHODS ##################################
		//##################################################################################
		
		/**
		 * The calculation of the eclidean distance.
		 * @param a
		 * @param b
		 * @return error value as decimal
		 */
		public static double euclideanDistance(double a, double b)
		{
			return Math.sqrt(Math.abs(Math.pow(a, 2) - Math.pow(b, 2)));
		}
		
		//##################################################################################
		//#################################### EXAMPLE #####################################
		//##################################################################################
		
		/*
		 * EXAMPLE
		 */
		public static void main(String[] args) 
		{
			System.out.println(EuclideanDistance.euclideanDistance(2.68888, 2.7));
		}
}
