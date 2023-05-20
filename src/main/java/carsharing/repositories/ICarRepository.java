package carsharing.repositories;

import carsharing.models.Car;

import java.util.List;

public interface ICarRepository {
    void createCar(Car car);

    Car getCar(int carId);

    List<Car> getCars(int companyId);

    void updateCar(Car car);
}
