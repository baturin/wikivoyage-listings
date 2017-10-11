package org.wikivoyage.listings;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Map;

import org.junit.Test;
import org.wikivoyage.listings.entity.Listing;
import org.wikivoyage.listings.validators.EmailValidator;
import org.wikivoyage.listings.validators.WebsiteURLValidator;
import org.wikivoyage.listings.validators.WikidataBulkValidator;

public class ValidationTests {
    
    @Test
    public void validURLs() {
        assertNull(new WebsiteURLValidator().validate(TestWikivoyagePOI.createWithURL("http://wikivoyage.org")));
        assertNull(new WebsiteURLValidator().validate(TestWikivoyagePOI.createWithURL("https://wikivoyage.org")));
        assertNull(new WebsiteURLValidator().validate(TestWikivoyagePOI.createWithURL("http://wikivoyage.org/")));
        assertNull(new WebsiteURLValidator().validate(TestWikivoyagePOI.createWithURL("http://www.westinmalta.com")));
        assertNull(new WebsiteURLValidator().validate(TestWikivoyagePOI.createWithURL("http://www.nix.de")));
        assertNull(new WebsiteURLValidator().validate(TestWikivoyagePOI.createWithURL("http://www.kitaena.co.jp/info/馬籠線.pdf"))); // Should not that be considered valid? It works in browsers
        assertNull(new WebsiteURLValidator().validate(TestWikivoyagePOI.createWithURL("http://www.ville-douai.fr/index.php/Théâtre?idpage=14004")));
        assertNull(new WebsiteURLValidator().validate(TestWikivoyagePOI.createWithURL("http://www.kitaena.co.jp/info/馬籠線.pdf")));
        assertNull(new WebsiteURLValidator().validate(TestWikivoyagePOI.createWithURL("http://www.natuurenbos.be/nl-BE/Domeinen/Vlaams-Brabant/arborétum_Heverleebos.aspx")));
        assertNull(new WebsiteURLValidator().validate(TestWikivoyagePOI.createWithURL("http://www.some-url.de?q=glück")));
    }

    @Test
    public void invalidURLs() {
        assertNotNull(new WebsiteURLValidator().validate(TestWikivoyagePOI.createWithURL("\"http://wikivoyage.org")));
        assertNotNull(new WebsiteURLValidator().validate(TestWikivoyagePOI.createWithURL("[http://wikivoyage.org")));
        assertNotNull(new WebsiteURLValidator().validate(TestWikivoyagePOI.createWithURL("http://wikivoyage.org]")));
        assertNotNull(new WebsiteURLValidator().validate(TestWikivoyagePOI.createWithURL("wikivoyage.org")));
        assertNotNull(new WebsiteURLValidator().validate(TestWikivoyagePOI.createWithURL("mary@wikivoyage.org")));
        assertNotNull(new WebsiteURLValidator().validate(TestWikivoyagePOI.createWithURL("ftp://wikivoyage.org]")));
    }
    
    @Test
    public void email() {
        // Valid
        assertNull(new EmailValidator().validate(TestWikivoyagePOI.createWithEmail("mary@wikivoyage.org")));
        // Invalid
        assertNotNull(new EmailValidator().validate(TestWikivoyagePOI.createWithEmail("\"mary@wikivoyage.org")));
        assertNotNull(new EmailValidator().validate(TestWikivoyagePOI.createWithEmail("mary@wikivoyage.org, jane@wikivoyage.org")));
        assertNotNull(new EmailValidator().validate(TestWikivoyagePOI.createWithEmail("mary@wikivoyage.org (reservations)")));
        assertNotNull(new EmailValidator().validate(TestWikivoyagePOI.createWithEmail("http://wikivoyage.org")));
    }
    
    @Test
    public void wikidata() {
        // Valid
        Listing q1 = TestWikivoyagePOI.createWithWikidata("Q42");
        // Invalid
        Listing q2 = TestWikivoyagePOI.createWithWikidata("Q42\"");
        Listing q3 = TestWikivoyagePOI.createWithWikidata("Q4 2");
        Listing q4 = TestWikivoyagePOI.createWithWikidata("QID42");
        Listing q5 = TestWikivoyagePOI.createWithWikidata("q42");
        
        // Run bulk validation
        WikidataBulkValidator validator = new WikidataBulkValidator();
        validator.add(q1);
        validator.add(q2);
        validator.add(q3);
        validator.add(q4);
        validator.add(q5);
        Map<Listing, String> results = validator.validate();
        
        // Check results
        assertNull(results.get(q1));
        assertNotNull(results.get(q2));
        assertNotNull(results.get(q3));
        assertNotNull(results.get(q4));
        assertNotNull(results.get(q5));
    }
}

/**
 * Helper class to conveniently create POI without the huge constructor.
 */
@SuppressWarnings("serial")
class TestWikivoyagePOI extends Listing {
    public TestWikivoyagePOI() {
        super ("", "", "", "", "", "", "", "", "", "",
                "", "", "", "", "","", "", "", "", "",
                "", "", "", "", "");
    }
    
    public static TestWikivoyagePOI createWithEmail(String email) {
        TestWikivoyagePOI poi = new TestWikivoyagePOI();
        poi.email = email;
        return poi;
    }
    
    public static TestWikivoyagePOI createWithWikidata(String wikidata) {
        TestWikivoyagePOI poi = new TestWikivoyagePOI();
        poi.wikidata = wikidata;
        return poi;
    }
    
    public static TestWikivoyagePOI createWithURL(String url) {
        TestWikivoyagePOI poi = new TestWikivoyagePOI();
        poi.url = url;
        return poi;
    }
}