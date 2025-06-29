package xa.sh.bank.bank.Entity;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="admins")
public class Admin extends User{
    private String name;
    private String email;
    private LocalDateTime createdAt;
    public Admin(String name, String email, LocalDateTime createdAt) {
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
    }
    public Admin(String id, String username, String passwordHash, Role role, String name, String email,
            LocalDateTime createdAt) {
        super(id, username, passwordHash, role);
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
}
