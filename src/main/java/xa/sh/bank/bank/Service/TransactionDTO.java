package xa.sh.bank.bank.Service;

import java.math.BigDecimal;

import xa.sh.bank.bank.Entity.TransactionMethod;

public class TransactionDTO {
    private Long senderAcc;
    private Long receiverAcc;
    private BigDecimal amount;
    private TransactionMethod method;
    private String description;
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
    
}
