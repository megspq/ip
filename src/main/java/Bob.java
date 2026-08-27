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

        while (ui.hasNextCommand()) {
            String input = ui.readCommand();

            try {
                Parser.ParsedCommand command = Parser.parse(input, tasks.size());
                if (command.getType() == Parser.CommandType.BYE) {
                    ui.showGoodbye();
                    break;
                } else if (command.getType() == Parser.CommandType.LIST) {
                    ui.showTasks(tasks.asList());
                } else if (command.getType() == Parser.CommandType.MARK) {
                    int taskIndex = command.getTaskIndex();
                    setDone(taskIndex, true);
                    ui.showMarked(tasks.get(taskIndex));
                } else if (command.getType() == Parser.CommandType.UNMARK) {
                    int taskIndex = command.getTaskIndex();
                    setDone(taskIndex, false);
                    ui.showUnmarked(tasks.get(taskIndex));
                } else if (command.getType() == Parser.CommandType.DELETE) {
                    int taskIndex = command.getTaskIndex();
                    Task removedTask = deleteTask(taskIndex);
                    ui.showDeleted(removedTask, tasks.size());
                } else if (command.getType() == Parser.CommandType.ADD) {
                    addTask(command.getTask());
                }
            } catch (BobException exception) {
                ui.showError(exception.getMessage());
            } catch (IOException exception) {
                ui.showError("couldn't save your tasks; nothing was changed");
            }
            ui.showDivider();
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
     * Changes a task's status and restores it if saving fails.
     */
    private void setDone(int taskIndex, boolean isDone) throws IOException {
        Task task = tasks.get(taskIndex);
        boolean wasDone = task.isDone();
        if (isDone) {
            tasks.mark(taskIndex);
        } else {
            tasks.unmark(taskIndex);
        }
        try {
            storage.save(tasks.asList());
        } catch (IOException exception) {
            tasks.restoreDoneState(taskIndex, wasDone);
            throw exception;
        }
    }

    /**
     * Deletes a task and restores its position if saving fails.
     */
    private Task deleteTask(int taskIndex) throws IOException {
        Task removedTask = tasks.delete(taskIndex);
        try {
            storage.save(tasks.asList());
            return removedTask;
        } catch (IOException exception) {
            tasks.restoreDeletedTask(taskIndex, removedTask);
            throw exception;
        }
    }

    private void addTask(Task task) throws IOException {
        tasks.add(task);
        try {
            storage.save(tasks.asList());
        } catch (IOException exception) {
            tasks.delete(tasks.size() - 1);
            throw exception;
        }
        ui.showAdded(task, tasks.size());
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
