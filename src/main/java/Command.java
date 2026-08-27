import java.io.IOException;

/**
 * Represents a validated user command.
 */
public abstract class Command {
    /**
     * Performs this command using the application's task list, UI, and storage.
     *
     * @param tasks task list to query or change
     * @param ui user interface used for command feedback
     * @param storage storage used to persist changes
     * @throws IOException if a changed task list cannot be saved
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws IOException;

    /**
     * Returns whether this command should end the application.
     *
     * @return {@code true} only for an exit command
     */
    public boolean isExit() {
        return false;
    }
}
