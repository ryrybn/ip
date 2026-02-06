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

    public ParsedCommand(Type type) {
        this(type, -1, null);
    }

    public ParsedCommand(Type type, int index) {
        this(type, index, null);
    }

    public ParsedCommand(Type type, Task task) {
        this(type, -1, task);
    }

    private ParsedCommand(Type type, int index, Task task) {
        this.type = type;
        this.index = index;
        this.task = task;
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
}
