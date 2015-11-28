package org.wikivoyage.listings;

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

    public String getListingPath(String language, String dumpId, String formatExtension, boolean archive)
    {
        String archiveSuffix = "";
        if (archive) {
            archiveSuffix = ".bz2";
        }
        return (
            getListingsDir() + "/wikivoyage-listings-" +
            language + "-" + dumpId + formatExtension + archiveSuffix
        );
    }
}
