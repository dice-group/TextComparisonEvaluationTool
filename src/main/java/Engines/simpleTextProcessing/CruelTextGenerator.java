package Engines.simpleTextProcessing;

import java.util.Random;

/**
 * This class contains a generator for a fragment test to get a bottom value text for the evaluation.
 * @author TTurke
 *
 */
public class CruelTextGenerator 
{
	//#############################################################################
	//############################ USAGE METHODS ##################################
	//#############################################################################
	
	/**
	 * This method generates a simple text fragment by a given gold standard text.
	 * This generator is only use to get an automatic generated bottom value text.
	 * Attention in case of use this is a desired non optimal implementation.
	 * It is a try to mimic the worst text output of a trained text generating neural network.
	 * @param inputGT
	 * @return fragment text 
	 */
	public static String createRandomFragment(String inputGT)
	{
		String[] in_chars_GT = inputGT.split(" ");
		Random rnd = new Random();
		int length = in_chars_GT.length, min = 5, max = 35; 
		int sentence_len, elem_pos;
		String fragment = "";
		
		//more then 10 words elements for acceptable result
		if(length > 10)
		{
			
			for(int k = 0; k < 40; k++)	//Desired sentences count in the text
			{
				sentence_len = rnd.nextInt(max - min) + min;	//min 5, max 35 word elements
				
				for(int i = 0; i < sentence_len; i++)	//Desired sentence length
				{
					elem_pos = rnd.nextInt(length);		
					
					if(i == sentence_len-1)
					{
						if((sentence_len % 2) == 0)
						{
							fragment += in_chars_GT[elem_pos]+" ";
						}else{
							fragment += in_chars_GT[elem_pos];
						}
						
					}else{
						fragment += in_chars_GT[elem_pos]+" ";
					}
				}
				fragment += ". \n";
			}
		}
		return fragment;
	}
	
	//#############################################################################
	//############################### EXAMPLE #####################################
	//#############################################################################
	
	/*
	 * EXAMPLE of USE
	 */
	public static void main(String[] args) 
	{
		String example = "the common relatives in the [[south africa]] and [[the province of corrolitants]] in [[condia]]. "
				+ "roman most like [[performance chart|of school]] on the [[china san sanga]] of the [[grammarda series]] and [[chemical county security department|programming]] and [[national register]] in 1939 and a series of the [[greek geological community]] which was parts of the course of the short peoples and performance of the only similar of the same relatives and a street contribution for the archaeological  "
				+ "the most professional research council, which is a [[preach island]] and [[shed radio government|maintained british empire|bill baseball similar breat]]. "
				+ "in 1990, when he was not up to the country and former [[the county provincial county, community]] and [[auchrada comics]] and on the training contains the season. "
				+ "in 1993 the former song was appointed the 1960s and the college works from [[south africa]].";

		System.out.println(CruelTextGenerator.createRandomFragment(example));
		
	}

}
