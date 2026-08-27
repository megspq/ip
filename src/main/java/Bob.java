import java.io.IOException;
import java.nio.file.Path;

/**
 * Runs the Bob task-management chatbot.
 */
public class Bob {
    private final Storage storage;
    private final Ui ui;
    private TaskList tasks;

    /**
     * Creates a Bob application that saves tasks at the given file path.
     *
     * @param filePath location of the task data file
     */
    public Bob(String filePath) {
        this.storage = new Storage(Path.of(filePath));
        this.ui = new Ui();
        this.tasks = new TaskList();
    }

    /**
     * Starts Bob's command-reading loop and runs until the user exits or input ends.
     */
    public void run() {
        ui.showWelcome();

        tasks = loadTasks();
        boolean isExit = false;

        while (!isExit && ui.hasNextCommand()) {
            String input = ui.readCommand();

            try {
                Command command = Parser.parse(input, tasks.size());
                command.execute(tasks, ui, storage);
                isExit = command.isExit();
            } catch (BobException exception) {
                ui.showError(exception.getMessage());
            } catch (IOException exception) {
                ui.showError("couldn't save your tasks; nothing was changed");
            } finally {
                ui.showDivider();
            }
        }
        ui.close();
    }

    /**
     * Loads saved tasks, falling back to an empty list if the data cannot be used.
     */
    private TaskList loadTasks() {
        try {
            return new TaskList(storage.load());
        } catch (StorageException exception) {
            ui.showError("couldn't load saved tasks: " + exception.getMessage());
            ui.showDivider();
            return new TaskList();
        }
    }

    /**
     * Starts Bob using the default task data file.
     *
     * @param args command-line arguments, which Bob does not use
     */
    public static void main(String[] args) {
        new Bob("data/bob.txt").run();
    }
}
