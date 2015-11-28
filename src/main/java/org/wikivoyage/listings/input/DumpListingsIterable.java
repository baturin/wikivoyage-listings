package org.wikivoyage.listings.input;

import java.util.Iterator;

import org.wikivoyage.listings.entity.WikivoyagePOI;

import org.wikivoyage.listings.language.Language;

/**
 * Iterable for DumpListingsIterator
 */
public class DumpListingsIterable implements Iterable<WikivoyagePOI> {
    String filename;
    Language language;

    public DumpListingsIterable(String filename, Language language)
    {
        this.filename = filename;
        this.language = language;
    }

    public Iterator<WikivoyagePOI> iterator() {
        try {
            return new DumpListingsIterator(this.filename, language);
        } catch (Exception e) {
            throw new DumpReadException("Failed to create listings iterator for Wikivoyage dump", e);
        }
    }

}
