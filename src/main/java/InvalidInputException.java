public class InvalidInputException extends RuntimeException {
    /**
     * Constructs an InvalidInputException with the specified detail message.
     * @param message the detail message.
     */

    public InvalidInputException() {
        super("Invalid command.");
    }

    public InvalidInputException(String message) {
        super(message); // Pass the message to the parent Exception class
    }

    // You can add more constructors or custom fields/methods if needed
    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }

}