package org.wikivoyage.listings.input;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;

public class DumpDownloader {
    private static final Log log = LogFactory.getLog(DumpDownloader.class);
    private static final String BASE_URL = "https://dumps.wikimedia.org/";

    private static final String DATETIME_FORMAT = "yyyyMMdd_HHmmss-SSS";

    private String getTempFileName(String fileName) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
        String timestamp = LocalDateTime.now().format(formatter);
        return fileName + "_" + timestamp;
    }

    private void renameFile(String oldFileName, String newfileName) throws IOException {
        Path target = Paths.get(newfileName);
        Path source = Paths.get(oldFileName);
        Files.move(source, target);
    }

    public void downloadDumpFromUrl(String dumpUrl, String dumpFilename) throws IOException {
        log.info("Download dump from '" + dumpUrl + "'");
        URL website = new URL(dumpUrl);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        String tempDumpFileName = getTempFileName(dumpFilename);
        FileOutputStream fos = new FileOutputStream(tempDumpFileName);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        renameFile(tempDumpFileName, dumpFilename);
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

        Collections.sort(availableDumps);
        Collections.reverse(availableDumps);
        
        /*
         * I check the JSON status file of the last dump, if the dump is "partial" (in-progress)
         * I discard it in favour of the previous one, if exist
         */
        if (availableDumps.size()>0){
        		String dumpStatusURL = indexUrl + availableDumps.get(0) + "/dumpstatus.json"; 
    			String dumpStatus = this.getDumpStatus(dumpStatusURL);
        		if (this.isPartialDump(dumpStatus)) {
        			availableDumps.remove(0);
        		}
        	}
        
        return availableDumps;
    }

    public String getDumpStatus(String dumpStatusURL) throws IOException {
    		String dumpStatus="";
    		InputStream in = new URL(dumpStatusURL).openStream();
        try {
        		dumpStatus = IOUtils.toString(in);
        } finally {
            IOUtils.closeQuietly(in);
        }
        return dumpStatus;
    }

    /**
     * Check whether a dump is partial (currently being generated and not complete) or not
     * Data dumps/Status format: https://meta.wikimedia.org/wiki/Data_dumps/Status_format
     * @param	content of dumpstatus.json     
     * @return	true if the dump is partial	 
     */
    public boolean isPartialDump(String dumpStatus) throws IOException {
        JSONObject dumpStatusJSON = new JSONObject(dumpStatus);
        boolean partialDump=true;
        if (dumpStatusJSON
        		.getJSONObject("jobs")
        		.getJSONObject("articlesmultistreamdump")
        		.getString("status")
        		.equals("done")) {
        		partialDump=false;
        }
        return partialDump;
	}

	public String dumpUrl(String language, String dumpId)
    {
        return (
            BASE_URL + language + "wikivoyage/" +
                dumpId + "/" + language + "wikivoyage-" + dumpId + "-pages-articles.xml.bz2"
        );
    }
}
