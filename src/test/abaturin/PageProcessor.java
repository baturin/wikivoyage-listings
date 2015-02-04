package test.abaturin;

import java.util.ArrayList;

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

    public void processPage(String text)
    {
        System.out.println("Page ");
        int index = 0;
        int start;
        int end;
        do {
            index = text.indexOf("{{", index);
            if (index != -1) {
                start = index;
                end = text.indexOf("}}", start + 1);
                index = end;
                if (end != -1) {
                    String template = text.substring(start + 2, end);
                    String [] templateParts = template.split("\\|");
                    if (templateParts.length > 0) {
                        String templateName = templateParts[0];

                        if (templateName.equals("listing")) {
                            Float longitude = null;
                            Float latitude = null;
                            String title = null;
                            String type = "unknown";
                            String description = "";

                            for (int i = 1; i < templateParts.length; i++) {
                                String[] templatePropertyParts = templateParts[i].split("=", 2);
                                if (templatePropertyParts.length == 2) {

                                    String name = templatePropertyParts[0].trim();
                                    String value = templatePropertyParts[1].trim();

                                    if (name.equals("name")) {
                                        title = value;
                                    } else if (name.equals("type")) {
                                        type = value;
                                    } else if (name.equals("lat")) {
                                        try {
                                            latitude = Float.valueOf(value);
                                        } catch (NumberFormatException e) {
                                            // just ignore
                                        }
                                    } else if (name.equals("long")) {
                                        try {
                                            longitude = Float.valueOf(value);
                                        } catch (NumberFormatException e) {
                                            // just ignore
                                        }
                                    } else if (name.equals("description")) {
                                        description = value;
                                    }
                                }
                            }

                            if (title != null && latitude != null && longitude != null) {
                                pois.add(new WikivoyagePOI(type, title, description, latitude, longitude));
                            }
                        }

                    }

                }
            }
        } while (index != -1);
    }
}
