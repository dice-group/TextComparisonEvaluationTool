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
	 * This method check 2 vectors a equal
	 * @param v1
	 * @param v2
	 * @return boolean
	 */
	public static boolean equality(List<Double> v1, List<Double> v2)
	{
		for(int ix = 0; ix < v1.size(); ix++)
		{
			if(!(v1.get(ix).equals(v2.get(ix)))) return false;
		}
		return true;
	}
	
	/**
	 * Check v1 with only equal values and v2 with only equal values and v1,v2 are containing values like 0.0 or epsilon only
	 * @param v1
	 * @param v2
	 * @return boolean
	 */
	public static boolean isZeroToZeroVec(List<Double> v1, List<Double> v2)
	{
		double v1_value = Double.NaN, v2_value = Double.NaN;
		
		
		if(containsEqualValues(v1) && containsEqualValues(v2))
		{
			if(v1.get(0).equals(0.0))
			{
				v1_value = 0.0;
				v2_value = epsilon;
			}else if(v1.get(0).equals(epsilon)){
				v1_value = epsilon;
				v2_value = 0.0;
			}else{
				return false;
			}
		}else{
			return false;
		}
		
		
		for(int ix = 0; ix < v1.size(); ix++) if((v1.get(ix).equals(v1_value) && v2.get(ix).equals(v2_value))) return true;
		return false;
	}
	
	/**
	 * This method check a vector has only equal values
	 * @param v1
	 * @return boolean
	 */
	public static boolean containsEqualValues(List<Double> v1)
	{
		double old = v1.get(0);
		
		for(int k = 1; k < v1.size(); k++)
		{
			if(!v1.get(k).equals(old))
			{
				return false;
			}
			
			old = v1.get(k);
		}
		return true;
	}
	
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
			boolean dontCheck = true;
			
			if(equality(v1, v2))
			{
				dontCheck = true;
			}else if(isZeroToZeroVec(v1, v2)){
				dontCheck = true;
			}else{
				dontCheck = false;
			}
			
			
			
			if(dontCheck){
				return 0;
			}else{
				
				//Magnitude can also be described as square root of the dot product of a vector.
				
				for (int i = 0; i < v1.size(); i++)
				{
					upper_value += (v1.get(i) * v2.get(i));					//Dot product
					lower_left  += Math.pow(Math.abs(v1.get(i)), 2);		//Magnitude A
					lower_right += Math.pow(Math.abs(v2.get(i)), 2);		//Magnitude B	
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
		ArrayList<Double> v1 = new ArrayList<Double>(Arrays.asList(1.0,2.0,3.0));
		ArrayList<Double> v2 = new ArrayList<Double>(Arrays.asList(3.0,5.0,7.0));
		ArrayList<Double> zv = new ArrayList<Double>(
				Collections.nCopies(3, epsilon));
		
		System.out.println("Test_1: "+CosDistance.cosineDistanceDecimal(v1, v2));	//Desired round about 0.002585
		
		v1 = new ArrayList<Double>(Arrays.asList(5.0, 0.0, 3.0, 0.0, 2.0, 0.0, 0.0, 2.0, 0.0, 0.0));
		v2 = new ArrayList<Double>(Arrays.asList(3.0, 0.0, 2.0, 0.0, 1.0, 1.0, 0.0, 1.0, 0.0, 1.0));
		
		System.out.println("Test_2: "+CosDistance.cosineDistanceDecimal(v1, v2));	//Desired round about 0.064399
		
		v1 = new ArrayList<Double>(Arrays.asList(0.0,0.0,0.0));
		v2 = new ArrayList<Double>(Arrays.asList(3.0,5.0,7.0));
		
		System.out.println("Test_3: "+CosDistance.cosineDistanceDecimal(v1, v2));	//Should be okay 1.0
		
		v1 = new ArrayList<Double>(Arrays.asList(3.0,5.0,7.0));
		v2 = new ArrayList<Double>(Arrays.asList(0.0,0.0,0.0));
		
		
		System.out.println("Test_4: "+CosDistance.cosineDistanceDecimal(v1, v2));	//Should be okay 1.0
		
		v1 = new ArrayList<Double>(Arrays.asList(0.0,0.0,0.0));
		v2 = new ArrayList<Double>(Arrays.asList(0.0,0.0,0.0));
		
		
		System.out.println("Test_5: "+CosDistance.cosineDistanceDecimal(v1, v2));	//Should be okay identical 0.0
		System.out.println("Test_6: "+CosDistance.cosineDistanceDecimal(zv, zv));	//Should be okay identical 0.0
		System.out.println("Test_7: "+CosDistance.cosineDistanceDecimal(v1, zv));	//Should be okay identical 0.0
		
		v2 = new ArrayList<Double>(Arrays.asList(epsilon,epsilon,epsilon,epsilon,epsilon,epsilon,epsilon,epsilon,epsilon,epsilon));
		v1 = new ArrayList<Double>(Arrays.asList(	0.0,
													332.24566668767005,
													-43.6542558055136,
													-1.2163953243244934,
													-421.7892842641955,
													5.921213802559668,
													0.3171,
													0.1429,
													0.4616,
													0.2642));
		
		System.out.println("Test_8: "+CosDistance.cosineDistanceDecimal(v1, v2));	//Should be okay 1.0 [ERROR]
		
		v2 = new ArrayList<Double>(Arrays.asList(2.0, 1.0, 0.0, 2.0, 0.0, 1.0, 1.0, 1.0));
		v1 = new ArrayList<Double>(Arrays.asList(2.0, 1.0, 1.0, 1.0, 1.0, 0.0, 1.0, 1.0));
		
		System.out.println("Test_9: "+CosDistance.cosineDistanceDecimal(v1, v2));	//Should be round about (distance 0.822) (similarity 0.178)  
	}

}
