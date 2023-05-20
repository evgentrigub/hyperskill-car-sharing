package carsharing.repositories;

import carsharing.models.Customer;

import java.util.List;

public interface ICustomerRepository {

    void createCustomer(Customer customer);

    void updateCustomer(Customer customer);

    Customer getCustomer(int customerId);

    List<Customer> getCustomers();
}
