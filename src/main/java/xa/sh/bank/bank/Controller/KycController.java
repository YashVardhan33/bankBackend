package xa.sh.bank.bank.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import xa.sh.bank.bank.Service.KycService;
import xa.sh.bank.bank.dto.MessageResponse;

@RestController
@RequestMapping("/api/kyc")
public class KycController {

    @Autowired private KycService kycService;

    /** a) Request email verification */
    @PostMapping("/request-email")
    public ResponseEntity<?> requestEmail(@RequestParam String username) {
        kycService.issueEmailToken(username);
        return ResponseEntity.ok(new MessageResponse("Verification email sent."));
    }

    /** b) Verify email */
    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(
            @RequestParam String username,
            @RequestParam String token
    ) {
        kycService.verifyEmailToken(username, token);
        return ResponseEntity.ok(new MessageResponse("Email verified."));
    }

    /** c) Request KYC (documents) token */
    @PostMapping("/request-kyc")
    public ResponseEntity<?> requestKyc(@RequestParam String username) {
        kycService.issueKycToken(username);
        return ResponseEntity.ok(new MessageResponse("KYC code sent."));
    }

    /** d) Verify KYC code */
    @PostMapping("/verify-kyc")
    public ResponseEntity<?> verifyKyc(
            @RequestParam String username,
            @RequestParam String token
    ) {
        kycService.verifyKycToken(username, token);
        return ResponseEntity.ok(new MessageResponse("KYC completed."));
    }
}
