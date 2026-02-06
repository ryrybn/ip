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

    assertEquals("Please use yyyy-mm-dd for dates.", exception.getMessage());
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
  public void parseDateTime_invalidFormat_throws() {
    InvalidInputException exception =
        assertThrows(
            InvalidInputException.class, () -> DateTimeParser.parseDateTime("2024/02/01 13:45"));

    assertEquals(
        "Please use yyyy-mm-dd HHmm or yyyy-mm-dd HH:mm for date-time.", exception.getMessage());
  }
}
