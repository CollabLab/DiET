package diet.server;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class ClientDisconnectedException extends Exception {
    public ClientDisconnectedException() {
        super();
    }

    public ClientDisconnectedException(String message) {
        super(message);
    }

    public ClientDisconnectedException(Throwable cause) {
        super(cause);
    }

    public ClientDisconnectedException(String message, Throwable cause) {
        super(message, cause);
    }
}
