package nuknagnel;

/**
 * Represents a todo task with only a description.
 */
public class ToDo extends Task {
    /**
     * Creates a todo task.
     *
     * @param description Task description.
     */
    public ToDo(String description) {
        super(description);
    }

    /**
     * Returns a display string for this todo.
     *
     * @return Display string.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
