package G207DBAPP;
import java.sql.*;
import java.sql.Date;
import java.util.*;

public class order_transaction {

    // Order attributes
    public int orderNumber;
    public Date orderDate;
    public Date requiredDate;
    public Date shippedDate;
    public String status;
    public String comments;
    public int customerNumber;
    public List<OrderDetail> orderDetails;
    public OrderDetail orderDetail;

    // Constructor to initialize the order attributes
    public order_transaction() {
        orderNumber = 0;
        orderDate = null;
        requiredDate = null;
        shippedDate = null;
        status = "";
        comments = "";
        customerNumber = 0;
        orderDetails = new ArrayList<>();
    }

    // Nested class to represent order details
    public static class OrderDetail {
        public int orderNumber;
        public String productCode;
        public int quantityOrdered;
        public double priceEach;
        public int orderLineNumber;

        // Constructor to initialize the order detail attributes
        public OrderDetail(int orderNumber, String productCode, int quantityOrdered, double priceEach, int orderLineNumber) {
            this.orderNumber = orderNumber;
            this.productCode = productCode;
            this.quantityOrdered = quantityOrdered;
            this.priceEach = priceEach;
            this.orderLineNumber = orderLineNumber;
        }
    }

    // Method to validate if the product code exists and is in stock
    public boolean isProductValid(String productCode, int quantity) {
        Connection conn = null;
        try {
            // Establish database connection
            conn = DriverManager.getConnection(
                "jdbc:mysql://mysql-176128-0.cloudclusters.net:10107/dbsales?useTimezone=true&serverTimezone=UTC&user=CCINFOM_G207&password=DLSU1234");

            // Prepare SQL statement to check product validity
            PreparedStatement pstmt = conn.prepareStatement("SELECT quantityInStock FROM products WHERE productCode=?");
            pstmt.setString(1, productCode);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int quantityInStock = rs.getInt("quantityInStock");
                if (quantityInStock >= quantity) {
                    pstmt.close();
                    conn.close();
                    return true; // Product is valid and in stock
                }
            }
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false; // Product is invalid or not in stock
    }

    // Method to create a new order
    public int create_order() {
        Connection conn = null;
        try {
            // Establish database connection
            conn = DriverManager.getConnection(
                "jdbc:mysql://mysql-176128-0.cloudclusters.net:10107/dbsales?useTimezone=true&serverTimezone=UTC&user=CCINFOM_G207&password=DLSU1234");
            conn.setAutoCommit(false); // Disable auto-commit for transaction management
            System.out.println("\nConnection to DB Successful\n");

            // Prepare SQL statement to insert new order
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO orders (orderDate, requiredDate, shippedDate, status, comments, customerNumber) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            pstmt.setDate(1, new java.sql.Date(orderDate.getTime()));
            pstmt.setDate(2, new java.sql.Date(requiredDate.getTime()));
            pstmt.setDate(3, shippedDate != null ? new java.sql.Date(shippedDate.getTime()) : null);
            pstmt.setString(4, status);
            pstmt.setString(5, comments);
            pstmt.setInt(6, customerNumber);
            System.out.println("SQL Statement Prepared\n");

            // Execute SQL statement
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                orderNumber = rs.getInt(1);
            } else {
                throw new SQLException("Failed to retrieve order number");
            }

            // Prepare SQL statement to insert order details
            pstmt = conn.prepareStatement("INSERT INTO orderdetails (orderNumber, productCode, quantityOrdered, priceEach, orderLineNumber) VALUES (?, ?, ?, ?, ?)");
            for (OrderDetail detail : orderDetails) {
                if (!isProductValid(detail.productCode, detail.quantityOrdered)) {
                    System.out.println("Invalid product or insufficient stock for product code: " + detail.productCode);
                    conn.rollback();
                    return 0; // Invalid product or insufficient stock
                }
                pstmt.setInt(1, orderNumber);
                pstmt.setString(2, detail.productCode);
                pstmt.setInt(3, detail.quantityOrdered);
                pstmt.setDouble(4, detail.priceEach);
                pstmt.setInt(5, detail.orderLineNumber);
                pstmt.addBatch();
            }
            System.out.println("SQL Statements for Order Details Prepared\n");

            // Execute batch of order detail inserts
            pstmt.executeBatch();

            // Commit the transaction
            conn.commit();
            System.out.println("Transaction committed successfully\n");

            // Close statement and connection
            pstmt.close();
            conn.close();
            return 1; // Success
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Roll back the transaction in case of failure
                    System.out.println("Transaction rolled back due to error\n");
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
            System.out.println(e.getMessage());
            return 0; // Failure
        }
    }

    // Method to update an existing order
    public int update_order() {
        Connection conn = null;
        try {
            // Establish database connection
            conn = DriverManager.getConnection(
                "jdbc:mysql://mysql-176128-0.cloudclusters.net:10107/dbsales?useTimezone=true&serverTimezone=UTC&user=CCINFOM_G207&password=DLSU1234");
            conn.setAutoCommit(false); // Disable auto-commit for transaction management
            System.out.println("\nConnection to DB Successful\n");

            // Check if the order exists and is in-process
            PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM orders WHERE orderNumber=? AND status='In Process'");
            checkStmt.setInt(1, orderNumber);
            ResultSet checkRs = checkStmt.executeQuery();
            checkRs.next();
            if (checkRs.getInt(1) == 0) {
                System.out.println("This Order does not exist or is not in-process\n");
                return 0; // Order does not exist or is not in-process
            }
            checkStmt.close();

            // Prepare SQL statement to update order
            PreparedStatement pstmt = conn.prepareStatement("UPDATE orders SET orderDate=?, requiredDate=?, shippedDate=?, status=?, comments=?, customerNumber=? WHERE orderNumber=?");
            pstmt.setDate(1, new java.sql.Date(orderDate.getTime()));
            pstmt.setDate(2, new java.sql.Date(requiredDate.getTime()));
            pstmt.setDate(3, shippedDate != null ? new java.sql.Date(shippedDate.getTime()) : null);
            pstmt.setString(4, status);
            pstmt.setString(5, comments);
            pstmt.setInt(6, customerNumber);
            pstmt.setInt(7, orderNumber);
            System.out.println("SQL Statement Prepared\n");

            // Execute SQL statement
            pstmt.executeUpdate();
            conn.commit();
            System.out.println("Order was updated\n");

            // Close statement and connection
            pstmt.close();
            conn.close();
            return 1; // Success
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Roll back the transaction in case of failure
                    System.out.println("Transaction rolled back due to error\n");
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
            System.out.println(e.getMessage());
            return 0; // Failure
        }
    }

    // Method to update a specific product in an order
    public int update_order_detail(String productCode, int quantityOrdered, double priceEach) {
        Connection conn = null;
        try {
            // Establish database connection
            conn = DriverManager.getConnection(
                "jdbc:mysql://mysql-176128-0.cloudclusters.net:10107/dbsales?useTimezone=true&serverTimezone=UTC&user=CCINFOM_G207&password=DLSU1234");
            conn.setAutoCommit(false); // Disable auto-commit for transaction management
            System.out.println("\nConnection to DB Successful\n");

            // Check if the order exists and is in-process
            PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM orders WHERE orderNumber=? AND status='In Process'");
            checkStmt.setInt(1, orderNumber);
            ResultSet checkRs = checkStmt.executeQuery();
            checkRs.next();
            if (checkRs.getInt(1) == 0) {
                System.out.println("This Order does not exist or is not in-process\n");
                return 0; // Order does not exist or is not in-process
            }
            checkStmt.close();

            // Check if the product exists in the order
            checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM orderdetails WHERE orderNumber=? AND productCode=?");
            checkStmt.setInt(1, orderNumber);
            checkStmt.setString(2, productCode);
            checkRs = checkStmt.executeQuery();
            checkRs.next();
            if (checkRs.getInt(1) == 0) {
                System.out.println("Product does not exist in this order\n");
                return 0; // Product does not exist in this order
            }
            checkStmt.close();

            // Prepare SQL statement to update order detail
            PreparedStatement pstmt = conn.prepareStatement("UPDATE orderdetails SET quantityOrdered=?, priceEach=? WHERE orderNumber=? AND productCode=?");
            pstmt.setInt(1, quantityOrdered);
            pstmt.setDouble(2, priceEach);
            pstmt.setInt(3, orderNumber);
            pstmt.setString(4, productCode);
            System.out.println("SQL Statement Prepared\n");

            // Execute SQL statement
            pstmt.executeUpdate();
            conn.commit();
            System.out.println("Order detail was updated\n");

            // Close statement and connection
            pstmt.close();
            conn.close();
            return 1; // Success
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Roll back the transaction in case of failure
                    System.out.println("Transaction rolled back due to error\n");
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
            System.out.println(e.getMessage());
            return 0; // Failure
        }
    }

    // Method to delete a specific product in an order
    public int delete_order_detail(String productCode) {
        Connection conn = null;
        try {
            // Establish database connection
            conn = DriverManager.getConnection(
                "jdbc:mysql://mysql-176128-0.cloudclusters.net:10107/dbsales?useTimezone=true&serverTimezone=UTC&user=CCINFOM_G207&password=DLSU1234");
            conn.setAutoCommit(false); // Disable auto-commit for transaction management
            System.out.println("\nConnection to DB Successful\n");

            // Check if the order exists and is in-process
            PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM orders WHERE orderNumber=? AND status='In Process'");
            checkStmt.setInt(1, orderNumber);
            ResultSet checkRs = checkStmt.executeQuery();
            checkRs.next();
            if (checkRs.getInt(1) == 0) {
                System.out.println("This Order does not exist or is not in-process\n");
                return 0; // Order does not exist or is not in-process
            }
            checkStmt.close();

            // Prepare SQL statement to delete order detail
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM orderdetails WHERE orderNumber=? AND productCode=?");
            pstmt.setInt(1, orderNumber);
            pstmt.setString(2, productCode);
            System.out.println("SQL Statement Prepared\n");

            // Execute SQL statement
            pstmt.executeUpdate();
            conn.commit();
            System.out.println("Order detail was deleted\n");

            // Close statement and connection
            pstmt.close();
            conn.close();
            return 1; // Success
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Roll back the transaction in case of failure
                    System.out.println("Transaction rolled back due to error\n");
                } catch (SQLException se) {
                    se.printStackTrace();
                }
            }
            System.out.println(e.getMessage());
            return 0; // Failure
        }
    }

    // Method to get order details
    public int get_order() {
        int recordcount = 0;
        try {
            // Establish database connection
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://mysql-176128-0.cloudclusters.net:10107/dbsales?useTimezone=true&serverTimezone=UTC&user=CCINFOM_G207&password=DLSU1234");
            System.out.println("\nConnection to DB Successful\n");

            // Prepare SQL statement to get order details
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM orders WHERE orderNumber=?");
            pstmt.setInt(1, orderNumber);
            System.out.println("SQL Statement Prepared\n");

            // Execute SQL statement and retrieve order details
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                recordcount++;
                orderDate = rs.getDate("orderDate");
                requiredDate = rs.getDate("requiredDate");
                shippedDate = rs.getDate("shippedDate");
                status = rs.getString("status");
                comments = rs.getString("comments");
                customerNumber = rs.getInt("customerNumber");
                System.out.println("---------------------------------------------");
                System.out.println("Order Number      : " + orderNumber);
                System.out.println("Order Date        : " + orderDate);
                System.out.println("Required Date     : " + requiredDate);
                System.out.println("Shipped Date      : " + shippedDate);
                System.out.println("Status            : " + status);
                System.out.println("Comments          : " + comments);
                System.out.println("Customer Number   : " + customerNumber);
                System.out.println("---------------------------------------------\n");
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

    // Method to get order detail by product code
    public int get_order_detail(String productCode) {
        int recordcount = 0;
        try {
            // Establish database connection
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://mysql-176128-0.cloudclusters.net:10107/dbsales?useTimezone=true&serverTimezone=UTC&user=CCINFOM_G207&password=DLSU1234");
            System.out.println("\nConnection to DB Successful\n");

            // Prepare SQL statement to get order detail
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM orderdetails WHERE orderNumber=? AND productCode=?");
            pstmt.setInt(1, orderNumber);
            pstmt.setString(2, productCode);
            System.out.println("SQL Statement Prepared\n");

            // Execute SQL statement and retrieve order detail
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                orderDetail = new OrderDetail(
                    rs.getInt("orderNumber"),
                    rs.getString("productCode"),
                    rs.getInt("quantityOrdered"),
                    rs.getDouble("priceEach"),
                    rs.getInt("orderLineNumber")
                );
                recordcount++;
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

    // Method to display order details
    public void display_order_details() {
        System.out.println("---------------------------------------------");
        System.out.println("Order Number      : " + orderNumber);
        System.out.println("Order Date        : " + orderDate);
        System.out.println("Required Date     : " + requiredDate);
        System.out.println("Shipped Date      : " + shippedDate);
        System.out.println("Status            : " + status);
        System.out.println("Comments          : " + comments);
        System.out.println("Customer Number   : " + customerNumber);
        System.out.println("---------------------------------------------");
    }

    // Method to display a specific order detail
    public void display_order_detail(String productCode) {
        System.out.println("---------------------------------------------");
        System.out.println("Product Code      : " + productCode);
        System.out.println("Quantity Ordered  : " + orderDetail.quantityOrdered);
        System.out.println("Price Each        : " + orderDetail.priceEach);
        System.out.println("Order Line Number : " + orderDetail.orderLineNumber);
        System.out.println("---------------------------------------------");
    }

    // Method to check if order number exists
    public boolean isOrderNumberExists(int orderNumber) {
        boolean exists = false;
        try {
            // Establish database connection
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://mysql-176128-0.cloudclusters.net:10107/dbsales?useTimezone=true&serverTimezone=UTC&user=CCINFOM_G207&password=DLSU1234");
            System.out.println("\nConnection to DB Successful\n");

            // Prepare SQL statement to check if order number exists
            PreparedStatement checkStmt = conn.prepareStatement("SELECT COUNT(*) FROM orders WHERE orderNumber=?");
            checkStmt.setInt(1, orderNumber);
            ResultSet checkRs = checkStmt.executeQuery();
            checkRs.next();
            exists = checkRs.getInt(1) > 0;

            // Close statement and connection
            checkStmt.close();
            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return exists;
    }
}
