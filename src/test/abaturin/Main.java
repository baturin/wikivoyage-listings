package test.abaturin;

import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.osmand.IProgress;
import net.osmand.osm.io.IOsmStorageFilter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import net.osmand.data.preparation.IndexCreator;

public class Main {
    public static void main(String[] args) {
        String inputFilename = "/home/alexey/wikivoyage/ruwikivoyage-20141229-pages-articles-multistream.xml";
        String outputXmlFilename = "/home/alexey/wikivoyage/pois.xml";
        String workingDir = "/home/alexey/wikivoyage";

        PageProcessor pageProcessor = new PageProcessor();

        try {
            parseWikivoyageDump(inputFilename, pageProcessor);
            WikivoyagePOI[] pois = pageProcessor.getPOIs();
            writePOIsToXML(pois, outputXmlFilename);
            createObf(outputXmlFilename, workingDir);
        } catch (Exception e) {
            System.err.println("Failure");
            e.printStackTrace();
        }
    }

    private static void createObf(String outputFilename, String workingDir) throws IOException, SAXException, SQLException, InterruptedException {
        IndexCreator creator = new IndexCreator(new File(workingDir));
        creator.setIndexPOI(true);
        creator.generateIndexes(new File(outputFilename), IProgress.EMPTY_PROGRESS, null, null, null, null);
    }

    private static void parseWikivoyageDump(String inputFilename, final PageProcessor pageProcessor)
            throws ParserConfigurationException, SAXException, IOException
    {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        DefaultHandler handler = new DefaultHandler(){
            String pageName = null;
            String text = null;
            String ns = null;
            private StringBuffer curCharValue = new StringBuffer(1024);

            public void startElement(String uri, String localName,String qName,
                                     Attributes attributes) throws SAXException {
                if (qName == "page") {
                    pageName = null;
                    text = null;
                    ns = null;
                } else if (qName == "title" || qName == "text" || qName == "ns") {
                    curCharValue = new StringBuffer(1024);
                }
            }

            @Override
            public void characters (char ch[], int start, int length) throws SAXException
            {
                curCharValue.append(ch, start, length);
            }

            public void endElement(String uri, String localName,
                                   String qName) throws SAXException {

                if (qName == "title") {
                    pageName = curCharValue.toString();
                } else if (qName == "text") {
                    text = curCharValue.toString();
                } else if (qName == "ns") {
                    ns = curCharValue.toString();
                } else if (qName == "page") {
                    if (ns.equals("0")) {
                        pageProcessor.processPage(text);
                    }
                }

            }
        };
        saxParser.parse(inputFilename, handler);
    }

    private static void writePOIsToXML(WikivoyagePOI[] pois, String outputFilename) throws ParserConfigurationException, TransformerException {
        for (WikivoyagePOI poi: pois) {
            System.out.println(poi.humanReadable());
        }
        System.out.println(pois.length);

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();

        Element rootElement = doc.createElement("osm");
        rootElement.setAttribute("version", "0.5");
        rootElement.setAttribute("generator", "wikivoyage-pois-converter");
        doc.appendChild(rootElement);

        for (WikivoyagePOI poi: pois) {
            Element node = createPoiNode(doc, poi);
            rootElement.appendChild(node);
            addKVNode(doc, node, "wikivoyage", poi.getType());
            addKVNode(doc, node, "name", poi.getTitle());
            addKVNode(doc, node, "description", poi.getDescription());
        }

        writeXmlToFile(doc, outputFilename);
    }

    private static void writeXmlToFile(Document doc, String outputFilename) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        configureIndentation(transformer);
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(outputFilename));

        transformer.transform(source, result);
    }

    private static void configureIndentation(Transformer transformer) {
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
    }

    private static Element createPoiNode(Document doc, WikivoyagePOI poi) {
        Element node = doc.createElement("node");
        node.setAttribute("id", nodeId.toString());
        node.setAttribute("visible", "true");
        node.setAttribute("lat", new Float(poi.getLatitude()).toString());
        node.setAttribute("lon", new Float(poi.getLongitude()).toString());
        nodeId++;
        return node;
    }

    private static void addKVNode(Document doc, Element rootNode, String name, String value)
    {
        Element newNode = doc.createElement("tag");
        newNode.setAttribute("k", name);
        newNode.setAttribute("v", value);
        rootNode.appendChild(newNode);
    }

    private static Integer nodeId = 0;
}
