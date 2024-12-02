package app.finance.api.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import app.finance.api.dto.CategorySummary;
import app.finance.api.dto.MonthlySummary;
import app.finance.api.Model.AccountModel;
import app.finance.api.Model.CategoryType;
import app.finance.api.Model.TransactionModel;
import app.finance.api.Repository.IAccountRepository;
import app.finance.api.Repository.ITransactionRepository;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/Transactions")
@CrossOrigin(origins = "*")
public class TransactionController {

    @Autowired
    private ITransactionRepository transactionRepository;

    @Autowired
    private IAccountRepository accountRepository;

    @PostMapping("/category/{category}") // Add a new transaction
    public TransactionModel createTransaction(@PathVariable CategoryType category, @RequestBody TransactionModel transaction) {
        // Get account by id
        AccountModel account = accountRepository.findById(transaction.getAccount().getId()).orElseThrow(() -> new RuntimeException("Account not found"));

        // If is expense, subtract the value from the account balance otherwise add the value
        if(category == CategoryType.Expense) {
            account.setUpdatedAmount(account.getUpdatedAmount() - transaction.getValue());
        } else {
            account.setUpdatedAmount(account.getUpdatedAmount() + transaction.getValue());
        }

        //Update account balance
        accountRepository.save(account);
        //Set transaction date
        transaction.setCreateAt(new Date());
        //Save transaction
        return transactionRepository.save(transaction);
    }

    @PutMapping("/{id}/category/{category}") // Update a transaction
    public ResponseEntity<TransactionModel> updateTransaction(@PathVariable int id, @PathVariable CategoryType category, @RequestBody TransactionModel transactionDetails) {
        // Get account by id
        AccountModel account = accountRepository.findById(transactionDetails.getAccount().getId()).orElseThrow(() -> new RuntimeException("Account not found"));
        // Get transaction by id
        Optional<TransactionModel> transactionOptional = transactionRepository.findById(id);

        if (!transactionOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        TransactionModel transaction = transactionOptional.get();

        // If is expense, subtract the value from the account balance otherwise add the value
        if (category == CategoryType.Expense) {
            if (transaction.getValue() > transactionDetails.getValue()) { // If the value is less than the previous value, add the difference to the account balance
                account.setUpdatedAmount(account.getUpdatedAmount() + (transaction.getValue() - transactionDetails.getValue()));
            } else { // If the value is greater than the previous value, subtract the difference from the account balance
                account.setUpdatedAmount(account.getUpdatedAmount() - (transactionDetails.getValue() - transaction.getValue()));
            }
        } else { // If is income, do the opposite
            if (transaction.getValue() > transactionDetails.getValue()) {
                account.setUpdatedAmount(account.getUpdatedAmount() - (transaction.getValue() - transactionDetails.getValue()));
            } else {
                account.setUpdatedAmount(account.getUpdatedAmount() + (transactionDetails.getValue() - transaction.getValue()));
            }
        }

        accountRepository.save(account);

        // Update transaction details
        transaction.setAccount(transactionDetails.getAccount());
        transaction.setCategory(transactionDetails.getCategory());
        transaction.setValue(transactionDetails.getValue());
        transaction.setNotes(transactionDetails.getNotes());
        transaction.setDate(transactionDetails.getDate());
        transaction.setCreateAt(transaction.getCreateAt());

        return ResponseEntity.ok(transactionRepository.save(transaction));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable int id) {
        Optional<TransactionModel> transactionOptional = transactionRepository.findById(id);

        if (!transactionOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        TransactionModel transaction = transactionOptional.get();
        AccountModel account = accountRepository.findById(transaction.getAccount().getId()).orElseThrow(() -> new RuntimeException("Account not found"));

        if (transaction.getCategory().getCategoryType() == CategoryType.Expense) {
            account.setUpdatedAmount(account.getUpdatedAmount() + transaction.getValue());
        } else {
            account.setUpdatedAmount(account.getUpdatedAmount() - transaction.getValue());
        }
        accountRepository.save(account);

        transactionRepository.delete(transaction);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<TransactionModel> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionModel> getTransactionById(@PathVariable int id) {
        Optional<TransactionModel> transaction = transactionRepository.findById(id);
        return transaction.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{uid}/category/{category}")
    public List<TransactionModel> getTransactionsByUidAndCategory(@PathVariable String uid, @PathVariable CategoryType category) {
        return transactionRepository.findByUidAndCategory(uid, category);
    }


    @GetMapping("/monthly-summary/{uid}")
    public Map<String, Object> getMonthlySummary(@PathVariable int uid) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);
        Date startDate = calendar.getTime();

        List<MonthlySummary> summaries = transactionRepository.findMonthlySummaryByUid(startDate, uid);

        double[] incomes = new double[12];
        double[] expenses = new double[12];
        String[] months = new String[12];

        // Inicializar arrays com valores zero
        Arrays.fill(incomes, 0);
        Arrays.fill(expenses, 0);

        for (MonthlySummary summary : summaries) {
            int monthIndex = summary.getMonth() - 1;
            if (summary.getCategoryType() == CategoryType.Income) {
                incomes[monthIndex] = summary.getTotalValue();
            } else if (summary.getCategoryType() == CategoryType.Expense) {
                expenses[monthIndex] = summary.getTotalValue();
            }
            months[monthIndex] = new DateFormatSymbols().getShortMonths()[monthIndex] + " " + summary.getYear();
        }

        // Preencher meses que não têm transações
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < 12; i++) {
            if (months[i] == null) {
                cal.set(Calendar.MONTH, i);
                
                months[i] = new DateFormatSymbols().getShortMonths()[i] + " " + cal.get(Calendar.YEAR);
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("incomes", incomes);
        response.put("expenses", expenses);
        response.put("months", months);

        return response;
    }

    @GetMapping("/category-summary/{categoryType}/{startDate}/{endDate}")
    public List<CategorySummary> getCategorySummary(@PathVariable CategoryType categoryType,
                                                    @PathVariable String startDate,
                                                    @PathVariable String endDate) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date start = dateFormat.parse(startDate);
        Date end = dateFormat.parse(endDate);
        return transactionRepository.findCategorySummaryByTypeAndDateRange(categoryType, start, end);
    }

}