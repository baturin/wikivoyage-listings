package org.wikivoyage.ru.listings;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

public class CommandLine {
    @Parameter(names="-daily-update")
    public boolean dailyUpdate;
    @Parameter(names="-input-file")
    public String inputFile;
    @Parameter(names="-input-url")
    public String inputUrl;
    @Parameter(names="-input-latest")
    public String inputLatest;
    @Parameter(names="-output-obf")
    public String outputObf;
    @Parameter(names="-output-xml")
    public String outputXml;
    @Parameter(names="-poi-user-defined")
    public boolean poiUserDefined;
    @Parameter(names={"-help", "--help", "-h", "-?"})
    public boolean help;

    public void validate() throws ParameterException {
        if (help || dailyUpdate) {
            return;
        }

        if (inputFile == null && inputUrl == null && inputLatest == null) {
            throw new ParameterException(
                    "Input source is not specified. Specify one with options '-input-file', '-input-url', '-input-latest'"
            );
        }

        int inputCount = 0;
        if (inputFile != null) inputCount++;
        if (inputUrl != null) inputCount++;
        if (inputLatest != null) inputCount++;
        if (inputCount > 1) {
            throw new ParameterException(
                    "More than one input source ('-input-file', '-input-url', '-input-latest') is specified. Specify only one of them."
            );
        }

        if (outputObf == null && outputXml == null) {
            throw new ParameterException(
                    "Output files are not specified. Specify them with options '-output-obf', '-output-xml'"
            );
        }
    }

    public void printHelp()
    {
        System.out.println("wikivoyage-listings tool takes Wikivoyage (https://wikivoyage.org/) dumps, ");
        System.out.println("parses listings and generates XML and OBF files. OBF can be used with OsmAnd.");
        System.out.println();
        System.out.println("Usage:");
        System.out.println();
        System.out.println("*** Input configuration ***");
        System.out.println("Specify a source of Wikivoyage dump:");
        System.out.println("-input-url <url>: download Wikivoyage dump from specified URL");
        System.out.println("-input-latest (en|ru): download latest Wikivoyage dump for specified language");
        System.out.println("-input-file <filename>: use Wikivoyage dump from file");
        System.out.println("The utility accepts XML dump, packed with bzip2 or not.");
        System.out.println();
        System.out.println("*** Output configuration ***");
        System.out.println("Specify output files:");
        System.out.println("-output-obf <filename>: write OBF file to <filename>");
        System.out.println("-output-xml <filename>: write OSM XML file to <filename>");
        System.out.println();
        System.out.println("*** Additional options ***");
        System.out.println("-poi-user-defined: set all POI types to \"user defined\" to use OBF with OsmAnd 1.9.");
    }

    public void parse(String [] args)
    {
        try {
            new JCommander(this, args);
            validate();
        } catch (ParameterException e) {
            System.out.println(e.getMessage());
            System.out.println();
            printHelp();
            System.exit(1);
        }
    }
}
