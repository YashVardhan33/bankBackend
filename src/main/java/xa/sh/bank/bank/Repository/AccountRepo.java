package xa.sh.bank.bank.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import xa.sh.bank.bank.Entity.Account;
import xa.sh.bank.bank.Entity.AccountType;
import xa.sh.bank.bank.Entity.Customer;



@Repository
public interface AccountRepo extends MongoRepository<Account, String>{

    Optional<Account> findByAccountNumber(Long accountNumber);
    List<Account> findByCustomer(Customer customer);

    Page<Account> findByType(AccountType type, Pageable pageable);
}
