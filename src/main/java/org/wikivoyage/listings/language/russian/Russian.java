package org.wikivoyage.listings.language.russian;

import org.wikivoyage.listings.language.russian.template.RussianRoadTemplateToStringConverter;
import org.wikivoyage.listings.input.template.TemplateToStringConverter;
import org.wikivoyage.listings.language.english.English;

import java.util.LinkedList;
import java.util.List;

public class Russian extends English {
    @Override
    public String getLanguageCode() {
        return "ru";
    }

    @Override
    public List<TemplateToStringConverter> getTemplateConverters() {
        List<TemplateToStringConverter> converters = new LinkedList<>();
        converters.add(new RussianRoadTemplateToStringConverter());
        return converters;
    }
}
