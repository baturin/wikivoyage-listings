package org.wikivoyage.listings.cli_parse;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
import org.wikivoyage.listings.execution.ExecutionModeStrategy;
import org.wikivoyage.listings.cli_parse.converters.FilePathConverter;
import org.wikivoyage.listings.cli_parse.converters.LanguageConverter;
import org.wikivoyage.listings.cli_parse.converters.ModeToExecutionStrategyConverter;
import org.wikivoyage.listings.cli_parse.converters.OutputFormatConverter;
import org.wikivoyage.listings.cli_parse.validators.*;
import org.wikivoyage.listings.language.Language;
import org.wikivoyage.listings.output.OutputFormat;

import java.nio.file.Path;

import static org.wikivoyage.listings.execution.ExecutionModeStrategy.BATCH;
import static org.wikivoyage.listings.execution.ExecutionModeStrategy.SINGLE;
import static org.wikivoyage.listings.cli_parse.validators.OutputFormatValidator.allowedOutputsExtensions;

@Parameters(separators = "=")
public class CommandLine {

    @Parameter( names = "-mode",
                required = true,
                validateWith = ModeValidator.class,
                converter = ModeToExecutionStrategyConverter.class, // Converts input string to 0 or 1 for mode field
                description = "Either 'single' or 'batch'"
    )
    public ExecutionModeStrategy mode;

    // *** Batch files generation commands and options ***

    // When batch downloading we can download N number of data backups and run conversion on these.
    // This value represents how many backups we want to download
    @Parameter( names = "-latest-count",
                validateWith = CheckIntegerValid.class,
                description = "latest-count must be an integer greater than 0."
    )
    public Integer latestCount;

    @Parameter( names = "-listings-dir",
                validateWith = CheckValidDirectory.class,
                converter = FilePathConverter.class,
                description = "Which folder to output the resulting output formats to"
    )
    public Path listingsDir;

    @Parameter( names = "-dumps-cache-dir",
                validateWith = CheckValidDirectory.class,
                converter = FilePathConverter.class,
                description = "Which folder to put the cached data files in"
    )
    public Path dumpsCacheDir;

    @Parameter( names = "-working-dir",
                validateWith = CheckValidDirectory.class,
                converter = FilePathConverter.class,
                description = "Where directory to put the temporary files into that this tool creates"
    )
    public Path workingDir;

    // What's the point in this. Intermediate file never seems to do anything...
    @Parameter( names = "-use-intermediate-file")
    public boolean useIntermediateFile = true;


    // *** Single file generation commands and options ***

    // Input options
    @Parameter( names = "-input-file",
                validateWith = InputFileValidator.class,
                description = "Should be either a .bz2 file or .xml file")
    public String inputFile;

    // We do validation for valid URLs later. Worth doing here?
    @Parameter(names = "-input-url")
    public String inputUrl;

    @Parameter( names = "-input-language",
                validateWith = LanguageSupportValidator.class,
                converter = LanguageConverter.class
    )
    public Language inputLanguage;

    // Output options
    @Parameter( names = "-output-filename",
                required = true
    )
    public String outputFilename;

    @Parameter( names = "-output-format",
                validateWith = OutputFormatValidator.class,
                converter = OutputFormatConverter.class,
                required = true
    )
    public OutputFormat outputFormat;

    @Parameter(
            names = {"-help"},
            description = "Displays help information"
    )
    public boolean help;




    public void validateModeInputs() throws ParameterException {
        if (mode.type == SINGLE) {
            int inputCount = 0;
            if (inputFile != null) inputCount++;
            if (inputUrl != null) inputCount++;
            if (inputLanguage != null) inputCount++;

            if (inputCount > 1) {
                throw new ParameterException(
                        "More than one input source ('-input-file', '-input-url', '-input-count') " +
                                "is specified. Specify only one of them."
                );
            }
            else if (inputCount == 0) {
                throw new ParameterException(
                        "Input source is not specified. Specify one with options " +
                                "'-input-file', '-input-url', '-input-count'"
                );
            }
        }
        else if (mode.type == BATCH) {
            // *** Daily update batch file generation ***
            if (
                            inputFile != null ||
                            inputUrl != null ||
                            inputLanguage != null ||
                            outputFilename != null ||
                            outputFormat != null
                    ) {
                throw new ParameterException(
                        "-daily-update option is mutually exclusive with single file generation options"
                );
            }
        }
    }

    public void printHelp() {
        System.out.println("wikivoyage-listings tool takes Wikivoyage (https://wikivoyage.org/) dumps, ");
        System.out.println("parses listings and generates files in different formats, ");
        System.out.println("for example XML or OBF (which could be used with OsmAnd).");
        System.out.println();
        System.out.println("Usage:");
        System.out.println();
        System.out.println("There are 2 modes of work: single file generation and batch file generation.");
        System.out.println();
        System.out.println("========== Single file generation ==========");
        System.out.println("Specify: '-mode single' for single file generation.");
        System.out.println();
        System.out.println("*** Input configuration ***");
        System.out.println("Specify a source of Wikivoyage dump:");
        System.out.println("-input-url <url>: download Wikivoyage dump from specified URL");
        System.out.println("-input-count (en|ru|fr): download latest input-count number of Wikivoyage dumps for specified language");
        System.out.println("-input-file <filename>: use Wikivoyage dump from file");
        System.out.println("The utility accepts XML dump, packed with bzip2 or not.");
        System.out.println();
        System.out.println("*** Output configuration ***");
        System.out.println("Specify output files:");
        System.out.println("-output-filename <filename>: write output data to <filename>");
        System.out.println("-output-format <format>: select one of supported formats:");
        System.out.println("    " + allowedOutputsExtensions + ".");
        System.out.println("    All formats with 'user-defined' prefix set POI types to \"user defined\"");
        System.out.println("    to be ready to used with OsmAnd 1.9");
        System.out.println();
        System.out.println("========== Batch files generation ==========");
        System.out.println("Specify: '-mode batch' for batch file generation.");
        System.out.println("*** Options ***");
        System.out.println("-latest-count <num>: maximum number of latest Wikivoyage dumps ");
        System.out.println("    to process for each language");
        System.out.println("-listings-dir <directory>: output directory to put listings to");
        System.out.println("-dumps-cache-dir <directory>: directory to download and cache Wikivoyage dumps");
        System.out.println("-working-dir <directory>: directory for temporary files");
    }

    public void parse(String[] args) {
        try {
            new JCommander(this, args); // First step of validation of cli
            validateModeInputs(); // Second step of validation
        }
        catch (ParameterException e) {
            System.out.println(e.getMessage());
            System.out.println();
            printHelp();
            System.exit(1);
        }
    }
}
