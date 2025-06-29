package xa.sh.bank.bank.Repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import xa.sh.bank.bank.Entity.Customer;

@Repository
public interface CustomerRepo extends MongoRepository<Customer, String> {
      Optional<Customer> findByUsername(String username);
}
