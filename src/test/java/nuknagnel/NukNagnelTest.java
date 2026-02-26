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
}
