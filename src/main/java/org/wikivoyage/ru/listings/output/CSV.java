package org.wikivoyage.ru.listings.output;

import org.apache.commons.lang.StringEscapeUtils;
import org.wikivoyage.ru.listings.entity.WikivoyagePOI;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CSV implements OutputFormat {
    public void write(WikivoyagePOI[] pois, String outputFilename) throws WriteOutputException
    {
        BufferedWriter writer = null;
        try {
            try {
                FileWriter fwriter = new FileWriter(outputFilename);
                writer = new BufferedWriter(fwriter);

                for (WikivoyagePOI poi : pois) {
                    writer.write(StringEscapeUtils.escapeCsv(poi.getLatitude()));
                    writer.write(",");
                    writer.write(StringEscapeUtils.escapeCsv(poi.getLongitude()));
                    writer.write(",");
                    writer.write(StringEscapeUtils.escapeCsv(poi.getArticle()));
                    writer.write(",");
                    writer.write(StringEscapeUtils.escapeCsv(poi.getType()));
                    writer.write(",");
                    writer.write(StringEscapeUtils.escapeCsv(poi.getTitle().replace('\n', ' ')));
                    writer.write(",");
                    writer.write(StringEscapeUtils.escapeCsv(poi.getDescription().replace('\n', ' ')));
                    writer.write("\n");
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

    public String getDefaultExtension()
    {
        return ".csv";
    }
}
