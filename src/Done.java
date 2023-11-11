import java.sql.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
public class Done {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/todolist";
    public void done(int userID) {
        try (Connection connection = DriverManager.getConnection(DB_URL, "root", "");
             Scanner scanner = new Scanner(System.in)) {
            MainMenu mainMenu=new MainMenu();
            List<String> doingTasks = getTasksFromColumn(connection, userID, "Doing");

            if (doingTasks.isEmpty()) {
                System.out.println("No tasks in 'Doing' column.");
                return;
            }

            System.out.println("Tasks in 'Doing' column for user " + userID + ":");
            for (int i = 0; i < doingTasks.size(); i++) {
                System.out.println((i + 1) + ". " + doingTasks.get(i));
            }

            System.out.println("Enter the number of the task you've completed:");
            int taskNumber = scanner.nextInt();

            if (taskNumber > 0 && taskNumber <= doingTasks.size()) {
                moveTask(connection, userID, taskNumber, "Doing", "Done", doingTasks.get(taskNumber - 1));
                System.out.println("Task moved to 'Done' column!");
                mainMenu.Menu();
            } else {
                System.out.println("Invalid task number.");
                mainMenu.Menu();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error accessing the database.");
        }
    }
    private List<String> getTasksFromColumn(Connection connection, int userID, String column) {
        List<String> tasks = new ArrayList<>();
        try {
            String selectQuery = "SELECT " + column + " FROM tasks_" + userID + " WHERE " + column + " IS NOT NULL";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            while (resultSet.next()) {
                tasks.add(resultSet.getString(column));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error fetching tasks from column.");
        }
        return tasks;
    }
    private void moveTask(Connection connection, int userID, int taskID, String fromColumn, String toColumn, String taskDescription) {
        try {

            String moveQuery = "UPDATE tasks_" + userID + " SET " + toColumn + " = ? WHERE TaskID = ?";
            String deleteQuery = "UPDATE tasks_" + userID + " SET " + fromColumn + " = NULL WHERE TaskID = ?";

            PreparedStatement moveStatement = connection.prepareStatement(moveQuery);
            moveStatement.setString(1, taskDescription);
            moveStatement.setInt(2, taskID);
            moveStatement.executeUpdate();

            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
            deleteStatement.setInt(1, taskID);
            deleteStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error moving the task.");
        }
    }
}