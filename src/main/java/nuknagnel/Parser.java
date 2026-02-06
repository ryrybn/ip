package nuknagnel;

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
    String command = parts[0];
    String rest = parts.length > 1 ? parts[1] : "";
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
      throw new InvalidInputException("Please input the required parameters for your command.");
    }
    try {
      int index = Integer.parseInt(raw.trim()) - 1;
      if (index < 0) {
        throw new InvalidInputException("Please provide a valid task number.");
      }
      return index;
    } catch (NumberFormatException e) {
      throw new InvalidInputException("Please provide a valid task number.");
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
      throw new InvalidInputException("Todo tasks must include a description.");
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
      throw new InvalidInputException("Deadline tasks must include /by <date>.");
    }
    String[] parts = raw.split("/by ", 2);
    String description = parts[0].stripTrailing();
    if (description.isBlank()) {
      throw new InvalidInputException("Deadline tasks must include a description.");
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
      throw new InvalidInputException("Event tasks must include /from <start> /to <end>.");
    }
    String[] parts = raw.split("/from ", 2);
    String description = parts[0].stripTrailing();
    if (description.isBlank()) {
      throw new InvalidInputException("Event tasks must include a description.");
    }
    String[] timeParts = parts[1].split(" /to ", 2);
    String from = timeParts[0].trim();
    String to = timeParts[1].trim();
    return new Event(
        description, DateTimeParser.parseDateTime(from), DateTimeParser.parseDateTime(to));
  }
}
