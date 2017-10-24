package org.wikivoyage.listings.validators;

import org.wikivoyage.listings.entity.Listing;

/**
 * Enumeration of possible validation issues
 */
public enum ValidationIssue {
    INVALID_EMAIL,
    INVALID_LATITUDE,
    INVALID_LONGITUDE,
    INVALID_URL,
    INVALID_WIKIDATA_QID,
    REDIRECT_WIKIDATA_QID;
    
    private String [] categories = {
        "E-mail",
        "Latitude",
        "Longitude",
        "Website URL",
        "Wikidata QID",
        "Wikidata QID"
    };
    
    /**
     * Get category for display purposes.
     */
    public String getCategory() {
        return categories[this.ordinal()];
    }
    
    /**
     * Get description for display purposes.
     */
    public String getDescription(Listing poi) {
        switch (this) {
        case INVALID_EMAIL:
            return String.format("Invalid e-mail '%s'", poi.rawEmail());
        case INVALID_LATITUDE:
            return String.format("Malformed latitude '%s'", poi.rawLatitude());
        case INVALID_LONGITUDE:
            return String.format("Malformed longitude '%s'", poi.rawLongitude());
        case INVALID_URL:
            return String.format("Invalid URL '%s'", poi.rawUrl());
        case INVALID_WIKIDATA_QID:
            return String.format("Invalid QID '%s'", poi.rawWikidata());
        case REDIRECT_WIKIDATA_QID:
            return String.format("Redirect QID '%s'", poi.rawWikidata());
        default:
            return null;
        }
    }
}
