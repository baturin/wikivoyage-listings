package org.wikivoyage.listings.validators;

import org.wikivoyage.listings.entity.WikivoyagePOI;

public interface Validator {
    
    /**
     * Validate the POI
     * @return null if valid, an explanation of the problem if not valid.
     */
    String validate(WikivoyagePOI poi);
    
    /**
     * Returns the type of issue, for instance "E-mail".
     */
    String getIssueType();
}
