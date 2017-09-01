package org.wikivoyage.listings.input;

import static org.junit.Assert.fail;

import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.wikivoyage.listings.input.DumpDownloader;

public class DumpDownloaderTest {

	/**
	 * Test if the partial dump is detected correctly
	 */
	@Test
	public void isNotPartialDump() throws Exception {
		String dumpStatus="", partialDumpStatus="";
    		dumpStatus = IOUtils.toString(
            this.getClass().getResourceAsStream("/dumpstatus.json"), "UTF-8" 
            );
    		partialDumpStatus = IOUtils.toString(
            this.getClass().getResourceAsStream("/partial_dumpstatus.json"), "UTF-8"
            );
    		DumpDownloader downloader = new DumpDownloader();
    		Assert.assertFalse(downloader.isPartialDump(dumpStatus));
    		Assert.assertTrue(downloader.isPartialDump(partialDumpStatus));
	}

}
