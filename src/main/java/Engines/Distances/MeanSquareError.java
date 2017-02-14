package Engines.Distances;

import java.util.Arrays;
import java.util.List;

import Engines.SimpleObjects.SimpleRounding;

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
		if(a.size() > 0 && a.size() == b.size())
		{
			return Math.sqrt(meanSquareError(a,b));
		}else{
			System.out.println("List done haven same size or some are empty for RMSE calculation!");
			return Double.NaN;
		}
		
	}
	
	/**
	 * The calculation of the mean square error.
	 * @param a
	 * @param b
	 * @return error value as decimal
	 */
	public static double meanSquareError(List<Double> a, List<Double> b)
	{
		if(a.size() > 0 && a.size() == b.size())
		{
			double sum = 0.00000000;
			
			for(int k = 0; k < a.size(); k++) sum += Math.pow((a.get(k) - b.get(k)), 2);
			return sum/a.size();
		}else{
			System.out.println("List done haven same size or some are empty for MSE calculation!");
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
		if(a.size() > 0 && a.size() == b.size())
		{
			double sum = 0.00000000;
			
			for(int k = 0; k < a.size(); k++) sum += Math.abs((a.get(k) - b.get(k)));
			
			return sum/a.size();
		}else{
			System.out.println("List done haven same size or some are empty for MAE calculation!");
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
		List<Double> a = Arrays.asList(7.0, 10.0, 12.0, 10.0, 10.0, 8.0, 7.0, 8.0, 11.0, 13.0, 10.0, 8.0); 
		List<Double> b = Arrays.asList(6.0, 10.0, 14.0, 16.0, 7.0, 5.0, 5.0, 13.0, 12.0, 13.0, 8.0, 5.0);
		
		List<Double> i = Arrays.asList(12.0); 
		List<Double> j = Arrays.asList(14.0);
		
		System.out.println("MSE: "+SimpleRounding.round(MeanSquareError.meanSquareError(a,b)));
		System.out.println("RMSE: "+SimpleRounding.round(MeanSquareError.rootedMeanSquareError(a,b)));
		System.out.println("MAE: "+SimpleRounding.round(MeanSquareError.meanAbsoluteError(a,b)));
		
		System.out.println("MSE: "+SimpleRounding.round(MeanSquareError.meanSquareError(i,j)));
		System.out.println("RMSE: "+SimpleRounding.round(MeanSquareError.rootedMeanSquareError(i,j)));
		System.out.println("MAE: "+SimpleRounding.round(MeanSquareError.meanAbsoluteError(i,j)));
		
	}

}
