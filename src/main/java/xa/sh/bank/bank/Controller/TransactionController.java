package xa.sh.bank.bank.Controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import xa.sh.bank.bank.Entity.Transaction;
import xa.sh.bank.bank.Entity.TransactionMethod;
import xa.sh.bank.bank.Service.TransactionDTO;
import xa.sh.bank.bank.Service.TransactionService;
import xa.sh.bank.bank.dto.MessageResponse;

@RestController
@RequestMapping("/api/trans/")
public class TransactionController {

    @Autowired
    private TransactionService transService;

    @PostMapping("create")
    public ResponseEntity<?> createTransaction(@RequestBody TransactionDTO transaction) {
        return ResponseEntity.ok(transService.createTransaction(transaction));
    }

    @PostMapping("getOTP")
    public ResponseEntity<?> getOTP(@RequestBody TransactionDTO transaction) {
        try {
            transService.initiateTransactionWithOtp(transaction);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Something wrong : "+e.getMessage()));
        }
        return ResponseEntity.ok(new MessageResponse("OTP sent"));
    }

    @PostMapping("confirmTran")
    public ResponseEntity<?> createTransaction(@RequestBody TransactionDTO transaction, @RequestParam String otp ) {
        return ResponseEntity.ok(transService.confirmTransactionWithOtp(transaction, otp));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTransactionById(@PathVariable String id) {
        try {
            Transaction transaction = transService.findbyId(id);
            return ResponseEntity.ok(transaction);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Transaction Not found: " + e.getMessage()));

        }
    }

    @GetMapping("/all/{acc}")
    public ResponseEntity<Page<Transaction>> getAllTransactions(
            @PathVariable Long acc,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(transService.findAllTransactionsOfAcc(acc, page, size));
    }

    @GetMapping("/sent/{acc}")
    public ResponseEntity<Page<Transaction>> getAllSentByAcc(
            @PathVariable Long acc,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(transService.findAllSentByAcc(acc, page, size));
    }

    @GetMapping("/rec/{acc}")
    public ResponseEntity<Page<Transaction>> getAllRecAcc(
            @PathVariable Long acc,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(transService.findAllReceivedByAcc(acc, page, size));
    }

    @GetMapping("/sent/success/{acc}")
    public ResponseEntity<Page<Transaction>> getAllSuccessSent(
            @PathVariable Long acc,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(transService.findAllSuccessfulSent(acc, page, size));
    }

    @GetMapping("/failed/{acc}/sent")
    public ResponseEntity<Page<Transaction>> getAllFailedSent(
            @PathVariable Long acc,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(transService.findAllFailedSent(acc, page, size));
    }

    @GetMapping("/rec/{acc}/between")
    public ResponseEntity<Page<Transaction>> getReceivedBwDates(
            @PathVariable Long acc,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(transService.findReceivedBetweenDates(acc, from, to, page, size));
    }

    @GetMapping("/sent/{acc}/between")
    public ResponseEntity<Page<Transaction>> getSentBetweenDates(
            @PathVariable Long acc,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(transService.findReceivedBetweenDates(acc, from, to, page, size));
    }

    @GetMapping("/all/{acc}/between")
    public ResponseEntity<Page<Transaction>> findAllByAccountBetweenDates(
            @PathVariable Long acc,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(transService.findReceivedBetweenDates(acc, from, to, page, size));
    }

    @PreAuthorize("ADMIN")
    @GetMapping("/getAll")
    public ResponseEntity<Page<Transaction>> getAllBetweenDates(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(transService.findAllBetweenDates(from, to, page, size));
    }

    //@PreAuthorize("ADMIN")
    @GetMapping("/hvt/{acc}")
    public ResponseEntity<Page<Transaction>> getHighValueTransactions(
            @PathVariable Long acc,
            @RequestParam BigDecimal amount,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(transService.findHighValueTransactions(acc, amount, page, size));
    }

    @GetMapping("/failed/all")
    public ResponseEntity<Page<Transaction>> getAllFailed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(transService.findAllFailed(page, size));
    }

    @GetMapping("/failed/{acc}")
    public ResponseEntity<Page<Transaction>> getHighValueTransactions(
            @PathVariable Long acc,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(transService.findAllFailedByAcc(acc, page, size));
    }

    @GetMapping("/repeat")
    public ResponseEntity<?> getHighValueTransactions(
            @RequestParam Long sender,
            @RequestParam Long receiver,
            @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(transService.findRepeatTransaction(sender, receiver, amount));
    }

    @GetMapping("/byMethod")
    public ResponseEntity<Page<Transaction>> getByMethodDateRange(
            @RequestParam TransactionMethod method,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(transService.findByMethodAndDateRange(method, from, to, page, size));
    }

}
