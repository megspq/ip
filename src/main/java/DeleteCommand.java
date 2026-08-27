import java.io.IOException;

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

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws IOException {
        Task removedTask = tasks.delete(taskIndex);
        try {
            storage.save(tasks.asList());
        } catch (IOException exception) {
            tasks.restoreDeletedTask(taskIndex, removedTask);
            throw exception;
        }
        ui.showDeleted(removedTask, tasks.size());
    }
}
