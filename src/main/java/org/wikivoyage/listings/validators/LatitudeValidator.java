package org.wikivoyage.listings.validators;

import org.apache.commons.lang.StringUtils;
import org.wikivoyage.listings.entity.Listing;

public class LatitudeValidator extends SimpleValidator {
	
	//0 at the equator; 90 at the respective poles
	private static final int MIN_LATITUDE = -90;
	private static final int MAX_LATITUDE = 90;
	
    @Override
    public void validate(Listing poi) {
    	String latitude = poi.getLatitude();
    	if (StringUtils.isEmpty(latitude)) {
    		//Do nothing; not invalid but no point parsing
    		return;
    	}
    	
    	//Sanitise the latitude to see if it contained invalid chars. Not trying to fix, just flag as invalid.
    	// This is more robust than just relying on the NFE because we explicitly know what a latitude SHOULD contain.
    	String sanitisedLatitude = latitude.replaceAll("[^0-9.-]","");
    	if (!sanitisedLatitude.equals(latitude)) {
        	poi.add(ValidationIssue.INVALID_LATITUDE);
        }
        else {
        	//Otherwise try to parse this as a valid latitude.
            try {
                float latitudeF = Float.parseFloat(latitude);
                //There are fixed values for latitude. Outside these, it's invalid.
                if (latitudeF < MIN_LATITUDE || latitudeF > MAX_LATITUDE) {
                	poi.add(ValidationIssue.INVALID_LATITUDE);
                }
            } catch (NumberFormatException e) {
            	//This is now an unexpected case, seeing as we've removed all non-numeric chars.
                poi.add(ValidationIssue.INVALID_LATITUDE);
            }
        }
    }
}
