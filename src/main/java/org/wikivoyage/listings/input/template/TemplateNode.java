package org.wikivoyage.listings.input.template;

import de.fau.cs.osr.ptk.common.ast.AstStringNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtTemplate;
import org.sweble.wikitext.parser.nodes.WtTemplateArgument;
import org.sweble.wikitext.parser.nodes.WtXmlComment;
import org.sweble.wikitext.parser.utils.WtRtDataPrinter;
import org.wikivoyage.listings.utils.StringUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Wrapper around org.sweble.wikitext.parser.nodes.WtTemplate for easy work with wikitext template data
 */
public class TemplateNode {
    private WtTemplate node;
    private HashMap<String, String> templateArguments;
    private HashMap<String, String> templateArgumentsLowercase;

    private static final Log log = LogFactory.getLog(TemplateNode.class);

    private final TemplateParser[] templateParsers = {
        new FrenchPrixTemplateParser(),
        new RussianRoadTemplateParser(),
        new FrenchHoraireTemplateParser()
    };

    public TemplateNode(WtTemplate node)
    {
        this.node = node;
        parseArguments();
        initArgumentsLowercase();
    }

    public String getName()
    {
        return TemplateUtils.convertToStringSimple(node.getName()).trim();
    }

    public String getNameLowercase()
    {
        return getName().toLowerCase();
    }

    public String getArgument(String name)
    {
        return templateArgumentsLowercase.get(name.toLowerCase());
    }

    public boolean hasArgument(String name)
    {
        return templateArgumentsLowercase.containsKey(name.toLowerCase());
    }

    /**
     * Parse template arguments into a dictionary
     */
    private void parseArguments() {
        templateArguments = new LinkedHashMap<>();

        for (WtNode templateArgumentsChildNode: node.getArgs()) {
            if (templateArgumentsChildNode instanceof WtTemplateArgument) {
                WtTemplateArgument templateArgument = (WtTemplateArgument) templateArgumentsChildNode;

                String name = convertWtNodeToString(templateArgument.getName()).trim();
                String value = convertWtNodeToString(templateArgument.getValue()).trim();
                templateArguments .put(name, value);
            }
        }
    }

    /**
     * Initialize template arguments dictionary with lowercase keys
     */
    private void initArgumentsLowercase() {
        templateArgumentsLowercase = new LinkedHashMap<>();

        for (String key: templateArguments.keySet()) {
            templateArgumentsLowercase.put(key.toLowerCase(), templateArguments.get(key));
        }
    }

    /**
     * Text conversion of WtNode object to string.
     */
    private String convertWtNodeToString(WtNode node)
    {
        if (node instanceof WtTemplate) {
            WtTemplate templateNode = (WtTemplate) node;
            String templateName = convertWtNodeToString(templateNode.getName()).trim();

            for (TemplateParser parser: templateParsers) {
                if (StringUtils.equalsCaseInsensitive(templateName, parser.getTemplateName())) {
                    return parser.parse(templateNode);
                }
            }

            log.debug("Template '" + templateName + "' was not parsed");
            return WtRtDataPrinter.print(templateNode);
        } else if (node instanceof WtXmlComment) {
            // HTML comments inside listings are ignored
            return "";
        } else if (node instanceof AstStringNode) {
            return ((AstStringNode) node).getContent().replaceAll("\\[\\[([^|\\]]*?\\||)([^|\\]]*?)\\]\\]", "$2");
        } else {
            String s = "";
            for (WtNode childNode: node) {
                s += convertWtNodeToString(childNode);
            }
            return s;
        }
    }
}
