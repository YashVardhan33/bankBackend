package xa.sh.bank.bank.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xa.sh.bank.bank.Entity.Account;
import xa.sh.bank.bank.Entity.AccountType;
import xa.sh.bank.bank.Entity.Transaction;
import xa.sh.bank.bank.Entity.TransactionMethod;
import xa.sh.bank.bank.Repository.AccountRepo;
import xa.sh.bank.bank.Repository.TransactionRepo;

@Service

public class InterestDepositingService {
    @Autowired private InterestRateService interestRateService;
    @Autowired private AccountRepo accountRepo;
    @Autowired private TransactionRepo tranRepo;

    @Transactional
    @Scheduled(cron = "0 0 0 1 1,4,7,10 *")
    public void applyQuarterlyInterestToSavingsAccounts(){
        BigDecimal annualRate = interestRateService.getAnnualRate(AccountType.SAVINGS).divide(BigDecimal.valueOf(100.00));
        BigDecimal quarterlyRate = annualRate.divide(BigDecimal.valueOf(4),6,RoundingMode.HALF_UP);
        System.out.println("method running");
        Pageable pageable = PageRequest.of(0, 100);
        Page<Account> page;

        do {
            page = accountRepo.findByType(AccountType.SAVINGS,pageable);
            for(Account acc : page.getContent()){
                if (acc.isFrozen()) {
                    continue;
                }
                BigDecimal interest = acc.getBalance().multiply(quarterlyRate).setScale(2,RoundingMode.HALF_UP);
                if(interest.compareTo(BigDecimal.ZERO)<=0) continue;
                acc.setBalance(acc.getBalance().add(interest));
                acc.setLastModifiedAt(LocalDateTime.now());

                Transaction interestTxn = new Transaction();
                interestTxn.setSenderAcc(000_000_000L);
                interestTxn.setReceiverAcc(acc.getAccountNumber());
                interestTxn.setAmount(interest);
                interestTxn.setTransactionDate(LocalDateTime.now());
                interestTxn.setMethod(TransactionMethod.INTERNAL);
                interestTxn.setDescription("Quarterly interest credited");
                interestTxn.setSuccessful(true);

                Transaction savedTxn = tranRepo.save(interestTxn);

                if (acc.getReceivedTransactions()==null) {
                    acc.setReceivedTransactions(new ArrayList<>());
                }
                acc.getReceivedTransactions().add(savedTxn);
                accountRepo.save(acc);

            }
            pageable = page.hasNext()?page.nextPageable():null;

        } while (pageable!=null);
    }


    @Transactional
    @Scheduled(cron = "0 0 0 1 1,4,7,10 *")
    public void applyQuarterlyInterestToZeroAccounts(){
        BigDecimal annualRate = interestRateService.getAnnualRate(AccountType.ZERO).divide(BigDecimal.valueOf(100.00));
        BigDecimal quarterlyRate = annualRate.divide(BigDecimal.valueOf(4),6,RoundingMode.HALF_UP);
        System.out.println("method running");
        Pageable pageable = PageRequest.of(0, 100);
        Page<Account> page;

        do {
            page = accountRepo.findByType(AccountType.ZERO,pageable);
            for(Account acc : page.getContent()){
                if (acc.isFrozen()) {
                    continue;
                }
                BigDecimal interest = acc.getBalance().multiply(quarterlyRate).setScale(2,RoundingMode.HALF_UP);
                if(interest.compareTo(BigDecimal.ZERO)<=0) continue;
                acc.setBalance(acc.getBalance().add(interest));
                acc.setLastModifiedAt(LocalDateTime.now());

                Transaction interestTxn = new Transaction();
                interestTxn.setSenderAcc(000_000_000L);
                interestTxn.setReceiverAcc(acc.getAccountNumber());
                interestTxn.setAmount(interest);
                interestTxn.setTransactionDate(LocalDateTime.now());
                interestTxn.setMethod(TransactionMethod.INTERNAL);
                interestTxn.setDescription("Quarterly interest credited");
                interestTxn.setSuccessful(true);

                Transaction savedTxn = tranRepo.save(interestTxn);

                if (acc.getReceivedTransactions()==null) {
                    acc.setReceivedTransactions(new ArrayList<>());
                }
                acc.getReceivedTransactions().add(savedTxn);
                accountRepo.save(acc);

            }
            pageable = page.hasNext()?page.nextPageable():null;

        } while (pageable!=null);
    }
}
