package org.wikivoyage.listings.input;

/**
 * Exception for problems that happened when reading Wikivoyage dump
 * and parsing listings
 */
public class DumpReadException extends RuntimeException {
    DumpReadException(String message, Exception cause)
    {
        super(message, cause);
    }
}
