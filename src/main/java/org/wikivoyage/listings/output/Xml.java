package org.wikivoyage.listings.output;

import org.wikivoyage.listings.entity.Listing;
import org.wikivoyage.listings.utils.XMLSimpleNode;
import org.wikivoyage.listings.utils.XMLSimpleNodeException;

public class Xml extends NavigationXMLOutputFormat {
    private static Integer nodeId = 0;

    public XMLSimpleNode createXml(Listing[] pois, String dumpDate) throws XMLSimpleNodeException
    {
        XMLSimpleNode genericNode = new XMLSimpleNode("wikivoyage", dumpDate)
                .attrib("version", "0.5")
                .attrib("generator", "wikivoyage-pois-converter");

        for (Listing poi: pois) {
            XMLSimpleNode poiNode = new XMLSimpleNode(genericNode, "node")
                    .attrib("id", nodeId.toString())
                    .attrib("article", poi.getArticle())
                    .attrib("type", poi.getType())
                    .attrib("title", poi.getTitle())
                    .attrib("alt", poi.getAlt())
                    .attrib("wikidata", poi.getWikidata())
                    .attrib("wikipedia", poi.getWikipedia())
                    .attrib("address", poi.getAddress())
                    .attrib("directions", poi.getDirections())
                    .attrib("phone", poi.getPhone())
                    .attrib("tollFree", poi.getTollFree())
                    .attrib("email", poi.getEmail())
                    .attrib("fax", poi.getFax())                    
                    .attrib("url", poi.getUrl())
                    .attrib("lat", poi.getLatitude())
                    .attrib("lon", poi.getLongitude())
                    .attrib("hours", poi.getHours())
                    .attrib("checkIn", poi.getCheckIn())
                    .attrib("checkOut", poi.getCheckOut())
                    .attrib("image", poi.getImage())
                    .attrib("price", poi.getPrice())
                    .attrib("wifi", poi.getWifi())
                    .attrib("accessibility", poi.getAccessibility())
                    .attrib("lastEdit", poi.getLastEdit())
                    .attrib("description", poi.getDescription());
            nodeId++;
        }

        return genericNode;
    }

    private static void addTagNode(XMLSimpleNode node, String name, String value)
    {
        new XMLSimpleNode(node, "tag").attrib("k", name).attrib("v", value);
    }

    public String getDefaultExtension()
    {
        return ".generic.xml";
    }
}
