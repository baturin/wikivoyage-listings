package org.wikivoyage.listings.execution;

import org.wikivoyage.listings.entity.Listing;
import org.wikivoyage.listings.input.DumpDownloader;
import org.wikivoyage.listings.input.ListingsIterable;
import org.wikivoyage.listings.output.WriteOutputException;
import org.wikivoyage.listings.retrieval.LocalRetrieval;
import org.wikivoyage.listings.retrieval.WikiMediaServersRetrieval;
import org.wikivoyage.listings.utils.FileUtils;
import org.wikivoyage.listings.validators.Validator;

import java.io.IOException;
import java.util.List;

public class SingleMode extends ExecutionModeStrategy {

    String dumpRetrieved;

    public SingleMode() {
        this.type = SINGLE;
    }

    @Override
    public void execute() {
        // Retrieval
        determineRetrievalMethod();
        dumpRetrieved = retrievalMethod.retrieve().toString();

        // Parsing and Validation
        log.info("Parse dump");
        Iterable<Listing> listingIterables = new ListingsIterable(dumpRetrieved);
        listingIterables = Validator.validateAll(listingIterables);

        // Outputting
        String dumpID = new DumpDownloader().getLatestDumpIdByLanguage(cli.inputLanguage);
        try {
            cli.outputFormat.write(listingIterables, cli.outputFilename, dumpID);
            log.info("Save to '" + cli.outputFilename + "'");
        }
        catch (WriteOutputException e) {
            e.printStackTrace();
        }
    }


    /**
     * Based upon the command line arguments, we determine the method of retrieval
     * for the input data source.
     *
     * If theres an input file specified in the CLI then we use that and locally retrieve
     *
     * If no input file is specified, we try to get from latest dump file from the internet.
     *
     * UNLESS a cached file is present in the cached data folder, then we retrieve locally.
     *
     */
    public void determineRetrievalMethod() {
        // If theres an input file specified in the CLI then we retrieve that file locally
        String inputPathFilename;
        if (cli.inputFile != null) {
            inputPathFilename = projectFolders.workingDirPath("dump.xml.bz2");
            retrievalMethod = new LocalRetrieval(inputPathFilename);
            log.info("Taking data from local file '" + inputPathFilename + "'");
            return;
        }

        // We need to figure out if we have the latest dump file for the language cached
        // If we do, then we locally retrieve
        boolean cachedFileExists;
        try {

            DumpDownloader dl = new DumpDownloader();
            List<String> dumps = dl.listDumpIDs(cli.inputLanguage);

            if (!dumps.isEmpty()) {
                String dumpID = dumps.get(0); // Get the last dump ID
                inputPathFilename = projectFolders.dumpCacheFilename(cli.inputLanguage, dumpID);
                cachedFileExists = FileUtils.fileExists(inputPathFilename);

                if (cachedFileExists) {
                    retrievalMethod = new LocalRetrieval(inputPathFilename);
                    log.info("Using a cached dump");
                }
                else {
                    String urlToRetrieveFrom = dl.dumpUrl(cli.inputLanguage, dumpID);
                    retrievalMethod = new WikiMediaServersRetrieval(urlToRetrieveFrom, inputPathFilename);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}