package org.wikivoyage.listings.validators;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.wikivoyage.listings.entity.Listing;

public class WikidataBulkValidator implements BulkValidator {
    private static final Log log = LogFactory.getLog(WikidataBulkValidator.class);
    
    private Map<Listing, String> results = new HashMap<>();
    private List<Listing> existenceCheckList = new ArrayList<>();
    
    @Override
    public void add(Listing poi) {
        if (poi.getWikidata() != null && !poi.getWikidata().equals("")) {
            if (!poi.getWikidata().matches("Q\\d+")) {
                // If QID does not pass basic validation, add error to results...
                results.put(poi, "Invalid QID '" + poi.getWikidata() + "'");
            } else {
                // ...otherwise, add it to list to check existence
                existenceCheckList.add(poi);
                if (existenceCheckList.size() == 200) {
                    // every 200 QIDs, run existence check, and empty the list
                    results.putAll(checkExistence(existenceCheckList));
                    existenceCheckList.clear();
                }
            }
        }
    }
    
    @Override
    public Map<Listing, String> validate() {
        if (!existenceCheckList.isEmpty()) {
            // If there are some QIDs left in list, check them before we return the whole results Map 
            results.putAll(checkExistence(existenceCheckList));
            existenceCheckList.clear();
        }
        return results;
    }

    @Override
    public String getIssueType() {
        return "Wikidata QID";
    }
    
    private Map<Listing, String> checkExistence(Iterable<Listing> pois) {
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
            "    BIND('Redirect' AS ?type) ." +
            "  } UNION {" +
            "    MINUS { ?item owl:sameAs [] }" +
            "    FILTER NOT EXISTS { ?item schema:version [] }" +
            "    BIND('Invalid' AS ?type) ." + 
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
        Map<Listing, String> validationResults = new HashMap<>();
        for (int index = 0; index < bindings.length(); index++) {
            JSONObject binding =  bindings.getJSONObject(index);
            String itemUri = binding.getJSONObject("item").getString("value");
            String qid = itemUri.substring(itemUri.indexOf("www.wikidata.org/entity/Q") + 24); // parse itemUri to QID
            String resultType = binding.getJSONObject("type").getString("value"); // resultType will be either Invalid or Redirect
            validationResults.put(qidPoiMap.get(qid), resultType + " QID '" + qid + "'");
        }
        return validationResults;
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
