package app.finance.api.Repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import app.finance.api.Model.TransferModel;

// ITransferRepository interface
public interface ITransferRepository extends JpaRepository<TransferModel, Integer> {
    // Find transfer by from account user uid
    @Query("SELECT t FROM TransferModel t WHERE t.fromAccount.user.uid = :uid")
    List<TransferModel> findByFromAccountUserUid(@Param("uid") int uid);
}