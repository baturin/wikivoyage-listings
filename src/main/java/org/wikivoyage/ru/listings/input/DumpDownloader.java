package org.wikivoyage.ru.listings.input;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DumpDownloader {
    private static final Log log = LogFactory.getLog(DumpDownloader.class);

    public void downloadDumpFromUrl(String dumpUrl, String dumpFilename) throws IOException {
        log.info("Download dump from '" + dumpUrl + "'");
        URL website = new URL(dumpUrl);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream(dumpFilename);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    }

    public void downloadLanguageDump(String language, String dumpFilename) throws IOException
    {
        HashMap<String, String> dumpUrls = new HashMap<String, String>();
        dumpUrls.put("ru", "https://dumps.wikimedia.org/ruwikivoyage/latest/ruwikivoyage-latest-pages-articles.xml.bz2");
        dumpUrls.put("en", "https://dumps.wikimedia.org/enwikivoyage/latest/enwikivoyage-latest-pages-articles.xml.bz2");

        downloadDumpFromUrl(dumpUrls.get(language), dumpFilename);
    }

    public List<String> listDumps(String language) throws IOException
    {
        List<String> availableDumps = new LinkedList<String>();
        String indexUrl = "https://dumps.wikimedia.org/" + language + "wikivoyage/";
        InputStream in = new URL(indexUrl).openStream();

        try {
            String indexHtml = IOUtils.toString(in);
            Pattern p = Pattern.compile("a\\s*href\\s*=\\s*\"(\\d+)/\"");
            Matcher m = p.matcher(indexHtml);
            while (m.find()) {
                availableDumps.add(m.group(1));
            }
        } finally {
            IOUtils.closeQuietly(in);
        }

        return availableDumps;
    }

    public String dumpUrl(String language, String dumpId)
    {
        return (
            "https://dumps.wikimedia.org/" + language + "wikivoyage/" +
                    dumpId + "/" + language + "wikivoyage-" + dumpId + "-pages-articles.xml.bz2"
        );
    }
}
