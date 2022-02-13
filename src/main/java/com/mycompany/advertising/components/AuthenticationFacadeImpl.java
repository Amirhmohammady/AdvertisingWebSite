package com.mycompany.advertising.components;

import com.mycompany.advertising.components.api.AuthenticationFacade;
import com.mycompany.advertising.model.to.UserTo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Created by Amir on 6/24/2020.
 */
@Component
public class AuthenticationFacadeImpl implements AuthenticationFacade {

    @Override
    public UserTo getUserToDetails() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        if (principal instanceof UserTo) {
            return (UserTo)principal;
        } else {
            return null;
        }
    }

    @Override
    public boolean hasRole(String role) {
        System.out.println("checking for Authentication----------------------");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        for(GrantedAuthority authoritie:authorities){
            System.out.println(authoritie.toString());
            if (role.equals(authoritie.getAuthority())){
                System.out.println("returning true in hasRole");
                return true;
            }
        }
        System.out.println("returning false in hasRole");
        return false;
    }
    /*@Override
    public UserTo getUserToDetails2() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        GrantedAuthority grantedAuthority = authorities.iterator().next();
        if (grantedAuthority.getAuthority().equals("ROLE_ANONYMOUS")) return null;
        else {
            try {
                String username;
                Object principal = auth.getPrincipal();
                if (principal instanceof UserDetails) {
                    username = ((UserDetails) principal).getUsername();
                } else {
                    username = principal.toString();
                }
                return userToDetailsService.loadUserToByUsername(username);
            } catch (UsernameNotFoundException e) {
                return null;
            }
        }
    }*/
}
