package org.wikivoyage.listings.input;

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
import org.wikivoyage.listings.entity.Listing;
import org.wikivoyage.listings.input.template.TemplateNode;
import org.wikivoyage.listings.language.Language;

/**
 * Parser of a single Wikivoyage article
 */
public class ArticleParser {
    private static final Log log = LogFactory.getLog(ArticleParser.class);

    private Language language;
    
    public ArticleParser(Language language)
    {
    	this.language = language;
    }

    /**
     * Parse single Wikivoyage page, look for listings, put them into list of POIs
     * @param article Name of Wikivoyage article
     * @param text Wikivoyage page as string
     */
    public List<Listing> parsePage(String article, String text) {
        log.debug("Start: parse article '" + article + "'");

        LinkedList<Listing> pois = new LinkedList<>();
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

    private void processNode(String article, WtNode node, List<Listing> pois) throws StringConversionException
    {
    		String nextPoiType = null;
        for (WtNode childNode: node) {
            if (childNode instanceof WtTemplate) {
                TemplateNode template = new TemplateNode(
                    language.getLanguageCode(), (WtTemplate) childNode, language.getTemplateConverters()
                );
               
                if (language.getListingTemplates().contains(template.getNameLowercase())) {
				    	if (template.hasArgument(language.getNameElement())) {   		
				    		Listing poi = language.parseListingTemplate(article, template, nextPoiType);
				    		if (nextPoiType != null) {
				    			nextPoiType = null;
				    		}
				        if(poi != null) {
				        		pois.add(poi);
				        }
				    	}
                } 
                /*
                 * If I encounter the {flag} element, the next listing is a diplomatic-representation
                 */
                else if (template.getNameLowercase().equals(language.getFlagElement())) {
					nextPoiType = "diplomatic-representation";
			    }
                
                
            } else {
                processNode(article, childNode, pois);
            }
        }
    }
}
