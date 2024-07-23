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
    public boolean discontinued;

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
        discontinued = false;
    }

    public int add_product() {
        try {
            Connection conn;
            conn = DriverManager.getConnection("jdbc:mysql://mysql-176128-0.cloudclusters.net:10107/dbsales?useTimezone=true&serverTimezone=UTC&user=CCINFOM_G207&password=DLSU1234");
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO products VALUES (?,?,?,?,?,?,?,?,?,?)");
            pstmt.setString(1, productCode);
            pstmt.setString(2, productName);
            pstmt.setString(3, productLine);
            pstmt.setString(4, productScale);
            pstmt.setString(5, productDescription);
            pstmt.setString(6, productVendor);
            pstmt.setInt(7, quantityInStock);
            pstmt.setFloat(8, buyPrice);
            pstmt.setFloat(9, MSRP);
            pstmt.setBoolean(10, discontinued);
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
        if (get_product() == 0) {
            System.out.println("That record does not exist");
            return 0;
        }
        if (discontinued) {
            System.out.println("This product is discontinued and cannot be updated.");
            return 0;
        }
        try {
            Connection conn;
            conn = DriverManager.getConnection("jdbc:mysql://mysql-176128-0.cloudclusters.net:10107/dbsales?useTimezone=true&serverTimezone=UTC&user=CCINFOM_G207&password=DLSU1234");
            System.out.println("Connection to DB Successful");
            PreparedStatement pstmt = conn.prepareStatement("UPDATE products SET productName=?, productLine=?, productScale=?, productDescription=?, productVendor=?, quantityInStock=?, buyPrice=?, MSRP=? WHERE productCode=?");
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
        if (get_product() == 0) {
            System.out.println("That record does not exist");
            return 0;
        }
        try {
            Connection conn;
            conn = DriverManager.getConnection("jdbc:mysql://mysql-176128-0.cloudclusters.net:10107/dbsales?useTimezone=true&serverTimezone=UTC&user=CCINFOM_G207&password=DLSU1234");
            System.out.println("Connection to DB Successful");

            PreparedStatement pstmtCheck = conn.prepareStatement("SELECT COUNT(*) AS refCount FROM orderdetails WHERE productCode=?");
            pstmtCheck.setString(1, productCode);
            ResultSet rs = pstmtCheck.executeQuery();
            if (rs.next() && rs.getInt("refCount") > 0) {
                System.out.println("Cannot delete product as it is referenced in orders.");
                pstmtCheck.close();
                conn.close();
                return 0;
            }
            pstmtCheck.close();

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

    public int get_product() {
        int recordcount = 0;
        try {
            Connection conn;
            conn = DriverManager.getConnection("jdbc:mysql://mysql-176128-0.cloudclusters.net:10107/dbsales?useTimezone=true&serverTimezone=UTC&user=CCINFOM_G207&password=DLSU1234");
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
                discontinued = rs.getBoolean("discontinued");
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

    public int discontinue_product() {
        if (get_product() == 0) {
            System.out.println("That record does not exist");
            return 0;
        }
        try {
            Connection conn;
            conn = DriverManager.getConnection("jdbc:mysql://mysql-176128-0.cloudclusters.net:10107/dbsales?useTimezone=true&serverTimezone=UTC&user=CCINFOM_G207&password=DLSU1234");
            PreparedStatement pstmt = conn.prepareStatement("UPDATE products SET discontinued=? WHERE productCode=?");
            pstmt.setBoolean(1, true);
            pstmt.setString(2, productCode);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
            return 1;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return 0;
        }
    }
}
