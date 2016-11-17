package SimpleObjects;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

/**
 * This class contains a sorting algorithm for the word frequency presentation
 * @author TTurke
 *
 */
public class FrequencySorting 
{
	
	/**
	 * This method sorts 2 HashMaps by PTL (1st order Percentage then 2nd order Lexical)
	 * and return a sorted list of triple objects containing all Map elements in desired order. 
	 * @param percentage
	 * @param count
	 * @return sorted triple list
	 */
	public static LinkedList<Triple> sortByPTL(Map<String, Double> percentage, Map<String, Integer> count)
	{
		//Get key values
		ArrayList<String> keys = new ArrayList<String>(percentage.keySet());
		
		//Init output list
		LinkedList<Triple> triples_sorted = new LinkedList<Triple>();
		
		//Over all keys
		for(int k = 0; k < keys.size(); k++)
		{	
			//initital position for each run
			int position = 0;
			
			//New triple object 
			Triple t = new Triple((String)keys.get(k), percentage.get(keys.get(k)), count.get(keys.get(k)));
			
			//If we have already some elements in the sorted list
			if(triples_sorted.size() > 0)
			{
				
				//Sort the triples
				for(int i = 0; i < triples_sorted.size(); i++)
				{
					
					//find new position of the triple depending on the percentage
					
					// position step forward if reach a higher value 
					if(triples_sorted.get(i).getValue() >  t.getValue())
					{
						position++;
					}else if(triples_sorted.get(i).getValue() ==  t.getValue())	//look closer if we get a equal object
					{
						
						// find new position depending of the triple depending on the lexical order
						
						//if current triple is lexical lower continue
						if(triples_sorted.get(i).getKey().compareTo(t.getKey()) < 0)
						{							
							position++;
							continue;
						}
					}
				}
				
				// finaly add the element at the desired position
				triples_sorted.add(position, t);
				
			}else{	
				// otherwise
				triples_sorted.add(t);	
			}
		}
		
		return triples_sorted;
	}
	
}
