package org.wikivoyage.listings.input;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.wikivoyage.listings.entity.Article;

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
public class DumpArticlesIterator implements Iterator<Article> {
    private XMLStreamReader reader;
    private Article currentArticle;
    private String languageCode;

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
        // first, read language code
        readLanguageCode();
        // then, try to read the first article
        getNext();
    }

    @Override
    public boolean hasNext() {
        return currentArticle != null;
    }

    @Override
    public Article next() {
        if (currentArticle == null) {
            // no more articles in the dump
            throw new NoSuchElementException();
        }
        Article result = currentArticle;
        currentArticle = null;
        getNext();
        return result;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /**
     * Get language code of the dump. If language code was not detected, return empty string.
     */
    public String getLanguageCode()
    {
        return languageCode;
    }

    /**
     * Read language code from the dump, put it to languageCode variable.
     *
     * Important: this function must be called before any of "getNext" function calls,
     * because data about language always precedes articles .
     */
    private void readLanguageCode() throws DumpReadException {
        boolean inSiteInfo = false;
        boolean inDbName = false;
        StringBuilder dbNameBuffer = new StringBuilder(128);

        // by default, consider empty string
        languageCode = "";

        try {
            int event = reader.getEventType();

            while (true) {
                String tagName;

                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        tagName = reader.getLocalName();
                        if (tagName.equals("siteinfo")) {
                            inSiteInfo = true;
                        } else if (tagName.equals("dbname") && inSiteInfo) {
                            inDbName = true;
                        }
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        if (inDbName) {
                            dbNameBuffer.append(reader.getText());
                        }
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        tagName = reader.getLocalName();
                        if (tagName.equals("siteinfo")) {
                            inSiteInfo = false;
                        } else if (tagName.equals("dbname")) {
                            languageCode = dbNameBuffer.substring(0, 2);
                            return;
                        }
                        break;
                }

                if (!reader.hasNext()) {
                    break;
                }

                event = reader.next();
            }
        } catch (XMLStreamException e) {
            throw new DumpReadException(
                "Failed to read language code from Wikivoyage dump: error when reading XML", e
            );
        }
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
                                    currentArticle = new Article(pageTitle, pageText);
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
