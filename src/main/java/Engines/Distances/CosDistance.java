package Engines.Distances;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class contains the calculation of the cosine distance between 2 vectors
 * Resource: http://reference.wolfram.com/language/ref/CosineDistance.html
 * @author TTurke
 *
 */
public class CosDistance 
{
	/**
	 * Cosine distance between 2 double values containing vectors
	 * @param v1
	 * @param v2
	 * @return cosine distance as double
	 */
	public static double cosineDistanceDecimal(List<Double> v1, List<Double> v2)
	{
		if(v1.size() == v2.size())
		{
			double upper_value = 0.0;
			double lower_left = 0.0, lower_right= 0.0;
			double similarity;
			
			for (int i = 0; i < v1.size(); i++)
			{
				upper_value += (v1.get(i) * v2.get(i));
				lower_left  += Math.pow(Math.abs(v1.get(i)), 2);
				lower_right += Math.pow(Math.abs(v2.get(i)), 2);	
			}
			
			//calc cosin similarity 
			similarity = upper_value/(Math.sqrt(lower_left)*(Math.sqrt(lower_right)));
			
			//return cosin dist
			return 1.0 -similarity;
			
		}else{
			System.err.println("Vector size equality miss match!");
			return Double.NaN;
		}
	}
	
	/*
	 * EXAMPLE of USE
	 */
	public static void main(String[] args) 
	{
		ArrayList<Double> v1 = new ArrayList<Double>(Arrays.asList(1.0,2.0,3.0));
		ArrayList<Double> v2 = new ArrayList<Double>(Arrays.asList(3.0,5.0,7.0));
		
		System.out.println(CosDistance.cosineDistanceDecimal(v1, v2));	//Desired round about 0.002585
		
		v1 = new ArrayList<Double>(Arrays.asList(5.0, 0.0, 3.0, 0.0, 2.0, 0.0, 0.0, 2.0, 0.0, 0.0));
		v2 = new ArrayList<Double>(Arrays.asList(3.0, 0.0, 2.0, 0.0, 1.0, 1.0, 0.0, 1.0, 0.0, 1.0));
		
		System.out.println(CosDistance.cosineDistanceDecimal(v1, v2));	//Desired round about 0.064399
	}

}
