package nuknagnel;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

public class NukNagnelTest {

  @Test
  public void getResponse_duplicateTask_rejected() throws IOException {
    Path tempFile = Files.createTempFile("nuknagnel-duplicate", ".txt");
    try {
      NukNagnel app = new NukNagnel(tempFile.toString());

      app.getResponse("todo read book");
      String secondResponse = app.getResponse("todo read book");

      assertTrue(secondResponse.contains("That task is already on your board."));
    } finally {
      Files.deleteIfExists(tempFile);
    }
  }

  @Test
  public void getResponse_list_skipsCorruptedEventInStorage() throws IOException {
    Path tempFile = Files.createTempFile("nuknagnel-corrupt", ".txt");
    try {
      Files.write(
          tempFile,
          List.of(
              "E | 0 | sync | 2024-02-01T13:00 | 2024-02-01T13:00",
              "T | 0 | valid task"));

      NukNagnel app = new NukNagnel(tempFile.toString());
      String response = app.getResponse("list");

      assertTrue(response.contains("valid task"));
      assertFalse(response.contains("sync"));
    } finally {
      Files.deleteIfExists(tempFile);
    }
  }

  @Test
  public void getResponse_fullTaskLifecycle_success() throws IOException {
    Path tempFile = Files.createTempFile("nuknagnel-lifecycle", ".txt");
    try {
      NukNagnel app = new NukNagnel(tempFile.toString());

      String addResponse = app.getResponse("todo read book");
      String markResponse = app.getResponse("mark 1");
      String unmarkResponse = app.getResponse("unmark 1");
      String deleteResponse = app.getResponse("delete 1");

      assertTrue(addResponse.contains("Added to your list:"));
      assertTrue(markResponse.contains("marked done"));
      assertTrue(unmarkResponse.contains("marked not done"));
      assertTrue(deleteResponse.contains("Removed this task:"));
      assertTrue(deleteResponse.contains("0 tasks in the list."));
    } finally {
      Files.deleteIfExists(tempFile);
    }
  }

  @Test
  public void getResponse_bye_setsExitFlag() throws IOException {
    Path tempFile = Files.createTempFile("nuknagnel-bye", ".txt");
    try {
      NukNagnel app = new NukNagnel(tempFile.toString());

      String response = app.getResponse("bye");

      assertTrue(response.contains("Session closed."));
      assertTrue(app.isExit());
    } finally {
      Files.deleteIfExists(tempFile);
    }
  }

  @Test
  public void getResponse_invalidIndex_returnsFriendlyError() throws IOException {
    Path tempFile = Files.createTempFile("nuknagnel-invalid-index", ".txt");
    try {
      NukNagnel app = new NukNagnel(tempFile.toString());

      String response = app.getResponse("mark 1");

      assertTrue(response.contains("That task number doesn't exist."));
    } finally {
      Files.deleteIfExists(tempFile);
    }
  }

  @Test
  public void getResponse_saveFailure_returnsStorageError() throws IOException {
    Path tempDir = Files.createTempDirectory("nuknagnel-save-failure");
    try {
      NukNagnel app = new NukNagnel(tempDir.toString());

      String response = app.getResponse("todo read book");

      assertTrue(response.contains("I couldn't save your tasks to disk."));
    } finally {
      Files.deleteIfExists(tempDir);
    }
  }
}
