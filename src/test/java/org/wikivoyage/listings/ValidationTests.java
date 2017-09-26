package org.wikivoyage.listings;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.wikivoyage.listings.entity.Listing;
import org.wikivoyage.listings.validators.*;

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
        assertNull(new WikidataValidator().validate(TestWikivoyagePOI.createWithWikidata("Q42")));
        // Invalid
        assertNotNull(new WikidataValidator().validate(TestWikivoyagePOI.createWithWikidata("Q42\"")));
        assertNotNull(new WikidataValidator().validate(TestWikivoyagePOI.createWithWikidata("Q4 2")));
        assertNotNull(new WikidataValidator().validate(TestWikivoyagePOI.createWithWikidata("QID42")));
        assertNotNull(new WikidataValidator().validate(TestWikivoyagePOI.createWithWikidata("q42")));
    }

    @Test
    public void validLatitudeTests() {
        LatitudeValidator validator = new LatitudeValidator();

        assertNull(validator.validate(TestWikivoyagePOI.createWithLatitude("")));
        assertNull(validator.validate(TestWikivoyagePOI.createWithLatitude("57.058889")));
        assertNull(validator.validate(TestWikivoyagePOI.createWithLatitude("41°55'49.4\"N")));
    }

    @Test
    public void invalidLatitudeTests() {
        LatitudeValidator validator = new LatitudeValidator();

        assertNotNull(validator.validate(TestWikivoyagePOI.createWithLatitude("-90.1")));
        assertNotNull(validator.validate(TestWikivoyagePOI.createWithLatitude("not a coordinate")));
        assertNotNull(validator.validate(TestWikivoyagePOI.createWithLatitude("90.005")));
    }

    @Test
    public void validLongitudeTests() {
        LongitudeValidator validator = new LongitudeValidator();

        assertNull(validator.validate(TestWikivoyagePOI.createWithLongitude("")));
        assertNull(validator.validate(TestWikivoyagePOI.createWithLongitude("-9.920728")));
        assertNull(validator.validate(TestWikivoyagePOI.createWithLongitude("179.920728")));
        assertNull(validator.validate(TestWikivoyagePOI.createWithLongitude("17°38'15.60\"E")));
    }

    @Test
    public void invalidLongitudeTests() {
        LongitudeValidator validator = new LongitudeValidator();

        assertNotNull(validator.validate(TestWikivoyagePOI.createWithLongitude("-181.23")));
        assertNotNull(validator.validate(TestWikivoyagePOI.createWithLongitude("-180.00005")));
        assertNotNull(validator.validate(TestWikivoyagePOI.createWithLongitude("not a longitude")));
        assertNotNull(validator.validate(TestWikivoyagePOI.createWithLongitude("123,345,567")));
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

    public static TestWikivoyagePOI createWithLongitude(String longitude) {
        TestWikivoyagePOI poi = new TestWikivoyagePOI();
        poi.longitude = longitude;
        return poi;
    }

    public static TestWikivoyagePOI createWithLatitude(String latitude) {
        TestWikivoyagePOI poi = new TestWikivoyagePOI();
        poi.latitude = latitude;
        return poi;
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