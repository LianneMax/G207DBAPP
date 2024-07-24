package G207DBAPP;
import java.io.*;
import java.util.Scanner;


public class order_transaction_menu {

    public order_transaction_menu() {
    }

    public int menu() {
        int menuselection = -1;
        Scanner console = new Scanner(System.in);
        order_transaction ot = new order_transaction();

        while (menuselection != 0) {
            System.out.println("\n=======================================================");
            System.out.println("                   Order Transaction Menu               ");
            System.out.println("=======================================================");
            System.out.println("|  [1] Create a New Order                              |");
            System.out.println("|  [2] Update an Order                                 |");
            System.out.println("|  [3] Update a Product in an Order                    |");
            System.out.println("|  [4] Delete a Product in an Order                    |");
            System.out.println("|  [0] Exit Order Transaction Menu                     |");
            System.out.println("=======================================================");
            System.out.print("Enter Selected Function: ");

            if (console.hasNextInt()) {
                menuselection = console.nextInt();
                console.nextLine(); // Consume newline

                switch (menuselection) {
                    case 1:
                        System.out.println("\n------- Enter order information -------");
                        System.out.print("Order Date (yyyy-mm-dd): ");
                        ot.orderDate = java.sql.Date.valueOf(console.nextLine());
                        System.out.print("Required Date (yyyy-mm-dd): ");
                        ot.requiredDate = java.sql.Date.valueOf(console.nextLine());
                        System.out.print("Shipped Date (yyyy-mm-dd, optional): ");
                        String shippedDateStr = console.nextLine();
                        if (!shippedDateStr.isEmpty()) {
                            ot.shippedDate = java.sql.Date.valueOf(shippedDateStr);
                        }
                        System.out.print("Status: ");
                        ot.status = console.nextLine();
                        System.out.print("Comments: ");
                        ot.comments = console.nextLine();
                        System.out.print("Customer Number: ");
                        ot.customerNumber = console.nextInt();
                        console.nextLine(); // Consume newline

                        // Enter order details
                        System.out.println("\n------- Enter order details -------");
                        String addMore;
                        do {
                            System.out.print("Product Code: ");
                            String productCode = console.nextLine();
                            System.out.print("Quantity Ordered: ");
                            int quantityOrdered = console.nextInt();
                            System.out.print("Price Each: ");
                            double priceEach = console.nextDouble();
                            System.out.print("Order Line Number: ");
                            int orderLineNumber = console.nextInt();
                            console.nextLine(); // Consume newline

                            ot.orderDetails.add(new order_transaction.OrderDetail(ot.orderNumber, productCode, quantityOrdered, priceEach, orderLineNumber));

                            System.out.print("Add more products? (yes/no): ");
                            addMore = console.nextLine();
                        } while (addMore.equalsIgnoreCase("yes"));

                        ot.create_order();
                        break;
                    case 2:
                        System.out.println("\n------- Update order information -------");
                        System.out.print("Order Number: ");
                        ot.orderNumber = console.nextInt();
                        console.nextLine(); // Consume newline
                        System.out.print("Order Date (yyyy-mm-dd): ");
                        ot.orderDate = java.sql.Date.valueOf(console.nextLine());
                        System.out.print("Required Date (yyyy-mm-dd): ");
                        ot.requiredDate = java.sql.Date.valueOf(console.nextLine());
                        System.out.print("Shipped Date (yyyy-mm-dd, optional): ");
                        shippedDateStr = console.nextLine();
                        if (!shippedDateStr.isEmpty()) {
                            ot.shippedDate = java.sql.Date.valueOf(shippedDateStr);
                        }
                        System.out.print("Status: ");
                        ot.status = console.nextLine();
                        System.out.print("Comments: ");
                        ot.comments = console.nextLine();
                        System.out.print("Customer Number: ");
                        ot.customerNumber = console.nextInt();
                        console.nextLine(); // Consume newline

                        ot.update_order();
                        break;
                    case 3:
                        System.out.println("\n------- Update product in order -------");
                        System.out.print("Order Number: ");
                        ot.orderNumber = console.nextInt();
                        console.nextLine(); // Consume newline
                        System.out.print("Product Code: ");
                        String productCode = console.nextLine();
                        System.out.print("Quantity Ordered: ");
                        int quantityOrdered = console.nextInt();
                        System.out.print("Price Each: ");
                        double priceEach = console.nextDouble();
                        console.nextLine(); // Consume newline

                        ot.update_order_detail(productCode, quantityOrdered, priceEach);
                        break;
                    case 4:
                        System.out.println("\n------- Delete product in order -------");
                        System.out.print("Order Number: ");
                        ot.orderNumber = console.nextInt();
                        console.nextLine(); // Consume newline
                        System.out.print("Product Code: ");
                        productCode = console.nextLine();

                        ot.delete_order_detail(productCode);
                        break;
                    case 0:
                        System.out.println("\nExiting Order Transaction Menu.");
                        break;
                    default:
                        System.out.println("Invalid selection. Please enter a number between 0 and 4.");
                        break;
                }
            } else {
                System.out.println("Invalid input. Please enter a valid integer.");
                console.next(); // Consume the invalid input
            }
        }

        return menuselection;
    }

    public static void main(String args[]) {
        order_transaction_menu otm = new order_transaction_menu();
        otm.menu();
    }
}