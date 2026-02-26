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
    return "Good day friend! My name is NukNagnel.\nWhat can I do for you?";
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
      switch (command.getType()) {
        case LIST:
          return formatTaskList();
        case MARK:
          Task toMark = tasks.get(command.getIndex());
          toMark.markAsDone();
          storage.save(tasks);
          return "Awesome! The task below has been marked as *done*:\n" + toMark;
        case UNMARK:
          Task toUnmark = tasks.get(command.getIndex());
          toUnmark.markAsUndone();
          storage.save(tasks);
          return "Alright, I have marked this task as *not done yet*\n" + toUnmark;
        case DELETE:
          Task removed = tasks.remove(command.getIndex());
          storage.save(tasks);
          return "The task has been successfully removed.\n"
              + removed
              + "\nNow you have "
              + tasks.size()
              + " tasks stored in the list.";
        case TODO:
        case DEADLINE:
        case EVENT:
          tasks.add(command.getTask());
          storage.save(tasks);
          return "I have added the task as requested:\n"
              + command.getTask()
              + "\nNow you have "
              + tasks.size()
              + " tasks stored in the list.";
        case BYE:
          isExit = true;
          return "This conversation has ended.\nHope to chat again soon and have a splendid day ahead!";
        default:
          throw new InvalidInputException();
      }
    } catch (InvalidInputException e) {
      return e.getMessage();
    } catch (IndexOutOfBoundsException e) {
      return "Please provide a valid task number.";
    } catch (DataLoadingException e) {
      return "Unable to save tasks to disk.";
    }
  }

  private String formatTaskList() {
    StringBuilder builder = new StringBuilder("Below are the tasks stored in your list:");
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
