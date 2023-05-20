package carsharing.controllers.menus;

import carsharing.models.Car;
import carsharing.models.Company;
import carsharing.models.Customer;
import carsharing.repositories.CarRepository;
import carsharing.repositories.CompanyRepository;
import carsharing.repositories.CustomerRepository;

import java.util.List;
import java.util.stream.IntStream;

public class ConsoleCustomerMenu extends ConsoleMenu {

    Customer customer;

    private static final String[] MENU_OPTIONS_LIST = {
            "1. Rent a car",
            "2. Return a rented car",
            "3. My rented car",
            "0. Back"
    };
    protected ConsoleCustomerMenu(int customerId) {
        super(MENU_OPTIONS_LIST);
        this.customer = CustomerRepository.getInstance().getCustomer(customerId);
    }

    @Override
    public void handleUserInput(int menuNum) {
        switch(menuNum){
            case 0 -> {
                var menu = new ConsoleMainMenu();
                menu.showMenu();
                menu.handleUserInput(Integer.parseInt(scanInput()));
            }
            case 1 -> { this.handleRentCar(customer); }
            case 2 -> { this.handleReturnCar(customer); }
            case 3 -> { this.handleShowRentedCar(customer); }
        }
    }

    private void handleRentCar(Customer customer){
        if (customer.getRentedCarId() != 0) {
            System.out.println("You've already rented a car!");
            System.out.println();
            this.showMenu();
            this.handleUserInput(Integer.parseInt(scanInput()));
            return;
        }

        final var companies = CompanyRepository.getInstance().getCompanies();
        if (companies.isEmpty()) {
            System.out.println("The company list is empty!");
            System.out.println();
            this.showMenu();
            this.handleUserInput(Integer.parseInt(scanInput()));
            return;
        }

        handelCompanies(companies);
    }

    private void handelCompanies(List<Company> companies){
        this.showCompanies(companies);
        int companyId = Integer.parseInt(scanInput());
        if (companyId == 0){
            this.showMenu();
            this.handleUserInput(Integer.parseInt(scanInput()));
            return;
        }

        final Company company = CompanyRepository.getInstance().getCompany(companyId);
        final List<Car> cars = CarRepository.getInstance().getCars(company.getId())
                .stream().filter(el -> !el.isRented()).toList();

        if (cars.isEmpty()) {
            System.out.printf("No available cars in the '%s' company\n", company.getName());
            System.out.println();
//            this.showCompanies(companies);
            this.handelCompanies(companies);
            return;
        }

        this.showCars(cars);
        int carInput = Integer.parseInt(scanInput());
        if(carInput == 0){
//            this.showCompanies(companies);
            this.handelCompanies(companies);
            return;
        }
        Car car = cars.get(carInput - 1);
        car.setRented(true);
        CarRepository.getInstance().updateCar(car);

        customer.setRentedCarId(car.getId());
        CustomerRepository.getInstance().updateCustomer(customer);

        System.out.printf("You rented '%s'\n", car.getName());
        System.out.println();
        this.showMenu();
        this.handleUserInput(Integer.parseInt(scanInput()));
    }

    private void showCars(List<Car> cars){
        System.out.println("Choose a car:");
        IntStream.range(0, cars.size())
                .forEach(idx -> System.out.printf("%d. %s\n", idx + 1, cars.get(idx).getName()));
        System.out.println("0. Back");
    }

    private void showCompanies(List<Company> companies){
        System.out.println("Choose a company:");
        companies.forEach(el -> {
            System.out.printf("%s. %s\n", el.getId(), el.getName());
        });
        System.out.println("0. Back");
    }

    private void handleReturnCar(Customer customer){
        if (customer.getRentedCarId() == 0) {
            System.out.println("You didn't rent a car!");
            System.out.println();
            this.showMenu();
            this.handleUserInput(Integer.parseInt(scanInput()));
            return;
        }

        Car car = CarRepository.getInstance().getCar(customer.getRentedCarId());
        car.setRented(false);
        CarRepository.getInstance().updateCar(car);

        customer.setRentedCarId(0);
        CustomerRepository.getInstance().updateCustomer(customer);

        System.out.println("You've returned a rented car!");
        System.out.println();

        this.showMenu();
        this.handleUserInput(Integer.parseInt(scanInput()));
    }

    private void handleShowRentedCar(Customer customer){
        System.out.println(customer.getRentedCarId());
        if (customer.getRentedCarId() == 0) {
            System.out.println("You didn't rent a car!");
            System.out.println();
            this.showMenu();
            this.handleUserInput(Integer.parseInt(scanInput()));
            return;
        }
        var car = CarRepository.getInstance().getCar(customer.getRentedCarId());
        var company = CompanyRepository.getInstance().getCompany(car.getCompanyId());
        System.out.println("Your rented car:");
        System.out.println(car.getName());
        System.out.println("Company:");
        System.out.println(company.getName());
        System.out.println();
        this.showMenu();
        this.handleUserInput(Integer.parseInt(scanInput()));
    }
}
