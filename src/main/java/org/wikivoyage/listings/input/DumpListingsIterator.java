package org.wikivoyage.listings.input;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wikivoyage.listings.entity.Article;
import org.wikivoyage.listings.entity.Listing;

import org.wikivoyage.listings.language.Language;
import org.wikivoyage.listings.language.Languages;

/**
 * Iterate over listings in Wikivoyage database dump
 */
public class DumpListingsIterator implements Iterator<Listing>, Iterable<Listing> {
    private static final Log log = LogFactory.getLog(DumpListingsIterator.class);

    private ArticleParser pageParser;
    private DumpArticlesIterator articlesIterator;
    private Iterator<Listing> currentArticleListingIterator;
    private Listing currentListing;

    public DumpListingsIterator(String filename) {
        articlesIterator = new DumpArticlesIterator(filename);
        log.debug("Detected language code for dump '" + filename + "': " + articlesIterator.getLanguageCode());
        Language language = Languages.create(articlesIterator.getLanguageCode());
        pageParser = new ArticleParser(language);
        getNext();
    }

    private void getNext()
    {
        currentListing = null;
        while (currentArticleListingIterator == null || !currentArticleListingIterator.hasNext()) {
            if (!articlesIterator.hasNext()) {
                return;
            }
            Article article = articlesIterator.next();
            currentArticleListingIterator = pageParser.parsePage(article.getTitle(), article.getText()).iterator();
        }
        currentListing = currentArticleListingIterator.next();
    }

    @Override
    public boolean hasNext() {
        return currentListing != null;
    }

    @Override
    public Listing next() {
        Listing result = currentListing;
        if (currentListing == null) {
            throw new NoSuchElementException();
        }
        getNext();
        return result;
    }

    @Override
    public void remove() {
        throw  new UnsupportedOperationException();
    }

    @Override
    public Iterator<Listing> iterator() {
        return this;
    }
}
