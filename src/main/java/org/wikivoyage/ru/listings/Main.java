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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Main {
    private static final Log log = LogFactory.getLog(Main.class);

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
                    log.info("Take POIs from '" + inputFilename + "'");
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
                log.info("Finished");
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

        log.info("Parse dump");
        DumpParser.parseWikivoyageDump(inputFilename, pageParser);
        WikivoyagePOI[] pois = pageParser.getPOIs();
        log.info("Total " + pois.length + " POIs were found");
        log.info("Save XML to '" + outputXmlFilename + "'");
        OsmXml.writePOIsToXML(pois, outputXmlFilename, userDefined);

        if (outputObf != null) {
            log.info("Save OBF to '" + outputObf + "'");
            OBF.createObf(outputXmlFilename, WORKING_DIR, tempMapFilename);
            Files.move(Paths.get(WORKING_DIR + "/" + tempMapFilename), Paths.get(outputObf));
        }
    }
}
