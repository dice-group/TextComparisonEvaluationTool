package Engines.SimpleObjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import Engines.Distances.CosDistance;
import Engines.Distances.QuadError;
import Engines.KLDiv.KullbackLeiblerDivergenz;

/**
 * This class gather all informations for a the necessary vector type by a given TextInformations object.
 * It also provides functions to calculate distances between 2 vectors and allow further to rate there distance.
 * @author TTurke
 *
 */
public class MetricVectorProcessing 
{
	//epsilon
	static double epsilon = 0.00000001;
	
	//Non_Gerbil metrics
	int symbol_average;
	HashMap<Character, Integer> symbol_error_dist;
	HashMap<String, Integer> syntactic_error_dist;
	HashMap<Integer, Integer> word_occurrence_dist;
	HashMap<String, Integer> pos_tags_dist;
	HashMap<Integer, Integer> annotated_entity_dist;
	
	//Gerbil metrics
	HashMap<String, Double> gerbil_metrics;
	
	ArrayList<Double> distanceVector;
	ArrayList<Double> zero_vector;
	
	//##################################################################################
	//################################## CONSTRUCTOR ###################################
	//##################################################################################
	
	/**
	 * Constructor
	 * Need the count of all non GERBIL metrics
	 * @param insertion
	 * @param ngm_count
	 * @param gm_count
	 */
	public MetricVectorProcessing(TextInformations insertion, int ngm_count)
	{
		System.out.println();
		
		
		
		this.symbol_average = insertion.getSymbol_count();				/*M_1*/
		this.symbol_error_dist = insertion.getSymbol_error_dist();		/*M_2*/
		this.syntactic_error_dist = insertion.getSyn_error_dist();		/*M_3*/
		this.word_occurrence_dist = insertion.getWords_occurr_distr();	/*M_4*/
		this.pos_tags_dist = insertion.getPos_tags_dist();				/*M_5*/
		this.annotated_entity_dist = insertion.getAnnotation_dist();	/*M_6*/
		this.gerbil_metrics = insertion.getMetrics_GERBIL();			/*M_GERBIL*/
		
		//at the beginning empty
		distanceVector = new ArrayList<Double>();	
		
		// 10 value = 6 non-gerbil metrics + 4 gerbil metrics 
		zero_vector = new ArrayList<Double>(
				Collections.nCopies(ngm_count+insertion.getMetrics_GERBIL().keySet().size(), epsilon));
	}
	
	//##################################################################################
	//################################# USAGE METHODS ##################################
	//##################################################################################
	
	/*
	 * TODO FURTHER PROGRAMMING:
	 * implement separated list for non GERBIL metrics and the opposite, 
	 * because you can add easier add more metrics for both list 
	 * and calculate the distance vector later.
	 * => list1.add(list2); 
	 */
	/**
	 * This method allow tho generate the distance vector by 
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static ArrayList<Double> calcDistanceVector(MetricVectorProcessing v1, MetricVectorProcessing v2)
	{
		ArrayList<Double> distance_vector = new ArrayList<Double>();
		double[] gm;
		
		// Gerbil metrics (6 metrics currently) ATTENTION add more right here if you need them
		distance_vector.add(QuadError.calcQE(v1.symbol_average, v2.symbol_average));
		distance_vector.add(KullbackLeiblerDivergenz.EasyKLDivergenceTI(v1.symbol_error_dist, v2.symbol_error_dist));
		distance_vector.add(KullbackLeiblerDivergenz.EasyKLDivergenceTI(v1.syntactic_error_dist, v2.syntactic_error_dist));
		distance_vector.add(KullbackLeiblerDivergenz.EasyKLDivergenceTI(v1.word_occurrence_dist, v2.word_occurrence_dist));
		distance_vector.add(KullbackLeiblerDivergenz.EasyKLDivergenceTI(v1.pos_tags_dist, v2.pos_tags_dist));
		distance_vector.add(KullbackLeiblerDivergenz.EasyKLDivergenceTI(v1.annotated_entity_dist, v2.annotated_entity_dist));
		
		// Gerbil metrics (4 metrics currently)
		ArrayList<String> keys = new ArrayList<String>(v1.gerbil_metrics.keySet());
		
		for (String key :  keys) 
		{
			System.out.println("V1: "+v1.gerbil_metrics+" | V2: "+v2.gerbil_metrics+" | Key: "+key);
			gm = useOrRepalceDouble(v1.gerbil_metrics, v2.gerbil_metrics, key);
			
			System.out.println("V1: "+gm[0]+" | V2: "+gm[1]+" | QE: "+QuadError.calcQE(gm[0], gm[1]));
			distance_vector.add(QuadError.calcQE(gm[0], gm[1]));
		}
		
		return distance_vector;
	}
	
	/*
	 * TODO FURTHER PROGRAMMING:
	 * Find a way to merge useOrRepalceDouble and useOrRepalceInteger class,
	 * because they are similar in general.
	 */
	/**
	 * This method check 2 maps about a key is existing and return there values, 
	 * and if some map hasn't the key it set a zero for it.
	 * @param m1
	 * @param m2
	 * @param key
	 * @return 2 decimal
	 */
	public static double[] useOrRepalceDouble(Map<String, Double> m1, Map<String, Double> m2, String key)
	{
		if(key != null)
		{
			double[] output = new double[2];
			
			
			//Check key existence and set value for m1
			if(m1.containsKey(key))
			{
				output[0] = m1.get(key);
			}else{
				output[0] = epsilon;
			}
			
			//Check key existence and set value for m2
			if(m2.containsKey(key))
			{
				output[1] = m2.get(key);
			}else{
				output[1] = epsilon;
			}
			
			return output;
		}else{
			return null;
		}
	}
	
	/**
	 * This method check 2 maps about a key is existing and return there values, 
	 * and if some map hasn't the key it set a zero for it.
	 * @param m1
	 * @param m2
	 * @param key
	 * @return 2 integers
	 */
	public static int[] useOrRepalceInteger(Map<String, Integer> m1, Map<String, Integer> m2, String key)
	{
		if(key != null)
		{
			int[] output = new int[2];
			
			
			//Check key existence and set value for m1
			if(m1.containsKey(key))
			{
				output[0] = m1.get(key);
			}else{
				output[0] = 0;
			}
			
			//Check key existence and set value for m2
			if(m2.containsKey(key))
			{
				output[1] = m2.get(key);
			}else{
				output[1] = 0;
			}
			
			return output;
		}else{
			return null;
		}
	}
	
	/**
	 * This method rate a distance vector towards the zero vector by calculation the cosine distance between them.
	 * @param dist_vec
	 * @param zero_vec
	 * @return rating decimal value
	 */
	public static double rate(ArrayList<Double> dist_vec, ArrayList<Double> zero_vec)
	{
		return CosDistance.cosineDistanceDecimal(dist_vec, zero_vec);
	}
	
	//##################################################################################
	//############################## GETTERS AND SETTERS ###############################
	//##################################################################################
	
	public ArrayList<Double> getZero_vector() {
		return zero_vector;
	}
	
	//##################################################################################
	//#################################### EXAMPLE #####################################
	//##################################################################################
	
	public static void main(String[] args) throws NoSuchFieldException, SecurityException, ClassNotFoundException 
	{
		
		
	}
}
