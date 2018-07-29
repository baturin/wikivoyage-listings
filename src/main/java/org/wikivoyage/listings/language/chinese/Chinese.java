package org.wikivoyage.listings.language.chinese;

import org.wikivoyage.listings.entity.Listing;
import org.wikivoyage.listings.input.template.TemplateNode;
import org.wikivoyage.listings.input.template.TemplateToStringConverter;
import org.wikivoyage.listings.language.Language;
import org.wikivoyage.listings.language.english.English;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Chinese implements Language {

    @Override
    public String getLanguageCode() {
        return "zh";
    }

    /**
     * All listings that can be found in the English edition of Wikivoyage.
     */
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

    /**
     * The name of the place, which is the only strictly required element.
     */
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
        return new LinkedList<>();
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
                "", // No Wikidata property on Chinese Wikivoyage
                "", // No Wikipedia property on Chinese Wikivoyage
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
                "", // No Wi-Fi property on Chinese Wikivoyage
                "", // No accessibility property on Chinese Wikivoyage
                template.getArgument("lastedit"),
                description,
                getLanguageCode()
        );
    }
}
