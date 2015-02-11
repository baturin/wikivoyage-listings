package org.wikivoyage.ru.listings.input;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;

public class DumpDownloader {
    public void downloadDumpFromUrl(String dumpUrl, String dumpFilename) throws IOException {
        System.out.println("Downloading dump...");
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
}
