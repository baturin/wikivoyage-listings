package org.wikivoyage.ru.listings.output;

import org.wikivoyage.ru.listings.entity.WikivoyagePOI;
import org.wikivoyage.ru.listings.utils.XMLSimpleNode;
import org.wikivoyage.ru.listings.utils.XMLSimpleNodeException;

import java.util.LinkedList;

/**
 * Base class for XML-based output formats, that should be used for navigation purposes
 */
abstract public class NavigationXMLOutputFormat implements OutputFormat {
    @Override
    public void write(Iterable<WikivoyagePOI> pois, String outputFilename) throws WriteOutputException {
        try {
            LinkedList<WikivoyagePOI> filteredPois = new LinkedList<>();
            for (WikivoyagePOI poi: pois) {
                if (!poi.isPositionalDataEmpty()) {
                    // POIs with no positional data are useless for navigation - do not add them
                    // to reduce size of output file
                    filteredPois.add(poi);
                }
            }
            WikivoyagePOI [] filteredPoisArr = filteredPois.toArray(new WikivoyagePOI[filteredPois.size()]);
            XMLSimpleNode rootNode = createXml(filteredPoisArr);

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
    public abstract XMLSimpleNode createXml(WikivoyagePOI[] pois) throws XMLSimpleNodeException;
}
