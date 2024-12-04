package app.finance.api.Repository;

import app.finance.api.Model.CategoryType;
import app.finance.api.Model.TransactionModel;
import app.finance.api.dto.CategorySummary;
import app.finance.api.dto.MonthlySummary;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

// ITransactionRepository interface
@Repository
public interface ITransactionRepository extends JpaRepository<TransactionModel, Integer> {
        List<TransactionModel> findById(Long id);

        // Find transaction by account id
        @Query("SELECT t FROM TransactionModel t WHERE t.account.user.uid = :uid AND t.category.categoryType = :categoryType")
        List<TransactionModel> findByUidAndCategory(@Param("uid") String uid, @Param("categoryType") CategoryType categoryType);

        // Find transaction by account id and date range
        @Query("SELECT new app.finance.api.dto.MonthlySummary(MONTH(t.date), YEAR(t.date), t.category.categoryType, SUM(t.value)) " +
                "FROM TransactionModel t " +
                "WHERE t.date >= :startDate AND t.account.user.uid = :uid " +
                "GROUP BY YEAR(t.date), MONTH(t.date), t.category.categoryType " +
                "ORDER BY YEAR(t.date), MONTH(t.date)")
        List<MonthlySummary> findMonthlySummaryByUid(@Param("startDate") Date startDate, 
                                                        @Param("uid") int uid);

        // Find transaction by account id, type and date range
        @Query("SELECT new app.finance.api.dto.CategorySummary(t.category.id, SUM(t.value), t.category.name) " +
                "FROM TransactionModel t " +
                "WHERE t.account.user.uid = :uid AND t.category.categoryType = :categoryType AND t.date BETWEEN :startDate AND :endDate " +
                "GROUP BY t.category.id, t.category.name")
        List<CategorySummary> findCategorySummaryByUidAndTypeAndDateRange(@Param("uid") int uid,
                                                                        @Param("categoryType") CategoryType categoryType,
                                                                        @Param("startDate") Date startDate,
                                                                        @Param("endDate") Date endDate);
}