package xa.sh.bank.bank.Repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import xa.sh.bank.bank.Entity.Admin;

@Repository
public interface AdminRepo extends MongoRepository<Admin, String> {
    Optional<Admin> findByUsername(String username);
}
