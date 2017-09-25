package org.wikivoyage.listings.validators;

import org.wikivoyage.listings.entity.Listing;

public class LatitudeValidator implements Validator {
    private final int MAX_LATITUDE = 90;
    private final String ERROR_PREFIX = "Malformed latitude";

    @Override
    public String validate(Listing poi) {

        String errorMessage = String.format("%s '%s'", ERROR_PREFIX, poi.getLatitude());

        if (poi.getLatitude() != null && !poi.getLatitude().equals("")) {
            try {
                Float coordinate = Float.parseFloat(poi.getLatitude());
                if (Math.abs(coordinate) > MAX_LATITUDE) {
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
        return "Latitude";
    }
}
