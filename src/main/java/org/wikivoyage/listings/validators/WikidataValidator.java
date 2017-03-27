package org.wikivoyage.listings.validators;

import org.wikivoyage.listings.entity.Listing;

public class WikidataValidator implements Validator {
    @Override
    public String validate(Listing poi) {
        if (poi.getWikidata() != null && !poi.getWikidata().equals("")) {
            if ( ! validQID(poi.getWikidata())) {
                return "Invalid QID '" + poi.getWikidata() + "'";
            }
        }
        return null;
    }

    @Override
    public String getIssueType() {
        return "Wikidata QID";
    }
    
    private boolean validQID(String qid) {
        return qid.matches("Q\\d+");
    }
}
