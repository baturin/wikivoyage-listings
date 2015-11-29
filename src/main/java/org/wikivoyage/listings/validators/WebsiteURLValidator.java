package org.wikivoyage.listings.validators;

import org.apache.commons.validator.routines.UrlValidator;
import org.wikivoyage.listings.entity.WikivoyagePOI;

public class WebsiteURLValidator implements Validator {
    @Override
    public String validate(WikivoyagePOI poi) {
        if (poi.getUrl() != null && !poi.getUrl().equals("")) {
            if (!UrlValidator.getInstance().isValid(poi.getUrl())) {
                return "Invalid URL '" + poi.getUrl() + "'";
            }
        }
        return null;
    }
}
