import java.io.IOException;
import java.nio.file.Path;

/**
 * Runs the Bob task-management chatbot.
 */
public class Bob {
    private static final Storage STORAGE = new Storage(Path.of("data", "bob.txt"));
    private static final Ui UI = new Ui();

    public static void main(String[] args) {
        UI.showWelcome();

        TaskList tasks = loadTasks();

        while (UI.hasNextCommand()) {
            String input = UI.readCommand();

            try {
                Parser.ParsedCommand command = Parser.parse(input, tasks.size());
                if (command.getType() == Parser.CommandType.BYE) {
                    UI.showGoodbye();
                    break;
                } else if (command.getType() == Parser.CommandType.LIST) {
                    UI.showTasks(tasks.asList());
                } else if (command.getType() == Parser.CommandType.MARK) {
                    int taskIndex = command.getTaskIndex();
                    setDone(tasks, taskIndex, true);
                    UI.showMarked(tasks.get(taskIndex));
                } else if (command.getType() == Parser.CommandType.UNMARK) {
                    int taskIndex = command.getTaskIndex();
                    setDone(tasks, taskIndex, false);
                    UI.showUnmarked(tasks.get(taskIndex));
                } else if (command.getType() == Parser.CommandType.DELETE) {
                    int taskIndex = command.getTaskIndex();
                    Task removedTask = deleteTask(tasks, taskIndex);
                    UI.showDeleted(removedTask, tasks.size());
                } else if (command.getType() == Parser.CommandType.ADD) {
                    addTask(tasks, command.getTask());
                }
            } catch (BobException exception) {
                UI.showError(exception.getMessage());
            } catch (IOException exception) {
                UI.showError("couldn't save your tasks; nothing was changed");
            }
            UI.showDivider();
        }
        UI.close();
    }

    /**
     * Loads saved tasks, falling back to an empty list if the data cannot be used.
     */
    private static TaskList loadTasks() {
        try {
            return new TaskList(STORAGE.load());
        } catch (StorageException exception) {
            UI.showError("couldn't load saved tasks: " + exception.getMessage());
            UI.showDivider();
            return new TaskList();
        }
    }

    /**
     * Changes a task's status and restores it if saving fails.
     */
    private static void setDone(TaskList tasks, int taskIndex, boolean isDone) throws IOException {
        Task task = tasks.get(taskIndex);
        boolean wasDone = task.isDone();
        if (isDone) {
            tasks.mark(taskIndex);
        } else {
            tasks.unmark(taskIndex);
        }
        try {
            STORAGE.save(tasks.asList());
        } catch (IOException exception) {
            tasks.restoreDoneState(taskIndex, wasDone);
            throw exception;
        }
    }

    /**
     * Deletes a task and restores its position if saving fails.
     */
    private static Task deleteTask(TaskList tasks, int taskIndex) throws IOException {
        Task removedTask = tasks.delete(taskIndex);
        try {
            STORAGE.save(tasks.asList());
            return removedTask;
        } catch (IOException exception) {
            tasks.restoreDeletedTask(taskIndex, removedTask);
            throw exception;
        }
    }

    private static void addTask(TaskList tasks, Task task) throws IOException {
        tasks.add(task);
        try {
            STORAGE.save(tasks.asList());
        } catch (IOException exception) {
            tasks.delete(tasks.size() - 1);
            throw exception;
        }
        UI.showAdded(task, tasks.size());
    }
}
