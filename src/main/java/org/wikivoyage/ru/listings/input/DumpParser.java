package org.wikivoyage.ru.listings.input;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Parser of a whole Wikivoyage database dump
 */
public class DumpParser {
    private static final Log log = LogFactory.getLog(DumpParser.class);

    public static void parseWikivoyageDump(String inputFilename, final PageParser pageParser)
            throws ParserConfigurationException, SAXException, IOException
    {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        SAXParser wikivoyageDumpSaxParser = saxParserFactory.newSAXParser();

        DefaultHandler handler = new DefaultHandler(){
            String pageTitle = null;
            String pageText = null;
            String pageNs = null;
            private StringBuffer curCharValue = new StringBuffer(1024);

            public void startElement(String uri, String localName,String qName,
                                     Attributes attributes) throws SAXException {
                if (qName.equals("page")) {
                    pageTitle = null;
                    pageText = null;
                    pageNs = null;
                } else if (qName.equals("title") || qName.equals("text") || qName.equals("ns")) {
                    curCharValue = new StringBuffer(1024);
                }
            }

            @Override
            public void characters (char ch[], int start, int length) throws SAXException
            {
                curCharValue.append(ch, start, length);
            }

            public void endElement(String uri, String localName,
                                   String qName) throws SAXException {

                if (qName.equals("title")) {
                    pageTitle = curCharValue.toString();
                } else if (qName.equals("text")) {
                    pageText = curCharValue.toString();
                } else if (qName.equals("ns")) {
                    pageNs = curCharValue.toString();
                } else if (qName.equals("page")) {
                    if (pageNs.equals("0")) {
                        log.debug("Process page '" + pageTitle + "'");
                        pageParser.processPage(pageText);
                    }
                }

            }
        };

        InputStream in;

        if (inputFilename.endsWith(".bz2")) {
            in = new BufferedInputStream(
                    new BZip2CompressorInputStream(
                            new BufferedInputStream(
                                    new FileInputStream(inputFilename)
                            ),
                            true // support for multistream bzip2
                    )
            );
        } else {
            in = new FileInputStream(new File(inputFilename));
        }
        wikivoyageDumpSaxParser.parse(in, handler);
    }
}
