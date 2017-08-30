package org.wikivoyage.listings.input;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.wikivoyage.listings.input.DumpDownloader;

public class DumpDownloaderTest {

	/**
	 * Test if the partial dump is detected correctly
	 */
	@Test
	public void isPartialDump() {
		String dumpStatus="";
		InputStream in=null;
	    try {
	    		dumpStatus = IOUtils.toString(
	                this.getClass().getResourceAsStream("/dumpstatus.json"), "UTF-8"
	            );
	    		DumpDownloader downloader = new DumpDownloader();
	    		Assert.assertFalse(downloader.isPartialDump(dumpStatus));
		} catch (IOException e) {
			e.printStackTrace();
			fail("Donwloader isPartiaDump not working correctly ");
		} finally {
	        IOUtils.closeQuietly(in);
	    }
	}

}
