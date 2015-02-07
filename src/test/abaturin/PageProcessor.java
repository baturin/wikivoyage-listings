package test.abaturin;

import de.fau.cs.osr.ptk.common.ast.AstStringNode;
import org.sweble.wikitext.parser.ParserConfig;
import org.sweble.wikitext.parser.WikitextPreprocessor;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtTemplate;
import org.sweble.wikitext.parser.nodes.WtTemplateArgument;
import org.sweble.wikitext.parser.utils.SimpleParserConfig;
import org.sweble.wikitext.parser.utils.StringConversionException;

import java.util.*;

public class PageProcessor {
    ArrayList<WikivoyagePOI> pois = null;

    public PageProcessor()
    {
        pois = new ArrayList<WikivoyagePOI>();
    }

    public WikivoyagePOI[] getPOIs()
    {
        return pois.toArray(new WikivoyagePOI[pois.size()]);
    }

    public void processPage(String text) {
        try {
            ParserConfig config = new SimpleParserConfig();
            WikitextPreprocessor p = new WikitextPreprocessor(config);
            WtNode node = p.parseArticle(text, "");
            processNode(node);
        } catch (Exception e) {
            System.err.println("Failure");
            e.printStackTrace();
        }
    }

    public void processNode(WtNode node) throws StringConversionException
    {
        for (WtNode childNode: node) {
            if (childNode instanceof WtTemplate) {
                WtTemplate templateNode = (WtTemplate) childNode;
                String templateName = convertWtNodeToString(templateNode.getName());

                if (templateName.equals("listing")) {
                    HashMap<String, String> args = getTemplateArgumentsDict(templateNode);
                    if (args.containsKey("name") && args.containsKey("lat") && args.containsKey("long") && args.containsKey("type")) {
                        try {
                            Float longitude = Float.valueOf(args.get("long"));
                            Float latitude = Float.valueOf((args.get("lat")));
                            String description = "";
                            if (args.containsKey("description")) {
                                description = args.get("description");
                            }

                            pois.add(new WikivoyagePOI(
                                    args.get("type"), args.get("name"),
                                    description,
                                    latitude, longitude
                            ));
                        } catch (NumberFormatException e) {
                            // coordinates are not correctly formatted;
                            // no sense to put such POI into output, simply skip it
                        }

                    }
                }
            } else {
                processNode(childNode);
            }
        }
    }

    private HashMap<String, String> getTemplateArgumentsDict(WtTemplate templateNode) {
        HashMap<String, String> templateArgumentsDict = new LinkedHashMap<String, String>();

        for (WtNode templateArgumentsChildNode : templateNode.getArgs()) {
            if (templateArgumentsChildNode instanceof WtTemplateArgument) {
                WtTemplateArgument templateArgument = (WtTemplateArgument) templateArgumentsChildNode;

                String name = convertWtNodeToString(templateArgument.getName()).trim();
                String value = convertWtNodeToString(templateArgument.getValue()).trim();
                templateArgumentsDict.put(name, value);
            }
        }

        return templateArgumentsDict;
    }

    /**
     * Simple text conversion of WtNode object to string.
     * It ignores templates and presents their contents as plain text, with no conversion.
     */
    public String convertWtNodeToString(WtNode node)
    {
        if (node instanceof AstStringNode) {
            return ((AstStringNode) node).getContent();
        } else {
            String s = "";
            for (WtNode childNode: node) {
                s += convertWtNodeToString(childNode);
            }
            return s;
        }
    }
}
