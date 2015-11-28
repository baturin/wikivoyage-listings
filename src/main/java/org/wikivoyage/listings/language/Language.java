package org.wikivoyage.listings.language;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.wikivoyage.listings.entity.WikivoyagePOI;

/**
 * A Language object defines the specificities of a particular language of Wikivoyage.
 */
public abstract class Language
{
	/**
	 * Get the appropriate language object for a particular language code.
	 */
    public static Language create(String languageCode) {
    	switch (languageCode) {
    		case "fr":
    			return new French();
    		default:
    			return new English();
    	}
    }


    /**
     * All listings that can be found in this language's Wikivoyage.
     */
    public abstract HashSet<String> getListingTemplates();
    
    /**
     * The name of the place, which is the only strictly required element.
     */
    public abstract String getNameElement();
    
    /**
     * Make a listings' parameters into a WikivoyagePOI object.
     */
    public abstract WikivoyagePOI parseArgumentsDict(
    		String article, String templateName, HashMap<String, String> args);

    /**
     * All available language codes.
     */
    public static List<String> getLanguageCodes() {
        return Arrays.asList(
            "ru",
            "en",
            "fr");
    }

    /**
     * Try to guess the language from an URL.
     * Quite unreliable, but should at least work for the standard dump download URLs.
     */
    public static Language guessFromUrl(String url) {
    	// Return the first language whose two letters are in the URL.
    	for(Iterator<String> i = getLanguageCodes().iterator(); i.hasNext(); ) {
    	    String languageCode = i.next();
    	    if(url.contains(languageCode)) {
    	    	return create(languageCode);
    	    }
    	}
    	
    	// Could not guess, return default.
    	return new English();
    }
}