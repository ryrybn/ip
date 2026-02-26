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

    assertEquals("Invalid command.", exception.getMessage());
  }

  @Test
  public void parse_missingIndex_throws() {
    InvalidInputException exception =
        assertThrows(InvalidInputException.class, () -> Parser.parse("mark"));

    assertEquals("Please input the required parameters for your command.", exception.getMessage());
  }

  @Test
  public void parse_invalidIndex_throws() {
    InvalidInputException exception =
        assertThrows(InvalidInputException.class, () -> Parser.parse("delete 0"));

    assertEquals("Please provide a valid task number.", exception.getMessage());
  }

  @Test
  public void parse_missingTodoDescription_throws() {
    InvalidInputException exception =
        assertThrows(InvalidInputException.class, () -> Parser.parse("todo "));

    assertEquals("Todo tasks must include a description.", exception.getMessage());
  }
}
