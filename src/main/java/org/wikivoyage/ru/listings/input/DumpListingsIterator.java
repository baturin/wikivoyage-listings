package org.wikivoyage.ru.listings.input;

import org.wikivoyage.ru.listings.entity.DumpArticle;
import org.wikivoyage.ru.listings.entity.WikivoyagePOI;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterate over listings in Wikivoyage database dump
 */
public class DumpListingsIterator implements Iterator<WikivoyagePOI>, Iterable<WikivoyagePOI> {
    PageParser pageParser;
    DumpArticlesIterator articlesIterator;
    Iterator<WikivoyagePOI> currentArticleListingIterator;
    WikivoyagePOI currentListing;

    public DumpListingsIterator(String filename) {
        articlesIterator = new DumpArticlesIterator(filename);
        pageParser = new PageParser();
        getNext();
    }

    private void getNext()
    {
        currentListing = null;
        while (currentArticleListingIterator == null || !currentArticleListingIterator.hasNext()) {
            if (!articlesIterator.hasNext()) {
                return;
            }
            DumpArticle article = articlesIterator.next();
            currentArticleListingIterator = pageParser.parsePage(article.getTitle(), article.getText()).iterator();
        }
        currentListing = currentArticleListingIterator.next();
    }

    @Override
    public boolean hasNext() {
        return currentListing != null;
    }

    @Override
    public WikivoyagePOI next() {
        WikivoyagePOI result = currentListing;
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
    public Iterator<WikivoyagePOI> iterator() {
        return this;
    }
}
