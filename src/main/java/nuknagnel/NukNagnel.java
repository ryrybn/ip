package nuknagnel;

/** Main application class for the NukNagnel task manager. */
public class NukNagnel {
  private static final String DEFAULT_FILE_PATH = "data/nuknagnel.txt";

  private final Storage storage;
  private final TaskList tasks;
  private final Ui ui;
  private boolean isExit;

  /**
   * Creates the app and loads tasks from storage.
   *
   * @param filePath Path to the task data file.
   */
  public NukNagnel(String filePath) {
    assert filePath != null : "Application storage file path must not be null.";
    ui = new Ui();
    storage = new Storage(filePath);
    TaskList loadedTasks;
    try {
      loadedTasks = new TaskList(storage.load());
    } catch (DataLoadingException e) {
      ui.showLoadingError();
      loadedTasks = new TaskList();
    }
    tasks = loadedTasks;
    isExit = false;
  }

  /** Runs the main input loop. */
  public void run() {

    ui.showLine();
    System.out.println(getWelcomeMessage());
    ui.showLine();
    while (!isExit) {
      String input = ui.readCommand();
      ui.showLine();
      System.out.println(getResponse(input));
      ui.showLine();
    }
  }

  /** Returns the greeting shown when the app starts. */
  public String getWelcomeMessage() {
    return "NukNagnel here. I keep your tasks on track.\nWhat should we plan first?";
  }

  /** Returns true when the app has received an exit command. */
  public boolean isExit() {
    return isExit;
  }

  /**
   * Processes a raw user input and returns the app response text.
   *
   * @param input Raw user input.
   * @return Response text.
   */
  public String getResponse(String input) {
    try {
      ParsedCommand command = Parser.parse(input);
      assert command != null : "Parser should always return a command or throw.";
      switch (command.getType()) {
        case LIST:
          return formatTaskList();
        case MARK:
          assert command.getIndex() >= 0 : "MARK command should have a non-negative index.";
          Task toMark = tasks.get(command.getIndex());
          toMark.markAsDone();
          storage.save(tasks);
          return "Logged. This task is now marked done:\n" + toMark;
        case UNMARK:
          assert command.getIndex() >= 0 : "UNMARK command should have a non-negative index.";
          Task toUnmark = tasks.get(command.getIndex());
          toUnmark.markAsUndone();
          storage.save(tasks);
          return "Noted. This task is marked not done:\n" + toUnmark;
        case DELETE:
          assert command.getIndex() >= 0 : "DELETE command should have a non-negative index.";
          Task removed = tasks.remove(command.getIndex());
          storage.save(tasks);
          return "Removed this task:\n"
              + removed
              + "\nYou now have "
              + tasks.size()
              + " tasks in the list.";
        case TODO:
        case DEADLINE:
        case EVENT:
          assert command.getTask() != null : "Task creation commands should provide a task payload.";
          if (tasks.containsEquivalent(command.getTask())) {
            return "That task is already on your board. No duplicate added.";
          }
          tasks.add(command.getTask());
          storage.save(tasks);
          return "Added to your list:\n"
              + command.getTask()
              + "\nYou now have "
              + tasks.size()
              + " tasks in the list.";
        case BYE:
          isExit = true;
          return "Session closed. Your tasks are saved.";
        default:
          throw new InvalidInputException();
      }
    } catch (InvalidInputException e) {
      return e.getMessage();
    } catch (IndexOutOfBoundsException e) {
      return "That task number doesn't exist. Try `list` to check.";
    } catch (DataLoadingException e) {
      return "I couldn't save your tasks to disk.";
    }
  }

  private String formatTaskList() {
    StringBuilder builder = new StringBuilder("Here is your task board:");
    for (int i = 0; i < tasks.size(); i++) {
      builder.append("\n").append(i + 1).append(". ").append(tasks.get(i));
    }
    return builder.toString();
  }

  /**
   * Entry point of the application.
   *
   * @param args Command-line arguments.
   */
  public static void main(String[] args) {
    new NukNagnel(DEFAULT_FILE_PATH).run();
  }
}
