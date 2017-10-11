package org.wikivoyage.listings.validators;

import java.util.Map;

import org.wikivoyage.listings.entity.Listing;

public interface BulkValidator {    
    /**
     * Add POI to BulkValidator
     */
    void add(Listing poi);
    
    /**
     * Validate POIs added to BulkValidator
     *
     * @return a Map of each invalid POI to it's error message
     */
    Map<Listing, String> validate();
    
    /**
     * Returns the type of issue, for instance "E-mail".
     */
    String getIssueType();
}
