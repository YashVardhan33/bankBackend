package xa.sh.bank.bank.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import xa.sh.bank.bank.Entity.AccountType;
import xa.sh.bank.bank.Entity.PaymentMethod;

public class AccountDto {
    AccountType type;
    String ifsc;
    BigDecimal initialBalance; 
    Long paymentSender;
    String transactionId;
    LocalDateTime transactionDate;
    PaymentMethod method;
    public Long getPaymentSender() {
        return paymentSender;
    }
    public void setPaymentSender(Long paymentSender) {
        this.paymentSender = paymentSender;
    }
    public String getTransactionId() {
        return transactionId;
    }
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    public PaymentMethod getMethod() {
        return method;
    }
    public void setMethod(PaymentMethod method) {
        this.method = method;
    }
    String customerId;
    public String getCustomerId() {
        return customerId;
    }
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    public AccountType getType() {
        return type;
    }
    public void setType(AccountType type) {
        this.type = type;
    }
    public AccountDto() {
    }
    public AccountDto(AccountType type, String ifsc, BigDecimal initialBalance, String customerId) {
        this.type = type;
        this.ifsc = ifsc;
        this.initialBalance = initialBalance;
        this.customerId = customerId;
    }
    public String getIfsc() {
        return ifsc;
    }
    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }
    public BigDecimal getInitialBalance() {
        return initialBalance;
    }
    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
    
}
