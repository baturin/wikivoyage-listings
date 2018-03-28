package org.wikivoyage.listings.language.espanol; 

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
public class Espanol implements Language
{
    @Override
    public String getLanguageCode() {
        return "es";
    }

    /**
     * Todos los tipos de listados que se pueden encontrar en la edición en español de Wikiviajes.
     */
    @Override
    public HashSet<String> getListingTemplates()
    {
    	HashSet<String> listingTemplates = new HashSet<>();

    	listingTemplates.add("listado");
	    listingTemplates.add("ver");
	    listingTemplates.add("hacer");
	    listingTemplates.add("comprar");
	    listingTemplates.add("comer");
	    listingTemplates.add("beber");
	    listingTemplates.add("dormir");
		listingTemplates.add("evento");
	    
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
					case "ver":
						poiType = "see";
						break;
					case "hacer":
					case "evento":
						poiType = "do";
						break;
					case "comprar":
						poiType = "buy";
						break;
					case "comer":
						poiType = "eat";
						break;
					case "beber":
						poiType = "drink";
						break;
					case "dormir":
						poiType = "sleep";
						break;
					case "listado":
					default:
						poiType = "listing";
						break;
				}
			}
        
    	return new Listing(
            article,
            poiType,
            template.getArgument("nombre"),
            template.getArgument("alt"),
            template.getArgument("wikidata"),
            template.getArgument("wikipedia"), 
            template.getArgument("dirección"),
            template.getArgument("indicaciones"),
            template.getArgument("tlf"),
            template.getArgument("tlf_gratuito"),
            template.getArgument("email"),
            template.getArgument("fax"),
            template.getArgument("url"),
            template.getArgument("horario"),
            template.getArgument("hora_entrada"),
            template.getArgument("hora_salida"),
            template.getArgument("imagen"),
            template.getArgument("precio"),
            template.getArgument("lat"),
            template.getArgument("long"),
			"", // No hay un parámetro "Wi-Fi"
			"", // No hay un parámetro "accesibilidad"
			template.getArgument("lastedit"),
            template.getArgument("descripción"),
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
		return "bandera";
	}
}
