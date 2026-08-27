import java.io.IOException;

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

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws IOException {
        tasks.add(task);
        try {
            storage.save(tasks.asList());
        } catch (IOException exception) {
            tasks.delete(tasks.size() - 1);
            throw exception;
        }
        ui.showAdded(task, tasks.size());
    }
}
