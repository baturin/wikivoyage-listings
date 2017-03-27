package org.wikivoyage.listings.entity;

import java.io.Serializable;

/**
 * Entity representing Wikivoyage article in dump file
 */
public class Article implements Serializable {
    
    private static final long serialVersionUID = 3567768144708772742L;
    
    /**
     * Title of the Wikivoyage article, for instance "Tokyo/Minato".
     */
    String title;
    
    /**
     * Wikicode text of the Wikivoyage article.
     */
    String text;

    public Article(String title, String text)
    {
        this.title = title;
        this.text = text;
    }

    /**
     * Get title of the article
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Get raw text of the article (templates are not expanded)
     */
    public String getText()
    {
        return text;
    }
}
