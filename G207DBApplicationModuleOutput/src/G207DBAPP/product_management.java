package G207DBAPP;
import java.sql.*;
import java.util.Scanner;

public class product_management {

    public String productCode;
    public String productName;
    public String productLine;
    public String productScale;
    public String productDescription;
    public String productVendor;
    public int quantityInStock;
    public float buyPrice;
    public float MSRP;

    private final String[] validProductLines = {
        "Classic Cars", "Motorcycles", "Planes", "Ships",
        "Trains", "Trucks and Buses", "Vintage Cars"
    };

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

    public boolean isProductLineValid(String productLine) {
        for (String validLine : validProductLines) {
            if (validLine.equalsIgnoreCase(productLine)) {
                return true;
            }
        }
        return false;
    }

    public boolean isProductScaleValid(String productScale) {
        return productScale.matches("\\d+:\\d+");
    }

    public int add_product() {
        try {
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
                return 0;
            }
            checkStmt.close();

            if (!isProductLineValid(productLine)) {
                System.out.println("Invalid Product Line");
                return 0;
            }

            if (!isProductScaleValid(productScale)) {
                System.out.println("Invalid Product Scale Format");
                return 0;
            }

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
            pstmt.executeUpdate();
            System.out.println("Record was created");
            pstmt.close();
            conn.close();
            return 1;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public int update_product() {
        try {
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
                return 0;
            }
            checkStmt.close();

            if (!isProductLineValid(productLine)) {
                System.out.println("Invalid Product Line");
                return 0;
            }

            if (!isProductScaleValid(productScale)) {
                System.out.println("Invalid Product Scale Format");
                return 0;
            }

            PreparedStatement pstmt = conn.prepareStatement(
                "UPDATE products SET productName=?, productLine=?, productScale=?, productDescription=?, productVendor=?, quantityInStock=?, buyPrice=?, MSRP=? WHERE productCode=?");
            pstmt.setString(9, productCode);
            pstmt.setString(1, productName);
            pstmt.setString(2, productLine);
            pstmt.setString(3, productScale);
            pstmt.setString(4, productDescription);
            pstmt.setString(5, productVendor);
            pstmt.setInt(6, quantityInStock);
            pstmt.setFloat(7, buyPrice);
            pstmt.setFloat(8, MSRP);
            System.out.println("SQL Statement Prepared");
            pstmt.executeUpdate();
            System.out.println("Record was updated");
            pstmt.close();
            conn.close();
            return 1;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public int delete_product() {
        try {
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
                return 0;
            }
            checkStmt.close();

            // Check if the product is referenced in orders
            PreparedStatement refCheckStmt = conn.prepareStatement("SELECT COUNT(*) FROM orderdetails WHERE productCode=?");
            refCheckStmt.setString(1, productCode);
            ResultSet refCheckRs = refCheckStmt.executeQuery();
            refCheckRs.next();
            if (refCheckRs.getInt(1) > 0) {
                System.out.println("This product cannot be deleted as it is referenced in other records");
                return 0;
            }
            refCheckStmt.close();

            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM products WHERE productCode=?");
            pstmt.setString(1, productCode);
            System.out.println("SQL Statement Prepared");
            pstmt.executeUpdate();
            System.out.println("Record was deleted");
            pstmt.close();
            conn.close();
            return 1;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public int discontinue_product() {
        try {
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
                return 0;
            }
            checkStmt.close();

            PreparedStatement pstmt = conn.prepareStatement("UPDATE products SET discontinued=? WHERE productCode=?");
            pstmt.setBoolean(1, true);
            pstmt.setString(2, productCode);
            System.out.println("SQL Statement Prepared");
            pstmt.executeUpdate();
            System.out.println("Product was discontinued");
            pstmt.close();
            conn.close();
            return 1;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public int get_product() {
        int recordcount = 0;
        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://mysql-176128-0.cloudclusters.net:10107/dbsales?useTimezone=true&serverTimezone=UTC&user=CCINFOM_G207&password=DLSU1234");
            System.out.println("Connection to DB Successful");
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM products WHERE productCode=?");
            pstmt.setString(1, productCode);
            System.out.println("SQL Statement Prepared");
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
            pstmt.close();
            conn.close();
            return recordcount;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }

    public int get_product_orders(int year) {
        int recordcount = 0;
        try {
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
                return 0;
            }
            checkStmt.close();

            PreparedStatement pstmt = conn.prepareStatement(
                "SELECT orders.orderNumber, orders.orderDate, orders.status FROM orderdetails " +
                "JOIN orders ON orderdetails.orderNumber = orders.orderNumber " +
                "WHERE orderdetails.productCode = ? AND YEAR(orders.orderDate) = ?");
            pstmt.setString(1, productCode);
            pstmt.setInt(2, year);
            System.out.println("SQL Statement Prepared");
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
            pstmt.close();
            conn.close();
            return recordcount;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }
}
