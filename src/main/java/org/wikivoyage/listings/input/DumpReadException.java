package org.wikivoyage.listings.input;

/**
 * Exception for problems that happened when reading Wikivoyage dump
 * and parsing listings
 */
@SuppressWarnings("serial")
public class DumpReadException extends RuntimeException {
    
    /* package */ DumpReadException(String message, Exception cause)
    {
        super(message, cause);
    }
}
