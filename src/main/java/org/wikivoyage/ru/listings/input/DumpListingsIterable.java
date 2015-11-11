package org.wikivoyage.ru.listings.input;

import org.wikivoyage.ru.listings.entity.WikivoyagePOI;

import java.util.Iterator;

/**
 * Iterable for DumpListingsIterator
 */
public class DumpListingsIterable implements Iterable<WikivoyagePOI> {
    String filename;

    public DumpListingsIterable(String filename)
    {
        this.filename = filename;
    }

    public Iterator<WikivoyagePOI> iterator() {
        try {
            return new DumpListingsIterator(this.filename);
        } catch (Exception e) {
            throw new DumpReadException("Failed to create listings iterator for Wikivoyage dump", e);
        }
    }

}
