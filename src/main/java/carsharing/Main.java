package carsharing;

import carsharing.controllers.ConsoleController;
import carsharing.repositories.CarRepository;
import carsharing.repositories.CompanyRepository;
import carsharing.repositories.CustomerRepository;

import java.io.File;
import java.sql.SQLException;

public class Main {
    private static final String DB_FOLDER_NAME = "src/carsharing/db";
    private static final String DB_FILE_DEFAULT_NAME = "carsharing";

    public static void main(String[] args) throws SQLException {
        final var dbFileName = DB_FILE_DEFAULT_NAME;
        final var dbFolder = new File(DB_FOLDER_NAME);
        final var dbFile = new File(dbFolder, dbFileName);
        final var dbFileWithExt = new File(dbFolder, String.format("%s.mv.db", dbFileName));
        final var dbUri = String.format("jdbc:h2:%s", dbFile.getAbsolutePath());

        dbFolder.mkdirs();
        try {
            dbFileWithExt.createNewFile();
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        ConnectionContainer.setDbUri(dbUri);
        CompanyRepository.getInstance();
        CarRepository.getInstance();
        CustomerRepository.getInstance();
        new ConsoleController().run();

        ConnectionContainer.closeConnection();
    }
}
