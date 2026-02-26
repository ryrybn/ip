package nuknagnel;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;
import java.util.Map;

/** Parses date and date-time strings from user input. */
public class DateTimeParser {
  private static final DateTimeFormatter DATE_TIME_COMPACT_FORMAT =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
  private static final DateTimeFormatter DATE_TIME_COLON_FORMAT =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
  private static final DateTimeFormatter TIME_COMPACT_FORMAT = DateTimeFormatter.ofPattern("HHmm");
  private static final DateTimeFormatter TIME_COLON_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

  private static final Map<String, DayOfWeek> DAY_OF_WEEK_MAP =
      Map.ofEntries(
          Map.entry("mon", DayOfWeek.MONDAY),
          Map.entry("monday", DayOfWeek.MONDAY),
          Map.entry("tue", DayOfWeek.TUESDAY),
          Map.entry("tues", DayOfWeek.TUESDAY),
          Map.entry("tuesday", DayOfWeek.TUESDAY),
          Map.entry("wed", DayOfWeek.WEDNESDAY),
          Map.entry("wednesday", DayOfWeek.WEDNESDAY),
          Map.entry("thu", DayOfWeek.THURSDAY),
          Map.entry("thur", DayOfWeek.THURSDAY),
          Map.entry("thurs", DayOfWeek.THURSDAY),
          Map.entry("thursday", DayOfWeek.THURSDAY),
          Map.entry("fri", DayOfWeek.FRIDAY),
          Map.entry("friday", DayOfWeek.FRIDAY),
          Map.entry("sat", DayOfWeek.SATURDAY),
          Map.entry("saturday", DayOfWeek.SATURDAY),
          Map.entry("sun", DayOfWeek.SUNDAY),
          Map.entry("sunday", DayOfWeek.SUNDAY));

  /**
   * Parses an ISO-8601 date string (yyyy-MM-dd).
   *
   * @param raw Raw date string.
   * @return Parsed date.
   * @throws InvalidInputException If the format is invalid.
   */
  public static LocalDate parseDate(String raw) {
    return parseDate(raw, LocalDate.now());
  }

  static LocalDate parseDate(String raw, LocalDate referenceDate) {
    if (raw == null || raw.trim().isEmpty()) {
      throw new InvalidInputException(
          "I couldn't read that date. Use `yyyy-mm-dd` or a weekday like `Mon`.");
    }
    assert referenceDate != null : "Reference date must not be null.";

    String trimmed = raw.trim();
    try {
      return LocalDate.parse(trimmed);
    } catch (DateTimeParseException e) {
      // fall through to natural weekday parsing
    }

    LocalDate naturalDate = parseNaturalDate(trimmed, referenceDate);
    if (naturalDate != null) {
      return naturalDate;
    }

    throw new InvalidInputException(
        "I couldn't read that date. Use `yyyy-mm-dd` or a weekday like `Mon`.");
  }

  /**
   * Parses a date-time string in supported formats.
   *
   * @param raw Raw date-time string.
   * @return Parsed date-time.
   * @throws InvalidInputException If the format is invalid.
   */
  public static LocalDateTime parseDateTime(String raw) {
    return parseDateTime(raw, LocalDate.now());
  }

  static LocalDateTime parseDateTime(String raw, LocalDate referenceDate) {
    if (raw == null || raw.trim().isEmpty()) {
      throw new InvalidInputException(
          "I couldn't read that date-time. Use `yyyy-mm-dd HHmm`, `yyyy-mm-dd HH:mm`, "
              + "or a weekday like `Mon 1400`.");
    }
    assert referenceDate != null : "Reference date must not be null.";

    String trimmed = raw.trim();
    try {
      return LocalDateTime.parse(trimmed);
    } catch (DateTimeParseException e) {
      // fall through to custom formats
    }

    DateTimeFormatter[] formats =
        new DateTimeFormatter[] {DATE_TIME_COMPACT_FORMAT, DATE_TIME_COLON_FORMAT};
    for (DateTimeFormatter format : formats) {
      try {
        return LocalDateTime.parse(trimmed, format);
      } catch (DateTimeParseException e) {
        // try next format
      }
    }

    LocalDateTime naturalDateTime = parseNaturalDateTime(trimmed, referenceDate);
    if (naturalDateTime != null) {
      return naturalDateTime;
    }

    throw new InvalidInputException(
        "I couldn't read that date-time. Use `yyyy-mm-dd HHmm`, `yyyy-mm-dd HH:mm`, "
            + "or a weekday like `Mon 1400`.");
  }

  private static LocalDate parseNaturalDate(String raw, LocalDate referenceDate) {
    DayOfWeek dayOfWeek = DAY_OF_WEEK_MAP.get(raw.toLowerCase(Locale.ROOT));
    if (dayOfWeek == null) {
      return null;
    }
    return referenceDate.with(TemporalAdjusters.next(dayOfWeek));
  }

  private static LocalDateTime parseNaturalDateTime(String raw, LocalDate referenceDate) {
    String[] parts = raw.split("\\s+");
    if (parts.length == 0 || parts.length > 2) {
      return null;
    }

    LocalDate naturalDate = parseNaturalDate(parts[0], referenceDate);
    if (naturalDate == null) {
      return null;
    }

    if (parts.length == 1) {
      return naturalDate.atStartOfDay();
    }

    LocalTime parsedTime = parseTime(parts[1]);
    if (parsedTime == null) {
      return null;
    }
    return naturalDate.atTime(parsedTime);
  }

  private static LocalTime parseTime(String raw) {
    try {
      return LocalTime.parse(raw, TIME_COMPACT_FORMAT);
    } catch (DateTimeParseException e) {
      // fall through to next format
    }

    try {
      return LocalTime.parse(raw, TIME_COLON_FORMAT);
    } catch (DateTimeParseException e) {
      return null;
    }
  }
}
