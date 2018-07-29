package org.wikivoyage.listings.execution;

import org.wikivoyage.listings.entity.Listing;
import org.wikivoyage.listings.input.DumpDownloader;
import org.wikivoyage.listings.input.JavaSerializedIterable;
import org.wikivoyage.listings.input.ListingsIterable;
import org.wikivoyage.listings.language.Language;
import org.wikivoyage.listings.output.JavaSerializedObject;
import org.wikivoyage.listings.output.OutputFormat;
import org.wikivoyage.listings.output.WriteOutputException;
import org.wikivoyage.listings.retrieval.LocalRetrieval;
import org.wikivoyage.listings.retrieval.RetrievalStrategy;
import org.wikivoyage.listings.retrieval.WikiMediaServersRetrieval;
import org.wikivoyage.listings.utils.FileUtils;
import org.wikivoyage.listings.utils.FileUtilsException;
import org.wikivoyage.listings.validators.Validator;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class BatchMode extends ExecutionModeStrategy {

    public BatchMode() {
        this.type = BATCH;
    }

    /**
     * When we run the daily update we're not sure if it's been run before.
     * If it has it will have generated a whole bunch of output format files generated in the listings/ folder
     * which represent a specific dumps' output files. E.G in a particular format and language i.e listings/en-20180502.csv.bz2
     *
     *
     *
     */
    @Override
    public void execute() {
        // For every single language's dumps in every single output format, determine whether we have a copy locally or not.
        // Store this plan of retrievals in a list for local/server download.
        DumpDownloader dl = new DumpDownloader();

        for (Language language : Language.languages) {
            log.info("Processing language " + language);

            List<String> dumpIDs;
            try {
                dumpIDs = dl.listDumpIDs(language);
                if (dumpIDs.isEmpty()) {
                    continue;
                }

                // If the limit on the number of dumps we want to download is input, use that number
                // instead of downloading EVERY HISTORICAL DUMP in this language
                if (cli.latestCount != null) {
                    log.info("Processing the latest " + cli.latestCount + " dumps");
                    dumpIDs = dumpIDs.subList(0, cli.latestCount);
                }

                for (String dumpID: dumpIDs) {

                    // Check all the outputs exist for that dumpID language
                    Map<String, OutputFormat> formats = OutputFormat.generateAllOutputFormats();
                    boolean allFileExists = true;
                    for (OutputFormat format: formats.values()) {
                        String fileName = projectFolders.getListingPath(language, dumpID, format, true);
                        if (!FileUtils.fileExists(fileName)) {
                            allFileExists = false;
                            break;
                        }
                    }

                    if (allFileExists) {
                        log.info("All files already exist for '" + language + "-" + dumpID + "'");
                        break;
                    }


                    // Retrieval
                    RetrievalStrategy rs = determineRetrievalMethod(language, dumpID);
                    rs.retrieve();

                    // Parsing
                    Iterable<Listing> listings = new ListingsIterable(rs.whereToStore);

                    // If we're using an intermediate file, then change listings to an
                    // iterator compatible with serialized data
                    try {
                        if (cli.useIntermediateFile) {
                            log.info("Write intermediate file with parsed listings");
                            String javaSerialFile = projectFolders.workingDirPath("serialized-pois.bin");
                            FileUtils.removeFile(javaSerialFile);
                            new JavaSerializedObject().write(listings, javaSerialFile, dumpID);
                            listings = new JavaSerializedIterable(javaSerialFile);
                        }
                    }
                    catch (FileUtilsException | WriteOutputException e) {
                        log.info("Failed to create dump " + dumpID);
                    }

                    // Listings Validation
                    listings = Validator.validateAll(listings);

                    // Outputting
                    for (OutputFormat format : formats.values()) {
                        String whereToOutput = projectFolders.getListingPath(language, dumpID, format, false);
                        try {
                            format.write(listings, whereToOutput, dumpID);
                        }
                        catch (WriteOutputException e) {
                            System.out.println("Failed to write file: " + e.getMessage());
                        }
                    }
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public RetrievalStrategy determineRetrievalMethod(Language language, String dumpID) {
        String inputPathFilename;
        boolean cachedFileExists;

        inputPathFilename = projectFolders.dumpCacheFilename(language, dumpID);
        cachedFileExists = FileUtils.fileExists(inputPathFilename);
        if (cachedFileExists) {
            return new LocalRetrieval(inputPathFilename);
        }
        else {
            DumpDownloader dl = new DumpDownloader();
            String urlToRetrieveFrom = dl.dumpUrl(cli.inputLanguage, dumpID);
            return new WikiMediaServersRetrieval(urlToRetrieveFrom, inputPathFilename);
        }
    }
}