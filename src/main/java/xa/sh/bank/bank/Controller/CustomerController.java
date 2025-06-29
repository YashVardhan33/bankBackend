package xa.sh.bank.bank.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import xa.sh.bank.bank.Entity.Customer;
import xa.sh.bank.bank.Entity.User;
import xa.sh.bank.bank.Service.CustomerService;
import xa.sh.bank.bank.Service.UserService;

@RestController
@RequestMapping("/api")
public class CustomerController {

    @Autowired
    private CustomerService cSer;
    @Autowired
    private UserService uSer;

    @PostMapping("/customer")
    public Customer createCustomer(@RequestBody Customer customer) {
        Customer customer1 = cSer.createCustomer(customer);
        return customer1;
    }

    @PostMapping("/user")
    public User createUser(@RequestBody User user) {
        User customer2 = uSer.createUser(user);
        return customer2;
    }

    @GetMapping("/customer")
    public Optional<Customer> getCustomer(@RequestParam String username) {
        Optional<Customer> customer = cSer.getUser(username);
        return customer;
    }

    @GetMapping("/user")
    public Optional<User> getUser(@RequestParam String username) {
        Optional<User> customer = uSer.getUser(username);
        return customer;
    }
}
