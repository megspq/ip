import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Runs the Bob task-management chatbot.
 */
public class Bob {
    private static final String DIVIDER = "____________________________________________________________";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Task> tasks = new ArrayList<>();

        String banner = " ____        _     \n"
                + "| __ )  ___ | |__  \n"
                + "|  _ \\ / _ \\| '_ \\ \n"
                + "| |_) | (_) | |_) |\n"
                + "|____/ \\___/|_.__/ \n";

        System.out.println(banner);
        System.out.println("hello im bob !!");
        System.out.println("how can i help :)");
        System.out.println(DIVIDER);

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();

            if (input.equals("bye")) {
                System.out.println("  yippee glad to have helped (＠＾◡＾)");
                System.out.println("  byebye !! have a good day (๑˃ᴗ˂)ﻭ");
                System.out.println(DIVIDER);
                break;
            }

            try {
                if (input.equals("list")) {
                    printTasks(tasks);
                } else if (input.equals("mark") || input.startsWith("mark ")) {
                    int taskIndex = parseTaskIndex(input, "mark", tasks.size());
                    tasks.get(taskIndex).markAsDone();
                    System.out.println(" yippee task done, i've marked it as so:");
                    System.out.println("   " + tasks.get(taskIndex));
                } else if (input.equals("unmark") || input.startsWith("unmark ")) {
                    int taskIndex = parseTaskIndex(input, "unmark", tasks.size());
                    tasks.get(taskIndex).markAsNotDone();
                    System.out.println(" okie, i've marked this task incomplete:");
                    System.out.println("   " + tasks.get(taskIndex));
                } else if (input.equals("delete") || input.startsWith("delete ")) {
                    int taskIndex = parseTaskIndex(input, "delete", tasks.size());
                    Task removedTask = tasks.remove(taskIndex);
                    System.out.println(" okays here's the task i deleted: ");
                    System.out.println("   " + removedTask);
                    System.out.println(" pls get to the remaining " + tasks.size() + " tasks in your list");
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
                System.out.println(" oopsies !! (´ ∀ ` *) " + exception.getMessage());
            }
            System.out.println(DIVIDER);
        }
        scanner.close();
    }

    private static void printTasks(List<Task> tasks) {
        System.out.println(" here are your tasks (⌒‿⌒) 加油 !! :");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
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
    private static void addDeadline(List<Task> tasks, String input) throws BobException {
        int byPosition = input.indexOf(" /by");
        if (byPosition == -1) {
            throw new BobException("a deadline needs /by and a date or time, eg play /by today");
        }
        String description = input.substring("deadline".length(), byPosition).trim();
        String by = input.substring(byPosition + " /by".length()).trim();
        requireNotEmpty(description, "pls give a desc before /by.");
        requireNotEmpty(by, "pls give a date or time after /by.");
        addTask(tasks, new Deadline(description, by));
    }

    /**
     * Validates and adds an event command's description, start, and end.
     */
    private static void addEvent(List<Task> tasks, String input) throws BobException {
        int fromPosition = input.indexOf(" /from");
        int toPosition = input.indexOf(" /to");
        if (fromPosition == -1 || toPosition == -1 || toPosition < fromPosition) {
            throw new BobException("an event needs /from and /to, eg event meeting /from 2pm /to 4pm");
        }
        String description = input.substring("event".length(), fromPosition).trim();
        String from = input.substring(fromPosition + " /from".length(), toPosition).trim();
        String to = input.substring(toPosition + " /to".length()).trim();
        requireNotEmpty(description, "pls gimme event desc before /from.");
        requireNotEmpty(from, "pls gimme start time after /from.");
        requireNotEmpty(to, "pls gimme end time after /to.");
        addTask(tasks, new Event(description, from, to));
    }

    private static void requireNotEmpty(String value, String message) throws BobException {
        if (value.isEmpty()) {
            throw new BobException(message);
        }
    }

    private static void addTask(List<Task> tasks, Task task) {
        tasks.add(task);
        System.out.println(" okays task added:");
        System.out.println("   " + task);
        System.out.println(" you now have " + tasks.size() + " tasks in the list, get to it !!");
    }
}
