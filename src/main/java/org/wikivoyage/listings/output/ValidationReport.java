package org.wikivoyage.listings.output;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.wikivoyage.listings.entity.Listing;
import org.wikivoyage.listings.validators.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Generate an HTML report showing what syntax errors exist in the Wikivoyage data.
 */
public class ValidationReport implements OutputFormat {
    @Override
    public void write(Iterable<Listing> pois, String outputFilename, String dumpDate) throws WriteOutputException {
        Validator [] validators = {
            new LatitudeValidator(),
            new LongitudeValidator(),
            new WebsiteURLValidator(),
            new EmailValidator()
        };
        BulkValidator bulkValidator = new WikidataBulkValidator();

        try {
            StringBuilder rows = new StringBuilder();
            for (Listing poi: pois) {
                for (Validator validator: validators) {
                    String errorMessage = validator.validate(poi);
                    if (errorMessage != null) {
                        rows.append(createRow(poi, errorMessage, validator.getIssueType()));
                    }
                }
                bulkValidator.add(poi);
            }
            Map<Listing, String> bulkValidationResults = bulkValidator.validate();
            for (Entry<Listing, String> entry : bulkValidationResults.entrySet()) {
                rows.append(createRow(entry.getKey(), entry.getValue(), bulkValidator.getIssueType()));
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
    
    private String createRow(Listing poi, String issue, String issueType) {
        return
            "{" +
                "'language': '"  + StringEscapeUtils.escapeJavaScript(poi.getLanguage())  +  "', " +
                "'article': '"  + StringEscapeUtils.escapeJavaScript(poi.getArticle())  +  "', " +
                "'listing': '" + StringEscapeUtils.escapeJavaScript(poi.getTitle())+ "', " +
                "'issue': '" + StringEscapeUtils.escapeJavaScript(issue) + "', " +
                "'issueType': '" + StringEscapeUtils.escapeJavaScript(issueType) + "'" +
            "},\n";
    }
}