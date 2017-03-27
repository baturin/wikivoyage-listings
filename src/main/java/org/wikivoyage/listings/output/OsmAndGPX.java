package org.wikivoyage.listings.output;

import org.wikivoyage.listings.utils.XMLSimpleNode;
import org.wikivoyage.listings.utils.XMLSimpleNodeException;
import org.wikivoyage.listings.entity.Listing;

/**
 * OsmAnd only shows a single label, which is rather inconvenient when you want to
 * determine whether a particular POI is a hotel or not, for instance.
 * Also, OsmAnd does not provide an URL, so finding more information would be a
 * pain without knowing the article name.
 * 
 * So, this format outputs GPX titles like this: title (article, type)
 * Example: Le Logis de Pompois (Thouars, restaurant)
 */
public class OsmAndGPX extends NavigationXMLOutputFormat {
    @Override
    public XMLSimpleNode createXml(Listing[] pois, String dumpDate) throws XMLSimpleNodeException
    {
        XMLSimpleNode gpxNode = new XMLSimpleNode("gpx", dumpDate)
                .attrib("version", "1.1")
                .attrib("creator", "wikivoyage-pois-converter")
                .attrib("xmlns", "http://www.topografix.com/GPX/1/1")
                .attribNS(
                        "http://www.w3.org/2001/XMLSchema-instance",
                        "xsi:schemaLocation",
                        "http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd"
                );

        for (Listing poi: pois) {
        	
        	String name = poi.getTitle() + " (" + poi.getArticle() + ", " + poi.getType() + ")"; 
        	
            new XMLSimpleNode(gpxNode, "wpt")
                    .attrib("lat", poi.getLatitude())
                    .attrib("lon", poi.getLongitude())
                    .textChild("name", name);
        }

        return gpxNode;
    }

    @Override
    public String getDefaultExtension() {
        return ".osmand.gpx";
    }
}
