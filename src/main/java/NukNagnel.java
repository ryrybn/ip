import java.util.Scanner;

public class NukNagnel {
    public static void main(String[] args) {
        String intro = "____________________________________________________________\n" +
                " Hello! I'm NukNagnel\n" +
                " What can I do for you?\n" +
                "____________________________________________________________";
        System.out.println(intro);
        Scanner scanner = new Scanner(System.in);
        String userInput = "";
        do {
            userInput = scanner.next();
            System.out.println("____________________________________________________________");
            System.out.println(userInput);
            System.out.println("____________________________________________________________");
        } while (!userInput.equals("bye"));
        String exit = " Bye. Hope to see you again soon!\n" +
                "____________________________________________________________\n";
        System.out.println(exit);
    }
}
