package xa.sh.bank.bank.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import xa.sh.bank.bank.Entity.LoanAccount;
import xa.sh.bank.bank.Entity.LoanStatus;


@Repository
public interface LoanAccountRepo extends MongoRepository<LoanAccount, String>{
    LoanAccount findByAccountName(String accountName);
    Optional<LoanAccount> findByAccountNumber(Long accountNumber);
    List<LoanAccount> findByPreferredEmiDayAndStatus(LocalDate day, LoanStatus status);
}
