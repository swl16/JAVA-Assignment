package cinema;

import java.util.Scanner;

class Item {
    String name = "";
    double price = 0.0;
    int quantity = 0;
}

public class Concession {
    private Scanner scanner = new Scanner(System.in);

    private Item food = new Item();
    private Item drink = new Item();
    private Item combo = new Item();

    public void startOrder(){
        int choice;

        do {
            System.out.println("======= F & B =======");
            System.out.println("1. Buy Food");
            System.out.println("2. Buy Drink");
            System.out.println("3. Buy Combo");
            System.out.println("4. Exit Selection");
            System.out.print("Please select (1-4) : ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1: selectFood();
                break;
                case 2: selectDrink();
                break;
                case 3: selectCombo();
                break;
                case 4: System.out.println("Thank you!");
                showTotal();
                break;
                default: System.out.println("Invalid choice!");
            }
        } while (choice != 4);
    }

    private void showTotal() {
        boolean hasItems = false;
        if (food.quantity > 0) {
            System.out.println("Food : " + food.name + " (Qty : " + food.quantity + ")");
            hasItems = true;
        }
        if (drink.quantity > 0) {
            System.out.println("Drink : " + drink.name + " (Qty : " + drink.quantity + ")");
            hasItems = true;
        }
        if (combo.quantity > 0) {
            System.out.println("Combo : " + combo.name + " (Qty : " + combo.quantity + ")");
            hasItems = true;
        }

        if (!hasItems) {
            System.out.println("No items were ordered.");
        }
    }

    private void selectFood(){
        System.out.println("\n1. Popcorn (RM15) | 2. Hotdog (RM12) | 3. Nachos (RM10)");
        System.out.print("Please enter food code (1-3) : ");
        int code = scanner.nextInt();

        switch (code) {
            case 1: food.name = "Popcorn"; food.price = 15.0;
            break;
            case 2: food.name = "Hotdog"; food.price = 12.0;
            break;
            case 3: food.name = "Nachos"; food.price = 10.0;
            break;
            default: return;
        }
        System.out.print("Please enter quantity for " + food.name + ": ");
        food.quantity += scanner.nextInt();
    }

    private void selectDrink(){
        System.out.println("\n1. Cola (RM8) | 2. Water (RM3) | 3. Coffee (RM7)");
        System.out.print("Please enter drink code (1-3) : ");
        int code = scanner.nextInt();

        switch (code) {
            case 1: drink.name = "Cola"; drink.price = 8.0;
                break;
            case 2: drink.name = "Water"; drink.price = 3.0;
                break;
            case 3: drink.name = "Coffee"; drink.price = 7.0;
                break;
            default: return;
        }
        System.out.print("Please enter quantity for " + drink.name + ": ");
        drink.quantity += scanner.nextInt();
    }

    private void selectCombo(){
        System.out.println("\n1. Combo A - Popcorn + Cola (RM22) | 2. Combo B - " +
                "Hotdog + Cola (RM20) | 3. Combo C - Nachos + Cola (RM18)");
        System.out.print("Please enter combo code (1-3) : ");
        int code = scanner.nextInt();

        switch (code) {
            case 1: combo.name = "Popcorn + Cola"; combo.price = 22.0;
                break;
            case 2: combo.name = "Hotdog + Cola"; combo.price = 20.0;
                break;
            case 3: combo.name = "Nachos + Cola"; combo.price = 18.0;
                break;
            default: return;
        }
        System.out.print("Please enter quantity for " + combo.name + ": ");
        combo.quantity += scanner.nextInt();
    }
}

