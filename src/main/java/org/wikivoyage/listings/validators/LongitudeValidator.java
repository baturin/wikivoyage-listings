package org.wikivoyage.listings.validators;

import org.wikivoyage.listings.entity.WikivoyagePOI;

public class LongitudeValidator implements Validator {
    @Override
    public String validate(WikivoyagePOI poi) {
        if (poi.getLongitude() != null && !poi.getLongitude().equals("")) {
            try {
                Float.parseFloat(poi.getLongitude());
            } catch (NumberFormatException e) {
                return "Malformed longitude '" + poi.getLongitude() + "'";
            }
        }
        return null;
    }
}
