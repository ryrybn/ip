package nuknagnel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ParserTest {

  @Test
  public void parse_listCommand_success() {
    ParsedCommand command = Parser.parse("list");

    assertEquals(ParsedCommand.Type.LIST, command.getType());
  }

  @Test
  public void parse_markCommand_success() {
    ParsedCommand command = Parser.parse("mark 2");

    assertEquals(ParsedCommand.Type.MARK, command.getType());
    assertEquals(1, command.getIndex());
  }

  @Test
  public void parse_markCommand_withExtraSpaces_success() {
    ParsedCommand command = Parser.parse("   mark    2   ");

    assertEquals(ParsedCommand.Type.MARK, command.getType());
    assertEquals(1, command.getIndex());
  }

  @Test
  public void parse_todoCommand_success() {
    ParsedCommand command = Parser.parse("todo read book");

    assertEquals(ParsedCommand.Type.TODO, command.getType());
    assertInstanceOf(ToDo.class, command.getTask());
    assertEquals("[T][ ] read book", command.getTask().toString());
  }

  @Test
  public void parse_deadlineCommand_success() {
    ParsedCommand command = Parser.parse("deadline return book /by 2024-02-03");

    assertEquals(ParsedCommand.Type.DEADLINE, command.getType());
    assertInstanceOf(Deadline.class, command.getTask());
    assertEquals("[D][ ] return book (by: Feb 3 2024)", command.getTask().toString());
  }

  @Test
  public void parse_eventCommand_success() {
    ParsedCommand command = Parser.parse("event meeting /from 2024-02-03 1200 /to 2024-02-03 1300");

    assertEquals(ParsedCommand.Type.EVENT, command.getType());
    assertInstanceOf(Event.class, command.getTask());
    assertEquals(
        "[E][ ] meeting (from: Feb 3 2024 12:00 to: Feb 3 2024 13:00)",
        command.getTask().toString());
  }

  @Test
  public void parse_unknownCommand_throws() {
    InvalidInputException exception =
        assertThrows(InvalidInputException.class, () -> Parser.parse("find book"));

    assertEquals("I couldn't read that command. Try `list` or `todo <task>`.", exception.getMessage());
  }

  @Test
  public void parse_missingIndex_throws() {
    InvalidInputException exception =
        assertThrows(InvalidInputException.class, () -> Parser.parse("mark"));

    assertEquals("I need a task number for that command.", exception.getMessage());
  }

  @Test
  public void parse_invalidIndex_throws() {
    InvalidInputException exception =
        assertThrows(InvalidInputException.class, () -> Parser.parse("delete 0"));

    assertEquals("That task number doesn't exist. Try `list` to check.", exception.getMessage());
  }

  @Test
  public void parse_missingTodoDescription_throws() {
    InvalidInputException exception =
        assertThrows(InvalidInputException.class, () -> Parser.parse("todo "));

    assertEquals("I need a description for that todo.", exception.getMessage());
  }

  @Test
  public void parse_missingDeadlineDescription_throws() {
    InvalidInputException exception =
        assertThrows(InvalidInputException.class, () -> Parser.parse("deadline /by 2024-02-01"));

    assertEquals("I need a description for that deadline.", exception.getMessage());
  }

  @Test
  public void parse_deadlineMissingBy_throws() {
    InvalidInputException exception =
        assertThrows(InvalidInputException.class, () -> Parser.parse("deadline submit report"));

    assertEquals("Use `deadline <description> /by <date>`.", exception.getMessage());
  }

  @Test
  public void parse_deadlineWithRepeatedBy_throws() {
    InvalidInputException exception =
        assertThrows(
            InvalidInputException.class,
            () -> Parser.parse("deadline report /by 2024-02-01 /by 2024-02-02"));

    assertEquals("Use only one `/by` in a deadline command.", exception.getMessage());
  }

  @Test
  public void parse_eventWithRepeatedFromOrTo_throws() {
    InvalidInputException exception =
        assertThrows(
            InvalidInputException.class,
            () ->
                Parser.parse(
                    "event sync /from 2024-02-01 1300 /from 2024-02-01 1400 /to 2024-02-01 1500"));

    assertEquals("Use one `/from` and one `/to` in an event command.", exception.getMessage());
  }

  @Test
  public void parse_eventMissingDescription_throws() {
    InvalidInputException exception =
        assertThrows(
            InvalidInputException.class,
            () -> Parser.parse("event /from 2024-02-01 1300 /to 2024-02-01 1400"));

    assertEquals("I need a description for that event.", exception.getMessage());
  }

  @Test
  public void parse_eventMissingEnd_throws() {
    InvalidInputException exception =
        assertThrows(
            InvalidInputException.class, () -> Parser.parse("event sync /from 2024-02-01 1300"));

    assertEquals("Use `event <description> /from <start> /to <end>`.", exception.getMessage());
  }

  @Test
  public void parse_eventWithSameStartAndEnd_throws() {
    InvalidInputException exception =
        assertThrows(
            InvalidInputException.class,
            () -> Parser.parse("event sync /from 2024-02-01 1300 /to 2024-02-01 1300"));

    assertEquals("The event end time must be after the start time.", exception.getMessage());
  }
}
