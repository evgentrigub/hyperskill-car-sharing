package carsharing.repositories;

import carsharing.models.Company;

import java.util.List;

public interface ICompanyRepository {
    void createCompany(Company company);

    Company getCompany(int companyId);

    List<Company> getCompanies();
}
