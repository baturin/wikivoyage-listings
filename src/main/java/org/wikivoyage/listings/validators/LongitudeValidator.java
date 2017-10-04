package org.wikivoyage.listings.validators;

import org.wikivoyage.listings.entity.Listing;

public class LongitudeValidator implements Validator {
    private final int MAX_LONGITUDE = 180;
    private final String ERROR_PREFIX = "Malformed longitude";
    private final String COORD_REGEX = "^(((\\d+(\\.\\d+)?)[^0-9.]*){3}[NESW])$";

    @Override
    public String validate(Listing poi) {
        boolean isValid = true;

        if (poi.getLongitude() != null && !poi.getLongitude().equals("")) {
            try {
                Float coordinate = Float.parseFloat(poi.getLongitude());
                if (Math.abs(coordinate) > MAX_LONGITUDE) {
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                if (!poi.getLongitude().matches(COORD_REGEX)) {
                    isValid = false;
                }
            }
        }

        return isValid ? null : String.format("%s '%s'", ERROR_PREFIX, poi.getLongitude());
    }

    @Override
    public String getIssueType() {
        return "Longitude";
    }
}
