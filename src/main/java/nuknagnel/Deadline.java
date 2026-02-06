package nuknagnel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/** Represents a task with a due date. */
public class Deadline extends Task {

  private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM d yyyy");
  protected LocalDate by;

  /**
   * Creates a deadline task.
   *
   * @param description Task description.
   * @param by Due date.
   */
  public Deadline(String description, LocalDate by) {
    super(description);
    this.by = by;
  }

  /**
   * Returns the due date.
   *
   * @return Due date.
   */
  public LocalDate getBy() {
    return this.by;
  }

  /**
   * Returns a display string for this deadline.
   *
   * @return Display string.
   */
  @Override
  public String toString() {
    return "[D]" + super.toString() + " (by: " + by.format(OUTPUT_FORMAT) + ")";
  }
}
