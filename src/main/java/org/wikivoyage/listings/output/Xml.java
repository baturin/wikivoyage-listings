package org.wikivoyage.listings.output;

import org.wikivoyage.listings.entity.WikivoyagePOI;
import org.wikivoyage.listings.utils.XMLSimpleNode;
import org.wikivoyage.listings.utils.XMLSimpleNodeException;

public class Xml extends NavigationXMLOutputFormat {
    private static Integer nodeId = 0;
    private boolean userDefined;

    public Xml(boolean userDefined) {
        this.userDefined = userDefined;
    }

    public XMLSimpleNode createXml(WikivoyagePOI[] pois) throws XMLSimpleNodeException
    {
        XMLSimpleNode genericNode = new XMLSimpleNode("generic")
                .attrib("version", "0.5")
                .attrib("generator", "wikivoyage-pois-converter");

        for (WikivoyagePOI poi: pois) {
            XMLSimpleNode poiNode = new XMLSimpleNode(genericNode, "node")
                    .attrib("id", nodeId.toString())
                    .attrib("visible", "true")
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

            if (!userDefined) {
                addTagNode(poiNode, "wikivoyage", poi.getType());
            } else {
                addTagNode(poiNode, "user_defined", "user_defined");
            }
            addTagNode(poiNode, "name", poi.getTitle());
            addTagNode(poiNode, "description", poi.getDescription());
        }

        return genericNode;
    }

    private static void addTagNode(XMLSimpleNode node, String name, String value)
    {
        new XMLSimpleNode(node, "tag").attrib("k", name).attrib("v", value);
    }

    public String getDefaultExtension()
    {
        if (userDefined) {
            return ".user-defined.xml";
        } else {
            return ".xml";
        }
    }
}
