package xa.sh.bank.bank.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="loan_interest_rates")
public class LoanInterestRateConfig {
        @Id
        private String id;
        @Indexed(unique= true)
        private LoanType type;
        public LoanInterestRateConfig() {
        }
        public String getId() {
            return id;
        }
        public void setId(String id) {
            this.id = id;
        }
        public LoanType getType() {
            return type;
        }
        public void setType(LoanType type) {
            this.type = type;
        }
        public BigDecimal getAnnualRate() {
            return annualRate;
        }
        public void setAnnualRate(BigDecimal annualRate) {
            this.annualRate = annualRate;
        }
        public LocalDateTime getModifiedDate() {
            return modifiedDate;
        }
        public void setModifiedDate(LocalDateTime modifiedDate) {
            this.modifiedDate = modifiedDate;
        }
        private BigDecimal annualRate;
        @LastModifiedDate
        private LocalDateTime modifiedDate;
}
