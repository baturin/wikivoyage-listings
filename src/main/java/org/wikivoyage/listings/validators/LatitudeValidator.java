package org.wikivoyage.listings.validators;

import org.wikivoyage.listings.entity.Listing;

public class LatitudeValidator implements Validator {
    private final int MAX_LATITUDE = 90;
    private final String ERROR_PREFIX = "Malformed latitude";
    private final String COORD_REGEX = "^(((\\d+(\\.\\d+)?)[^0-9.]*){3}[NESW])$";

    @Override
    public String validate(Listing poi) {
        boolean isValid = true;

        if (poi.getLatitude() != null && !poi.getLatitude().equals("")) {
            try {
                Float coordinate = Float.parseFloat(poi.getLatitude());
                if (Math.abs(coordinate) > MAX_LATITUDE) {
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                if (!poi.getLatitude().matches(COORD_REGEX)) {
                    isValid = false;
                }
            }
        }

        return isValid ? null : String.format("%s '%s'", ERROR_PREFIX, poi.getLatitude());
    }

    @Override
    public String getIssueType() {
        return "Latitude";
    }
}
