package org.wikivoyage.ru.listings.output;

import org.apache.commons.lang.StringEscapeUtils;
import org.wikivoyage.ru.listings.entity.WikivoyagePOI;

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
                    "DROP TABlE IF EXISTS wikivoyage_pois;\n"
                );
                writer.write(
                    "CREATE TABLE wikivoyage_pois (" +
                            "title VARCHAR(128), " +
                            "article VARCHAR(128), " +
                            "type VARCHAR(64), " +
                            "description VARCHAR(4096), " +
                            "latitude DECIMAL(10, 8), " +
                            "longitude DECIMAL(10, 8)" +
                    ");\n"
                );

                for (WikivoyagePOI poi : pois) {
                    writer.write(
                        "INSERT INTO wikivoyage_pois (" +
                                "title, " +
                                "article, " +
                                "type, " +
                                "description, " +
                                "latitude, " +
                                "longitude" +
                        ") " +
                        "VALUES (" +
                                escape(poi.getTitle()) + ", " +
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
