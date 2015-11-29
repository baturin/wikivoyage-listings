package org.wikivoyage.listings.input.template;

import org.wikivoyage.listings.utils.StringUtils;

public class FrenchPrixTemplateToStringConverter implements TemplateToStringConverter {
    @Override
    public String convertToString(TemplateNode template)
    {
        return StringUtils.joinStrings(template.getPositionalArguments());
    }

    public String getTemplateName()
    {
        return "prix";
    }
}
