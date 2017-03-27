package org.wikivoyage.listings;

import java.io.IOException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.wikivoyage.listings.entity.Listing;
import org.wikivoyage.listings.input.ArticleParser;
import org.wikivoyage.listings.language.Language;
import org.wikivoyage.listings.language.english.English;
import org.wikivoyage.listings.language.french.French;
import org.wikivoyage.listings.language.german.German;
import org.wikivoyage.listings.language.russian.Russian;


public class InputTests {
    
	@Test
	public void processEnglish() throws Exception {
        List<Listing> pois = parseResourcePOIs("sample-article-en.wikicode", new English(), "Tokyo/Roppongi");

		// Check number of POIs
		Assert.assertEquals(71, pois.size());

		// Check a particular POI in detail
		Listing poi = pois.get(68);
		Assert.assertEquals("Tokyo/Roppongi", poi.getArticle());
		Assert.assertEquals("sleep", poi.getType());
		Assert.assertEquals("Grand Hyatt Tokyo", poi.getTitle());
		Assert.assertEquals("", poi.getAlt());
		Assert.assertEquals("6-10-3 Roppongi", poi.getAddress());
		Assert.assertEquals("In Roppongi Hills", poi.getDirections());
		Assert.assertEquals("+81 3 4333-1234", poi.getPhone());
		Assert.assertEquals("", poi.getTollFree());
		Assert.assertEquals("", poi.getEmail());
		Assert.assertEquals("+81 3 4333-8123", poi.getFax());
		Assert.assertEquals("http://tokyo.grand.hyatt.com/", poi.getUrl());
		Assert.assertEquals("", poi.getHours());
		Assert.assertEquals("15:00", poi.getCheckIn());
		Assert.assertEquals("12:00", poi.getCheckOut());
		Assert.assertEquals("Grand Hyatt Tokyo.JPG", poi.getImage());
		Assert.assertEquals("Rack rates marginally cheaper than the Park Hyatt at ¥37,000 and up", poi.getPrice());
		Assert.assertEquals("35.65968", poi.getLatitude());
		Assert.assertEquals("139.72814", poi.getLongitude());
		Assert.assertEquals(
            "A part of Roppongi Hills, and not to be confused with the more famous Park Hyatt " +
            "of ''Lost in Translation'' fame, which is in Shinjuku. Sleek and minimalistic, " +
            "all black, gray and brown, with expensive design that never hesitates to sacrifice " +
            "function for form, but the superlative service makes up for it.",
            poi.getDescription()
        );
	}
	
    @Test
    public void processGerman() throws Exception {
        List<Listing> pois = parseResourcePOIs("sample-article-de.wikicode", new German(), "Karwendel");

        // Check number of POIs
        Assert.assertEquals(69, pois.size());

        // Check a particular POI in detail
        Listing poi = pois.get(60);
        Assert.assertEquals("Karwendel", poi.getArticle());
        Assert.assertEquals("sleep", poi.getType());
        Assert.assertEquals("Hochlandhütte", poi.getTitle());
        Assert.assertEquals("", poi.getAlt());
        Assert.assertEquals("Schöttlkarstr. 10, 82481 Mittenwald", poi.getAddress());
        Assert.assertEquals("", poi.getDirections());
        Assert.assertEquals("0174 9897863", poi.getPhone());
        Assert.assertEquals("", poi.getTollFree());
        Assert.assertEquals("", poi.getEmail());
        Assert.assertEquals("", poi.getFax());
        Assert.assertEquals("http://www.hochlandhuette.de/", poi.getUrl());
        Assert.assertEquals("von Ende Mai bis Mitte Oktober, im Winter wegen Lawinengefahr geschlossen", poi.getHours());
        Assert.assertEquals("", poi.getCheckIn());
        Assert.assertEquals("", poi.getCheckOut());
        Assert.assertEquals("", poi.getImage());
        Assert.assertEquals("", poi.getPrice());
        Assert.assertEquals("47.44976", poi.getLatitude());
        Assert.assertEquals("11.31884", poi.getLongitude());
        Assert.assertEquals("", poi.getDescription());
    }

	@Test
	public void processFrench() throws Exception {
        List<Listing> pois = parseResourcePOIs("sample-article-fr.wikicode", new French(), "Thouars");

		// Check number of POIs
		Assert.assertEquals(28, pois.size());

		// Check a particular POI in detail
		Listing poi = pois.get(25);
		Assert.assertEquals("Thouars", poi.getArticle());
		Assert.assertEquals("sleep", poi.getType());
		Assert.assertEquals("Camping municipal", poi.getTitle());
		Assert.assertEquals("", poi.getAlt());
		Assert.assertEquals("13 Rue de la Grande Côte de Crevant", poi.getAddress());
		Assert.assertEquals("", poi.getDirections());
		Assert.assertEquals("+33 5 49 66 17 99", poi.getPhone());
		Assert.assertEquals("", poi.getTollFree());
		Assert.assertEquals("", poi.getEmail());
		Assert.assertEquals("", poi.getFax());
		Assert.assertEquals("http://www.ville-thouars.fr/decouvrir/camping.htm", poi.getUrl());
		Assert.assertEquals("", poi.getHours());
		Assert.assertEquals("", poi.getCheckIn());
		Assert.assertEquals("", poi.getCheckOut());
		Assert.assertEquals("", poi.getImage());
		Assert.assertEquals("3.2€", poi.getPrice());
		Assert.assertEquals("46.979967", poi.getLatitude());
		Assert.assertEquals("-0.219622", poi.getLongitude());
		Assert.assertEquals(
            "Situé au bord de la rivière, immédiatement en contrebas du Parc Imbert et du vieux " +
            "centre ville. 9 h - 12 h et 15 h 30 - 19 h 30, seulement l'été.",
            poi.getDescription()
        );
	}

	@Test
	public void processFrenchPrixTemplate() throws Exception {
		Listing poi = parseResourceSinglePOI("prix-template.wikicode", new French());
		Assert.assertEquals(
			poi.getPrice(),
			"1.50€ à 4€. Entrée gratuite pour les enfants jusqu’à 12 ans, gratuit le premier dimanche du mois"
		);
	}

    @Test
    public void processFrenchHoraireTemplate() throws Exception {
        List<Listing> pois = parseResourcePOIs("horaire-template.wikicode", new French());
        Assert.assertEquals(5, pois.size());
        Assert.assertEquals("lun. - jeu.: 8 h 30 - 22 h", pois.get(0).getHours());
        Assert.assertEquals("8 h 30 - 22 h", pois.get(1).getHours());
        Assert.assertEquals("8 h 30", pois.get(2).getHours());
        Assert.assertEquals("lun. - jeu.", pois.get(3).getHours());
        Assert.assertEquals("lun. - jeu.: 8 h 30 - 12 h 30 et 13 h - 15 h 45", pois.get(4).getHours());
    }

	@Test
	public void processRussianRoadTemplate() throws Exception {
		Listing poi = parseResourceSinglePOI("russian-road-template.wikicode", new Russian());
		Assert.assertEquals(
			"На 38 км Ново-Рижского шоссе М9 (19 км от МКАД) " +
			"свернуть по указателю на пирамиду.",
            poi.getDirections()
		);
	}

	@Test
	public void processUnknownTemplate() throws Exception {
        Listing poi = parseResourceSinglePOI("unknown-template.wikicode");
        Assert.assertEquals(
            "Description with some {{unknown|template}} and another {{unknown|name=template}}",
            poi.getDescription()
        );
	}

	@Test
    public void processHTMLComments() throws Exception {
        Listing poi = parseResourceSinglePOI("html-comment.wikicode");
        Assert.assertEquals(
            "AMBER HOUSE - at the centre!",
            poi.getTitle()
        );
    }

    @Test
    public void ignoreLinks() throws Exception {
        Listing poi = parseResourceSinglePOI("links-inside-template.wikicode");
        Assert.assertEquals(
            "Торговые ряды во Владимире очень странные. Их постоянно перестраивали: " +
            "в конце XVIII века начали с типовой одноэтажной арочной конструкции " +
            "в духе Костромы или Галича, " +
            "потом некоторые арки заделали, добавили двухэтажную пристройку на углу с улицей Гагарина, " +
            "украсили эту пристройку башней-ротондой с барельефами и, наконец, " +
            "уже в начале 2000-х полностью переделали северную часть, " +
            "превратив четырёхугольное здание с внутренним двором в большой торговый центр под одной крышей. " +
            "Сейчас торговые ряды так густо завешаны рекламой, что разглядеть в них хоть какую-то " +
            "историческую архитектуру сложно. Внутри тоже ничего не осталось.",
            poi.getDescription()
        );
    }

    @Test
    public void caseInsensitiveListingNames() throws Exception {
        Listing poi = parseResourceSinglePOI("case-insensitive-templates.wikicode");
        Assert.assertEquals(
            "Черниговская областная филармония",
            poi.getTitle()
        );
    }

    @Test
    public void skipDeadLinks() throws Exception {
        Listing poi = parseResourceSinglePOI("dead-link.wikicode");
        Assert.assertEquals(
            "http://www.isbryderen-elbjorn.dk",
            poi.getUrl()
        );
    }

    private Listing parseResourceSinglePOI(String resourceFile) throws Exception
    {
        List<Listing> pois = parseResourcePOIs(resourceFile);
        Assert.assertEquals(1, pois.size());
        return pois.get(0);
    }

    private Listing parseResourceSinglePOI(String resourceFile, Language language) throws Exception
    {
        List<Listing> pois = parseResourcePOIs(resourceFile, language);
        Assert.assertEquals(1, pois.size());
        return pois.get(0);
    }

    private List<Listing> parseResourcePOIs(String resourceFile) throws IOException
    {
        return parseResourcePOIs(resourceFile, new English(), "TestArticle");
    }

    private List<Listing> parseResourcePOIs(String resourceFile, Language language) throws IOException
    {
        return parseResourcePOIs(resourceFile, language, "TestArticle");
    }

    private List<Listing> parseResourcePOIs(
            String resourceFile, Language language, String articleName
    ) throws IOException
    {
        String wikicode = IOUtils.toString(
            this.getClass().getResourceAsStream("/" + resourceFile), "UTF-8"
        );

        ArticleParser pageParser = new ArticleParser(language);
        return pageParser.parsePage(articleName, wikicode);
    }
}
