import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;

/**
 * Runs the Bob task-management chatbot.
 */
public class Bob {
    private static final Storage STORAGE = new Storage(Path.of("data", "bob.txt"));
    private static final Ui UI = new Ui();
    private static final DateTimeFormatter EVENT_INPUT_FORMAT = DateTimeFormatter
            .ofPattern("uuuu-MM-dd HHmm")
            .withResolverStyle(ResolverStyle.STRICT);

    public static void main(String[] args) {
        UI.showWelcome();

        List<Task> tasks = loadTasks();

        while (UI.hasNextCommand()) {
            String input = UI.readCommand();

            if (input.equals("bye")) {
                UI.showGoodbye();
                break;
            }

            try {
                if (input.equals("list")) {
                    UI.showTasks(tasks);
                } else if (input.equals("mark") || input.startsWith("mark ")) {
                    int taskIndex = parseTaskIndex(input, "mark", tasks.size());
                    setDone(tasks, taskIndex, true);
                    UI.showMarked(tasks.get(taskIndex));
                } else if (input.equals("unmark") || input.startsWith("unmark ")) {
                    int taskIndex = parseTaskIndex(input, "unmark", tasks.size());
                    setDone(tasks, taskIndex, false);
                    UI.showUnmarked(tasks.get(taskIndex));
                } else if (input.equals("delete") || input.startsWith("delete ")) {
                    int taskIndex = parseTaskIndex(input, "delete", tasks.size());
                    Task removedTask = deleteTask(tasks, taskIndex);
                    UI.showDeleted(removedTask, tasks.size());
                } else if (input.equals("todo") || input.startsWith("todo ")) {
                    String description = input.substring(4).trim();
                    requireNotEmpty(description, "oopsies a todo needs a desc, eg: todo sleep");
                    addTask(tasks, new Todo(description));
                } else if (input.equals("deadline") || input.startsWith("deadline ")) {
                    addDeadline(tasks, input);
                } else if (input.equals("event") || input.startsWith("event ")) {
                    addEvent(tasks, input);
                } else {
                    throw new BobException("pls try either one of list, todo, deadline, event, mark, unmark, delete, or bye");
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
    private static List<Task> loadTasks() {
        try {
            return STORAGE.load();
        } catch (StorageException exception) {
            UI.showError("couldn't load saved tasks: " + exception.getMessage());
            UI.showDivider();
            return new ArrayList<>();
        }
    }

    /**
     * Changes a task's status and restores it if saving fails.
     */
    private static void setDone(List<Task> tasks, int taskIndex, boolean isDone) throws IOException {
        Task task = tasks.get(taskIndex);
        boolean wasDone = task.isDone();
        if (isDone) {
            task.markAsDone();
        } else {
            task.markAsNotDone();
        }
        try {
            STORAGE.save(tasks);
        } catch (IOException exception) {
            if (wasDone) {
                task.markAsDone();
            } else {
                task.markAsNotDone();
            }
            throw exception;
        }
    }

    /**
     * Deletes a task and restores its position if saving fails.
     */
    private static Task deleteTask(List<Task> tasks, int taskIndex) throws IOException {
        Task removedTask = tasks.remove(taskIndex);
        try {
            STORAGE.save(tasks);
            return removedTask;
        } catch (IOException exception) {
            tasks.add(taskIndex, removedTask);
            throw exception;
        }
    }

    private static int parseTaskIndex(String input, String command, int taskCount) throws BobException {
        String numberText = input.substring(command.length()).trim();
        if (numberText.isEmpty()) {
            throw new BobException("can't help if idk which task no");
        }

        final int taskNumber;
        try {
            taskNumber = Integer.parseInt(numberText);
        } catch (NumberFormatException exception) {
            throw new BobException("enter a valid task no pls");
        }
        if (taskCount == 0) {
            throw new BobException("can't do anyth if there's no task");
        }
        if (taskNumber < 1 || taskNumber > taskCount) {
            throw new BobException("task doesn't exist, whats your fav no from 1 to " + taskCount + "?");
        }
        return taskNumber - 1;
    }

    /**
     * Validates and adds a deadline command's description and deadline.
     */
    private static void addDeadline(List<Task> tasks, String input) throws BobException, IOException {
        int byPosition = input.indexOf(" /by");
        if (byPosition == -1) {
            throw new BobException("a deadline needs /by and a date, eg play /by 2019-12-02");
        }
        String description = input.substring("deadline".length(), byPosition).trim();
        String by = input.substring(byPosition + " /by".length()).trim();
        requireNotEmpty(description, "pls give a desc before /by.");
        requireNotEmpty(by, "pls give a date after /by.");
        try {
            addTask(tasks, new Deadline(description, LocalDate.parse(by)));
        } catch (DateTimeParseException exception) {
            throw new BobException("use yyyy-MM-dd for deadline dates, eg 2019-12-02");
        }
    }

    /**
     * Validates and adds an event command's description, start, and end.
     */
    private static void addEvent(List<Task> tasks, String input) throws BobException, IOException {
        int fromPosition = input.indexOf(" /from");
        int toPosition = input.indexOf(" /to");
        if (fromPosition == -1 || toPosition == -1 || toPosition < fromPosition) {
            throw new BobException("an event needs /from and /to, eg event meeting /from 2019-12-02 1400 /to 2019-12-02 1600");
        }
        String description = input.substring("event".length(), fromPosition).trim();
        String from = input.substring(fromPosition + " /from".length(), toPosition).trim();
        String to = input.substring(toPosition + " /to".length()).trim();
        requireNotEmpty(description, "pls gimme event desc before /from.");
        requireNotEmpty(from, "pls gimme start time after /from.");
        requireNotEmpty(to, "pls gimme end time after /to.");
        try {
            LocalDateTime start = LocalDateTime.parse(from, EVENT_INPUT_FORMAT);
            LocalDateTime end = LocalDateTime.parse(to, EVENT_INPUT_FORMAT);
            if (end.isBefore(start)) {
                throw new BobException("an event's end cannot be before its start");
            }
            addTask(tasks, new Event(description, start, end));
        } catch (DateTimeParseException exception) {
            throw new BobException("use yyyy-MM-dd HHmm for event dates and times, eg 2019-12-02 1800");
        }
    }

    private static void requireNotEmpty(String value, String message) throws BobException {
        if (value.isEmpty()) {
            throw new BobException(message);
        }
    }

    private static void addTask(List<Task> tasks, Task task) throws IOException {
        tasks.add(task);
        try {
            STORAGE.save(tasks);
        } catch (IOException exception) {
            tasks.remove(tasks.size() - 1);
            throw exception;
        }
        UI.showAdded(task, tasks.size());
    }
}
