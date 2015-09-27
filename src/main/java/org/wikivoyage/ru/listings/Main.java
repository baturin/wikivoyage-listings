package org.wikivoyage.ru.listings;

import javax.xml.parsers.*;
import javax.xml.transform.TransformerException;

import org.apache.commons.compress.utils.IOUtils;
import org.wikivoyage.ru.listings.entity.WikivoyagePOI;
import org.wikivoyage.ru.listings.input.DumpDownloader;
import org.wikivoyage.ru.listings.input.DumpParser;
import org.wikivoyage.ru.listings.input.PageParser;
import org.wikivoyage.ru.listings.output.OBF;
import org.wikivoyage.ru.listings.output.OsmXml;
import org.xml.sax.SAXException;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Main {
    private static final Log log = LogFactory.getLog(Main.class);

    private static final FileNames fileNames = new FileNames();

    public static void main(String[] args) {
        CommandLine cl = new CommandLine();
        cl.parse(args);

        try {
            if (cl.help) {
                cl.printHelp();
            } else if (cl.dailyUpdate) {
                createWorkingDir();
                createListingsDir();
                createDumpsCacheDir();

                DumpDownloader downloader = new DumpDownloader();
                for (String language: Languages.getLanguages()) {
                    log.info("Processing language " + language);
                    List<String> dumpIds = downloader.listDumps(language);

                    if (dumpIds.size() == 0) {
                        continue;
                    }

                    Collections.sort(dumpIds);
                    Collections.reverse(dumpIds);

                    String latestDumpId = dumpIds.get(0);

                    if (cl.latestCount != null) {
                        log.info("Processing the latest " + cl.latestCount + " dumps");
                        dumpIds = dumpIds.subList(0, cl.latestCount);
                    }

                    for (String dumpId: dumpIds) {
                        log.info("Processing dump " + dumpId);
                        try {
                            String outputXml = fileNames.listingXmlPath(language, dumpId, false);
                            String outputObf = fileNames.listingObfPath(language, dumpId, false);
                            String outputXmlUserDefined = fileNames.listingXmlUserDefinedPath(language, dumpId, false);
                            String outputObfUserDefined = fileNames.listingObfUserDefinedPath(language, dumpId, false);
                            String outputXmlArchive = fileNames.listingXmlPath(language, dumpId, true);
                            String outputObfArchive = fileNames.listingObfPath(language, dumpId, true);
                            String outputXmlUserDefinedArchive = fileNames.listingXmlUserDefinedPath(language, dumpId, true);
                            String outputObfUserDefinedArchive = fileNames.listingObfUserDefinedPath(language, dumpId, true);

                            if (
                                !fileExists(outputXmlArchive) ||
                                !fileExists(outputObfArchive) ||
                                !fileExists(outputXmlUserDefinedArchive) ||
                                !fileExists(outputObfUserDefinedArchive)
                            ) {
                                log.info("Create POIs for '" + dumpId + "'");

                                String dumpUrl = downloader.dumpUrl(language, dumpId);
                                String dumpPath = fileNames.dumpCacheFilename(language, dumpId);
                                if (!fileExists(dumpPath)) {
                                    downloader.downloadDumpFromUrl(dumpUrl, dumpPath);
                                }
                                generateFiles(dumpPath, outputXml, outputObf, false);
                                generateFiles(dumpPath, outputXmlUserDefined, outputObfUserDefined, true);

                                if (dumpId.equals(latestDumpId)) {
                                    log.info("Updating latest files");
                                    removeFile(fileNames.listingXmlLatestPath(language));
                                    removeFile(fileNames.listingXmlUserDefinedLatestPath(language));
                                    removeFile(fileNames.listingObfLatestPath(language));
                                    removeFile(fileNames.listingObfUserDefinedLatestPath(language));
                                    copyFile(outputXml, fileNames.listingXmlLatestPath(language));
                                    copyFile(outputXmlUserDefined,fileNames.listingXmlUserDefinedLatestPath(language));
                                    copyFile(outputObf, fileNames.listingObfLatestPath(language));
                                    copyFile(outputObfUserDefined, fileNames.listingObfUserDefinedLatestPath(language));
                                }

                                archive(outputXml, outputXmlArchive);
                                archive(outputXmlUserDefined, outputXmlUserDefinedArchive);
                                archive(outputObf, outputObfArchive);
                                archive(outputObfUserDefined, outputObfUserDefinedArchive);
                            }
                        } catch (Exception e) {
                            log.info("Failed to create dump " + dumpId);
                            log.debug("Exception: ", e);
                        }
                    }
                }
                System.exit(0);
            } else {
                String inputFilename;
                createWorkingDir();
                if (cl.inputFile != null) {
                    inputFilename = cl.inputFile;
                    log.info("Take POIs from '" + inputFilename + "'");
                } else {
                    inputFilename = fileNames.workingDirPath("dump.xml.bz2");
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

    private static void createListingsDir()
    {
        new File(fileNames.listingsDir()).mkdirs();
    }

    private static void createDumpsCacheDir()
    {
        new File(fileNames.dumpsCacheDir()).mkdirs();
    }

    private static void createWorkingDir()
    {
        new File(fileNames.workingDir()).mkdirs();
    }

    private static void removeFile(String filename)
    {
        new File(filename).delete();
    }

    private static void archive(String inputFilename, String outputFilename) throws IOException {
        InputStream in = new FileInputStream(inputFilename);
        OutputStream out = new FileOutputStream(outputFilename);
        BZip2CompressorOutputStream os = new BZip2CompressorOutputStream(out);
        try {
            IOUtils.copy(in, os);
        } finally {
            os.close();
            in.close();
            out.close();
        }
        removeFile(inputFilename);
    }

    private static void copyFile(String fromFilename, String toFilename) throws IOException {
        InputStream in = new FileInputStream(fromFilename);
        OutputStream out = new FileOutputStream(toFilename);

        try {
            IOUtils.copy(in, out);
        } finally {
            in.close();
            out.close();
        }
    }

    private static boolean fileExists(String filename)
    {
        File f = new File(filename);
        return f.exists() && !f.isDirectory();
    }

    private static void generateFiles(String inputFilename, String outputXmlFilename, String outputObf, boolean userDefined) throws ParserConfigurationException, SAXException, IOException, TransformerException, SQLException, InterruptedException {
        String tempMapFilename = "pois.obf";

        if (outputXmlFilename == null) {
            outputXmlFilename = fileNames.workingDirPath("pois.xml");
        }

        removeFile(fileNames.workingDirPath(tempMapFilename));

        PageParser pageParser = new PageParser();

        log.info("Parse dump");
        DumpParser.parseWikivoyageDump(inputFilename, pageParser);
        WikivoyagePOI[] pois = pageParser.getPOIs();
        log.info("Total " + pois.length + " POIs were found");
        log.info("Save XML to '" + outputXmlFilename + "'");
        OsmXml.writePOIsToXML(pois, outputXmlFilename, userDefined);

        if (outputObf != null) {
            log.info("Save OBF to '" + outputObf + "'");
            OBF.createObf(outputXmlFilename, fileNames.workingDir(), tempMapFilename);
            Files.move(Paths.get(fileNames.workingDirPath(tempMapFilename)), Paths.get(outputObf));
        }
    }
}
