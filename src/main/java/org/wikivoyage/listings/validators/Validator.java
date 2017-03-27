package org.wikivoyage.listings.validators;

import org.wikivoyage.listings.entity.Listing;

public interface Validator {
    
    /**
     * Validate the POI
     * @return null if valid, an explanation of the problem if not valid.
     */
    String validate(Listing poi);
    
    /**
     * Returns the type of issue, for instance "E-mail".
     */
    String getIssueType();
}
