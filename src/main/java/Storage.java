import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads and saves Bob's tasks using a file on the local hard disk.
 */
public class Storage {
    private final Path filePath;

    /**
     * Creates storage that writes to the given file.
     *
     * @param filePath location of the task data file
     */
    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads all tasks from the data file.
     * An empty task list is returned when Bob has not created the file yet.
     *
     * @return tasks stored in the data file, in their original order
     * @throws IOException if an existing data file cannot be read
     */
    public List<Task> load() throws IOException {
        if (!Files.exists(filePath)) {
            return new ArrayList<>();
        }

        List<Task> tasks = new ArrayList<>();
        for (String line : Files.readAllLines(filePath)) {
            tasks.add(parseTask(line));
        }
        return tasks;
    }

    /**
     * Reconstructs one task from its pipe-delimited storage fields.
     */
    private Task parseTask(String line) {
        String[] fields = line.split(" \\| ", -1);
        Task task = switch (fields[0]) {
        case "T" -> new Todo(fields[2]);
        case "D" -> new Deadline(fields[2], fields[3]);
        case "E" -> new Event(fields[2], fields[3], fields[4]);
        default -> throw new IllegalArgumentException("Unknown task type: " + fields[0]);
        };

        if (fields[1].equals("1")) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Replaces the data file with the current task list.
     *
     * @param tasks current tasks to save
     * @throws IOException if the directory or file cannot be written
     */
    public void save(List<Task> tasks) throws IOException {
        Files.createDirectories(filePath.getParent());
        List<String> lines = tasks.stream()
                .map(Task::toStorageString)
                .toList();
        Files.write(filePath, lines);
    }
}
