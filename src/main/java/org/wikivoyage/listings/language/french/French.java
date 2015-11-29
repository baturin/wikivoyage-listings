package org.wikivoyage.listings.language.french;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.wikivoyage.listings.entity.WikivoyagePOI;
import org.wikivoyage.listings.language.french.template.HoraireTemplateToStringConverter;
import org.wikivoyage.listings.language.french.template.PrixTemplateToStringConverter;
import org.wikivoyage.listings.input.template.TemplateNode;
import org.wikivoyage.listings.input.template.TemplateToStringConverter;
import org.wikivoyage.listings.language.Language;

/**
 * The specificities of the French edition of Wikivoyage.
 */
public class French implements Language
{
    @Override
    public String getLanguageCode() {
        return "fr";
    }

    /**
     * All listings that can be found in the French edition of Wikivoyage.
     */
    public HashSet<String> getListingTemplates()
    {
    	HashSet<String> listingTemplates = new HashSet<>();
    	
	    listingTemplates.add("aller");
	    listingTemplates.add("circuler");
	    listingTemplates.add("voir");
	    listingTemplates.add("faire");
	    listingTemplates.add("acheter");
	    listingTemplates.add("manger");
	    listingTemplates.add("sortir");
	    listingTemplates.add("se loger");
	    listingTemplates.add("ville");
	    listingTemplates.add("destination");
	    listingTemplates.add("représentation diplomatique");
	    listingTemplates.add("listing");
	    listingTemplates.add("autres");
	    
	    return listingTemplates;
    }
    
    /**
     * The name of the place, which is the only strictly required element.
     */
    public String getNameElement() {
    		return "nom";
    }
    
    /**
     * Convert listing template into a WikivoyagePOI object.
     */
    public WikivoyagePOI parseListingTemplate(String article, TemplateNode template) {

        // Type
        String poiType;
		switch(template.getNameLowercase()) {
    		case "voir":
    			poiType = "see";
    			break;
    		case "faire":
    			poiType = "do";
    			break;
    		case "acheter":
    			poiType = "buy";
    			break;
    		case "manger":
    			poiType = "eat";
    			break;
    		case "sortir":
    			poiType = "drink";
    			break;
    		case "se loger":
    			poiType = "sleep";
    			break;
    		case "aller": // Or create new types?
    		case "ville":
    		case "destination":
    		case "représentation diplomatique":
    		case "autre":
    		default:
    			poiType = "listing";
    			break;
		}

    	return new WikivoyagePOI(
			article,
			poiType,
			template.getArgument("nom"),
			template.getArgument("alt"),
			template.getArgument("adresse"),
			template.getArgument("directions"),
			template.getArgument("téléphone"),
			template.getArgument("numéro gratuit"),
			template.getArgument("email"),
			template.getArgument("fax"),
			template.getArgument("url"),
			template.getArgument("horaire"),
			template.getArgument("arrivée"),
			template.getArgument("départ"),
			template.getArgument("image"),
			template.getArgument("prix"),
			template.getArgument("latitude"),
			template.getArgument("longitude"),
			template.getArgument("description"),
            getLanguageCode()
            // TODO: other parameters like wikipédia, wikidata, facebook, wifi, téléphone portable, handicap, mise à jour
        );
    }

    @Override
    public List<TemplateToStringConverter> getTemplateConverters() {
        List<TemplateToStringConverter> converters = new LinkedList<>();
        converters.add(new HoraireTemplateToStringConverter());
        converters.add(new PrixTemplateToStringConverter());
        return converters;
    }
}