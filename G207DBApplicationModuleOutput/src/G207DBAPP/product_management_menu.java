package G207DBAPP;
import java.io.*;
import java.util.Scanner;



public class product_management_menu {

    public product_management_menu() {
        
    }
    
    public int menu() {
        int menuselection = -1;
        Scanner console = new Scanner(System.in);
        
        while (true) {
            // Display the Menu
            System.out.println();
            System.out.println("=======================================================");
            System.out.println("                   Product Management Menu             ");
            System.out.println("=======================================================");
            System.out.println("|  [1] Create a New Product                            |");
            System.out.println("|  [2] Update a Product Record                         |");
            System.out.println("|  [3] Delete a Product Record                         |");
            System.out.println("|  [4] Discontinue a Product                           |");
            System.out.println("|  [5] View a Product Record                           |");
            System.out.println("|  [0] Exit Product Management                         |");
            System.out.println("=======================================================");

            System.out.print("Enter Selected Function: ");
            String input = console.nextLine();
            
            try {
                menuselection = Integer.parseInt(input);
                if (menuselection < 0 || menuselection > 5) {
                    System.out.println("Invalid selection. Please enter a number between 0 and 5.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                continue;
            }
            
            if (menuselection == 0) {
                break;  // Exit the menu
            }
            
            performMenuAction(menuselection, console);
        }
        return menuselection;
    }
    
    private void performMenuAction(int menuselection, Scanner console) {
        product_management p = new product_management();
        
        switch (menuselection) {
            case 1:
                // Adding a new Record, ask the user for the values of the record fields
                System.out.println("Enter product information");
                System.out.print("Product Code        : ");
                p.productCode = console.nextLine();

                if (p.get_product() > 0) {
                    System.out.println("This Product Already Exists");
                    return;
                }

                System.out.print("Product Name        : ");
                p.productName = console.nextLine();
                while (true) {
                    System.out.print("Product Line        : ");
                    p.productLine = console.nextLine();
                    if (!p.isProductLineValid(p.productLine)) {
                        System.out.println("Invalid Product Line. Valid options are: Classic Cars, Motorcycles, Planes, Ships, Trains, Trucks and Buses, Vintage Cars");
                    } else {
                        break;
                    }
                }
                while (true) {
                    System.out.print("Product Scale       : ");
                    p.productScale = console.nextLine();
                    if (!p.isProductScaleValid(p.productScale)) {
                        System.out.println("Invalid Product Scale. Format should be n:n");
                    } else {
                        break;
                    }
                }
                System.out.print("Product Description : ");
                p.productDescription = console.nextLine();
                System.out.print("Product Vendor      : ");
                p.productVendor = console.nextLine();
                while (true) {
                    try {
                        System.out.print("Initial quantity    : ");
                        p.quantityInStock = Integer.parseInt(console.nextLine());
                        if (p.quantityInStock < 0) {
                            throw new NumberFormatException();
                        }
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input for quantity. Please enter a non-negative integer.");
                    }
                }
                while (true) {
                    try {
                        System.out.print("Buy Price           : ");
                        p.buyPrice = Float.parseFloat(console.nextLine());
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input for Buy Price. Please enter a valid number.");
                    }
                }
                while (true) {
                    try {
                        System.out.print("MSRP                : ");
                        p.MSRP = Float.parseFloat(console.nextLine());
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input for MSRP. Please enter a valid number.");
                    }
                }
                
                p.add_product();
                break;
            
            case 2:
                // Updating a Record
                System.out.println("Enter product information");
                System.out.print("Product Code        : ");
                p.productCode = console.nextLine();

                if (p.get_product() == 0) {
                    System.out.println("This Product does not exist");
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
                    p.productName = console.nextLine();
                    while (true) {
                        System.out.print("Product Line        : ");
                        p.productLine = console.nextLine();
                        if (!p.isProductLineValid(p.productLine)) {
                            System.out.println("Invalid Product Line. Valid options are: Classic Cars, Motorcycles, Planes, Ships, Trains, Trucks and Buses, Vintage Cars");
                        } else {
                            break;
                        }
                    }
                    while (true) {
                        System.out.print("Product Scale       : ");
                        p.productScale = console.nextLine();
                        if (!p.isProductScaleValid(p.productScale)) {
                            System.out.println("Invalid Product Scale. Format should be n:n");
                        } else {
                            break;
                        }
                    }
                    System.out.print("Product Description : ");
                    p.productDescription = console.nextLine();
                    System.out.print("Product Vendor      : ");
                    p.productVendor = console.nextLine();
                    while (true) {
                        try {
                            System.out.print("Initial quantity    : ");
                            p.quantityInStock = Integer.parseInt(console.nextLine());
                            if (p.quantityInStock < 0) {
                                throw new NumberFormatException();
                            }
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input for quantity. Please enter a non-negative integer.");
                        }
                    }
                    while (true) {
                        try {
                            System.out.print("Buy Price           : ");
                            p.buyPrice = Float.parseFloat(console.nextLine());
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input for Buy Price. Please enter a valid number.");
                        }
                    }
                    while (true) {
                        try {
                            System.out.print("MSRP                : ");
                            p.MSRP = Float.parseFloat(console.nextLine());
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input for MSRP. Please enter a valid number.");
                        }
                    }
                    
                    p.update_product();
                }
                break;
                
            case 3:
                System.out.println("Enter product information");
                System.out.print("Product Code        : ");
                p.productCode = console.nextLine();        
                p.delete_product();
                break;
                
            case 4:
                System.out.println("Enter product information");
                System.out.print("Product Code        : ");
                p.productCode = console.nextLine();        
                p.discontinue_product();
                break;
                
            case 5:
                System.out.println("Enter product information");
                System.out.print("Product Code        : ");
                p.productCode = console.nextLine();

                if (p.get_product() == 0) {
                    System.out.println("This Product does not exist");
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

                    System.out.print("Enter the year to view orders: ");
                    int year = Integer.parseInt(console.nextLine());
                    p.get_product_orders(year);
                }
                break;
        }
    }
}
