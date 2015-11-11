package org.wikivoyage.ru.listings.entity;

/**
 * Entity representing Wikivoyage article in dump file
 */
public class DumpArticle {
    String title;
    String text;

    public DumpArticle(String title, String text)
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
