package org.wikivoyage.ru.listings;

public class FileNames
{
    public String listingsDir()
    {
        return "listings";
    }

    public String dumpsCacheDir()
    {
        return "dumps-cache";
    }

    public String workingDir()
    {
        return "tmp";
    }

    public String workingDirPath(String filename)
    {
        return workingDir() + "/" + filename;
    }

    public String dumpsCacheDirPath(String filename)
    {
        return dumpsCacheDir() + "/" + filename;
    }

    public String dumpCacheFilename(String language, String dumpId)
    {
        return dumpsCacheDirPath(language + "-" + dumpId + ".xml.bz2");
    }

    public String listingXmlPath(String language, String dumpId)
    {
        return listingPath(language, dumpId, false, "xml");
    }

    public String listingXmlUserDefinedPath(String language, String dumpId)
    {
        return listingPath(language, dumpId, true, "xml");
    }

    public String listingObfPath(String language, String dumpId)
    {
        return listingPath(language, dumpId, false, "obf");
    }

    public String listingObfUserDefinedPath(String language, String dumpId)
    {
        return listingPath(language, dumpId, true, "obf");
    }

    private String listingPath(String language, String dumpId, boolean userDefined, String extension)
    {
        String userDefinedSuffix = "";
        if (userDefined) {
            userDefinedSuffix = "-user-defined";
        }
        return listingsDir() + "/wikivoyage-listings-" + language + "-" + dumpId + userDefinedSuffix + "." + extension;
    }
}
