

/**
 * FtpException Class
 * 
 * @author 	Majid Ghaderi
 *
 */


public class FtpException extends Exception {
    /**
     * Constructor calls Exception super class with message
     */
    public FtpException() {
        super("Ftp exception");
    }

    /**
     * Constructor calls Exception super class with message
     * @param message The message of exception
     */
    public FtpException(String message) {
        super(message);
    }

    /**
     * Constructor calls Exception super class with exception
     * @param e The original exception
     */
    public FtpException(Exception e) {
        super(e);
    }
}
