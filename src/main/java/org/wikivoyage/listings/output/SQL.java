package org.wikivoyage.listings.output;

import org.apache.commons.lang.StringEscapeUtils;
import org.wikivoyage.listings.entity.WikivoyagePOI;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SQL implements OutputFormat{

    @Override
    public void write(Iterable<WikivoyagePOI> pois, String outputFilename) throws WriteOutputException {
        BufferedWriter writer = null;
        try {
            try {
                FileWriter fwriter = new FileWriter(outputFilename);
                writer = new BufferedWriter(fwriter);
                writer.write(
                    "DROP TABLE IF EXISTS wikivoyage_listings;\n"
                );
                writer.write(
                    "CREATE TABLE wikivoyage_listings (" +
                            "title VARCHAR(128), " +
                            "language VARCHAR(2), " +
                            "article VARCHAR(128), " +
                            "type VARCHAR(64), " +
                            "description VARCHAR(4096), " +
                            "latitude DECIMAL(10, 8), " +
                            "longitude DECIMAL(10, 8)" +
                    ");\n"
                );

                for (WikivoyagePOI poi : pois) {
                    writer.write(
                        "INSERT INTO wikivoyage_listings (" +
                                "title, " +
                                "language, " +
                                "article, " +
                                "type, " +
                                "description, " +
                                "latitude, " +
                                "longitude" +
                        ") " +
                        "VALUES (" +
                                escape(poi.getTitle()) + ", " +
                                escape(poi.getLanguage()) + ", " +
                                escape(poi.getArticle()) + ", " +
                                escape(poi.getType()) + ", " +
                                escape(poi.getDescription())+ ", " +
                                escape(poi.getLatitude()) + ", " +
                                escape(poi.getLongitude()) +
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

    private String escape(String value)
    {
        return "'" + StringEscapeUtils.escapeSql(value) + "'";
    }

    @Override
    public String getDefaultExtension() {
        return ".sql";
    }
}
