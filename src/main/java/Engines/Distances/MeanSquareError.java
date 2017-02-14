package Engines.Distances;

import java.util.Arrays;
import java.util.List;

/**
 * This class calculate the (rooted) mean square error of 2 decimal values. 
 * @author TTurke
 *
 */
public class MeanSquareError 
{
	//##################################################################################
	//################################# USAGE METHODS ##################################
	//##################################################################################
	
	/**
	 * The calculation of the rooted mean square error.
	 * @param a
	 * @param b
	 * @return error value as decimal
	 */
	public static double rootedMeanSquareError(List<Double> a, List<Double> b)
	{
		return Math.sqrt(meanSquareError(a,b));
	}
	
	/**
	 * The calculation of the mean square error.
	 * @param a
	 * @param b
	 * @return error value as decimal
	 */
	public static double meanSquareError(List<Double> a, List<Double> b)
	{
		if(a.size() == b.size())
		{
			double sum = 0.00000000;
			
			for(int k = 0; k < a.size(); k++) sum += Math.pow((a.get(k) - b.get(k)), 2);
			
			return sum/a.size();
		}else{
			System.out.println("List done haven same size for MSE calculation!");
			return Double.NaN;
		}
	}
	
	/**
	 * The calculation of the mean absolute error.
	 * @param a
	 * @param b
	 * @return error value as decimal
	 */
	public static double meanAbsoluteError(List<Double> a, List<Double> b)
	{
		if(a.size() == b.size())
		{
			double sum = 0.00000000;
			
			for(int k = 0; k < a.size(); k++) sum += Math.abs((a.get(k) - b.get(k)));
			
			return sum/a.size();
		}else{
			System.out.println("List done haven same size for MSE calculation!");
			return Double.NaN;
		}
	}
	
	//##################################################################################
	//#################################### EXAMPLE #####################################
	//##################################################################################
	
	/*
	 * EXAMPLE
	 */
	public static void main(String[] args) 
	{
		List<Double> a = Arrays.asList(2.68888); 
		List<Double> b = Arrays.asList(2.7);
		System.out.println("MSE: "+MeanSquareError.meanSquareError(a,b));
		System.out.println("RMSE: "+MeanSquareError.meanSquareError(a,b));
		
	}

}
