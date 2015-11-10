package org.wikivoyage.ru.listings.input;

import de.fau.cs.osr.ptk.common.ast.AstStringNode;
import org.sweble.wikitext.parser.ParserConfig;
import org.sweble.wikitext.parser.WikitextPreprocessor;
import org.sweble.wikitext.parser.nodes.*;
import org.sweble.wikitext.parser.utils.SimpleParserConfig;
import org.sweble.wikitext.parser.utils.StringConversionException;
import org.wikivoyage.ru.listings.entity.WikivoyagePOI;

import java.util.*;

/**
 * Parser of a single Wikivoyage page
 */
public class PageParser {

	/**
     * Set of allowed templates used in Wikivoyage pages for listings
     */
    private HashSet<String> listingTemplates;

    public PageParser()
    {
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
                    if (args.containsKey("name") && args.containsKey("lat") && args.containsKey("long")) {
                        
                        WikivoyagePOI poi = parseArgumentsDict(article, templateName, args);
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
    
    private WikivoyagePOI parseArgumentsDict(
    		String article, String templateName, HashMap<String, String> args) {

        // Type
        String poiType;
        if (templateName.equals("listing")) {
            if (args.containsKey("type")) {
                poiType = args.get("type");
            } else {
                poiType = "other";
            }
        } else {
            poiType = templateName;
        }
        
        // Description
        String description = "";
        if (args.containsKey("description")) {
            description = args.get("description");
        } else if (args.containsKey("content")) {
            description = args.get("content");
        }

    	return new WikivoyagePOI(
                article,
                poiType,
                args.get("name"),
                args.get("alt"),
                args.get("address"),
                args.get("directions"),
                args.get("phone"),
                args.get("tollFree"),
                args.get("email"),
                args.get("fax"),
                args.get("url"),
                args.get("hours"),
                args.get("checkin"),
                args.get("checkout"),
                args.get("image"),
                args.get("price"),
                args.get("lat"),
                args.get("long"),
                description);
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

    private List<String> getTemplateArgumentValuesList(WtTemplate templateNode) {
        LinkedList<String> result = new LinkedList<>();

        for (WtNode templateArgumentsChildNode : templateNode.getArgs()) {
            if (templateArgumentsChildNode instanceof WtTemplateArgument) {
                WtTemplateArgument templateArgument = (WtTemplateArgument) templateArgumentsChildNode;

                String value = convertWtNodeToString(templateArgument.getValue()).trim();
                result.add(value);
            }
        }

        return result;
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
            if (templateName.equals("Российская трасса")) {
                String roadTitle = "";
                for (String arg: getTemplateArgumentValuesList(templateNode)) {
                    roadTitle += arg;
                }
                return roadTitle;
            } else if (node instanceof AstStringNode) {
                return ((AstStringNode) node).getContent();
            } else {
                return "";
            }
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
        listingTemplates = new HashSet<>();
        listingTemplates.add("listing");
        listingTemplates.add("see");
        listingTemplates.add("do");
        listingTemplates.add("buy");
        listingTemplates.add("eat");
        listingTemplates.add("drink");
        listingTemplates.add("sleep");
    }
}
