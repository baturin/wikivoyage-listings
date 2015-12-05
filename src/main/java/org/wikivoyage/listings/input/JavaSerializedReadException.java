package org.wikivoyage.listings.input;

/**
 * Exception for problems that happened when reading Java serialized file with Wikivoyage listings
 */
public class JavaSerializedReadException extends RuntimeException {
    JavaSerializedReadException(String message, Exception cause)
    {
        super(message, cause);
    }
}
