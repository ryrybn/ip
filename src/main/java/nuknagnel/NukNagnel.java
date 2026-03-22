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
    Storage initializedStorage;
    TaskList loadedTasks = new TaskList();
    try {
      initializedStorage = new Storage(filePath);
      loadedTasks = new TaskList(initializedStorage.load());
    } catch (IllegalArgumentException | DataLoadingException e) {
      ui.showLoadingError();
      initializedStorage = null;
    }
    storage = initializedStorage;
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
      return switch (command.getType()) {
        case LIST -> formatTaskList();
        case MARK -> handleMark(command.getIndex());
        case UNMARK -> handleUnmark(command.getIndex());
        case DELETE -> handleDelete(command.getIndex());
        case TODO, DEADLINE, EVENT -> handleAdd(command.getTask());
        case BYE -> handleBye();
      };
    } catch (InvalidInputException e) {
      return e.getMessage();
    } catch (IndexOutOfBoundsException e) {
      return "That task number doesn't exist. Try `list` to check.";
    }
  }

  private String saveTasks() {
    if (storage == null) {
      return "I couldn't save your tasks to disk.";
    }
    try {
      storage.save(tasks);
      return null;
    } catch (DataLoadingException | RuntimeException e) {
      return "I couldn't save your tasks to disk.";
    }
  }

  private String handleMark(int index) {
    assert index >= 0 : "MARK command should have a non-negative index.";
    Task toMark = tasks.get(index);
    toMark.markAsDone();
    String markSaveError = saveTasks();
    if (markSaveError != null) {
      return markSaveError;
    }
    return "Logged. This task is now marked done:\n" + toMark;
  }

  private String handleUnmark(int index) {
    assert index >= 0 : "UNMARK command should have a non-negative index.";
    Task toUnmark = tasks.get(index);
    toUnmark.markAsUndone();
    String unmarkSaveError = saveTasks();
    if (unmarkSaveError != null) {
      return unmarkSaveError;
    }
    return "Noted. This task is marked not done:\n" + toUnmark;
  }

  private String handleDelete(int index) {
    assert index >= 0 : "DELETE command should have a non-negative index.";
    Task removed = tasks.remove(index);
    String deleteSaveError = saveTasks();
    if (deleteSaveError != null) {
      return deleteSaveError;
    }
    return "Removed this task:\n" + removed + "\nYou now have " + tasks.size() + " tasks in the list.";
  }

  private String handleAdd(Task task) {
    assert task != null : "Task creation commands should provide a task payload.";
    if (tasks.containsEquivalent(task)) {
      return "That task is already on your board. No duplicate added.";
    }
    tasks.add(task);
    String addSaveError = saveTasks();
    if (addSaveError != null) {
      return addSaveError;
    }
    return "Added to your list:\n" + task + "\nYou now have " + tasks.size() + " tasks in the list.";
  }

  private String handleBye() {
    isExit = true;
    return "Session closed. Your tasks are saved.";
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
