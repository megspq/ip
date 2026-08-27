/**
 * Represents a request to delete one task.
 */
public class DeleteCommand extends Command {
    private final int taskIndex;

    /**
     * Creates a command for a zero-based task index.
     *
     * @param taskIndex index of the task to delete
     */
    public DeleteCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    /**
     * Returns the zero-based task index.
     *
     * @return index of the task to delete
     */
    public int getTaskIndex() {
        return taskIndex;
    }
}
