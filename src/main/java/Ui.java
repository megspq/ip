import java.util.List;
import java.util.Scanner;

/**
 * Handles all console input and output for Bob.
 */
public class Ui {
    private static final String DIVIDER = "____________________________________________________________";
    private final Scanner scanner;

    /**
     * Creates a user interface that reads from standard input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays Bob's greeting when the application starts.
     */
    public void showWelcome() {
        String banner = " ____        _     \n"
                + "| __ )  ___ | |__  \n"
                + "|  _ \\ / _ \\| '_ \\ \n"
                + "| |_) | (_) | |_) |\n"
                + "|____/ \\___/|_.__/ \n";

        System.out.println(banner);
        System.out.println("hello im bob !!");
        System.out.println("how can i help :)");
        showDivider();
    }

    /**
     * Returns whether another command is available from the user.
     *
     * @return {@code true} when standard input has another line
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next command and removes surrounding whitespace.
     *
     * @return the next user command
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /**
     * Displays Bob's farewell.
     */
    public void showGoodbye() {
        System.out.println("  yippee glad to have helped (＠＾◡＾)");
        System.out.println("  byebye !! have a good day (๑˃ᴗ˂)ﻭ");
    }

    /**
     * Displays the current tasks with one-based numbering.
     *
     * @param tasks tasks to display
     */
    public void showTasks(List<Task> tasks) {
        System.out.println(" here are your tasks (⌒‿⌒) 加油 !! :");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Displays confirmation that a task was marked complete.
     *
     * @param task task that was marked
     */
    public void showMarked(Task task) {
        System.out.println(" yippee task done, i've marked it as so:");
        System.out.println("   " + task);
    }

    /**
     * Displays confirmation that a task was marked incomplete.
     *
     * @param task task that was unmarked
     */
    public void showUnmarked(Task task) {
        System.out.println(" okie, i've marked this task incomplete:");
        System.out.println("   " + task);
    }

    /**
     * Displays confirmation that a task was deleted.
     *
     * @param task deleted task
     * @param remainingTaskCount number of tasks left
     */
    public void showDeleted(Task task, int remainingTaskCount) {
        System.out.println(" okays here's the task i deleted: ");
        System.out.println("   " + task);
        System.out.println(" pls get to the remaining " + remainingTaskCount + " tasks in your list");
    }

    /**
     * Displays confirmation that a task was added.
     *
     * @param task added task
     * @param taskCount new number of tasks
     */
    public void showAdded(Task task, int taskCount) {
        System.out.println(" okays task added:");
        System.out.println("   " + task);
        System.out.println(" you now have " + taskCount + " tasks in the list, get to it !!");
    }

    /**
     * Displays an error message to the user.
     *
     * @param message explanation of the problem
     */
    public void showError(String message) {
        System.out.println(" oopsies !! (´ ∀ ` *) " + message);
    }

    /**
     * Displays the separator between command responses.
     */
    public void showDivider() {
        System.out.println(DIVIDER);
    }

    /**
     * Releases the input scanner when Bob exits.
     */
    public void close() {
        scanner.close();
    }
}
