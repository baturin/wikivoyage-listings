package org.wikivoyage.listings.utils;


import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class XMLSimpleNode {
    public boolean isRoot;
    public Element node;

    public XMLSimpleNode(String tag, String dumpDate) throws XMLSimpleNodeException
    {
        // Create the document.
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder;
        try {
            docBuilder = docFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new XMLSimpleNodeException("Failed to create XML simple node: failed to create document builder", e);
        }
        Document doc = docBuilder.newDocument();
     
        // Add a comment node as a header. A line break would be nice, but seems tricky to achieve:
        // http://stackoverflow.com/questions/24551962/adding-linebreak-in-xml-file-before-root-node 
        Comment comment = doc.createComment("Created from " + dumpDate + " Wikivoyage data");
        doc.appendChild(comment);
        
        // Create the element.
        node = doc.createElement(tag);
        doc.appendChild(node);
        isRoot = true;
    }

    public XMLSimpleNode(XMLSimpleNode root, String tag)
    {
        node = root.node.getOwnerDocument().createElement(tag);
        root.node.appendChild(node);
        isRoot = false;
    }

    public XMLSimpleNode attrib(String name, String value)
    {
        node.setAttribute(name, value);
        return this;
    }

    public XMLSimpleNode textChild(String tag, String text)
    {
        Element newNode = node.getOwnerDocument().createElement(tag);
        newNode.setTextContent(text);
        node.appendChild(newNode);
        return this;
    }

    public XMLSimpleNode attribNS(String namespaceURI, String qualifiedName, String value)
    {
        node.setAttributeNS(namespaceURI, qualifiedName, value);
        return this;
    }

    public void writeToFile(String filename) throws XMLSimpleNodeException
    {
        if (!isRoot) {
            throw new XMLSimpleNodeException("Non-root node cannot be written to file");
        }
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerException e) {
            throw new XMLSimpleNodeException("Failed to create transformer", e);
        }
        configureIndentation(transformer);
        DOMSource source = new DOMSource(node.getOwnerDocument());
        StreamResult result = new StreamResult(new File(filename));

        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            throw new XMLSimpleNodeException("Failed to perform XML transformation", e);
        }
    }

    private static void configureIndentation(Transformer transformer) {
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
    }
}
