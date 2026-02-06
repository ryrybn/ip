package nuknagnel;

public class NukNagnel {
  private final Storage storage;
  private final TaskList tasks;
  private final Ui ui;

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
  }

  public void run() {
    ui.showWelcome();
    boolean isExit = false;
    while (!isExit) {
      try {
        String input = ui.readCommand();
        ParsedCommand command = Parser.parse(input);
        ui.showLine();
        switch (command.getType()) {
          case LIST:
            ui.showTaskList(tasks);
            break;
          case MARK:
            Task toMark = tasks.get(command.getIndex());
            toMark.markAsDone();
            storage.save(tasks);
            ui.showMarked(toMark);
            break;
          case UNMARK:
            Task toUnmark = tasks.get(command.getIndex());
            toUnmark.markAsUndone();
            storage.save(tasks);
            ui.showUnmarked(toUnmark);
            break;
          case DELETE:
            Task removed = tasks.remove(command.getIndex());
            storage.save(tasks);
            ui.showTaskDeleted(removed, tasks.size());
            break;
          case TODO:
          case DEADLINE:
          case EVENT:
            tasks.add(command.getTask());
            storage.save(tasks);
            ui.showTaskAdded(command.getTask(), tasks.size());
            break;
          case FIND:
            ui.showMatchingTasks(tasks.find(command.getKeyword()));
            break;
          case BYE:
            isExit = true;
            break;
          default:
            throw new InvalidInputException();
        }
      } catch (InvalidInputException e) {
        ui.showError(e.getMessage());
      } catch (IndexOutOfBoundsException e) {
        ui.showError("Please provide a valid task number.");
      } catch (DataLoadingException e) {
        ui.showSavingError();
      } finally {
        ui.showLine();
      }
    }
    ui.showGoodbye();
  }

  public static void main(String[] args) {
    new NukNagnel("data/nuknagnel.txt").run();
  }
}
