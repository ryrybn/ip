public class NukNagnel {
    private static void printHoriLine() {
        System.out.println("\n____________________________________________________________");
    }

    private static void printIntro() {
        printHoriLine();
        String intro = " Hello! I'm NukNagnel\n" +
                " What can I do for you?";
        System.out.print(intro);
        printHoriLine();
    }

    private static void printOutro() {
        String exit = " Bye. Hope to see you again soon!";
        System.out.print(exit);
        printHoriLine();
    }

    public static void main(String[] args) {
        printIntro();
        printOutro();
    }
}