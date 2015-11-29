package org.wikivoyage.listings.utils;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;

public class UnrecognizeTemplateCounter {
    private static UnrecognizeTemplateCounter instance = new UnrecognizeTemplateCounter();
    private static final Log log = LogFactory.getLog(UnrecognizeTemplateCounter.class);
    private HashMap<String, HashMap<String, Integer>> templateCount;

    public static UnrecognizeTemplateCounter getInstance()
    {
        return instance;
    }

    public UnrecognizeTemplateCounter()
    {
        templateCount = new HashMap<>();
    }

    public void addUnrecognizedTemplate(String languageCode, String templateName)
    {
        templateName = templateName.toLowerCase();
        if (!templateCount.containsKey(languageCode)) {
            templateCount.put(languageCode, new HashMap<String, Integer>());
        }
        if (!templateCount.get(languageCode).containsKey(templateName)) {
            templateCount.get(languageCode).put(templateName, 0);
        }
        int oldValue = templateCount.get(languageCode).get(templateName);
        templateCount.get(languageCode).put(templateName, oldValue + 1);
    }

    public void logUnrecognizeTemplatesSummary()
    {
        String summary = "========== Unrecognized templates stat ==========\n";
        summary += "Count\tLanguage\tTemplate name\n";
        for (String languageCode: templateCount.keySet()) {
            for (String templateName: templateCount.get(languageCode).keySet()) {
                summary += (
                    templateCount.get(languageCode).get(templateName) + "\t" +
                    languageCode + "\t" +
                    templateName.replace("\n", " ") + "\n"
                );
            }
        }
        log.debug(summary);
    }
}
