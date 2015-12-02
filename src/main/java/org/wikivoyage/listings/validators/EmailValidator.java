package org.wikivoyage.listings.validators;

import org.wikivoyage.listings.entity.WikivoyagePOI;

public class EmailValidator implements Validator {
    @Override
    public String validate(WikivoyagePOI poi) {
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
