package org.wikivoyage.ru.listings;

public class FileNames
{
    private String listingDir;
    private String dumpsCacheDir;
    private String workingDir;

    public FileNames(String listingDir, String dumpsCacheDir, String workingDir)
    {
        if (listingDir != null) {
            this.listingDir = listingDir;
        } else {
            this.listingDir = "listings";
        }
        if (dumpsCacheDir != null) {
            this.dumpsCacheDir = dumpsCacheDir;
        } else {
            this.dumpsCacheDir = "dumps-cache";
        }
        if (workingDir != null) {
            this.workingDir = workingDir;
        } else {
            this.workingDir = "tmp";
        }
    }

    public String getListingsDir()
    {
        return listingDir;
    }

    public String getDumpsCacheDir()
    {
        return dumpsCacheDir;
    }

    public String getWorkingDir()
    {
        return workingDir;
    }

    public String tempXmlFilename()
    {
        return workingDirPath("pois.xml");
    }

    public String workingDirPath(String filename)
    {
        return getWorkingDir() + "/" + filename;
    }

    public String dumpsCacheDirPath(String filename)
    {
        return getDumpsCacheDir() + "/" + filename;
    }

    public String dumpCacheFilename(String language, String dumpId)
    {
        return dumpsCacheDirPath(language + "-" + dumpId + ".xml.bz2");
    }

    public String listingXmlPath(String language, String dumpId, boolean archive)
    {
        return listingPath(language, dumpId, false, "xml", archive);
    }

    public String listingXmlLatestPath(String language)
    {
        return listingPath(language, "latest", false, "xml", false);
    }

    public String listingXmlUserDefinedPath(String language, String dumpId, boolean archive)
    {
        return listingPath(language, dumpId, true, "xml", archive);
    }

    public String listingXmlUserDefinedLatestPath(String language)
    {
        return listingPath(language, "latest", true, "xml", false);
    }

    public String listingObfPath(String language, String dumpId, boolean archive)
    {
        return listingPath(language, dumpId, false, "obf", archive);
    }

    public String listingObfLatestPath(String language)
    {
        return listingPath(language, "latest", false, "obf", false);
    }

    public String listingObfUserDefinedPath(String language, String dumpId, boolean archive)
    {
        return listingPath(language, dumpId, true, "obf", archive);
    }

    public String listingObfUserDefinedLatestPath(String language)
    {
        return listingPath(language, "latest", true, "obf", false);
    }

    private String listingPath(String language, String dumpId, boolean userDefined, String extension, boolean archive)
    {
        String userDefinedSuffix = "";
        if (userDefined) {
            userDefinedSuffix = "-user-defined";
        }
        String archiveSuffix = "";
        if (archive) {
            archiveSuffix = ".bz2";
        }
        return (
                getListingsDir() + "/wikivoyage-listings-" + language + "-" +
                        dumpId + userDefinedSuffix + "." + extension + archiveSuffix);
    }
}
