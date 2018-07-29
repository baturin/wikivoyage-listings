package org.wikivoyage.listings;

import org.wikivoyage.listings.language.Language;
import org.wikivoyage.listings.output.OutputFormat;
import org.wikivoyage.listings.utils.FileUtils;
import org.wikivoyage.listings.utils.FileUtilsException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ProjectFolders {
    private Path listingDir;
    private Path dumpsCacheDir;
    private Path workingDir;

    public ProjectFolders(Path listingDir, Path dumpsCacheDir, Path workingDir) {
        // Create Folders required for tool
        this.listingDir = listingDir != null ? listingDir : Paths.get("listings");
        this.dumpsCacheDir = dumpsCacheDir != null ? dumpsCacheDir : Paths.get("dumps-cache");
        this.workingDir = workingDir != null ? workingDir : Paths.get("tmp");
    }

    public void createProjectFolders() {
        try {
            FileUtils.createDirectory(listingDir);
            FileUtils.createDirectory(dumpsCacheDir);
            FileUtils.createDirectory(workingDir);
        }
        catch (FileUtilsException e) {
            e.printStackTrace();
        }
    }

    public Path getListingsDir() {
        return listingDir;
    }

    public Path getDumpsCacheDir() {
        return dumpsCacheDir;
    }

    public Path getWorkingDir() {
        return workingDir;
    }

    public String workingDirPath(String filename) {
        return getWorkingDir().toString() + "/" + filename;
    }

    public String dumpsCacheDirPath(String filename) {
        return getDumpsCacheDir() + "/" + filename;
    }

    public String dumpCacheFilename(Language language, String dumpId) {
        return dumpsCacheDirPath(language.getLanguageCode() + "-" + dumpId + ".xml.bz2");
    }

    public String getListingPath(Language language, String dumpId, OutputFormat formatExtension, boolean archive) {
        String archiveSuffix = "";
        if (archive) {
            archiveSuffix = ".bz2";
        }
        return (
                getListingsDir() + "/wikivoyage-listings-" +
                        language.getLanguageCode() + "-" + dumpId + formatExtension.getDefaultExtension() + archiveSuffix
        );
    }
}
