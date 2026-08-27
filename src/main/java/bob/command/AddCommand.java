package bob.command;

import java.io.IOException;

import bob.Storage;
import bob.Ui;
import bob.task.Task;
import bob.task.TaskList;

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
     * Adds and saves the task, rolling back the addition if saving fails.
     *
     * @param tasks task list to update
     * @param ui user interface used for confirmation
     * @param storage storage used to persist the updated list
     * @throws IOException if the updated task list cannot be saved
     */
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
