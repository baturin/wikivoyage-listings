package org.wikivoyage.listings.validators;

import org.apache.commons.validator.routines.UrlValidator;
import org.wikivoyage.listings.entity.Listing;

import java.net.*;

public class WebsiteURLValidator implements Validator {
    @Override
    public String validate(Listing poi) {
        if (poi.getUrl() != null && !poi.getUrl().equals("")) {
            if (!validWebsiteURL(poi.getUrl())) {
                return "Invalid URL '" + poi.getUrl() + "'";
            }
        }
        return null;
    }

    @Override
    public String getIssueType() {
        return "Website URL";
    }

    private boolean validWebsiteURL(String urlString) {
        try {
            UrlValidator validator = new UrlValidator(new String[] {"https", "http"});
            URL url = new URL(urlString);
            URI uri = new URI(url.getProtocol()
                    , url.getUserInfo()
                    , IDN.toASCII(url.getHost())
                    , url.getPort()
                    , url.getPath()
                    , url.getQuery()
                    , url.getRef()
            );

            return validator.isValid(uri.toASCIIString());

        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }
}