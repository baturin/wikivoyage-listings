package org.wikivoyage.listings.language;

import java.util.HashSet;
import java.util.List;

import org.wikivoyage.listings.entity.WikivoyagePOI;
import org.wikivoyage.listings.input.template.TemplateNode;
import org.wikivoyage.listings.input.template.TemplateToStringConverter;

/**
 * A Language object defines the specificities of a particular language of Wikivoyage.
 */
public interface Language
{
    /**
     * Get language 2-character code (in lowercase), for example "en" or "fr"
     */
    String getLanguageCode();

    /**
     * All listings that can be found in this language's Wikivoyage.
	 * This function should return listing template names in lowercase.
     */
    HashSet<String> getListingTemplates();
    
    /**
     * The name of the place, which is the only strictly required element.
     */
    String getNameElement();
    
    /**
     * Convert listing template into a WikivoyagePOI object.
     */
    WikivoyagePOI parseListingTemplate(String article, TemplateNode template);

    /**
     * Get template to string converters to be used inside listing template arguments
     */
    List<TemplateToStringConverter> getTemplateConverters();
}