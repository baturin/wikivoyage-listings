package org.wikivoyage.listings.validators;

import org.wikivoyage.listings.entity.WikivoyagePOI;

public interface Validator {
    String validate(WikivoyagePOI poi);
}
