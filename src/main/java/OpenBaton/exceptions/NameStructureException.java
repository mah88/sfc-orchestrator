package OpenBaton.exceptions;

/**
 * Created by mah on 3/1/16.
 */
public class NameStructureException extends Exception{ //I can't believe that has to be created

    public NameStructureException(){super();}

    public NameStructureException(Throwable throwable){ super(throwable);}

    public NameStructureException(String message){ super(message);}

    public NameStructureException(String message, Throwable throwable){ super(message,throwable); }
}
