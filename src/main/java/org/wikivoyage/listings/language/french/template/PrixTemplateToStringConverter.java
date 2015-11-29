package org.wikivoyage.listings.language.french.template;

import org.wikivoyage.listings.input.template.TemplateNode;
import org.wikivoyage.listings.input.template.TemplateToStringConverter;
import org.wikivoyage.listings.utils.StringUtils;

public class PrixTemplateToStringConverter implements TemplateToStringConverter {
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
