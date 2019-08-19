package org.wikivoyage.listings.validators;

import org.wikivoyage.listings.entity.Listing;

public class LongitudeValidator extends SimpleValidator {
    String longitude = poi.getLongitude();
    @Override
    public void validate(Listing poi) {
        if (longitude!= null && !(longitude.equals(""))) {
            try {
                Float.parseFloat(longitude);
            } catch (NumberFormatException e) {
                poi.add(ValidationIssue.INVALID_LONGITUDE);
            }
        }
    }
}
