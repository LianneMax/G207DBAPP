package G207DBAPP;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class product_management {

    // Product attributes
    public String productCode;
    public String productName;
    public String productLine;
    public String productScale;
    public String productDescription;
    public String productVendor;
    public int quantityInStock;
    public float buyPrice;
    public float MSRP;

    // Constructor to initialize the product attributes
    public product_management() {
        productCode = "";
        productName = "";
        productLine = "";
        productScale = "";
        productDescription = "";
        productVendor = "";
        quantityInStock = 0;
        buyPrice = 0;
        MSRP = 0;
    }

    // Method to get the list of valid product lines from the database
    public List<String> getValidProductLines() {
        List<String> validProductLines = new ArrayList<>();
        try {
            // Establish database connection
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://mysql-176128-0.cloudclusters.net:10107/dbsales?useTimezone=true&serverTimezone=UTC&user=CCINFOM_G207&password=DLSU1234");

            // Prepare and execute SQL query to get product lines
            PreparedStatement pstmt = conn.prepareStatement("SELECT productLine FROM productlines");
            ResultSet rs = pstmt.executeQuery();
            
            // Add each product line to the list
            while (rs.next()) {
                validProductLines.add(rs.getString("productLine"));
            }

            // Close statement and connection
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return validProductLines;
    }

    // Method to validate if the product line is valid
    public boolean isProductLineValid(String productLine) {
        List<String> validProductLines = getValidProductLines();
        return validProductLines.contains(productLine);
    }

    // Method to validate if the product scale is in the correct format
    public boolean isProductScaleValid(String productScale) {
        return productScale.matches("\\d+:\\d+");
    }

    // Method to add a new product to the database
    public int add_product() {
        try {
            // Establish database connection
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://mysql-176128-0.cloudclusters.net:10107/dbsales?useTimezone=true&serverTimezone=UTC&user=CCINFOM_G207&password=DLSU1234");
            System.out.println("Connection to DB Successful");

            // Check if the product already exists
            PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM products WHERE productCode=?");
            checkStmt.setString(1, productCode);
            ResultSet checkRs = checkStmt.executeQuery();
            checkRs.next();
            if (checkRs.getInt(1) > 0) {
                System.out.println("This Product Already Exists");
                return 0; // Product already exists
            }
            checkStmt.close();

            // Validate product line
            if (!isProductLineValid(productLine)) {
                System.out.println("Invalid Product Line");
                return 0; // Invalid product line
            }

            // Validate product scale
            if (!isProductScaleValid(productScale)) {
                System.out.println("Invalid Product Scale Format");
                return 0; // Invalid product scale format
            }

            // Prepare SQL statement to insert new product
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO products VALUES (?,?,?,?,?,?,?,?,?)");
            pstmt.setString(1, productCode);
            pstmt.setString(2, productName);
            pstmt.setString(3, productLine);
            pstmt.setString(4, productScale);
            pstmt.setString(5, productDescription);
            pstmt.setString(6, productVendor);
            pstmt.setInt(7, quantityInStock);
            pstmt.setFloat(8, buyPrice);
            pstmt.setFloat(9, MSRP);
            System.out.println("SQL Statement Prepared");

            // Execute SQL statement
            pstmt.executeUpdate();
            System.out.println("Record was created");

            // Close statement and connection
            pstmt.close();
            conn.close();
            return 1; // Success
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0; // Failure
        }
    }

    // Method to update an existing product in the database
    public int update_product() {
        try {
            // Establish database connection
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://mysql-176128-0.cloudclusters.net:10107/dbsales?useTimezone=true&serverTimezone=UTC&user=CCINFOM_G207&password=DLSU1234");
            System.out.println("Connection to DB Successful");

            // Check if the product exists
            PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM products WHERE productCode=?");
            checkStmt.setString(1, productCode);
            ResultSet checkRs = checkStmt.executeQuery();
            checkRs.next();
            if (checkRs.getInt(1) == 0) {
                System.out.println("This Product does not exist");
                return 0; // Product does not exist
            }
            checkStmt.close();

            // Validate product line
            if (!isProductLineValid(productLine)) {
                System.out.println("Invalid Product Line");
                return 0; // Invalid product line
            }

            // Validate product scale
            if (!isProductScaleValid(productScale)) {
                System.out.println("Invalid Product Scale Format");
                return 0; // Invalid product scale format
            }

            // Prepare SQL statement to update product
            PreparedStatement pstmt = conn.prepareStatement(
                "UPDATE products SET productName=?, productLine=?, productScale=?, productDescription=?, productVendor=?, quantityInStock=?, buyPrice=?, MSRP=? WHERE productCode=?");
            pstmt.setString(1, productName);
            pstmt.setString(2, productLine);
            pstmt.setString(3, productScale);
            pstmt.setString(4, productDescription);
            pstmt.setString(5, productVendor);
            pstmt.setInt(6, quantityInStock);
            pstmt.setFloat(7, buyPrice);
            pstmt.setFloat(8, MSRP);
            pstmt.setString(9, productCode);
            System.out.println("SQL Statement Prepared");

            // Execute SQL statement
            pstmt.executeUpdate();
            System.out.println("Record was updated");

            // Close statement and connection
            pstmt.close();
            conn.close();
            return 1; // Success
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0; // Failure
        }
    }

    // Method to delete a product from the database
    public int delete_product() {
        try {
            // Establish database connection
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://mysql-176128-0.cloudclusters.net:10107/dbsales?useTimezone=true&serverTimezone=UTC&user=CCINFOM_G207&password=DLSU1234");
            System.out.println("Connection to DB Successful");

            // Check if the product exists
            PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM products WHERE productCode=?");
            checkStmt.setString(1, productCode);
            ResultSet checkRs = checkStmt.executeQuery();
            checkRs.next();
            if (checkRs.getInt(1) == 0) {
                System.out.println("This Product does not exist");
                return 0; // Product does not exist
            }
            checkStmt.close();

            // Check if the product is referenced in order details
            PreparedStatement refCheckStmt = conn.prepareStatement("SELECT COUNT(*) FROM orderdetails WHERE productCode=?");
            refCheckStmt.setString(1, productCode);
            ResultSet refCheckRs = refCheckStmt.executeQuery();
            refCheckRs.next();
            if (refCheckRs.getInt(1) > 0) {
                System.out.println("This product cannot be deleted as it is referenced in other records");
                return 0; // Product is referenced in other records
            }
            refCheckStmt.close();

            // Prepare SQL statement to delete product
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM products WHERE productCode=?");
            pstmt.setString(1, productCode);
            System.out.println("SQL Statement Prepared");

            // Execute SQL statement
            pstmt.executeUpdate();
            System.out.println("Record was deleted");

            // Close statement and connection
            pstmt.close();
            conn.close();
            return 1; // Success
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0; // Failure
        }
    }

    // Method to discontinue a product (mark as discontinued)
    public int discontinue_product() {
        try {
            // Establish database connection
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://mysql-176128-0.cloudclusters.net:10107/dbsales?useTimezone=true&serverTimezone=UTC&user=CCINFOM_G207&password=DLSU1234");
            System.out.println("Connection to DB Successful");

            // Check if the product exists
            PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM products WHERE productCode=?");
            checkStmt.setString(1, productCode);
            ResultSet checkRs = checkStmt.executeQuery();
            checkRs.next();
            if (checkRs.getInt(1) == 0) {
                System.out.println("This Product does not exist");
                return 0; // Product does not exist
            }
            checkStmt.close();

            // Prepare SQL statement to mark product as discontinued
            PreparedStatement pstmt = conn.prepareStatement("UPDATE products SET discontinued=? WHERE productCode=?");
            pstmt.setBoolean(1, true);
            pstmt.setString(2, productCode);
            System.out.println("SQL Statement Prepared");

            // Execute SQL statement
            pstmt.executeUpdate();
            System.out.println("Product was discontinued");

            // Close statement and connection
            pstmt.close();
            conn.close();
            return 1; // Success
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0; // Failure
        }
    }

    // Method to get a product's details from the database
    public int get_product() {
        int recordcount = 0;
        try {
            // Establish database connection
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://mysql-176128-0.cloudclusters.net:10107/dbsales?useTimezone=true&serverTimezone=UTC&user=CCINFOM_G207&password=DLSU1234");
            System.out.println("Connection to DB Successful");

            // Prepare SQL statement to get product details
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM products WHERE productCode=?");
            pstmt.setString(1, productCode);
            System.out.println("SQL Statement Prepared");

            // Execute SQL statement and retrieve product details
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                recordcount++;
                productName = rs.getString("productName");
                productLine = rs.getString("productLine");
                productScale = rs.getString("productScale");
                productDescription = rs.getString("productDescription");
                productVendor = rs.getString("productVendor");
                quantityInStock = rs.getInt("quantityInStock");
                buyPrice = rs.getFloat("buyPrice");
                MSRP = rs.getFloat("MSRP");
                System.out.println("Record was Retrieved");
            }

            // Close statement and connection
            pstmt.close();
            conn.close();
            return recordcount; // Return number of records found
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0; // Failure
        }
    }

    // Method to get the orders for a product in a specific year
    public int get_product_orders(int year) {
        int recordcount = 0;
        try {
            // Establish database connection
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://mysql-176128-0.cloudclusters.net:10107/dbsales?useTimezone=true&serverTimezone=UTC&user=CCINFOM_G207&password=DLSU1234");
            System.out.println("Connection to DB Successful");

            // Check if the product exists
            PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM products WHERE productCode=?");
            checkStmt.setString(1, productCode);
            ResultSet checkRs = checkStmt.executeQuery();
            checkRs.next();
            if (checkRs.getInt(1) == 0) {
                System.out.println("This Product does not exist");
                return 0; // Product does not exist
            }
            checkStmt.close();

            // Prepare SQL statement to get orders for the product in the specified year
            PreparedStatement pstmt = conn.prepareStatement(
                "SELECT orders.orderNumber, orders.orderDate, orders.status FROM orderdetails " +
                "JOIN orders ON orderdetails.orderNumber = orders.orderNumber " +
                "WHERE orderdetails.productCode = ? AND YEAR(orders.orderDate) = ?");
            pstmt.setString(1, productCode);
            pstmt.setInt(2, year);
            System.out.println("SQL Statement Prepared");

            // Execute SQL statement and retrieve orders
            ResultSet rs = pstmt.executeQuery();
            System.out.println("Orders for Product Code: " + productCode + " in Year: " + year);
            System.out.println("-------------------------------------------------------------------");
            while (rs.next()) {
                recordcount++;
                System.out.println("Order Number: " + rs.getInt("orderNumber"));
                System.out.println("Order Date: " + rs.getDate("orderDate"));
                System.out.println("Order Status: " + rs.getString("status"));
                System.out.println("-------------------------------------------------------------------");
            }

            // Close statement and connection
            pstmt.close();
            conn.close();
            return recordcount; // Return number of records found
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0; // Failure
        }
    }
}
