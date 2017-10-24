package org.wikivoyage.listings;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Iterator;

import org.junit.Test;
import org.wikivoyage.listings.entity.Listing;
import org.wikivoyage.listings.validators.EmailValidator;
import org.wikivoyage.listings.validators.WebsiteURLValidator;
import org.wikivoyage.listings.validators.WikidataValidator;

public class ValidationTests {
    
    @Test
    public void validURLs() {
        Listing p1 = TestWikivoyagePOI.createWithURL("http://wikivoyage.org");
        Listing p2 = TestWikivoyagePOI.createWithURL("https://wikivoyage.org");
        Listing p3 = TestWikivoyagePOI.createWithURL("http://wikivoyage.org/");
        Listing p4 = TestWikivoyagePOI.createWithURL("http://www.westinmalta.com");
        Listing p5 = TestWikivoyagePOI.createWithURL("http://www.nix.de");
        Listing p6 = TestWikivoyagePOI.createWithURL("http://www.kitaena.co.jp/info/馬籠線.pdf"); // Should not that be considered valid? It works in browsers
        Listing p7 = TestWikivoyagePOI.createWithURL("http://www.ville-douai.fr/index.php/Théâtre?idpage=14004");
        Listing p8 = TestWikivoyagePOI.createWithURL("http://www.kitaena.co.jp/info/馬籠線.pdf");
        Listing p9 = TestWikivoyagePOI.createWithURL("http://www.natuurenbos.be/nl-BE/Domeinen/Vlaams-Brabant/arborétum_Heverleebos.aspx");
        Listing p10 = TestWikivoyagePOI.createWithURL("http://www.some-url.de?q=glück");
        
        // Prepare validation iterable
        Iterable<Listing> pois = new WebsiteURLValidator().validate(Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10));
        
        // Check results
        for (Listing poi : pois) {
            assertTrue(poi.isValid());
        }
    }

    @Test
    public void invalidURLs() {
        Listing p1 = TestWikivoyagePOI.createWithURL("\"http://wikivoyage.org");
        Listing p2 = TestWikivoyagePOI.createWithURL("[http://wikivoyage.org");
        Listing p3 = TestWikivoyagePOI.createWithURL("http://wikivoyage.org]");
        Listing p4 = TestWikivoyagePOI.createWithURL("wikivoyage.org");
        Listing p5 = TestWikivoyagePOI.createWithURL("mary@wikivoyage.org");
        Listing p6 = TestWikivoyagePOI.createWithURL("ftp://wikivoyage.org]");
        
        // Prepare validation iterable
        Iterable<Listing> pois = new WebsiteURLValidator().validate(Arrays.asList(p1, p2, p3, p4, p5, p6));
        
        // Check results
        for (Listing poi : pois) {
            assertFalse(poi.isValid());
        }
    }
    
    @Test
    public void email() {
        // Valid
        Listing p1 = TestWikivoyagePOI.createWithEmail("mary@wikivoyage.org");
        // Invalid
        Listing p2 = TestWikivoyagePOI.createWithEmail("\"mary@wikivoyage.org");
        Listing p3 = TestWikivoyagePOI.createWithEmail("mary@wikivoyage.org, jane@wikivoyage.org");
        Listing p4 = TestWikivoyagePOI.createWithEmail("mary@wikivoyage.org (reservations)");
        Listing p5 = TestWikivoyagePOI.createWithEmail("http://wikivoyage.org");
        
        // Prepare validation iterator
        Iterator<Listing> validationIterator = 
            new EmailValidator().validate(Arrays.asList(p1, p2, p3, p4, p5)).iterator();
        
        // Check results
        assertTrue(validationIterator.next().isValid());
        assertFalse(validationIterator.next().isValid());
        assertFalse(validationIterator.next().isValid());
        assertFalse(validationIterator.next().isValid());
        assertFalse(validationIterator.next().isValid());
    }
    
    @Test
    public void wikidata() {
        // Valid
        Listing p1 = TestWikivoyagePOI.createWithWikidata("Q42");
        // Invalid
        Listing p2 = TestWikivoyagePOI.createWithWikidata("Q42\"");
        Listing p3 = TestWikivoyagePOI.createWithWikidata("Q4 2");
        Listing p4 = TestWikivoyagePOI.createWithWikidata("QID42");
        Listing p5 = TestWikivoyagePOI.createWithWikidata("q42");
        
        // Prepare validation iterator
        Iterator<Listing> validationIterator = 
            new WikidataValidator().validate(Arrays.asList(p1, p2, p3, p4, p5)).iterator();
        
        // Check results
        assertTrue(validationIterator.next().isValid());
        assertFalse(validationIterator.next().isValid());
        assertFalse(validationIterator.next().isValid());
        assertFalse(validationIterator.next().isValid());
        assertFalse(validationIterator.next().isValid());
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