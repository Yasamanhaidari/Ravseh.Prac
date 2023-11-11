import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrintAllColumns {
    MainMenu mainMenu=new MainMenu();
    private static final String DB_URL = "jdbc:mysql://localhost:3306/todolist";

    public void printAllColumns(int userID) {
        try (Connection connection = DriverManager.getConnection(DB_URL, "root", "")) {

            System.out.println("Tasks for user " + userID + ":");
            System.out.println("-------------------------------------------------------");
            System.out.printf("| %-6s | %-20s | %-20s | %-20s |\n", "TaskID", "Do", "Doing", "Done");
            System.out.println("-------------------------------------------------------");

            List<String> doColumn = getTasksFromColumn(connection, userID, "Do");
            List<String> doingColumn = getTasksFromColumn(connection, userID, "Doing");
            List<String> doneColumn = getTasksFromColumn(connection, userID, "Done");

            int maxRowCount = Math.max(Math.max(doColumn.size(), doingColumn.size()), doneColumn.size());

            for (int i = 0; i < maxRowCount; i++) {
                String doTask = i < doColumn.size() ? doColumn.get(i) : "";
                String doingTask = i < doingColumn.size() ? doingColumn.get(i) : "";
                String doneTask = i < doneColumn.size() ? doneColumn.get(i) : "";

                System.out.printf("| %-6d | %-20s | %-20s | %-20s |\n", i + 1, doTask, doingTask, doneTask);
            }

            System.out.println("-------------------------------------------------------");
            mainMenu.Menu();

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error accessing the database.");
            mainMenu.Menu();
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
            mainMenu.Menu();
        }
        return tasks;
    }

    public static void main(String[] args) {
        // Example usage
        PrintAllColumns printAllColumns = new PrintAllColumns();
        printAllColumns.printAllColumns(123); // Replace 123 with the actual user ID
    }
}
