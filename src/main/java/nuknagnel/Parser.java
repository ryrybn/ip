package nuknagnel;

import java.time.LocalDateTime;

/** Parses user input into executable commands. */
public class Parser {
  /**
   * Converts a raw input string into a {@link ParsedCommand}.
   *
   * @param input Raw user input.
   * @return Parsed command with any required arguments.
   * @throws InvalidInputException If the input is invalid or incomplete.
   */
  public static ParsedCommand parse(String input) {
    String trimmed = input == null ? "" : input.trim();
    if (trimmed.isEmpty()) {
      throw new InvalidInputException();
    }
    String[] parts = trimmed.split("\\s+", 2);
    assert parts.length >= 1 : "Split command should always yield at least one token.";
    String command = parts[0];
    String rest = parts.length > 1 ? parts[1] : "";
    assert !command.isEmpty() : "Command token should not be empty after trim.";
    switch (command) {
      case "bye":
        return new ParsedCommand(ParsedCommand.Type.BYE);
      case "list":
        return new ParsedCommand(ParsedCommand.Type.LIST);
      case "mark":
        return new ParsedCommand(ParsedCommand.Type.MARK, parseIndex(rest));
      case "unmark":
        return new ParsedCommand(ParsedCommand.Type.UNMARK, parseIndex(rest));
      case "delete":
        return new ParsedCommand(ParsedCommand.Type.DELETE, parseIndex(rest));
      case "todo":
        return new ParsedCommand(ParsedCommand.Type.TODO, parseTodo(rest));
      case "deadline":
        return new ParsedCommand(ParsedCommand.Type.DEADLINE, parseDeadline(rest));
      case "event":
        return new ParsedCommand(ParsedCommand.Type.EVENT, parseEvent(rest));
      default:
        throw new InvalidInputException();
    }
  }

  /**
   * Parses a 1-based index from user input and converts it to 0-based.
   *
   * @param raw Raw index string.
   * @return Zero-based index.
   * @throws InvalidInputException If the index is missing or invalid.
   */
  private static int parseIndex(String raw) {
    if (raw == null || raw.trim().isEmpty()) {
      throw new InvalidInputException("I need a task number for that command.");
    }
    try {
      int index = Integer.parseInt(raw.trim()) - 1;
      if (index < 0) {
        throw new InvalidInputException("That task number doesn't exist. Try `list` to check.");
      }
      return index;
    } catch (NumberFormatException e) {
      throw new InvalidInputException("That task number doesn't exist. Try `list` to check.");
    }
  }

  /**
   * Parses a todo command into a {@link ToDo}.
   *
   * @param raw Raw description.
   * @return Todo task.
   * @throws InvalidInputException If the description is missing.
   */
  private static Task parseTodo(String raw) {
    String description = raw == null ? "" : raw.trim();
    if (description.isEmpty()) {
      throw new InvalidInputException("I need a description for that todo.");
    }
    return new ToDo(description);
  }

  /**
   * Parses a deadline command into a {@link Deadline}.
   *
   * @param raw Raw description and date.
   * @return Deadline task.
   * @throws InvalidInputException If the format is missing or invalid.
   */
  private static Task parseDeadline(String raw) {
    if (raw == null || !raw.contains("/by ")) {
      throw new InvalidInputException("Use `deadline <description> /by <date>`.");
    }
    if (countOccurrences(raw, "/by ") > 1) {
      throw new InvalidInputException("Use only one `/by` in a deadline command.");
    }
    String[] parts = raw.split("/by ", 2);
    assert parts.length == 2 : "Deadline split by /by should produce 2 parts.";
    String description = parts[0].stripTrailing();
    if (description.isBlank()) {
      throw new InvalidInputException("I need a description for that deadline.");
    }
    return new Deadline(description, DateTimeParser.parseDate(parts[1].trim()));
  }

  /**
   * Parses an event command into an {@link Event}.
   *
   * @param raw Raw description and time range.
   * @return Event task.
   * @throws InvalidInputException If the format is missing or invalid.
   */
  private static Task parseEvent(String raw) {
    if (raw == null || !raw.contains("/from ") || !raw.contains(" /to ")) {
      throw new InvalidInputException("Use `event <description> /from <start> /to <end>`.");
    }
    if (countOccurrences(raw, "/from ") > 1 || countOccurrences(raw, " /to ") > 1) {
      throw new InvalidInputException("Use one `/from` and one `/to` in an event command.");
    }
    String[] parts = raw.split("/from ", 2);
    assert parts.length == 2 : "Event split by /from should produce 2 parts.";
    String description = parts[0].stripTrailing();
    if (description.isBlank()) {
      throw new InvalidInputException("I need a description for that event.");
    }
    String[] timeParts = parts[1].split(" /to ", 2);
    assert timeParts.length == 2 : "Event split by /to should produce 2 parts.";
    String from = timeParts[0].trim();
    String to = timeParts[1].trim();
    if (from.isEmpty() || to.isEmpty()) {
      throw new InvalidInputException("I need both a start and end date-time for that event.");
    }
    LocalDateTime start = DateTimeParser.parseDateTime(from);
    LocalDateTime end = DateTimeParser.parseDateTime(to);
    if (!end.isAfter(start)) {
      throw new InvalidInputException("The event end time must be after the start time.");
    }
    return new Event(description, start, end);
  }

  private static int countOccurrences(String source, String token) {
    int count = 0;
    int start = 0;
    while (true) {
      int found = source.indexOf(token, start);
      if (found < 0) {
        break;
      }
      count++;
      start = found + token.length();
    }
    return count;
  }
}
