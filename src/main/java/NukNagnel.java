import java.util.*;

public class NukNagnel {
    private Task[] items = new Task[100];
    private int itemsPtr = 0;

    // Sample data to test functionality
//    private void samplePopulate() {
//        items[0] = new ToDo("read book");
//        items[0].markAsDone();
//        items[1] = new Deadline("return book", "June 6th");
//        items[2] = new Event("project meeting", "Aug 6th 2pm", "4pm");
//        items[3] = new ToDo("joins sports club");
//        items[3].markAsDone();
//        itemsPtr = 4;
//    }

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
                    item = items[userInputScanner.nextInt()-1];
                    item.markAsDone();
                    System.out.println("Awesome! The task below has been marked as *done*:");
                    System.out.print(item);
                    break;
                case "unmark":
                    item = items[userInputScanner.nextInt()-1];
                    item.markAsUndone();
                    System.out.println("Alright, I have marked this task as *not done yet*");
                    System.out.print(item);
                    break;
                default:
                    storeItem(command, userInputScanner.nextLine());
            }
            printHoriLine();
        } while (!userInput.equals("bye"));
    }

    private void printItemsList() {
        System.out.println("Below are the tasks stored in your list:");
        for (int i = 0; i < items.length; i++) {
            Task item = items[i];
            if (item == null) break;
            String s = String.valueOf(i+1) + ". " + item;
            System.out.println(s);
        }
    }

    private void storeItem(String type, String item) {
        String[] cmd;
        switch (type) {
            case "todo":
                items[itemsPtr] = new ToDo(item);
                break;
            case "deadline":
                cmd = item.split("/by ");
                items[itemsPtr] = new Deadline(cmd[0].stripTrailing(), cmd[1]);
                break;
            case "event":
                cmd = item.split("/from ");
                String desc = cmd[0].stripTrailing();;
                cmd = cmd[1].split(" /to ");
                String from = cmd[0];
                String to = cmd[1];
                items[itemsPtr] = new Event(desc, from, to);
                break;
        }
        System.out.println("I have added the task as requested:\n" + items[itemsPtr]);
        System.out.println("Now you have " + String.valueOf(++itemsPtr) + " tasks stored in the list.");
    }

    public static void main(String[] args) {
        NukNagnel chat = new NukNagnel();
//        chat.samplePopulate();
        chat.printIntro();
        chat.taskTracker();
        chat.printOutro();
    }
}