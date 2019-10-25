package org.wikivoyage.listings.validators;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.wikivoyage.listings.entity.Listing;

/**
 * General superclass for longitude/latitude to reduce code proliferation.
 */
public abstract class GeographicPositionValidator extends SimpleValidator {
  	
	//Declare statically so we compile this once
	private static Pattern INVALID_CHARS_FOR_GEOGRAPHIC_POSITION = Pattern.compile("[^0-9.-]");
	
	protected abstract int getMinValue();
    protected abstract int getMaxValue();
    protected abstract ValidationIssue getValidationIssue();
    protected abstract String getGeographicPositionValue(Listing poi);
    
      @Override
      public void validate(Listing poi) {
      	String geographicPosition = getGeographicPositionValue(poi);
      	if (StringUtils.isEmpty(geographicPosition)) {
      		//Do nothing; not invalid but no point parsing
      		return;
      	}
      	
      	//Detect invalid chars in the position. Not trying to fix, just flag as invalid.
      	// This is more robust than just relying on the NFE because we explicitly know what these SHOULD contain.
      	Matcher m = INVALID_CHARS_FOR_GEOGRAPHIC_POSITION.matcher(geographicPosition);
      	if (m.find()) {
          	poi.add(getValidationIssue());
          }
          else {
          	//Otherwise try to parse this as valid.
              try {
                  float geographicPositionF = Float.parseFloat(geographicPosition);
                  //There are fixed values for these positions. Outside these, it's invalid.
                  if (geographicPositionF < getMinValue() || geographicPositionF > getMaxValue()) {
                  	poi.add(getValidationIssue());
                  }
              } catch (NumberFormatException e) {
              	//This is now an unexpected case, seeing as we've removed all non-numeric chars.
                  poi.add(getValidationIssue());
              }
          }
      }
}
