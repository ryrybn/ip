import java.util.*;

public class NukNagnel {
    private static void printHoriLine() {
        System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }

    private static void printIntro() {
        printHoriLine();
        String intro = " Good day friend! My name is NukNagnel.\n" +
                " What can I do you for?";
        System.out.print(intro);
        printHoriLine();
    }

    private static void printOutro() {
        String exit = " This conversation has ended.\n Hope to chat again soon and have a splendid day ahead!";
        System.out.print(exit);
        printHoriLine();
    }

    private static void echoComd() {
        Scanner scanner = new Scanner(System.in);
        String userInput = "";
        do {
            System.out.print(" > ");
            userInput = scanner.next();
            printHoriLine();
            System.out.print(userInput);
            printHoriLine();
        } while (!userInput.equals("bye"));
    }

    public static void main(String[] args) {
        printIntro();
        echoComd();
        printOutro();
    }
}