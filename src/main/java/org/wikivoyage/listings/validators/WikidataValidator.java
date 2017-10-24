package org.wikivoyage.listings.validators;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.wikivoyage.listings.entity.Listing;

public class WikidataValidator implements Validator {
    private static final Log log = LogFactory.getLog(WikidataValidator.class);
    
    @Override
    public Iterable<Listing> validate(final Iterable<Listing> listingIterable) {
        return new Iterable<Listing>() {
            @Override
            public Iterator<Listing> iterator() {
                return new WikidataValidatorIterator(listingIterable.iterator());
            }
        };
    }
    
    private class WikidataValidatorIterator implements Iterator<Listing> {
        private Iterator<Listing> listingIterator;
        private Queue<Listing> outputBuffer = new LinkedList<>();
        
        public WikidataValidatorIterator(Iterator<Listing> listingIterator) {
            this.listingIterator = listingIterator;
        }
        
        @Override
        public boolean hasNext() {
            return !outputBuffer.isEmpty() || listingIterator.hasNext();
        }
        
        @Override
        public Listing next() {
            if (outputBuffer.isEmpty() && listingIterator.hasNext()) {
                outputBuffer.addAll(validateNextBatch());
            }
            return outputBuffer.remove();
        }
        
        private List<Listing> validateNextBatch() {
            List<Listing> batch = new ArrayList<>();
            List<Listing> existenceCheckList = new ArrayList<>();
            while (listingIterator.hasNext() && existenceCheckList.size() < 200) {
                Listing poi = listingIterator.next();
                batch.add(poi);
                if (poi.getWikidata() != null && !poi.getWikidata().equals("")) {
                    if (!poi.getWikidata().matches("Q\\d+")) {
                        // If QID does not pass basic validation...
                        poi.add(ValidationIssue.INVALID_WIKIDATA_QID);
                    } else {
                        // ...otherwise, add it to list to check existence
                        existenceCheckList.add(poi);
                    }
                }
            }
            checkExistence(existenceCheckList);
            return batch;
        }
        
        private void checkExistence(List<Listing> pois) {
            // Prepare SPARQL query String
            StringBuilder qids = new StringBuilder();
            for (Listing poi : pois) {
                qids.append(" wd:").append(poi.getWikidata());
            }
            String query = String.format(
                "SELECT ?item ?type WHERE {" +
                "  VALUES ?item { %s }" +
                "  {" + 
                "    ?item owl:sameAs [] ." +
                "    BIND('REDIRECT_WIKIDATA_QID' AS ?type) ." +
                "  } UNION {" +
                "    MINUS { ?item owl:sameAs [] }" +
                "    FILTER NOT EXISTS { ?item schema:version [] }" +
                "    BIND('INVALID_WIKIDATA_QID' AS ?type) ." + 
                "  }" +
                "}", qids.toString());
            // Run the query
            String json = null;
            try {
                json = executeSpaqrlQuery(query);
            } catch (IOException e) {
                throw new RuntimeException("Error executing SPARQL query", e);
            }
            JSONArray bindings = new JSONObject(json).getJSONObject("results").getJSONArray("bindings");
            // Remap query result bindings to validation results
            Map<String, Listing> qidPoiMap = new HashMap<>();
            for (Listing poi : pois) {
                qidPoiMap.put(poi.getWikidata(), poi);
            }
            for (int index = 0; index < bindings.length(); index++) {
                JSONObject binding =  bindings.getJSONObject(index);
                String itemUri = binding.getJSONObject("item").getString("value");
                String qid = itemUri.substring(itemUri.indexOf("www.wikidata.org/entity/Q") + 24); // parse itemUri to QID
                String resultType = binding.getJSONObject("type").getString("value"); // resultType will be either INVALID_WIKIDATA_QID or REDIRECT_WIKIDATA_QID
                Listing poi = qidPoiMap.get(qid);
                poi.add(ValidationIssue.valueOf(resultType));
            }
        }
        
        private String executeSpaqrlQuery(String query) throws IOException {
            log.debug("Execute SPARQL query:\n" + query);
            // Prepare SPARQL service GET request URL
            URL url = new URL("https://query.wikidata.org/sparql?format=json&query=" + URLEncoder.encode(query, "UTF-8"));
            InputStream inputStream = url.openConnection().getInputStream();
            // Read response from input stream and write to output stream through a byte buffer
            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }
            return result.toString("UTF-8");
        }
    }
}
