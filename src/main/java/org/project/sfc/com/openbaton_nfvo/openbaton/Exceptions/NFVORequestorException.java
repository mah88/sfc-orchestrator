package org.project.sfc.com.openbaton_nfvo.openbaton.Exceptions;

/**
 * Created by mah on 5/26/16.
 */
public class NFVORequestorException extends Exception {
    public NFVORequestorException() {
    }

    public NFVORequestorException(Throwable cause) {
        super(cause);
    }

    public NFVORequestorException(String message) {
        super(message);
    }

    public NFVORequestorException(String message, Throwable cause) {
        super(message, cause);
    }
}
