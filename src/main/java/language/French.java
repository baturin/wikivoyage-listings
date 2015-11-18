package language;

import java.util.HashMap;
import java.util.HashSet;

import org.wikivoyage.ru.listings.entity.WikivoyagePOI;

/**
 * The specificities of the French edition of Wikivoyage.
 */
public class French extends Language
{
    /**
     * All listings that can be found in the French edition of Wikivoyage.
     */
    public HashSet<String> getListingTemplates()
    {
    	HashSet<String> listingTemplates = new HashSet<>();
    	
	    listingTemplates.add("Aller");
	    listingTemplates.add("Circuler");
	    listingTemplates.add("Voir");
	    listingTemplates.add("Faire");
	    listingTemplates.add("Acheter");
	    listingTemplates.add("Manger");
	    listingTemplates.add("Sortir");
	    listingTemplates.add("Se loger");
	    listingTemplates.add("Ville");
	    listingTemplates.add("Destination");
	    listingTemplates.add("Représentation diplomatique");
	    listingTemplates.add("Listing");
	    listingTemplates.add("Autres");
	    
	    return listingTemplates;
    }
    
    /**
     * The name of the place, which is the only strictly required element.
     */
    public String getNameElement() {
    		return "nom";
    }
    
    /**
     * Make a listings' parameters into a WikivoyagePOI object.
     */
    public WikivoyagePOI parseArgumentsDict(
    		String article, String templateName, HashMap<String, String> args) {

        // Type
        String poiType;
		switch(templateName) {
    		case "Voir":
    			poiType = "see";
    			break;
    		case "Faire":
    			poiType = "do";
    			break;
    		case "Acheter":
    			poiType = "buy";
    			break;
    		case "Manger":
    			poiType = "eat";
    			break;
    		case "Sortir":
    			poiType = "drink";
    			break;
    		case "Se loger":
    			poiType = "sleep";
    			break;
    		case "Aller": // Or create new types?
    		case "Ville":
    		case "Destination":
    		case "Représentation diplomatique":
    		case "Autre":
    		default:
    			poiType = "listing";
    			break;
		}

    	return new WikivoyagePOI(
                article,
                poiType,
                args.get("nom"), // TODO language
                args.get("alt"),
                args.get("adresse"),
                args.get("directions"),
                args.get("téléphone"),
                args.get("numéro gratuit"),
                args.get("email"),
                args.get("fax"),
                args.get("url"),
                args.get("horaire"),
                args.get("arrivée"),
                args.get("départ"),
                args.get("image"),
                args.get("prix"),
                args.get("latitude"),
                args.get("longitude"),
                args.get("description"));
    	// TODO: other parameters like wikipédia, wikidata, facebook, wifi, téléphone portable, handicap, mise à jour
    }
}