/**
 * Represents a task that must be completed by a given date or time.
 * The date or time is kept as text so users can enter any useful format.
 */
public class Deadline extends Task {
    private final String by;

    /**
     * Creates an incomplete deadline task.
     *
     * @param description description of the task
     * @param by date or time by which the task should be completed
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toStorageString() {
        return "D | " + super.toStorageString() + " | " + by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}
