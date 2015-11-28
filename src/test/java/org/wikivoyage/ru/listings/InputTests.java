package org.wikivoyage.ru.listings;

import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.wikivoyage.ru.listings.entity.WikivoyagePOI;
import org.wikivoyage.ru.listings.input.PageParser;

import language.English;
import language.French;
import language.Language;


public class InputTests {

	@Test
	public void processEnglish() throws Exception {
		String wikicode = IOUtils.toString(this.getClass().getResourceAsStream("/sample-article-en.wikicode"), "UTF-8");

		PageParser pageParser = new PageParser(new English());
		List<WikivoyagePOI> pois = pageParser.parsePage("Tokyo/Roppongi", wikicode);

		// Check number of POIs
		Assert.assertEquals(71, pois.size());

		// Check a particular POI in detail
		WikivoyagePOI poi = pois.get(68);
		Assert.assertEquals("Tokyo/Roppongi", poi.getArticle());
		Assert.assertEquals("sleep", poi.getType());
		Assert.assertEquals("Grand Hyatt Tokyo", poi.getTitle());
		Assert.assertEquals(null, poi.getAlt());
		Assert.assertEquals("6-10-3 Roppongi", poi.getAddress());
		Assert.assertEquals("In Roppongi Hills", poi.getDirections());
		Assert.assertEquals("+81 3 4333-1234", poi.getPhone());
		Assert.assertEquals(null, poi.getTollFree());
		Assert.assertEquals("", poi.getEmail()); // TODO decide whether null or "" is desirable, and stick to it
		Assert.assertEquals("+81 3 4333-8123", poi.getFax());
		Assert.assertEquals("http://tokyo.grand.hyatt.com/", poi.getUrl());
		Assert.assertEquals("", poi.getHours());
		Assert.assertEquals("15:00", poi.getCheckIn());
		Assert.assertEquals("12:00", poi.getCheckOut());
		Assert.assertEquals("Grand Hyatt Tokyo.JPG", poi.getImage());
		Assert.assertEquals("Rack rates marginally cheaper than the Park Hyatt at ¥37,000 and up", poi.getPrice());
		Assert.assertEquals("35.65968", poi.getLatitude());
		Assert.assertEquals("139.72814", poi.getLongitude());
		Assert.assertEquals("A part of Roppongi Hills, and not to be confused with the more famous Park Hyatt of ''Lost in Translation'' fame, which is in Shinjuku. Sleek and minimalistic, all black, gray and brown, with expensive design that never hesitates to sacrifice function for form, but the superlative service makes up for it.", poi.getDescription());
	}

	@Test
	public void processFrench() throws Exception {
		String wikicode = IOUtils.toString(this.getClass().getResourceAsStream("/sample-article-fr.wikicode"), "UTF-8");

		PageParser pageParser = new PageParser(new French());
		List<WikivoyagePOI> pois = pageParser.parsePage("Thouars", wikicode);

		// Check number of POIs
		Assert.assertEquals(28, pois.size());

		// Check a particular POI in detail
		WikivoyagePOI poi = pois.get(25);
		Assert.assertEquals("Thouars", poi.getArticle());
		Assert.assertEquals("sleep", poi.getType());
		Assert.assertEquals("Camping municipal", poi.getTitle());
		Assert.assertEquals("", poi.getAlt());
		Assert.assertEquals("13 Rue de la Grande Côte de Crevant", poi.getAddress());
		Assert.assertEquals(null, poi.getDirections());
		Assert.assertEquals("+33 5 49 66 17 99", poi.getPhone());
		Assert.assertEquals("", poi.getTollFree());
		Assert.assertEquals("", poi.getEmail());
		Assert.assertEquals("", poi.getFax());
		Assert.assertEquals("http://www.ville-thouars.fr/decouvrir/camping.htm", poi.getUrl());
		Assert.assertEquals(null, poi.getHours());
		Assert.assertEquals("", poi.getCheckIn());
		Assert.assertEquals("", poi.getCheckOut());
		Assert.assertEquals(null, poi.getImage());
		Assert.assertEquals("3.2€", poi.getPrice());
		Assert.assertEquals("46.979967", poi.getLatitude());
		Assert.assertEquals("-0.219622", poi.getLongitude());
		// Assert.assertEquals("Situé au bord de la rivière, immédiatement en contrebas du Parc Imbert et du vieux centre ville. {{Horaire|||9||12||15|30|19|30}}, seulement l'été.", poi.getDescription()); TODO fix per https://github.com/baturin/wikivoyage-listings/issues/11
	}

	@Test
	public void processFrenchPrixTemplate() throws Exception {
		String wikicode = IOUtils.toString(this.getClass().getResourceAsStream("/prix-template.wikicode"), "UTF-8");

		PageParser pageParser = new PageParser(new French());
		List<WikivoyagePOI> pois = pageParser.parsePage("TestArticle", wikicode);

		Assert.assertEquals(1, pois.size());
		WikivoyagePOI poi = pois.get(0);
		Assert.assertEquals(
			poi.getPrice(),
			"1.50€ à 4€. Entrée gratuite pour les enfants jusqu’à 12 ans, gratuit le premier dimanche du mois"
		);
	}

	@Test
	public void processRussianRoadTemplate() throws Exception {
		String wikicode = IOUtils.toString(this.getClass().getResourceAsStream("/russian-road-template.wikicode"), "UTF-8");

		PageParser pageParser = new PageParser(new English());
		List<WikivoyagePOI> pois = pageParser.parsePage("TestArticle", wikicode);

		Assert.assertEquals(1, pois.size());
		WikivoyagePOI poi = pois.get(0);
		Assert.assertEquals(
			poi.getDirections(),
			"На 38 км Ново-Рижского шоссе М9 (19 км от МКАД) " +
			"свернуть по указателю на пирамиду."
		);
	}
}
