import java.util.*;

public class NukNagnel {
    private Task[] items = new Task[100];
    private int itemsPtr = 0;

    // Sample data to test functionality
//    private void samplePopulate() {
//        items[0] = new Task("read book");
//        items[0].markAsDone();
//        items[1] = new Task("return book");
//        items[2] = new Task("buy bread");
//        itemsPtr = 3;
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
            printHoriLine();
            Scanner userInputScanner = new Scanner(userInput);
            Task item;
            switch(userInputScanner.next()) {
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
                    storeItem(userInput);
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

    private void storeItem(String item) {
        items[itemsPtr] = new Task(item);
        itemsPtr++;
        System.out.println("added: " + item);
    }

    public static void main(String[] args) {
        NukNagnel chat = new NukNagnel();
//        chat.samplePopulate();
        chat.printIntro();
        chat.taskTracker();
        chat.printOutro();
    }
}