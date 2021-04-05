package com.mycompany.advertising.service;

import com.mycompany.advertising.api.UserService;
import com.mycompany.advertising.entity.UserIsAvailable;
import com.mycompany.advertising.model.dao.UserRepository;
import com.mycompany.advertising.model.to.UserTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Created by Amir on 6/7/2020.
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    UserDetailsService userDetailsService;

    @Override
    public void svaeUser(UserTo userTo) throws UserIsAvailable {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userTo.getUsername());
            throw new UserIsAvailable(userTo.getUsername());
        }catch (UsernameNotFoundException e) {
            userTo.setPassword(passwordEncoder.encode(userTo.getPassword()));
            userRepository.save(userTo);
        }
    }

    /*@Override
    public UserTo getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        UserTo userTo;
        if (principal instanceof UserTo) {
            userTo = (UserTo)auth.getPrincipal();
        } else {
            userTo = null;
        }
        return userTo;
    }*/
}
