import java.util.ArrayList;
import java.util.List;

/**
 * Owns Bob's task collection and provides operations for changing it.
 */
public class TaskList {
    private final List<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list containing the supplied tasks in the same order.
     *
     * @param tasks initial tasks
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Returns the task at a zero-based index.
     *
     * @param index index of the task
     * @return task at the given index
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns the number of tasks in this list.
     *
     * @return current task count
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Deletes and returns the task at a zero-based index.
     *
     * @param index index of the task to delete
     * @return deleted task
     */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    /**
     * Marks a task as completed.
     *
     * @param index index of the task to mark
     */
    public void mark(int index) {
        tasks.get(index).markAsDone();
    }

    /**
     * Marks a task as incomplete.
     *
     * @param index index of the task to unmark
     */
    public void unmark(int index) {
        tasks.get(index).markAsNotDone();
    }

    /**
     * Returns a read-only snapshot for displaying or saving the tasks.
     *
     * @return unmodifiable snapshot of the current tasks
     */
    public List<Task> asList() {
        return List.copyOf(tasks);
    }

    /**
     * Restores a deleted task to its original position after a failed save.
     *
     * @param index original index of the task
     * @param task task to restore
     */
    void restoreDeletedTask(int index, Task task) {
        tasks.add(index, task);
    }

    /**
     * Restores a task's previous completion state after a failed save.
     *
     * @param index index of the task to restore
     * @param wasDone completion state before the attempted change
     */
    void restoreDoneState(int index, boolean wasDone) {
        if (wasDone) {
            mark(index);
        } else {
            unmark(index);
        }
    }
}
