import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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
     * @throws StorageException if an existing data file cannot be read or is malformed
     */
    public List<Task> load() throws StorageException {
        if (Files.notExists(filePath)) {
            return new ArrayList<>();
        }

        List<Task> tasks = new ArrayList<>();
        final List<String> lines;
        try {
            lines = Files.readAllLines(filePath);
        } catch (IOException exception) {
            throw new StorageException("couldn't read the data file", exception);
        }

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.isBlank()) {
                continue;
            }
            try {
                tasks.add(parseTask(line));
            } catch (IllegalArgumentException exception) {
                throw new StorageException("invalid data on line " + (i + 1), exception);
            }
        }
        return tasks;
    }

    /**
     * Reconstructs one task from its pipe-delimited storage fields.
     */
    private Task parseTask(String line) {
        List<String> fieldList = splitFields(line);
        String[] fields = fieldList.toArray(String[]::new);
        if (fields.length < 2 || (!fields[1].equals("0") && !fields[1].equals("1"))) {
            throw new IllegalArgumentException("Invalid completion status");
        }

        Task task = switch (fields[0]) {
        case "T" -> {
            requireFieldCount(fields, 3);
            yield new Todo(requireText(fields[2]));
        }
        case "D" -> {
            requireFieldCount(fields, 4);
            yield new Deadline(requireText(fields[2]), requireText(fields[3]));
        }
        case "E" -> {
            requireFieldCount(fields, 5);
            yield new Event(requireText(fields[2]), requireText(fields[3]), requireText(fields[4]));
        }
        default -> throw new IllegalArgumentException("Unknown task type: " + fields[0]);
        };

        if (fields[1].equals("1")) {
            task.markAsDone();
        }
        return task;
    }

    /**
     * Splits a record while decoding escaped pipes and backslashes.
     */
    private List<String> splitFields(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean isEscaped = false;
        for (int i = 0; i < line.length(); i++) {
            char character = line.charAt(i);
            if (isEscaped) {
                if (character != '|' && character != '\\') {
                    throw new IllegalArgumentException("Invalid escape sequence");
                }
                field.append(character);
                isEscaped = false;
            } else if (character == '\\') {
                isEscaped = true;
            } else if (character == '|') {
                fields.add(field.toString().trim());
                field.setLength(0);
            } else {
                field.append(character);
            }
        }
        if (isEscaped) {
            throw new IllegalArgumentException("Incomplete escape sequence");
        }
        fields.add(field.toString().trim());
        return fields;
    }

    private void requireFieldCount(String[] fields, int expectedCount) {
        if (fields.length != expectedCount) {
            throw new IllegalArgumentException("Wrong number of fields");
        }
    }

    private String requireText(String value) {
        if (value.isBlank()) {
            throw new IllegalArgumentException("Task fields cannot be empty");
        }
        return value;
    }

    /**
     * Replaces the data file with the current task list.
     *
     * @param tasks current tasks to save
     * @throws IOException if the directory or file cannot be written
     */
    public void save(List<Task> tasks) throws IOException {
        Path parent = filePath.getParent();
        if (parent == null) {
            parent = Path.of(".");
        }
        Files.createDirectories(parent);
        List<String> lines = tasks.stream()
                .map(Task::toStorageString)
                .toList();
        Path temporaryFile = Files.createTempFile(parent, "bob-", ".tmp");
        try {
            Files.write(temporaryFile, lines);
            try {
                Files.move(temporaryFile, filePath, StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
            } catch (java.nio.file.AtomicMoveNotSupportedException exception) {
                Files.move(temporaryFile, filePath, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporaryFile);
        }
    }
}
