package org.wikivoyage.listings.output;

import org.wikivoyage.listings.utils.XMLSimpleNode;
import org.wikivoyage.listings.entity.Listing;
import org.wikivoyage.listings.utils.XMLSimpleNodeException;

import java.util.LinkedList;

/**
 * Base class for XML-based output formats, that should be used for navigation purposes
 */
abstract public class NavigationXMLOutputFormat implements OutputFormat {
    @Override
    public void write(Iterable<Listing> pois, String outputFilename, String dumpDate) throws WriteOutputException {
        try {
            LinkedList<Listing> filteredPois = new LinkedList<>();
            for (Listing poi: pois) {
                if (!poi.isPositionalDataEmpty()) {
                    // POIs with no positional data are useless for navigation - do not add them
                    // to reduce size of output file
                    filteredPois.add(poi);
                }
            }
            Listing [] filteredPoisArr = filteredPois.toArray(new Listing[filteredPois.size()]);
            XMLSimpleNode rootNode = createXml(filteredPoisArr, dumpDate);

            rootNode.writeToFile(outputFilename);
        } catch (XMLSimpleNodeException e) {
            throw new WriteOutputException();
        }
    }

    /**
     * Create XML document for specified POIs
     *
     * @param pois list of POIs to serialize
     * @return root XML node of the document
     * @throws XMLSimpleNodeException
     */
    public abstract XMLSimpleNode createXml(Listing[] pois, String dumpDate) throws XMLSimpleNodeException;
}
