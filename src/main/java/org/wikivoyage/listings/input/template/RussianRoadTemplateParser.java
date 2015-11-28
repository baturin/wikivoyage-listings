package org.wikivoyage.listings.input.template;

import org.sweble.wikitext.parser.nodes.WtTemplate;

public class RussianRoadTemplateParser implements TemplateParser {
    @Override
    public String parse(WtTemplate template)
    {
        return TemplateUtils.convertTemplateToStringByJoin(template);
    }

    @Override
    public String getTemplateName()
    {
        return "российская трасса";
    }
}
