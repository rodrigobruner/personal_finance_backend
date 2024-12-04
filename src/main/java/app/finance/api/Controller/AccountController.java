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

// Account for the account controller
@RestController
@RequestMapping("/Accounts") // API route
@CrossOrigin(origins = "*") // Allow requests from any origin
public class AccountController {

    // dependency injection for the repositories
    @Autowired
    private IAccountRepository accountRepository;

    // dependency injection for the repositories
    @Autowired
    private IUserRepository userRepository;

    // dependency injection for the repositories
    @Autowired
    private IAccountTypeRepository accountTypeRepository;

    // Create a new account
    @PostMapping("/")
    public ResponseEntity<AccountModel> createAccount(@RequestBody AccountModel accountModel) {
        try {
            // define the creation date
            accountModel.setCreateAt(new Date());

            // define the status as active
            accountModel.setStatus(Status.Active);

            // convert user_id to UserModel
            Optional<UserModel> user = userRepository.findById(accountModel.getUser().getUid());
            if (user.isPresent()) {
                // set the user
                accountModel.setUser(user.get());
            } else {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            // Convert account_type_id to AccountTypeModel
            Optional<AccountTypeModel> accountType = accountTypeRepository.findById(accountModel.getAccountType().getId());
            if (accountType.isPresent()) {
                // set the account type
                accountModel.setAccountType(accountType.get());
            } else {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            // Save the account
            AccountModel savedAccount = accountRepository.save(accountModel);
            return new ResponseEntity<>(savedAccount, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // update an account
    @PutMapping("/{id}")
    public ResponseEntity<AccountModel> updateAccount(@PathVariable("id") int id, @RequestBody AccountModel accountModel) {
        // check if the account exists and get the account
        Optional<AccountModel> accountData = accountRepository.findById(id);

        // if the account exists
        if (accountData.isPresent()) {
            // get the existing account
            AccountModel existingAccount = accountData.get();

            // calculate the difference between the initial amount
            double difference = accountModel.getInitialAmount() - existingAccount.getInitialAmount();
            // update the updated amount
            existingAccount.setUpdatedAmount(existingAccount.getUpdatedAmount()+difference);

            // update the account
            existingAccount.setName(accountModel.getName());
            existingAccount.setAccountType(accountModel.getAccountType());
            existingAccount.setInitialAmount(accountModel.getInitialAmount());
            existingAccount.setCreateAt(existingAccount.getCreateAt());
            existingAccount.setStatus(accountModel.getStatus());
            return new ResponseEntity<>(accountRepository.save(existingAccount), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // update the status of an account
    @PutMapping("/{id}/status")
    public ResponseEntity<AccountModel> updateAccountStatus(@PathVariable("id") int id, @RequestBody Map<String, String> statusUpdate) {
        try {
            // get the account by id 
            Optional<AccountModel> accountOptional = accountRepository.findById(id);
            // if the account exists
            if (accountOptional.isPresent()) {
                // get the account
                AccountModel account = accountOptional.get();
                // get the new status
                String newStatus = statusUpdate.get("status");
                // update the status
                account.setStatus("ACTIVE".equalsIgnoreCase(newStatus) ? Status.Active : Status.Inactive);
                // save the account
                accountRepository.save(account);
                return new ResponseEntity<>(account, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // get all accounts
    @GetMapping("/")
    public ResponseEntity<List<AccountModel>> getAllAccounts() {
        try {
            // get all accounts
            List<AccountModel> accounts = accountRepository.findAll();
            if (accounts.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(accounts, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get account by id
    @GetMapping("/{id}")
    public ResponseEntity<AccountModel> getAccountById(@PathVariable("id") int id) {
        try {
            // get the account by id
            Optional<AccountModel> account = accountRepository.findById(id);
            // if the account exists
            if (account.isPresent()) {
                // return the account
                return new ResponseEntity<>(account.get(), HttpStatus.OK);
            } else {
                // return not found
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // get accounts by user id
    @GetMapping("/user/{uid}")
    public ResponseEntity<List<AccountModel>> getAccountByUid(@PathVariable("uid") int uid) {
        // get the accounts by user id
        List<AccountModel> accountData = accountRepository.findByUserUid(uid);
        if (!accountData.isEmpty()) { // if the accounts exist return them
            return new ResponseEntity<>(accountData, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // get active accounts by user id
    @GetMapping("/user/{uid}/active")
    public ResponseEntity<List<AccountModel>> getActiveAccountsByUid(@PathVariable("uid") int uid) {
        // get the active accounts by user id
        List<AccountModel> accountData = accountRepository.findByUserUidAndStatus(uid, Status.Active);
        if (!accountData.isEmpty()) { // if the accounts exist return them
            return new ResponseEntity<>(accountData, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}