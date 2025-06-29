package xa.sh.bank.bank.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import xa.sh.bank.bank.Entity.PaymentMethod;

public class RepaymentDto {
    private Long loanAccountNumber;
    private BigDecimal amountPaid;
    private LocalDate repaymentDate;
    private PaymentMethod method;
    private String referenceNote;

    public RepaymentDto() {
    }

    public Long getLoanAccountNumber() {
        return loanAccountNumber;
    }

    public void setLoanAccountNumber(Long loanAccountNumber) {
        this.loanAccountNumber = loanAccountNumber;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }

    public LocalDate getRepaymentDate() {
        return repaymentDate;
    }

    public void setRepaymentDate(LocalDate repaymentDate) {
        this.repaymentDate = repaymentDate;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }

    public String getReferenceNote() {
        return referenceNote;
    }

    public void setReferenceNote(String referenceNote) {
        this.referenceNote = referenceNote;
    }

}
