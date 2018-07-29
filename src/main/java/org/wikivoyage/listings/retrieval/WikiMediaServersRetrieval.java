package org.wikivoyage.listings.retrieval;

import lombok.Setter;
import org.wikivoyage.listings.input.DumpDownloader;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class WikiMediaServersRetrieval extends RetrievalStrategy {

    DumpDownloader downloader = new DumpDownloader();

    @Setter List<String> toDownload = new ArrayList<>();

    public WikiMediaServersRetrieval(String toRetrieveFrom, String whereToStore) {
        super(whereToStore);
        this.url = toRetrieveFrom;
    }

    @Override
    public Path retrieve() {
        try {
            downloader.downloadAndStoreDumpFromUrl(url, whereToStore);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return Paths.get(whereToStore);
    }
}