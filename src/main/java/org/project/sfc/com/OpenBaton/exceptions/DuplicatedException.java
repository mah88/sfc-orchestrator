package org.project.sfc.com.OpenBaton.exceptions;

/**
 * Created by mah on 3/1/16.
 */
public class DuplicatedException extends Exception{

    public DuplicatedException(){super();}

    public DuplicatedException(Throwable throwable){ super(throwable);}

    public DuplicatedException(String message){ super(message);}

    public DuplicatedException(String message, Throwable throwable){ super(message,throwable); }

}
