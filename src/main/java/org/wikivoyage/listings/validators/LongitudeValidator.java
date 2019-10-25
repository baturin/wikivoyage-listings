package org.wikivoyage.listings.validators;

import org.apache.commons.lang.StringUtils;
import org.wikivoyage.listings.entity.Listing;

public class LongitudeValidator extends SimpleValidator {
    
	//0 at the prime meridian; 180 degrees east and west
  	private static final int MIN_LONGITUDE = -180;
  	private static final int MAX_LONGITUDE = 180;
  	
      @Override
      public void validate(Listing poi) {
      	String longitude = poi.getLongitude();
      	if (StringUtils.isEmpty(longitude)) {
      		//Do nothing; not invalid but no point parsing
      		return;
      	}
      	
      	//Sanitise the longitude to see if it contained invalid chars. Not trying to fix, just flag as invalid.
      	// This is more robust than just relying on the NFE because we explicitly know what a longitude SHOULD contain.
      	String sanitisedLongitude = longitude.replaceAll("[^0-9.-]","");
      	if (!sanitisedLongitude.equals(longitude)) {
          	poi.add(ValidationIssue.INVALID_LONGITUDE);
          }
          else {
          	//Otherwise try to parse this as a valid longitude.
              try {
                  float longitudeF = Float.parseFloat(longitude);
                  //There are fixed values for longitude. Outside these, it's invalid.
                  if (longitudeF < MIN_LONGITUDE || longitudeF > MAX_LONGITUDE) {
                  	poi.add(ValidationIssue.INVALID_LONGITUDE);
                  }
              } catch (NumberFormatException e) {
              	//This is now an unexpected case, seeing as we've removed all non-numeric chars.
                  poi.add(ValidationIssue.INVALID_LONGITUDE);
              }
          }
      }
}
