package org.wikivoyage.listings.validators;

import org.apache.commons.validator.routines.UrlValidator;
import org.wikivoyage.listings.entity.Listing;

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
    
    private boolean validWebsiteURL(String url) {
        return UrlValidator.getInstance().isValid(url) &&
            ( url.startsWith("http://") || url.startsWith("https://"));
    }
}
