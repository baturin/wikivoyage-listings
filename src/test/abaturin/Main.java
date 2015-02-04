package test.abaturin;

import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.osmand.IProgress;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

import net.osmand.data.preparation.IndexCreator;

public class Main {
    public static void main(String[] args) {
        try {
            if (args.length < 1) {
                printHelp();
                System.exit(1);
            }

            String command = args[0];
            if (command.equals("generate")) {
                if (args.length != 4) {
                    printHelp();
                    System.exit(1);
                }

                String inputFilename = args[1];
                String outputXmlFilename = args[2];
                String mapFilename = args[3];

                String workingDir = "tmp";
                String tempMapFilename = "pois.obf";

                new File(workingDir).mkdirs();
                new File(workingDir + "/" + tempMapFilename).delete();

                PageProcessor pageProcessor = new PageProcessor();

                parseWikivoyageDump(inputFilename, pageProcessor);
                WikivoyagePOI[] pois = pageProcessor.getPOIs();
                writePOIsToXML(pois, outputXmlFilename);
                createObf(outputXmlFilename, workingDir, tempMapFilename);
                Files.move(Paths.get(workingDir + "/" + tempMapFilename), Paths.get(mapFilename));

            } else if (command.equals("help")) {
                printHelp();
                System.exit(0);
            } else {
                printHelp();
                System.exit(1);
            }
        } catch (Exception e) {
            System.err.println("Failure");
            e.printStackTrace();
        }
    }

    private static void printHelp() {
        System.out.println("Available commands: ");
        System.out.println("- help");
        System.out.println("- generate <wikivoyage-dump> <output-xml> <output-obf>");
    }

    private static void createObf(String outputFilename, String workingDir, String mapFile) throws IOException, SAXException, SQLException, InterruptedException {
        IndexCreator creator = new IndexCreator(new File(workingDir));
        creator.setMapFileName(mapFile);
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
                if (qName.equals("page")) {
                    pageName = null;
                    text = null;
                    ns = null;
                } else if (qName.equals("title") || qName.equals("text") || qName.equals("ns")) {
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

                if (qName.equals("title")) {
                    pageName = curCharValue.toString();
                } else if (qName.equals("text")) {
                    text = curCharValue.toString();
                } else if (qName.equals("ns")) {
                    ns = curCharValue.toString();
                } else if (qName.equals("page")) {
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
        node.setAttribute("lat", Float.toString(poi.getLatitude()));
        node.setAttribute("lon", Float.toString(poi.getLongitude()));
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
