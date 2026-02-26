package nuknagnel;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    this.filePath = Paths.get(filePath);
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
    } catch (IOException e) {
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
      Files.createDirectories(filePath.getParent());
      List<String> lines = new ArrayList<>();
      for (Task task : tasks.getTasks()) {
        assert task != null : "Persisted task entries must not be null.";
        lines.add(serializeTask(task));
      }
      Files.write(filePath, lines);
    } catch (IOException e) {
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
          task = new Deadline(description, DateTimeParser.parseDate(parts[3]));
        } catch (InvalidInputException e) {
          return null;
        }
        break;
      case "E":
        if (parts.length != 5) {
          return null;
        }
        try {
          task =
              new Event(
                  description,
                  DateTimeParser.parseDateTime(parts[3]),
                  DateTimeParser.parseDateTime(parts[4]));
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
    if (task instanceof Deadline) {
      Deadline deadline = (Deadline) task;
      return String.join(" | ", "D", status, task.getDescription(), deadline.getBy().toString());
    }
    if (task instanceof Event) {
      Event event = (Event) task;
      return String.join(
          " | ",
          "E",
          status,
          task.getDescription(),
          event.getFrom().toString(),
          event.getTo().toString());
    }
    return String.join(" | ", "T", status, task.getDescription());
  }
}
