package org.project.sfc.com.OpenBaton.exceptions;

/**
 * Created by mah on 3/1/16.
 */
public class ApplicationNotFoundException extends Exception {

    public ApplicationNotFoundException(){super();}

    public ApplicationNotFoundException(Throwable throwable){ super(throwable);}

    public ApplicationNotFoundException(String message){ super(message);}

    public ApplicationNotFoundException(String message, Throwable throwable){ super(message,throwable); }

}
