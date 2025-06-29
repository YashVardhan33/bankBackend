package xa.sh.bank.bank.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import xa.sh.bank.bank.Entity.Repayment;

@Repository
public interface RepaymentRepo extends MongoRepository<Repayment, String> {

}
