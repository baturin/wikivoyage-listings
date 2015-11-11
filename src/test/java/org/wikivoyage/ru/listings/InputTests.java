package org.wikivoyage.ru.listings;

import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.wikivoyage.ru.listings.entity.WikivoyagePOI;
import org.wikivoyage.ru.listings.input.PageParser;


public class InputTests {

	@Test
	public void process() throws Exception {
		String wikicode = IOUtils.toString(this.getClass().getResourceAsStream("/sample-article.wikicode"), "UTF-8");
		
		PageParser pageParser = new PageParser();
		List<WikivoyagePOI> pois = pageParser.parsePage("Tokyo/Roppongi", wikicode);
		
		// Check number of POIs
		Assert.assertEquals(pois.size(), 71);
		
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
}
