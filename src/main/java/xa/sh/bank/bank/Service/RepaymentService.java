package xa.sh.bank.bank.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xa.sh.bank.bank.Entity.LoanAccount;
import xa.sh.bank.bank.Entity.Repayment;
import xa.sh.bank.bank.Entity.RepaymentSchedule;
import xa.sh.bank.bank.Repository.LoanAccountRepo;
import xa.sh.bank.bank.Repository.RepaymentRepo;
import xa.sh.bank.bank.Repository.RepaymentScheduleRepo;
import xa.sh.bank.bank.dto.RepaymentDto;

@Service
public class RepaymentService {
    @Autowired private LoanAccountRepo loanRepo;
    @Autowired private RepaymentRepo repaymentRepo;
    @Autowired private RepaymentScheduleRepo scheduleRepo;

    @Transactional
    public Repayment repayLoan(RepaymentDto dto) throws Exception {
        LoanAccount account = loanRepo.findByAccountNumber(dto.getLoanAccountNumber())
            .orElseThrow(() -> new Exception("Loan Account not found"));

        Repayment repayment = new Repayment();
        repayment.setLoanAccountNumber(dto.getLoanAccountNumber());
        repayment.setAmountPaid(dto.getAmountPaid());
        repayment.setRepaymentDate(dto.getRepaymentDate());
        repayment.setMethod(dto.getMethod());
        repayment.setSuccessful(true);
        repayment.setReferenceNote(dto.getReferenceNote());

        repayment = repaymentRepo.save(repayment);

        // Update schedule
        RepaymentSchedule nextDue = scheduleRepo.findFirstByAccountNoAndPaidFalseOrderByDueDateAsc(account.getAccountNumber())
            .orElse(null);

        if (nextDue != null && dto.getAmountPaid().compareTo(nextDue.getExpectedAmount()) >= 0) {
            nextDue.setPaid(true);
            scheduleRepo.save(nextDue);
        }

        // Update loan account
        BigDecimal updatedBalance = account.getBalanceRemaining().subtract(dto.getAmountPaid());
        account.setBalanceRemaining(updatedBalance.max(BigDecimal.ZERO));
        account.setLastModifiedAt(LocalDateTime.now());

        List<Repayment> repayments = account.getRepayments();
        if (repayments == null) repayments = new ArrayList<>();
        repayments.add(repayment);
        account.setRepayments(repayments);
        loanRepo.save(account);

        return repayment;
    }
}
