package app.finance.api.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import app.finance.api.Model.AccountTypeModel;

public interface IAccountTypeRepository extends JpaRepository<AccountTypeModel, Integer> {

    List<AccountTypeModel> findByLocale(String locale);
}