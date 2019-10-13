package org.wikivoyage.listings.language.russian.template;

import org.wikivoyage.listings.input.template.TemplateNode;
import org.wikivoyage.listings.input.template.TemplateToStringConverter;

import static org.apache.commons.lang.StringUtils.join;

public class RussianRoadTemplateToStringConverter implements TemplateToStringConverter {
    @Override
    public String convertToString(TemplateNode template)
    {
        return join(template.getPositionalArguments().toArray());
    }

    @Override
    public String getTemplateName()
    {
        return "российская трасса";
    }
}
