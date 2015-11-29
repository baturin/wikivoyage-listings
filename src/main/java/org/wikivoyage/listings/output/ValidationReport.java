package org.wikivoyage.listings.output;

import org.wikivoyage.listings.entity.WikivoyagePOI;
import org.wikivoyage.listings.validators.LatitudeValidator;
import org.wikivoyage.listings.validators.LongitudeValidator;
import org.wikivoyage.listings.validators.Validator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;

public class ValidationReport implements OutputFormat {
    @Override
    public void write(Iterable<WikivoyagePOI> pois, String outputFilename) throws WriteOutputException {
        Validator [] validators = {
            new LatitudeValidator(),
            new LongitudeValidator()
        };

        try {
            BufferedWriter writer = null;

            try {
                FileWriter fwriter = new FileWriter(outputFilename);
                writer = new BufferedWriter(fwriter);


                writer.write("<!DOCTYPE html>\n");
                writer.write("<html>\n");
                writer.write("<head>\n");
                writer.write("<meta charset=\"utf-8\">");
                writer.write("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css\" integrity=\"sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7\" crossorigin=\"anonymous\">\n");
                writer.write("<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css\" integrity=\"sha384-fLW2N01lMqjakBkx3l/M9EahuwpSfeNvV63J5ezn3uZzapT0u7EYsXMjQV+0En5r\" crossorigin=\"anonymous\">\n");
                writer.write("<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js\" integrity=\"sha384-0mSbJDEHialfmuBBQP6A4Qrprq5OVfW37PRR3j5ELqxss1yVqOtnepnHVP9aJ7xS\" crossorigin=\"anonymous\"></script>\n");
                writer.write("<title>Wikivoyage validation report</title>");
                writer.write("</head>\n");
                writer.write("<body>\n");
                writer.write("<div class=\"container-fluid\">\n");
                writer.write("<table class=\"table\">\n");
                writer.write("<tr><th>Article</th><th>Listing</th><th>Issue</th></tr>\n");
                for (WikivoyagePOI poi: pois) {
                    for (Validator validator: validators) {
                        String errorMessage = validator.validate(poi);
                        if (errorMessage != null) {
                            writer.write(
                                "<tr><td><a href='http://" + poi.getLanguage() + ".wikivoyage.org/wiki/" +
                                URLEncoder.encode(poi.getArticle().replace(" ", "_"), "UTF-8") + "'>"
                                + poi.getArticle() + "</a></td><td>" + poi.getTitle() + "</td>" +
                                "<td>" + errorMessage + "</td></tr>"
                            );
                        }
                    }
                }
                writer.write("</table>\n");
                writer.write("</div>\n");
                writer.write("</body>\n");
                writer.write("</html>\n");
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
        return "validation-report.html";
    }
}