package org.wikivoyage.ru.listings;

import javax.xml.parsers.*;
import javax.xml.transform.TransformerException;

import org.apache.commons.compress.utils.IOUtils;
import org.wikivoyage.ru.listings.entity.WikivoyagePOI;
import org.wikivoyage.ru.listings.input.DumpDownloader;
import org.wikivoyage.ru.listings.input.DumpParser;
import org.wikivoyage.ru.listings.input.PageParser;
import org.wikivoyage.ru.listings.output.*;
import org.xml.sax.SAXException;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;


import java.io.*;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Main {
    private static final Log log = LogFactory.getLog(Main.class);

    private static FileNames fileNames;

    public static void main(String[] args) {
        HashMap<String, OutputFormat> formats = new HashMap<>();
        formats.put("csv", new CSV());
        formats.put("osmand-xml", new OsmXml(false));
        formats.put("osmand-xml-user-defined", new OsmXml(true));
        formats.put("obf", new OBF(false, "tmp", "tmp/pois.xml"));
        formats.put("obf-user-defined", new OBF(true, "tmp", "tmp/pois.xml"));

        CommandLine cl = new CommandLine();
        String [] formatNames = formats.keySet().toArray(new String [formats.keySet().size()]);
        cl.parse(args, formatNames);
        fileNames = new FileNames(cl.listingsDir, cl.dumpsCacheDir, cl.workingDir);

        try {
            if (cl.help) {
                cl.printHelp();
            } else if (cl.dailyUpdate) {
                dailyUpdate(cl, formats);
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

                if (cl.outputFormat != null) {
                    OutputFormat format = formats.get(cl.outputFormat);
                    generateFileForFormat(inputFilename, cl.outputFilename, format);
                }
                log.info("Finished");
            }
        } catch (Exception e) {
            System.err.println("Failure");
            e.printStackTrace();
        }
    }

    private static void dailyUpdate(CommandLine cl, HashMap<String, OutputFormat> formats) throws IOException {
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
                    processDump(downloader, language, latestDumpId, dumpId, formats);
                } catch (Exception e) {
                    log.info("Failed to create dump " + dumpId);
                    log.debug("Exception: ", e);
                }
            }
        }
        System.exit(0);
    }

    private static void processDump(
        DumpDownloader downloader, String language, String latestDumpId, String dumpId,
        HashMap<String, OutputFormat> formats
    ) throws IOException, ParserConfigurationException, SAXException, TransformerException, SQLException, InterruptedException {
        boolean allFileExists = true;
        for (OutputFormat format: formats.values()) {
            String fileName = fileNames.getListingPath(language, dumpId, format.getDefaultExtension(), true);
            if (!fileExists(fileName)) {
                allFileExists = false;
                break;
            }
        }

        if (allFileExists) {
            log.info("All files already exist for '" + language + "-" + dumpId + "'");
            return;
        }

        log.info("Create POIs for '" + dumpId + "'");

        String dumpUrl = downloader.dumpUrl(language, dumpId);
        String dumpPath = fileNames.dumpCacheFilename(language, dumpId);
        if (!fileExists(dumpPath)) {
            downloader.downloadDumpFromUrl(dumpUrl, dumpPath);
        }

        log.info("Parse dump");
        PageParser pageParser = new PageParser();
        DumpParser.parseWikivoyageDump(dumpPath, pageParser);
        WikivoyagePOI[] pois = pageParser.getPOIs();

        for (OutputFormat format: formats.values()) {
            String fileName = fileNames.getListingPath(language, dumpId, format.getDefaultExtension(), false);
            try {
                format.write(pois, fileName);
                if (dumpId.equals(latestDumpId)) {
                    String latestFileName = fileNames.getListingPath(
                            language, "latest", format.getDefaultExtension(), false
                    );
                    removeFile(latestFileName);
                    copyFile(fileName, latestFileName);
                }
                String fileNameArchive = fileNames.getListingPath(language, dumpId, format.getDefaultExtension(), true);
                archive(fileName, fileNameArchive);
            } catch (WriteOutputException e) {
                System.out.println("Failed to write file: " + e.getMessage());
            }
        }
    }

    private static void createListingsDir()
    {
        new File(fileNames.getListingsDir()).mkdirs();
    }

    private static void createDumpsCacheDir()
    {
        new File(fileNames.getDumpsCacheDir()).mkdirs();
    }

    private static void createWorkingDir()
    {
        new File(fileNames.getWorkingDir()).mkdirs();
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

    private static void generateFileForFormat(String inputFilename, String outputFilename, OutputFormat format) throws WriteOutputException, IOException, SAXException, ParserConfigurationException {
        log.info("Parse dump");
        PageParser pageParser = new PageParser();
        DumpParser.parseWikivoyageDump(inputFilename, pageParser);
        WikivoyagePOI[] pois = pageParser.getPOIs();
        log.info("Total " + pois.length + " POIs were found");
        log.info("Save to '" + outputFilename + "'");
        format.write(pois, outputFilename);
    }
}
