package com.mycompany.advertising.model.to;

import com.mycompany.advertising.components.utils.AViewableException;
import com.mycompany.advertising.entity.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Amir on 10/29/2019.
 */

@Entity
public class UserTo implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String phonenumber;
    private String password;

    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;

    @Column(name = "enabled")
    private boolean enabled;
    private String email;
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Role> roles;

    public UserTo() {
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = false;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        Matcher matcher = Pattern.compile("^09[\\d]{9}$").matcher(phonenumber);
        if (!matcher.matches()) throw new AViewableException("phone number format should some thing like: 09xxxxxxxxx");
        this.phonenumber = phonenumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        Matcher matcher = Pattern.compile("^[a-zA-Z][\\w]{3,19}$").matcher(username);
        if (!matcher.matches())
            throw new AViewableException("User name should start with a..Z and contain a..Z,0..9 and contain 4 .. 20 chars");
        else this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getEnabled() {
        return enabled;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password.length() < 6) throw new AViewableException("password should be at least 6 chars");
        this.password = password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void grantAuthority(Role authority) {
        if (roles == null) roles = new ArrayList<>();
        roles.add(authority);
    }

    @Override
    public List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.toString())));
        return authorities;
    }

    @Override
    public String toString() {
        return "UserTo{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", accountNonExpired=" + accountNonExpired +
                ", accountNonLocked=" + accountNonLocked +
                ", credentialsNonExpired=" + credentialsNonExpired +
                ", enabled=" + enabled +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                '}';
    }
}