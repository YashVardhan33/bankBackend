package xa.sh.bank.bank.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="loan_accounts")
public class LoanAccount {
    @Id
    private String id;

    @Indexed(unique=true)
    private Long accountNumber;
    private String accountName;
    private LoanType type;
    private BigDecimal loanAmount;
    private BigDecimal interestRate;
    private int tenureInMonths;
    private LocalDate startDate;
    private LocalDate dueDate;
    private BigDecimal balanceRemaining;
    private BigDecimal emiAmount;

    @DBRef
    @Indexed
    private Customer borrower;

    private LoanStatus status;

    @DBRef
    private List<Repayment> repayments;

    @DBRef
    private List<RepaymentSchedule> repaymentSchedule;

    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    @Version
    private Long version;
    private int preferredEmiDay = 1; // 1 = 1st of month (default)

    private BigDecimal disbursedAmount=BigDecimal.ZERO;
    @DBRef
    private List<DisbursementRecord> disbursements;
    private LocalDate holidayUntil;
    @Indexed
    private boolean autoDebit = false;

    @DBRef
    private Account primaryAccount;


    public LoanAccount() {
        //TODO document why this constructor is empty
    }
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
    public String getAccountName() {
        return accountName;
    }
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
    public BigDecimal getLoanAmount() {
        return loanAmount;
    }
    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
    public int getTenureInMonths() {
        return tenureInMonths;
    }
    public void setTenureInMonths(int tenureInMonths) {
        this.tenureInMonths = tenureInMonths;
    }
    public LocalDate getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public LocalDate getDueDate() {
        return dueDate;
    }
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
    public BigDecimal getBalanceRemaining() {
        return balanceRemaining;
    }
    public void setBalanceRemaining(BigDecimal balanceRemaining) {
        this.balanceRemaining = balanceRemaining;
    }
    public BigDecimal getEmiAmount() {
        return emiAmount;
    }
    public void setEmiAmount(BigDecimal emiAmount) {
        this.emiAmount = emiAmount;
    }
    public Customer getBorrower() {
        return borrower;
    }
    public void setBorrower(Customer borrower) {
        this.borrower = borrower;
    }
    public LoanStatus getStatus() {
        return status;
    }
    public void setStatus(LoanStatus status) {
        this.status = status;
    }
    public List<Repayment> getRepayments() {
        return repayments;
    }
    public void setRepayments(List<Repayment> repayments) {
        this.repayments = repayments;
    }
    public List<RepaymentSchedule> getRepaymentSchedule() {
        return repaymentSchedule;
    }
    public void setRepaymentSchedule(List<RepaymentSchedule> repaymentSchedule) {
        this.repaymentSchedule = repaymentSchedule;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
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
    public Long getVersion() {
        return version;
    }
    public void setVersion(Long version) {
        this.version = version;
    }
    

    public LoanType getType() {
        return type;
    }

    public void setType(LoanType type) {
        this.type = type;
    }

    public int getPreferredEmiDay() {
        return preferredEmiDay;
    }

    public void setPreferredEmiDay(int preferredEmiDay) {
        this.preferredEmiDay = preferredEmiDay;
    }



    /**
     * @return BigDecimal return the disbursedAmount
     */
    public BigDecimal getDisbursedAmount() {
        return disbursedAmount;
    }

    /**
     * @param disbursedAmount the disbursedAmount to set
     */
    public void setDisbursedAmount(BigDecimal disbursedAmount) {
        this.disbursedAmount = disbursedAmount;
    }

    /**
     * @return List<DisbursementRecord> return the disbursements
     */
    public List<DisbursementRecord> getDisbursements() {
        return disbursements;
    }

    /**
     * @param disbursements the disbursements to set
     */
    public void setDisbursements(List<DisbursementRecord> disbursements) {
        this.disbursements = disbursements;
    }

    /**
     * @return LocalDate return the holidayUntil
     */
    public LocalDate getHolidayUntil() {
        return holidayUntil;
    }

    /**
     * @param holidayUntil the holidayUntil to set
     */
    public void setHolidayUntil(LocalDate holidayUntil) {
        this.holidayUntil = holidayUntil;
    }


    /**
     * @return boolean return the autoDebit
     */
    public boolean isAutoDebit() {
        return autoDebit;
    }

    /**
     * @param autoDebit the autoDebit to set
     */
    public void setAutoDebit(boolean autoDebit) {
        this.autoDebit = autoDebit;
    }


    /**
     * @return Long return the primaryAccount
     */
    public Account getPrimaryAccount() {
        return primaryAccount;
    }

    /**
     * @param primaryAccount the primaryAccount to set
     */
    public void setPrimaryAccount(Account primaryAccount) {
        this.primaryAccount = primaryAccount;
    }

}


