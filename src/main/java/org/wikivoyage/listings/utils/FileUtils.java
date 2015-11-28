package org.wikivoyage.listings.utils;


import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Various utilities to work with files
 */
public class FileUtils {
    /**
     * Check if file exists on filesystem
     *
     * @param filename name of a file to check
     * @return whether the provided file exists
     */
    public static boolean fileExists(String filename)
    {
        File f = new File(filename);
        return f.exists() && !f.isDirectory();
    }

    /**
     * Try to remove file from filesystem, do not check if it was actually removed
     *
     * @param filename name of a file to remove
     * @throws FileUtilsException
     */
    public static void removeFile(String filename) throws FileUtilsException
    {
        removeFile(filename, false);
    }

    /**
     * Remove file from filesystem
     *
     * @param filename name of a file to remove
     * @param check whether to check if file was actually removed
     * @throws FileUtilsException
     */
    public static void removeFile(String filename, boolean check) throws FileUtilsException
    {
        boolean result = new File(filename).delete();
        if (check && !result) {
            throw new FileUtilsException("Failed to remove file '" + filename + "'");
        }
    }

    /**
     * Create directory
     *
     * @param directoryName name of a directory to create
     */
    public static void createDirectory(String directoryName) throws FileUtilsException
    {
        File file = new File(directoryName);
        if (file.isDirectory()) {
            return;
        }
        boolean result = new File(directoryName).mkdirs();
        if (!result) {
            throw new FileUtilsException("Failed to create directory '" + directoryName + "'");
        }
    }

    /**
     * Copy file to another location
     *
     * @param fromFilename source file path
     * @param toFilename destination file path
     * @throws FileUtilsException
     */
    public static void copyFile(String fromFilename, String toFilename) throws FileUtilsException
    {
        try {

            try (
                OutputStream out = new FileOutputStream(toFilename);
                InputStream in = new FileInputStream(fromFilename)
            ) {
                IOUtils.copy(in, out);
            }
        } catch (IOException e) {
            throw new FileUtilsException(
                "Failed to copy file from '" + fromFilename + "' to '" + toFilename + "': " + e.getMessage(),
                e
            );
        }
    }

    /**
     * Create BZIP archive out of a file
     *
     * @param inputFilename name of a file to pack
     * @param outputFilename name of output archive
     * @throws FileUtilsException
     */
    public static void archive(String inputFilename, String outputFilename) throws FileUtilsException
    {
        try {
            OutputStream out = new FileOutputStream(outputFilename);
            try (
                BZip2CompressorOutputStream os = new BZip2CompressorOutputStream(out);
                InputStream in = new FileInputStream(inputFilename)
            ) {
                IOUtils.copy(in, os);
            } finally {
                out.close();
            }
            removeFile(inputFilename);
        } catch (IOException e) {
            throw new FileUtilsException(
                "Failed to create file archive for file '" + inputFilename + "': " + e.getMessage(),
                e
            );
        }
    }
}
