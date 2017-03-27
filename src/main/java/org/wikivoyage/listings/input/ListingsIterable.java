package org.wikivoyage.listings.input;

import java.util.Iterator;

import org.wikivoyage.listings.entity.Listing;

/**
 * Iterate over Wikivoyage POIs taken from a particular dump file.
 */
public class ListingsIterable implements Iterable<Listing> {
    
    String filename;

    public ListingsIterable(String filename)
    {
        this.filename = filename;
    }

    public Iterator<Listing> iterator() {
        try {
            return new DumpListingsIterator(filename);
        } catch (Exception e) {
            throw new DumpReadException("Failed to create listings iterator for Wikivoyage dump", e);
        }
    }

}
