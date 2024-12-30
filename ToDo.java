import java.io.*;
import java.util.*;

class Task {
    private String title;
    private String date;
    private boolean isCompleted;

    public Task(String title, String date) {
        this.title = title;
        this.date = date;
        this.isCompleted = false;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void markAsCompleted() {
        this.isCompleted = true;
    }

    @Override
    public String toString() {
        return title + " | " + date + " | " + (isCompleted ? "Completed" : "Not Completed");
    }
}

public class ToDo {
    private static final String FILE_NAME = "TaskManager/tasks.txt";
    private List<Task> tasks = new ArrayList<>();

    public void addTask(String title, String date) {
        tasks.add(new Task(title, date));
        System.out.println("Task added successfully.");
    }

    public void deleteTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.remove(index);
            System.out.println("Task deleted successfully.");
        } else {
            System.out.println("Invalid task index.");
        }
    }

    public void markTaskAsCompleted(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.get(index).markAsCompleted();
            System.out.println("Task marked as completed.");
        } else {
            System.out.println("Invalid task index.");
        }
    }

    public void viewTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks available.");
        } else {
            System.out.println("\n# | Title                          |    Date       | Status");
            System.out.println("--------------------------------------------------------------");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.printf("%d | %-30s | %-12s | %-15s\n", (i + 1), tasks.get(i).getTitle(), tasks.get(i).getDate(), (tasks.get(i).isCompleted() ? "Completed" : "Not Completed"));
            }
        }
    }

    public void saveTasksToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            writer.write("Title|Date|Status");
            writer.newLine();
            for (Task task : tasks) {
                writer.write(task.getTitle() + "|" + task.getDate() + "|" + task.isCompleted());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    public void loadTasksFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("No existing task file found. A new one will be created.");
            return;
        }

        tasks.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Skip the header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    Task task = new Task(parts[0], parts[1]);
                    if (Boolean.parseBoolean(parts[2])) {
                        task.markAsCompleted();
                    }
                    tasks.add(task);
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ToDo manager = new ToDo();
        Scanner scanner = new Scanner(System.in);

        manager.loadTasksFromFile();

        while (true) {
            System.out.println("\n=============================");
            System.out.println("       Task Manager Menu     ");
            System.out.println("=============================");
            System.out.println("1. Add Task");
            System.out.println("2. Delete Task");
            System.out.println("3. Mark Task as Completed");
            System.out.println("4. View Tasks");
            System.out.println("5. 4Exit");
            System.out.println("=============================");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter task title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter task due date (YYYY-MM-DD): ");
                    String date = scanner.nextLine();
                    manager.addTask(title, date);
                    break;
                case 2:
                    System.out.print("Enter task index to delete: ");
                    int deleteIndex = scanner.nextInt();
                    manager.deleteTask(deleteIndex - 1);
                    break;
                case 3:
                    System.out.println("Available tasks:");
                    manager.viewTasks();
                    System.out.print("Enter task index to mark as completed: ");
                    int completeIndex = scanner.nextInt();
                    manager.markTaskAsCompleted(completeIndex - 1);
                    break;
                case 4:
                    manager.viewTasks();
                    break;
                case 5:
                    manager.saveTasksToFile();
                    System.out.println("Tasks saved. Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
