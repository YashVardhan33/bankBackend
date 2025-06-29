package xa.sh.bank.bank.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import xa.sh.bank.bank.Entity.VerificationLog;

public interface LogRepo extends MongoRepository<VerificationLog, String> {

}
