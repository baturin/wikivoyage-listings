package org.wikivoyage.listings.output;

import org.wikivoyage.listings.entity.WikivoyagePOI;
import org.wikivoyage.listings.utils.XMLSimpleNode;
import org.wikivoyage.listings.utils.XMLSimpleNodeException;

public class GPX extends NavigationXMLOutputFormat {
    @Override
    public XMLSimpleNode createXml(WikivoyagePOI[] pois) throws XMLSimpleNodeException
    {
        XMLSimpleNode gpxNode = new XMLSimpleNode("gpx")
                .attrib("version", "1.1")
                .attrib("creator", "wikivoyage-pois-converter")
                .attrib("xmlns", "http://www.topografix.com/GPX/1/1")
                .attribNS(
                        "http://www.w3.org/2001/XMLSchema-instance",
                        "xsi:schemaLocation",
                        "http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd"
                );

        for (WikivoyagePOI poi: pois) {
            new XMLSimpleNode(gpxNode, "wpt")
                    .attrib("lat", poi.getLatitude())
                    .attrib("lon", poi.getLongitude())
                    .textChild("name", poi.getTitle())
                    .textChild("desc", poi.getDescription());
        }

        return gpxNode;
    }

    @Override
    public String getDefaultExtension() {
        return ".gpx";
    }
}
