package Engines.SimpleObjects;

import java.time.LocalDateTime;

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
}
