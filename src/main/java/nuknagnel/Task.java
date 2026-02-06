package nuknagnel;

/**
 * Represents a task with a description and completion status.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates a task with the given description.
     *
     * @param description Task description.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns a status icon representing completion.
     *
     * @return "X" if done, otherwise a space.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    /**
     * Marks this task as completed.
     */
    public void markAsDone() {
        this.isDone = true; // set isDone to true
    }

    /**
     * Marks this task as not completed.
     */
    public void markAsUndone() {
        this.isDone = false; // set isDone to false
    }

    /**
     * Returns the task description.
     *
     * @return Task description.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns a display string for this task.
     *
     * @return Display string with status and description.
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + getDescription();
    }
}
