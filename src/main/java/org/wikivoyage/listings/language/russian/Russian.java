package org.wikivoyage.listings.language.russian;

import org.wikivoyage.listings.entity.Listing;
import org.wikivoyage.listings.input.template.TemplateNode;
import org.wikivoyage.listings.input.template.TemplateToStringConverter;
import org.wikivoyage.listings.language.Language;
import org.wikivoyage.listings.language.russian.template.RussianRoadTemplateToStringConverter;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Russian implements Language {

    @Override
    public String getLanguageCode() {
        return "ru";
    }

    @Override
    public HashSet<String> getListingTemplates() {
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

    @Override
    public String getNameElement() {
        return "name";
    }

    @Override
    public String getFlagElement() {
        return "flag";
    }

    @Override
    public List<TemplateToStringConverter> getTemplateConverters() {
        List<TemplateToStringConverter> converters = new LinkedList<>();
        converters.add(new RussianRoadTemplateToStringConverter());
        return converters;
    }

    /**
     * Convert listing template into a WikivoyagePOI object.
     */
    @Override
    public Listing parseListingTemplate(String article, TemplateNode template, String poiType) {
        // Type
        if (poiType == null) {
            if (template.getNameLowercase().equals("listing")) {
                if (template.hasArgument("type")) {
                    poiType = template.getArgument("type");
                } else {
                    poiType = "other";
                }
            } else {
                poiType = template.getNameLowercase();
            }
        }

        // Description
        String description = "";
        if (template.hasArgument("description")) {
            description = template.getArgument("description");
        } else if (template.hasArgument("content")) {
            description = template.getArgument("content");
        }

        return new Listing(
                article,
                poiType,
                template.getArgument("name"),
                template.getArgument("alt"),
                template.getArgument("wdid"),
                "", // No Wikipedia property on Russian Wikivoyage
                template.getArgument("address"),
                template.getArgument("directions"),
                template.getArgument("phone"),
                "", // No tollfree property on Russian Wikivoyage
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
                "", // No Wi-Fi property on Russian Wikivoyage
                "", // No accessibility property on Russian Wikivoyage
                template.getArgument("lastedit"),
                description,
                getLanguageCode()
        );
    }
}
