package org.wikivoyage.listings.language.portuguese; 

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.wikivoyage.listings.entity.Listing;
import org.wikivoyage.listings.input.template.TemplateNode;
import org.wikivoyage.listings.input.template.TemplateToStringConverter;
import org.wikivoyage.listings.language.Language;
/*import org.wikivoyage.listings.language.es.template.DeadLinkTemplateToStringConverter;*/

/**
 * The specificities of the Portuguese edition of Wikivoyage.
 */
public class Portuguese implements Language
{
    @Override
    public String getLanguageCode() {
        return "pt";
    }

    /**
     * All listings that can be found in the Portuguese edition of Wikivoyage.
     */
    @Override
    public HashSet<String> getListingTemplates()
    {
    	HashSet<String> listingTemplates = new HashSet<>();

    	//listingTemplates.add("listado");
	    listingTemplates.add("veja");
	    listingTemplates.add("faça");
	    listingTemplates.add("compre");
	    listingTemplates.add("coma");
	    listingTemplates.add("beba");
	    listingTemplates.add("durma");
	    listingTemplates.add("outroitem");
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

				switch (template.getNameLowercase ()) {
					case "veja":
						poiType = "see";
						break;
					case "faça":
						poiType = "do";
						break;
					case "compre":
						poiType = "buy";
						break;
					case "coma":
						poiType = "eat";
						break;
					case "beba":
						poiType = "drink";
						break;
					case "durma":
						poiType = "sleep";
						break;
					case "outroitem":
					default:
						poiType = "listing";
						break;
				}
			}
        
    	return new Listing(
            article,
            poiType,
            template.getArgument("nome"),
            template.getArgument("alt"),
            "", // template.getArgument("wikidata"),
            "", // template.getArgument("wikipedia"),
            template.getArgument("endereço"),
            template.getArgument("direções"),
            template.getArgument("tel"),
            template.getArgument("telgratis"),
            template.getArgument("email"),
            template.getArgument("fax"),
            template.getArgument("site"),
            template.getArgument("funcionamento"),
            template.getArgument("checkin"),
            template.getArgument("checkout"),
            "", // template.getArgument("image"),
            template.getArgument("preço"),
            template.getArgument("lat"),
            template.getArgument("long"),
	    "", // template.getArgument("Wi-Fi"),
	    "", // template.getArgument("lastedit"),
            template.getArgument("sobre"),
            "",
            getLanguageCode()
        );
    }

    @Override
    public List<TemplateToStringConverter> getTemplateConverters() {
        return new LinkedList<>();
    }

	@Override
	public String getFlagElement() {
		return "";
	}
}
