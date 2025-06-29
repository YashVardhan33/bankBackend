package xa.sh.bank.bank.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import xa.sh.bank.bank.Entity.Customer;
import xa.sh.bank.bank.Entity.VerificationLog;
import xa.sh.bank.bank.Repository.CustomerRepo;
import xa.sh.bank.bank.Repository.LogRepo;

@Service
public class KycService {

    private static final Duration TOKEN_TTL = Duration.ofMinutes(10);

    @Autowired
    private CustomerRepo customRepo;
    @Autowired
    private StringRedisTemplate redis;
    @Autowired
    private EmailSender emailSender;
    @Autowired
    private LogRepo logRepo;

    private void log(String username, String type, String triggeredBy) {
        VerificationLog log = new VerificationLog();
        log.setUsername(username);
        log.setType(type);
        log.setTriggeredBy(triggeredBy);
        log.setTimestamp(LocalDateTime.now());
        logRepo.save(log);

    }

    public void issueEmailToken(String username) {
        Customer c = customRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("Customer not found"));

        String email = c.getEmail();
        String token = UUID.randomUUID().toString();
        // c.setKycVerificationToken(token);
        // c.setKycTokenExpiry(LocalDateTime.now().plus(TOKEN_TTL));
        // customRepo.save(c);
        redis.opsForValue().set("email:" + username, token, TOKEN_TTL);
        String link = String.format("http://localhost:8080/api/kyc/verify-email?username=%s&token=%s", username, token);
        String html = "<p>Please verify your email by <a href=\"" + link + "\">clicking here</a>.<br>" + "This link expires in 10 minutes.</p>";
        emailSender.send(email, "Verify Your email", html);
        log(username, "Email_request", username);
    }

    public void verifyEmailToken(String username, String token) {
        Customer c = customRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("No customer found with username:" + username));

        String storedToken = redis.opsForValue().get("email:" + username);

        if (!token.equals(storedToken)) {
            throw new IllegalArgumentException("Invalid or expired token");

        }

        c.setIsEmailVerified(true);
        customRepo.save(c);
        redis.delete("email: " + username);
        log(username, "Email_Verified", username);
    }

    public void issueKycToken(String username) {

        Customer c = customRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("NO customer found with username: " + username));
        String email = c.getEmail();
        String token = UUID.randomUUID().toString();
        redis.opsForValue().set("kyc: " + username, token, TOKEN_TTL);
        // c.setKycVerificationToken(token);
        // c.setKycTokenExpiry(LocalDateTime.now().plus(TOKEN_TTL));
        // customRepo.save(c);

        String html = "<p>Your KYC verification code is: <b>" + token + "</b><br>"
                + "Expires in 30 minutes.</p>";
        emailSender.send(email, "Complete Your KYC", html);
        log(username, "KYC_REQUEST", username);

    }

    public void verifyKycToken(String username, String token) {
        Customer c = customRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("username not found "));

        String storedToken = redis.opsForValue().get("kyc: " + username);
        if (!token.equals(storedToken)) {
            throw new IllegalArgumentException("Invalid or expired Kyc Token");

        }

        c.setKycVerified(true);
        customRepo.save(c);
        redis.delete("kyc: " + username);
        log(username, "KYC_VERIFIED", username);
    }

}
