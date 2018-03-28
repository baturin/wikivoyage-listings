package org.wikivoyage.listings.language.italian; 

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.wikivoyage.listings.entity.Listing;
import org.wikivoyage.listings.input.template.TemplateNode;
import org.wikivoyage.listings.input.template.TemplateToStringConverter;
import org.wikivoyage.listings.language.Language;
/*import org.wikivoyage.listings.language.es.template.DeadLinkTemplateToStringConverter;*/

/**
 * The specificities of the Italian edition of Wikivoyage.
 */
public class Italian implements Language
{
    @Override
    public String getLanguageCode() {
        return "it";
    }

    /**
     * All listings that can be found in the Italian edition of Wikivoyage.
     */
    @Override
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
    @Override
    public String getNameElement() {
		return "nome";
    }
    
    /**
     * Convert listing template into a WikivoyagePOI object.
     */
    @Override
    public Listing parseListingTemplate(String article, TemplateNode template, String poiType) {
        // Type
    		if (poiType==null) {
	        if (template.getNameLowercase().equals("listing")) {
	            if (template.hasArgument("tipo")) {
	                poiType = template.getArgument("tipo");
	            } else {
	                poiType = "listing";
	            }
	        } else {
	            poiType = template.getNameLowercase();
	        }
	   }

    	return new Listing(
            article,
            poiType,
            template.getArgument("nome"),
            template.getArgument("alt"),
            template.getArgument("sito"),
            template.getArgument("wikidata"),
            template.getArgument("wikipedia"),
            template.getArgument("indirizzo"),
            template.getArgument("indicazioni"),
            template.getArgument("tel"),
            template.getArgument("numero verde"),
            template.getArgument("email"),
            template.getArgument("fax"),
            template.getArgument("orari"),
            template.getArgument("checkin"),
            template.getArgument("checkout"),
            template.getArgument("immagine"),
            template.getArgument("prezzo"),
            template.getArgument("lat"),
            template.getArgument("long"),
			      "", // No hay un parámetro "Wi-Fi"
			      "", // No hay un parámetro "accesibilidad"
			      template.getArgument("lastedit"),
            template.getArgument("descrizione"),
            getLanguageCode()
        );
    }

    @Override
    public List<TemplateToStringConverter> getTemplateConverters() {
        return new LinkedList<>();
    }

	@Override
	public String getFlagElement() {
	    return "bandiera";
	}
}
