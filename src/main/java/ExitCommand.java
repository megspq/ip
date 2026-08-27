/**
 * Represents a request to exit Bob.
 */
public class ExitCommand extends Command {
    @Override
    public boolean isExit() {
        return true;
    }
}
