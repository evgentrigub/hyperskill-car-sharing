package carsharing.controllers.menus;

import carsharing.models.Company;
import carsharing.repositories.CompanyRepository;

import javax.crypto.Cipher;
import java.util.List;
import java.util.Scanner;

public class ConsoleManagerMenu extends ConsoleMenu {

    private static final String[] MENU_OPTIONS_LIST = {
            "1. Company list",
            "2. Create a company",
            "0. Back"
    };

    protected ConsoleManagerMenu() {
        super(MENU_OPTIONS_LIST);
    }

    @Override
    public void handleUserInput(int menuNum) {
        switch (menuNum) {
            case 0 -> {
                var menu = new ConsoleMainMenu();
                menu.showMenu();
                menu.handleUserInput(Integer.parseInt(scanInput()));
            }
            case 1 -> {
                this.showCompanyList();
            }
            case 2 -> {
                this.createCompany();
            }
            default -> {
            }
        }
    }

    private void showCompanyList(){
        var companies = CompanyRepository.getInstance().getCompanies();
        if(companies.isEmpty()){
            System.out.println("The company list is empty!");
            System.out.println();
            this.showMenu();
            this.handleUserInput(Integer.parseInt(scanInput()));
        } else {
            handleUserInputCompany(companies);
            System.out.println();
        }
    }

    public void createCompany(){
        Company company = new Company();
        System.out.println("Enter the company name:");
        company.setName(scanInput());
        CompanyRepository.getInstance().createCompany(company);
        System.out.println("The company was created!");
        System.out.println();

        this.showMenu();
        this.handleUserInput(Integer.parseInt(scanInput()));
    }

    private void handleUserInputCompany(List<Company> companies) {
        System.out.println("Choose the company:");
        companies.forEach(el -> {
            System.out.printf("%s. %s\n", el.getId(), el.getName());
        });
        System.out.println("0. Back");

        int companyId = Integer.parseInt(scanInput());
        if(companyId == 0){
            this.handleUserInput(Integer.parseInt(scanInput()));
        } else {
            ConsoleCompanyMenu menu = new ConsoleCompanyMenu(companyId);
            System.out.printf("'%s' company\n", menu.company.getName());
            menu.showMenu();
            menu.handleUserInput(Integer.parseInt(scanInput()));
        }
    }
}
