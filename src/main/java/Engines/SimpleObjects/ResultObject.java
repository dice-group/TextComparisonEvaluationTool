package Engines.SimpleObjects;

import java.util.ArrayList;

/**
 * This class store all relevant results for 1 text
 * @author TTurke
 *
 */
public class ResultObject {

	private String resource = null;
	private double rating = Double.NaN;
	private ArrayList<Double> distance_vector = new ArrayList<Double>();
	private String ratingPath = null;
	
	public ResultObject(double rating, ArrayList<Double> distance_vector, String ratingPath, String resource)
	{
		this.rating = rating;
		this.distance_vector = distance_vector;
		this.ratingPath = ratingPath;
		this.resource = resource;
	}

	public double getRating() {
		return rating;
	}

	public ArrayList<Double> getDistance_vector() {
		return distance_vector;
	}

	public String getRatingPath() {
		return ratingPath;
	}

	public String getResource() {
		return resource;
	}
}
