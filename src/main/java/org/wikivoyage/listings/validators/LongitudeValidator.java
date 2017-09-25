package org.wikivoyage.listings.validators;

import org.wikivoyage.listings.entity.Listing;

public class LongitudeValidator implements Validator {
    private final int MAX_LONGITUDE = 180;
    private final String ERROR_PREFIX = "Malformed longitude";

    @Override
    public String validate(Listing poi) {

        String errorMessage = String.format("%s '%s'", ERROR_PREFIX, poi.getLongitude());

        if (poi.getLongitude() != null && !poi.getLongitude().equals("")) {
            try {
                Float coordinate = Float.parseFloat(poi.getLongitude());
                if (Math.abs(coordinate) > MAX_LONGITUDE) {
                    return errorMessage;
                }

            } catch (NumberFormatException e) {
                return errorMessage;
            }
        }
        return null;
    }

    @Override
    public String getIssueType() {
        return "Longitude";
    }
}
