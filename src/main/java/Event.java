/**
 * Represents a task that takes place between a start and an end date or time.
 * Both values are kept as text so users can enter any useful format.
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an incomplete event task.
     *
     * @param description description of the event
     * @param from date or time at which the event starts
     * @param to date or time at which the event ends
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toStorageString() {
        return "E | " + super.toStorageString() + " | "
                + escapeStorageField(from) + " | " + escapeStorageField(to);
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
