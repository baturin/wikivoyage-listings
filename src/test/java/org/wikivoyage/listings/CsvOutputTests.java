package org.wikivoyage.listings;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.wikivoyage.listings.output.CSV;
import org.wikivoyage.listings.entity.Listing;

public class CsvOutputTests extends OutputTests {

	/**
	 * Test CSV generation of the sample
	 */
	@Test
	public void Generate() throws Exception {

		List<Listing> pois = new ArrayList<Listing>();
		pois.add(getSample());
		String output = File.createTempFile("wikivoyage-listings-unit-tests", ".tmp").getAbsolutePath();
		
		new CSV().write(pois, output, "20160720");
		
		compareWithSample(output, "sample-result.csv");
	}
	
	/**
	 * Compare a generated file with the expected result from the resources folder.
	 */
	public void compareWithSample(String file, String sample) throws IOException {
		byte[] f1 = Files.readAllBytes(Paths.get(file));
		byte[] f2 = Files.readAllBytes(Paths.get(this.getClass().getResource("/" + sample).getPath()));
		
		if ( ! Arrays.equals(f1, f2)) {
			fail("Files not equal");
		}
	}

}
