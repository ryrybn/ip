import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeParser {
    public static LocalDate parseDate(String raw) {
        try {
            return LocalDate.parse(raw);
        } catch (DateTimeParseException e) {
            throw new InvalidInputException("Please use yyyy-mm-dd for dates.");
        }
    }

    public static LocalDateTime parseDateTime(String raw) {
        try {
            return LocalDateTime.parse(raw);
        } catch (DateTimeParseException e) {
            // fall through to custom formats
        }
        DateTimeFormatter[] formats = new DateTimeFormatter[] {
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
        throw new InvalidInputException("Please use yyyy-mm-dd HHmm or yyyy-mm-dd HH:mm for date-time.");
    }
}
