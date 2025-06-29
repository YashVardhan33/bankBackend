package xa.sh.bank.bank.Controller;

import java.util.List;

public class JwtResponse {

    private String jwt;
    private String refreshToken;
    private String userId;
    private String username;
    private List<String> roles;

    public String getJwt() {
        return jwt;
    }

    public JwtResponse(String jwt, String refreshToken, String userId, String username, List<String> roles) {
        this.jwt = jwt;
        this.refreshToken = refreshToken;
        this.userId = userId;
        this.username = username;
        this.roles = roles;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

}
