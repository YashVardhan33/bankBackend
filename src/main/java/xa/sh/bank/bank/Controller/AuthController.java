package xa.sh.bank.bank.Controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import xa.sh.bank.bank.Entity.Customer;
import xa.sh.bank.bank.Entity.Role;
import xa.sh.bank.bank.Repository.AdminRepo;
import xa.sh.bank.bank.Repository.CustomerRepo;
import xa.sh.bank.bank.dto.LoginRequest;
import xa.sh.bank.bank.dto.MessageResponse;
import xa.sh.bank.bank.dto.SignupRequest;
import xa.sh.bank.bank.redis.RateLimiterService;
import xa.sh.bank.bank.security.jwt.JwtUtils;
import xa.sh.bank.bank.security.services.UserDetailsImpl;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomerRepo customRepo;
    @Autowired
    private AdminRepo adminRepo;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private RateLimiterService rateLimiter;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        //Customer customer = customRepo.findByUsername(loginRequest.getUsername()).orElse(null);
        //Long acctNo = (customer!=null)?customer.getPrimaryAccountNo(): null;
        String userKey = loginRequest.getUsername();
        if (rateLimiter.isAccountLocked(userKey)) {
            return ResponseEntity.status(HttpStatus.LOCKED).body(new MessageResponse("Account locked due to failded atttempts. "));
        }

        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            rateLimiter.unlockAccount(userKey);

            String jwt = jwtUtils.generateJwtToken(authentication);
            String refreshJwt = jwtUtils.generateRefreshToken(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            // String refreshToken = jwtUtils.generateRefreshToken(authentication); // If using refresh tokens.
            return ResponseEntity.ok(new JwtResponse(jwt,
                    refreshJwt,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    roles));
        } catch (org.springframework.security.core.AuthenticationException e) {
            rateLimiter.recordFailedAttempt(userKey);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Invalid Username or password"));
        }

    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerCustomer(@RequestBody SignupRequest signUpRequest) {
        if (customRepo.findByUsername(signUpRequest.getUsername()).isPresent()
                || adminRepo.findByUsername(signUpRequest.getUsername()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        // Create new Customer account (frozen until KYC + admin activation)
        Customer customer = new Customer();
        customer.setUsername(signUpRequest.getUsername());
        customer.setPasswordHash(encoder.encode(signUpRequest.getPassword()));
        customer.setRole(Role.CUSTOMER);
        customer.setName(signUpRequest.getName());
        customer.setPhone(signUpRequest.getPhone());
        customer.setKyc(null);
        customer.setDob(signUpRequest.getDob());
        customer.setKycVerified(false);
        customer.setAccountNumbers(List.of());
        customer.setPrimaryAccountNo(null);
        customer.setEmail(signUpRequest.getEmail());
        // createdAt + lastModifiedAt injected via @CreatedDate/@LastModifiedDate
        customRepo.save(customer);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new MessageResponse("Customer registered successfully!"));
    }

    @PostMapping("/signin/atm-unlock")
    public ResponseEntity<?> unlockViaAtm(@RequestParam String username) {
        rateLimiter.unlockAccount(username);
        return ResponseEntity.ok(
                new MessageResponse("Account unlocked via ATM withdrawal.")
        );
    }
}
