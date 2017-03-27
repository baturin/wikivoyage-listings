package org.wikivoyage.listings.output;

import org.apache.commons.lang.StringEscapeUtils;
import org.wikivoyage.listings.entity.Listing;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Output Wikivoyage POIs as a Comma-Separated Values file.
 * https://en.wikipedia.org/wiki/Comma-separated_values
 * With a header, values in double quotes if needed, inner double quotes paired, commas between values, and UNIX line endings.
 */
public class CSV implements OutputFormat {

	/**
	 * Hard-coded CSV keywords.
	 * If needed these may be replaced with tabs, semicolons, Windows new lines, etc.
	 */
	public final static String SEPARATOR = ",";
	public final static String NEW_LINE = "\n";
	
    public void write(Iterable<Listing> pois, String outputFilename, String dumpDate) throws WriteOutputException
    {
        BufferedWriter writer = null;
        try {
            try {
                FileWriter fwriter = new FileWriter(outputFilename);
                writer = new BufferedWriter(fwriter);

                // Write the CSV header.
                writer.write(
                		"article"+ SEPARATOR +
                		"type" + SEPARATOR +
                		"title" + SEPARATOR +
                		"alt" + SEPARATOR +
                		"wikidata" + SEPARATOR +
                		"wikipedia" + SEPARATOR +
                		"address" + SEPARATOR +
                		"directions" + SEPARATOR +
                		"phone" + SEPARATOR +
                		"tollFree" + SEPARATOR +
                		"email" + SEPARATOR +
                		"fax" + SEPARATOR +
                		"url" + SEPARATOR +
                		"hours" + SEPARATOR +
                		"checkIn" + SEPARATOR +
                		"checkOut" + SEPARATOR +
                		"image" + SEPARATOR +
                		"price" + SEPARATOR +
                		"latitude" + SEPARATOR +
                		"longitude" + SEPARATOR +
                		"wifi" + SEPARATOR +
                		"accessibility" + SEPARATOR +
                		"lastEdit" + SEPARATOR +
                		"description" + NEW_LINE);
                
                // Write each POI.
                for (Listing poi : pois) {
                    writer.write(foolproof(poi.getArticle()) + SEPARATOR);
                    writer.write(foolproof(poi.getType()) + SEPARATOR);
                    writer.write(foolproof(poi.getTitle()) + SEPARATOR);
                    writer.write(foolproof(poi.getAlt()) + SEPARATOR);
                    writer.write(foolproof(poi.getWikidata()) + SEPARATOR);
                    writer.write(foolproof(poi.getWikipedia()) + SEPARATOR);
                    writer.write(foolproof(poi.getAddress()) + SEPARATOR);
                    writer.write(foolproof(poi.getDirections()) + SEPARATOR);
                    writer.write(foolproof(poi.getPhone()) + SEPARATOR);
                    writer.write(foolproof(poi.getTollFree()) + SEPARATOR);
                    writer.write(foolproof(poi.getEmail()) + SEPARATOR);
                    writer.write(foolproof(poi.getFax()) + SEPARATOR);
                    writer.write(foolproof(poi.getUrl()) + SEPARATOR);
                    writer.write(foolproof(poi.getHours()) + SEPARATOR);
                    writer.write(foolproof(poi.getCheckIn()) + SEPARATOR);
                    writer.write(foolproof(poi.getCheckOut()) + SEPARATOR);
                    writer.write(foolproof(poi.getImage()) + SEPARATOR);
                    writer.write(foolproof(poi.getPrice()) + SEPARATOR);
                    writer.write(foolproof(poi.getLatitude()) + SEPARATOR);
                    writer.write(foolproof(poi.getLongitude()) + SEPARATOR);
                    writer.write(foolproof(poi.getWifi()) + SEPARATOR);
                    writer.write(foolproof(poi.getAccessibility()) + SEPARATOR);
                    writer.write(foolproof(poi.getLastEdit()) + SEPARATOR);
                    writer.write(foolproof(poi.getDescription()) + NEW_LINE);
                }
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        } catch (IOException e) {
            throw new WriteOutputException();
        }
    }

    /**
     * Make any string usable as a CSV value.
     */
    private String foolproof(String str) {
    	if (str == null) {
    		return ""; // Values non present in Wikivoyage are replaced with empty strings.
    	}
    	else {
    		return StringEscapeUtils.escapeCsv( // Escape CSV-problematic characters.
    				str.replace('\n', ' ')); // Escape line ends.
    	}
    }
    
    public String getDefaultExtension()
    {
        return ".csv";
    }
}
