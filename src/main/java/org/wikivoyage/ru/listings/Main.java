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
                    DumpDownloader downloader = new DumpDownloader();
                    if (cl.inputUrl != null) {
                        downloader.downloadDumpFromUrl(cl.inputUrl, inputFilename);
                    } else {
                        downloader.downloadLanguageDump(cl.inputLatest, inputFilename);
                    }
                }

                generateFiles(inputFilename, cl.outputXml, cl.outputObf, cl.poiUserDefined);
                System.out.println("Finished");
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

    private static void generateFiles(String inputFilename, String outputXmlFilename, String outputObf, boolean userDefined) throws ParserConfigurationException, SAXException, IOException, TransformerException, SQLException, InterruptedException {
        String tempMapFilename = "pois.obf";

        if (outputXmlFilename == null) {
            outputXmlFilename = WORKING_DIR + "/pois.xml";
        }

        new File(WORKING_DIR + "/" + tempMapFilename).delete();

        PageParser pageParser = new PageParser();

        System.out.println("Parsing dump...");
        DumpParser.parseWikivoyageDump(inputFilename, pageParser);
        WikivoyagePOI[] pois = pageParser.getPOIs();
        System.out.println("Saving XML...");
        OsmXml.writePOIsToXML(pois, outputXmlFilename, userDefined);

        if (outputObf != null) {
            System.out.println("Saving OBF...");
            OBF.createObf(outputXmlFilename, WORKING_DIR, tempMapFilename);
            Files.move(Paths.get(WORKING_DIR + "/" + tempMapFilename), Paths.get(outputObf));
        }
    }
}
