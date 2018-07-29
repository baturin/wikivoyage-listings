package org.wikivoyage.listings.output;

import org.wikivoyage.listings.entity.Listing;
import org.wikivoyage.listings.output.navigationalOutputs.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface OutputFormat {
    /**
     * Write POIs to a file
     *
     * @param pois           list of POIs to write to file
     * @param outputFilename name of a file to write to
     * @param dumpDate       Date of the dump. Can be embedded in files to provide freshness information to users (example: "Generated from Wikivoyage 2016/07/20 data).
     * @throws WriteOutputException
     */
    void write(Iterable<Listing> pois, String outputFilename, String dumpDate) throws WriteOutputException;

    /**
     * Get default extension of this output format.
     * Could be ued to automatically generate filenames for files of this output format.
     *
     * @return extension string, with leading dot, for example ".xml", or ".gpx"
     */
    String getDefaultExtension();


    /**
     * Generates a Hashmap containing the name of the input format and maps to
     * a OutputFormat object.
     * @return The hashmap containing the Name, OutputFormat obj
     */
    static Map<String, OutputFormat> generateAllOutputFormats() {
        HashMap<String, OutputFormat> formats = new HashMap<>();
        formats.put("csv", new CSV());
        formats.put("osmand-xml", new OsmXml(false));
        formats.put("osmand-xml-user-defined", new OsmXml(true));
        formats.put("xml", new Xml());
        formats.put("obf", new OBF(false, "tmp", "tmp/pois.xml"));
        formats.put("obf-user-defined", new OBF(true, "tmp", "tmp/pois.xml"));
        formats.put("sql", new SQL());
        formats.put("gpx", new GPX());
        formats.put("osmand.gpx", new OsmAndGPX());
        formats.put("kml", new KML());
        formats.put("validation-report", new ValidationReport());

        return formats;
    }

    /**
     * Gets a list of all the valid extensions for every output format
     * @return
     */
    static Map<String, OutputFormat> getExtensionsToFormatType() {
        Map<String, OutputFormat> extensionsToOutput = new HashMap<>();

        // Gets all instances
        List<OutputFormat> formats = new ArrayList<>(generateAllOutputFormats().values());
        for (OutputFormat of: formats) {
            extensionsToOutput.put(of.getDefaultExtension(), of);
        }

        return extensionsToOutput;
    }
}
