package xa.sh.bank.bank.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="customers")
public class Customer extends User {
        private String name;
        @Indexed
        private String phone;
        @Indexed(unique=true)
        private String CIN;
        @DBRef
        private KYCInfo kyc;
        private LocalDate dob;
        private boolean isKycVerified;
        //
        @Indexed(unique=true)
        private String email;
        private boolean isEmailVerified;
        private List<Long> accountNumbers;
        private Long primaryAccountNo;
        @CreatedDate
        private LocalDateTime createdAt;
        @LastModifiedDate
        private LocalDateTime lastModifiedAt;

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getPhone() {
            return phone;
        }
        public void setPhone(String phone) {
            this.phone = phone;
        }
        public String getCIN() {
            return CIN;
        }
        public void setCIN(String cIN) {
            this.CIN = cIN;
        }
        public KYCInfo getKyc() {
            return kyc;
        }
        public void setKyc(KYCInfo kyc) {
            this.kyc = kyc;
        }
        public LocalDate getDob() {
            return dob;
        }
        public void setDob(LocalDate dob) {
            this.dob = dob;
        }
        public boolean isKycVerified() {
            return isKycVerified;
        }
        public void setKycVerified(boolean isKycVerified) {
            this.isKycVerified = isKycVerified;
        }
        public List<Long> getAccountNumbers() {
            return accountNumbers;
        }
        public void setAccountNumbers(List<Long> accountNumbers) {
            this.accountNumbers = accountNumbers;
        }
        public Long getPrimaryAccountNo() {
            return primaryAccountNo;
        }
        public void setPrimaryAccountNo(Long primaryAccountNo) {
            this.primaryAccountNo = primaryAccountNo;
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
        public Customer() {
            super();   
        }
        public Customer(String id, String username, String passwordHash, Role role) {
            super(id, username, passwordHash, role);
        }
        public Customer(String id, String username, String passwordHash, Role role, String name, String phone,
                String cIN, KYCInfo kyc, LocalDate dob, boolean isKycVerified, List<Long> accountNumbers,
                Long primaryAccountNo, LocalDateTime createdAt, LocalDateTime lastModifiedAt) {
            super(id, username, passwordHash, role);
            this.name = name;
            this.phone = phone;
            this.CIN = cIN;
            this.kyc = kyc;
            this.dob = dob;
            this.isKycVerified = isKycVerified;
            this.accountNumbers = accountNumbers;
            this.primaryAccountNo = primaryAccountNo;
            this.createdAt = createdAt;
            this.lastModifiedAt = lastModifiedAt;
        }

    

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isIsEmailVerified() {
        return isEmailVerified;
    }

    public void setIsEmailVerified(boolean isEmailVerified) {
        this.isEmailVerified = isEmailVerified;
    }

        

        

}
