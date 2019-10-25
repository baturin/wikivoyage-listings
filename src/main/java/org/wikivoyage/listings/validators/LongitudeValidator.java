package org.wikivoyage.listings.validators;

import org.wikivoyage.listings.entity.Listing;

public class LongitudeValidator extends GeographicPositionValidator {
    
	//0 at the prime meridian; 180 degrees east and west
  	private static final int MIN_LONGITUDE = -180;
  	private static final int MAX_LONGITUDE = 180;

	@Override
	protected int getMinValue() {
		return MIN_LONGITUDE;
	}

	@Override
	protected int getMaxValue() {
		return MAX_LONGITUDE;
	}

	@Override
	protected ValidationIssue getValidationIssue() {
		return ValidationIssue.INVALID_LONGITUDE;
	}

	@Override
	protected String getGeographicPositionValue(Listing poi) {
		return poi.getLongitude();
	}
}
