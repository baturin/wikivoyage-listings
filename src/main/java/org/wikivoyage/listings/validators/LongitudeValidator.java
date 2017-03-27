package org.wikivoyage.listings.validators;

import org.wikivoyage.listings.entity.Listing;

public class LongitudeValidator implements Validator {
    @Override
    public String validate(Listing poi) {
        if (poi.getLongitude() != null && !poi.getLongitude().equals("")) {
            try {
                Float.parseFloat(poi.getLongitude());
            } catch (NumberFormatException e) {
                return "Malformed longitude '" + poi.getLongitude() + "'";
            }
        }
        return null;
    }

    @Override
    public String getIssueType() {
        return "Longitude";
    }
}
