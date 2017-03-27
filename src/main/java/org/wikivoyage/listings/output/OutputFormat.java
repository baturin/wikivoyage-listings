package org.wikivoyage.listings.output;

import org.wikivoyage.listings.entity.Listing;


public interface OutputFormat {
    /**
     * Write POIs to a file
     * @param pois list of POIs to write to file
     * @param outputFilename name of a file to write to
     * @param dumpDate Date of the dump. Can be embedded in files to provide freshness information to users (example: "Generated from Wikivoyage 2016/07/20 data).
     * @throws WriteOutputException
     */
    public void write(Iterable<Listing> pois, String outputFilename, String dumpDate) throws WriteOutputException;

    /**
     * Get default extension of this output format.
     * Could be ued to automatically generate filenames for files of this output format.
     * @return extension string, with leading dot, for example ".xml", or ".gpx"
     */
    public String getDefaultExtension();
}
