package xa.sh.bank.bank.security.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import xa.sh.bank.bank.Entity.Admin;
import xa.sh.bank.bank.Entity.Customer;
import xa.sh.bank.bank.Repository.AdminRepo;
import xa.sh.bank.bank.Repository.CustomerRepo;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private CustomerRepo customRepo;
    @Autowired
    private AdminRepo adminRepo;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        Optional<Admin> adminOp = adminRepo.findByUsername(username);
        if(adminOp.isPresent()){
            return UserDetailsImpl.build(adminOp.get());
        }
        // Optional<Customer> customer = CustomerRepo.findByUsername(username).orElseThrow(()-> new UsernameNotFoundException("User not found with this username"));
        Optional<Customer> customerOp = customRepo.findByUsername(username);
        if (customerOp.isEmpty()) {
            throw new UsernameNotFoundException("user not found with this username");
        }
        return UserDetailsImpl.build(customerOp.get());
    }
}
