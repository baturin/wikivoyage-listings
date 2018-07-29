package org.wikivoyage.listings.retrieval;

import java.nio.file.Path;

public abstract class RetrievalStrategy {

    protected String url;
    public String whereToStore;

    public RetrievalStrategy(String whereToStore) {
        this.whereToStore = whereToStore;
    }

    public abstract Path retrieve();

}
