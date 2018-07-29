package org.wikivoyage.listings.validators;

import org.wikivoyage.listings.entity.Listing;

import java.util.Iterator;

/**
 * Abstract implementation of Validator for simple validators which
 * can validateModeInputs Listings one by one (ie. not in bulk).
 * <p>
 * Classes extending this one only need to implement {@link #validate(Listing)} method
 * and SimpleValidator will take care of supplying appropriate Iterable required by Validator.
 */
public abstract class SimpleValidator implements Validator {
    @Override
    public Iterable<Listing> validate(final Iterable<Listing> listingIterable) {
        return () -> new SimpleValidatorIterator(listingIterable.iterator());
    }

    protected abstract void validate(Listing listing);

    private class SimpleValidatorIterator implements Iterator<Listing> {
        private Iterator<Listing> listingIterator;

        public SimpleValidatorIterator(Iterator<Listing> listingIterator) {
            this.listingIterator = listingIterator;
        }

        @Override
        public boolean hasNext() {
            return listingIterator.hasNext();
        }

        @Override
        public Listing next() {
            Listing poi = listingIterator.next();
            validate(poi);
            return poi;
        }
    }
}
