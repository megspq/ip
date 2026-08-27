/**
 * Represents a request to mark one task as complete.
 */
public class MarkCommand extends Command {
    private final int taskIndex;

    /**
     * Creates a command for a zero-based task index.
     *
     * @param taskIndex index of the task to mark
     */
    public MarkCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    /**
     * Returns the zero-based task index.
     *
     * @return index of the task to mark
     */
    public int getTaskIndex() {
        return taskIndex;
    }
}
