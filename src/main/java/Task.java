/**
 * Represents a task and whether it has been completed.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates a task that is initially not done.
     *
     * @param description description of the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Marks this task as completed.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as not completed.
     */
    public void markAsNotDone() {
        this.isDone = false;
    }

    /**
     * Returns the character used to display this task's completion state.
     *
     * @return {@code "X"} if done, or a space otherwise
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    /**
     * Returns whether this task has been completed.
     *
     * @return {@code true} if this task is done
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Returns the completion state in the compact form used for storage.
     *
     * @return {@code "1"} if done, or {@code "0"} otherwise
     */
    protected String getStorageStatus() {
        return isDone ? "1" : "0";
    }

    /**
     * Escapes storage separators so task text can contain pipes or backslashes.
     *
     * @param value field value to escape
     * @return escaped field value
     */
    protected String escapeStorageField(String value) {
        return value.replace("\\", "\\\\").replace("|", "\\|");
    }

    /**
     * Returns this task in the application's storage format.
     * Subclasses include their task type and any additional fields.
     *
     * @return one line suitable for writing to the task data file
     */
    public String toStorageString() {
        return getStorageStatus() + " | " + escapeStorageField(description);
    }

    /**
     * Returns this task in the format used by Bob's task list.
     *
     * @return the status icon followed by the task description
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
