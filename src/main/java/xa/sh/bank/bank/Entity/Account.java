package xa.sh.bank.bank.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="accounts")
public class Account {
    @Id
    private String id;
    @Indexed(unique=true)
    private Long accountNumber;
    private AccountType type;
    private String ifsc;
    private BigDecimal balance;
    @DBRef
    @Indexed
    private Customer customer;
    @DBRef
    private List<Transaction> sentTransactions;
    @DBRef
    private List<Transaction> receivedTransactions;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime lastModifiedAt;
    private boolean frozen;
    private String freezeReason;
    @Version
    private Long version;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Long getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }
    public AccountType getType() {
        return type;
    }
    public void setType(AccountType type) {
        this.type = type;
    }
    
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public Customer getCustomer() {
        return customer;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    public List<Transaction> getSentTransactions() {
        return sentTransactions;
    }
    public void setSentTransactions(List<Transaction> sentTransactions) {
        this.sentTransactions = sentTransactions;
    }
    public List<Transaction> getReceivedTransactions() {
        return receivedTransactions;
    }
    public void setReceivedTransactions(List<Transaction> receivedTransactions) {
        this.receivedTransactions = receivedTransactions;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public Account (){

    }
    public Account(Long accountNumber, AccountType type, String iFSC, BigDecimal balance, Customer customer,
            LocalDateTime createdAt, boolean frozen) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.ifsc = iFSC;
        this.balance = balance;
        this.customer = customer;
        this.createdAt = LocalDateTime.now();
        this.frozen = frozen;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }
    public void setLastModifiedAt(LocalDateTime lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }
    public boolean isFrozen() {
        return frozen;
    }
    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }
    public Long getVersion() {
        return version;
    }
    public void setVersion(Long version) {
        this.version = version;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getFreezeReason() {
        return freezeReason;
    }

    public void setFreezeReason(String freezeReason) {
        this.freezeReason = freezeReason;
    }


    

}
