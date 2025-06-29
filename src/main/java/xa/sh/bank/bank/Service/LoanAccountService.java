package xa.sh.bank.bank.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xa.sh.bank.bank.Entity.Account;
import xa.sh.bank.bank.Entity.Customer;
import xa.sh.bank.bank.Entity.DisbursementRecord;
import xa.sh.bank.bank.Entity.LoanAccount;
import xa.sh.bank.bank.Entity.LoanInterestRateConfig;
import xa.sh.bank.bank.Entity.LoanStatus;
import xa.sh.bank.bank.Entity.RepaymentSchedule;
import xa.sh.bank.bank.Entity.Transaction;
import xa.sh.bank.bank.Entity.TransactionMethod;
import xa.sh.bank.bank.Repository.AccountRepo;
import xa.sh.bank.bank.Repository.CustomerRepo;
import xa.sh.bank.bank.Repository.DisbursementRecordRepo;
import xa.sh.bank.bank.Repository.LoanAccountRepo;
import xa.sh.bank.bank.Repository.LoanRateRepo;
import xa.sh.bank.bank.Repository.RepaymentScheduleRepo;
import xa.sh.bank.bank.Repository.TransactionRepo;
import xa.sh.bank.bank.dto.LoanAccountDto;

@Service
public class LoanAccountService {
    @Autowired private LoanAccountRepo loanAccountRepo;
    @Autowired private CustomerRepo customerRepo;
    @Autowired private LoanRateRepo loanRateRepo;
    @Autowired private RepaymentScheduleRepo scheduleRepo;
    @Autowired private AccountRepo accountRepo;
    @Autowired private TransactionRepo transactionRepo;
    @Autowired private DisbursementRecordRepo disRecRepo;

    public LoanAccount createLoanAccount(LoanAccountDto dto) throws Exception {
        Customer customer = customerRepo.findById(dto.getCustomerId())
            .orElseThrow(() -> new Exception("Customer not found"));

        BigDecimal annualRate = loanRateRepo.findByType(dto.getType())
            .map(LoanInterestRateConfig::getAnnualRate)
            .orElse(BigDecimal.valueOf(6.5));

        LoanAccount account = new LoanAccount();
        account.setAccountNumber(generateUniqueLoanAccNo());
        account.setAccountName(dto.getAccountName());
        account.setType(dto.getType());
        account.setLoanAmount(dto.getLoanAmount());
        account.setInterestRate(annualRate);
        account.setTenureInMonths(dto.getTenure());
        account.setStartDate(LocalDate.now());
        account.setDueDate(LocalDate.now().plusMonths(dto.getTenure()));
        account.setBorrower(customer);
        account.setBalanceRemaining(BigDecimal.ZERO);
        account.setDisbursedAmount(BigDecimal.ZERO);
        account.setStatus(LoanStatus.ACTIVE);
        account.setCreatedAt(LocalDateTime.now());
        account.setLastModifiedAt(LocalDateTime.now());

        BigDecimal emi = calculateEMI(dto.getLoanAmount(), annualRate, dto.getTenure());
        account.setEmiAmount(emi);

        LoanAccount saved = loanAccountRepo.save(account);

        // Generate initial repayment schedule
        for (int i = 1; i <= dto.getTenure(); i++) {
            RepaymentSchedule sched = new RepaymentSchedule();
            sched.setAccountNo(saved.getAccountNumber());
            sched.setDueDate(LocalDate.now().plusMonths(i));
            sched.setExpectedAmount(emi);
            sched.setPaid(false);
            scheduleRepo.save(sched);
        }

        return saved;
    }

    public LoanAccount getLoanByAccountNo(Long accountNo) throws Exception {
        return loanAccountRepo.findByAccountNumber(accountNo)
                .orElseThrow(() -> new Exception("Loan account not found"));
    }

    private Long generateUniqueLoanAccNo() {
        Long accNo;
        do {
            accNo = 70000000000L + new Random().nextLong(2999999999L);
        } while (loanAccountRepo.findByAccountNumber(accNo).isPresent());
        return accNo;
    }

    private BigDecimal calculateEMI(BigDecimal principal, BigDecimal annualRate, int months) {
        BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(12 * 100), 10, RoundingMode.HALF_UP);
        BigDecimal onePlusRPowerN = BigDecimal.ONE.add(monthlyRate).pow(months);
        return principal.multiply(monthlyRate).multiply(onePlusRPowerN)
                .divide(onePlusRPowerN.subtract(BigDecimal.ONE), 2, RoundingMode.HALF_UP);
    }




    @Transactional
    public DisbursementRecord createDisbursement(Long loanAccountNo ,Long receiverAccNo ,BigDecimal amount, String reference  ) throws Exception{
            LoanAccount loan = loanAccountRepo.findByAccountNumber(loanAccountNo).orElseThrow(()->new Exception("Loan account not found."));
            if (loan.getLoanAmount().subtract(loan.getDisbursedAmount()).compareTo(amount) < 0) {
                    throw new IllegalArgumentException("Insufficient undisbursed balance.");
                }
            if (loan.getHolidayUntil().isAfter(LocalDate.now())) {
                throw new Exception("Cant disburse after Holiday Period");
            }    
            Optional<Account> receiverAccOp = accountRepo.findByAccountNumber(receiverAccNo);
            if(receiverAccOp.isEmpty()){throw new Exception("receiver account not found");}
            Account receiverAcc = receiverAccOp.get();
            if (receiverAcc.isFrozen()) {
                throw new Exception("Receiver Account is frozen");
            }

            Transaction txn = new Transaction();
            txn.setAmount(amount);
            txn.setDescription(reference);
            txn.setReceiverAcc(receiverAccNo);
            txn.setSenderAcc(loanAccountNo);
            txn.setMethod(TransactionMethod.INTERNAL);
            txn.setTransactionDate(LocalDateTime.now());
            Transaction savedTxn = transactionRepo.save(txn);
            
            receiverAcc.setBalance(receiverAcc.getBalance().add(amount));
            if (receiverAcc.getReceivedTransactions() == null) {
                    receiverAcc.setReceivedTransactions(new ArrayList<>());
                }
            receiverAcc.getReceivedTransactions().add(savedTxn);
            accountRepo.save(receiverAcc);

            loan.setDisbursedAmount(loan.getDisbursedAmount().add(amount));
            loan.setBalanceRemaining(loan.getBalanceRemaining().add(amount));
            loan.setLastModifiedAt(LocalDateTime.now());

            DisbursementRecord record = new DisbursementRecord();
            record.setAccount(loan);
            record.setAmount(amount);
            record.setDisbursementDate(LocalDateTime.now());
            record.setReceiverAccount(receiverAcc);
            record.setReference(reference);
            DisbursementRecord savedRecord= disRecRepo.save(record);
            if (loan.getDisbursements()==null) {
                loan.setDisbursements(new ArrayList<>());
            }
            loan.getDisbursements().add(savedRecord);

            BigDecimal emi = calculateEMI(loan.getDisbursedAmount(), loan.getInterestRate(), loan.getTenureInMonths());

            loan.setEmiAmount(emi);


            var unpaidSchedules = scheduleRepo.findByAccountNoAndPaidFalse(loan.getAccountNumber());
            for (RepaymentSchedule sched : unpaidSchedules) {
                sched.setExpectedAmount(emi);
                scheduleRepo.save(sched);
            }


            loanAccountRepo.save(loan);
            
            return savedRecord;


    }
}

