package org.wikivoyage.listings.utils;

public class StringUtils {
    public static boolean equalsCaseInsensitive(String s1, String s2)
    {
        return s1.toLowerCase().equals(s2.toLowerCase());
    }
}
