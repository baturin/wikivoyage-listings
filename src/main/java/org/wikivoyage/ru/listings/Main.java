package org.wikivoyage.ru.listings;

import javax.xml.parsers.*;
import javax.xml.transform.TransformerException;

import org.wikivoyage.ru.listings.entity.WikivoyagePOI;
import org.wikivoyage.ru.listings.input.DumpDownloader;
import org.wikivoyage.ru.listings.input.DumpParser;
import org.wikivoyage.ru.listings.input.PageParser;
import org.wikivoyage.ru.listings.output.OBF;
import org.wikivoyage.ru.listings.output.OsmXml;
import org.xml.sax.SAXException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

public class Main {

    static final String RU_DUMP_URL = "https://dumps.wikimedia.org/ruwikivoyage/latest/ruwikivoyage-latest-pages-articles.xml.bz2";
    static final String WORKING_DIR = "tmp";

    public static void main(String[] args) {
        CommandLine cl = new CommandLine();
        cl.parse(args);

        try {
            if (cl.help) {
                cl.printHelp();
            } else {
                String inputFilename;
                createWorkingDir();
                if (cl.inputFile != null) {
                    inputFilename = cl.inputFile;
                } else {
                    inputFilename = WORKING_DIR + "/" + "dump.xml.bz2";
                    String dumpUrl;
                    if (cl.inputUrl != null) {
                        dumpUrl = cl.inputUrl;
                    } else {
                        dumpUrl = RU_DUMP_URL;
                    }

                    DumpDownloader.downloadDump(dumpUrl, inputFilename);
                }

                generateFiles(inputFilename, cl.outputXml, cl.outputObf);
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

    private static void generateFiles(String inputFilename, String outputXmlFilename, String outputObf) throws ParserConfigurationException, SAXException, IOException, TransformerException, SQLException, InterruptedException {
        String tempMapFilename = "pois.obf";

        if (outputXmlFilename == null) {
            outputXmlFilename = WORKING_DIR + "/pois.xml";
        }

        new File(WORKING_DIR + "/" + tempMapFilename).delete();

        PageParser pageParser = new PageParser();

        DumpParser.parseWikivoyageDump(inputFilename, pageParser);
        WikivoyagePOI[] pois = pageParser.getPOIs();
        OsmXml.writePOIsToXML(pois, outputXmlFilename);

        if (outputObf != null) {
            OBF.createObf(outputXmlFilename, WORKING_DIR, tempMapFilename);
            Files.move(Paths.get(WORKING_DIR + "/" + tempMapFilename), Paths.get(outputObf));
        }
    }
}
