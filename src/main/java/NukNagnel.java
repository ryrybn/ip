import java.util.*;

public class NukNagnel {
    private String[] items = new String[100];
    private int itemsPtr = 0;

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

    private void echoComd() {
        Scanner scanner = new Scanner(System.in);
        String userInput = "";
        do {
            System.out.print(" > ");
            userInput = scanner.nextLine();
            printHoriLine();
            if (userInput.equals("list")) {
                printItemsList();
            } else {
                storeItem(userInput);
            }
            printHoriLine();
        } while (!userInput.equals("bye"));
    }

    private void printItemsList() {
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) break;
            System.out.print(i+1);
            System.out.print(". ");
            System.out.println(items[i]);
        }
    }

    private void storeItem(String item) {
        items[itemsPtr] = item;
        itemsPtr++;
        System.out.println("added: " + item);
    }

    public static void main(String[] args) {
        NukNagnel chat = new NukNagnel();
        chat.printIntro();
        chat.echoComd();
        chat.printOutro();
    }
}