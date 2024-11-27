package app.finance.api.Controller;

import app.finance.api.Model.AccountModel;
import app.finance.api.Model.TransferModel;
import app.finance.api.Repository.IAccountRepository;
import app.finance.api.Repository.ITransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

@RestController
@RequestMapping("/Transfers")
@CrossOrigin(origins = "*")
public class TransferController {

    @Autowired
    private ITransferRepository transferRepository;

    @Autowired
    private IAccountRepository accountRepository;

    // Create a new transfer
    @PostMapping
    @Transactional
    public ResponseEntity<TransferModel> createTransfer(@RequestBody TransferModel transfer) {
        transfer.setCreateAt(new Date());

        AccountModel fromAccount = accountRepository.findById(transfer.getFromAccount().getId()).orElseThrow(() -> new RuntimeException("From account not found"));
        AccountModel toAccount = accountRepository.findById(transfer.getToAccount().getId()).orElseThrow(() -> new RuntimeException("To account not found"));

        fromAccount.setUpdatedAmount(fromAccount.getUpdatedAmount() - transfer.getValue());
        toAccount.setUpdatedAmount(toAccount.getUpdatedAmount() + transfer.getValue());

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        TransferModel savedTransfer = transferRepository.save(transfer);

        return ResponseEntity.ok(savedTransfer);
    }

    // Get all transfers
    @GetMapping
    public List<TransferModel> getAllTransfers() {
        return transferRepository.findAll();
    }

    // Get a transfer by ID
    @GetMapping("/{id}")
    public ResponseEntity<TransferModel> getTransferById(@PathVariable int id) {
        Optional<TransferModel> transfer = transferRepository.findById(id);
        return transfer.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update a transfer
    @PutMapping("/{id}")
    public ResponseEntity<TransferModel> updateTransfer(@PathVariable int id, @RequestBody TransferModel transferDetails) {
        Optional<TransferModel> optionalTransfer = transferRepository.findById(id);
        if (optionalTransfer.isPresent()) {
            TransferModel transfer = optionalTransfer.get();
            transfer.setFromAccount(transferDetails.getFromAccount());
            transfer.setToAccount(transferDetails.getToAccount());
            transfer.setValue(transferDetails.getValue());
            transfer.setNotes(transferDetails.getNotes());
            transfer.setDate(transferDetails.getDate());
            transfer.setCreateAt(new Date());
            TransferModel updatedTransfer = transferRepository.save(transfer);
            return ResponseEntity.ok(updatedTransfer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete a transfer
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransfer(@PathVariable int id) {
        Optional<TransferModel> transfer = transferRepository.findById(id);
        if (transfer.isPresent()) {
            transferRepository.delete(transfer.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}