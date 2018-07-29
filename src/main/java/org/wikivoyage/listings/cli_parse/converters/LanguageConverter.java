package org.wikivoyage.listings.cli_parse.converters;

import com.beust.jcommander.IStringConverter;
import org.wikivoyage.listings.language.Language;

public class LanguageConverter implements IStringConverter<Language> {
    @Override
    public Language convert(String s) {
        return Language.getLanguageCodeToLanguage().get(s.toLowerCase());
    }
}
