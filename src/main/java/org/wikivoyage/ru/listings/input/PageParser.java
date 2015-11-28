package org.wikivoyage.ru.listings.input;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.helper.StringUtil;
import org.sweble.wikitext.parser.ParserConfig;
import org.sweble.wikitext.parser.WikitextPreprocessor;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtTemplate;
import org.sweble.wikitext.parser.nodes.WtTemplateArgument;
import org.sweble.wikitext.parser.utils.SimpleParserConfig;
import org.sweble.wikitext.parser.utils.StringConversionException;
import org.wikivoyage.ru.listings.entity.WikivoyagePOI;

import de.fau.cs.osr.ptk.common.ast.AstStringNode;
import language.Language;
import org.wikivoyage.ru.listings.input.template.FrenchPrixTemplateParser;
import org.wikivoyage.ru.listings.input.template.RussianRoadTemplateParser;
import org.wikivoyage.ru.listings.input.template.TemplateParser;
import org.wikivoyage.ru.listings.utils.StringUtils;

/**
 * Parser of a single Wikivoyage page
 */
public class PageParser {

    private Language language;
    private final TemplateParser [] templateParsers = {
        new FrenchPrixTemplateParser(),
        new RussianRoadTemplateParser()
    };

	/**
     * Set of allowed templates used in Wikivoyage pages for listings
     */
    private HashSet<String> listingTemplates;
    
    public PageParser(Language language)
    {
    	this.language = language;
        initListingTemplates();
    }

    /**
     * Parse single Wikivoyage page, look for listings, put them into list of POIs
     * @param article Name of Wikivoyage article
     * @param text Wikivoyage page as string
     */
    public List<WikivoyagePOI> parsePage(String article, String text) {
        LinkedList<WikivoyagePOI> pois = new LinkedList<>();
        try {
            ParserConfig config = new SimpleParserConfig();
            WikitextPreprocessor p = new WikitextPreprocessor(config);
            WtNode node = p.parseArticle(text, "");
            processNode(article, node, pois);
        } catch (Exception e) {
            System.err.println("Failure");
            e.printStackTrace();
        }
        return pois;
    }

    private void processNode(String article, WtNode node, List<WikivoyagePOI> pois) throws StringConversionException
    {
        for (WtNode childNode: node) {
            if (childNode instanceof WtTemplate) {
                WtTemplate templateNode = (WtTemplate) childNode;
                String templateName = convertWtNodeToString(templateNode.getName()).trim();

                if (listingTemplates.contains(templateName)) {
                    HashMap<String, String> args = getTemplateArgumentsDict(templateNode);
                    if (args.containsKey(language.getNameElement())) {
                        
                        WikivoyagePOI poi = language.parseArgumentsDict(article, templateName, args);
                        if(poi != null) {
                        	pois.add(poi);
                        }
                    }
                }
            } else {
                processNode(article, childNode, pois);
            }
        }
    }

    /**
     * Get arguments of template as key-value dictionary (hash map)
     */
    private HashMap<String, String> getTemplateArgumentsDict(WtTemplate templateNode) {
        HashMap<String, String> templateArgumentsDict = new LinkedHashMap<>();

        for (WtNode templateArgumentsChildNode : templateNode.getArgs()) {
            if (templateArgumentsChildNode instanceof WtTemplateArgument) {
                WtTemplateArgument templateArgument = (WtTemplateArgument) templateArgumentsChildNode;

                String name = convertWtNodeToString(templateArgument.getName()).trim();
                String value = convertWtNodeToString(templateArgument.getValue()).trim();
                templateArgumentsDict.put(name, value);
            }
        }

        return templateArgumentsDict;
    }

    /**
     * Simple text conversion of WtNode object to string.
     * It ignores templates and presents their contents as plain text, with no conversion.
     */
    public String convertWtNodeToString(WtNode node)
    {
        if (node instanceof WtTemplate) {
            WtTemplate templateNode = (WtTemplate) node;
            String templateName = convertWtNodeToString(templateNode.getName()).trim();

            for (TemplateParser parser: templateParsers) {
                if (StringUtils.equalsCaseInsensitive(templateName, parser.getTemplateName())) {
                    return parser.parse(templateNode);
                }
            }

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

    private void initListingTemplates() {
        listingTemplates = language.getListingTemplates();
    }
}
