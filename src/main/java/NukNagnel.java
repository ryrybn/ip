import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class NukNagnel {
    private ArrayList<Task> items = new ArrayList<>();
    private static final Path DATA_FILE = Paths.get("data", "nuknagnel.txt");

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
            try {
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
                        item = items.get(userInputScanner.nextInt() - 1);
                        item.markAsDone();
                        saveToDisk();
                        System.out.println("Awesome! The task below has been marked as *done*:");
                        System.out.print(item);
                        break;
                    case "unmark":
                        item = items.get(userInputScanner.nextInt() - 1);
                        item.markAsUndone();
                        saveToDisk();
                        System.out.println("Alright, I have marked this task as *not done yet*");
                        System.out.print(item);
                        break;
                    case "delete":
                        int id = userInputScanner.nextInt() - 1;
                        item = items.get(id);
                        items.remove(id);
                        saveToDisk();
                        System.out.println("The task has been successfully removed.");
                        System.out.print(item);
                        printNoOfItems(items);
                        break;
                    case "todo":
                    case "deadline":
                    case "event":
                        storeItem(command, userInputScanner.nextLine());
                        break;
                    default:
                        throw new InvalidInputException();
                }
            } catch (InvalidInputException e) {
                System.err.println(e.getMessage());
            } catch (NoSuchElementException e) {
                System.err.println("Please input the required parameters for your command.");
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
                if (!item.contains("/by ")) {
                    throw new InvalidInputException("Deadline tasks must include /by <date>.");
                }
                cmd = item.split("/by ", 2);
                task = new Deadline(cmd[0].stripTrailing(), parseDate(cmd[1].trim()));
                break;
            case "event":
                if (!item.contains("/from ") || !item.contains(" /to ")) {
                    throw new InvalidInputException("Event tasks must include /from <start> /to <end>.");
                }
                cmd = item.split("/from ", 2);
                String desc = cmd[0].stripTrailing();;
                cmd = cmd[1].split(" /to ", 2);
                String from = cmd[0].trim();
                String to = cmd[1].trim();
                task = new Event(desc, parseDateTime(from), parseDateTime(to));
                break;
        }
        items.add(task);
        saveToDisk();
        System.out.println("I have added the task as requested:\n" + task);
        printNoOfItems(items);
    }

    private void loadFromDisk() {
        if (Files.notExists(DATA_FILE)) {
            return;
        }
        try {
            List<String> lines = Files.readAllLines(DATA_FILE);
            for (String line : lines) {
                Task task = parseTask(line);
                if (task == null) {
                    System.err.println("Skipping corrupted line in data file: " + line);
                    continue;
                }
                items.add(task);
            }
        } catch (IOException e) {
            System.err.println("Unable to load tasks from disk.");
        }
    }

    private Task parseTask(String line) {
        String trimmed = line.strip();
        if (trimmed.isEmpty()) {
            return null;
        }
        String[] parts = trimmed.split("\\s*\\|\\s*");
        if (parts.length < 3) {
            return null;
        }
        String type = parts[0];
        String status = parts[1];
        String description = parts[2];
        Task task;
        switch (type) {
            case "T":
                if (parts.length != 3) {
                    return null;
                }
                task = new ToDo(description);
                break;
            case "D":
                if (parts.length != 4) {
                    return null;
                }
                try {
                    task = new Deadline(description, parseDate(parts[3]));
                } catch (InvalidInputException e) {
                    return null;
                }
                break;
            case "E":
                if (parts.length != 5) {
                    return null;
                }
                try {
                    task = new Event(description, parseDateTime(parts[3]), parseDateTime(parts[4]));
                } catch (InvalidInputException e) {
                    return null;
                }
                break;
            default:
                return null;
        }
        if ("1".equals(status)) {
            task.markAsDone();
        } else if (!"0".equals(status)) {
            return null;
        }
        return task;
    }

    private LocalDate parseDate(String raw) {
        try {
            return LocalDate.parse(raw);
        } catch (DateTimeParseException e) {
            throw new InvalidInputException("Please use yyyy-mm-dd for dates.");
        }
    }

    private LocalDateTime parseDateTime(String raw) {
        try {
            return LocalDateTime.parse(raw);
        } catch (DateTimeParseException e) {
            // fall through to custom formats
        }
        DateTimeFormatter[] formats = new DateTimeFormatter[] {
                DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        };
        for (DateTimeFormatter format : formats) {
            try {
                return LocalDateTime.parse(raw, format);
            } catch (DateTimeParseException e) {
                // try next format
            }
        }
        throw new InvalidInputException("Please use yyyy-mm-dd HHmm or yyyy-mm-dd HH:mm for date-time.");
    }

    private void saveToDisk() {
        try {
            Files.createDirectories(DATA_FILE.getParent());
            List<String> lines = new ArrayList<>();
            for (Task task : items) {
                lines.add(serializeTask(task));
            }
            Files.write(DATA_FILE, lines);
        } catch (IOException e) {
            System.err.println("Unable to save tasks to disk.");
        }
    }

    private String serializeTask(Task task) {
        String status = task.isDone ? "1" : "0";
        if (task instanceof ToDo) {
            return String.join(" | ", "T", status, task.getDescription());
        }
        if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            return String.join(" | ", "D", status, task.getDescription(), deadline.getBy().toString());
        }
        if (task instanceof Event) {
            Event event = (Event) task;
            return String.join(" | ", "E", status, task.getDescription(),
                    event.getFrom().toString(), event.getTo().toString());
        }
        return String.join(" | ", "T", status, task.getDescription());
    }

    public static void main(String[] args) {
        NukNagnel chat = new NukNagnel();
//        chat.samplePopulate();
        chat.loadFromDisk();
        chat.printIntro();
        chat.taskTracker();
        chat.printOutro();
    }
}
