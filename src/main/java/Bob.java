import java.util.Scanner;

public class Bob {
    private static final int MAX_TASKS = 100;
    private static final String DIVIDER = "____________________________________________________________";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;

        String banner = " ____        _     \n"
                + "| __ )  ___ | |__  \n"
                + "|  _ \\ / _ \\| '_ \\ \n"
                + "| |_) | (_) | |_) |\n"
                + "|____/ \\___/|_.__/ \n";

        System.out.println(banner);
        System.out.println("hello im bob !!");
        System.out.println("how can i help :)");
        System.out.println(DIVIDER);

        while (true) {
            String input = scanner.nextLine();

            if (input.equals("bye")) {
                System.out.println("  yippee glad to have helped (＠＾◡＾)");
                System.out.println("  byebye !! have a good day (๑˃ᴗ˂)ﻭ");
                System.out.println(DIVIDER);
                break;
            }

            if (input.equals("list")) {
                System.out.println(" here are your tasks (⌒‿⌒) 加油 !! :");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println(" " + (i + 1) + "." + tasks[i]);
                }
            } else if (input.startsWith("mark ")) {
                int taskIndex = Integer.parseInt(input.substring(5).trim()) - 1;
                tasks[taskIndex].markAsDone();
                System.out.println(" yippee task done, i've marked it as so:");
                System.out.println("   " + tasks[taskIndex]);
            } else if (input.startsWith("unmark ")) {
                int taskIndex = Integer.parseInt(input.substring(7).trim()) - 1;
                tasks[taskIndex].markAsNotDone();
                System.out.println(" okie, i've marked this task incomplete:");
                System.out.println("   " + tasks[taskIndex]);
            } else {
                tasks[taskCount] = new Task(input);
                taskCount++;
                System.out.println("added: " + input);
            }
            System.out.println(DIVIDER);
        }
        scanner.close();
    }
}
