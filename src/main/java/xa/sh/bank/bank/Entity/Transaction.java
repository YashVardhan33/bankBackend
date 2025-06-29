package xa.sh.bank.bank.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="transactions")
public class Transaction {
    @Id
    private String id;
    @Indexed
    private Long senderAcc;    
    @Indexed
    private Long receiverAcc;
    private BigDecimal amount;
    @CreatedDate
    @Indexed
    private LocalDateTime transactionDate;
    @Indexed
    private TransactionMethod method;    
    private String description;
    private boolean isSuccessful;
    public Long getSenderAcc() {
        return senderAcc;
    }
    public void setSenderAcc(Long senderAcc) {
        this.senderAcc = senderAcc;
    }
    public Long getReceiverAcc() {
        return receiverAcc;
    }
    public void setReceiverAcc(Long receiverAcc) {
        this.receiverAcc = receiverAcc;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    public TransactionMethod getMethod() {
        return method;
    }
    public void setMethod(TransactionMethod method) {
        this.method = method;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public boolean isSuccessful() {
        return isSuccessful;
    }
    public void setSuccessful(boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public Transaction() {
    }
    public Transaction(Long senderAcc, Long receiverAcc, BigDecimal amount, TransactionMethod method,
            String description, boolean isSuccessful) {
        this.senderAcc = senderAcc;
        this.receiverAcc = receiverAcc;
        this.amount = amount;
        this.method = method;
        this.description = description;
        this.isSuccessful = isSuccessful;
    }
    
}
