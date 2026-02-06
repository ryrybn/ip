package nuknagnel;

public class ParsedCommand {
  public enum Type {
    LIST,
    MARK,
    UNMARK,
    DELETE,
    TODO,
    DEADLINE,
    EVENT,
    FIND,
    BYE
  }

  private final Type type;
  private final int index;
  private final Task task;
  private final String keyword;

  public ParsedCommand(Type type) {
    this(type, -1, null, null);
  }

  public ParsedCommand(Type type, int index) {
    this(type, index, null, null);
  }

  public ParsedCommand(Type type, Task task) {
    this(type, -1, task, null);
  }

  public ParsedCommand(Type type, String keyword) {
    this(type, -1, null, keyword);
  }

  private ParsedCommand(Type type, int index, Task task, String keyword) {
    this.type = type;
    this.index = index;
    this.task = task;
    this.keyword = keyword;
  }

  public Type getType() {
    return type;
  }

  public int getIndex() {
    return index;
  }

  public Task getTask() {
    return task;
  }

  public String getKeyword() {
    return keyword;
  }
}
