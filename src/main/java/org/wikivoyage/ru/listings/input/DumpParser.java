package org.wikivoyage.ru.listings.input;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;

/**
 * Parser of a whole Wikivoyage database dump
 */
public class DumpParser {
    public static void parseWikivoyageDump(String inputFilename, final PageParser pageParser)
            throws ParserConfigurationException, SAXException, IOException
    {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        DefaultHandler handler = new DefaultHandler(){
            String pageName = null;
            String text = null;
            String ns = null;
            private StringBuffer curCharValue = new StringBuffer(1024);

            public void startElement(String uri, String localName,String qName,
                                     Attributes attributes) throws SAXException {
                if (qName.equals("page")) {
                    pageName = null;
                    text = null;
                    ns = null;
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
                    pageName = curCharValue.toString();
                } else if (qName.equals("text")) {
                    text = curCharValue.toString();
                } else if (qName.equals("ns")) {
                    ns = curCharValue.toString();
                } else if (qName.equals("page")) {
                    if (ns.equals("0")) {
                        pageParser.processPage(text);
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
        saxParser.parse(in, handler);
    }
}
