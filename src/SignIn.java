import java.sql.*;
import java.util.Scanner;

public class SignIn {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/todolist";
    public void signin(){
        Main main = new Main();
        MainMenu mainMenu=new MainMenu();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter Your UserName:");
        String User = validateInput("[A-Z][a-z]*\\d{2}", "Invalid UserName format. Please use a capital letter followed by lowercase letters and two digits.");

        System.out.println("Enter Your Password:");
        String Pass = validateInput(User.charAt(0) + "\\d{3}", "Invalid Password format. Please use the first letter of the UserName in uppercase followed by three digits.");

        System.out.println("Enter Your ID:");
        int ID = Integer.parseInt(validateInput("[1-9][0-9]{2}", "Invalid ID format. Please enter a three-digit number between 100 and 999."));

        System.out.println("Enter Your Phone Number with (+98):");
        Double phoneNumber = scanner.nextDouble();

        try (Connection connection = DriverManager.getConnection(DB_URL, "root", "")) {
            connection.setAutoCommit(false);

            String insertUserQuery = "INSERT INTO users (UserName, Pass, ID, PhoneNumber) VALUES (?, ?, ?, ?)";
            try (PreparedStatement userPreparedStatement = connection.prepareStatement(insertUserQuery)) {
                userPreparedStatement.setString(1, User);
                userPreparedStatement.setString(2, Pass);
                userPreparedStatement.setInt(3, ID);
                userPreparedStatement.setDouble(4, phoneNumber);
                userPreparedStatement.executeUpdate();

                String createTasksTableQuery =  "CREATE TABLE tasks_" + ID + " (TaskID INT AUTO_INCREMENT, Do TEXT, Doing TEXT, Done TEXT, PRIMARY KEY (TaskID))";
                Statement createTableStatement = connection.createStatement();
                createTableStatement.executeUpdate(createTasksTableQuery);

                connection.commit();
                System.out.println("User registered successfully!");
                mainMenu.Menu();
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("User registration failed.");
                connection.rollback();
                main.startToDoList();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("User registration failed.");
        }
    }
    private String validateInput(String regex, String errorMessage) {
        Main main=new Main();
        Scanner scanner = new Scanner(System.in);
        String input;
        while (true) {
            input = scanner.next();
            if (input.matches(regex)) {
                return input;
            } else {
                System.err.println(errorMessage);
                main.startToDoList();
            }
        }
    }
}