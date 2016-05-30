package org.wikivoyage.listings.entity;

import java.io.Serializable;

/**
 * Entity representing a Wikivoyage listing.
 */
public class WikivoyagePOI implements Serializable {
	
    /**
     * Wikivoyage article in which the POI was found, for instance "Tokyo/Roppongi".
     */
    private String article;
    
    /**
     * Type of POI
     * Example: "see" or "buy"
     */
    private String type;
    
    /**
     * Name of the POI
     * , for instance "Mori Art Museum".
     */
    private String title;
    
    /**
     * Alternative name of the POI, for instance in the local language
     * Example: "森美術館"
     */
    private String alt;
    
    /**
     * Wikidata entity that talks about the POI, if such an entity exists in Wikidata
     */
    private String wikidata;
    
    /**
     * Wikipedia article that talks about the POI.
     * While theoretically findable from the wikidata property, it is used on the French Wikivoyage because
     * it is more user-friendly for editors and allows for faster rendition.
     * Example: "Mori Art Museum"
     */
    private String wikipedia;
    
    /**
     * Address of the POI
     * It is relative to the article's scope, which means it often omits the country and city name, for instance
     * Example: "Mori Tower, 53F"
     */
    private String address;
    
    /**
     * How to reach the POI, for instance from the nearest station.
     */
    private String directions;
    
    /**
     * Full phone number of the POI
     */
    private String phone;
    
    /**
     * Full toll-free phone number of the POI
     */
    private String tollFree;
    
    /**
     * Email adress of the POI
     */
    private String email;
    
    /**
     * FAX number of the POI
     */
    private String fax;
    
    /**
     * URL of the official website of the POI
     */
    private String url;
    
    /**
     * Business hours of the POI
     */
    private String hours;
    
    /**
     * For sleep POIs, time of check-in
     */
    private String checkIn;

    /**
     * For sleep POIs, time of check-out
     */
    private String checkOut;
    
    /**
     * Wikimedia Commons image showing the POI, or something that represents it
     */
    private String image;
    
    /**
     * Price of entrance to the POI, or average price meal for a eat POI
     */
    private String price;
    
    /**
     * Latitude of the POI
     */
    private String latitude;
    
    /**
     * Longitude of the POI
     */
    private String longitude;
    
    /**
     * Whether Wi-Fi is available at the POI
     */
    private String wifi;
    
    /**
     * Whether the POI is accessible to persons with disabilities
     */
    private String accessibility;
    /**
     * Description of the POI
     */
    private String description;

    /**
     * Last time the information about this POI has been updated in Wikivoyage
     */
    private String lastEdit;
    
    /**
     * 2-character language code
     */
    private String language;

    public WikivoyagePOI(
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
    
    public String getWikidata() {
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

	public String getEmail() {
		return email;
	}

	public String getFax() {
		return fax;
	}

	public String getUrl() {
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

	public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
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
}
