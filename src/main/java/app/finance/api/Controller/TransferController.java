package app.finance.api.Controller;

import app.finance.api.Model.AccountModel;
import app.finance.api.Model.TransferModel;
import app.finance.api.Repository.IAccountRepository;
import app.finance.api.Repository.ITransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

// Transfer for the transfer controller
@RestController
@RequestMapping("/Transfers") // API route
@CrossOrigin(origins = "*") // Allow requests from any origin
public class TransferController {

    // dependency injection for the repositories
    @Autowired
    private ITransferRepository transferRepository;

    // dependency injection for the repositories
    @Autowired
    private IAccountRepository accountRepository;

    // Create a new transfer
    @PostMapping
    @Transactional
    public ResponseEntity<TransferModel> createTransfer(@RequestBody TransferModel transfer) {
        // Set the creation date
        transfer.setCreateAt(new Date());
        //  get the from account
        AccountModel fromAccount = accountRepository.findById(transfer.getFromAccount().getId()).orElseThrow(() -> new RuntimeException("From account not found"));
        // get the to account
        AccountModel toAccount = accountRepository.findById(transfer.getToAccount().getId()).orElseThrow(() -> new RuntimeException("To account not found"));

        // update the account balance
        fromAccount.setUpdatedAmount(fromAccount.getUpdatedAmount() - transfer.getValue());
        toAccount.setUpdatedAmount(toAccount.getUpdatedAmount() + transfer.getValue());

        // save the accounts with new balance
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        // save the transfer
        TransferModel savedTransfer = transferRepository.save(transfer);

        return ResponseEntity.ok(savedTransfer);
    }

    // Update a transfer
    @PutMapping("/{id}")
    public ResponseEntity<TransferModel> updateTransfer(@PathVariable int id, @RequestBody TransferModel transferDetails) {
        // get the original transfer
        Optional<TransferModel> originalTransfer = transferRepository.findById(id);

        //  if the original transfer is not found
        if (!originalTransfer.isPresent()) {
            return ResponseEntity.notFound().build(); // return not found
        }
        // get the from account
        AccountModel accountFrom = accountRepository.findById(originalTransfer.get().getFromAccount().getId()).orElseThrow(() -> new RuntimeException("Account From not found"));
        // get the to account
        AccountModel accountTo = accountRepository.findById(originalTransfer.get().getToAccount().getId()).orElseThrow(() -> new RuntimeException("Account To not found"));

        // update the account balance
        accountFrom.setUpdatedAmount(accountFrom.getUpdatedAmount() + originalTransfer.get().getValue());
        accountTo.setUpdatedAmount(accountTo.getUpdatedAmount() - originalTransfer.get().getValue());
        // save the accounts with new balance
        accountFrom.setUpdatedAmount(accountFrom.getUpdatedAmount() - transferDetails.getValue());
        accountTo.setUpdatedAmount(accountTo.getUpdatedAmount() + transferDetails.getValue());
        // save the accounts with new balance
        accountRepository.save(accountFrom);
        accountRepository.save(accountTo);

        // if the original transfer is found
        if (originalTransfer.isPresent()) {
            // get the original transfer
            TransferModel transfer = originalTransfer.get();
            // set the transfer details
            transfer.setFromAccount(transferDetails.getFromAccount());
            transfer.setToAccount(transferDetails.getToAccount());
            transfer.setValue(transferDetails.getValue());
            transfer.setNotes(transferDetails.getNotes());
            transfer.setDate(transferDetails.getDate());
            transfer.setCreateAt(new Date());
            // save the updated transfer
            TransferModel updatedTransfer = transferRepository.save(transfer);
            return ResponseEntity.ok(updatedTransfer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a transfer
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransfer(@PathVariable int id) {
        // get the transfer by id
        Optional<TransferModel> transfer = transferRepository.findById(id);
        
        if (!transfer.isPresent()) { // if the transfer is not found
            return ResponseEntity.notFound().build(); // return not found
        }
        // get the from account
        AccountModel accountFrom = accountRepository.findById(transfer.get().getFromAccount().getId()).orElseThrow(() -> new RuntimeException("Account not found"));
        // get the to account
        AccountModel accountTo = accountRepository.findById(transfer.get().getToAccount().getId()).orElseThrow(() -> new RuntimeException("Account not found"));
        // update the account balance
        accountFrom.setUpdatedAmount(accountFrom.getUpdatedAmount() + transfer.get().getValue());
        accountTo.setUpdatedAmount(accountTo.getUpdatedAmount() - transfer.get().getValue());
        // save the accounts with new balance
        if (transfer.isPresent()) {
            transferRepository.delete(transfer.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Get all transfers
    @GetMapping
    public List<TransferModel> getAllTransfers() {
        // get all transfers
        return transferRepository.findAll();
    }

    // Get a transfer by ID
    @GetMapping("/{id}")
    public ResponseEntity<TransferModel> getTransferById(@PathVariable int id) {
        // get the transfer by id
        Optional<TransferModel> transfer = transferRepository.findById(id);
        return transfer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get transfers by user UID
    @GetMapping("/user/{uid}")
    public ResponseEntity<List<TransferModel>> getTransfersByUserUid(@PathVariable int uid) {
        // get the transfers by user UID
        List<TransferModel> transfers = transferRepository.findByFromAccountUserUid(uid);
        if (transfers.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(transfers);
        }
    }
}