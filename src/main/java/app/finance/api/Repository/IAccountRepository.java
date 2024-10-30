package app.finance.api.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import app.finance.api.Model.AccountModel;
import java.util.List;

public interface IAccountRepository extends JpaRepository<AccountModel, Integer> {
    List<AccountModel> findByUserUid(int uid);
}