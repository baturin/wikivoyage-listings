package org.wikivoyage.listings.language.russian.template;

import org.wikivoyage.listings.input.template.TemplateNode;
import org.wikivoyage.listings.input.template.TemplateToStringConverter;
import org.wikivoyage.listings.utils.StringUtils;

public class RussianRoadTemplateToStringConverter implements TemplateToStringConverter {
    @Override
    public String convertToString(TemplateNode template)
    {
        return StringUtils.joinStrings(template.getPositionalArguments());
    }

    @Override
    public String getTemplateName()
    {
        return "российская трасса";
    }
}
