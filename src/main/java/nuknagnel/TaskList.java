package nuknagnel;

import java.util.ArrayList;
import java.util.List;

public class TaskList {
  private final ArrayList<Task> tasks;

  public TaskList() {
    this.tasks = new ArrayList<>();
  }

  public TaskList(ArrayList<Task> tasks) {
    this.tasks = tasks;
  }

  public void add(Task task) {
    tasks.add(task);
  }

  public Task get(int index) {
    return tasks.get(index);
  }

  public Task remove(int index) {
    return tasks.remove(index);
  }

  public int size() {
    return tasks.size();
  }

  public List<Task> getTasks() {
    return tasks;
  }

  public List<Task> find(String keyword) {
    String normalizedKeyword = keyword.trim().toLowerCase();
    List<Task> matches = new ArrayList<>();
    for (Task task : tasks) {
      if (task.getDescription().toLowerCase().contains(normalizedKeyword)) {
        matches.add(task);
      }
    }
    return matches;
  }
}
