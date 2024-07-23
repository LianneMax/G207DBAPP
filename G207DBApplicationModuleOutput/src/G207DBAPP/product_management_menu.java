package G207DBAPP;
import java.io.*;
import java.util.Scanner;

public class product_management_menu {

    public product_management_menu() {
    }

    public int menu() {
        int menuselection = -1;
        Scanner console = new Scanner(System.in);

        while (menuselection < 0 || menuselection > 5) {
            System.out.println();
            System.out.println("=======================================================");
            System.out.println("                Product Management Menu                ");
            System.out.println("=======================================================");
            System.out.println("|  [1] Create a new Product Record                    |");
            System.out.println("|  [2] Update a Product Record                        |");
            System.out.println("|  [3] Delete a Product Record                        |");
            System.out.println("|  [4] Discontinue a Product Record                   |");
            System.out.println("|  [5] View a Product Record                          |");
            System.out.println("|  [0] Exit Product Management                        |");
            System.out.println("=======================================================");
            System.out.print("Enter Selected Function: ");

            if (console.hasNextInt()) {
                menuselection = console.nextInt();
                if (menuselection < 0 || menuselection > 5) {
                    System.out.println("Invalid selection. Please enter a number between 0 and 5.");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid integer.");
                console.next(); // Consume the invalid input
            }
        }

        if (menuselection == 1) {
            createProduct(console);
        } else if (menuselection == 2) {
            updateProduct(console);
        } else if (menuselection == 3) {
            deleteProduct(console);
        } else if (menuselection == 4) {
            discontinueProduct(console);
        } else if (menuselection == 5) {
            viewProduct(console);
        }

        return menuselection;
    }

    private void createProduct(Scanner console) {
        product_management p = new product_management();
        console.nextLine(); // consume newline

        System.out.print("Enter product information\nProduct Code: ");
        p.productCode = getNonEmptyInput(console);
        System.out.print("Product Name: ");
        p.productName = getNonEmptyInput(console);
        System.out.print("Product Line: ");
        p.productLine = getNonEmptyInput(console);
        System.out.print("Product Scale: ");
        p.productScale = getNonEmptyInput(console);
        System.out.print("Product Description: ");
        p.productDescription = getNonEmptyInput(console);
        System.out.print("Product Vendor: ");
        p.productVendor = getNonEmptyInput(console);
        System.out.print("Initial quantity: ");
        p.quantityInStock = getValidInt(console);
        System.out.print("Buy Price: ");
        p.buyPrice = getValidFloat(console);
        System.out.print("MSRP: ");
        p.MSRP = getValidFloat(console);

        p.add_product();
    }

    private void updateProduct(Scanner console) {
        product_management p = new product_management();
        console.nextLine(); // consume newline

        System.out.print("Enter product information\nProduct Code: ");
        p.productCode = console.nextLine();

        if (p.get_product() == 0) {
            System.out.println("That product does not exist.");
        } else {
            if (p.discontinued) {
                System.out.println("This product is discontinued and cannot be updated.");
            } else {
                System.out.println("Current Product information");
                System.out.println("-------------------------------------------------------------------");
                System.out.println("Product Code        : " + p.productCode);
                System.out.println("Product Name        : " + p.productName);
                System.out.println("Product Line        : " + p.productLine);
                System.out.println("Product Scale       : " + p.productScale);
                System.out.println("Product Description : " + p.productDescription);
                System.out.println("Product Vendor      : " + p.productVendor);
                System.out.println("Initial quantity    : " + p.quantityInStock);
                System.out.println("Buy Price           : " + p.buyPrice);
                System.out.println("MSRP                : " + p.MSRP);

                System.out.println("Enter updated product information");
                System.out.println("-------------------------------------------------------------------");
                System.out.print("Product Name        : ");
                p.productName = getNonEmptyInput(console);
                System.out.print("Product Line        : ");
                p.productLine = getNonEmptyInput(console);
                System.out.print("Product Scale       : ");
                p.productScale = getNonEmptyInput(console);
                System.out.print("Product Description : ");
                p.productDescription = getNonEmptyInput(console);
                System.out.print("Product Vendor      : ");
                p.productVendor = getNonEmptyInput(console);
                System.out.print("Initial quantity    : ");
                p.quantityInStock = getValidInt(console);
                System.out.print("Buy Price           : ");
                p.buyPrice = getValidFloat(console);
                System.out.print("MSRP                : ");
                p.MSRP = getValidFloat(console);

                p.update_product();
            }
        }
    }

    private void deleteProduct(Scanner console) {
        product_management p = new product_management();
        console.nextLine(); // consume newline

        System.out.print("Enter product information\nProduct Code: ");
        p.productCode = console.nextLine();

        p.delete_product();
    }

    private void discontinueProduct(Scanner console) {
        product_management p = new product_management();
        console.nextLine(); // consume newline

        System.out.print("Enter product information\nProduct Code: ");
        p.productCode = console.nextLine();

        p.discontinue_product();
    }

    private void viewProduct(Scanner console) {
        product_management p = new product_management();
        console.nextLine(); // consume newline

        System.out.print("Enter product information\nProduct Code: ");
        p.productCode = console.nextLine();

        if (p.get_product() == 0) {
            System.out.println("That product does not exist.");
        } else {
            System.out.println("Current Product information");
            System.out.println("-------------------------------------------------------------------");
            System.out.println("Product Code        : " + p.productCode);
            System.out.println("Product Name        : " + p.productName);
            System.out.println("Product Line        : " + p.productLine);
            System.out.println("Product Scale       : " + p.productScale);
            System.out.println("Product Description : " + p.productDescription);
            System.out.println("Product Vendor      : " + p.productVendor);
            System.out.println("Initial quantity    : " + p.quantityInStock);
            System.out.println("Buy Price           : " + p.buyPrice);
            System.out.println("MSRP                : " + p.MSRP);
        }
    }

    private String getNonEmptyInput(Scanner console) {
        String input;
        while ((input = console.nextLine().trim()).isEmpty()) {
            System.out.print("Input cannot be empty. Please enter again: ");
        }
        return input;
    }

    private int getValidInt(Scanner console) {
        int value;
        while (true) {
            try {
                value = Integer.parseInt(console.nextLine().trim());
                break;
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a valid integer: ");
            }
        }
        return value;
    }

    private float getValidFloat(Scanner console) {
        float value;
        while (true) {
            try {
                value = Float.parseFloat(console.nextLine().trim());
                break;
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a valid float: ");
            }
        }
        return value;
    }

    public static void main(String args[]) {
        product_management_menu pmm = new product_management_menu();
        while (pmm.menu() != 0) {
        }
    }
}
