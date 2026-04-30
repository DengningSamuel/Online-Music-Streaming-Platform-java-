import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class AddUser {

    private static final String URL = "jdbc:postgresql://localhost:5432/users";
    private static final String USER = "postgres";
    private static final String PASSWORD = "qwertyuiop";

    public AddUser(){

    }

    public void AddUserDetails(String Name, String Email) {
        Connection connection = null;

        try {
            Class.forName("org.postgresql.Driver");

            connection = DriverManager.getConnection(URL, USER, PASSWORD);

            if (connection != null) {

                Scanner sc = new Scanner(System.in);
                System.out.println("Connected to the PostgreSQL database successfully!");
                String usernm = Name;
                String mail = Email;
                getAddUser(connection, usernm, mail);
                sc.close();

            } else {

                System.out.println("Failed to connect to the database.");

            }

        } catch (ClassNotFoundException e) {

            System.out.println("PostgreSQL JDBC Driver not found. Include it in your library path.");
            e.printStackTrace();
        } catch (SQLException e) {

            System.out.println("Connection failed. Check output console.");
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                    System.out.println("Connection closed.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void getAddUser(Connection connection, String name, String email) throws SQLException {
        
        var addUser = "INSERT INTO users (username, email) VALUES(?,?);";
        System.out.println("Ok 1");
        PreparedStatement prpd = connection.prepareStatement(addUser);
        prpd.setString(1, name);
        System.out.println("Ok 2");
        prpd.setString(2, email);
        prpd.executeUpdate();
        System.out.println("USER ADDED.");
    }
}