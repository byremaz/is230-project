import java.sql.*;
import java.util.Scanner;

public class project {
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        Connection connection = null;
        String url = "jdbc:mariadb://localhost:3306/TickGo"; // Adjust DB name if needed
        String user = "root";
        String password = "1234";
        int choice;

        try {
            connection = DriverManager.getConnection(url, user, password);
            Statement stmt = connection.createStatement();

            do {
                System.out.println("\nTable Station:");
                System.out.println("1) Insert new record");
                System.out.println("2) Display all records");
                System.out.println("3) Exit");
                System.out.print("Choose an operation (^_^): ");
                
                choice = input.nextInt();
                input.nextLine(); // Consume leftover newline

                switch (choice) {
                    case 1:
                        insertStation(stmt);
                        break;
                    case 2:
                        displayStations(stmt);
                        break;
                    case 3:
                        System.out.println("Program terminated.");
                        break;
                    default:
                        System.out.println("Invalid input. Please choose 1, 2, or 3.");
                }
            } while (choice != 3);

            stmt.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    private static void insertStation(Statement stmt) {
       String name, type, streetName, postalCode;
       int workingHours ; // ... to ... OR total 
       System.out.println("Station insertion");
       System.out.println("Input name: ");
       name = input.nextLine();

       while ( name.length() == 0 ){
        System.out.println("Enter name: ");
        name = input.nextLine();
       }
        
       System.out.println("Input type: ");
       type = input.nextLine();
       while ( !type.equalsIgnoreCase("Metro") && !type.equalsIgnoreCase("Bus") ){
        System.out.println("Enter Metro or Bus: ");
        type = input.nextLine();
       }

    }

    private static void displayStations(Statement stmt){
    ResultSet rs = null;
    try {
        rs = stmt.executeQuery("SELECT * FROM Station");

        boolean hasData = false;
        while (rs.next()){
            System.out.println("\n--- Station Record ---");
            System.out.println("Station ID    : " + rs.getInt("StationID"));
            System.out.println("Name          : " + rs.getString("Name"));
            System.out.println("Type          : " + rs.getString("Type"));
            System.out.println("Street Name   : " + rs.getString("Street_Name"));
            System.out.println("Postal Code   : " + rs.getString("Postal_code"));
            System.out.println("Working Hours : " + rs.getString("Working_Hours"));
            System.out.println("-----------------------------");
            hasData = true;
        }

        if(!hasData){
            System.out.println("No station records found in the database.");
        }
    } catch (SQLException e) {
        System.out.println("Unable to retrieve station records.");
        System.out.println("Reason: " + e.getMessage());
}

    }
}
