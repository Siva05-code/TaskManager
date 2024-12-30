import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.io.*;

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

public class TaskManagerUI {
    private static final String FILE_NAME = "TaskManager/tasks.txt";
    private List<Task> tasks = new ArrayList<>();  // Ensure tasks is initialized correctly
    private DefaultListModel<String> taskListModel = new DefaultListModel<>();
    private JList<String> taskList;
    private JTextField titleField, dateField;

    // Constructor
    public TaskManagerUI() {
        loadTasksFromFile();  // Load tasks from file on initialization
    }

    // Method to add a task
    public void addTask(String title, String date) {
        tasks.add(new Task(title, date));
        updateTaskList();  // Update the task list displayed
        JOptionPane.showMessageDialog(null, "Task added successfully!");
    }

    // Method to delete a task
    public void deleteTask(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.remove(index);  // Remove the task from the list
            updateTaskList();  // Update the task list displayed
            JOptionPane.showMessageDialog(null, "Task deleted successfully!");
        } else {
            JOptionPane.showMessageDialog(null, "Invalid task index.");
        }
    }

    // Method to mark a task as completed
    public void markTaskAsCompleted(int index) {
        if (index >= 0 && index < tasks.size()) {
            tasks.get(index).markAsCompleted();
            updateTaskList();  // Update the task list displayed
            JOptionPane.showMessageDialog(null, "Task marked as completed!");
        } else {
            JOptionPane.showMessageDialog(null, "Invalid task index.");
        }
    }

    // Method to update the task list
    public void updateTaskList() {
        taskListModel.clear();
        for (Task task : tasks) {
            taskListModel.addElement(task.toString());  // Update task list model
        }
    }

    // Method to save tasks to file
    public void saveTasksToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            writer.write("Title|Date|Status");
            writer.newLine();
            for (Task task : tasks) {
                writer.write(task.getTitle() + "|" + task.getDate() + "|" + task.isCompleted());
                writer.newLine();
            }
            JOptionPane.showMessageDialog(null, "Tasks saved successfully.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving tasks: " + e.getMessage());
        }
    }

    // Method to load tasks from file
    public void loadTasksFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return;  // If file doesn't exist, nothing to load
        }

        tasks.clear();  // Clear the list before loading new tasks
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Skip header line
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    Task task = new Task(parts[0], parts[1]);
                    if (Boolean.parseBoolean(parts[2])) {
                        task.markAsCompleted();  // Mark as completed if true
                    }
                    tasks.add(task);  // Add task to list
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading tasks: " + e.getMessage());
        }
    }

    // Method to create and show the GUI
    private void createAndShowGUI() {
        JFrame frame = new JFrame("Task Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Task list area
        taskList = new JList<>(taskListModel);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane listScrollPane = new JScrollPane(taskList);
        frame.add(listScrollPane, BorderLayout.CENTER);

        // Input fields and buttons
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));
        inputPanel.add(new JLabel("Title:"));
        titleField = new JTextField();
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("Date (YYYY-MM-DD):"));
        dateField = new JTextField();
        inputPanel.add(dateField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 4));
        
        JButton addButton = new JButton("Add Task");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                String date = dateField.getText();
                if (!title.isEmpty() && !date.isEmpty()) {
                    addTask(title, date);  // Add task
                    titleField.setText("");  // Clear title field
                    dateField.setText("");  // Clear date field
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter both title and date.");
                }
            }
        });
        buttonPanel.add(addButton);

        JButton deleteButton = new JButton("Delete Task");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = taskList.getSelectedIndex();
                if (selectedIndex != -1) {
                    deleteTask(selectedIndex);  // Delete selected task
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a task to delete.");
                }
            }
        });
        buttonPanel.add(deleteButton);

        JButton completeButton = new JButton("Mark Completed");
        completeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = taskList.getSelectedIndex();
                if (selectedIndex != -1) {
                    markTaskAsCompleted(selectedIndex);  // Mark selected task as completed
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a task to mark as completed.");
                }
            }
        });
        buttonPanel.add(completeButton);

        JButton saveButton = new JButton("Save Tasks");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveTasksToFile();  // Save tasks to file
            }
        });
        buttonPanel.add(saveButton);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TaskManagerUI manager = new TaskManagerUI();
                manager.createAndShowGUI();  // Show GUI
            }
        });
    }
}
