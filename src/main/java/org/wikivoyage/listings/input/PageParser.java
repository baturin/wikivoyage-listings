package org.wikivoyage.listings.input;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sweble.wikitext.parser.ParserConfig;
import org.sweble.wikitext.parser.WikitextPreprocessor;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtTemplate;
import org.sweble.wikitext.parser.utils.SimpleParserConfig;
import org.sweble.wikitext.parser.utils.StringConversionException;
import org.wikivoyage.listings.entity.WikivoyagePOI;
import org.wikivoyage.listings.input.template.*;

import org.wikivoyage.listings.language.Language;

/**
 * Parser of a single Wikivoyage page
 */
public class PageParser {
    private static final Log log = LogFactory.getLog(PageParser.class);

    private Language language;

	/**
     * Set of allowed templates used in Wikivoyage pages for listings
     */
    private HashSet<String> listingTemplates;
    
    public PageParser(Language language)
    {
    	this.language = language;
        listingTemplates = language.getListingTemplates();
    }

    /**
     * Parse single Wikivoyage page, look for listings, put them into list of POIs
     * @param article Name of Wikivoyage article
     * @param text Wikivoyage page as string
     */
    public List<WikivoyagePOI> parsePage(String article, String text) {
        log.debug("Start: parse article '" + article + "'");

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
        log.debug("End: parse article '" + article + "'");
        return pois;
    }

    private void processNode(String article, WtNode node, List<WikivoyagePOI> pois) throws StringConversionException
    {
        for (WtNode childNode: node) {
            if (childNode instanceof WtTemplate) {
                TemplateNode template = new TemplateNode(
                    language.getLanguageCode(), (WtTemplate) childNode, language.getTemplateConverters()
                );

                if (listingTemplates.contains(template.getNameLowercase())) {
                    if (template.hasArgument(language.getNameElement())) {
                        WikivoyagePOI poi = language.parseListingTemplate(article, template);
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
}
