package G207DBAPP;
import java.io.*;
import java.util.Scanner;
import java.sql.Date;

public class order_transaction_menu {

    public order_transaction_menu() {
    }

    public int menu() {
        int menuselection = -1;
        Scanner console = new Scanner(System.in);
        order_transaction ot = new order_transaction();

        while (menuselection != 0) {
            System.out.println();
            System.out.println("=======================================================");
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
                        createOrder(console, ot);
                        break;
                    case 2:
                        updateOrder(console, ot);
                        break;
                    case 3:
                        updateOrderDetail(console, ot);
                        break;
                    case 4:
                        deleteOrderDetail(console, ot);
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

    private void createOrder(Scanner console, order_transaction ot) {
        System.out.println("\n------- Enter order information -------");
        ot.orderNumber = getInt(console, "Order Number: ");
        if (ot.isOrderNumberExists(ot.orderNumber)) {
            System.out.println("This Order Number already exists. Please enter a different Order Number.");
            return;
        }
        ot.orderDate = getDate(console, "Order Date (yyyy-mm-dd): ");
        ot.requiredDate = getDate(console, "Required Date (yyyy-mm-dd): ");
        ot.shippedDate = getOptionalDate(console, "Shipped Date (yyyy-mm-dd, optional): ");
        ot.status = getString(console, "Status: ");
        ot.comments = getString(console, "Comments: ");
        ot.customerNumber = getInt(console, "Customer Number: ");

        // Enter order details
        System.out.println("\n------- Enter order details -------");
        String addMore;
        do {
            String productCode = getString(console, "Product Code: ");
            int quantityOrdered = getInt(console, "Quantity Ordered: ");
            double priceEach = getDouble(console, "Price Each: ");
            int orderLineNumber = getInt(console, "Order Line Number: ");

            ot.orderDetails.add(new order_transaction.OrderDetail(ot.orderNumber, productCode, quantityOrdered, priceEach, orderLineNumber));

            System.out.print("Add more products? (yes/no): ");
            addMore = console.nextLine();
        } while (addMore.equalsIgnoreCase("yes"));

        ot.create_order();
    }

    private void updateOrder(Scanner console, order_transaction ot) {
        System.out.println("\n------- Update order information -------");
        ot.orderNumber = getInt(console, "Order Number: ");

        // Fetch and display current order details
        if (ot.get_order() > 0) {
            // Current order information is already displayed within get_order()
            ot.orderDate = getDate(console, "Order Date (yyyy-mm-dd): ");
            ot.requiredDate = getDate(console, "Required Date (yyyy-mm-dd): ");
            ot.shippedDate = getOptionalDate(console, "Shipped Date (yyyy-mm-dd, optional): ");
            ot.status = getString(console, "Status: ");
            ot.comments = getString(console, "Comments: ");
            ot.customerNumber = getInt(console, "Customer Number: ");

            ot.update_order();
        } else {
            System.out.println("This Order does not exist");
        }
    }

    private void updateOrderDetail(Scanner console, order_transaction ot) {
        System.out.println("\n------- Update product in order -------");
        ot.orderNumber = getInt(console, "Order Number: ");
        String productCode = getString(console, "Product Code: ");

        // Fetch and display current order detail
        if (ot.get_order_detail(productCode) > 0) {
            // Current order detail information is already displayed within get_order_detail()
            int quantityOrdered = getInt(console, "Quantity Ordered: ");
            double priceEach = getDouble(console, "Price Each: ");

            ot.update_order_detail(productCode, quantityOrdered, priceEach);
        } else {
            System.out.println("This Product does not exist");
        }
    }

    private void deleteOrderDetail(Scanner console, order_transaction ot) {
        System.out.println("\n------- Delete product in order -------");
        ot.orderNumber = getInt(console, "Order Number: ");
        String productCode = getString(console, "Product Code: ");

        // Check if order detail exists before attempting to delete
        if (ot.get_order_detail(productCode) > 0) {
            ot.delete_order_detail(productCode);
        } else {
            System.out.println("This Product does not exist");
        }
    }

    private Date getDate(Scanner console, String prompt) {
        Date date = null;
        while (date == null) {
            System.out.print(prompt);
            try {
                date = Date.valueOf(console.nextLine());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid date format. Please enter a valid date (yyyy-mm-dd).");
            }
        }
        return date;
    }

    private Date getOptionalDate(Scanner console, String prompt) {
        System.out.print(prompt);
        String input = console.nextLine();
        if (input.isEmpty()) {
            return null;
        }
        try {
            return Date.valueOf(input);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date format. Please enter a valid date (yyyy-mm-dd).");
            return getOptionalDate(console, prompt);
        }
    }

    private int getInt(Scanner console, String prompt) {
        int value = 0;
        while (true) {
            System.out.print(prompt);
            if (console.hasNextInt()) {
                value = console.nextInt();
                console.nextLine(); // Consume newline
                break;
            } else {
                System.out.println("Invalid input. Please enter a valid integer.");
                console.next(); // Consume the invalid input
            }
        }
        return value;
    }

    private double getDouble(Scanner console, String prompt) {
        double value = 0;
        while (true) {
            System.out.print(prompt);
            if (console.hasNextDouble()) {
                value = console.nextDouble();
                console.nextLine(); // Consume newline
                break;
            } else {
                System.out.println("Invalid input. Please enter a valid number.");
                console.next(); // Consume the invalid input
            }
        }
        return value;
    }

    private String getString(Scanner console, String prompt) {
        System.out.print(prompt);
        return console.nextLine();
    }

    public static void main(String args[]) {
        order_transaction_menu otm = new order_transaction_menu();
        otm.menu();
    }
}
