package nuknagnel;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores and manages a list of tasks.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list with the provided tasks.
     *
     * @param tasks Existing tasks to wrap.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to the list.
     *
     * @param task Task to add.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Returns the task at the specified index.
     *
     * @param index Zero-based index.
     * @return Task at the index.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Removes and returns the task at the specified index.
     *
     * @param index Zero-based index.
     * @return Removed task.
     */
    public Task remove(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return Task count.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the backing task list.
     *
     * @return Tasks list.
     */
    public List<Task> getTasks() {
        return tasks;
    }
}
