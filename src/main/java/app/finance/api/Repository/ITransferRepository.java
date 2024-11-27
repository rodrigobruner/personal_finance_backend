package app.finance.api.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import app.finance.api.Model.TransferModel;

public interface ITransferRepository extends JpaRepository<TransferModel, Integer> {

}