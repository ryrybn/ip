package nuknagnel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/** Parses date and date-time strings from user input. */
public class DateTimeParser {
  /**
   * Parses an ISO-8601 date string (yyyy-MM-dd).
   *
   * @param raw Raw date string.
   * @return Parsed date.
   * @throws InvalidInputException If the format is invalid.
   */
  public static LocalDate parseDate(String raw) {
    try {
      return LocalDate.parse(raw);
    } catch (DateTimeParseException e) {
      throw new InvalidInputException("Please use yyyy-mm-dd for dates.");
    }
  }

  /**
   * Parses a date-time string in supported formats.
   *
   * @param raw Raw date-time string.
   * @return Parsed date-time.
   * @throws InvalidInputException If the format is invalid.
   */
  public static LocalDateTime parseDateTime(String raw) {
    try {
      return LocalDateTime.parse(raw);
    } catch (DateTimeParseException e) {
      // fall through to custom formats
    }
    DateTimeFormatter[] formats =
        new DateTimeFormatter[] {
          DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"),
          DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        };
    for (DateTimeFormatter format : formats) {
      try {
        return LocalDateTime.parse(raw, format);
      } catch (DateTimeParseException e) {
        // try next format
      }
    }
    throw new InvalidInputException(
        "Please use yyyy-mm-dd HHmm or yyyy-mm-dd HH:mm for date-time.");
  }
}
