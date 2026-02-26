package nuknagnel;

/** Signals an invalid or malformed user command. */
public class InvalidInputException extends RuntimeException {
  /** Creates a default invalid input exception. */
  public InvalidInputException() {
    super("I couldn't read that command. Try `list` or `todo <task>`.");
  }

  /**
   * Creates an exception with a specific message.
   *
   * @param message Error message.
   */
  public InvalidInputException(String message) {
    super(message);
  }

  /**
   * Creates an exception with a message and root cause.
   *
   * @param message Error message.
   * @param cause Root cause.
   */
  public InvalidInputException(String message, Throwable cause) {
    super(message, cause);
  }
}
