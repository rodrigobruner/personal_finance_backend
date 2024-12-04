package app.finance.api.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import app.finance.api.Model.AccountModel;
import app.finance.api.Model.Status;

import java.util.List;

// IAccountRepository interface
public interface IAccountRepository extends JpaRepository<AccountModel, Integer> { // JPA Repository
    // Find account by user uid
    List<AccountModel> findByUserUid(int uid);
    // Find account by account id
    AccountModel getAccountById(int id);
    // Find account by user uid and status
    List<AccountModel> findByUserUidAndStatus(int uid, Status active);
}