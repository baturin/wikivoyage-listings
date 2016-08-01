package org.wikivoyage.listings;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

import java.util.Arrays;

public class CommandLine {
    // *** Batch files generation commands and options ***
    @Parameter(names="-daily-update")
    public boolean dailyUpdate;
    @Parameter(names="-latest-count")
    public Integer latestCount;
    @Parameter(names="-listings-dir")
    public String listingsDir;
    @Parameter(names="-dumps-cache-dir")
    public String dumpsCacheDir;
    @Parameter(names="-working-dir")
    public String workingDir;
    @Parameter(names="-do-not-use-intermediate-file")
    public boolean doNotUseIntermediateFile;

    // *** Single file generation commands and options ***
    @Parameter(names="-generate")
    public boolean generate;
    // Input options
    @Parameter(names="-input-file")
    public String inputFile;
    @Parameter(names="-input-url")
    public String inputUrl;
    @Parameter(names="-input-latest")
    public String inputLatest;

    // Output options
    @Parameter(names="-output-filename")
    public String outputFilename;
    @Parameter(names="-output-format")
    public String outputFormat;

    // *** Help ***
    @Parameter(names={"-help", "--help", "-h", "-?"})
    public boolean help;

    public void validate(String [] allowedFormats) throws ParameterException {
        if (help) {
            // *** Help ***
            if (
                dailyUpdate ||
                latestCount != null ||
                listingsDir != null ||
                dumpsCacheDir != null ||
                workingDir != null ||
                generate ||
                inputFile != null ||
                inputUrl != null ||
                inputLatest != null ||
                outputFilename != null ||
                outputFormat != null
            )
            throw new ParameterException(
                "-help option is mutually exclusive with all other options"
            );
        } else if (dailyUpdate) {
            // *** Daily update batch file generation ***
            if (
                generate ||
                inputFile != null ||
                inputUrl != null ||
                inputLatest != null ||
                outputFilename != null ||
                outputFormat != null
            ) {
                throw new ParameterException(
                    "-daily-update option is mutually exclusive with single file generation options"
                );
            }
        } else if (generate) {
            // *** Single file generation ***
            int inputCount = 0;
            if (inputFile != null) inputCount++;
            if (inputUrl != null) inputCount++;
            if (inputLatest != null) inputCount++;
            if (inputCount > 1) {
                throw new ParameterException(
                    "More than one input source ('-input-file', '-input-url', '-input-latest') " +
                    "is specified. Specify only one of them."
                );
            } else if (inputCount == 0) {
                throw new ParameterException(
                    "Input source is not specified. Specify one with options " +
                    "'-input-file', '-input-url', '-input-latest'"
                );
            }
            if (outputFilename == null) {
                throw new ParameterException(
                    "Output file was not specified. Specify one with -output-filename option"
                );
            }
            if (outputFormat == null) {
                throw new ParameterException(
                    "Output format was not specified. Specify one with -output-format option"
                );
            }
            if (!Arrays.asList(allowedFormats).contains(outputFormat)) {
                throw new ParameterException(
                    "Output format '" + outputFormat + "' is not supported. Specify one of supported formats: " +
                    Arrays.toString(allowedFormats)
                );
            }
        } else {
            throw new ParameterException(
                "No mode was specified. Specify either '-generate' or '-daily-update' command"
            );
        }
    }

    public void printHelp(String [] allowedFormats)
    {
        System.out.println("wikivoyage-listings tool takes Wikivoyage (https://wikivoyage.org/) dumps, ");
        System.out.println("parses listings and generates files in different formats, ");
        System.out.println("for example XML or OBF (which could be used with OsmAnd).");
        System.out.println();
        System.out.println("Usage:");
        System.out.println();
        System.out.println("There are 2 modes of work: single file generation and batch file generation.");
        System.out.println();
        System.out.println("========== Single file generation ==========");
        System.out.println("Specify: '-generate' for single file generation.");
        System.out.println();
        System.out.println("*** Input configuration ***");
        System.out.println("Specify a source of Wikivoyage dump:");
        System.out.println("-input-url <url>: download Wikivoyage dump from specified URL");
        System.out.println("-input-latest (en|ru|fr): download latest Wikivoyage dump for specified language");
        System.out.println("-input-file <filename>: use Wikivoyage dump from file");
        System.out.println("The utility accepts XML dump, packed with bzip2 or not.");
        System.out.println();
        System.out.println("*** Output configuration ***");
        System.out.println("Specify output files:");
        System.out.println("-output-filename <filename>: write output data to <filename>");
        System.out.println("-output-format <format>: select one of supported formats:");
        System.out.println("    " + Arrays.toString(allowedFormats) + ".");
        System.out.println("    All formats with 'user-defined' prefix set POI types to \"user defined\"");
        System.out.println("    to be ready to used with OsmAnd 1.9");
        System.out.println();
        System.out.println("========== Batch files generation ==========");
        System.out.println("Specify: '-daily-update' for batch file generation.");
        System.out.println("*** Options ***");
        System.out.println("-latest-count <num>: maximum number of latest Wikivoyage dumps ");
        System.out.println("    to process for each language");
        System.out.println("-listings-dir <directory>: output directory to put listings to");
        System.out.println("-dumps-cache-dir <directory>: directory to download and cache Wikivoyage dumps");
        System.out.println("-working-dir <directory>: directory for temporary files");
    }

    public void parse(String [] args, String [] allowedFormats)
    {
        try {
            new JCommander(this, args);
            validate(allowedFormats);
        } catch (ParameterException e) {
            System.out.println(e.getMessage());
            System.out.println();
            printHelp(allowedFormats);
            System.exit(1);
        }
    }
}
