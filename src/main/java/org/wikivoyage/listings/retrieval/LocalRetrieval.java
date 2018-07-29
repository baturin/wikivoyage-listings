package org.wikivoyage.listings.retrieval;

import java.nio.file.Path;
import java.nio.file.Paths;

public class LocalRetrieval extends RetrievalStrategy {

    public LocalRetrieval(String whereToGet) {
        super(whereToGet);
    }

    @Override
    public Path retrieve() {
        return Paths.get(whereToStore);
    }
}
