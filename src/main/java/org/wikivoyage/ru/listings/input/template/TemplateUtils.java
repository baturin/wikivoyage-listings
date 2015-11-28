package org.wikivoyage.ru.listings.input.template;

import de.fau.cs.osr.ptk.common.ast.AstStringNode;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtTemplate;
import org.sweble.wikitext.parser.nodes.WtTemplateArgument;

/**
 * Utilities for working with wiki text templates
 */
public class TemplateUtils {
    /**
     * Convert wiki text template to string by simply joining all arguments
     *
     * For example:
     * {{mytemplate|param1|param2|param3}}
     * will be converted to
     * param1param2param3
     */
    public static String convertTemplateToStringByJoin(WtTemplate templateNode)
    {
        String textResult = "";
        for (WtNode childNode: templateNode.getArgs()) {
            if (childNode instanceof WtTemplateArgument) {
                WtTemplateArgument argChildNode = (WtTemplateArgument)childNode;
                textResult += convertToStringSimple(argChildNode.getValue());
            }
        }

        return textResult;
    }

    /**
     * Convert simple wiki text node to string
     */
    public static String convertToStringSimple(WtNode node)
    {
        if (node instanceof AstStringNode) {
            // Handle simple string node - just get its contents
            AstStringNode stringNode = (AstStringNode) node;
            return stringNode.getContent();
        } else {
            // Handle compound node, that consists of several others
            String textResult = "";
            for (WtNode childNode: node) {
                textResult += convertToStringSimple(childNode);
            }
            return textResult;
        }
    }
}
