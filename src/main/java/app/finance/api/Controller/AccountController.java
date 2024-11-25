package app.finance.api.Controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import app.finance.api.Model.AccountModel;
import app.finance.api.Model.AccountTypeModel;
import app.finance.api.Model.Status;
import app.finance.api.Model.UserModel;
import app.finance.api.Repository.IAccountRepository;
import app.finance.api.Repository.IAccountTypeRepository;
import app.finance.api.Repository.IUserRepository;

@RestController
@RequestMapping("/Accounts")
@CrossOrigin(origins = "*")
public class AccountController {

    @Autowired
    private IAccountRepository accountRepository;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IAccountTypeRepository accountTypeRepository;

    @PostMapping("/")
    public ResponseEntity<AccountModel> createAccount(@RequestBody AccountModel accountModel) {
        try {
            // Definir createAt com o datetime do servidor
            accountModel.setCreateAt(new Date());

            // Definir status como Ativo
            accountModel.setStatus(Status.Active);

            // Converter uid para UserModel
            Optional<UserModel> user = userRepository.findById(accountModel.getUser().getUid());
            if (user.isPresent()) {
                accountModel.setUser(user.get());
            } else {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            // Converter accountType_id para AccountTypeModel
            Optional<AccountTypeModel> accountType = accountTypeRepository.findById(accountModel.getAccountType().getId());
            if (accountType.isPresent()) {
                accountModel.setAccountType(accountType.get());
            } else {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            AccountModel savedAccount = accountRepository.save(accountModel);
            return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountModel> updateAccount(@PathVariable("id") int id, @RequestBody AccountModel accountModel) {
        Optional<AccountModel> accountData = accountRepository.findById(id);

        if (accountData.isPresent()) {
            AccountModel existingAccount = accountData.get();
            existingAccount.setName(accountModel.getName());
            existingAccount.setAccountType(accountModel.getAccountType());
            existingAccount.setInitialAmount(accountModel.getInitialAmount());
            existingAccount.setUpdatedAmount(accountModel.getUpdatedAmount());
            existingAccount.setCreateAt(accountModel.getCreateAt());
            existingAccount.setStatus(accountModel.getStatus());
            return new ResponseEntity<>(accountRepository.save(existingAccount), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<AccountModel> updateAccountStatus(@PathVariable("id") int id, @RequestBody Map<String, String> statusUpdate) {
        try {
            Optional<AccountModel> accountOptional = accountRepository.findById(id);
            if (accountOptional.isPresent()) {
                AccountModel account = accountOptional.get();
                String newStatus = statusUpdate.get("status");
                account.setStatus("ACTIVE".equalsIgnoreCase(newStatus) ? Status.Active : Status.Inactive);
                accountRepository.save(account);
                return new ResponseEntity<>(account, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    

    @GetMapping("/")
    public ResponseEntity<List<AccountModel>> getAllAccounts() {
        try {
            List<AccountModel> accounts = accountRepository.findAll();
            if (accounts.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(accounts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountModel> getAccountById(@PathVariable("id") int id) {
        try {
            Optional<AccountModel> account = accountRepository.findById(id);
            if (account.isPresent()) {
                return new ResponseEntity<>(account.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{uid}")
    public ResponseEntity<List<AccountModel>> getAccountByUid(@PathVariable("uid") int uid) {
        List<AccountModel> accountData = accountRepository.findByUserUid(uid);
        if (!accountData.isEmpty()) {
            return new ResponseEntity<>(accountData, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}