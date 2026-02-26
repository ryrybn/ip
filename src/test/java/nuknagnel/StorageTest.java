package nuknagnel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class StorageTest {

  @Test
  public void load_missingFile_returnsEmptyList() throws Exception {
    Path nonExistent = Path.of("build", "tmp", "missing-" + System.nanoTime() + ".txt");
    Storage storage = new Storage(nonExistent.toString());

    ArrayList<Task> loaded = storage.load();

    assertTrue(loaded.isEmpty());
  }

  @Test
  public void saveAndLoad_roundTrip_preservesTaskTypesAndState() throws Exception {
    Path tempFile = Files.createTempFile("nuknagnel-storage-roundtrip", ".txt");
    try {
      Storage storage = new Storage(tempFile.toString());
      TaskList tasks = new TaskList();
      tasks.add(new ToDo("read"));
      Deadline deadline = new Deadline("submit", LocalDate.of(2024, 2, 1));
      deadline.markAsDone();
      tasks.add(deadline);
      tasks.add(new Event("sync", LocalDateTime.of(2024, 2, 1, 10, 0), LocalDateTime.of(2024, 2, 1, 11, 0)));

      storage.save(tasks);
      ArrayList<Task> loaded = storage.load();

      assertEquals(3, loaded.size());
      assertInstanceOf(ToDo.class, loaded.get(0));
      assertInstanceOf(Deadline.class, loaded.get(1));
      assertInstanceOf(Event.class, loaded.get(2));
      assertEquals("X", loaded.get(1).getStatusIcon());
    } finally {
      Files.deleteIfExists(tempFile);
    }
  }

  @Test
  public void load_corruptedLines_skipsInvalidRecords() throws Exception {
    Path tempFile = Files.createTempFile("nuknagnel-storage-corrupt", ".txt");
    try {
      Files.write(
          tempFile,
          List.of(
              "T | 0 | keep me",
              "X | 0 | bad type",
              "D | 0 | bad date | 2024-02-30",
              "E | 0 | bad range | 2024-02-01T12:00 | 2024-02-01T12:00",
              "T | 2 | bad status"));

      Storage storage = new Storage(tempFile.toString());
      ArrayList<Task> loaded = storage.load();

      assertEquals(1, loaded.size());
      assertEquals("keep me", loaded.get(0).getDescription());
    } finally {
      Files.deleteIfExists(tempFile);
    }
  }

  @Test
  public void constructor_invalidPath_throwsIllegalArgumentException() {
    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> new Storage("bad\0path"));

    assertTrue(exception.getMessage().contains("Storage path is invalid."));
  }

  @Test
  public void save_withDirectoryPath_throwsDataLoadingException() throws IOException {
    Path tempDir = Files.createTempDirectory("nuknagnel-storage-dir");
    try {
      Storage storage = new Storage(tempDir.toString());
      TaskList tasks = new TaskList();
      tasks.add(new ToDo("read"));

      DataLoadingException exception =
          assertThrows(DataLoadingException.class, () -> storage.save(tasks));
      assertEquals("Unable to save tasks to disk.", exception.getMessage());
    } finally {
      Files.deleteIfExists(tempDir);
    }
  }
}
