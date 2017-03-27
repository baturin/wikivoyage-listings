package org.wikivoyage.listings.validators;

import org.wikivoyage.listings.entity.Listing;

public class EmailValidator implements Validator {
    @Override
    public String validate(Listing poi) {
        if (poi.getEmail() != null && !poi.getEmail().equals("")) {
            if (!org.apache.commons.validator.routines.EmailValidator.getInstance().isValid(poi.getEmail())) {
                return "Invalid e-mail '" + poi.getEmail() + "'";
            }
        }
        return null;
    }

    @Override
    public String getIssueType() {
        return "E-mail";
    }
}
