package Engines.SimpleObjects;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * This class just round a to long decimal number by reducing and rounding her decimal places.
 * Max 4 decimal places will be returned!
 * @author TTurke
 *
 */
public class SimpleRounding 
{
	/**
	 * This method round decimal until 4 decimal place
	 * @param value
	 * @return rounded and trimmed decimal value
	 */
	public static double round(double value)
	{
		DecimalFormat df = new DecimalFormat("#.####");
		df.setRoundingMode(RoundingMode.HALF_EVEN);
		return Double.parseDouble(df.format(value).replace(",", "."));
	}
	
	public static void main(String [] args)
	{
		System.out.println(SimpleRounding.round(332.24566668767005));
		System.out.println(SimpleRounding.round(-43.6542558055136));
		System.out.println(SimpleRounding.round(5.921213802559668));
		System.out.println(SimpleRounding.round(0.0));
	}
}
