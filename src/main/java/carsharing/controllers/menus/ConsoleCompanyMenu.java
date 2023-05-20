package carsharing.controllers.menus;

import carsharing.models.Car;
import carsharing.models.Company;
import carsharing.repositories.CarRepository;
import carsharing.repositories.CompanyRepository;

import java.util.stream.IntStream;

public class ConsoleCompanyMenu extends ConsoleMenu {

    Company company;

    private static final String[] MENU_OPTIONS_LIST = {
            "1. Car list",
            "2. Create a car",
            "0. Back"
    };

    protected ConsoleCompanyMenu(int companyId) {
        super(MENU_OPTIONS_LIST);
        this.company = CompanyRepository.getInstance().getCompany(companyId);
    }

    @Override
    public void handleUserInput(int menuNum) {
        switch (menuNum) {
            case 0 -> {
                var menu = new ConsoleManagerMenu();
                menu.showMenu();
                menu.handleUserInput(Integer.parseInt(scanInput()));
            }
            case 1 -> {
                this.showCarList(this.company.getId());
            }
            case 2 -> {
                this.createCar();
            }
            default -> {
            }
        }
    }

    private void showCarList(int companyId) {
        var cars = CarRepository.getInstance().getCars(companyId);
        if (cars.isEmpty()) {
            System.out.println("The car list is empty!");
            System.out.println();
            this.showMenu();
            this.handleUserInput(Integer.parseInt(scanInput()));
        } else {
            System.out.println("Car list:");
            IntStream.range(0, cars.size())
                    .forEach(idx -> System.out.printf("%d. %s\n", idx + 1, cars.get(idx).getName()));
            System.out.println();

            showMenu();
            handleUserInput(Integer.parseInt(scanInput()));
        }
    }

    private void createCar() {
        Car car = new Car();
        System.out.println("Enter the car name:");
        car.setName(scanInput());
        car.setCompanyId(this.company.getId());
        car.setRented(false);
        CarRepository.getInstance().createCar(car);
        System.out.println("The car was added!");
        System.out.println();

        showMenu();
        handleUserInput(Integer.parseInt(scanInput()));
    }

}
