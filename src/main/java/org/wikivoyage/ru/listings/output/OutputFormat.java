package org.wikivoyage.ru.listings.output;

import org.wikivoyage.ru.listings.entity.WikivoyagePOI;

import java.io.IOException;

public interface OutputFormat {
    public void write(WikivoyagePOI[] pois, String outputFilename) throws IOException;
}
