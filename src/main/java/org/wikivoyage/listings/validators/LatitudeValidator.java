package org.wikivoyage.listings.validators;

import org.wikivoyage.listings.entity.WikivoyagePOI;

public class LatitudeValidator implements Validator {
    @Override
    public String validate(WikivoyagePOI poi) {
        if (poi.getLatitude() != null && !poi.getLatitude().equals("")) {
            try {
                Float.parseFloat(poi.getLatitude());
            } catch (NumberFormatException e) {
                return "Malformed latitude '" + poi.getLatitude() + "'";
            }
        }
        return null;
    }
}
