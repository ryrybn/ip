package nuknagnel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task with a start and end date-time.
 */
public class Event extends Task {

    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM d yyyy HH:mm");
    protected LocalDateTime from;
    protected LocalDateTime to;

    /**
     * Creates an event task.
     *
     * @param description Task description.
     * @param from Start date-time.
     * @param to End date-time.
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the start date-time.
     *
     * @return Start date-time.
     */
    public LocalDateTime getFrom() {
        return this.from;
    }

    /**
     * Returns the end date-time.
     *
     * @return End date-time.
     */
    public LocalDateTime getTo() {
        return this.to;
    }

    /**
     * Returns a display string for this event.
     *
     * @return Display string.
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.from.format(OUTPUT_FORMAT)
                + " to: " + this.to.format(OUTPUT_FORMAT) + ")";
    }
}
