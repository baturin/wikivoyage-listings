package org.wikivoyage.listings.input;

/**
 * Exception for problems that happened when reading Java serialized file with Wikivoyage listings
 */
@SuppressWarnings("serial")
public class JavaSerializedReadException extends RuntimeException {
    
    /* package */ JavaSerializedReadException(String message, Exception cause)
    {
        super(message, cause);
    }
}
