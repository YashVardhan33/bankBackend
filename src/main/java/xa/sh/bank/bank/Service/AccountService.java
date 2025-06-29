package xa.sh.bank.bank.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xa.sh.bank.bank.Entity.Account;
import xa.sh.bank.bank.Entity.Customer;
import xa.sh.bank.bank.Entity.Transaction;
import xa.sh.bank.bank.Entity.TransactionMethod;
import xa.sh.bank.bank.Repository.AccountRepo;
import xa.sh.bank.bank.Repository.CustomerRepo;
import xa.sh.bank.bank.Repository.TransactionRepo;
import xa.sh.bank.bank.dto.AccountDto;
import xa.sh.bank.bank.security.SecurityUtils;

@Service
public class AccountService {

    @Autowired
    private AccountRepo accountRepo;
    @Autowired
    private CustomerRepo customerRepo;
    @Autowired
    private TransactionRepo transRepo;
    public SecurityUtils secUtil;

    private Long generateUniqueAccountNumber() {
        Random random = new Random();
        Long accNo;
        do {
            accNo = 100000000L + (long) (Math.random() * 900000000L);
        } while (accountRepo.findByAccountNumber(accNo).isPresent());
        return accNo;
    }

    @Transactional
    public Account createAccount(AccountDto accDto) throws Exception {
        Optional<Customer> customerOpt = customerRepo.findById(accDto.getCustomerId());
        if (customerOpt.isEmpty()) {
            throw new Exception("Customer not found.");
        }
        Customer customer = customerOpt.get();

        if (accDto.getInitialBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new Exception("Initial balance cannot be negative.");
        }

        List<Account> existingAccounts = accountRepo.findByCustomer(customer);
        for (Account acc : existingAccounts) {
            if (acc.getType() == accDto.getType()) {
                throw new Exception("Customer already has an account of type: " + accDto.getType());
            }
        }

        Long accNo = generateUniqueAccountNumber();
        Account account = new Account();
        account.setAccountNumber(accNo);
        account.setType(accDto.getType());
        account.setIfsc(accDto.getIfsc());
        account.setBalance(accDto.getInitialBalance());
        account.setCustomer(customer);
        account.setCreatedAt(LocalDateTime.now());
        account.setLastModifiedAt(LocalDateTime.now());
        account.setFrozen(true);

        Transaction transaction = new Transaction();
        transaction.setSenderAcc(accDto.getPaymentSender());
        if (accDto.getTransactionDate() == null) {
            transaction.setTransactionDate(LocalDateTime.now());
        } else {
            transaction.setTransactionDate(accDto.getTransactionDate());
        }
        transaction.setAmount(accDto.getInitialBalance());
        transaction.setReceiverAcc(accNo);
        transaction.setSuccessful(true);
        transaction.setMethod(TransactionMethod.CASH);
        transaction.setDescription("Account opened with amount " + accDto.getInitialBalance());
        Transaction transaction1 = transRepo.save(transaction);
        // List<Transaction> receivedTransaction = new ArrayList<>();
        // receivedTransaction.add(transaction1);
        account.setReceivedTransactions(List.of(transaction1));
        account.setSentTransactions(new ArrayList<>());
        Account saved = accountRepo.save(account);
        if (customer.getAccountNumbers() == null) {
            customer.setAccountNumbers(new ArrayList<>());
        }
        customer.getAccountNumbers().add(accNo);
        if (customer.getPrimaryAccountNo() == null) {
            customer.setPrimaryAccountNo(accNo);

        }
        customer.setLastModifiedAt(LocalDateTime.now());
        customerRepo.save(customer);
        return saved;
    }

    public void unFreezeAccount(Long accountNumber) throws Exception {
        System.out.println(SecurityUtils.getAuthenticatedUserDetails().toString());
        System.out.println(SecurityUtils.getAuthenticatedUserId());
        Account acc = accountRepo.findByAccountNumber(accountNumber).orElseThrow(() -> new Exception("Account not found"));
        acc.setFrozen(false);
        acc.setLastModifiedAt(LocalDateTime.now());
        accountRepo.save(acc);
    }

    public void freezeAccount(Long accountNumber, String freezeReason) throws Exception {
        Account acc = accountRepo.findByAccountNumber(accountNumber).orElseThrow(() -> new Exception("Account not found"));
        acc.setFrozen(true);
        acc.setLastModifiedAt(LocalDateTime.now());
        acc.setFreezeReason(freezeReason);
        accountRepo.save(acc);
    }

    public List<Account> getAllAccountsOfCustomer(String customerId) throws Exception {
        Customer customer = customerRepo.findById(customerId).orElseThrow(() -> new Exception("Account not found"));
        return accountRepo.findByCustomer(customer);
    }

    @Cacheable(value = "accounts", key = "#accountNumber")
    public Account getAccountByNumber(Long accountNumber) {
        return accountRepo.findByAccountNumber(accountNumber).orElseThrow();
    }

}
