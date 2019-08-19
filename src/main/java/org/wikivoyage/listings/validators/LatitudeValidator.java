package org.wikivoyage.listings.validators;

import org.wikivoyage.listings.entity.Listing;

public class LatitudeValidator extends SimpleValidator {
    @Override
    public void validate(Listing poi) {
        String latitude = poi.getLatitude();
        if (latitude!= null && !(latitude.equals(""))) {
            try {
                Float.parseFloat(latitude);
            } catch (NumberFormatException e) {
                poi.add(ValidationIssue.INVALID_LATITUDE);
            }
        }
    }
}
