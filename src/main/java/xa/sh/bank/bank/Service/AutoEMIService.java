package xa.sh.bank.bank.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xa.sh.bank.bank.Entity.Account;
import xa.sh.bank.bank.Entity.LoanAccount;
import xa.sh.bank.bank.Entity.LoanStatus;
import xa.sh.bank.bank.Entity.PaymentMethod;
import xa.sh.bank.bank.Entity.Repayment;
import xa.sh.bank.bank.Entity.RepaymentSchedule;
import xa.sh.bank.bank.Entity.Transaction;
import xa.sh.bank.bank.Entity.TransactionMethod;
import xa.sh.bank.bank.Repository.AccountRepo;
import xa.sh.bank.bank.Repository.LoanAccountRepo;
import xa.sh.bank.bank.Repository.RepaymentRepo;
import xa.sh.bank.bank.Repository.RepaymentScheduleRepo;
import xa.sh.bank.bank.Repository.TransactionRepo;

@Service
public class AutoEMIService {

    @Autowired
    private LoanAccountRepo loanAccountRepo;
    @Autowired
    private RepaymentScheduleRepo rScheduleRepo;
    @Autowired
    private EmailSender emailSender;

    @Autowired private AccountRepo accRepo;
    @Autowired private RepaymentRepo repaymentRepo;
    @Autowired private TransactionRepo transactionRepo;

    @Scheduled(cron = "0 0 2 1,5,10,20 * ?")
    @Transactional
    public void generateMonthlyEMIs() {
        LocalDate today = LocalDate.now();
        int day = today.getDayOfMonth();

        List<LoanAccount> loanDueToday = loanAccountRepo.findByPreferredEmiDayAndStatus(today, LoanStatus.ACTIVE);
        for (LoanAccount loan : loanDueToday) {
            if (loan.getHolidayUntil() != null && today.isBefore(loan.getHolidayUntil())) {
                continue;
            }
            if (loan.getDisbursedAmount() == null || loan.getDisbursedAmount().compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }

            boolean alreadyExists = rScheduleRepo.existsByAccountNoAndDueDate(loan.getAccountNumber(), today);
            if (alreadyExists) {
                continue;
            }

            BigDecimal emi = calculateEMI(loan.getDisbursedAmount(), loan.getInterestRate(), loan.getTenureInMonths());
            RepaymentSchedule schedule = new RepaymentSchedule();
            if (loan.getEmiAmount() == null || loan.getEmiAmount().compareTo(BigDecimal.ZERO) <= 0) {
                loan.setEmiAmount(emi);
                loanAccountRepo.save(loan);
            }

            schedule.setAccountNo(loan.getAccountNumber());
            schedule.setDueDate(today);
            schedule.setExpectedAmount(emi);
            schedule.setPaid(false);
            rScheduleRepo.save(schedule);
        }

    }

    @Scheduled(cron = "0 30 8 * * ?") // Runs daily at 8:30 AM
    public void sendDueReminders() {
        LocalDate today = LocalDate.now();
        LocalDate dueSoon = today.plusDays(3); // Send reminders for EMIs due in the next 3 days

        List<RepaymentSchedule> dueSchedules = rScheduleRepo.findByDueDateBetweenAndPaidFalse(today, dueSoon);

        for (RepaymentSchedule schedule : dueSchedules) {
            Optional<LoanAccount> loanOpt = loanAccountRepo.findByAccountNumber(schedule.getAccountNo());
            if (loanOpt.isEmpty())
                continue;

            LoanAccount loan = loanOpt.get();
            if (loan.getHolidayUntil() != null && schedule.getDueDate().isBefore(loan.getHolidayUntil()))
                continue;
            if (loan.getBorrower() == null || loan.getBorrower().getEmail() == null)
                continue;

            String email = loan.getBorrower().getEmail();
            String content = String.format(
                    """
                            <p><strong>EMI Due Reminder</strong></p>
                            <p>This is a gentle reminder that your EMI of <strong>₹%s</strong> is due on <strong>%s</strong> for your loan account <strong>%s</strong>.</p>
                            <p>Please ensure timely payment to avoid late fees or penalties.</p>
                            """,
                    schedule.getExpectedAmount(),
                    schedule.getDueDate(),
                    loan.getAccountNumber());

            emailSender.send(email, "EMI Due Reminder", content);
        }
    }

    private BigDecimal calculateEMI(BigDecimal principal, BigDecimal annualRate, int months) {
        if (principal.compareTo(BigDecimal.ZERO) <= 0)
            return BigDecimal.ZERO;
        BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(12 * 100), 10, RoundingMode.HALF_UP);
        BigDecimal onePlusRPowerN = BigDecimal.ONE.add(monthlyRate).pow(months);
        return principal.multiply(monthlyRate).multiply(onePlusRPowerN)
                .divide(onePlusRPowerN.subtract(BigDecimal.ONE), 2, RoundingMode.HALF_UP);
    }



    @Scheduled(cron = "0 0 3 1,5,10,25 * ?") // Runs at 3 AM on 1st, 5th, 10th, 25th
@Transactional
public void autoDebitEMIs() {
    LocalDate today = LocalDate.now();

    // Find all unpaid schedules due today
    List<RepaymentSchedule> dueToday = rScheduleRepo.findByDueDateAndPaidFalse(today);

    for (RepaymentSchedule sched : dueToday) {
        Optional<LoanAccount> loanOpt = loanAccountRepo.findByAccountNumber(sched.getAccountNo());
        if (loanOpt.isEmpty()) continue;

        LoanAccount loan = loanOpt.get();

        // Only process if autoDebit is enabled
        if (!loan.isAutoDebit()) continue;

        // Skip if loan is under holiday/grace period
        if (loan.getHolidayUntil() != null && today.isBefore(loan.getHolidayUntil())) continue;

        // Get the borrower's linked primary account
        Account primaryAcc = loan.getPrimaryAccount();
        if (primaryAcc == null) continue;
        if (primaryAcc.isFrozen()) continue;

        BigDecimal emiAmount = sched.getExpectedAmount();

        if (primaryAcc.getBalance().compareTo(emiAmount) >= 0) {
            // Sufficient funds — perform debit
            primaryAcc.setBalance(primaryAcc.getBalance().subtract(emiAmount));

            // Record transaction
            Transaction txn = new Transaction();
            txn.setAmount(emiAmount);
            txn.setDescription("Auto-debit EMI for Loan Account: " + loan.getAccountNumber());
            txn.setSenderAcc(primaryAcc.getAccountNumber());
            txn.setReceiverAcc(loan.getAccountNumber());
            txn.setMethod(TransactionMethod.AUTO_DEBIT);
            txn.setTransactionDate(LocalDateTime.now());
            Transaction savedTxn = transactionRepo.save(txn);

            // Add to sent transactions
            if (primaryAcc.getSentTransactions() == null) {
                primaryAcc.setSentTransactions(new ArrayList<>());
            }
            primaryAcc.getSentTransactions().add(savedTxn);

            accRepo.save(primaryAcc);

            // Mark schedule paid
            sched.setPaid(true);
            rScheduleRepo.save(sched);

            // Update loan outstanding balance
            loan.setBalanceRemaining(
                loan.getBalanceRemaining().subtract(emiAmount).max(BigDecimal.ZERO)
            );
            loan.setLastModifiedAt(LocalDateTime.now());

            // Save repayment record
            Repayment repay = new Repayment();
            repay.setLoanAccountNumber(loan.getAccountNumber());
            repay.setAmountPaid(emiAmount);
            repay.setRepaymentDate(today);
            repay.setMethod(PaymentMethod.AUTO_DEBIT);
            repay.setSuccessful(true);
            repay.setReferenceNote("Auto-debit successful");

            repaymentRepo.save(repay);

            if (loan.getRepayments() == null) loan.setRepayments(new ArrayList<>());
            loan.getRepayments().add(repay);

            loanAccountRepo.save(loan);

        } else {
            // Insufficient funds — send failure email
            String email = loan.getBorrower() != null ? loan.getBorrower().getEmail() : null;
            if (email != null) {
                emailSender.send(
                    email,
                    "Auto-Debit Failed",
                    String.format(
                        "Auto-debit of ₹%s for your loan account %s failed due to insufficient balance in your linked account.",
                        emiAmount, loan.getAccountNumber()
                    )
                );
            }
        }
    }
}

}
