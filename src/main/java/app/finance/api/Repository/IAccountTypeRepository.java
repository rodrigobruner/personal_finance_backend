package app.finance.api.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import app.finance.api.Model.AccountTypeModel;

// IAccountTypeRepository interface
public interface IAccountTypeRepository extends JpaRepository<AccountTypeModel, Integer> {
    // Find account type by locale
    List<AccountTypeModel> findByLocale(String locale);
}