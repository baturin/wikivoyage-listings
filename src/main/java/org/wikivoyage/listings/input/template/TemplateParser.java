package org.wikivoyage.listings.input.template;

import org.sweble.wikitext.parser.nodes.WtTemplate;

public interface TemplateParser {
    public String parse(WtTemplate template);
    public String getTemplateName();
}
