package org.wikivoyage.listings.validators;

import org.wikivoyage.listings.entity.Listing;

public class EmailValidator extends SimpleValidator {
    @Override
    public void validate(Listing poi) {
        String email = poi.getEmail();
        if (email!= null && !email.equals("")) {
            if (!org.apache.commons.validator.routines.EmailValidator.getInstance().isValid(email)) {
                poi.add(ValidationIssue.INVALID_EMAIL);
            }
        }
    }
}
