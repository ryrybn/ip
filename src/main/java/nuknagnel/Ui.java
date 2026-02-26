package nuknagnel;

import java.util.List;
import java.util.Scanner;

/** Handles user interaction and console output. */
public class Ui {
  private final Scanner scanner = new Scanner(System.in);

  /** Displays the welcome message. */
  public void showWelcome() {
    showLine();
    String intro = " NukNagnel here. I keep your tasks on track.\n What should we plan first?";
    System.out.print(intro);
    showLine();
  }

  /** Displays the goodbye message. */
  public void showGoodbye() {
    String exit = " Session closed. Your tasks are saved.";
    System.out.print(exit);
    showLine();
  }

  /** Prints a separator line. */
  public void showLine() {
    System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
  }

  /**
   * Reads a command from standard input.
   *
   * @return Raw user input.
   */
  public String readCommand() {
    System.out.print(" > ");
    return scanner.nextLine();
  }

  /**
   * Displays the task list.
   *
   * @param tasks Task list to render.
   */
  public void showTaskList(TaskList tasks) {
    System.out.println("Here is your task board:");
    List<Task> items = tasks.getTasks();
    for (int i = 0; i < items.size(); i++) {
      String s = String.valueOf(i + 1) + ". " + items.get(i);
      System.out.println(s);
    }
  }

  /**
   * Displays a confirmation when a task is added.
   *
   * @param task Added task.
   * @param size Updated task count.
   */
  public void showTaskAdded(Task task, int size) {
    System.out.println("Added to your list:\n" + task);
    showTaskCount(size);
  }

  /**
   * Displays a confirmation when a task is deleted.
   *
   * @param task Deleted task.
   * @param size Updated task count.
   */
  public void showTaskDeleted(Task task, int size) {
    System.out.println("Removed this task:");
    System.out.print(task);
    showTaskCount(size);
  }

  /**
   * Displays a confirmation when a task is marked done.
   *
   * @param task Marked task.
   */
  public void showMarked(Task task) {
    System.out.println("Logged. This task is now marked done:");
    System.out.print(task);
  }

  /**
   * Displays a confirmation when a task is unmarked.
   *
   * @param task Unmarked task.
   */
  public void showUnmarked(Task task) {
    System.out.println("Noted. This task is marked not done:");
    System.out.print(task);
  }

  /**
   * Displays the current task count.
   *
   * @param size Task count.
   */
  public void showTaskCount(int size) {
    System.out.println("\nYou now have " + size + " tasks in the list.");
  }

  /**
   * Displays an error message.
   *
   * @param message Error text.
   */
  public void showError(String message) {
    System.err.println(message);
  }

  /** Displays an error when loading fails. */
  public void showLoadingError() {
    System.err.println("I couldn't load your tasks from disk.");
  }

  /** Displays an error when saving fails. */
  public void showSavingError() {
    System.err.println("I couldn't save your tasks to disk.");
  }
}
