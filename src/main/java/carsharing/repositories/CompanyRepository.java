package carsharing.repositories;

import carsharing.ConnectionContainer;
import carsharing.models.Company;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompanyRepository implements ICompanyRepository {

    private static CompanyRepository companyRepository = null;

    public static CompanyRepository getInstance() {
        if (companyRepository == null) {
            companyRepository = new CompanyRepository();
        }
        return companyRepository;
    }

    private CompanyRepository() {
//        final var sql = "DROP TABLE COMPANY";
        final var sql = "CREATE TABLE IF NOT EXISTS COMPANY(" +
                "ID INT AUTO_INCREMENT PRIMARY KEY, " +
                "NAME VARCHAR(255) UNIQUE NOT NULL" +
                ")";

        try(var stmt = ConnectionContainer.getConnection().createStatement()){
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createCompany(Company company) {
        final var sql = "INSERT INTO COMPANY VALUES (DEFAULT, ?)";

        try (var stmt = ConnectionContainer.getConnection().prepareStatement(sql)) {
            stmt.setString(1, company.getName());
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Company> getCompanies() {
        var sql = "SELECT * FROM COMPANY ORDER BY ID";

        try (var stmt = ConnectionContainer.getConnection().createStatement()) {
            var rs = stmt.executeQuery(sql);
            var companies = new ArrayList<Company>();
            while (rs.next()){
                companies.add(convertToCompany(rs));
            }
            return companies;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Company getCompany(int companyId) {
        var sql = "SELECT * FROM COMPANY WHERE ID=?";

        try (var stmt = ConnectionContainer.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, companyId);
            var rs = stmt.executeQuery();
            if (!rs.next()) {
                return null;
            }
            return convertToCompany(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Company convertToCompany(ResultSet rs) throws SQLException {
        var company = new Company();
        company.setId(rs.getInt("ID"));
        company.setName(rs.getString("NAME"));
        return company;
    }
}
