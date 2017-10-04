package org.wikivoyage.listings.language.german;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.wikivoyage.listings.entity.Listing;
import org.wikivoyage.listings.input.template.TemplateNode;
import org.wikivoyage.listings.input.template.TemplateToStringConverter;
import org.wikivoyage.listings.language.Language;

/**
 * The specificities of the German edition of Wikivoyage.
 */
public class German implements Language
{
    @Override
    public String getLanguageCode() {
        return "de";
    }

    /**
     * All listings that can be found in the German edition of Wikivoyage, lowercase.
     */
    @Override
    public HashSet<String> getListingTemplates()
    {
    	HashSet<String> listingTemplates = new HashSet<>();
    	
    	listingTemplates.add("vcard");
    	
    	/*
	    listingTemplates.add("hotel");
        listingTemplates.add("restaurant");
        listingTemplates.add("bar");
        listingTemplates.add("shop");
        listingTemplates.add("book seller");
        listingTemplates.add("mall");
        listingTemplates.add("airport");
        listingTemplates.add("terminal");
        listingTemplates.add("bus");
        listingTemplates.add("sight");
        listingTemplates.add("park");
        listingTemplates.add("museum");
        listingTemplates.add("gallery");
        listingTemplates.add("university");
        listingTemplates.add("school");
        listingTemplates.add("college");
        listingTemplates.add("theater");
        listingTemplates.add("music");
        listingTemplates.add("club");
        listingTemplates.add("cinema");
        listingTemplates.add("amusement park");
        listingTemplates.add("festival");
        listingTemplates.add("sport");
        listingTemplates.add("library");
        listingTemplates.add("post");
        listingTemplates.add("office");
        listingTemplates.add("bank");
        listingTemplates.add("hospital");
        listingTemplates.add("pharmacy");
        listingTemplates.add("religious site");
        listingTemplates.add("landmark");
        */
	    
	    return listingTemplates;
    }
    
    /**
     * The name of the place, which is the only strictly required element.
     */
    @Override
    public String getNameElement() {
    		return "name";
    }
    
    @Override
	public String getFlagElement() {
		return "";
	}
    
    /**
     * Convert listing template into a WikivoyagePOI object.
     */
    @Override
    public Listing parseListingTemplate(String article, TemplateNode template, String poiType) {

        // Type
        if (poiType==null) {
			switch(template.getArgument("type")) {
			    case "sight":
	    		case "museum":
	    		case "gallery":
	    		case "religious site":
	            case "landmark":
	    			poiType = "see";
	    			break;
	    		case "park":
	    		case "university":
	    		case "school":
	    		case "college":
	    		case "theater":
	    		case "music":
	    		case "club":
	    		case "cinema":
	    		case "amusement park":
	    		case "festival":
	    		case "sport":
	    		case "library":
	    			poiType = "do";
	    			break;
	    		case "shop":
	    		case "book seller":
	    		case "mall":
	    		case "bank":
	    			poiType = "buy";
	    			break;
	    		case "restaurant":
	    			poiType = "eat";
	    			break;
	    		case "bar":
	    			poiType = "drink";
	    			break;
	    		case "hotel":
	    		case "alpine hut":
	    			poiType = "sleep";
	    			break;
	    		case "airport": // Or create new types?
	    		case "terminal":
	    		case "bus":
	    		case "post":
	    		case "office":
	    		case "hospital":
	    		case "pharmacy":
	    		default:
	    			poiType = "listing";
	    			break;
			}
        }

    	return new Listing(
			article,
			poiType,
			template.getArgument("name"),
			template.getArgument("alt"),
            template.getArgument("wikidata"), // Wikidata
            "", // No Wikipedia property on German Wikivoyage
			template.getArgument("address"),
			template.getArgument("directions"),
			template.getArgument("phone"),
			template.getArgument("tollfree"),
			template.getArgument("email"),
			template.getArgument("fax"),
			template.getArgument("url"),
			template.getArgument("hours"),
			template.getArgument("checkin"),
			template.getArgument("checkout"),
			template.getArgument("image"),
			template.getArgument("price"),
			template.getArgument("lat"),
			template.getArgument("long"),
			"", // No Wi-Fi property on German Wikivoyage
            "", // No accessibility property on German Wikivoyage
			template.getArgument("lastedit"),
			template.getArgument("description"),
            getLanguageCode()
            // TODO: other parameters like subtype, group, facebook, twitter, skype, intl-area-code, credit-cards, before
        );
    }

    @Override
    public List<TemplateToStringConverter> getTemplateConverters() {
        return new LinkedList<>();
    }

	
}