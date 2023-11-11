import java.sql.*;
import java.util.Scanner;

public class  MainMenu extends Main {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/todolist";

    public void Menu() {
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.println("Welcome to main menu");
            System.out.println("1. Write New Task");
            System.out.println("2. Start A Task");
            System.out.println("3. Complete the task");
            System.out.println("4. Print All Column");
            System.out.println("5. Delete All Tasks");
            System.out.println("6. Exit");

            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1: {
                        System.out.println("Enter Your ID:");
                        int ID = scanner.nextInt();
                        ToDo(ID);
                        break;
                    }
                    case 2: {
                        System.out.println("Enter Your ID:");
                        int ID1 = scanner.nextInt();
                        Doing(ID1);
                        break;
                    }
                    case 3: {
                        Done done = new Done();
                        System.out.println("Enter Your ID:");
                        int ID2 = scanner.nextInt();
                        done.done(ID2);
                        break;
                    }
                    case 4: {
                        PrintAllColumns printAllColumns=new PrintAllColumns();
                        System.out.println("Enter Your ID:");
                        int ID3 = scanner.nextInt();
                        printAllColumns.printAllColumns(ID3);
                        break;
                    }
                    case 5: {
                        DeleteAllTasks deleteAllTasks=new DeleteAllTasks();
                        System.out.println("Enter Your ID:");
                        int ID4 = scanner.nextInt();
                        deleteAllTasks.deleteAllTasks(ID4);
                        break;
                    }
                    case 6:
                        System.out.println("Exiting...");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1-5.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        } while (scanner.hasNext());

        scanner.close();
    }


    public void ToDo(int userID) {
        try (Connection connection = DriverManager.getConnection(DB_URL, "root", "")) {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Enter your text for the 'Do' column:");
            String userText = scanner.nextLine();

            String insertQuery = "INSERT INTO tasks_" + userID + " (Do) VALUES (?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setString(1, userText);
                preparedStatement.executeUpdate();
                System.out.println("Text added to 'Do' column successfully!");
                Menu();
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Failed to add text to 'Do' column.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error accessing the database.");
        }
    }
    public void Doing(int userID) {
        try (Connection connection = DriverManager.getConnection(DB_URL, "root", "");
             Scanner scanner = new Scanner(System.in)) {
            displayTasks(connection, userID, "Do");

            System.out.println("Enter the TaskID you want to move to 'Doing':");
            int taskId = scanner.nextInt();

            moveTask(connection, userID, taskId, "Do", "Doing");

            System.out.println("Start working!");
            Menu();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error accessing the database.");
        }
    }
    private void displayTasks(Connection connection, int userID, String column) {
        try {
            String tableName = "tasks_" + userID;
            String selectQuery = "SELECT TaskID, " + column + " FROM " + tableName + " WHERE " + column + " IS NOT NULL";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            System.out.println("Values in '" + column + "' column for user " + userID + ":");
            while (resultSet.next()) {
                int taskId = resultSet.getInt("TaskID");
                String task = resultSet.getString(column);
                System.out.println("TaskID: " + taskId + " - " + task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error accessing the database.");
        }
    }

    private void moveTask(Connection connection, int userID, int taskId, String fromColumn, String toColumn) throws SQLException {
        String updateQuery = "UPDATE tasks_" + userID +
                " SET " + toColumn + " = (SELECT " + fromColumn +
                " FROM tasks_" + userID + " WHERE TaskID = ?), " + fromColumn + " = NULL WHERE TaskID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setInt(1, taskId);
            preparedStatement.setInt(2, taskId);
            preparedStatement.executeUpdate();
        }

    }
}