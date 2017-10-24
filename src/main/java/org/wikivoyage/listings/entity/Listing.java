package org.wikivoyage.listings.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.wikivoyage.listings.validators.ValidationIssue;

/**
 * Entity representing a Wikivoyage listing.
 */
public class Listing implements Serializable {
	
    private static final long serialVersionUID = -1756001427780563695L;

    /**
     * Wikivoyage article in which the POI was found, for instance "Tokyo/Roppongi".
     */
    protected String article;
    
    /**
     * Type of POI
     * Example: "see" or "buy"
     */
    protected String type;
    
    /**
     * Name of the POI
     * , for instance "Mori Art Museum".
     */
    protected String title;
    
    /**
     * Alternative name of the POI, for instance in the local language
     * Example: "森美術館"
     */
    protected String alt;
    
    /**
     * Wikidata entity that talks about the POI, if such an entity exists in Wikidata
     */
    protected String wikidata;
    
    /**
     * Wikipedia article that talks about the POI.
     * While theoretically findable from the wikidata property, it is used on the French Wikivoyage because
     * it is more user-friendly for editors and allows for faster rendition.
     * Example: "Mori Art Museum"
     */
    protected String wikipedia;
    
    /**
     * Address of the POI
     * It is relative to the article's scope, which means it often omits the country and city name, for instance
     * Example: "Mori Tower, 53F"
     */
    protected String address;
    
    /**
     * How to reach the POI, for instance from the nearest station.
     */
    protected String directions;
    
    /**
     * Full phone number of the POI
     */
    protected String phone;
    
    /**
     * Full toll-free phone number of the POI
     */
    protected String tollFree;
    
    /**
     * Email adress of the POI
     */
    protected String email;
    
    /**
     * FAX number of the POI
     */
    protected String fax;
    
    /**
     * URL of the official website of the POI
     */
    protected String url;
    
    /**
     * Business hours of the POI
     */
    protected String hours;
    
    /**
     * For sleep POIs, time of check-in
     */
    protected String checkIn;

    /**
     * For sleep POIs, time of check-out
     */
    protected String checkOut;
    
    /**
     * Wikimedia Commons image showing the POI, or something that represents it
     */
    protected String image;
    
    /**
     * Price of entrance to the POI, or average price meal for a eat POI
     */
    protected String price;
    
    /**
     * Latitude of the POI
     */
    protected String latitude;
    
    /**
     * Longitude of the POI
     */
    protected String longitude;
    
    /**
     * Whether Wi-Fi is available at the POI
     */
    protected String wifi;
    
    /**
     * Whether the POI is accessible to persons with disabilities
     */
    protected String accessibility;
    /**
     * Description of the POI
     */
    protected String description;

    /**
     * Last time the information about this POI has been updated in Wikivoyage
     */
    protected String lastEdit;
    
    /**
     * 2-character language code
     */
    protected String language;
    
    /**
     * List of ValidationIssues found with this POI during validation
     */
    protected List<ValidationIssue> validationIssues = new ArrayList<>();

    public Listing(
        String article, String type, String title, String alt, String wikidata, String wikipedia, String address, String directions,
        String phone, String tollFree, String email, String fax, String url,
        String hours, String checkIn, String checkOut, String image, String price,
        String latitude, String longitude, String wifi, String accessibility, String lastEdit, String description, String language
    ) {
        this.article = article;
        this.type = type;
        this.title = title;
        this.wikidata = wikidata;
        this.wikipedia = wikipedia;
        this.alt = alt;
        this.address = address;
        this.directions = directions;
        this.phone = phone;
        this.tollFree = tollFree;
        this.email = email;
        this.fax = fax;
        this.url =url;
        this.hours = hours;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.image = image;
        this.price = price;
        this.latitude = latitude;
        this.longitude = longitude;
        this.wifi = wifi;
        this.accessibility = accessibility;
        this.lastEdit = lastEdit;
        this.description = description;
        this.language = language;
    }

    public String getArticle()
    {
        return article;
    }
    
    public String getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getAlt() {
		return alt;
	}
    
    /**
     * Get wikidata.
     * 
     * @return wikidata if its valid, otherwise null
     */
    public String getWikidata() {
        if (validationIssues.contains(ValidationIssue.INVALID_WIKIDATA_QID) || validationIssues.contains(ValidationIssue.REDIRECT_WIKIDATA_QID)) {
            return null;
        } else {
            return wikidata;
        }
    }

    /**
     * Get raw wikidata for purpose of validation report.
     * 
     * @return unvalidated wikidata
     */
    public String rawWikidata() {
    	return wikidata;
    }
    
    public String getWikipedia() {
    	return wikipedia;
    }

	public String getAddress() {
		return address;
	}

	public String getDirections() {
		return directions;
	}

	public String getPhone() {
		return phone;
	}

	public String getTollFree() {
		return tollFree;
	}

    /**
     * Get email.
     * 
     * @return email if its valid, otherwise null
     */
	public String getEmail() {
        if (validationIssues.contains(ValidationIssue.INVALID_EMAIL)) {
            return null;
        } else {
            return email;
        }
    }

    /**
     * Get raw email for purpose of validation report.
     * 
     * @return unvalidated email
     */
	public String rawEmail() {
		return email;
	}

	public String getFax() {
		return fax;
	}

    /**
     * Get URL.
     * 
     * @return URL if its valid, otherwise null
     */
	public String getUrl() {
        if (validationIssues.contains(ValidationIssue.INVALID_URL)) {
            return null;
        } else {
            return url;
        }
    }

    /**
     * Get raw URL for purpose of validation report.
     * 
     * @return unvalidated URL
     */
	public String rawUrl() {
		return url;
	}

	public String getHours() {
		return hours;
	}

	public String getCheckIn() {
		return checkIn;
	}

	public String getCheckOut() {
		return checkOut;
	}

	public String getImage() {
		return image;
	}

	public String getPrice() {
		return price;
	}

    /**
     * Get latitude.
     * 
     * @return latitude if its valid, otherwise null
     */
	public String getLatitude() {
        if (validationIssues.contains(ValidationIssue.INVALID_LATITUDE)) {
            return null;
        } else {
            return latitude;
        }
    }

    /**
     * Get raw latitude for purpose of validation report.
     * 
     * @return unvalidated latitude
     */
	public String rawLatitude() {
        return latitude;
    }

    /**
     * Get longitude.
     * 
     * @return longitude if its valid, otherwise null
     */
    public String getLongitude() {
        if (validationIssues.contains(ValidationIssue.INVALID_LONGITUDE)) {
            return null;
            } else {
            return longitude;
        }
    }

    /**
     * Get raw longitude for purpose of validation report.
     * 
     * @return unvalidated longitude
     */
    public String rawLongitude() {
        return longitude;
    }
    
    public String getWifi() {
    	return wifi;
    }
    
    public String getAccessibility() {
    	return accessibility;
    }
    
    public String getLastEdit() {
    	return lastEdit;
    }

    public String getDescription() {
        return description;
    }

    public String getLanguage() {
        return language;
    }

    public boolean isPositionalDataEmpty()
    {
        return latitude == null || longitude == null ||
        		latitude.equals("") || longitude.equals("");
    }
    
    public void add(ValidationIssue issue) {
        validationIssues.add(issue);
    }
    
    public List<ValidationIssue> getValidationIssues() {
        return validationIssues;
    }
    
    public boolean isValid() {
        return validationIssues.isEmpty();
    }
}
