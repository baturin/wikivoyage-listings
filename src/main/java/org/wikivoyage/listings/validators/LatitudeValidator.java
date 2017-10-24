package org.wikivoyage.listings.validators;

import org.wikivoyage.listings.entity.Listing;

public class LatitudeValidator extends SimpleValidator {
    @Override
    public void validate(Listing poi) {
        if (poi.getLatitude() != null && !poi.getLatitude().equals("")) {
            try {
                Float.parseFloat(poi.getLatitude());
            } catch (NumberFormatException e) {
                poi.add(ValidationIssue.INVALID_LATITUDE);
            }
        }
    }
}
