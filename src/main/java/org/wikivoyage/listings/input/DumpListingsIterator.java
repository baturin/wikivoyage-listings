package org.wikivoyage.listings.input;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.wikivoyage.listings.entity.DumpArticle;
import org.wikivoyage.listings.entity.WikivoyagePOI;

import org.wikivoyage.listings.language.Language;

/**
 * Iterate over listings in Wikivoyage database dump
 */
public class DumpListingsIterator implements Iterator<WikivoyagePOI>, Iterable<WikivoyagePOI> {
    PageParser pageParser;
    DumpArticlesIterator articlesIterator;
    Iterator<WikivoyagePOI> currentArticleListingIterator;
    WikivoyagePOI currentListing;

    public DumpListingsIterator(String filename, Language language) {
        articlesIterator = new DumpArticlesIterator(filename);
        pageParser = new PageParser(language);
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
