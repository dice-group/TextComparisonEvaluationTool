package Engines.IO;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import Engines.SimpleObjects.MetricVectorProcessing;

public class PropWriterXML {

	/**
	 * This method stores the raw content of the Gold Standard Text
	 * 
	 * @param mvp
	 * @param path
	 * @return content file path
	 */
	public static void writeMVP(MetricVectorProcessing mvp, String path) {
		String root = mvp.getName();
		Element rootElem, featureElem, typ1Elem, typ2Elem;
		Document dom;
		DocumentBuilder db;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {
			db = dbf.newDocumentBuilder(); // use factory to get an instance of
											// document builder
			dom = db.newDocument(); // create instance of DOM
			rootElem = dom.createElement(root); // create the root element
			
			//##############################################################################
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

			featureElem = dom.createElement("M_1");
			rootElem.appendChild(featureElem);					// Set feature node
			
			for(Character key : mvp.symbol_error_dist.keySet())
			{
				typ1Elem = dom.createElement(""+key);
				typ2Elem = dom.createElement("");
			}
			
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			
			featureElem = dom.createElement("M_2");
			typ1Elem = dom.createElement("");
			typ2Elem = dom.createElement("");
			
			rootElem.appendChild(featureElem);					// Set feature node
			
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			
			featureElem = dom.createElement("M_3");
			typ1Elem = dom.createElement("");
			typ2Elem = dom.createElement("");
			
			rootElem.appendChild(featureElem);					// Set feature node
			
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			
			featureElem = dom.createElement("M_4");
			typ1Elem = dom.createElement("");
			typ2Elem = dom.createElement("");
			
			rootElem.appendChild(featureElem);					// Set feature node
			
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			
			featureElem = dom.createElement("M_5");
			typ1Elem = dom.createElement("");
			typ2Elem = dom.createElement("");
			
			rootElem.appendChild(featureElem);					// Set feature node
			
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			
			featureElem = dom.createElement("M_6");
			typ1Elem = dom.createElement("");
			typ2Elem = dom.createElement("");
			
			rootElem.appendChild(featureElem);					// Set feature node
			
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			
			featureElem = dom.createElement("M_GERBIL");
			typ1Elem = dom.createElement("");
			typ2Elem = dom.createElement("");
			
			rootElem.appendChild(featureElem);					// Set feature node

			
			
			//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
			//##############################################################################
			
			dom.appendChild(rootElem);

			try {
				Transformer tr = TransformerFactory.newInstance().newTransformer();
				tr.setOutputProperty(OutputKeys.INDENT, "yes");
				tr.setOutputProperty(OutputKeys.METHOD, "xml");
				tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

				// send DOM to file
				tr.transform(new DOMSource(dom), new StreamResult(new FileOutputStream(pathXML)));

			} catch (TransformerException te) {
				System.out.println(te.getMessage());
			} catch (IOException ioe) {
				System.out.println(ioe.getMessage());
			}
		} catch (ParserConfigurationException pce) {
			System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
		}

	}

}
