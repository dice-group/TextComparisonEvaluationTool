package IOContent;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import internalEngineParts.WordFrequencyEngine;
import simpleTextProcessing.TextConversion;

public class StringToInputStream 
{
	
	public static void close(BufferedReader br) throws IOException
	{
		br.close();
	}
	
	public static BufferedReader init(String input)
	{
		InputStream is = new ByteArrayInputStream(input.getBytes());
		return new BufferedReader(new InputStreamReader(is));
	}
	
	
	public static void main(String[] args) throws IOException 
	{
		String str1 ="C:/Users/Subadmin/Desktop/BA AKSW/Deep LSTM/epoch- 70 Final.txt";
		String str2 = "C:/Users/Subadmin/Desktop/BA AKSW/Testtexte bad/Bsp1.txt";
		String text = TextConversion.decompose(TextReader.fileReader(str1));
		
		WordFrequencyEngine wfe = new WordFrequencyEngine();	//create set and map
		
		LinkedList<String> sentences = TextConversion.splitIntoSentencesViaSubs(text);
		
		for(String s : sentences)
		{
			wfe.gatherWordFrequency(s);
		}
		
		HashMap outer_map = wfe.wordAppearancePercentage(wfe.getMap());
		
//		System.out.println(wfe.sizeEqualityMaps(outer_map, wfe.getMap()));
//		System.out.println(wfe.sizeEqualitySetMap(wfe.getMap(), wfe.getSet()));
	}
}
