package org.wikivoyage.listings.output;

import org.apache.commons.lang.StringEscapeUtils;
import org.wikivoyage.listings.entity.WikivoyagePOI;

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
    public void write(Iterable<WikivoyagePOI> pois, String outputFilename, String dumpDate) throws WriteOutputException {
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
                            "title VARCHAR(128), " +
                            "language VARCHAR(2), " +
                            "article VARCHAR(128), " +
                            "type VARCHAR(64), " +
                            "wikidata VARCHAR(64), " +
                            "wikipedia VARCHAR(64), " +
                            "description VARCHAR(4096), " +
                            "latitude DECIMAL(10, 8), " +
                            "longitude DECIMAL(11, 8), " +
                            "image VARCHAR(255)" +
                    ");\n"
                );

                for (WikivoyagePOI poi : pois) {
                    writer.write(
                        "INSERT INTO wikivoyage_listings (" +
                                "title, " +
                                "language, " +
                                "article, " +
                                "type, " +
                                "wikidata, " +
                                "wikipedia, " +
                                "description, " +
                                "latitude, " +
                                "longitude, " +
                                "image" +
                        ") " +
                        "VALUES (" +
                                escape(poi.getTitle()) + ", " +
                                escape(poi.getLanguage()) + ", " +
                                escape(poi.getArticle()) + ", " +
                                escape(poi.getType()) + ", " +
                                escape(poi.getWikidata()) + ", " +
                                escape(poi.getWikipedia()) + ", " +
                                escape(poi.getDescription())+ ", " +
                                getPositionalValue(poi.getLatitude(), 90.0) + ", " +
                                getPositionalValue(poi.getLongitude(), 180.0) + ", " +
                                escape(poi.getImage()) +
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
