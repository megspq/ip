package bob.command;

import bob.Storage;
import bob.Ui;
import bob.task.TaskList;

/**
 * Represents a request to exit Bob.
 */
public class ExitCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
