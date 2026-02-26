package nuknagnel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class ParsedCommandTest {

  @Test
  public void constructor_typeOnly_setsDefaults() {
    ParsedCommand command = new ParsedCommand(ParsedCommand.Type.LIST);

    assertEquals(ParsedCommand.Type.LIST, command.getType());
    assertEquals(-1, command.getIndex());
    assertNull(command.getTask());
  }

  @Test
  public void constructor_withIndex_setsIndex() {
    ParsedCommand command = new ParsedCommand(ParsedCommand.Type.MARK, 3);

    assertEquals(ParsedCommand.Type.MARK, command.getType());
    assertEquals(3, command.getIndex());
    assertNull(command.getTask());
  }

  @Test
  public void constructor_withTask_setsTask() {
    Task task = new ToDo("read");
    ParsedCommand command = new ParsedCommand(ParsedCommand.Type.TODO, task);

    assertEquals(ParsedCommand.Type.TODO, command.getType());
    assertEquals(-1, command.getIndex());
    assertEquals(task, command.getTask());
  }
}
