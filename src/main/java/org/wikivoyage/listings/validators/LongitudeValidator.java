package org.wikivoyage.listings.validators;

import org.wikivoyage.listings.entity.Listing;

public class LongitudeValidator extends SimpleValidator {
    @Override
    public void validate(Listing poi) {
        if (poi.getLongitude() != null && !poi.getLongitude().equals("")) {
            try {
                Float.parseFloat(poi.getLongitude());
            } catch (NumberFormatException e) {
                poi.add(ValidationIssue.INVALID_LONGITUDE);
            }
        }
    }
}
