package org.wikivoyage.listings.output;

import org.wikivoyage.listings.utils.XMLSimpleNode;
import org.wikivoyage.listings.entity.Listing;
import org.wikivoyage.listings.utils.XMLSimpleNodeException;

public class KML extends NavigationXMLOutputFormat {
    @Override
    public XMLSimpleNode createXml(Listing[] pois, String dumpDate) throws XMLSimpleNodeException
    {
        XMLSimpleNode kmlNode = new XMLSimpleNode("kml", dumpDate)
                .attrib("xmlns", "http://www.opengis.net/kml/2.2");
        XMLSimpleNode documentNode = new XMLSimpleNode(kmlNode, "Document");
        for (Listing poi: pois) {
            XMLSimpleNode placemarkNode = new XMLSimpleNode(documentNode, "Placemark");
            placemarkNode.textChild("name", poi.getTitle());
            placemarkNode.textChild("description", poi.getDescription());
            XMLSimpleNode pointNode = new XMLSimpleNode(placemarkNode, "Point");
            pointNode.textChild("coordinates", poi.getLongitude() + "," + poi.getLatitude());
        }

        return kmlNode;
    }

    @Override
    public String getDefaultExtension() {
        return ".kml";
    }
}
