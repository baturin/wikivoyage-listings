package org.wikivoyage.listings.cli_parse.validators;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;
import org.wikivoyage.listings.language.Language;

import java.util.Set;

public class LanguageSupportValidator implements IParameterValidator {

    @Override
    public void validate(String s, String s1) throws ParameterException {
        Set<String> allowedLanguages = Language.getLanguageCodeToLanguage().keySet();

        if (!allowedLanguages.contains(s1.toLowerCase())) {
            String message = String.format( "The language supplied is not one we support. Please use one of " +
                    "the following formats: \n %s", allowedLanguages.toString());
            throw new ParameterException(message);
        }
    }
}
