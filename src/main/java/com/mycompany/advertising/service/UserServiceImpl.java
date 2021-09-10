package com.mycompany.advertising.service;

import com.mycompany.advertising.api.UserService;
import com.mycompany.advertising.entity.TokenNotFoundtException;
import com.mycompany.advertising.model.dao.UserRepository;
import com.mycompany.advertising.model.dao.VerificationTokenRepository;
import com.mycompany.advertising.model.to.UserTo;
import com.mycompany.advertising.model.to.VerificationToken;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;


/**
 * Created by Amir on 6/7/2020.
 */
@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = Logger.getLogger(UserServiceImpl.class);
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Override
    public void registerNewUserAccount(UserTo userTo) throws UsernameNotFoundException {
        if (isEmailExist(userTo.getEmail())) {
            logger.debug(userTo.toString() + "email is exist");
            throw new UsernameNotFoundException(userTo.getUsername());
        } else {
            userTo.setPassword(passwordEncoder.encode(userTo.getPassword()));
            userTo.setEnabled(false);
            userRepository.save(userTo);
            logger.debug(userTo.toString() + "saved successfully");
        }
    }

    @Override
    public void saveRegisteredUser(UserTo user) {
        userRepository.save(user);
    }

    @Override
    public void createVerificationToken(UserTo user, String token) {

    }

    @Override
    public VerificationToken getVerificationToken(String VerificationToken) {
        return null;
    }

    @Override
    public boolean isEmailExist(String email) {
        if (userRepository.existsByEmail(email)) {
            logger.trace("Email " + email + " is exist");
            return true;
        } else {
            logger.trace("Email " + email + " is not exist");
            return false;
        }
    }

    @Override
    public UserTo getUserByToken(String verificationToken) {
        return null;
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
