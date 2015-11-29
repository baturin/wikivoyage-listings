package org.wikivoyage.listings.utils;

import java.util.List;

public class StringUtils {
    public static boolean equalsCaseInsensitive(String s1, String s2)
    {
        return s1.toLowerCase().equals(s2.toLowerCase());
    }

    public static String joinStrings(List<String> strings)
    {
        String result = "";
        for (String s: strings) {
            result += s;
        }
        return result;
    }
}
