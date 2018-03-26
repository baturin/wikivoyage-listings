package org.wikivoyage.listings.language.italiano; 

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.wikivoyage.listings.entity.Listing;
import org.wikivoyage.listings.input.template.TemplateNode;
import org.wikivoyage.listings.input.template.TemplateToStringConverter;
import org.wikivoyage.listings.language.Language;
/*import org.wikivoyage.listings.language.es.template.DeadLinkTemplateToStringConverter;*/

/**
 * Características de la edición en español de Wikiviajes.
 */
public class Italiano implements Language
{
    @Override
    public String getLanguageCode() {
        return "it";
    }

    /**
     * Todos los tipos de listados que se pueden encontrar en la edición en español de Wikiviajes.
     */
    @Override
    public HashSet<String> getListingTemplates()
    {
    	HashSet<String> listingTemplates = new HashSet<>();

	    listingTemplates.add("cosa vedere");
      listingTemplates.add("eventi e feste");
      listingTemplates.add("cosa fare");
      listingTemplates.add("acquisti");
      listingTemplates.add("dove mangiare");
	    listingTemplates.add("bevande");
	    listingTemplates.add("dove alloggiare");
      listingTemplates.add("sicurezza");
      listingTemplates.add("come arrivare");
      listingTemplates.add("come spostarsi");
      listingTemplates.add("come divertirsi");
      listingTemplates.add("come restare in contatto");
	    listingTemplates.add("listato");
      
	    return listingTemplates;
    }
    
    /**
     * El nombre del lugar, el único elemento requerido.
     */
    @Override
    public String getNameElement() {
		return "nombre";
    }
    
    /**
     * Convertir las plantillas de listado en un objeto POI (punto de interés) de Wikiviajes.
     */
    @Override
    public Listing parseListingTemplate(String article, TemplateNode template, String poiType) {
        // Type
    		if (poiType==null) {/*
	        if (template.getNameLowercase().equals("listado")) {
	            if (template.hasArgument("tipo")) {
	                poiType = template.getArgument("tipo");
	            } else {
	                poiType = "otro";
	            }
	        } else {
	            poiType = template.getNameLowercase();
	        }
    		*/
				switch (template.getNameLowercase ()) {
					case "cosa vedere":
						poiType = "see";
						break;
          case "eventi e feste":
					case "cosa fare":
						poiType = "do";
						break;
          case "acquisti":
						poiType = "buy";
						break;
          case "dove mangiare":
						poiType = "eat";
						break;
					case "bevande":
						poiType = "drink";
						break;
					case "dove alloggiare":
						poiType = "sleep";
						break;
          case "sicurezza":
          case "come arrivare":
          case "come spostarsi":
          case "come divertirsi":
          case "come restare in contatto":
					case "listato":
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
        List<TemplateToStringConverter> converters = new LinkedList<>();
        /*converters.add(new DeadLinkTemplateToStringConverter());*/
        return converters;
    }

	@Override
	public String getFlagElement() {
		return "flag";
	}
}
