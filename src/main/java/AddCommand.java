/**
 * Represents a request to add a task.
 */
public class AddCommand extends Command {
    private final Task task;

    /**
     * Creates a command that adds the given task.
     *
     * @param task task to add
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    /**
     * Returns the task to add.
     *
     * @return task created from the user's input
     */
    public Task getTask() {
        return task;
    }
}
