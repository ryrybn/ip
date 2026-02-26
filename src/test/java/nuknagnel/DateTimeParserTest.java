package nuknagnel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class DateTimeParserTest {

  @Test
  public void parseDate_validIsoDate_success() {
    LocalDate date = DateTimeParser.parseDate("2024-02-01");

    assertEquals(LocalDate.of(2024, 2, 1), date);
  }

  @Test
  public void parseDate_invalidFormat_throws() {
    InvalidInputException exception =
        assertThrows(InvalidInputException.class, () -> DateTimeParser.parseDate("02-01-2024"));

    assertEquals(
        "I couldn't read that date. Use `yyyy-mm-dd` or a weekday like `Mon`.",
        exception.getMessage());
  }

  @Test
  public void parseDate_blankInput_throws() {
    InvalidInputException exception =
        assertThrows(InvalidInputException.class, () -> DateTimeParser.parseDate("   "));

    assertEquals(
        "I couldn't read that date. Use `yyyy-mm-dd` or a weekday like `Mon`.",
        exception.getMessage());
  }

  @Test
  public void parseDate_naturalWeekday_success() {
    LocalDate referenceDate = LocalDate.of(2024, 2, 1); // Thursday

    LocalDate date = DateTimeParser.parseDate("Mon", referenceDate);

    assertEquals(LocalDate.of(2024, 2, 5), date);
  }

  @Test
  public void parseDate_naturalWeekdaySameDay_picksNextWeek() {
    LocalDate referenceDate = LocalDate.of(2024, 2, 5); // Monday

    LocalDate date = DateTimeParser.parseDate("monday", referenceDate);

    assertEquals(LocalDate.of(2024, 2, 12), date);
  }

  @Test
  public void parseDateTime_isoFormat_success() {
    LocalDateTime dateTime = DateTimeParser.parseDateTime("2024-02-01T13:45");

    assertEquals(LocalDateTime.of(2024, 2, 1, 13, 45), dateTime);
  }

  @Test
  public void parseDateTime_compactFormat_success() {
    LocalDateTime dateTime = DateTimeParser.parseDateTime("2024-02-01 1345");

    assertEquals(LocalDateTime.of(2024, 2, 1, 13, 45), dateTime);
  }

  @Test
  public void parseDateTime_spacedFormat_success() {
    LocalDateTime dateTime = DateTimeParser.parseDateTime("2024-02-01 13:45");

    assertEquals(LocalDateTime.of(2024, 2, 1, 13, 45), dateTime);
  }

  @Test
  public void parseDateTime_naturalWeekdayWithCompactTime_success() {
    LocalDate referenceDate = LocalDate.of(2024, 2, 1); // Thursday

    LocalDateTime dateTime = DateTimeParser.parseDateTime("Mon 1345", referenceDate);

    assertEquals(LocalDateTime.of(2024, 2, 5, 13, 45), dateTime);
  }

  @Test
  public void parseDateTime_naturalWeekdayWithColonTime_success() {
    LocalDate referenceDate = LocalDate.of(2024, 2, 1); // Thursday

    LocalDateTime dateTime = DateTimeParser.parseDateTime("Mon 13:45", referenceDate);

    assertEquals(LocalDateTime.of(2024, 2, 5, 13, 45), dateTime);
  }

  @Test
  public void parseDateTime_naturalWeekdayOnly_success() {
    LocalDate referenceDate = LocalDate.of(2024, 2, 1); // Thursday

    LocalDateTime dateTime = DateTimeParser.parseDateTime("Tue", referenceDate);

    assertEquals(LocalDateTime.of(2024, 2, 6, 0, 0), dateTime);
  }

  @Test
  public void parseDateTime_invalidFormat_throws() {
    InvalidInputException exception =
        assertThrows(
            InvalidInputException.class, () -> DateTimeParser.parseDateTime("2024/02/01 13:45"));

    assertEquals(
        "I couldn't read that date-time. Use `yyyy-mm-dd HHmm`, `yyyy-mm-dd HH:mm`, "
            + "or a weekday like `Mon 1400`.",
        exception.getMessage());
  }
}
