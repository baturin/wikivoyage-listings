package org.wikivoyage.listings.input;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DumpDownloader {
    private static final Log log = LogFactory.getLog(DumpDownloader.class);
    private static final String BASE_URL = "https://dumps.wikimedia.org/";

    public void downloadDumpFromUrl(String dumpUrl, String dumpFilename) throws IOException {
        log.info("Download dump from '" + dumpUrl + "'");
        URL website = new URL(dumpUrl);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream(dumpFilename);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
    }

    public List<String> listDumps(String language) throws IOException
    {
        List<String> availableDumps = new LinkedList<String>();
        String indexUrl = BASE_URL + language + "wikivoyage/";
        InputStream in = new URL(indexUrl).openStream();

        try {
            String indexHtml = IOUtils.toString(in);
            Pattern p = Pattern.compile("a\\s*href\\s*=\\s*\"(\\d+)/\"");
            Matcher m = p.matcher(indexHtml);
            while (m.find()) {
                String dumpId = m.group(1);
                availableDumps.add(dumpId);
                log.debug("Detected dump for language '" + language + "': " + dumpId);
            }
        } finally {
            IOUtils.closeQuietly(in);
        }

        return availableDumps;
    }

    public String dumpUrl(String language, String dumpId)
    {
        return (
            BASE_URL + language + "wikivoyage/" +
                dumpId + "/" + language + "wikivoyage-" + dumpId + "-pages-articles.xml.bz2"
        );
    }
}
