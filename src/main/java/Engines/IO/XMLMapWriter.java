import com.epam.lab.model.exceptions.CreateDocumentConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class XmlBuilder {

    private DocumentBuilder builder;

    private Document doc;

    /**
     * Constructs an item list builder.
     *
     * @throws CreateDocumentConfigurationException
     */
    public XmlBuilder() throws CreateDocumentConfigurationException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new CreateDocumentConfigurationException("exception create new document", e);
        }
    }

    /**
     * Builds a DOM document for an array list of items.
     *
     * @param elementMap map of items.
     * @return a DOM document describing the items.
     */
    public Document build(Map<String, List<String>> elementMap) {
        doc = builder.newDocument();
        doc.appendChild(createItems(elementMap));
        return doc;
    }

    /**
     * Builds a DOM element for an array list of items.
     *
     * @param elementMap the map of items
     * @return a DOM element describing the items
     */
    private Element createItems(Map<String, List<String>> elementMap) {
        Element e = null;
        for (Map.Entry<String, List<String>> anItem : elementMap.entrySet()) {
            e = doc.createElement(anItem.getKey());
            for (Node node : createItemsList(anItem.getValue())) {
                e.appendChild(node);
            }
        }
        return e;
    }

    private List<Node> createItemsList(List<String> items) {
        List<Node> result = new ArrayList<>();
        for (String item : items) {
            Element item1 = createItem(item);
            result.add(item1);
        }
        return result;
    }

    /**
     * Builds a DOM element for an item.
     *
     * @param anItem the item
     * @return a DOM element describing the item
     */
    private Element createItem(String anItem) {
        // if you need some text element to your element - just append it here.
        return doc.createElement(anItem);
    }

    /**
     * Builds the text content for document
     *
     * @param name element
     * @param text content
     * @return text element
     */
    private Element createTextElement(String name, String text) {
        Text t = doc.createTextNode(text);
        Element e = doc.createElement(name);
        e.appendChild(t);
        return e;
    }


    private String generateXmlContent(Map<String, List<String>> elementMap) {
        String content;

        Document doc = build(elementMap);
        DOMImplementation impl = doc.getImplementation();
        DOMImplementationLS implLS = (DOMImplementationLS) impl.getFeature("LS", "3.0");

        LSSerializer ser = implLS.createLSSerializer();
        ser.getDomConfig().setParameter("format-pretty-print", true);
        content = ser.writeToString(doc);

        return content;
    }

    public void writeToXmlFile(String xmlContent) {
        File theDir = new File("./output");
        if (!theDir.exists())
            theDir.mkdir();

        String fileName = "./output/" + this.getClass().getSimpleName() + "_"
                + Calendar.getInstance().getTimeInMillis() + ".xml";

        try (OutputStream stream = new FileOutputStream(new File(fileName))) {
            try (OutputStreamWriter out = new OutputStreamWriter(stream, StandardCharsets.UTF_16)) {
                out.write(xmlContent);
                out.write("\n");
            }
        } catch (IOException ex) {
            System.err.println("Cannot write to file!" + ex.getMessage());
        }
    }


    public static void main(String[] args) throws CreateDocumentConfigurationException {
        XmlBuilder xmlBuilder = new XmlBuilder();
        Map<String, List<String>> map = MapFactory.mapOf(MapFactory.entry("root", Arrays.asList("element1", "element2", "element3")));

        String xmlContent = xmlBuilder.generateXmlContent(map);
        xmlBuilder.writeToXmlFile(xmlContent);
    }
}