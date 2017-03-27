package org.wikivoyage.listings.input;

import org.wikivoyage.listings.entity.Listing;

import java.util.Iterator;

public class JavaSerializedIterable implements Iterable<Listing> {
    String filename;

    public JavaSerializedIterable(String filename)
    {
        this.filename = filename;
    }

    public Iterator<Listing> iterator() {
        try {
            return new JavaSerializedIterator(this.filename);
        } catch (Exception e) {
            throw new DumpReadException("Failed to read Java serialized file with Wikivoyage POIs", e);
        }
    }
}
