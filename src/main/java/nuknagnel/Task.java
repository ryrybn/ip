package nuknagnel;

import java.util.Objects;

/** Represents a task with a description and completion status. */
public class Task {
  protected String description;
  protected boolean isDone;

  /**
   * Creates a task with the given description.
   *
   * @param description Task description.
   */
  public Task(String description) {
    assert description != null : "Task description must not be null.";
    assert !description.isBlank() : "Task description should not be blank.";
    this.description = description;
    this.isDone = false;
  }

  /**
   * Returns a status icon representing completion.
   *
   * @return "X" if done, otherwise a space.
   */
  public String getStatusIcon() {
    return (isDone ? "X" : " ");
  }

  /** Marks this task as completed. */
  public void markAsDone() {
    this.isDone = true;
  }

  /** Marks this task as not completed. */
  public void markAsUndone() {
    this.isDone = false;
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
   * Returns true if another task represents the same task content.
   *
   * @param other Task to compare against.
   * @return True if both tasks represent the same details.
   */
  public boolean isSameTask(Task other) {
    return other != null
        && getClass().equals(other.getClass())
        && Objects.equals(description, other.description);
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
