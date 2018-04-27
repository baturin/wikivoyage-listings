package org.wikivoyage.listings.validators;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.validator.routines.UrlValidator;
import org.wikivoyage.listings.entity.Listing;

public class WebsiteURLValidator extends SimpleValidator {
    @Override
    public void validate(Listing poi) {
        if (poi.getUrl() != null && !poi.getUrl().equals("")) {
            if (!validWebsiteURL(poi.getUrl()) || !urlExists(poi.getUrl())) {
                poi.add(ValidationIssue.INVALID_URL);
            }
        }
    }

    public boolean urlExists(String urlString) {
    	URL url;
    	if (urlString.contains("{{dead link")) {
    	    return false;
    	}
		try {
			url = new URL(urlString);
			
			// We want to check the current URL
	        HttpURLConnection.setFollowRedirects(false);

	        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

	        // We don't need to get data
	        httpURLConnection.setRequestMethod("HEAD");

	        // Some websites don't like programmatic access so pretend to be a browser
	        httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.0; en-US; rv:1.9.1.2) Gecko/20090729 Firefox/3.5.2 (.NET CLR 3.5.30729)");
	        int responseCode = httpURLConnection.getResponseCode();

	        // We only accept response code 200
	        return responseCode == HttpURLConnection.HTTP_OK;
		} catch (IOException e) {
			return false;
		}
	}

	private boolean validWebsiteURL(String urlString) {
        try {
            UrlValidator validator = new UrlValidator(new String[] {"https", "http"});
            URL url = new URL(urlString);
            URI uri = new URI(url.getProtocol()
                    , url.getUserInfo()
                    , url.getHost()
                    , url.getPort()
                    , url.getPath()
                    , url.getQuery()
                    , url.getRef()
            );

            return validator.isValid(uri.toASCIIString());

        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }
}