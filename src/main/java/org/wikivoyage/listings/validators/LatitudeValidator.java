package org.wikivoyage.listings.validators;

import org.wikivoyage.listings.entity.Listing;

public final class LatitudeValidator extends GeographicPositionValidator {
	
	//0 at the equator; 90 at the respective poles
	private static final int MIN_LATITUDE = -90;
	private static final int MAX_LATITUDE = 90;

	@Override
	protected int getMinValue() {
		return MIN_LATITUDE;
	}

	@Override
	protected int getMaxValue() {
		return MAX_LATITUDE;
	}

	@Override
	protected ValidationIssue getValidationIssue() {
		return ValidationIssue.INVALID_LATITUDE;
	}

	@Override
	protected String getGeographicPositionValue(Listing poi) {
		return poi.getLatitude();
	}
}
