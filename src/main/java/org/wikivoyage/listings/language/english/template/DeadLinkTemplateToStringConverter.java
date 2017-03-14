package org.wikivoyage.listings.language.english.template;

import org.wikivoyage.listings.input.template.TemplateNode;
import org.wikivoyage.listings.input.template.TemplateToStringConverter;


public class DeadLinkTemplateToStringConverter implements TemplateToStringConverter {
    @Override
    public String convertToString(TemplateNode template)
    {
        return "";
    }

    public String getTemplateName()
    {
        return "dead link";
    }
}
