import java.sql.*;
import java.util.Scanner;

// Base class: Medicine
class Medicine {
    private String name;
    private String manufacturer;
    private int quantity;
    private String expiryDate;

    public Medicine(String name, String manufacturer, int quantity, String expiryDate) {
        this.name = name;
        this.manufacturer = manufacturer;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Manufacturer: " + manufacturer + ", Quantity: " + quantity + ", Expiry Date: " + expiryDate;
    }
}

// MedicineInventory class with database connectivity
class MedicineInventory {
    private Connection connection;

    // Constructor to establish a database connection
    public MedicineInventory() {
        try {
            // Replace with your database credentials
            String url = "jdbc:mysql://localhost:3306/medicine_db";
            String user = "nithya";
            String password = "1234";
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the database successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add medicine to the database
    public void addMedicine(Medicine medicine) {
        String sql = "INSERT INTO medicines (name, manufacturer, quantity, expiry_date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, medicine.getName());
            stmt.setString(2, medicine.getManufacturer());
            stmt.setInt(3, medicine.getQuantity());
            stmt.setDate(4, Date.valueOf(medicine.getExpiryDate()));
            stmt.executeUpdate();
            System.out.println("Medicine added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // View all medicines
    public void viewMedicines() {
        String sql = "SELECT * FROM medicines";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("Current Medicines in Inventory:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Name: " + rs.getString("name") +
                        ", Manufacturer: " + rs.getString("manufacturer") +
                        ", Quantity: " + rs.getInt("quantity") +
                        ", Expiry Date: " + rs.getDate("expiry_date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Search medicine by name
    public void searchMedicine(String name) {
        String sql = "SELECT * FROM medicines WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Medicine found: " +
                            "ID: " + rs.getInt("id") +
                            ", Name: " + rs.getString("name") +
                            ", Manufacturer: " + rs.getString("manufacturer") +
                            ", Quantity: " + rs.getInt("quantity") +
                            ", Expiry Date: " + rs.getDate("expiry_date"));
                } else {
System.out.println("Medicine not found.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Remove medicine by name
    public void removeMedicine(String name) {
        String sql = "DELETE FROM medicines WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Medicine removed successfully!");
            } else {
                System.out.println("Medicine not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Close the database connection
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

// Main class: MedicineTrackingSystem
public class MedicineTrackingSystem {
    private Scanner scanner = new Scanner(System.in);
    private MedicineInventory inventory = new MedicineInventory();

    // Add medicine menu
    public void addMedicine() {
        System.out.print("Enter medicine name: ");
        String name = scanner.nextLine();

        System.out.print("Enter manufacturer: ");
        String manufacturer = scanner.nextLine();

        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter expiry date (YYYY-MM-DD): ");
        String expiryDate = scanner.nextLine();

        Medicine medicine = new Medicine(name, manufacturer, quantity, expiryDate);
        inventory.addMedicine(medicine);
    }

    // Main menu
    public void menu() {
        while (true) {
            System.out.println("\nMedicine Tracking System");
            System.out.println("1. Add Medicine");
            System.out.println("2. View Medicines");
            System.out.println("3. Search Medicine");
            System.out.println("4. Remove Medicine");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addMedicine();
                    break;
                case 2:
                    inventory.viewMedicines();
                    break;
                case 3:
                    System.out.print("Enter medicine name to search: ");
                    String searchName = scanner.nextLine();
                    inventory.searchMedicine(searchName);
                    break;
                case 4:
                    System.out.print("Enter medicine name to remove: ");
                    String removeName = scanner.nextLine();
                    inventory.removeMedicine(removeName);
                    break;
                case 5:
                    inventory.closeConnection();
                    System.out.println("Exiting system. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Main method
    public static void main(String[] args) {
        MedicineTrackingSystem system = new MedicineTrackingSystem();
        system.menu();
    }
}
                   
