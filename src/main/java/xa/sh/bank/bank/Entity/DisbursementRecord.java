package xa.sh.bank.bank.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "disbursementRecords")
public class DisbursementRecord {
    @Id
    private String id;
    @DBRef
    @Indexed
    private LoanAccount account;

    private LocalDateTime disbursementDate;

    private BigDecimal amount;
    @DBRef
    private Account receiverAccount;
    private String reference;




    public DisbursementRecord(){}
    /**
     * @return String return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return LoanAccount return the account
     */
    public LoanAccount getAccount() {
        return account;
    }

    /**
     * @param account the account to set
     */
    public void setAccount(LoanAccount account) {
        this.account = account;
    }

    /**
     * @return LocalDateTime return the disbursementDate
     */
    public LocalDateTime getDisbursementDate() {
        return disbursementDate;
    }

    /**
     * @param disbursementDate the disbursementDate to set
     */
    public void setDisbursementDate(LocalDateTime disbursementDate) {
        this.disbursementDate = disbursementDate;
    }

    /**
     * @return BigDecimal return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * @return Account return the receiverAccount
     */
    public Account getReceiverAccount() {
        return receiverAccount;
    }

    /**
     * @param receiverAccount the receiverAccount to set
     */
    public void setReceiverAccount(Account receiverAccount) {
        this.receiverAccount = receiverAccount;
    }

    /**
     * @return String return the reference
     */
    public String getReference() {
        return reference;
    }

    /**
     * @param reference the reference to set
     */
    public void setReference(String reference) {
        this.reference = reference;
    }

}
