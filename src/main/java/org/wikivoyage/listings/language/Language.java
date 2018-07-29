package org.wikivoyage.listings.language;

import org.wikivoyage.listings.entity.Listing;
import org.wikivoyage.listings.input.template.TemplateNode;
import org.wikivoyage.listings.input.template.TemplateToStringConverter;
import org.wikivoyage.listings.language.chinese.Chinese;
import org.wikivoyage.listings.language.english.English;
import org.wikivoyage.listings.language.espanol.Espanol;
import org.wikivoyage.listings.language.french.French;
import org.wikivoyage.listings.language.german.German;
import org.wikivoyage.listings.language.italian.Italian;
import org.wikivoyage.listings.language.portuguese.Portuguese;
import org.wikivoyage.listings.language.russian.Russian;

import java.util.*;

/**
 * A Language object defines the specificities of a particular language of Wikivoyage.
 */
public interface Language {

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
     * The name of the place, which is the only strictly required element.
     */
    String getFlagElement();

    /**
     * Convert listing template into a WikivoyagePOI object.
     * "see"
     * "do"
     * "buy"
     * "eat"
     * "drink"
     * "sleep"
     * "diplomatic-representation"
     * "listing"
     */
    Listing parseListingTemplate(String article, TemplateNode template, String poiType);

    /**
     * Get template to string converters to be used inside listing template arguments
     */
    List<TemplateToStringConverter> getTemplateConverters();



    // *** HELPERS ***

    List<Language> languages = new ArrayList<Language>() {{
        add(new English());
        add(new Russian());
        add(new French());
        add(new German());
        add(new Espanol());
        add(new Italian());
        add(new Chinese());
        add(new Portuguese());
    }};
    Language defaultLanguage = languages.get(0);

    /**
     * Get the appropriate language object for a particular language code.
     */
    static Language create(String languageCode) {
        return getLanguageCodeToLanguage().get(languageCode);
    }

    /**
     * Get list of all available language codes.
     */
    static Set<String> getLanguageCodes() {
        return getLanguageCodeToLanguage().keySet();
    }

    static Map<String, Language> getLanguageCodeToLanguage() {
        Map<String, Language> languageCodeToLanguage = new HashMap<>();

        // Gets all instances
        for (Language lang: languages) {
            languageCodeToLanguage.put(lang.getLanguageCode(), lang);
        }

        return languageCodeToLanguage;
    }
}