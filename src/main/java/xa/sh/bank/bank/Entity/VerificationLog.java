package xa.sh.bank.bank.Entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "verification_logs")
public class VerificationLog {
    @Id
    private String id;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    
    public VerificationLog() {
    }

    private String username;
    private String type; // "EMAIL_REQUEST", "KYC_VERIFIED", etc.
    public String getTriggeredBy() {
        return triggeredBy;
    }
    public void setTriggeredBy(String triggeredBy) {
        this.triggeredBy = triggeredBy;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    private String triggeredBy;
    private LocalDateTime timestamp;
}
