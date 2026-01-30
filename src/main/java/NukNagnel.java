import java.util.*;

public class NukNagnel {
    private ArrayList<Task> items = new ArrayList<>();

    // Sample data to test functionality
//    private void samplePopulate() {
//        items.add(new ToDo("read book"));
//        items.get(items.size() - 1).markAsDone();
//        items.add(new Deadline("return book", "June 6th"));
//        items.add(new Event("project meeting", "Aug 6th 2pm", "4pm"));
//        items.add(new ToDo("joins sports club"));
//        items.get(items.size() - 1).markAsDone();
//        items.add(new ToDo("borrow book"));
//    }

    private void printNoOfItems(ArrayList<Task> items) {
        System.out.println("\nNow you have " + items.size() + " tasks stored in the list.");
    }

    private void printHoriLine() {
        System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    private void printIntro() {
        printHoriLine();
        String intro = " Good day friend! My name is NukNagnel.\n" +
                " What can I do you for?";
        System.out.print(intro);
        printHoriLine();
    }

    private void printOutro() {
        String exit = " This conversation has ended.\n Hope to chat again soon and have a splendid day ahead!";
        System.out.print(exit);
        printHoriLine();
    }

    private void taskTracker() {
        Scanner scanner = new Scanner(System.in);
        String userInput = "";
        do {
            System.out.print(" > ");
            userInput = scanner.nextLine();
            if (userInput.equals("bye")) break;
            printHoriLine();
            Scanner userInputScanner = new Scanner(userInput);
            Task item;
            String command = userInputScanner.next();
            switch(command) {
                case "list":
                    printItemsList();
                    break;
                case "mark":
                    item = items.get(userInputScanner.nextInt()-1);
                    item.markAsDone();
                    System.out.println("Awesome! The task below has been marked as *done*:");
                    System.out.print(item);
                    break;
                case "unmark":
                    item = items.get(userInputScanner.nextInt()-1);
                    item.markAsUndone();
                    System.out.println("Alright, I have marked this task as *not done yet*");
                    System.out.print(item);
                    break;
                case "delete":
                    int id = userInputScanner.nextInt()-1;
                    item = items.get(id);
                    items.remove(id);
                    System.out.println("The task has been successfully removed.");
                    System.out.print(item);
                    printNoOfItems(items);
                    break;
                default:
                    storeItem(command, userInputScanner.nextLine());
            }
            printHoriLine();
        } while (!userInput.equals("bye"));
    }

    private void printItemsList() {
        System.out.println("Below are the tasks stored in your list:");
        for (int i = 0; i < items.size(); i++) {
            Task item = items.get(i);
            if (item == null) break;
            String s = String.valueOf(i+1) + ". " + item;
            System.out.println(s);
        }
    }

    private void storeItem(String type, String item) {
        String[] cmd;
        Task task = new Task("");
        switch (type) {
            case "todo":
                task = new ToDo(item);
                break;
            case "deadline":
                cmd = item.split("/by ");
                task = new Deadline(cmd[0].stripTrailing(), cmd[1]);
                break;
            case "event":
                cmd = item.split("/from ");
                String desc = cmd[0].stripTrailing();;
                cmd = cmd[1].split(" /to ");
                String from = cmd[0];
                String to = cmd[1];
                task = new Event(desc, from, to);
                break;
        }
        items.add(task);
        System.out.println("I have added the task as requested:\n" + task);
        printNoOfItems(items);
    }

    public static void main(String[] args) {
        NukNagnel chat = new NukNagnel();
//        chat.samplePopulate();
        chat.printIntro();
        chat.taskTracker();
        chat.printOutro();
    }
}
