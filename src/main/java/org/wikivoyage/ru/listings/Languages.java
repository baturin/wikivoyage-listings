package org.wikivoyage.ru.listings;

import java.util.LinkedList;
import java.util.List;

public class Languages
{
    public static List<String> getLanguages()
    {
        List<String> languages = new LinkedList<String>();
        languages.add("ru");
        languages.add("en");
        return languages;
    }
}
