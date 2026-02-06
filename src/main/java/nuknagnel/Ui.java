package nuknagnel;

import java.util.List;
import java.util.Scanner;

public class Ui {
    private final Scanner scanner = new Scanner(System.in);

    public void showWelcome() {
        showLine();
        String intro = " Good day friend! My name is NukNagnel.\n" +
                " What can I do you for?";
        System.out.print(intro);
        showLine();
    }

    public void showGoodbye() {
        String exit = " This conversation has ended.\n Hope to chat again soon and have a splendid day ahead!";
        System.out.print(exit);
        showLine();
    }

    public void showLine() {
        System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    public String readCommand() {
        System.out.print(" > ");
        return scanner.nextLine();
    }

    public void showTaskList(TaskList tasks) {
        System.out.println("Below are the tasks stored in your list:");
        List<Task> items = tasks.getTasks();
        for (int i = 0; i < items.size(); i++) {
            String s = String.valueOf(i + 1) + ". " + items.get(i);
            System.out.println(s);
        }
    }

    public void showTaskAdded(Task task, int size) {
        System.out.println("I have added the task as requested:\n" + task);
        showTaskCount(size);
    }

    public void showTaskDeleted(Task task, int size) {
        System.out.println("The task has been successfully removed.");
        System.out.print(task);
        showTaskCount(size);
    }

    public void showMarked(Task task) {
        System.out.println("Awesome! The task below has been marked as *done*:");
        System.out.print(task);
    }

    public void showUnmarked(Task task) {
        System.out.println("Alright, I have marked this task as *not done yet*");
        System.out.print(task);
    }

    public void showTaskCount(int size) {
        System.out.println("\nNow you have " + size + " tasks stored in the list.");
    }

    public void showError(String message) {
        System.err.println(message);
    }

    public void showLoadingError() {
        System.err.println("Unable to load tasks from disk.");
    }

    public void showSavingError() {
        System.err.println("Unable to save tasks to disk.");
    }
}
