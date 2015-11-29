package org.wikivoyage.listings.language;

import java.util.HashSet;

import org.wikivoyage.listings.entity.WikivoyagePOI;
import org.wikivoyage.listings.input.template.TemplateNode;

/**
 * The specificities of the English edition of Wikivoyage.
 */
public class English extends Language
{
	
    /**
     * All listings that can be found in the English edition of Wikivoyage.
     */
    public HashSet<String> getListingTemplates()
    {
    	HashSet<String> listingTemplates = new HashSet<>();

    	listingTemplates.add("listing");
	    listingTemplates.add("see");
	    listingTemplates.add("do");
	    listingTemplates.add("buy");
	    listingTemplates.add("eat");
	    listingTemplates.add("drink");
	    listingTemplates.add("sleep");
	    
	    return listingTemplates;
    }
    
    /**
     * The name of the place, which is the only strictly required element.
     */
    public String getNameElement() {
		return "name";
    }
    
    /**
     * Convert listing template into a WikivoyagePOI object.
     */
    public WikivoyagePOI parseListingTemplate(String article, TemplateNode template) {
        // Type
        String poiType;
        if (template.getNameLowercase().equals("listing")) {
            if (template.hasArgument("type")) {
                poiType = template.getArgument("type");
            } else {
                poiType = "other";
            }
        } else {
            poiType = template.getNameLowercase();
        }
        
        // Description
        String description = "";
        if (template.hasArgument("description")) {
            description = template.getArgument("description");
        } else if (template.hasArgument("content")) {
            description = template.getArgument("content");
        }

    	return new WikivoyagePOI(
            article,
            poiType,
            template.getArgument("name"), // TODO language
            template.getArgument("alt"),
            template.getArgument("address"),
            template.getArgument("directions"),
            template.getArgument("phone"),
            template.getArgument("tollFree"),
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
            description
        );
    }
}