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

// Transaction for the transaction controller
@RestController
@RequestMapping("/Transactions") // API route
@CrossOrigin(origins = "*") //  Allow requests from any origin
public class TransactionController {

    // dependency injection for the repositories
    @Autowired
    private ITransactionRepository transactionRepository;

    // dependency injection for the repositories
    @Autowired
    private IAccountRepository accountRepository;

    // Create a new transaction
    @PostMapping("/category/{category}") // Add a new transaction
    public TransactionModel createTransaction(@PathVariable String category, @RequestBody TransactionModel transaction) {
        // Get account by id
        AccountModel account = accountRepository.findById(transaction.getAccount().getId()).orElseThrow(() -> new RuntimeException("Account not found"));

        // If is expense, subtract the value from the account balance otherwise add the value
        if(CategoryType.Expense.equals(CategoryType.fromString(category))) {
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

    // Update a transaction
    @PutMapping("/{id}/category/{category}") // Update a transaction
    public ResponseEntity<TransactionModel> updateTransaction(@PathVariable int id, @PathVariable CategoryType category, @RequestBody TransactionModel transactionDetails) {
        // Get account by id
        AccountModel account = accountRepository.findById(transactionDetails.getAccount().getId()).orElseThrow(() -> new RuntimeException("Account not found"));
        // Get transaction by id
        Optional<TransactionModel> transactionOptional = transactionRepository.findById(id);
        // If transaction is not found
        if (!transactionOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // Get transaction
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
        // Update account balance
        accountRepository.save(account);

        // Update transaction details
        transaction.setAccount(transactionDetails.getAccount());
        transaction.setCategory(transactionDetails.getCategory());
        transaction.setValue(transactionDetails.getValue());
        transaction.setNotes(transactionDetails.getNotes());
        transaction.setDate(transactionDetails.getDate());
        transaction.setCreateAt(transaction.getCreateAt());
        // Save transaction
        return ResponseEntity.ok(transactionRepository.save(transaction));
    }

    // Delete a transaction
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable int id) {
        // Get transaction by id
        Optional<TransactionModel> transactionOptional = transactionRepository.findById(id);
        // If transaction is not found
        if (!transactionOptional.isPresent()) {
            // Return not found
            return ResponseEntity.notFound().build();
        }
        // Get transaction
        TransactionModel transaction = transactionOptional.get();
        // Get account by id
        AccountModel account = accountRepository.findById(transaction.getAccount().getId()).orElseThrow(() -> new RuntimeException("Account not found"));
        // If is expense, add the value to the account balance otherwise subtract the value
        if (transaction.getCategory().getCategoryType() == CategoryType.Expense) {
            account.setUpdatedAmount(account.getUpdatedAmount() + transaction.getValue());
        } else {
            account.setUpdatedAmount(account.getUpdatedAmount() - transaction.getValue());
        }
        // Update account balance
        accountRepository.save(account);
        // Delete transaction
        transactionRepository.delete(transaction);
        return ResponseEntity.noContent().build();
    }

    // Get all transactions
    @GetMapping
    public List<TransactionModel> getAllTransactions() {
        // Get all transactions
        return transactionRepository.findAll();
    }

    // Get a transaction by ID
    @GetMapping("/{id}")
    public ResponseEntity<TransactionModel> getTransactionById(@PathVariable int id) {
        // Get a transaction by ID
        Optional<TransactionModel> transaction = transactionRepository.findById(id);
        return transaction.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get transactions by user ID
    @GetMapping("/user/{uid}/category/{category}")
    public List<TransactionModel> getTransactionsByUidAndCategory(@PathVariable String uid, @PathVariable CategoryType category) {
        // Get transactions by user ID and category
        return transactionRepository.findByUidAndCategory(uid, category);
    }

    // Get transactions by user ID
    @GetMapping("/monthly-summary/{uid}")
    public Map<String, Object> getMonthlySummary(@PathVariable int uid) {
        // Get transactions by user ID
        Calendar calendar = Calendar.getInstance();
        // Get the date one year ago
        calendar.add(Calendar.YEAR, -1);
        // Set the date
        Date startDate = calendar.getTime();
        // Get monthly summary
        List<MonthlySummary> summaries = transactionRepository.findMonthlySummaryByUid(startDate, uid);
        // Initialize arrays
        double[] incomes = new double[12];
        double[] expenses = new double[12];
        String[] months = new String[12];

        Arrays.fill(incomes, 0);
        Arrays.fill(expenses, 0);

        // Fill arrays
        for (MonthlySummary summary : summaries) {
            // Get the month index
            int monthIndex = summary.getMonth() - 1;
            // If the category type is income, add the value to the incomes array
            if (summary.getCategoryType() == CategoryType.Income) {
                // Add the value to the incomes array
                incomes[monthIndex] = summary.getTotalValue();
            } else if (summary.getCategoryType() == CategoryType.Expense) {
                // Add the value to the expenses array
                expenses[monthIndex] = summary.getTotalValue();
            }
            // Set the month
            months[monthIndex] = new DateFormatSymbols().getShortMonths()[monthIndex] + " " + summary.getYear();
        }

        // Preencher meses que não têm transações
        Calendar cal = Calendar.getInstance();
        // Fill months that have no transactions
        for (int i = 0; i < 12; i++) {
            if (months[i] == null) {
                // Set the month
                cal.set(Calendar.MONTH, i);
                // Set the year
                months[i] = new DateFormatSymbols().getShortMonths()[i] + " " + cal.get(Calendar.YEAR);
            }
        }

        // Create a response
        Map<String, Object> response = new HashMap<>();
        response.put("incomes", incomes);
        response.put("expenses", expenses);
        response.put("months", months);
        // Return the response
        return response;
    }

    // Get transactions by user ID, category and date range
    @GetMapping("/category-summary/{uid}/{categoryType}/{startDate}/{endDate}")
    public List<CategorySummary> getCategorySummary(@PathVariable int uid,
                                                    @PathVariable CategoryType categoryType,
                                                    @PathVariable String startDate,
                                                    @PathVariable String endDate) throws ParseException {

        // Get transactions by user ID, category and date range
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Set the date format
        Date start = dateFormat.parse(startDate); // Set the start date
        Date end = dateFormat.parse(endDate); // Set the end date
        return transactionRepository.findCategorySummaryByUidAndTypeAndDateRange(uid, categoryType, start, end);
    }

}