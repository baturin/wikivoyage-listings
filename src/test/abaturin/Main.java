package test.abaturin;

import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.osmand.IProgress;
import org.apache.tools.bzip2.CBZip2InputStream;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

import net.osmand.data.preparation.IndexCreator;

public class Main {

    static final String RU_DUMP_URL = "https://dumps.wikimedia.org/ruwikivoyage/latest/ruwikivoyage-latest-pages-articles.xml.bz2";
    static final String WORKING_DIR = "tmp";

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

                createWorkingDir();
                generateFiles(inputFilename, outputXmlFilename, mapFilename);
            } else if (command.equals("generate-latest")) {
                if (args.length != 3) {
                    printHelp();
                    System.exit(1);
                }

                String outputXmlFilename = args[1];
                String mapFilename = args[2];

                createWorkingDir();

                String dumpFilename = WORKING_DIR + "/" + "dump.xml.bz2";

                System.out.println("Downloading dump...");
                URL website = new URL(RU_DUMP_URL);
                ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                FileOutputStream fos = new FileOutputStream(dumpFilename);
                fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

                System.out.println("Generating files...");
                generateFiles(dumpFilename, outputXmlFilename, mapFilename);
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

    private static void createWorkingDir()
    {
        new File(WORKING_DIR).mkdirs();
    }

    private static void generateFiles(String inputFilename, String outputXmlFilename, String mapFilename) throws ParserConfigurationException, SAXException, IOException, TransformerException, SQLException, InterruptedException {
        String tempMapFilename = "pois.obf";

        new File(WORKING_DIR + "/" + tempMapFilename).delete();

        PageProcessor pageProcessor = new PageProcessor();

        parseWikivoyageDump(inputFilename, pageProcessor);
        WikivoyagePOI[] pois = pageProcessor.getPOIs();
        writePOIsToXML(pois, outputXmlFilename);
        createObf(outputXmlFilename, WORKING_DIR, tempMapFilename);
        Files.move(Paths.get(WORKING_DIR + "/" + tempMapFilename), Paths.get(mapFilename));
    }

    private static void printHelp() {
        System.out.println("Available commands: ");
        System.out.println("- help");
        System.out.println("- generate <wikivoyage-dump> <output-xml> <output-obf>");
        System.out.println("- generate-latest <output-xml> <output-obf>");
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

        InputStream in;

        if (inputFilename.endsWith(".bz2")) {
            FileInputStream fin = new FileInputStream(inputFilename);
            BufferedInputStream bufin = new BufferedInputStream(fin);

            // skip first 2 bytes for CBZip2InputStream
            bufin.read();
            bufin.read();

            in = new CBZip2InputStream(bufin);
        } else {
            in = new FileInputStream(new File(inputFilename));
        }
        saxParser.parse(in, handler);
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
