package xa.sh.bank.bank.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xa.sh.bank.bank.Entity.Customer;
import xa.sh.bank.bank.Repository.CustomerRepo;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepo customerRepo;

    public Customer createCustomer(Customer customer) {
        return customerRepo.save(customer);
    }

    public Optional<Customer> getUser(String username) {
        return customerRepo.findByUsername(username);
    }

}
