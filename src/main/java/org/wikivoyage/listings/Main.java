package org.wikivoyage.listings;

import org.wikivoyage.listings.entity.WikivoyagePOI;
import org.wikivoyage.listings.input.DumpDownloader;
import org.wikivoyage.listings.input.DumpListingsIterable;
import org.wikivoyage.listings.input.DumpReadException;
import org.wikivoyage.listings.language.Languages;
import org.wikivoyage.listings.output.*;
import org.wikivoyage.listings.utils.FileUtils;
import org.wikivoyage.listings.utils.FileUtilsException;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wikivoyage.listings.utils.UnrecognizeTemplateCounter;

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
        formats.put("sql", new SQL());
        formats.put("gpx", new GPX());
        formats.put("osmand.gpx", new OsmAndGPX());
        formats.put("kml", new KML());
        formats.put("validation-report", new ValidationReport());


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
                    DumpDownloader downloader = new DumpDownloader();
                    if (cl.inputUrl != null) {
                        inputFilename = fileNames.workingDirPath("dump.xml.bz2");
                        downloader.downloadDumpFromUrl(cl.inputUrl, inputFilename);
                    } else {
                        createDumpsCacheDir();
                        List<String> dumpIds = downloader.listDumps(cl.inputLatest);
                        String latestDumpId = dumpIds.get(0);

                        inputFilename = fileNames.dumpCacheFilename(cl.inputLatest, latestDumpId);
                        if (!FileUtils.fileExists(inputFilename)) {
                            String dumpUrl = downloader.dumpUrl(cl.inputLatest, latestDumpId);
                            downloader.downloadDumpFromUrl(dumpUrl, inputFilename);
                        } else {
                            log.info("Use cached dump");
                        }
                    }
                }

                if (cl.outputFormat != null) {
                    OutputFormat format = formats.get(cl.outputFormat);
                    generateFileForFormat(inputFilename, cl.outputFilename, format);
                }
                UnrecognizeTemplateCounter.getInstance().logUnrecognizeTemplatesSummary();
                log.info("Finished");
            }
        } catch (Exception e) {
            System.err.println("Failure");
            e.printStackTrace();
        }
    }

    private static void dailyUpdate(CommandLine cl, HashMap<String, OutputFormat> formats) throws IOException, FileUtilsException {
        createWorkingDir();
        createListingsDir();
        createDumpsCacheDir();

        DumpDownloader downloader = new DumpDownloader();
        for (String language: Languages.getLanguageCodes()) {
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
    ) throws IOException, FileUtilsException, InterruptedException {
        boolean allFileExists = true;
        for (OutputFormat format: formats.values()) {
            String fileName = fileNames.getListingPath(language, dumpId, format.getDefaultExtension(), true);
            if (!FileUtils.fileExists(fileName)) {
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
        if (!FileUtils.fileExists(dumpPath)) {
            downloader.downloadDumpFromUrl(dumpUrl, dumpPath);
        }

        log.info("Parse dump");
        Iterable<WikivoyagePOI> listingIterable = new DumpListingsIterable(dumpPath);

        for (OutputFormat format: formats.values()) {
            String fileName = fileNames.getListingPath(language, dumpId, format.getDefaultExtension(), false);
            try {
                format.write(listingIterable, fileName);
                if (dumpId.equals(latestDumpId)) {
                    String latestFileName = fileNames.getListingPath(
                            language, "latest", format.getDefaultExtension(), false
                    );
                    FileUtils.removeFile(latestFileName);
                    FileUtils.copyFile(fileName, latestFileName);
                }
                String fileNameArchive = fileNames.getListingPath(language, dumpId, format.getDefaultExtension(), true);
                FileUtils.archive(fileName, fileNameArchive);
            } catch (WriteOutputException e) {
                System.out.println("Failed to write file: " + e.getMessage());
            }
        }
    }

    private static void createListingsDir() throws FileUtilsException
    {
        FileUtils.createDirectory(fileNames.getListingsDir());
    }

    private static void createDumpsCacheDir() throws FileUtilsException
    {
        FileUtils.createDirectory(fileNames.getDumpsCacheDir());
    }

    private static void createWorkingDir() throws FileUtilsException
    {
        FileUtils.createDirectory(fileNames.getWorkingDir());
    }

    private static void generateFileForFormat(
        String inputFilename, String outputFilename, OutputFormat format
    ) throws WriteOutputException, DumpReadException {
        log.info("Parse dump");
        Iterable<WikivoyagePOI> listingIterable = new DumpListingsIterable(inputFilename);
        log.info("Save to '" + outputFilename + "'");
        format.write(listingIterable, outputFilename);
    }
}
