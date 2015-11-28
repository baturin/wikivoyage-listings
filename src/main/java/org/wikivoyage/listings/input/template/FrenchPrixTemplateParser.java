package org.wikivoyage.listings.input.template;

import org.sweble.wikitext.parser.nodes.WtTemplate;

public class FrenchPrixTemplateParser implements TemplateParser {
    @Override
    public String parse(WtTemplate template)
    {
        return TemplateUtils.convertTemplateToStringByJoin(template);
    }

    public String getTemplateName()
    {
        return "prix";
    }
}
