package bob;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

import bob.command.AddCommand;
import bob.command.Command;
import bob.command.DeleteCommand;
import bob.command.ExitCommand;
import bob.command.ListCommand;
import bob.command.MarkCommand;
import bob.command.UnmarkCommand;
import bob.task.Deadline;
import bob.task.Event;
import bob.task.Task;
import bob.task.Todo;

/**
 * Interprets user input and converts it into commands that Bob can execute.
 */
public class Parser {
    private static final DateTimeFormatter EVENT_INPUT_FORMAT = DateTimeFormatter
            .ofPattern("uuuu-MM-dd HHmm")
            .withResolverStyle(ResolverStyle.STRICT);

    private Parser() {
        // This class contains only stateless parsing operations.
    }

    /**
     * Parses one line of input and validates its command syntax.
     *
     * @param input command entered by the user
     * @return command data ready for Bob to execute
     * @throws BobException if the command or any of its arguments is invalid
     */
    public static Command parse(String input) throws BobException {
        if (input.equals("bye")) {
            return new ExitCommand();
        } else if (input.equals("list")) {
            return new ListCommand();
        } else if (input.equals("mark") || input.startsWith("mark ")) {
            return new MarkCommand(parseTaskNumber(input, "mark"));
        } else if (input.equals("unmark") || input.startsWith("unmark ")) {
            return new UnmarkCommand(parseTaskNumber(input, "unmark"));
        } else if (input.equals("delete") || input.startsWith("delete ")) {
            return new DeleteCommand(parseTaskNumber(input, "delete"));
        } else if (input.equals("todo") || input.startsWith("todo ")) {
            return new AddCommand(parseTodo(input));
        } else if (input.equals("deadline") || input.startsWith("deadline ")) {
            return new AddCommand(parseDeadline(input));
        } else if (input.equals("event") || input.startsWith("event ")) {
            return new AddCommand(parseEvent(input));
        }
        throw new BobException("pls try either one of list, todo, deadline, event, mark, unmark, delete, or bye");
    }

    /**
     * Extracts and validates the one-based task number following a command word.
     *
     * @param input complete user input
     * @param command command word preceding the number
     * @return parsed one-based task number
     * @throws BobException if the number is missing or is not an integer
     */
    private static int parseTaskNumber(String input, String command) throws BobException {
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
        return taskNumber;
    }

    /**
     * Parses a to-do command and requires a non-empty description.
     *
     * @param input complete user input
     * @return to-do task described by the input
     * @throws BobException if the description is empty
     */
    private static Todo parseTodo(String input) throws BobException {
        String description = input.substring("todo".length()).trim();
        requireNotEmpty(description, "oopsies a todo needs a desc, eg: todo sleep");
        return new Todo(description);
    }

    /**
     * Parses a deadline command containing a description and ISO date.
     *
     * @param input complete user input
     * @return deadline task described by the input
     * @throws BobException if required fields are missing or the date is invalid
     */
    private static Deadline parseDeadline(String input) throws BobException {
        int byPosition = input.indexOf(" /by");
        if (byPosition == -1) {
            throw new BobException("a deadline needs /by and a date, eg play /by 2019-12-02");
        }
        String description = input.substring("deadline".length(), byPosition).trim();
        String by = input.substring(byPosition + " /by".length()).trim();
        requireNotEmpty(description, "pls give a desc before /by.");
        requireNotEmpty(by, "pls give a date after /by.");
        try {
            return new Deadline(description, LocalDate.parse(by));
        } catch (DateTimeParseException exception) {
            throw new BobException("use yyyy-MM-dd for deadline dates, eg 2019-12-02");
        }
    }

    /**
     * Parses an event command containing a description, start, and end time.
     *
     * @param input complete user input
     * @return event task described by the input
     * @throws BobException if required fields are missing, a date-time is invalid,
     *         or the event ends before it starts
     */
    private static Event parseEvent(String input) throws BobException {
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
            return new Event(description, start, end);
        } catch (DateTimeParseException exception) {
            throw new BobException("use yyyy-MM-dd HHmm for event dates and times, eg 2019-12-02 1800");
        }
    }

    /**
     * Rejects an empty required command field with the supplied user-facing message.
     *
     * @param value field value to validate
     * @param message message to use when validation fails
     * @throws BobException if {@code value} is empty
     */
    private static void requireNotEmpty(String value, String message) throws BobException {
        if (value.isEmpty()) {
            throw new BobException(message);
        }
    }
}
