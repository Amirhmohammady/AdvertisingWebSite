package com.mycompany.advertising.model.to;

import com.mycompany.advertising.components.utils.AViewableException;
import com.mycompany.advertising.entity.Role;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)//AUTO
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(nullable = false, length = 20)
    private String username;
    @Column(nullable = false, unique = true, length = 20)
    private String profilename;
    private String password;
    private String fullname;
    @Column(columnDefinition = "TEXT", length = 1024)
    private String aboutme;
    private String websiteurl;
    private String email;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    @Column(name = "enabled")
    private boolean enabled;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    //@OneToMany(cascade = CascadeType.ALL)
    @ElementCollection(fetch = FetchType.EAGER)
    @JoinColumn()//reqired for OnDelete
    @OnDelete(action = OnDeleteAction.CASCADE)//for CASCADE
    @Cascade(value={org.hibernate.annotations.CascadeType.ALL})//for CASCADE
    private List<Role> roles;
    /*@Column(nullable = true)
    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER)
    private TokenForChangePhoneNumberTo tokenForChangePhoneNumberTo;*/

    public UserTo() {
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = false;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        if (fullname == null) websiteurl = "";
        if (fullname.length() > 50)
            throw new AViewableException("Full Name should less than 50 chars");
        this.fullname = fullname;
    }

    public String getWebsiteurl() {
        return websiteurl;
    }

    public void setWebsiteurl(String websiteurl) {
        if (websiteurl == null) websiteurl = "";
        if (websiteurl.length() > 200)
            throw new AViewableException("Website URL should less than 200 chars");
        this.websiteurl = websiteurl;
    }

    public String getAboutme() {
        return aboutme;
    }

    public void setAboutme(String aboutme) {
        if (aboutme.length() > 1024)
            throw new AViewableException("About me should less than 1024 chars");
        this.aboutme = aboutme;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        Matcher matcher = Pattern.compile("^09[\\d]{9}$").matcher(username);
        if (!matcher.matches()) throw new AViewableException("phone number format should some thing like: 09xxxxxxxxx");
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfilename() {
        return profilename;
    }

    public void setProfilename(String profilename) {
/*        Matcher matcher = Pattern.compile("^[a-zA-Z][\\w]{3,19}$").matcher(profilename);
        if (!matcher.matches())
            throw new AViewableException("User name should start with a..Z and contain a..Z,0..9 and contain 4 .. 20 chars");*/
        if (profilename == null || profilename.length() > 20 || profilename.length() < 4)
            throw new AViewableException("User name should contain 4 .. 20 chars");
        else this.profilename = profilename;
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
        if (password == null || password.length() < 6)
            throw new AViewableException("password should be at least 6 chars");
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
                ", profilename='" + profilename + '\'' +
                ", accountNonExpired=" + accountNonExpired +
                ", accountNonLocked=" + accountNonLocked +
                ", credentialsNonExpired=" + credentialsNonExpired +
                ", enabled=" + enabled +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                '}';
    }
}