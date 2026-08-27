/**
 * Represents a validated user command.
 */
public abstract class Command {
    /**
     * Returns whether this command should end the application.
     *
     * @return {@code true} only for an exit command
     */
    public boolean isExit() {
        return false;
    }
}
