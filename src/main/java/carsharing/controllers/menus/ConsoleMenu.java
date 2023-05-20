package carsharing.controllers.menus;

import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

public abstract class ConsoleMenu implements IMenu {

    private final String[] menuOptionsList;

    protected ConsoleMenu(String[] menuOptionsList) {
        this.menuOptionsList = menuOptionsList;
    }

    @Override
    public void showMenu() {
        Arrays.stream(menuOptionsList).forEach(System.out::println);
    }

    protected String scanInput() {
        Scanner scanner = new Scanner(System.in);
        var input = scanner.nextLine().trim();
        System.out.println();
        return input;
    }

}
