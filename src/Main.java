import java.sql.*;
import java.util.Scanner;

public class Main {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/todolist";

    public static void main(String[] args) {
        Main program = new Main();
        program.startToDoList();
    }

    public void startToDoList() {
        System.out.println("Welcome To The To Do List Program");
        System.out.println("1. Log In");
        System.out.println("2. Sign Up");
        System.out.println("3. Forgot Password");
        System.out.println("4. Exit");

        try (Scanner scanner = new Scanner(System.in)) {
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    login();
                    break;
                case 2:
                    SignIn signIn=new SignIn();
                    signIn.signin();
                    break;
                case 3:
                    forgotPassword();
                    break;
                case 4:
                    System.out.println("Exiting the program. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please choose a valid option.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void login() {
        try (Connection connection = DriverManager.getConnection(DB_URL, "root", "");
             Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter username: ");
            String username = scanner.nextLine();

            System.out.println("Enter password: ");
            String password = scanner.nextLine();

            boolean isValid = validateUser(connection, username, password);

            if (isValid) {
                MainMenu mainMenu = new MainMenu();
                mainMenu.Menu();
            } else {
                System.out.println("Invalid username or password. Please try again.");
                startToDoList();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean validateUser(Connection connection, String username, String password) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE UserName = ? AND Pass = ?")) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void forgotPassword() {
        try (Connection connection = DriverManager.getConnection(DB_URL, "root", "");
             Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter username: ");
            String username = scanner.nextLine();

            String phoneNumber = retrievePhoneNumber(connection, username);

            if (phoneNumber != null) {
                System.out.println("Password will be sent to: " + phoneNumber);
            } else {
                System.out.println("Invalid username. Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String retrievePhoneNumber(Connection connection, String username) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT PhoneNumber FROM users WHERE UserName = ?")) {
            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("PhoneNumber");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}