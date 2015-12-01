package org.wikivoyage.listings.output;

import org.apache.commons.io.IOUtils;
import org.wikivoyage.listings.entity.WikivoyagePOI;
import org.wikivoyage.listings.validators.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;

public class ValidationReport implements OutputFormat {
    @Override
    public void write(Iterable<WikivoyagePOI> pois, String outputFilename) throws WriteOutputException {
        Validator [] validators = {
            new LatitudeValidator(),
            new LongitudeValidator(),
            new WebsiteURLValidator(),
            new EmailValidator()
        };

        try {
            StringBuilder rows = new StringBuilder();
            for (WikivoyagePOI poi: pois) {
                for (Validator validator: validators) {
                    String errorMessage = validator.validate(poi);
                    if (errorMessage != null) {
                        String row = "<tr><td><a href='http://" + poi.getLanguage() + ".wikivoyage.org/wiki/" +
                                URLEncoder.encode(poi.getArticle().replace(" ", "_"), "UTF-8") + "'>"
                                + poi.getArticle() + "</a></td><td>" + poi.getTitle() + "</td>" +
                                "<td>" + errorMessage + "</td></tr>\n";
                        rows.append(row);
                    }
                }
            }

            String template = IOUtils.toString(
                this.getClass().getResourceAsStream("/validation-report-template.htm"), "UTF-8"
            );

            BufferedWriter writer = null;

            try {
                FileWriter fwriter = new FileWriter(outputFilename);
                writer = new BufferedWriter(fwriter);
                writer.write(template.replace("{rows}", rows.toString()));
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        } catch (IOException e) {
            throw new WriteOutputException();
        }
    }

    @Override
    public String getDefaultExtension() {
        return ".validation-report.html";
    }
}