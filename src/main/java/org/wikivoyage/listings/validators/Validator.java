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

    static Iterable<Listing> validateAll(Iterable<Listing> listingIterable) {
        Iterable<Listing> validatedIterable = listingIterable;
        Validator[] validators = {
                new LatitudeValidator(),
                new LongitudeValidator(),
                new WebsiteURLValidator(),
                new EmailValidator(),
                new WikidataValidator()
        };
        for (Validator validator : validators) {
            validatedIterable = validator.validate(validatedIterable);
        }
        return validatedIterable;
    }
}
