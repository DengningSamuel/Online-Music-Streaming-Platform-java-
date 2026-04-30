import java.util.*;

public class Main {
    
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String name;
        String email;
        System.out.println("Adding User to DB");
        AddUser User = new AddUser();
        System.out.println("Enter the UserName below:");
        name = input.nextLine();
        System.out.println("Enter the Email below:");
        email = input.nextLine();
        input.close();
        try {
            User.AddUserDetails(name, email);
            System.out.println("Added Successfuly !!!");
        } catch (Exception e) {
            System.out.println("There was an Error: " + e);
        }

    }

}