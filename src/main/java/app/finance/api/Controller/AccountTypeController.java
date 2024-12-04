package app.finance.api.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.finance.api.Model.AccountTypeModel;
import app.finance.api.Repository.IAccountTypeRepository;

// AccountType for the account type controller
@RestController
@CrossOrigin(origins = "*") // Allow requests from any origin
@RequestMapping("/AccountTypes") // API route
public class AccountTypeController {

    // dependency injection for the repositories
    @Autowired
    private IAccountTypeRepository accountTypeRepository;

    // Get all account types
    @GetMapping("/")
    public ResponseEntity<List<AccountTypeModel>> getAllAccountTypes(@RequestParam String locale) {
        // get all account types by locale
        List<AccountTypeModel> accountTypes = accountTypeRepository.findByLocale(locale);
        return ResponseEntity.ok(accountTypes);
    }
}
