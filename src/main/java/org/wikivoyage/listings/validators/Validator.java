package org.wikivoyage.listings.validators;

import org.wikivoyage.listings.entity.Listing;

public interface Validator {
    
    /**
     * Validates an Iterable of POIs. The validation will occur when the returned 
     * Iteratable is iterated over, so Validator acts as validating filter.
     *   
     * @param listingIterable an Iterable of Listings to be validated
     * @return an Iterable of validated POIs
     */
    Iterable<Listing> validate(Iterable<Listing> listingIterable);
}
