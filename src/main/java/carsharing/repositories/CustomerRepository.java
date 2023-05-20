package carsharing.repositories;

import carsharing.ConnectionContainer;
import carsharing.models.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository implements ICustomerRepository {

    private static CustomerRepository customerRepository = null;

    public static CustomerRepository getInstance() {
        if (customerRepository == null) {
            customerRepository = new CustomerRepository();
        }
        return customerRepository;
    }

    public CustomerRepository() {
//        final var sql = "DROP TABLE CUSTOMER";
        final var sql = "CREATE TABLE IF NOT EXISTS CUSTOMER("+
                "ID INT AUTO_INCREMENT PRIMARY KEY," +
                "NAME VARCHAR(255) UNIQUE NOT NULL," +
                "RENTED_CAR_ID INT NULL, " +
                "FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(ID)" +
                ")";
        try (var stmt = ConnectionContainer.getConnection().createStatement()) {
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createCustomer(Customer customer) {
        final var sql = "INSERT INTO CUSTOMER (NAME) VALUES (?)";

        try (var stmt = ConnectionContainer.getConnection().prepareStatement(sql)) {
            stmt.setString(1, customer.getName());
            stmt.executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void updateCustomer(Customer customer) {
        final var sql = "UPDATE CUSTOMER SET RENTED_CAR_ID=? WHERE ID=?";

        try (var stmt = ConnectionContainer.getConnection().prepareStatement(sql)) {
            var rentedCarId = customer.getRentedCarId();
            if (rentedCarId == 0) {
                stmt.setString(1, null);
            } else {
                stmt.setInt(1, rentedCarId);
            }
            stmt.setInt(2, customer.getId());
            stmt.executeUpdate();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Customer getCustomer(int customerId) {
        final var sql = "SELECT * FROM CUSTOMER WHERE ID=?";

        try (var stmt = ConnectionContainer.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            var rs = stmt.executeQuery();
            if (!rs.next()) {
                return null;
            }
            return convertToCustomer(rs);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Customer> getCustomers() {
        final var sql = "SELECT * FROM CUSTOMER ORDER BY ID";

        try (var stmt = ConnectionContainer.getConnection().createStatement()) {
            var rs = stmt.executeQuery(sql);
            var customers = new ArrayList<Customer>();
            while (rs.next()) {
                var customer = convertToCustomer(rs);
                customers.add(customer);
            }
            return customers;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private Customer convertToCustomer(ResultSet rs) throws SQLException {
        var customer = new Customer();
        customer.setId(rs.getInt("ID"));
        customer.setName(rs.getString("NAME"));
        customer.setRentedCarId(rs.getInt("RENTED_CAR_ID"));
        return customer;
    }
}
