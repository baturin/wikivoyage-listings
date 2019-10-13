package org.wikivoyage.listings.language.french.template;

import org.apache.commons.lang.StringUtils;
import org.wikivoyage.listings.input.template.TemplateNode;
import org.wikivoyage.listings.input.template.TemplateToStringConverter;

public class PrixTemplateToStringConverter implements TemplateToStringConverter {
    @Override
    public String convertToString(TemplateNode template)
    {
        return StringUtils.join(template.getPositionalArguments().toArray());
    }

    public String getTemplateName()
    {
        return "prix";
    }
}
