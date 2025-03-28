import java.sql.*;
import java.util.Scanner;

public class TickGoSystem {
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        Connection connection = null;
        String url = "jdbc:mariadb://localhost:3306/TickGo"; 
        String user = "root";
        String password = "";
        int choice;

        try {
            connection = DriverManager.getConnection(url, user, password);
            Statement stmt = connection.createStatement();

            do {
                System.out.println("\nTable Station:");
                System.out.println("1) Insert new record");
                System.out.println("2) Display all records");
                System.out.println("3) Exit");
                System.out.print("Choose an operation: ");
                
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
        char choice;
        do {
            System.out.println("Station - INSERTION");
            
            System.out.print("Input name: ");
            name = input.nextLine();
            while (name.length() == 0) {
                System.out.print("Enter name: ");
                name = input.nextLine();
            }
            
            System.out.print("Input type (Metro/Bus): ");
            type = input.nextLine();
            while (!type.equalsIgnoreCase("Metro") && !type.equalsIgnoreCase("Bus")) {
                System.out.print("Enter Metro or Bus: ");
                type = input.nextLine();
            }
            
            System.out.print("Input Street Name: ");
            streetName = input.nextLine();
            while (streetName.length() == 0) {
                System.out.print("Enter Street Name: ");
                streetName = input.nextLine();
            }
            
            System.out.print("Input postal code: ");
            postalCode = input.nextLine();
            while (postalCode.length() != 5) {
                System.out.print("Enter valid 5-character postal code: ");
                postalCode = input.nextLine();
            }
            
            System.out.print("Input working hours: ");
            workingHours = input.nextInt();
            input.nextLine(); 
            while (workingHours < 0) {
                System.out.print("Enter valid working hours: ");
                workingHours = input.nextInt();
                input.nextLine(); 
            }
            
            do {
                System.out.print("Insert another record? (Y/N): ");
                choice = input.next().charAt(0);
                input.nextLine(); 
                if (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n') {
                    System.out.println("Invalid input! Please enter 'Y' for Yes or 'N' for No.");
                }
            } while (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n');
            
        } while (choice == 'Y' || choice == 'y');
        
    }

    private static void displayStations(Statement stmt) {
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
