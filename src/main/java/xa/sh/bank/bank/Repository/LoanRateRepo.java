package xa.sh.bank.bank.Repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import xa.sh.bank.bank.Entity.LoanInterestRateConfig;
import xa.sh.bank.bank.Entity.LoanType;


@Repository
public interface LoanRateRepo extends MongoRepository<LoanInterestRateConfig, String>{
    Optional<LoanInterestRateConfig> findByType(LoanType type);
}
