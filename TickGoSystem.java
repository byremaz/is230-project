import java.sql.*;
import java.util.InputMismatchException;
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
                input.nextLine(); 

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
        int workingHours;
        char choice;
        do {        		
            System.out.println("Station - INSERTION");
            
            System.out.print("Input name: ");
            name = input.nextLine();
            while (name.length() == 0 || !(name.matches("[a-zA-Z0-9\\s]+"))) { //validates not null and does not contain special characters
            	if(name.length()==0)
            		System.out.print("No name entered, please enter a name: ");
            	else if(!(name.matches("[a-zA-Z0-9\\s]+"))) 
            		System.out.print("The name entered contains special characters, please enter a valid name: ");
                name = input.nextLine();
            }
        	
            System.out.print("Input type (Metro/Bus): ");
            type = input.nextLine();
            while (!type.equalsIgnoreCase("Metro") && !type.equalsIgnoreCase("Bus")) {
                System.out.print("The type entered is not valid, please enter either Metro or Bus: ");
                type = input.nextLine();
            }
            
            System.out.print("Input Street Name: ");
            streetName = input.nextLine();
            while (streetName.length() == 0 || !(streetName.matches("[a-zA-Z0-9\\s]+"))) { //validates not null and does not contain special characters
            	if(streetName.length()==0)
            		System.out.print("No street name entered, please enter a street name: ");
            	else if(!(streetName.matches("[a-zA-Z0-9\\s]+")))
            		System.out.print("The street name entered contains special characters, please enter a valid street name: ");
            	streetName = input.nextLine();
            }
            
            System.out.print("Input postal code: ");
            postalCode = input.nextLine();
            while (postalCode.length() != 5) {
            	if ( postalCode.length() == 0 )
            		System.out.print("No postal code entered, Enter a valid 5-character postal code: ");
            	else 
            		System.out.print("Enter a valid 5-character postal code: ");
                postalCode = input.nextLine();
            }
            
            workingHours=-1; //give initial value that is not acceptable 
            while (true) {
            	String tempWorkingHours ="";
                try {
                    System.out.print("Input working hours: ");
                    tempWorkingHours = input.nextLine();
                    if ( tempWorkingHours.length() == 0 )
                    	System.out.print("No working hours entered, Enter valid working hours. ");
                    workingHours = Integer.parseInt(tempWorkingHours);
                    if (workingHours < 0) {
                        System.out.print("Enter valid working hours (non-negative). ");
                    } else {
                        break; // valid input
                    }
                } catch (NumberFormatException e) {
                	if ( tempWorkingHours.length() != 0 )
                    System.out.print("The working hours entered is not a number. ");
                }
            }
            
            
            String insertQuery = "INSERT INTO Station (Name, Type, Street_Name, Postal_code, Working_Hours) " +
                    "VALUES ('" + name + "', '" + type + "', '" + streetName + "', '" + postalCode + "', '" + workingHours + "')";

            try {
                stmt.executeUpdate(insertQuery);
                System.out.println("Station record inserted successfully!");
            } catch (SQLException e) {
                if (e.getErrorCode() == 1062) // Key constraint
                    System.out.println("A station with the same name already exists.");
                else if (e.getErrorCode() == 1048) // Entity integrity constraint
                    System.out.println("The station includes empty fields.");
                else
                    System.out.println("An error occurred while inserting the station record.");
            }
            
                System.out.print("Insert another record? (Y/N): ");
                choice = input.next().charAt(0);
                input.nextLine(); 
                while (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n') {
                    System.out.print("Invalid input! Please enter 'Y' for Yes or 'N' for No: ");
                    choice = input.next().charAt(0);
                    input.nextLine(); 
                }	
             
            
        } while (choice == 'Y' || choice == 'y');
       
    }

    private static void displayStations(Statement stmt) {
    ResultSet rs = null;
     try {
         rs = stmt.executeQuery("SELECT * FROM Station");
 
         boolean hasData = false;
         while (rs.next()){
             System.out.println("\n----- Station Record -------");
             System.out.println("Name          : " + rs.getString("Name"));
             System.out.println("Type          : " + rs.getString("Type"));
             System.out.println("Street Name   : " + rs.getString("Street_Name"));
             System.out.println("Postal Code   : " + rs.getString("Postal_code"));
             System.out.println("Working Hours : " + rs.getString("Working_Hours"));
             System.out.println("----------------------------");
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
