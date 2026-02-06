package nuknagnel;

/**
 * Signals a failure when reading or writing task data.
 */
public class DataLoadingException extends Exception {
    /**
     * Creates the exception with a message and root cause.
     *
     * @param message Error message.
     * @param cause Root cause.
     */
    public DataLoadingException(String message, Throwable cause) {
        super(message, cause);
    }
}
