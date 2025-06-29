package xa.sh.bank.bank.Repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import xa.sh.bank.bank.Entity.AccountType;
import xa.sh.bank.bank.Entity.DepositInterestRateConfig;


@Repository
public interface DepositRateRepo extends MongoRepository<DepositInterestRateConfig, String>{
        Optional<DepositInterestRateConfig> findByType(AccountType type);
}
