package nuknagnel;

/** Represents a parsed user command and any related arguments. */
public class ParsedCommand {
  public enum Type {
    LIST,
    MARK,
    UNMARK,
    DELETE,
    TODO,
    DEADLINE,
    EVENT,
    BYE
  }

  private final Type type;
  private final int index;
  private final Task task;

  /**
   * Creates a command with only a type.
   *
   * @param type Command type.
   */
  public ParsedCommand(Type type) {
    this(type, -1, null);
  }

  /**
   * Creates a command with a type and index.
   *
   * @param type Command type.
   * @param index Task index.
   */
  public ParsedCommand(Type type, int index) {
    this(type, index, null);
  }

  /**
   * Creates a command with a type and task.
   *
   * @param type Command type.
   * @param task Task payload.
   */
  public ParsedCommand(Type type, Task task) {
    this(type, -1, task);
  }

  private ParsedCommand(Type type, int index, Task task) {
    assert type != null : "Command type must not be null.";
    assert !isIndexCommand(type) || index >= 0 : "Index commands require a non-negative index.";
    assert !isTaskCommand(type) || task != null : "Task commands require a task payload.";
    assert isIndexCommand(type) || index == -1 : "Only index commands should carry index values.";
    assert isTaskCommand(type) || task == null : "Only task commands should carry task payloads.";
    this.type = type;
    this.index = index;
    this.task = task;
  }

  private static boolean isIndexCommand(Type type) {
    return type == Type.MARK || type == Type.UNMARK || type == Type.DELETE;
  }

  private static boolean isTaskCommand(Type type) {
    return type == Type.TODO || type == Type.DEADLINE || type == Type.EVENT;
  }

  /**
   * Returns the command type.
   *
   * @return Command type.
   */
  public Type getType() {
    return type;
  }

  /**
   * Returns the command index argument.
   *
   * @return Zero-based index, or -1 if unused.
   */
  public int getIndex() {
    return index;
  }

  /**
   * Returns the task argument.
   *
   * @return Task payload, or null if unused.
   */
  public Task getTask() {
    return task;
  }
}
