package com.mycompany.advertising.entity;

import com.mycompany.advertising.model.to.UserTo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by Amir on 6/7/2020.
 */
public class WhoAmI2 {
    public static UserTo getUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username;
        Object principal = auth.getPrincipal();
        UserTo userTo = (UserTo)auth.getPrincipal();
        /*if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        } else {
            username = principal.toString();
        }*/
        return userTo;
    }
}
