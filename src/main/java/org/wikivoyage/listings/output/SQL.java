package org.wikivoyage.listings.output;

import org.apache.commons.lang.StringEscapeUtils;
import org.wikivoyage.listings.entity.Listing;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class SQL implements OutputFormat {
    private DecimalFormat positionalDataFormat;

    public SQL()
    {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.GERMAN);
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator('.');
        positionalDataFormat = new DecimalFormat("#.########", symbols);
    }

    @Override
    public void write(Iterable<Listing> pois, String outputFilename, String dumpDate) throws WriteOutputException {
        BufferedWriter writer = null;
        try {
            try {
                FileWriter fwriter = new FileWriter(outputFilename);
                writer = new BufferedWriter(fwriter);
                writer.write("-- Generated from " + dumpDate + " Wikivoyage data.\n");
                writer.write(
                	"PRAGMA synchronous=OFF;\n" + // These PRAGMAs make loading thousand times faster.
                    "PRAGMA count_changes=OFF;\n" +
                    "PRAGMA journal_mode=MEMORY;\n" +
                    "PRAGMA temp_store=MEMORY;\n" +
                    "DROP TABLE IF EXISTS wikivoyage_listings;\n"
                );
                writer.write(
                    "CREATE TABLE wikivoyage_listings (" +
                            "id INTEGER PRIMARY KEY, " +
                            "article VARCHAR(128), " +
                            "type VARCHAR(64), " +
                            "title VARCHAR(128), " +
                            "wikidata VARCHAR(64), " +
                            "wikipedia VARCHAR(64), " +
                            "alt VARCHAR(128), " +
                            "address VARCHAR(255), " +
                            "directions VARCHAR(255), " +
                            "phone VARCHAR(128), " +
                            "tollfree VARCHAR(128), " +
                            "email VARCHAR(128), " +
                            "fax VARCHAR(128), " +
                            "url VARCHAR(128), " +
                            "hours VARCHAR(128), " +
                            "checkin VARCHAR(128), " +
                            "checkout VARCHAR(128), " +
                            "image VARCHAR(255)," +
                            "price VARCHAR(128), " +
                            "latitude DECIMAL(10, 8), " +
                            "longitude DECIMAL(11, 8), " +
                            "wifi VARCHAR(128), " +
                            "accessibility VARCHAR(128), " +
                            "lastedit VARCHAR(128), " +
                            "description VARCHAR(4096), " +
                            "language VARCHAR(2)" +
                    ");\n"
                );

                for (Listing poi : pois) {
                    writer.write(
                        "INSERT INTO wikivoyage_listings (" +
                                "article, " +
                                "type, " +
                                "title, " +
                                "wikidata, " +
                                "wikipedia, " +
                                "alt, " +
                                "address, " +
                                "directions, " +
                                "phone, " +
                                "tollfree, " +
                                "email, " +
                                "fax, " +
                                "url, " +
                                "hours, " +
                                "checkin, " +
                                "checkout, " +
                                "image, " +
                                "price, " +
                                "latitude, " +
                                "longitude, " +
                                "wifi, " +
                                "accessibility, " +
                                "lastedit, " +
                                "description, " +
                                "language" +
                        ") " +
                        "VALUES (" +
                                escape(poi.getArticle()) + ", " +
                                escape(poi.getType()) + ", " +
                                escape(poi.getTitle()) + ", " +
                                escape(poi.getWikidata()) + ", " +
                                escape(poi.getWikipedia()) + ", " +
                                escape(poi.getAlt()) + ", " +
                                escape(poi.getAddress()) + ", " +
                                escape(poi.getDirections()) + ", " +
                                escape(poi.getPhone()) + ", " +
                                escape(poi.getTollFree()) + ", " +
                                escape(poi.getEmail()) + ", " +
                                escape(poi.getFax()) + ", " +
                                escape(poi.getUrl()) + ", " +
                                escape(poi.getHours()) + ", " +
                                escape(poi.getCheckIn()) + ", " +
                                escape(poi.getCheckOut()) + ", " +
                                escape(poi.getImage()) + ", " +
                                escape(poi.getPrice()) + ", " +
                                getPositionalValue(poi.getLatitude(), 90.0) + ", " +
                                getPositionalValue(poi.getLongitude(), 180.0) + ", " +
                                escape(poi.getWifi()) + ", " +
                                escape(poi.getAccessibility()) + ", " +
                                escape(poi.getLastEdit()) + ", " +
                                escape(poi.getDescription())+ ", " +
                                escape(poi.getLanguage()) +
                        ");\n");
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

    private String getPositionalValue(String rawValue, double boundary)
    {
        if (rawValue == null || rawValue.equals("")) {
            return "NULL";
        }

        try {
            Double doubleValue = Double.parseDouble(rawValue);
            if (doubleValue >= -boundary && doubleValue <= boundary) {
                return escape(positionalDataFormat.format(doubleValue));
            } else {
                return "NULL";
            }
        } catch (NumberFormatException e) {
            return "NULL";
        }
    }

    private String escape(String value)
    {
        return "'" + StringEscapeUtils.escapeSql(value) + "'";
    }

    @Override
    public String getDefaultExtension() {
        return ".sql";
    }
}
