package org.wikivoyage.ru.listings.input;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class DumpDownloader {
    public static void downloadDump(String dumpUrl, String dumpFilename) throws IOException {
        System.out.println("Downloading dump...");
        URL website = new URL(dumpUrl);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream(dumpFilename);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    }
}
