package carsharing.controllers.menus;

import carsharing.models.Customer;
import carsharing.repositories.CustomerRepository;

import java.util.List;

public class ConsoleMainMenu extends ConsoleMenu {

    private static final String[] MENU_OPTIONS_LIST = {
            "1. Log in as a manager",
            "2. Log in as a customer",
            "3. Create a customer",
            "0. Exit"
    };

    public ConsoleMainMenu() {
        super(MENU_OPTIONS_LIST);
    }

    @Override
    public void handleUserInput(int menuNum) {
        switch (menuNum) {
            case 1 -> this.showManagerMenu();
            case 2 -> this.showCustomerMenu();
            case 3 -> this.handleCreateCustomer();
            default -> {}
        }
    }

    public void showManagerMenu(){
        ConsoleManagerMenu menu = new ConsoleManagerMenu();
        menu.showMenu();
        menu.handleUserInput(Integer.parseInt(scanInput()));
    }

    public void showCustomerMenu(){
        List<Customer> customers = CustomerRepository.getInstance().getCustomers();

        if (customers.isEmpty()) {
            System.out.println("The customer list is empty!");
            System.out.println();
            this.showMenu();
            this.handleUserInput(Integer.parseInt(scanInput()));
            return;
        }

        customers.forEach(el -> {
            System.out.printf("%s. %s\n", el.getId(), el.getName());
        });
        System.out.println("0. Back");

        int input = Integer.parseInt(scanInput());
        if (input == 0){
            this.showMenu();
            this.handleUserInput(Integer.parseInt(scanInput()));
        } else {
            ConsoleCustomerMenu menu = new ConsoleCustomerMenu(input);
            menu.showMenu();
            menu.handleUserInput(Integer.parseInt(scanInput()));
        }
    }

    private void handleCreateCustomer() {
        var customer = new Customer();
        System.out.println("Enter the customer name:");
        customer.setName(scanInput());
        CustomerRepository.getInstance().createCustomer(customer);
        System.out.println("The customer was added!");
        System.out.println();
        this.showMenu();
        this.handleUserInput(Integer.parseInt(scanInput()));
    }
}
