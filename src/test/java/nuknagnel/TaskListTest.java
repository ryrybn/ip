package nuknagnel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class TaskListTest {

  @Test
  public void addGetRemove_sizeUpdated() {
    TaskList list = new TaskList();
    Task todo = new ToDo("read");

    list.add(todo);
    assertEquals(1, list.size());
    assertEquals(todo, list.get(0));
    assertEquals(todo, list.remove(0));
    assertEquals(0, list.size());
  }

  @Test
  public void containsEquivalent_matchesByTaskTypeAndDetails() {
    TaskList list = new TaskList();
    list.add(new ToDo("read"));
    list.add(new Deadline("submit", LocalDate.of(2024, 2, 1)));
    list.add(new Event("sync", LocalDateTime.of(2024, 2, 1, 10, 0), LocalDateTime.of(2024, 2, 1, 11, 0)));

    assertTrue(list.containsEquivalent(new ToDo("read")));
    assertFalse(list.containsEquivalent(new ToDo("write")));
    assertTrue(list.containsEquivalent(new Deadline("submit", LocalDate.of(2024, 2, 1))));
    assertFalse(list.containsEquivalent(new Deadline("submit", LocalDate.of(2024, 2, 2))));
    assertTrue(
        list.containsEquivalent(
            new Event("sync", LocalDateTime.of(2024, 2, 1, 10, 0), LocalDateTime.of(2024, 2, 1, 11, 0))));
    assertFalse(
        list.containsEquivalent(
            new Event("sync", LocalDateTime.of(2024, 2, 1, 10, 0), LocalDateTime.of(2024, 2, 1, 12, 0))));
  }
}
