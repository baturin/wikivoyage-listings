package org.wikivoyage.listings.output;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.wikivoyage.listings.entity.WikivoyagePOI;
import org.wikivoyage.listings.validators.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ValidationReport implements OutputFormat {
    @Override
    public void write(Iterable<WikivoyagePOI> pois, String outputFilename, String dumpDate) throws WriteOutputException {
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
                        String row = (
                            "{" +
                                "'language': '"  + StringEscapeUtils.escapeJavaScript(poi.getLanguage())  +  "', " +
                                "'article': '"  + StringEscapeUtils.escapeJavaScript(poi.getArticle())  +  "', " +
                                "'listing': '" + StringEscapeUtils.escapeJavaScript(poi.getTitle())+ "', " +
                                "'issue': '" + StringEscapeUtils.escapeJavaScript(errorMessage) + "', " +
                                "'issueType': '" + StringEscapeUtils.escapeJavaScript(validator.getIssueType()) + "'" +
                            "},\n"
                        );
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
                
                // Replace variables in template.
                template = template.replace("{rows}", rows.toString());
                template = template.replaceAll("\\{dumpDate\\}", dumpDate);
                
                writer.write(template);
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