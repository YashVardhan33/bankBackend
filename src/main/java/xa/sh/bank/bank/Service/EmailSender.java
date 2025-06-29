package xa.sh.bank.bank.Service;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
public interface EmailSender {
    void send (String to, String subject, String htmlBody);
}
