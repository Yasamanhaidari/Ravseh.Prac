import java.sql.*;
import java.util.Scanner;

public class DeleteAllTasks {
    MainMenu mainMenu=new MainMenu();

    private static final String DB_URL = "jdbc:mysql://localhost:3306/todolist";

    public void deleteAllTasks(int userID) {
        try (Connection connection = DriverManager.getConnection(DB_URL, "root", "")) {
            if (isPasswordCorrect(connection, userID)) {
                deleteAllTasksFromTable(connection, userID);
                System.out.println("All tasks deleted successfully!");
                mainMenu.Menu();
            } else {
                System.out.println("Incorrect password. Unable to delete tasks.");
                mainMenu.Menu();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error accessing the database.");
            mainMenu.Menu();
        }
    }

    private boolean isPasswordCorrect(Connection connection, int userID) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your password to delete all tasks:");
        String enteredPassword = scanner.nextLine();

        try {
            String selectQuery = "SELECT * FROM users WHERE ID = ? AND Pass = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setInt(1, userID);
            preparedStatement.setString(2, enteredPassword);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error checking password.");
            return false;
        }
    }


    private void deleteAllTasksFromTable(Connection connection, int userID) {
        try {
            String deleteQuery = "DELETE FROM tasks_" + userID;
            Statement statement = connection.createStatement();
            statement.executeUpdate(deleteQuery);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error deleting tasks from the table.");
            mainMenu.Menu();

        }
    }

    public static void main(String[] args) {
        // Example usage
        DeleteAllTasks deleteAllTasks = new DeleteAllTasks();
        deleteAllTasks.deleteAllTasks(123); // Replace 123 with the actual user ID
    }
}

