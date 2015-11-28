package org.wikivoyage.listings.utils;

/**
 * Exception occured when working with files
 */
public class FileUtilsException extends Exception {
    public FileUtilsException(String message)
    {
        super(message);
    }

    public FileUtilsException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
