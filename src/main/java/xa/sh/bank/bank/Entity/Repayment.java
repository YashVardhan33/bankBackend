package xa.sh.bank.bank.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "repayments")
public class Repayment {
    @Id
    private String id;

    @Indexed
    private Long loanAccountNumber;

    private BigDecimal amountPaid;
    private LocalDate repaymentDate;
    @Indexed
    private PaymentMethod method; // Enum (e.g., UPI, NEFT, Cash, etc.)
    private boolean successful;
    private String referenceNote;
    @CreatedDate
    private LocalDateTime createdAt;
    public String getId() {
        return id;
    }
    public Repayment() {
    }
    public void setId(String id) {
        this.id = id;
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
    public boolean isSuccessful() {
        return successful;
    }
    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }
    public String getReferenceNote() {
        return referenceNote;
    }
    public void setReferenceNote(String referenceNote) {
        this.referenceNote = referenceNote;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
}
