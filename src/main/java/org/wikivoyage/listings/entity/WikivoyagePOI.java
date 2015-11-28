package org.wikivoyage.listings.entity;

/**
 * Entity representing a Wikivoyage listing.
 */
public class WikivoyagePOI {
	
    /**
     * Wikivoyage article in which the POI was found, for instance "Tokyo/Roppongi".
     */
    private String article;
    
    /**
     * Type of POI, for instance "see" or "buy".
     */
    private String type;
    
    /**
     * Name of the POI, for instance "Mori Art Museum".
     */
    private String title;
    private String alt;
    private String address;
    private String directions;
    private String phone;
    private String tollFree;
    private String email;
    private String fax;
    private String url;
    private String hours;
    private String checkIn;
    private String checkOut;
    private String image;
    private String price;
    private String latitude;
    private String longitude;
    private String description;

    public WikivoyagePOI(
        String article, String type, String title, String alt, String address, String directions,
        String phone, String tollFree, String email, String fax, String url,
        String hours, String checkIn, String checkOut, String image, String price,
        String latitude, String longitude, String description
    ) {
        this.article = article;
        this.type = type;
        this.title = title;
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
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public boolean isPositionalDataEmpty()
    {
        return latitude == null || longitude == null ||
        		latitude.equals("") || longitude.equals("");
    }
}
