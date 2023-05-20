package carsharing.controllers;

import carsharing.controllers.menus.ConsoleMainMenu;

import java.util.Scanner;

public class ConsoleController implements Runnable {
    @Override
    public void run() {
        var mainMenu = new ConsoleMainMenu();
        mainMenu.showMenu();

        Scanner scanner = new Scanner(System.in);
        var menuNum = scanner.nextInt();

        mainMenu.handleUserInput(menuNum);
    }
}
