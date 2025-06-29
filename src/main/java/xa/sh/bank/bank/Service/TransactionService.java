package xa.sh.bank.bank.Service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import xa.sh.bank.bank.Entity.Account;
import xa.sh.bank.bank.Entity.Customer;
import xa.sh.bank.bank.Entity.Transaction;
import xa.sh.bank.bank.Entity.TransactionMethod;
import xa.sh.bank.bank.Repository.AccountRepo;
import xa.sh.bank.bank.Repository.CustomerRepo;
import xa.sh.bank.bank.Repository.TransactionRepo;
import xa.sh.bank.bank.security.SecurityUtils;

@Service
public class TransactionService {

    @Autowired
    public TransactionRepo transactionRepo;

    public Page<Transaction> findAllTransactionsOfAcc(Long acc, int pageNo, int pageSize) {
        // build the Spring Data Pageable with sorting by transactionDate descending
        Pageable pageable = PageRequest.of(
                pageNo,
                pageSize,
                Sort.by(Sort.Direction.DESC, "transactionDate")
        );

        // call your repository method and return the Page<Transaction>
        return transactionRepo.findAllByAccount(acc, pageable);
    }

    public Page<Transaction> findAllSentByAcc(Long acc, int pageNo, int PageSize) {
        Pageable pageable = PageRequest.of(pageNo, PageSize, Sort.by("transactionDate").descending());

        return transactionRepo.findAllSentByAcc(acc, pageable);
    }

    public Page<Transaction> findAllReceivedByAcc(Long acc, int pageNo, int PageSize) {
        Pageable pageable = PageRequest.of(pageNo, PageSize, Sort.by("transactionDate").descending());

        return transactionRepo.findAllReceivedByAcc(acc, pageable);
    }

    public Page<Transaction> findAllSuccessfulSent(Long acc, int pageNo, int PageSize) {
        Pageable pageable = PageRequest.of(pageNo, PageSize, Sort.by("transactionDate").descending());

        return transactionRepo.findAllSuccessTransaction(acc, pageable);
    }

    public Page<Transaction> findAllFailedSent(Long acc, int pageNo, int PageSize) {
        Pageable pageable = PageRequest.of(pageNo, PageSize, Sort.by("transactionDate").descending());

        return transactionRepo.findAllFailedTransactions(acc, pageable);
    }

    public Transaction findbyId(String id) throws Exception {
        // 
        Optional<Transaction> trans = transactionRepo.findById(id);
        if (trans.isEmpty()) {
            throw new Exception("Transaction Not Find");
        }
        return trans.get();
    }

    public Page<Transaction> findReceivedBetweenDates(Long acc, LocalDateTime first, LocalDateTime last, int pageNo, int PageSize) {
        Pageable pageable = PageRequest.of(pageNo, PageSize, Sort.by("transactionDate").descending());

        return transactionRepo.findReceivedBetweenDate(acc, first, last, pageable);
    }

    public Page<Transaction> findSentBetweenDates(Long acc, LocalDateTime first, LocalDateTime last, int pageNo, int PageSize) {
        Pageable pageable = PageRequest.of(pageNo, PageSize, Sort.by("transactionDate").descending());

        return transactionRepo.findSentBetweenDate(acc, first, last, pageable);
    }

    public Page<Transaction> findAllByAccountBetweenDates(Long acc, LocalDateTime first, LocalDateTime last, int pageNo, int PageSize) {
        Pageable pageable = PageRequest.of(pageNo, PageSize, Sort.by("transactionDate").descending());

        return transactionRepo.findAllByAccountAndDateRange(acc, first, last, pageable);
    }

    /////////////////////////////////////
    /// For admin methods
    public Page<Transaction> findAllBetweenDates(LocalDateTime first, LocalDateTime last, int pageNo, int PageSize) {
        Pageable pageable = PageRequest.of(pageNo, PageSize, Sort.by("transactionDate").ascending());

        return transactionRepo.findAllBetweenDates(first, last, pageable);
    }

    public Page<Transaction> findHighValueTransactions(Long acc, BigDecimal threshold, int pageNo, int PageSize) {
        Pageable pageable = PageRequest.of(pageNo, PageSize, Sort.by("transactionDate").descending());

        return transactionRepo.findHighValueTransactions(acc, threshold, pageable);
    }

    public Page<Transaction> findAllFailed(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageSize, pageSize, Sort.by("transactionDate").descending());
        return transactionRepo.findAllFailed(pageable);
    }

    public Page<Transaction> findAllFailedByAcc(Long acc, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageSize, pageSize, Sort.by("transactionDate").descending());
        return transactionRepo.findFailedByAccount(acc, pageable);
    }

    public List<Transaction> findRepeatTransaction(Long sender, Long receiver, BigDecimal amount) {
        // Pageable pageable = PageRequest.of(pageSize, pageSize, Sort.by("transactionDate").descending());
        return transactionRepo.findSameAmountRepeatedTransactions(sender, receiver, amount);
    }

    public Page<Transaction> findByMethodAndDateRange(TransactionMethod method, LocalDateTime start, LocalDateTime end, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageSize, pageSize, Sort.by("transactionDate").descending());
        return transactionRepo.findByMethodAndDateRange(method, start, end, pageable);
    }

    //////////////////////////////////
    /// 
    /// 
    /// 
    /// 
    /// 
    @Autowired
    private AccountRepo accRepo;

    @Transactional
    public Transaction createTransaction(TransactionDTO transactionDto) {
        Transaction transaction = new Transaction();
        transaction.setSenderAcc(transactionDto.getSenderAcc());
        transaction.setReceiverAcc(transactionDto.getReceiverAcc());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setMethod(transactionDto.getMethod() != null ? transactionDto.getMethod() : TransactionMethod.CASH);
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setDescription(transactionDto.getDescription());
        try {
            // Validate amount
            if (transaction.getAmount() == null || transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new Exception("Amount must be greater than zero.");
            }

            // Validate accounts
            Account sender = accRepo.findByAccountNumber(transaction.getSenderAcc())
                    .orElseThrow(() -> new Exception("Sender account not found"));
            Account receiver = accRepo.findByAccountNumber(transaction.getReceiverAcc())
                    .orElseThrow(() -> new Exception("Receiver account not found"));

            // Validate frozen
            if (sender.isFrozen()) {
                throw new Exception("Sender account is frozen.");
            }
            if (receiver.isFrozen()) {
                throw new Exception("Receiver account is frozen.");
            }

            // Validate balance
            if (transaction.getAmount().compareTo(sender.getBalance()) > 0) {
                throw new Exception("Insufficient balance.");
            }

            // Perform transaction
            sender.setBalance(sender.getBalance().subtract(transaction.getAmount()));
            receiver.setBalance(receiver.getBalance().add(transaction.getAmount()));

            // Persist transaction
            transaction.setSuccessful(true);
            Transaction saved = transactionRepo.save(transaction);

            if (sender.getSentTransactions() == null) {
                sender.setSentTransactions(new ArrayList<>());
            }
            if (receiver.getReceivedTransactions() == null) {
                receiver.setReceivedTransactions(new ArrayList<>());
            }

            sender.getSentTransactions().add(saved);
            receiver.getReceivedTransactions().add(saved);
            accRepo.save(sender);
            accRepo.save(receiver);

            return saved;

        } catch (Exception ex) {
            transaction.setSuccessful(false);
            transaction.setDescription("FAILED: " + ex.getMessage());
            Account sender = accRepo.findByAccountNumber(transaction.getSenderAcc())
                    .orElse(null);
            Transaction saved = transaction;
            if (sender != null) {
                transaction.setSenderAcc(sender.getAccountNumber());
                saved = transactionRepo.save(transaction);
                if (sender.getSentTransactions() == null) {
                    sender.setSentTransactions(new ArrayList<>());
                }
                sender.getSentTransactions().add(saved);
                accRepo.save(sender);
                return saved;

            }

            return transactionRepo.save(transaction);
        }
    }



    @Autowired private CustomerRepo customRepo;
    @Autowired private StringRedisTemplate redis;
    @Autowired private EmailSender emailSender;
    public void initiateTransactionWithOtp(TransactionDTO dto) throws Exception{
        String customerId = SecurityUtils.getAuthenticatedUserId();
        if(customerId==null){
            throw new Exception("No jwt found or jwt expired");
        }
        Customer customer = customRepo.findById(customerId).orElseThrow(()->new Exception("Customer not found"));
        if(!customer.getAccountNumbers().contains(dto.getSenderAcc())){
            throw  new SecurityException("You are not authorised.");
        }

        String otp = String.format("%06d",new SecureRandom().nextInt(1000000));

        String txnHash = generateTransactionHash(dto);
        redis.opsForValue().set("otp: "+dto.getSenderAcc()+txnHash, otp, java.time.Duration.ofMinutes(10));

        String body = "Your transaction OTP is : "+otp+"\nDo not share it with anyone.";
        emailSender.send(customer.getEmail(), "Transaction OTP", body) ;
    }

    public Transaction confirmTransactionWithOtp(TransactionDTO dto, String otp){
        String customerId = SecurityUtils.getAuthenticatedUserId();
        Customer customer = customRepo.findById(customerId).orElseThrow(()->new RuntimeException("Customer not found"));
        if(!customer.getAccountNumbers().contains(dto.getSenderAcc())){
                throw new SecurityException("You are not authorised.");

        }
        String txnHash = generateTransactionHash(dto);
        String expectedOtp = redis.opsForValue().get("otp: "+dto.getSenderAcc()+txnHash);
        if(!otp.equals(expectedOtp)){
            throw new IllegalArgumentException("Invalid or Expired OTP.");

        }
        redis.delete("otp: "+dto.getSenderAcc());
        return createTransaction(dto);
    }

    public String generateTransactionHash(TransactionDTO dto) {
    String raw = dto.getSenderAcc() + "|" + dto.getReceiverAcc() + "|" +
                 dto.getAmount().toPlainString() + "|" + dto.getMethod();
    return DigestUtils.md5DigestAsHex(raw.getBytes(StandardCharsets.UTF_8));
}

}
