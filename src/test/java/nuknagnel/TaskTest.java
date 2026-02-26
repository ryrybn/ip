package nuknagnel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class TaskTest {

  @Test
  public void task_statusToggle_success() {
    Task task = new Task("read");

    assertEquals(" ", task.getStatusIcon());
    task.markAsDone();
    assertEquals("X", task.getStatusIcon());
    task.markAsUndone();
    assertEquals(" ", task.getStatusIcon());
  }

  @Test
  public void task_isSameTask_comparesTypeAndDescription() {
    Task task = new Task("read");

    assertTrue(task.isSameTask(new Task("read")));
    assertFalse(task.isSameTask(new ToDo("read")));
    assertFalse(task.isSameTask(new Task("write")));
    assertFalse(task.isSameTask(null));
  }

  @Test
  public void deadline_isSameTask_comparesDateToo() {
    Deadline first = new Deadline("submit", LocalDate.of(2024, 2, 1));
    Deadline same = new Deadline("submit", LocalDate.of(2024, 2, 1));
    Deadline different = new Deadline("submit", LocalDate.of(2024, 2, 2));

    assertTrue(first.isSameTask(same));
    assertFalse(first.isSameTask(different));
  }

  @Test
  public void event_isSameTask_comparesRangeToo() {
    Event first = new Event("sync", LocalDateTime.of(2024, 2, 1, 10, 0), LocalDateTime.of(2024, 2, 1, 11, 0));
    Event same = new Event("sync", LocalDateTime.of(2024, 2, 1, 10, 0), LocalDateTime.of(2024, 2, 1, 11, 0));
    Event different = new Event("sync", LocalDateTime.of(2024, 2, 1, 10, 30), LocalDateTime.of(2024, 2, 1, 11, 30));

    assertTrue(first.isSameTask(same));
    assertFalse(first.isSameTask(different));
  }
}
