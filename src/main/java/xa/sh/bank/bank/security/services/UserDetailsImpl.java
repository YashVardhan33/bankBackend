package xa.sh.bank.bank.security.services;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import xa.sh.bank.bank.Entity.Customer;
import xa.sh.bank.bank.Entity.User;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private final String id;
    private final String username;

    @JsonIgnore
    private final String password;

    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean enabled;
    public UserDetailsImpl(String id, String username, String password,
                           Collection<? extends GrantedAuthority> authorities, boolean enabled) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.enabled = enabled;
    }

    public static UserDetailsImpl build(User user) {
        String roleName = "ROLE_"+user.getRole().name();
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(roleName));

        boolean enabled = true;
        if (user instanceof xa.sh.bank.bank.Entity.Customer) {
            enabled = ((Customer) user).isKycVerified(); 
        }
        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getPasswordHash(),
                authorities, enabled);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getId() {
        return id;
    }

    @Override
    public String getUsername(){
        return username;
    }


    @Override
    public String getPassword() {
        return password;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode(){return Objects.hash(id);}
}
