package Engines.Distances;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This class contains the calculation of the cosine distance between 2 vectors
 * Resource: http://reference.wolfram.com/language/ref/CosineDistance.html
 * @author TTurke
 *
 */
public class CosDistance 
{
	static double epsilon = 0.00000001;
	
	//##################################################################################
	//################################# USAGE METHODS ##################################
	//##################################################################################
	
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
			double similarity, divider;
			boolean identicVecs = true;
			
			
			for(int ix = 0; ix < v1.size(); ix++)
			{
				System.out.println("V1: "+v1.get(ix)+" | V2: "+v2.get(ix));
				if(!(v1.get(ix).equals(v2.get(ix)))) identicVecs = false;
			}
			
			
			
			if(identicVecs){
				System.out.println("Identical vectors appeared!");
				return 0;
			}else{
				
				for (int i = 0; i < v1.size(); i++)
				{
					upper_value += (v1.get(i) * v2.get(i));
					lower_left  += Math.pow(Math.abs(v1.get(i)), 2);
					lower_right += Math.pow(Math.abs(v2.get(i)), 2);	
				}
				
				divider = Math.sqrt(lower_left)*Math.sqrt(lower_right);
				
				//evade a dividing 0
				if(divider > 0)
				{
					//calc cosin similarity 
					similarity = upper_value/divider;
				}else{
					//set similarity 0 for 0 dividing 
					similarity = 0;
				}
				
				//return cosin dist
				return 1.0 -similarity;
			}
		}else{
			System.out.println("Vector size equality miss match!");
			return Double.NaN;
		}
	}
	
	//##################################################################################
	//#################################### EXAMPLE #####################################
	//##################################################################################
	
	/*
	 * EXAMPLE of USE
	 */
	public static void main(String[] args) 
	{
		double epsilon = 0.00000001;
		ArrayList<Double> v1 = new ArrayList<Double>(Arrays.asList(1.0,2.0,3.0));
		ArrayList<Double> v2 = new ArrayList<Double>(Arrays.asList(3.0,5.0,7.0));
		ArrayList<Double> zv = new ArrayList<Double>(
				Collections.nCopies(3, epsilon));
		
		System.out.println(CosDistance.cosineDistanceDecimal(v1, v2)+"\n");	//Desired round about 0.002585
		
		v1 = new ArrayList<Double>(Arrays.asList(5.0, 0.0, 3.0, 0.0, 2.0, 0.0, 0.0, 2.0, 0.0, 0.0));
		v2 = new ArrayList<Double>(Arrays.asList(3.0, 0.0, 2.0, 0.0, 1.0, 1.0, 0.0, 1.0, 0.0, 1.0));
		
		System.out.println(CosDistance.cosineDistanceDecimal(v1, v2)+"\n");	//Desired round about 0.064399
		
		v1 = new ArrayList<Double>(Arrays.asList(0.0,0.0,0.0));
		v2 = new ArrayList<Double>(Arrays.asList(3.0,5.0,7.0));
		
		System.out.println(CosDistance.cosineDistanceDecimal(v1, v2)+"\n");	//Should be okay 0.0
		
		v1 = new ArrayList<Double>(Arrays.asList(3.0,5.0,7.0));
		v2 = new ArrayList<Double>(Arrays.asList(0.0,0.0,0.0));
		
		
		System.out.println(CosDistance.cosineDistanceDecimal(v1, v2)+"\n");	//Should be okay 0.0
		
		v1 = new ArrayList<Double>(Arrays.asList(0.0,0.0,0.0));
		v2 = new ArrayList<Double>(Arrays.asList(0.0,0.0,0.0));
		
		
		System.out.println(CosDistance.cosineDistanceDecimal(v1, v2)+"\n");	//Should be okay identical 0.0
		System.out.println(CosDistance.cosineDistanceDecimal(zv, zv)+"\n");	//Should be okay identical 0.0
	}

}
