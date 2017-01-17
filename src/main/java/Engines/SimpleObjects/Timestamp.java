package Engines.SimpleObjects;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class create a time stamp
 * @author TTurke
 *
 */
public class Timestamp 
{
	/**
	 * Simply return generation date of the class object
	 * @return LoclaDateTime
	 */
	public static LocalDateTime getCurrentTime()
	{
		LocalDateTime localDate = LocalDateTime.now();
		return localDate;
	}
	
	/**
	 * Simply return a String of a given local date object
	 * @param ld
	 * @return Date as String
	 */
	public static String getLocalDATAsString(LocalDateTime ld)
	{
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
		return dtf.format(ld);
	}
	
	/**
	 * Simply return a String of a given local date object
	 * @param ld
	 * @return Date as String
	 */
	public static String getLocalDateAsString(LocalDateTime ld)
	{
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		return dtf.format(ld);
	}
	
	public static void main(String[] args){
		System.out.println(Timestamp.getLocalDateAsString(Timestamp.getCurrentTime()));
	}
}
