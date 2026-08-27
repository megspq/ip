/**
 * Represents a request to mark one task as incomplete.
 */
public class UnmarkCommand extends Command {
    private final int taskIndex;

    /**
     * Creates a command for a zero-based task index.
     *
     * @param taskIndex index of the task to unmark
     */
    public UnmarkCommand(int taskIndex) {
        this.taskIndex = taskIndex;
    }

    /**
     * Returns the zero-based task index.
     *
     * @return index of the task to unmark
     */
    public int getTaskIndex() {
        return taskIndex;
    }
}
