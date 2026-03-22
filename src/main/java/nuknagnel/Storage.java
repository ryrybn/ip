package nuknagnel;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Handles loading and saving tasks to disk. */
public class Storage {
  private final Path filePath;

  /**
   * Creates a storage helper for the given file path.
   *
   * @param filePath Storage file path.
   */
  public Storage(String filePath) {
    assert filePath != null : "Storage file path must not be null.";
    assert !filePath.trim().isEmpty() : "Storage file path must not be blank.";
    try {
      this.filePath = Paths.get(filePath);
    } catch (InvalidPathException e) {
      throw new IllegalArgumentException("Storage path is invalid.", e);
    }
    assert this.filePath.getFileName() != null : "Storage path should include a file name.";
  }

  /**
   * Loads tasks from disk.
   *
   * @return List of tasks.
   * @throws DataLoadingException If loading fails.
   */
  public ArrayList<Task> load() throws DataLoadingException {
    if (Files.notExists(filePath)) {
      return new ArrayList<>();
    }
    try {
      List<String> lines = Files.readAllLines(filePath);
      ArrayList<Task> tasks = new ArrayList<>();
      for (String line : lines) {
        Task task = parseTask(line);
        if (task == null) {
          System.err.println("Skipping corrupted line in data file: " + line);
          continue;
        }
        tasks.add(task);
      }
      return tasks;
    } catch (IOException | SecurityException e) {
      throw new DataLoadingException("Unable to load tasks from disk.", e);
    }
  }

  /**
   * Saves tasks to disk.
   *
   * @param tasks Task list to persist.
   * @throws DataLoadingException If saving fails.
   */
  public void save(TaskList tasks) throws DataLoadingException {
    assert tasks != null : "Task list to save must not be null.";
    try {
      Path parent = filePath.getParent();
      if (parent != null) {
        Files.createDirectories(parent);
      }
      List<String> lines = new ArrayList<>();
      for (Task task : tasks.getTasks()) {
        assert task != null : "Persisted task entries must not be null.";
        lines.add(serializeTask(task));
      }
      Files.write(filePath, lines);
    } catch (IOException | SecurityException e) {
      throw new DataLoadingException("Unable to save tasks to disk.", e);
    }
  }

  /**
   * Parses a serialized task line into a {@link Task}.
   *
   * @param line Serialized task line.
   * @return Task instance, or null if the line is invalid.
   */
  private Task parseTask(String line) {
    if (line == null) {
      return null;
    }
    String trimmed = line.strip();
    if (trimmed.isEmpty()) {
      return null;
    }
    String[] parts = trimmed.split("\\s*\\|\\s*");
    if (parts.length < 3) {
      return null;
    }
    String status = parts[1];
    String description = parts[2];
    Task task = parseTaskByType(parts[0], description, parts);
    if (task == null) {
      return null;
    }
    if (!applyStatus(task, status)) {
      return null;
    }
    return task;
  }

  private Task parseTaskByType(String type, String description, String[] parts) {
    return switch (type) {
      case "T" -> parseTodo(description, parts);
      case "D" -> parseDeadline(description, parts);
      case "E" -> parseEvent(description, parts);
      default -> null;
    };
  }

  private Task parseTodo(String description, String[] parts) {
    if (parts.length != 3) {
      return null;
    }
    return new ToDo(description);
  }

  private Task parseDeadline(String description, String[] parts) {
    if (parts.length != 4) {
      return null;
    }
    try {
      return new Deadline(description, DateTimeParser.parseDate(parts[3]));
    } catch (InvalidInputException e) {
      return null;
    }
  }

  private Task parseEvent(String description, String[] parts) {
    if (parts.length != 5) {
      return null;
    }
    try {
      LocalDateTime from = DateTimeParser.parseDateTime(parts[3]);
      LocalDateTime to = DateTimeParser.parseDateTime(parts[4]);
      if (!to.isAfter(from)) {
        return null;
      }
      return new Event(description, from, to);
    } catch (InvalidInputException e) {
      return null;
    }
  }

  private boolean applyStatus(Task task, String status) {
    if ("1".equals(status)) {
      task.markAsDone();
      return true;
    }
    return "0".equals(status);
  }

  /**
   * Serializes a task into a single line representation.
   *
   * @param task Task to serialize.
   * @return Serialized task line.
   */
  private String serializeTask(Task task) {
    assert task != null : "Task to serialize must not be null.";
    String status = task.isDone ? "1" : "0";
    if (task instanceof ToDo) {
      return String.join(" | ", "T", status, task.getDescription());
    }
    if (task instanceof Deadline deadline) {
      return String.join(" | ", "D", status, task.getDescription(), deadline.getBy().toString());
    }
    if (task instanceof Event event) {
      return String.join(
          " | ",
          "E",
          status,
          task.getDescription(),
          event.getFrom().toString(),
          event.getTo().toString());
    }
    assert false : "Unsupported task subtype: " + task.getClass().getName();
    throw new IllegalStateException("Unsupported task subtype: " + task.getClass().getName());
  }
}
