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

@RestController
@CrossOrigin(origins = "*") 
@RequestMapping("/AccountTypes")
public class AccountTypeController {

    @Autowired
    private IAccountTypeRepository accountTypeRepository;

    @GetMapping("/")
    public ResponseEntity<List<AccountTypeModel>> getAllAccountTypes(@RequestParam String locale) {
        List<AccountTypeModel> accountTypes = accountTypeRepository.findByLocale(locale);
        return ResponseEntity.ok(accountTypes);
    }
}
