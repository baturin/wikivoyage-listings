package org.wikivoyage.listings.output;

import org.wikivoyage.listings.entity.Listing;
import org.wikivoyage.listings.utils.XMLSimpleNode;
import org.wikivoyage.listings.utils.XMLSimpleNodeException;

public class OsmXml extends NavigationXMLOutputFormat {
    private static Integer nodeId = 0;
    private boolean userDefined;

    public OsmXml(boolean userDefined) {
        this.userDefined = userDefined;
    }

    public XMLSimpleNode createXml(Listing[] pois, String dumpDate) throws XMLSimpleNodeException
    {
        XMLSimpleNode osmNode = new XMLSimpleNode("osm", dumpDate)
                .attrib("version", "0.5")
                .attrib("generator", "wikivoyage-pois-converter");

        for (Listing poi: pois) {
            XMLSimpleNode poiNode = new XMLSimpleNode(osmNode, "node")
                    .attrib("id", nodeId.toString())
                    .attrib("visible", "true")
                    .attrib("lat", poi.getLatitude())
                    .attrib("lon", poi.getLongitude());
            nodeId++;

            if (!userDefined) {
                addTagNode(poiNode, "wikivoyage", poi.getType());
            } else {
                addTagNode(poiNode, "user_defined", "user_defined");
            }
            addTagNode(poiNode, "name", poi.getTitle());
            addTagNode(poiNode, "description", poi.getDescription());
        }

        return osmNode;
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
