package org.wikivoyage.listings.validators;

import org.wikivoyage.listings.entity.Listing;

public class LongitudeValidator extends SimpleValidator {
    @Override
    public void validate(Listing poi) {
        String longitude = poi.getLongitude();
        if (longitude!= null && !(longitude.equals(""))) {
            try {
                Float.parseFloat(longitude);
            } catch (NumberFormatException e) {
                poi.add(ValidationIssue.INVALID_LONGITUDE);
            }
        }
    }
}
