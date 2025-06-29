package xa.sh.bank.bank.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xa.sh.bank.bank.Entity.Customer;
import xa.sh.bank.bank.Entity.User;
import xa.sh.bank.bank.Repository.UserRepo;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    public User createUser(User user){
        return userRepo.save(user);
    }

    public User createUser (Customer customer){
        User user = new User();
        user.setUsername(customer.getUsername());
        user.setPasswordHash(customer.getPasswordHash());
        user.setRole(customer.getRole());
        return  userRepo.save(user);
    }
    public Optional<User> getUser(String username){
        return userRepo.findByUsername(username);
    }
}
