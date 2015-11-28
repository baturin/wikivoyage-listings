package org.wikivoyage.listings.output;

import org.wikivoyage.listings.entity.WikivoyagePOI;


public interface OutputFormat {
    /**
     * Write POIs to a file
     * @param pois list of POIs to write to file
     * @param outputFilename name of a file to write to
     * @throws WriteOutputException
     */
    public void write(Iterable<WikivoyagePOI> pois, String outputFilename) throws WriteOutputException;

    /**
     * Get default extension of this output format.
     * Could be ued to automatically generate filenames for files of this output format.
     * @return extension string, with leading dot, for example ".xml", or ".gpx"
     */
    public String getDefaultExtension();
}
