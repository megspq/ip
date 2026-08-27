package bob.command;

import java.io.IOException;

import bob.BobException;
import bob.Storage;
import bob.Ui;
import bob.task.Task;
import bob.task.TaskList;

/**
 * Represents a request to mark one task as incomplete.
 */
public class UnmarkCommand extends Command {
    private final int taskNumber;

    /**
     * Creates a command for a one-based task number.
     *
     * @param taskNumber number of the task to unmark
     */
    public UnmarkCommand(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws BobException, IOException {
        int taskIndex = tasks.getTaskIndex(taskNumber);
        Task task = tasks.get(taskIndex);
        boolean wasDone = task.isDone();
        tasks.unmark(taskIndex);
        try {
            storage.save(tasks.asList());
        } catch (IOException exception) {
            tasks.restoreDoneState(taskIndex, wasDone);
            throw exception;
        }
        ui.showUnmarked(task);
    }
}
