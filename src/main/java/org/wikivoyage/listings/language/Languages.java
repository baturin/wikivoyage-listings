package org.wikivoyage.listings.language;

import org.wikivoyage.listings.language.english.English;
import org.wikivoyage.listings.language.german.German;
import org.wikivoyage.listings.language.russian.Russian;

import java.util.LinkedList;
import java.util.List;

/**
 * Class for listing and creating language objects
 */
public class Languages {
    private static Language [] languages = {
        new English(),
        new Russian(),
        new French(),
        new German()
    };
    private static Language defaultLanguage = languages[0];

    /**
     * Get the appropriate language object for a particular language code.
     */
    public static Language create(String languageCode) {
        for (Language lang: languages) {
            if (lang.getLanguageCode().equals(languageCode)) {
                return lang;
            }
        }
        return defaultLanguage;
}

    /**
     * Get list of all available language codes.
     */
    public static List<String> getLanguageCodes() {
        List<String> codes = new LinkedList<>();
        for (Language lang: languages) {
            codes.add(lang.getLanguageCode());
        }
        return codes;
    }
}
