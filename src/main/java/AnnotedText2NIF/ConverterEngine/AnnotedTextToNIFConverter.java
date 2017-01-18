package AnnotedText2NIF.ConverterEngine;
/**
 * This file is part of NIF transfer library for the General Entity Annotator Benchmark.
 *
 * NIF transfer library for the General Entity Annotator Benchmark is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NIF transfer library for the General Entity Annotator Benchmark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with NIF transfer library for the General Entity Annotator Benchmark.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.aksw.gerbil.io.nif.NIFWriter;
import org.aksw.gerbil.io.nif.impl.TurtleNIFWriter;
import org.aksw.gerbil.transfer.nif.Document;
import org.aksw.gerbil.transfer.nif.data.DocumentImpl;
import org.aksw.gerbil.transfer.nif.data.NamedEntity;
import org.aksw.gerbil.transfer.nif.data.SpanImpl;
import org.junit.Ignore;

import AnnotedText2NIF.IOContent.TextReader;
import AnnotedText2NIF.IOContent.TextWriter;

/**
 * This class generates NIF files from a given text containing wiki markdown.
 * The construction is base on the example from Michael Roeder (roeder@informatik.uni-leipzig.de)
 * @author TTurke
 *
 */
@Ignore
public class AnnotedTextToNIFConverter 
{
	static LinkedList<DefinitionObject> defObjList = new LinkedList<DefinitionObject>();
	
	//#############################################################################
	//############################# CONSTRUCTORS ##################################
	//#############################################################################
	
	public AnnotedTextToNIFConverter()
	{
		defObjList = new LinkedList<DefinitionObject>();
	}
	
	//#############################################################################
	//############################ USAGE METHODS ##################################
	//#############################################################################
	
	/**
	 * This method create a text marking as type SpanImpl.
	 * That means its just the marking and no additional information about appending uris.
	 * @param defObj
	 * @return SpanImpl marking
	 */
	public static SpanImpl createMarkingSpanImpl(DefinitionObject defObj)
	{
		if(defObj.getStartPos() > -1 && defObj.getContent().length() > 0)
		{
			return new SpanImpl(defObj.getStartPos(), defObj.getContent().length());
		}
		
		return null;
		
	}
	
	/**
	 * This method create a text marking as type NamedEntity.
	 * That means there are additional information about appending uris.
	 * @param defObj
	 * @return NamedEntity marking
	 */
	public static NamedEntity createMarkingNamedEntity (DefinitionObject defObj)
	{
		return new NamedEntity(defObj.getStartPos(), defObj.getContent().length(), defObj.getEngWikiUrl());
	}
	
	/**
	 * This method calculate and create a NIF file for a given direct text or text from file with Wikipedia Markup annotations.
	 * Attention GERBIL annotators need little documents to work optimal.
	 * @param input
	 * @param isText
	 * @return NIF structure String
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static String createNIFString(String input, boolean isText) throws IOException, InterruptedException
	{
		resetDefObjList();
		GatherAnnotationInformations gai = new GatherAnnotationInformations();
		LinkedList<DefinitionObject> DOList = new LinkedList<DefinitionObject>();
		
		if(isText){
			DOList = gai.gatherDefsFast(input);
		}else{
			DOList = gai.getAnnotationsOfFile(input, gai);
		}
		
		setDefObjList(DOList);
		
		Document document = new DocumentImpl(gai.getNot_annot_text(), "http://example.org/test_document");
		
		for(DefinitionObject dobj : DOList) document.addMarking(createMarkingNamedEntity(dobj));

		List<Document> documents = new ArrayList<Document>();
		documents.add(document);

		// Writing our new list of documents to a String
		NIFWriter writer = new TurtleNIFWriter();
		System.out.println(documents);
		String nifString = writer.writeNIF(documents);
		
		return nifString;
	}
	
	/**
	 * This method calculate and create a NIF file for a given direct text as sentence list with Wikipedia Markup annotations.
	 * @param input
	 * @param isText
	 * @return NIF structure String
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static String createNIFStringByList(LinkedList<String> input, GatherAnnotationInformations gai) throws IOException, InterruptedException
	{
		resetDefObjList();
		LinkedList<DefinitionObject> DOList = new LinkedList<DefinitionObject>();
		List<Document> documents = new ArrayList<Document>();
		Document document;
		
		for(int k = 0; k < input.size(); k++)
		{
			DOList = gai.gatherDefsFast(input.get(k));
			addToDefObjList(DOList);
			document = new DocumentImpl(gai.getNot_annot_text(), "http://example.org/document_"+(k+1));
			
			for(DefinitionObject dobj : DOList) document.addMarking(createMarkingNamedEntity(dobj));
			
			documents.add(document);
		}
		
		// Writing our new list of documents to a String
		NIFWriter writer = new TurtleNIFWriter();
		System.out.println(documents);
		String nifString = writer.writeNIF(documents);
		
		return nifString;
	}
	
	/**
	 *  This method generate a NIF file (type = TURTLE [*.ttl]) by given text and return its file path.
	 * @param input
	 * @param out_file_path
	 * @param isText
	 * @return path of the nif file
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public static String getNIFFileByText(String input, String out_file_path, boolean isText) throws IOException, InterruptedException
	{
		return TextWriter.fileWriter(createNIFString(input, isText), out_file_path);
	}

	/**
	 *  This method generate a NIF file (type = TURTLE [*.ttl]) by given sentence list and return its file path.
	 * @param input
	 * @param out_file_path
	 * @return path of the nif file
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	public String getNIFFileBySentences(LinkedList<String> input, String out_file_path, GatherAnnotationInformations gai) throws IOException, InterruptedException
	{
		return TextWriter.fileWriter(createNIFStringByList(input, gai), out_file_path);
	}
	
	//#############################################################################
	//###################### GETTERS, SETTERS & EDITS #############################
	//#############################################################################

	public static void setDefObjList(LinkedList<DefinitionObject> defObjList) {
		AnnotedTextToNIFConverter.defObjList = defObjList;
	}
	
	public static void addToDefObjList(LinkedList<DefinitionObject> defObjList) {
		defObjList.addAll(defObjList);
	}
	
	public static void resetDefObjList() {
		AnnotedTextToNIFConverter.defObjList = new LinkedList<DefinitionObject>();
	}
	
	//#############################################################################
	//############################### EXAMPLE #####################################
	//#############################################################################
	
	/*
	 * EXAMPLE of USE
	 */
	public static void main(String[] args) throws IOException, InterruptedException 
	{
		TextReader tr = new TextReader();
		
		
//		String infile_name = "epoch70Final.txt";
//		String outfile_name = "epoch70Final.ttl";
		
		String infile_name = "Bsp1.txt";
		String outfile_name = "bsp.ttl";
		
		String file_location = tr.getResourceFileAbsolutePath(infile_name);
		System.out.println(file_location);
		System.out.println(getNIFFileByText(file_location, tr.getResourceFileAbsolutePath(infile_name).replace(infile_name, outfile_name), false));
		
	}
}