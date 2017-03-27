package org.wikivoyage.listings.input;

import org.wikivoyage.listings.entity.Listing;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Iterator;

public class JavaSerializedIterator implements Iterator<Listing>, Iterable<Listing> {
    private Listing poi;
    FileInputStream fio;

    public JavaSerializedIterator(String filename) throws IOException {
        fio = new FileInputStream(filename);
        getNext();
    }

    @Override
    public Iterator<Listing> iterator() {
        return null;
    }

    @Override
    public boolean hasNext() {
        return poi != null;
    }

    @Override
    public Listing next() {
        Listing currentPOI = poi;
        getNext();
        return currentPOI;
    }

    private void getNext()
    {
        try {
            ObjectInputStream ois = new ObjectInputStream(fio);
            poi = (Listing) ois.readObject();
        } catch (EOFException e) {
            poi = null;
            closeInputStream();
        } catch (Exception e) {
            closeInputStream();
            throw new JavaSerializedReadException("Failed to read next Wikivoyage POI from Java serialized file", e);
        }
    }

    private void closeInputStream()
    {
        try {
            fio.close();
        } catch (IOException e) {
            throw new JavaSerializedReadException("Failed to close Java serialized file", e);
        }
    }

    @Override
    public void remove() {
        throw  new UnsupportedOperationException();
    }
}