package org.wikivoyage.ru.listings.input;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.wikivoyage.ru.listings.entity.DumpArticle;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterate over articles in Wikivoyage database dump
 */
public class DumpArticlesIterator implements Iterator<DumpArticle> {
    XMLStreamReader reader;
    DumpArticle currentArticle;

    public DumpArticlesIterator(String filename) throws DumpReadException
    {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        InputStream stream;
        try {
            stream = getFileInputStream(filename);
        } catch (IOException e) {
            throw new DumpReadException("Failed to read Wikivoyage dump file", e);
        }
        try {
            reader = factory.createXMLStreamReader(stream);
        } catch (XMLStreamException e) {
            throw  new DumpReadException("Failed to create XML stream for Wikivoyage dump", e);
        }
        getNext();
    }

    @Override
    public boolean hasNext() {
        return currentArticle != null;
    }

    @Override
    public DumpArticle next() {
        if (currentArticle == null) {
            // no more articles in the dump
            throw new NoSuchElementException();
        }
        DumpArticle result = currentArticle;
        currentArticle = null;
        getNext();
        return result;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /**
     * Read the next article from the dump, put it to reader variable.
     */
    private void getNext() throws DumpReadException
    {
        String pageTitle = "";
        String pageText = "";
        String pageNs = "";
        StringBuffer curCharValue = new StringBuffer(1024);

        try {
            try {
                int event = reader.getEventType();
                while (true) {
                    switch (event) {
                        case XMLStreamConstants.START_ELEMENT:
                            String tagName = reader.getLocalName();
                            if (tagName.equals("page")) {
                                pageTitle = null;
                                pageText = null;
                                pageNs = null;
                            } else if (tagName.equals("title") || tagName.equals("text") || tagName.equals("ns")) {
                                curCharValue = new StringBuffer(1024);
                            }
                            break;
                        case XMLStreamConstants.CHARACTERS:
                            curCharValue.append(reader.getText());
                            break;
                        case XMLStreamConstants.END_ELEMENT:
                            if (reader.getLocalName().equals("title")) {
                                pageTitle = curCharValue.toString();
                            } else if (reader.getLocalName().equals("text")) {
                                pageText = curCharValue.toString();
                            } else if (reader.getLocalName().equals("ns")) {
                                pageNs = curCharValue.toString();
                            } else if (reader.getLocalName().equals("page")) {
                                if (pageNs != null && pageNs.equals("0")) {
                                    currentArticle = new DumpArticle(pageTitle, pageText);
                                    return;
                                }
                            }
                            break;
                        case XMLStreamConstants.END_DOCUMENT:
                            break;
                    }

                    if (!reader.hasNext())
                        break;

                    event = reader.next();
                }
            } finally {
                reader.close();
            }
        } catch (XMLStreamException e) {
            throw new DumpReadException(
                "Failed to get article in Wikivoyage dump: error when reading XML", e
            );
        }
    }

    /**
     * Get input stream for specified filename, considering that it could be plain XML,
     * or XML packed with BZIP
     */
    private InputStream getFileInputStream(String filename) throws IOException {
        if (filename.endsWith(".bz2")) {
            return new BufferedInputStream(
                new BZip2CompressorInputStream(
                    new BufferedInputStream(
                            new FileInputStream(filename)
                    ),
                    true // support for multistream bzip2
                )
            );
        } else {
            return new FileInputStream(new File(filename));
        }
    }
}
