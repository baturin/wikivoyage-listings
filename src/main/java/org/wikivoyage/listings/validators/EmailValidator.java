package org.wikivoyage.listings.validators;

import org.wikivoyage.listings.entity.Listing;

public class EmailValidator extends SimpleValidator {
    @Override
    public void validate(Listing poi) {
        if (poi.getEmail() != null && !poi.getEmail().equals("")) {
            if (!org.apache.commons.validator.routines.EmailValidator.getInstance().isValid(poi.getEmail())) {
                poi.add(ValidationIssue.INVALID_EMAIL);
            }
        }
    }
}
