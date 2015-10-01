package org.wikivoyage.ru.listings.output;

import org.wikivoyage.ru.listings.entity.WikivoyagePOI;


public interface OutputFormat {
    public void write(WikivoyagePOI[] pois, String outputFilename) throws WriteOutputException;
    public String getDefaultExtension();
}
