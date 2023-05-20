package carsharing.repositories;

import carsharing.ConnectionContainer;
import carsharing.models.Car;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarRepository implements ICarRepository {

    private static CarRepository carRepository = null;

    public static CarRepository getInstance() {
        if (carRepository == null) {
            carRepository = new CarRepository();
        }
        return carRepository;
    }

    private CarRepository() {

//        final var sql = "DROP TABLE CAR";
        final var sql = "CREATE TABLE IF NOT EXISTS CAR(" +
                "ID INT AUTO_INCREMENT PRIMARY KEY, " +
                "NAME VARCHAR(255) UNIQUE NOT NULL, " +
                "COMPANY_ID INT NOT NULL, " +
                "IS_RENTED BOOLEAN NOT NULL, " +
                "FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(ID)" +
                ")";

        try(var stmt = ConnectionContainer.getConnection().createStatement()){
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createCar(Car car) {
        final var sql = "INSERT INTO CAR VALUES (DEFAULT, ?, ?, ?)";

        try (var stmt = ConnectionContainer.getConnection().prepareStatement(sql)) {
            stmt.setString(1, car.getName());
            stmt.setInt(2, car.getCompanyId());
            stmt.setBoolean(3, car.isRented());
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Car getCar(int carId) {
        final var sql = "SELECT * FROM CAR WHERE ID=?";

        try (var stmt = ConnectionContainer.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, carId);
            var rs = stmt.executeQuery();
            if (!rs.next()) {
                return null;
            }
            return convertToCar(rs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Car> getCars(int companyId) {
        final var sql = "SELECT ID AS ID, CAR.NAME AS NAME, CAR.COMPANY_ID AS COMPANY_ID, CAR.IS_RENTED as IS_RENTED " +
                "FROM CAR " +
                "WHERE CAR.COMPANY_ID = ? ORDER BY ID;";

        try (var stmt = ConnectionContainer.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, companyId);
            var rs = stmt.executeQuery();
            var cars = new ArrayList<Car>();
            while (rs.next()){
                cars.add(convertToCar(rs));
            }
            return cars;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateCar(Car car) {
        final var sql = "UPDATE CAR SET IS_RENTED=? WHERE ID=?";

        try (var stmt = ConnectionContainer.getConnection().prepareStatement(sql)) {
            stmt.setBoolean(1, car.isRented());
            stmt.setInt(2, car.getId());
            stmt.executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private Car convertToCar(ResultSet rs) throws SQLException {
        var car = new Car();
        car.setId(rs.getInt("ID"));
        car.setName(rs.getString("NAME"));
        car.setCompanyId(rs.getInt("COMPANY_ID"));
        car.setRented(rs.getBoolean("IS_RENTED"));
        return car;
    }
}
