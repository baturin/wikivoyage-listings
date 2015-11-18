package language;

import java.util.HashMap;
import java.util.HashSet;

import org.wikivoyage.ru.listings.entity.WikivoyagePOI;

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
     * Make a listings' parameters into a WikivoyagePOI object.
     */
    public WikivoyagePOI parseArgumentsDict(
    		String article, String templateName, HashMap<String, String> args) {

        // Type
        String poiType;
        if (templateName.equals("listing")) {
            if (args.containsKey("type")) {
                poiType = args.get("type");
            } else {
                poiType = "other";
            }
        } else {
            poiType = templateName;
        }
        
        // Description
        String description = "";
        if (args.containsKey("description")) {
            description = args.get("description");
        } else if (args.containsKey("content")) {
            description = args.get("content");
        }

    	return new WikivoyagePOI(
                article,
                poiType,
                args.get("name"), // TODO language
                args.get("alt"),
                args.get("address"),
                args.get("directions"),
                args.get("phone"),
                args.get("tollFree"),
                args.get("email"),
                args.get("fax"),
                args.get("url"),
                args.get("hours"),
                args.get("checkin"),
                args.get("checkout"),
                args.get("image"),
                args.get("price"),
                args.get("lat"),
                args.get("long"),
                description);
    }
}